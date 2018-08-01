package org.exolab.xml.wls8;

import org.exolab.xml.OutputFormat;
import org.exolab.xml.Serializer;
import org.exolab.xml.XMLSerializerFactory;

public class WeblogicXercesXMLSerializerFactory implements XMLSerializerFactory {
  public Serializer getSerializer()
  {
    return new WeblogicXercesSerializer();
  }
  public OutputFormat getOutputFormat()
  {
    return new WeblogicXercesOutputFormat();
  }
}
