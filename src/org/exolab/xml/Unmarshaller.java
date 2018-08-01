package org.exolab.xml;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exec.mapping.BindingType;
import org.exec.mapping.MappingUnmarshallListener;
import org.exec.mapping.MappingUnmarshaller;
import org.exec.xml.BackwardCompatibilityContext;
import org.exec.xml.InternalContext;
import org.exec.xml.UnmarshalListenerAdapter;
import org.exolab.mapping.Mapping;
import org.exolab.mapping.MappingException;
import org.exolab.mapping.MappingLoader;
import org.exolab.types.AnyNode;
import org.exolab.util.ObjectFactory;
import org.exolab.xml.location.FileLocation;
import org.exolab.xml.util.AnyNode2SAX2;
import org.exolab.xml.util.DOMEventProducer;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class Unmarshaller {
  private static final Log LOG = LogFactory.getLog(Unmarshaller.class);
  private Class _class;
  private boolean _clearCollections;
  private IDResolver _idResolver;
  private boolean _ignoreExtraAtts;
  private boolean _ignoreExtraElements;
  private Object _instanceObj;
  EntityResolver entityResolver;
  private ClassLoader _loader;
  private boolean _reuseObjects;
  private UnmarshalListener _unmarshalListener;
  private boolean _validate;
  private boolean _wsPreserve;
  private HashMap _namespaceToPackage;
  private ObjectFactory _objectFactory;
  private InternalContext _internalContext;

  public Unmarshaller() {
    this((Class)null);
  }

  public Unmarshaller(Class clazz) {
    this(new BackwardCompatibilityContext(), (Class)clazz);
  }

  public Unmarshaller(InternalContext internalContext) {
    this(internalContext, (Class)null, (ClassLoader)null);
  }

  public Unmarshaller(InternalContext internalContext, Class c) {
    this(internalContext, c, (ClassLoader)null);
  }

  public Unmarshaller(InternalContext internalContext, Class c, ClassLoader loader) {
    this._class = null;
    this._clearCollections = false;
    this._idResolver = null;
    this._ignoreExtraAtts = true;
    this._ignoreExtraElements = false;
    this._instanceObj = null;
    this.entityResolver = null;
    this._loader = null;
    this._reuseObjects = false;
    this._unmarshalListener = null;
    this._validate = false;
    this._wsPreserve = false;
    this._namespaceToPackage = null;
    if (internalContext == null) {
      String message = "InternalContext must not be null";
      LOG.warn(message);
      throw new IllegalArgumentException(message);
    } else {
      this.setInternalContext(internalContext);
      this.setClass(c);
      this._loader = loader;
      if (loader == null && c != null) {
        this._loader = c.getClassLoader();
      }

      this._internalContext.setClassLoader(this._loader);
    }
  }

  public Unmarshaller(Mapping mapping) throws MappingException {
    this(new BackwardCompatibilityContext(), (Mapping)mapping);
  }

  public Unmarshaller(InternalContext internalContext, Mapping mapping) throws MappingException {
    this(internalContext, (Class)null, (ClassLoader)null);
    if (mapping != null) {
      this.setMapping(mapping);
      this._loader = mapping.getClassLoader();
    }

  }

  public Unmarshaller(Object root) {
    this(new BackwardCompatibilityContext(), (Object)root);
  }

  public Unmarshaller(InternalContext internalContext, Object root) {
    this(internalContext, (Class)null, (ClassLoader)null);
    if (root != null) {
      Class clazz = root.getClass();
      this.setClass(clazz);
      this._loader = clazz.getClassLoader();
    }

    this._instanceObj = root;
  }

  public void addNamespaceToPackageMapping(String nsURI, String packageName) {
    if (this._namespaceToPackage == null) {
      this._namespaceToPackage = new HashMap();
    }

    String iNsUri = nsURI == null ? "" : nsURI;
    String iPackageName = packageName == null ? "" : packageName;
    this._namespaceToPackage.put(iNsUri, iPackageName);
  }

  public UnmarshalHandler createHandler() {
    UnmarshalHandler handler = new UnmarshalHandler(this._internalContext, this._class);
    handler.setClearCollections(this._clearCollections);
    handler.setReuseObjects(this._reuseObjects);
    handler.setValidation(this._validate);
    handler.setIgnoreExtraAttributes(this._ignoreExtraAtts);
    handler.setIgnoreExtraElements(this._ignoreExtraElements);
    handler.setInternalContext(this._internalContext);
    handler.setWhitespacePreserve(this._wsPreserve);
    if (this._objectFactory != null) {
      handler.setObjectFactory(this._objectFactory);
    }

    if (this._namespaceToPackage != null) {
      Iterator keys = this._namespaceToPackage.keySet().iterator();

      while(keys.hasNext()) {
        String nsURI = (String)keys.next();
        String pkgName = (String)this._namespaceToPackage.get(nsURI);
        handler.addNamespaceToPackageMapping(nsURI, pkgName);
      }
    }

    if (this._instanceObj != null) {
      handler.setRootObject(this._instanceObj);
    }

    if (this._idResolver != null) {
      handler.setIDResolver(this._idResolver);
    }

    if (this._loader != null) {
      handler.setClassLoader(this._loader);
    }

    return handler;
  }

  public boolean isValidating() {
    return this._validate;
  }

  public void setClass(Class clazz) {
    this._class = clazz;
  }

  public void setObject(Object root) {
    this._instanceObj = root;
  }

  public void setClassLoader(ClassLoader loader) {
    this._loader = loader;
  }

  public void setClearCollections(boolean clear) {
    this._clearCollections = clear;
  }


  public void setDebug(boolean debug) {
  }

  public void setEntityResolver(EntityResolver entityResolver) {
    this.entityResolver = entityResolver;
  }

  public void setIDResolver(IDResolver idResolver) {
    this._idResolver = idResolver;
  }

  public void setIgnoreExtraAttributes(boolean ignoreExtraAtts) {
    this._ignoreExtraAtts = ignoreExtraAtts;
  }

  public void setIgnoreExtraElements(boolean ignoreExtraElements) {
    this._ignoreExtraElements = ignoreExtraElements;
  }


  public void setLogWriter(PrintWriter printWriter) {
  }

  public void setMapping(Mapping mapping) throws MappingException {
    if (this._loader == null) {
      this._loader = mapping.getClassLoader();
    }

    MappingUnmarshaller mum = new MappingUnmarshaller();
    MappingLoader resolver = mum.getMappingLoader(mapping, BindingType.XML);
    this._internalContext.getXMLClassDescriptorResolver().setMappingLoader(resolver);
  }

  public void setReuseObjects(boolean reuse) {
    this._reuseObjects = reuse;
  }


  public void setUnmarshalListener(MappingUnmarshallListener listener) {
    if (listener == null) {
      this._unmarshalListener = null;
    } else {
      UnmarshalListenerAdapter adapter = new UnmarshalListenerAdapter();
      adapter.setOldListener( listener );

    }
  }

  public void setUnmarshalListener(UnmarshalListener listener) {

    this._unmarshalListener = listener;
  }

  public void setValidation(boolean validate) {
    this._validate = validate;
  }

  public void setWhitespacePreserve(boolean preserve) {
    this._wsPreserve = preserve;
  }

  public Object unmarshal(Reader reader) throws MarshalException, ValidationException {
    return this.unmarshal(new InputSource(reader));
  }


  public Object unmarshal(EventProducer eventProducer) throws MarshalException, ValidationException {
    UnmarshalHandler handler = this.createHandler();
    eventProducer.setDocumentHandler(handler);

    try {
      eventProducer.start();
    } catch (SAXException var4) {
      this.convertSAXExceptionToMarshalException(handler, var4);
    }

    return handler.getObject();
  }

  public Object unmarshal(SAX2EventProducer eventProducer) throws MarshalException, ValidationException {
    UnmarshalHandler handler = this.createHandler();
    eventProducer.setContentHandler(handler);

    try {
      eventProducer.start();
    } catch (SAXException var4) {
      this.convertSAXExceptionToMarshalException(handler, var4);
    }

    return handler.getObject();
  }

  public Object unmarshal(AnyNode anyNode) throws MarshalException {
    UnmarshalHandler handler = this.createHandler();

    try {
      AnyNode2SAX2.fireEvents(anyNode, handler);
    } catch (SAXException var4) {
      this.convertSAXExceptionToMarshalException(handler, var4);
    }

    return handler.getObject();
  }

  public Object unmarshal(InputSource source) throws MarshalException, ValidationException {
    XMLReader reader = null;
    Parser parser = null;

    try {
      reader = this._internalContext.getXMLReader();
      if (this.entityResolver != null) {
        reader.setEntityResolver(this.entityResolver);
      }
    } catch (RuntimeException var8) {
      LOG.debug("Unable to create SAX XMLReader, attempting SAX Parser.");
    }

    if (reader == null) {
      parser = this._internalContext.getParser();
      if (parser == null) {
        throw new MarshalException("Unable to create SAX Parser.");
      }

      if (this.entityResolver != null) {
        parser.setEntityResolver(this.entityResolver);
      }
    }

    UnmarshalHandler handler = this.createHandler();

    try {
      if (reader != null) {
        reader.setContentHandler(handler);
        reader.setErrorHandler(handler);
        reader.parse(source);
      } else {
        parser.setDocumentHandler(handler);
        parser.setErrorHandler(handler);
        parser.parse(source);
      }
    } catch (IOException var6) {
      throw new MarshalException(var6);
    } catch (SAXException var7) {
      this.convertSAXExceptionToMarshalException(handler, var7);
    }

    return handler.getObject();
  }

  public Object unmarshal(Node node) throws MarshalException, ValidationException {
    return this.unmarshal((EventProducer)(new DOMEventProducer(node)));
  }

  private void convertSAXExceptionToMarshalException(UnmarshalHandler handler, SAXException sex) throws MarshalException {
    Exception except = sex.getException();
    if (except == null) {
      except = sex;
    }

    MarshalException marshalEx = new MarshalException((Throwable)except);
    if (handler.getDocumentLocator() != null) {
      FileLocation location = new FileLocation();
      location.setFilename(handler.getDocumentLocator().getSystemId());
      location.setLineNumber(handler.getDocumentLocator().getLineNumber());
      location.setColumnNumber(handler.getDocumentLocator().getColumnNumber());
      marshalEx.setLocation(location);
    }

    throw marshalEx;
  }

  public static ContentHandler getContentHandler(UnmarshalHandler handler) throws SAXException {
    return handler;
  }

  public static Object unmarshal(Class c, Reader reader) throws MarshalException, ValidationException {
    Unmarshaller unmarshaller = createUnmarshaller(c);
    return unmarshaller.unmarshal(reader);
  }

  private static Unmarshaller createUnmarshaller(Class clazz) {
    XMLContext xmlContext = new XMLContext();
    Unmarshaller unmarshaller = xmlContext.createUnmarshaller();
    unmarshaller.setClass(clazz);
    if (LOG.isDebugEnabled()) {
      LOG.debug("*static* unmarshal method called, this will ignore any mapping files or changes made to an Unmarshaller instance.");
    }

    unmarshaller.setWhitespacePreserve(true);
    return unmarshaller;
  }

  public static Object unmarshal(Class c, InputSource source) throws MarshalException, ValidationException {
    Unmarshaller unmarshaller = createUnmarshaller(c);
    return unmarshaller.unmarshal(source);
  }

  public static Object unmarshal(Class c, Node node) throws MarshalException, ValidationException {
    Unmarshaller unmarshaller = createUnmarshaller(c);
    return unmarshaller.unmarshal(node);
  }

  public void setObjectFactory(ObjectFactory objectFactory) {
    this._objectFactory = objectFactory;
  }

  public String getProperty(String name) {
    Object propertyValue = this._internalContext.getProperty(name);
    if (propertyValue != null && !(propertyValue instanceof String)) {
      String message = "Requested property: " + name + " is not of type String, but: " + propertyValue.getClass() + " throwing IllegalStateException.";
      LOG.warn(message);
      throw new IllegalStateException(message);
    } else {
      return (String)propertyValue;
    }
  }

  public void setProperty(String name, String value) {
    this._internalContext.setProperty(name, value);
  }

  public void setInternalContext(InternalContext internalContext) {
    this._internalContext = internalContext;
    this.deriveProperties();
  }

  private void deriveProperties() {
    this._validate = this._internalContext.marshallingValidation();
    this._ignoreExtraElements = !this._internalContext.strictElements();
    String mappings = this._internalContext.getStringProperty("org.exolab.castor.xml.nspackages");
    if (mappings != null && mappings.length() > 0) {
      StringTokenizer tokens = new StringTokenizer(mappings, ",");

      while(tokens.hasMoreTokens()) {
        String token = tokens.nextToken();
        int sepIdx = token.indexOf(61);
        if (sepIdx >= 0) {
          String ns = token.substring(0, sepIdx).trim();
          String javaPackage = token.substring(sepIdx + 1).trim();
          this.addNamespaceToPackageMapping(ns, javaPackage);
        }
      }
    }

  }

  public InternalContext getInternalContext() {
    return this._internalContext;
  }

  public void setResolver(XMLClassDescriptorResolver xmlClassDescriptorResolver) {
    this._internalContext.setResolver(xmlClassDescriptorResolver);
  }
}
