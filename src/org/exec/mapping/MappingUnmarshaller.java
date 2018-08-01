package org.exec.mapping;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exec.core.CoreProperties;
import org.exec.core.util.Messages;
import org.exec.xml.AbstractInternalContext;
import org.exec.xml.InternalContext;
import org.exolab.mapping.Mapping;
import org.exolab.mapping.MappingException;
import org.exolab.mapping.MappingLoader;
import org.exolab.mapping.loader.AbstractMappingLoader;
import org.exolab.mapping.xml.ClassMapping;
import org.exolab.mapping.xml.Include;
import org.exolab.mapping.xml.KeyGeneratorDef;
import org.exolab.mapping.xml.MappingRoot;
import org.exolab.util.DTDResolver;
import org.exolab.xml.ClassDescriptorResolverFactory;
import org.exolab.xml.Introspector;
import org.exolab.xml.Unmarshaller;
import org.exolab.xml.XMLClassDescriptorResolver;
import org.exolab.xml.util.ResolverStrategy;
import org.exolab.xml.util.resolvers.ExecXMLStrategy;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class MappingUnmarshaller {
  private static final Log LOG = LogFactory.getLog(MappingUnmarshaller.class);
  private final MappingLoaderRegistry _registry = new MappingLoaderRegistry(new CoreProperties());
  private final MappingUnmarshallIDResolver _idResolver = new MappingUnmarshallIDResolver();
  private boolean _allowRedefinitions = false;
  private InternalContext _internalContext;

  public MappingUnmarshaller() {
    AbstractInternalContext internalContext = new AbstractInternalContext() {
    };
    internalContext.setClassLoader(this.getClass().getClassLoader());
    XMLClassDescriptorResolver cdr = (XMLClassDescriptorResolver)ClassDescriptorResolverFactory.createClassDescriptorResolver(BindingType.XML);
    cdr.setInternalContext(internalContext);
    internalContext.setXMLClassDescriptorResolver(cdr);
    Introspector introspector = new Introspector();
    introspector.setInternalContext(internalContext);
    internalContext.setIntrospector(introspector);
    cdr.setIntrospector(introspector);
    ResolverStrategy resolverStrategy = new ExecXMLStrategy();
    internalContext.setResolverStrategy(resolverStrategy);
    cdr.setResolverStrategy(resolverStrategy);
    this._internalContext = internalContext;
  }

  public void setAllowRedefinitions(boolean allow) {
    this._allowRedefinitions = allow;
  }

  public MappingLoader getMappingLoader(Mapping mapping, BindingType bindingType) throws MappingException {
    return this.getMappingLoader(mapping, bindingType, (Object)null);
  }

  public MappingLoader getMappingLoader(Mapping mapping, BindingType bindingType, Object param) throws MappingException {
    synchronized(this) {
      Iterator iter = mapping.getMappingSources().iterator();

      while(iter.hasNext()) {
        MappingSource source = (MappingSource)iter.next();
        this.loadMappingInternal(mapping, source.getResolver(), source.getSource());
      }

      AbstractMappingLoader loader = (AbstractMappingLoader)this._registry.getMappingLoader("CastorXmlMapping", bindingType);
      loader.setClassLoader(mapping.getClassLoader());
      loader.setAllowRedefinitions(this._allowRedefinitions);
      loader.setInternalContext(this._internalContext);
      loader.loadMapping(mapping.getRoot(), param);
      return loader;
    }
  }

  public void loadMappingOnly(Mapping mapping) throws MappingException {
    synchronized(this) {
      Iterator iter = mapping.getMappingSources().iterator();

      while(iter.hasNext()) {
        MappingSource source = (MappingSource)iter.next();
        this.loadMappingInternal(mapping, source.getResolver(), source.getSource());
      }

    }
  }

  protected void loadMappingInternal(Mapping mapping, DTDResolver resolver, String url) throws IOException, MappingException {
    try {
      InputSource source = resolver.resolveEntity((String)null, url);
      if (source == null) {
        source = new InputSource(url);
      }

      if (source.getSystemId() == null) {
        source.setSystemId(url);
      }

      LOG.info(Messages.format("mapping.loadingFrom", url));
      this.loadMappingInternal(mapping, resolver, source);
    } catch (SAXException var5) {
      throw new MappingException(var5);
    }
  }

  private void loadMappingInternal(Mapping mapping, DTDResolver resolver, InputSource source) throws MappingException {
    this._registry.clear();
    Object id = source.getSystemId();
    if (id == null) {
      id = source.getByteStream();
    }

    if (id != null) {
      if (mapping.processed(id)) {
        return;
      }

      mapping.markAsProcessed(id);
    }

    MappingRoot root = mapping.getRoot();
    this._idResolver.setMapping(root);

    try {
      Unmarshaller unm = new Unmarshaller(MappingRoot.class);
      unm.setValidation(false);
      unm.setEntityResolver(resolver);
      unm.setClassLoader(Mapping.class.getClassLoader());
      unm.setIDResolver(this._idResolver);
      unm.setUnmarshalListener(new MappingUnmarshallListener(this, mapping, resolver));
      MappingRoot loaded = (MappingRoot)unm.unmarshal(source);
      Enumeration includes = loaded.enumerateInclude();

      while(includes.hasMoreElements()) {
        Include include = (Include)includes.nextElement();
        if (!mapping.processed(include.getHref())) {
          try {
            this.loadMappingInternal(mapping, resolver, include.getHref());
          } catch (Exception var11) {
            throw new MappingException(var11);
          }
        }
      }

      Enumeration enumeration = loaded.enumerateClassMapping();

      while(enumeration.hasMoreElements()) {
        root.addClassMapping((ClassMapping)enumeration.nextElement());
      }

      enumeration = loaded.enumerateKeyGeneratorDef();

      while(enumeration.hasMoreElements()) {
        root.addKeyGeneratorDef((KeyGeneratorDef)enumeration.nextElement());
      }

      root.setFieldHandlerDef(loaded.getFieldHandlerDef());
    } catch (Exception var12) {
      throw new MappingException(var12);
    }
  }
}
