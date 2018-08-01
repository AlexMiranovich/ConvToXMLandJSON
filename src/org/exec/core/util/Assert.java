package org.exec.core.util;
public class Assert {
  private static final String NULL_PARAM_MESSAGE = "Parameter %s can not be null.";
  private static final String EMPTY_PARAM_MESSAGE = "Parameter %s can not be null or empty.";
  public static void paramNotNull(Object param, String paramName) {
    notNull(param, String.format("Parameter %s can not be null.", new Object[] { paramName }));
  }
  public static void paramNotEmpty(String param, String paramName) {
    notEmpty(param, String.format("Parameter %s can not be null or empty.", new Object[] { paramName }));
  }
  public static void notNull(Object obj, String msg) {
    if (obj == null) {
      throw new IllegalArgumentException(msg);
    }
  }
  public static void notEmpty(String obj, String msg) {
    notNull(obj, msg);
    if (obj.trim().length() == 0) {
      throw new IllegalArgumentException(msg);
    }
  }
}
