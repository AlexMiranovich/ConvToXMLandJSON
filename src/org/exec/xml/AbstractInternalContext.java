package org.exec.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import javax.xml.parsers.SAXParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exec.core.util.AbstractProperties;
import org.exec.core.util.Messages;
import org.exec.mapping.BindingType;
import org.exec.mapping.MappingUnmarshaller;
import org.exolab.mapping.Mapping;
import org.exolab.mapping.MappingException;
import org.exolab.mapping.MappingLoader;
import org.exolab.util.RegExpEvaluator;
import org.exolab.xml.Introspector;
import org.exolab.xml.NodeType;
import org.exolab.xml.OutputFormat;
import org.exolab.xml.ResolverException;
import org.exolab.xml.Serializer;
import org.exolab.xml.XMLClassDescriptorResolver;
import org.exolab.xml.XMLSerializerFactory;
import org.exolab.xml.util.DefaultNaming;
import org.exolab.xml.util.ResolverStrategy;
import org.exolab.xml.util.XMLParserUtils;
import org.xml.sax.DocumentHandler;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public abstract class AbstractInternalContext implements InternalContext {
  private static final Log LOG = LogFactory.getFactory().getInstance(AbstractInternalContext.class);
  private AbstractProperties _properties = XMLProperties.newInstance();
  private XMLClassDescriptorResolver _xmlClassDescriptorResolver;
  private Introspector _introspector;
  private ResolverStrategy _resolverStrategy;
  private MappingLoader _mappingLoader;
  private XMLNaming _xmlNaming;
  private JavaNaming _javaNaming = new JavaNamingImpl();
  private ClassLoader _classLoader;
  private NodeType _primitiveNodeType;
  private RegExpEvaluator _regExpEvaluator;

  public AbstractInternalContext() {
  }

  public void addMapping(Mapping mapping) throws MappingException {
    MappingUnmarshaller mappingUnmarshaller = new MappingUnmarshaller();
    MappingLoader mappingLoader = mappingUnmarshaller.getMappingLoader(mapping, BindingType.XML);
    this._xmlClassDescriptorResolver.setMappingLoader(mappingLoader);
  }

  public void addClass(Class clazz) throws ResolverException {
    this._xmlClassDescriptorResolver.addClass(clazz);
  }

  public void addClasses(Class[] clazzes) throws ResolverException {
    this._xmlClassDescriptorResolver.addClasses(clazzes);
  }

  public void addPackage(String packageName) throws ResolverException {
    this._xmlClassDescriptorResolver.addPackage(packageName);
  }

  public void addPackages(String[] packageNames) throws ResolverException {
    this._xmlClassDescriptorResolver.addPackages(packageNames);
  }

  public void setResolver(XMLClassDescriptorResolver xmlClassDescriptorResolver) {
    this._xmlClassDescriptorResolver = xmlClassDescriptorResolver;
  }

  public void setProperty(String propertyName, Object value) {
    IllegalArgumentException iae;
    if (propertyName == null) {
      iae = new IllegalArgumentException("setProperty must not be called with a propertyName == null");
      LOG.warn(iae.getMessage());
      throw iae;
    } else {
      if (propertyName.equals("org.exolab.xml.naming")) {
        if (value instanceof String) {
          this.setXMLNaming((String)value);
        } else {
          if (!(value instanceof XMLNaming)) {
            iae = new IllegalArgumentException("XML Naming can only be set to a String or an implementation of XMLNaming");
            LOG.warn(iae.getMessage());
            throw iae;
          }

          this.setXMLNaming((XMLNaming)value);
        }
      }
      if (propertyName.equals("org.exec.xml.java.naming")) {
        if (value instanceof String) {
          this.setJavaNaming((String)value);
        } else {
          if (!(value instanceof JavaNaming)) {
            iae = new IllegalArgumentException("Java Naming can only be set to a String or an implementation of JavaNaming");
            LOG.warn(iae.getMessage());
            throw iae;
          }
          this.setJavaNaming((JavaNaming)value);
        }
      }
      this._primitiveNodeType = null;
      this._regExpEvaluator = null;
      this._properties.put(propertyName, value);
    }
  }
  public Object getProperty(String propertyName) {
    return this._properties.getObject(propertyName);
  }
  public XMLNaming getXMLNaming() {
    if (this._xmlNaming != null) {
      return this._xmlNaming;
    } else {
      String prop = this._properties.getString("org.exolab.xml.naming", (String)null);
      this.setXMLNaming(prop);
      return this._xmlNaming;
    }
  }
  public XMLNaming getXMLNaming(ClassLoader classLoader) {
    return this.getXMLNaming();
  }
  public JavaNaming getJavaNaming() {
    return this._javaNaming;
  }
  public Parser getParser() {
    return this.getParser((String)null);
  }
  public Parser getParser(String features) {
    return XMLParserUtils.getParser(this._properties, features);
  }
  public XMLReader getXMLReader() {
    return this.getXMLReader((String)null);
  }
  public XMLReader getXMLReader(String features) {
    XMLReader reader = null;
    Boolean validation = this._properties.getBoolean("org.exolab.parser.validation");
    Boolean namespaces = this._properties.getBoolean("org.exolab.parser.namespaces");
    String readerClassName = this._properties.getString("org.exolab.parser");
    if (readerClassName == null || readerClassName.length() == 0) {
      SAXParser saxParser = XMLParserUtils.getSAXParser(validation, namespaces);
      if (saxParser != null) {
        try {
          reader = saxParser.getXMLReader();
        } catch (SAXException var8) {
          LOG.error(Messages.format("conf.configurationError", var8));
        }
      }
    }

    if (reader == null) {
      if (readerClassName == null || readerClassName.length() == 0 || readerClassName.equalsIgnoreCase("xerces")) {
        readerClassName = "org.apache.xerces.parsers.SAXParser";
      }
      reader = XMLParserUtils.instantiateXMLReader(readerClassName);
    }

    XMLParserUtils.setFeaturesOnXmlReader(this._properties.getString("org.exolab.sax.features", features), this._properties.getString("org.exolab.sax.features-to-disable", ""), validation, namespaces, reader);
    return reader;
  }

  public NodeType getPrimitiveNodeType() {
    if (this._primitiveNodeType != null) {
      return this._primitiveNodeType;
    } else {
      String prop = this._properties.getString("org.exolab.xml.introspector.primitive.nodetype", (String)null);
      if (prop == null) {
        return null;
      } else {
        this._primitiveNodeType = NodeType.getNodeType(prop);
        return this._primitiveNodeType;
      }
    }
  }

  public RegExpEvaluator getRegExpEvaluator() {
    if (this._regExpEvaluator != null) {
      return this._regExpEvaluator;
    } else {
      String className = this._properties.getString("org.exolab.regexp", "");
      if (className.length() == 0) {
        this._regExpEvaluator = null;
      } else {
        try {
          Class<?> regExpEvalClass = Class.forName(className);
          this._regExpEvaluator = (RegExpEvaluator)regExpEvalClass.newInstance();
        } catch (ClassNotFoundException var3) {
          throw new RuntimeException(Messages.format("conf.failedInstantiateRegExp", className, var3));
        } catch (InstantiationException var4) {
          throw new RuntimeException(Messages.format("conf.failedInstantiateRegExp", className, var4));
        } catch (IllegalAccessException var5) {
          throw new RuntimeException(Messages.format("conf.failedInstantiateRegExp", className, var5));
        }
      }

      return this._regExpEvaluator;
    }
  }
  public Serializer getSerializer() {
    return XMLParserUtils.getSerializer(this._properties);
  }
  public OutputFormat getOutputFormat() {
    return XMLParserUtils.getOutputFormat(this._properties);
  }
  protected XMLSerializerFactory getSerializerFactory(String serializerFactoryName) {
    return XMLParserUtils.getSerializerFactory(serializerFactoryName);
  }

  public DocumentHandler getSerializer(OutputStream output) throws IOException {
    Serializer serializer = this.getSerializer();
    serializer.setOutputByteStream(output);
    DocumentHandler docHandler = serializer.asDocumentHandler();
    if (docHandler == null) {
      throw new RuntimeException(Messages.format("conf.serializerNotSaxCapable", serializer.getClass().getName()));
    } else {
      return docHandler;
    }
  }

  public DocumentHandler getSerializer(Writer output) throws IOException {
    Serializer serializer = this.getSerializer();
    serializer.setOutputCharStream(output);
    DocumentHandler docHandler = serializer.asDocumentHandler();
    if (docHandler == null) {
      throw new RuntimeException(Messages.format("conf.serializerNotSaxCapable", serializer.getClass().getName()));
    } else {
      return docHandler;
    }
  }
  public XMLClassDescriptorResolver getXMLClassDescriptorResolver() {
    return this._xmlClassDescriptorResolver;
  }
  public Introspector getIntrospector() {
    return this._introspector;
  }
  public ResolverStrategy getResolverStrategy() {
    return this._resolverStrategy;
  }
  public void setResolverStrategy(ResolverStrategy resolverStrategy) {
    this._resolverStrategy = resolverStrategy;
  }
  public void setMappingLoader(MappingLoader mappingLoader) {
    this._mappingLoader = mappingLoader;
  }
  public MappingLoader getMappingLoader() {
    return this._mappingLoader;
  }
  public void setJavaNaming(JavaNaming javaNaming) {
    this._javaNaming = javaNaming;
  }
  public void setJavaNaming(String javaNamingProperty) {
    if (javaNamingProperty != null && javaNamingProperty.length() != 0) {
      try {
        Class<?> cls = Class.forName(javaNamingProperty);
        this._javaNaming = (JavaNaming)cls.newInstance();
      } catch (Exception var4) {
        IllegalArgumentException iae = new IllegalArgumentException("Failed to load JavaNaming: " + var4);
        LOG.warn(iae.getMessage());
        throw iae;
      }
    } else {
      this._javaNaming = new JavaNamingImpl();
    }

  }

  public void setXMLNaming(XMLNaming xmlNaming) {
    this._xmlNaming = xmlNaming;
    if (this._introspector != null) {
      this._introspector.setNaming(this._xmlNaming);
    }

  }

  public void setXMLNaming(String xmlNamingProperty) {
    if (xmlNamingProperty != null && !xmlNamingProperty.equalsIgnoreCase("lower")) {
      if (xmlNamingProperty.equalsIgnoreCase("mixed")) {
        DefaultNaming dn = new DefaultNaming();
        dn.setStyle((short)1);
        this.setXMLNaming((XMLNaming)dn);
      } else {
        try {
          Class<?> cls = Class.forName(xmlNamingProperty);
          this.setXMLNaming((XMLNaming)cls.newInstance());
        } catch (Exception var4) {
          IllegalArgumentException iae = new IllegalArgumentException("Failed to load XMLNaming: " + var4);
          LOG.warn(iae.getMessage());
          throw iae;
        }
      }
    } else {
      this.setXMLNaming((XMLNaming)(new DefaultNaming()));
    }
    if (this._xmlNaming == null) {
      IllegalArgumentException iae = new IllegalArgumentException("Failed to correctly set XMLNaming; property was: " + xmlNamingProperty);
      LOG.warn(iae.getMessage());
      throw iae;
    }
  }
  public void setProperty(String propertyName, boolean value) {
    this._properties.put(propertyName, value);
  }
  public Boolean getBooleanProperty(String propertyName) {
    return this._properties.getBoolean(propertyName);
  }
  public String getStringProperty(String propertyName) {
    return this._properties.getString(propertyName);
  }
  public void setClassLoader(ClassLoader classLoader) {
    this._classLoader = classLoader;
    if (this._xmlClassDescriptorResolver != null) {
      this._xmlClassDescriptorResolver.setClassLoader(classLoader);
    }
  }
  public void setXMLClassDescriptorResolver(XMLClassDescriptorResolver xmlClassDescriptorResolver) {
    this._xmlClassDescriptorResolver = xmlClassDescriptorResolver;
  }
  public void setIntrospector(Introspector introspector) {
    this._introspector = introspector;
  }
  public ClassLoader getClassLoader() {
    return this._classLoader;
  }
  public boolean getLenientIdValidation() {
    Boolean lenientIdValidation = this._properties.getBoolean("org.exolab.xml.lenient.id.validation");
    if (lenientIdValidation == null) {
      String message = "Property lenientIdValidation must not be null";
      LOG.warn(message);
      throw new IllegalStateException(message);
    } else {
      return lenientIdValidation;
    }
  }

  public boolean getLenientSequenceOrder() {
    Boolean lenientSequenceOrder = this._properties.getBoolean("org.exolab.xml.lenient.sequence.order");
    if (lenientSequenceOrder == null) {
      String message = "Property lenientSequenceOrder must not be null";
      LOG.warn(message);
      throw new IllegalStateException(message);
    } else {
      return lenientSequenceOrder;
    }
  }
  public Boolean getLoadPackageMapping() {
    return this._properties.getBoolean("org.exolab.xml.loadPackageMappings");
  }
  public void setLoadPackageMapping(Boolean loadPackageMapping) {
    this._properties.put("org.exolab.xml.loadPackageMappings", loadPackageMapping);
  }
  public Boolean getUseIntrospector() {
    return this._properties.getBoolean("org.exec.xml.class-resolver.use-introspection");
  }
  public void setUseIntrospector(Boolean useIntrospector) {
    this._properties.put("org.exec.xml.class-resolver.use-introspection", useIntrospector);
  }
  public boolean marshallingValidation() {
    Boolean marshallingValidation = this._properties.getBoolean("org.exolab.marshalling.validation");
    if (marshallingValidation == null) {
      String message = "Property marshallingValidation must not be null";
      LOG.warn(message);
      throw new IllegalStateException(message);
    } else {
      return marshallingValidation;
    }
  }
  public boolean strictElements() {
    Boolean strictElements = this._properties.getBoolean("org.exolab.xml.strictelements");
    if (strictElements == null) {
      String message = "Property strictElements must not be null";
      LOG.warn(message);
      throw new IllegalStateException(message);
    } else {
      return strictElements;
    }
  }
}
