package org.exec.xml;

import org.exec.core.CoreProperties;
import org.exec.core.util.AbstractProperties;
import org.exec.core.util.ExecProperties;

public class XMLProperties extends AbstractProperties {
  private static final String FILEPATH = "/org/exec/xml/";
  private static final String FILENAME = "exec.xml.properties";
  public static final String PRIMITIVE_NODE_TYPE = "org.exolab.exec.xml.introspector.primitive.nodetype";
  public static final String PARSER = "org.exolab.exec.parser";
  public static final String PARSER_VALIDATION = "org.exolab.exec.parser.validation";
  public static final String NAMESPACES = "org.exolab.exec.parser.namespaces";
  public static final String NAMESPACE_PACKAGE_MAPPINGS = "org.exolab.exec.xml.nspackages";
  public static final String XML_NAMING = "org.exolab.exec.xml.naming";
  public static final String JAVA_NAMING = "org.exec.xml.java.naming";
  public static final String MARSHALLING_VALIDATION = "org.exolab.exec.marshalling.validation";
  public static final String USE_INDENTATION = "org.exolab.exec.indent";
  public static final String PARSER_FEATURES = "org.exolab.exec.sax.features";
  public static final String PARSER_FEATURES_DISABLED = "org.exolab.exec.sax.features-to-disable";
  public static final String REG_EXP_CLASS_NAME = "org.exolab.exec.regexp";
  public static final String DEBUG = "org.exolab.exec.debug";
  public static final String STRICT_ELEMENTS = "org.exolab.exec.xml.strictelements";
  public static final String SAVE_MAP_KEYS = "org.exolab.exec.xml.saveMapKeys";
  public static final String LOAD_PACKAGE_MAPPING = "org.exolab.exec.xml.loadPackageMappings";
  public static final String SERIALIZER_FACTORY = "org.exolab.exec.xml.serializer.factory";
  public static final String LENIENT_SEQUENCE_ORDER = "org.exolab.exec.xml.lenient.sequence.order";
  public static final String LENIENT_ID_VALIDATION = "org.exolab.exec.xml.lenient.id.validation";
  public static final String PROXY_INTERFACES = "org.exolab.exec.xml.proxyInterfaces";
  public static final String LENIENT_INTROSPECTED_ELEMENT_STRICTNESS = "org.exolab.exec.xml.lenient.introspected.element.strictness";
  public static final String COLLECTION_HANDLERS_FOR_JAVA_11_OR_12 = "org.exolab.exec.mapping.collections";
  public static final String USE_INTROSPECTION = "org.exec.xml.class-resolver.use-introspection";
  public static final String WRAP_COLLECTIONS_PROPERTY = "org.exolab.exec.xml.introspector.wrapCollections";
  public static final String LENIENT_INTEGER_VALIDATION = "org.exolab.exec.xml.lenient.integer.validation";
  
  public static AbstractProperties newInstance()
  {
    AbstractProperties core = new CoreProperties();
    AbstractProperties xml = new XMLProperties(core);
    AbstractProperties exec = new ExecProperties(xml);
    return exec;
  }
  
  public static AbstractProperties newInstance(ClassLoader app, ClassLoader domain)
  {
    AbstractProperties core = new CoreProperties(app, domain);
    AbstractProperties xml = new XMLProperties(core);
    AbstractProperties exec = new ExecProperties(xml);
    return exec;
  }
  
  public XMLProperties(AbstractProperties parent)
  {
    super(parent);
    loadDefaultProperties("/org/exec/xml/", "exec.xml.properties");
  }
}
