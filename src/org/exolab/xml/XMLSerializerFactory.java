package org.exolab.xml;

public interface XMLSerializerFactory {
  Serializer getSerializer();
  OutputFormat getOutputFormat();
}
