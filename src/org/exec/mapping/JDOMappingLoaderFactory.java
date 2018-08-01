package org.exec.mapping;

public class JDOMappingLoaderFactory extends AbstractMappingLoaderFactory {
  public static final String NAME = "JDO";
  private static final String CLASS_NAME = "org.exolab.exec.jdo.engine.JDOMappingLoader";
  public String getName()
  {
    return "JDO";
  }
  public String getClassname()
  {
    return "org.exolab.exec.jdo.engine.JDOMappingLoader";
  }
  public BindingType getBindingType()
  {
    return BindingType.JDO;
  }
}
