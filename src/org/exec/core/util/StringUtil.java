package org.exec.core.util;

import org.apache.commons.lang.StringUtils;


public class StringUtil {
  public static String replaceAll(String source, String toReplace, String replacement) {
    return StringUtils.replace(source, toReplace, replacement);
  }
}
