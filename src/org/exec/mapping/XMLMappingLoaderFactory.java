package org.exec.mapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class XMLMappingLoaderFactory extends AbstractMappingLoaderFactory {
  private static final Log LOG = LogFactory.getLog(XMLMappingLoaderFactory.class);
  public static final String NAME = "XML";
  private static final String CLASS_NAME = "org.exolab.xml.XMLMappingLoader";
  public final String getName() {
    return "XML";
  }
  public String getClassname() {
    return "org.exolab.xml.XMLMappingLoader";
  }
  public BindingType getBindingType() {
    return BindingType.XML;
  }
}
