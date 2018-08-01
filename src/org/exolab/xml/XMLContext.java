package org.exolab.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exec.mapping.BindingType;
import org.exec.mapping.MappingUnmarshaller;
import org.exec.xml.AbstractInternalContext;
import org.exec.xml.InternalContext;
import org.exolab.mapping.Mapping;
import org.exolab.mapping.MappingException;
import org.exolab.mapping.MappingLoader;
import org.exolab.tools.MappingTool;
import org.exolab.util.ChangeLog2XML;
import org.exolab.xml.util.ResolverStrategy;
import org.exolab.xml.util.resolvers.ExecXMLStrategy;

public class XMLContext {
  private static final Log LOG = LogFactory.getFactory().getInstance(XMLContext.class);
  private InternalContext _internalContext;
  public XMLContext() {
    AbstractInternalContext internalContext = new AbstractInternalContext() {};
    internalContext.setClassLoader(getClass().getClassLoader());
    XMLClassDescriptorResolver cdr = (XMLClassDescriptorResolver)ClassDescriptorResolverFactory.createClassDescriptorResolver(BindingType.XML);
    internalContext.setXMLClassDescriptorResolver(cdr);
    cdr.setInternalContext(internalContext);
    Introspector introspector = new Introspector();
    introspector.setInternalContext(internalContext);
    internalContext.setIntrospector(introspector);
    cdr.setIntrospector(introspector);
    ResolverStrategy resolverStrategy = new ExecXMLStrategy();
    internalContext.setResolverStrategy(resolverStrategy);
    cdr.setResolverStrategy(resolverStrategy);
    this._internalContext = internalContext;
  }
  public void addMapping(Mapping mapping) throws MappingException {
    MappingUnmarshaller mappingUnmarshaller = new MappingUnmarshaller();
    MappingLoader mappingLoader = mappingUnmarshaller.getMappingLoader(mapping, BindingType.XML);
    this._internalContext.getXMLClassDescriptorResolver().setMappingLoader(mappingLoader);
  }
  public void addClass(Class clazz)
    throws ResolverException {
    this._internalContext.getXMLClassDescriptorResolver().addClass(clazz);
  }
  public void addClasses(Class[] clazzes) throws ResolverException {
    this._internalContext.getXMLClassDescriptorResolver().addClasses(clazzes);
  }
  public void addPackage(String packageName) throws ResolverException {
    this._internalContext.getXMLClassDescriptorResolver().addPackage(packageName);
  }
  public void addPackages(String[] packageNames) throws ResolverException {
    this._internalContext.getXMLClassDescriptorResolver().addPackages(packageNames);
  }
  public Mapping createMapping() {
    Mapping mapping = new Mapping();
    return mapping;
  }
  public Marshaller createMarshaller() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Creating new Marshaller instance.");
    }
    Marshaller marshaller = new Marshaller(this._internalContext);
    return marshaller;
  }
  public Unmarshaller createUnmarshaller() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Creating new Unmarshaller instance.");
    }
    Unmarshaller unmarshaller = new Unmarshaller(this._internalContext);
    return unmarshaller;
  }
  public MappingTool createMappingTool() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Creating new MappingTool instance.");
    }
    MappingTool mt = new MappingTool();
    mt.setInternalContext(this._internalContext);
    return mt;
  }
  public ChangeLog2XML createChangeLog2XML() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Creating new ChangeLog2XML instance.");
    }
    ChangeLog2XML changeLog2XML = new ChangeLog2XML();
    changeLog2XML.setInternalContext(this._internalContext);
    return changeLog2XML;
  }
  public void setProperty(String propertyName, Object value)
  {
    this._internalContext.setProperty(propertyName, value);
  }
  public void setProperty(String propertyName, boolean value) {
    this._internalContext.setProperty(propertyName, value);
  }
  public Object getProperty(String propertyName)
  {
    return this._internalContext.getProperty(propertyName);
  }
  public InternalContext getInternalContext()
  {
    return this._internalContext;
  }
}
