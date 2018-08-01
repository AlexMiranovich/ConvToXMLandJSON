package org.exec.core;

import org.exec.core.util.AbstractProperties;

public class CoreProperties extends AbstractProperties {
  private static final String FILEPATH = "/org/exec/core/";
  private static final String FILENAME = "exec.core.properties";
  public static final String MAPPING_LOADER_FACTORIES = "org.exec.mapping.loaderFactories";
  public CoreProperties()
  {
    loadDefaultProperties("/org/exec/core/", "exec.core.properties");
  }
  public CoreProperties(ClassLoader application, ClassLoader domain) {
    super(application, domain);
    loadDefaultProperties("/org/exec/core/", "exec.core.properties");
  }
}
