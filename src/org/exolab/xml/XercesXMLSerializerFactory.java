package org.exolab.xml;

public class XercesXMLSerializerFactory implements XMLSerializerFactory {
  public Serializer getSerializer()
  {
    return new XercesSerializer();
  }
  public OutputFormat getOutputFormat()
  {
    return new XercesOutputFormat();
  }
}
