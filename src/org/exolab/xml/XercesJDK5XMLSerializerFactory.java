package org.exolab.xml;

public class XercesJDK5XMLSerializerFactory implements XMLSerializerFactory {
  public Serializer getSerializer()
  {
    return new XercesJDK5Serializer();
  }
  public OutputFormat getOutputFormat()
  {
    return new XercesJDK5OutputFormat();
  }
}
