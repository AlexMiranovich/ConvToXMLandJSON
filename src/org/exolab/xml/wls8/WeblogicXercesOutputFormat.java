package org.exolab.xml.wls8;

import java.lang.reflect.Method;
import org.exolab.xml.OutputFormat;

public class WeblogicXercesOutputFormat
  extends WeblogicXercesImplementation
  implements OutputFormat
{
  private static Class outputFormatClass;
  private static Method setDoctype;
  private static Method setEncoding;
  private static Method setIndenting;
  private static Method setMethod;
  private static Method setOmitDocumentType;
  private static Method setOmitXMLDeclaration;
  private static Method setPreserveSpace;
  private Object outputFormat;
  static {
    String wlsOutputFormatClassFqcn = "weblogic.apache.xml.serialize.OutputFormat";
    try {
      outputFormatClass = Class.forName(wlsOutputFormatClassFqcn);
    } catch (ClassNotFoundException e) {
      handleStaticInitException("Could find class " + wlsOutputFormatClassFqcn, e);
    }
    Class[] parameterTwoStrings = { String.class, String.class };
    setDoctype = getMethod(outputFormatClass, "setDoctype", parameterTwoStrings);
    
    Class[] parameterOneString = { String.class };
    setEncoding = getMethod(outputFormatClass, "setEncoding", parameterOneString);
    Class[] parameterBoolean = { Boolean.TYPE };
    setIndenting = getMethod(outputFormatClass, "setIndenting", parameterBoolean);
    setMethod = getMethod(outputFormatClass, "setMethod", parameterOneString);
    setOmitDocumentType = getMethod(outputFormatClass, "setOmitDocumentType", parameterBoolean);
    setOmitXMLDeclaration = getMethod(outputFormatClass, "setOmitXMLDeclaration", parameterBoolean);
    setPreserveSpace = getMethod(outputFormatClass, "setPreserveSpace", parameterBoolean);
  }
  public WeblogicXercesOutputFormat() {
    try {
      this.outputFormat = outputFormatClass.newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(e.toString());
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e.toString());
    }
  }
  
  public void setMethod(String method) {
    Object[] params = { method };
    invoke(setMethod, params);
  }
  public void setIndenting(boolean indent) {
    Boolean[] params = { Boolean.valueOf(indent) };
    invoke(setIndenting, params);
  }
  public void setPreserveSpace(boolean preserveSpace) {
    Boolean[] params = { new Boolean(preserveSpace) };
    invoke(setPreserveSpace, params);
  }
  public Object getFormat()
  {
    return this.outputFormat;
  }
  public void setDoctype(String type1, String type2) {
    Object[] params = { type1, type2 };
    invoke(setDoctype, params);
  }
  public void setOmitXMLDeclaration(boolean omitXMLDeclaration) {
    Boolean[] params = { Boolean.valueOf(omitXMLDeclaration) };
    invoke(setOmitXMLDeclaration, params);
  }
  public void setOmitDocumentType(boolean omitDocumentType) {
    Boolean[] params = { Boolean.valueOf(omitDocumentType) };
    invoke(setOmitDocumentType, params);
  }
  public void setEncoding(String encoding) {
    String[] params = { encoding };
    invoke(setEncoding, params);
  }
  private Object invoke(Method method, Object[] params)
  {
    return invoke(this.outputFormat, method, params);
  }
}
