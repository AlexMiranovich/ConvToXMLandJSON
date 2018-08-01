package org.exolab.xml.util;

import java.util.StringTokenizer;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exec.core.util.AbstractProperties;
import org.exec.core.util.Messages;
import org.exolab.xml.OutputFormat;
import org.exolab.xml.Serializer;
import org.exolab.xml.XMLSerializerFactory;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

public class XMLParserUtils {
  static final Log LOG = LogFactory.getFactory().getInstance(XMLParserUtils.class);
  private static final String VALIDATION = "http://xml.org/sax/features/validation";
  private static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
  public static void setFeaturesOnXmlReader(String parserFeatures, String parserFeaturesToDisable, boolean validation, boolean namespaces, XMLReader xmlReader) {
    try {
      xmlReader.setFeature("http://xml.org/sax/features/validation", validation);
      xmlReader.setFeature("http://xml.org/sax/features/namespaces", namespaces);
      enableFeatures(parserFeatures, xmlReader);
      disableFeatures(parserFeaturesToDisable, xmlReader);
    }
    catch (SAXException except) {
      LOG.error(Messages.format("conf.configurationError", except));
    }
  }
  private static void enableFeatures(String features, XMLReader xmlReader) throws SAXNotRecognizedException, SAXNotSupportedException {
    if (features != null) {
      StringTokenizer token = new StringTokenizer(features, ", ");
      while (token.hasMoreTokens()) {
        xmlReader.setFeature(token.nextToken(), true);
      }
    }
  }
  private static void disableFeatures(String features, XMLReader xmlReader)
    throws SAXNotRecognizedException, SAXNotSupportedException {
    if (features != null) {
      StringTokenizer token = new StringTokenizer(features, ", ");
      while (token.hasMoreTokens()) {
        xmlReader.setFeature(token.nextToken(), false);
      }
    }
  }
  public static SAXParser getSAXParser(boolean validation, boolean namespaces) {
    SAXParser saxParser = null;
    SAXParserFactory factory = SAXParserFactory.newInstance();
    factory.setNamespaceAware(namespaces);
    factory.setValidating(validation);
    try {
      saxParser = factory.newSAXParser();
      if (LOG.isDebugEnabled()) {
        LOG.debug("Successfully instantiated a JAXP SAXParser instance.");
      }
    }
    catch (ParserConfigurationException pcx) {
      LOG.error(Messages.format("conf.configurationError", pcx));
    }
    catch (SAXException sx) {
      LOG.error(Messages.format("conf.configurationError", sx));
    }
    return saxParser;
  }
  public static XMLReader instantiateXMLReader(String className) {
    XMLReader xmlReader;
    try {
      Class cls = Class.forName(className);
      xmlReader = (XMLReader)cls.newInstance();
      if (LOG.isDebugEnabled()) {
        LOG.debug("Successfully instantiated " + className);
      }
    }
    catch (Exception except) {
      throw new RuntimeException(Messages.format("conf.failedInstantiateParser", className, except));
    }
    return xmlReader;
  }
  public static Parser instantiateParser(String className) {
    Parser parser;
    try
    {
      Class cls = Class.forName(className);
      parser = (Parser)cls.newInstance();
      if (LOG.isDebugEnabled()) {
        LOG.debug("Successfully instantiated " + className);
      }
    }
    catch (Exception except) {
      throw new RuntimeException(Messages.format("conf.failedInstantiateParser", className, except));
    }
    return parser;
  }
  public static Parser getParser(AbstractProperties properties, String features) {
    Parser parser = null;
    Boolean validation = properties.getBoolean("org.exolab.parser.validation");
    Boolean namespaces = properties.getBoolean("org.exolab.parser.namespaces");
    String parserClassName = properties.getString("org.exolab.parser");
    if ((parserClassName == null) || (parserClassName.length() == 0)) {
      SAXParser saxParser = getSAXParser(validation.booleanValue(), namespaces.booleanValue());
      if (saxParser != null) {
        try {
          parser = saxParser.getParser();
        }
        catch (SAXException e) {
          LOG.error(Messages.format("conf.configurationError", e));
        }
      }
    }
    if (parser == null) {
      if ((parserClassName == null) || (parserClassName.length() == 0) || (parserClassName.equalsIgnoreCase("xerces"))) {
        parserClassName = "org.apache.xerces.parsers.SAXParser";
      }
      parser = instantiateParser(parserClassName);
      if ((parser instanceof XMLReader))
      {
        XMLReader xmlReader = (XMLReader)parser;
        setFeaturesOnXmlReader(properties.getString("org.exolab.sax.features", features), properties.getString("org.exolab.sax.features-to-disable", ""), validation.booleanValue(), namespaces.booleanValue(), xmlReader);
      }
    }
    return parser;
  }
  public static Serializer getSerializer(AbstractProperties properties) {
    Serializer serializer = getSerializerFactory(properties.getString("org.exolab.xml.serializer.factory")).getSerializer();
    serializer.setOutputFormat(getOutputFormat(properties));
    return serializer;
  }
  public static OutputFormat getOutputFormat(AbstractProperties properties) {
    boolean indent = properties.getBoolean("org.exolab.indent", false);
    OutputFormat format = getSerializerFactory(properties.getString("org.exolab.xml.serializer.factory")).getOutputFormat();
    format.setMethod("xml");
    format.setIndenting(indent);
    if (!indent) {
      format.setPreserveSpace(true);
    }
    return format;
  }
  public static XMLSerializerFactory getSerializerFactory(String serializerFactoryName) {
    XMLSerializerFactory serializerFactory;
    try {
      serializerFactory = (XMLSerializerFactory)Class.forName(serializerFactoryName).newInstance();
    }
    catch (Exception except) {
      throw new RuntimeException(Messages.format("conf.failedInstantiateSerializerFactory", serializerFactoryName, except));
    }
    return serializerFactory;
  }
}
