package org.exec.xml;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Hashtable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JavaNamingImpl implements JavaNaming {
  private static final Log LOG = LogFactory.getLog(JavaNamingImpl.class);
  public static final String UPPER_CASE_AFTER_UNDERSCORE_PROPERTY = "org.exolab.xml.JavaNaming.upperCaseAfterUnderscore";
  public static boolean _upperCaseAfterUnderscore = false;
  private static final Hashtable SUBST = keywordMap();
  private static final String[] KEYWORDS = { "abstract", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "extends", "false", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "void", "volatile", "while" };
  public final boolean isKeyword(String name) {
    if (name == null) {
      return false;
    }
    for (int i = 0; i < KEYWORDS.length; i++) {
      if (KEYWORDS[i].equals(name)) {
        return true;
      }
    }
    return false;
  }
  public final boolean isValidJavaIdentifier(String string) {
    if ((string == null) || (string.length() == 0)) {
      return false;
    }
    for (int i = 0; i < string.length(); i++) {
      char ch = string.charAt(i);
      if (ch != '_') {
        if (ch != '$') {
          if ((ch < 'A') || (ch > 'Z')) {
            if ((ch < 'a') || (ch > 'z')) {
              if ((ch >= '0') && (ch <= '9'))
              {
                if (i == 0) {
                  return false;
                }
              }
              else {
                return false;
              }
            }
          }
        }
      }
    }
    if (isKeyword(string)) {
      return false;
    }
    return true;
  }
  public final String toJavaClassName(String name) {
    if ((name == null) || (name.length() <= 0)) {
      return name;
    }
    int colon = name.indexOf(':');
    if (colon != -1) {
      return toJavaName(name.substring(colon + 1), true);
    }
    return toJavaName(name, true);
  }
  public final String toJavaMemberName(String name)
  {
    return toJavaMemberName(name, true);
  }
  public final String toJavaMemberName(String name, boolean useKeywordSubstitutions) {
    if (name == null) {
      return null;
    }
    String memberName = toJavaName(name, false);
    if ((isKeyword(memberName)) && (useKeywordSubstitutions)) {
      String mappedName = (String)SUBST.get(memberName);
      if (mappedName != null) {
        memberName = mappedName;
      } else {
        memberName = '_' + memberName;
      }
    }
    return memberName;
  }
  
  public final boolean isValidPackageName(String packageName) {
    if ((packageName == null) || (packageName.length() < 1)) {
      return true;
    }
    if (".".equals(packageName)) {
      return false;
    }
    if ((packageName.startsWith(".")) || (packageName.endsWith("."))) {
      return false;
    }
    boolean valid = true;
    String[] packageNameParts = packageName.split("\\.");
    for (int i = 0; i < packageNameParts.length; i++) {
      String packageNamePart = packageNameParts[i];
      valid &= isValidJavaIdentifier(packageNamePart);
    }
    return valid;
  }
  public final String packageToPath(String packageName) {
    if (packageName == null) {
      return packageName;
    }
    if (!isValidPackageName(packageName)) {
      String message = "Package name: " + packageName + " is not valid";
      LOG.warn(message);
      throw new IllegalArgumentException(message);
    }
    return packageName.replace('.', File.separatorChar);
  }
  private static Hashtable keywordMap() {
    Hashtable ht = new Hashtable();
    ht.put("class", "clazz");
    return ht;
  }
  private String toJavaName(String name, boolean upperFirst) {
    int size = name.length();
    char[] ncChars = name.toCharArray();
    int next = 0;
    boolean uppercase = upperFirst;
    boolean lowercase = !uppercase;
    if ((size > 1) && (lowercase) && 
      (Character.isUpperCase(ncChars[0])) && (Character.isUpperCase(ncChars[1]))) {
      lowercase = false;
    }
    for (int i = 0; i < size; i++) {
      char ch = ncChars[i];
      switch (ch)
      {
      case ' ': 
      case '.': 
        ncChars[(next++)] = '_';
        break;
      case '-': 
      case ':': 
        uppercase = true;
        break;
      case '_': 
        if (_upperCaseAfterUnderscore) {
          uppercase = true;
          ncChars[next] = ch;
          next++;
        }
        break;
      }
      if (uppercase) {
        ncChars[next] = Character.toUpperCase(ch);
        uppercase = false;
      }
      else if (lowercase) {
        ncChars[next] = Character.toLowerCase(ch);
        lowercase = false;
      }
      else
      {
        ncChars[next] = ch;
      }
      next++;
    }
    return new String(ncChars, 0, next);
  }
  public final String getQualifiedFileName(String fileName, String packageName) {
    if ((packageName == null) || (packageName.length() == 0)) {
      return fileName;
    }
    StringBuffer result = new StringBuffer();
    result.append(packageToPath(packageName));
    result.append('/');
    result.append(fileName);
    return result.toString();
  }
  public final String getPackageName(String className) {
    if ((className == null) || (className.length() < 1)) {
      return className;
    }
    int idx = className.lastIndexOf('.');
    if (idx >= 0) {
      return className.substring(0, idx);
    }
    return "";
  }
  public final String extractFieldNameFromMethod(Method method) {
    if (method == null) {
      return null;
    }
    String fieldName = null;
    if (isSetMethod(method)) {
      fieldName = method.getName().substring("set".length());
    } else if (isCreateMethod(method)) {
      fieldName = method.getName().substring("create".length());
    } else if (isGetMethod(method)) {
      fieldName = method.getName().substring("get".length());
    } else if (isIsMethod(method)) {
      fieldName = method.getName().substring("is".length());
    } else if (isAddMethod(method)) {
      fieldName = method.getName().substring("add".length());
    }
    return toJavaMemberName(fieldName);
  }
  public final String extractFieldNameFromField(Field field) {
    if (field == null) {
      return null;
    }
    String fieldName = field.getName();
    if (fieldName.charAt(0) == '_') {
      fieldName = fieldName.substring(1);
    }
    return fieldName;
  }
  public final boolean isSetMethod(Method method) {
    if (method == null) {
      return false;
    }
    if (!method.getName().startsWith("set")) {
      return false;
    }
    if (method.getParameterTypes().length != 1) {
      return false;
    }
    if ((method.getReturnType() != Void.TYPE) && (method.getReturnType() != Void.class)) {
      return false;
    }
    return true;
  }
  public final boolean isCreateMethod(Method method) {
    if (method == null) {
      return false;
    }
    if (!method.getName().startsWith("create")) {
      return false;
    }
    if (method.getParameterTypes().length != 0) {
      return false;
    }
    if (method.getReturnType() == null) {
      return false;
    }
    return true;
  }
  public final boolean isGetMethod(Method method) {
    if (method == null) {
      return false;
    }
    if (!method.getName().startsWith("get")) {
      return false;
    }
    if (method.getParameterTypes().length != 0) {
      return false;
    }
    if (method.getReturnType() == null) {
      return false;
    }
    return true;
  }
  public final boolean isIsMethod(Method method) {
    if (method == null) {
      return false;
    }
    if (!method.getName().startsWith("is")) {
      return false;
    }
    if (method.getParameterTypes().length != 0) {
      return false;
    }
    if ((method.getReturnType().isPrimitive()) && (method.getReturnType() != Boolean.TYPE)) {
      return false;
    }
    if ((!method.getReturnType().isPrimitive()) && (method.getReturnType() != Boolean.class)) {
      return false;
    }
    return true;
  }
  public final boolean isAddMethod(Method method) {
    if (method == null) {
      return false;
    }
    if (!method.getName().startsWith("add")) {
      return false;
    }
    if (method.getParameterTypes().length != 1) {
      return false;
    }
    if ((method.getReturnType() != Void.TYPE) && (method.getReturnType() != Void.class)) {
      return false;
    }
    return true;
  }
  
  public final String getAddMethodNameForField(String fieldName)
  {
    return "add" + toJavaClassName(fieldName);
  }
  public final String getCreateMethodNameForField(String fieldName)
  {
    return "create" + toJavaClassName(fieldName);
  }
  public final String getGetMethodNameForField(String fieldName)
  {
    return "get" + toJavaClassName(fieldName);
  }
  public final String getIsMethodNameForField(String fieldName)
  {
    return "is" + toJavaClassName(fieldName);
  }
  public final String getSetMethodNameForField(String fieldName)
  {
    return "set" + toJavaClassName(fieldName);
  }
  public String getClassName(Class clazz) {
    if (clazz == null) {
      return null;
    }
    String name = clazz.getName();
    int idx = name.lastIndexOf('.');
    if (idx >= 0) {
      name = name.substring(idx + 1);
    }
    return name;
  }
}
