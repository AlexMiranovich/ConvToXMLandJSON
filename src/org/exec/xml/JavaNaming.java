package org.exec.xml;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface JavaNaming {
   String METHOD_PREFIX_ADD = "add";
   String METHOD_PREFIX_GET = "get";
   String METHOD_PREFIX_IS = "is";
   String METHOD_PREFIX_SET = "set";
   String METHOD_PREFIX_CREATE = "create";
   char FIELD_UNDERSCORE_PREFIX = '_';
   boolean isKeyword(String paramString);
   boolean isValidJavaIdentifier(String paramString);
   String toJavaClassName(String paramString);
   String toJavaMemberName(String paramString);
   String toJavaMemberName(String paramString, boolean paramBoolean);
   boolean isValidPackageName(String paramString);
   String packageToPath(String paramString);
   String getQualifiedFileName(String paramString1, String paramString2);
   String getClassName(Class paramClass);
   String getPackageName(String paramString);
   String extractFieldNameFromMethod(Method paramMethod);
   String extractFieldNameFromField(Field paramField);
   boolean isSetMethod(Method paramMethod);
   boolean isCreateMethod(Method paramMethod);
   boolean isGetMethod(Method paramMethod);
   boolean isIsMethod(Method paramMethod);
   boolean isAddMethod(Method paramMethod);
   String getAddMethodNameForField(String paramString);
   String getSetMethodNameForField(String paramString);
   String getGetMethodNameForField(String paramString);
   String getIsMethodNameForField(String paramString);
   String getCreateMethodNameForField(String paramString);
}
