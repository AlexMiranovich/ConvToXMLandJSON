package org.exec.core.util;

public final class ExecProperties extends AbstractProperties {
  private static final String FILENAME = "exec.properties";
  public ExecProperties(AbstractProperties parent) {
    super(parent);
    loadUserProperties("exec.properties");
  }
}
