package org.exolab.xml;

import java.lang.reflect.Method;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exec.core.util.Messages;

public class XercesOutputFormat implements OutputFormat {
  private static final Log LOG = LogFactory.getLog(XercesSerializer.class);
  private Object _outputFormat;
  public XercesOutputFormat() {
    try {
      this._outputFormat = Class.forName("org.apache.xml.serialize.OutputFormat").newInstance();
    } catch (Exception except) {
      throw new RuntimeException(Messages.format("conf.failedInstantiateOutputFormat", "org.apache.xml.serialize.XMLSerializer", except));
    }
  }
  public void setMethod(String method) {
    try {
      Method aMethod = this._outputFormat.getClass().getMethod("setMethod", new Class[] { String.class });
      aMethod.invoke(this._outputFormat, new Object[] { method });
    } catch (Exception e) {
      String msg = "Problem invoking OutputFormat.setMethod()";
      LOG.error(msg, e);
      throw new RuntimeException(msg + e.getMessage());
    }
  }
  public void setIndenting(boolean indent) {
    try {
      Method method = this._outputFormat.getClass().getMethod("setIndenting", new Class[] { Boolean.TYPE });
      method.invoke(this._outputFormat, new Object[] { Boolean.valueOf(indent) });
    } catch (Exception e) {
      String msg = "Problem invoking OutputFormat.setIndenting()";
      LOG.error(msg, e);
      throw new RuntimeException(msg + e.getMessage());
    }
  }
  public void setPreserveSpace(boolean preserveSpace) {
    try {
      Method method = this._outputFormat.getClass().getMethod("setPreserveSpace", new Class[] { Boolean.TYPE });
      method.invoke(this._outputFormat, new Object[] { Boolean.valueOf(preserveSpace) });
    } catch (Exception e) {
      String msg = "Problem invoking OutputFormat.setPreserveSpace()";
      LOG.error(msg, e);
      throw new RuntimeException(msg + e.getMessage());
    }
  }
  public Object getFormat()
  {
    return this._outputFormat;
  }
  public void setDoctype(String type1, String type2) {
    try {
      Method method = this._outputFormat.getClass().getMethod("setDoctype", new Class[] { String.class, String.class });
      method.invoke(this._outputFormat, new Object[] { type1, type2 });
    } catch (Exception e) {
      String msg = "Problem invoking OutputFormat.setDoctype()";
      LOG.error(msg, e);
      throw new RuntimeException(msg + e.getMessage());
    }
  }
  public void setOmitXMLDeclaration(boolean omitXMLDeclaration) {
    try {
      Method method = this._outputFormat.getClass().getMethod("setOmitXMLDeclaration", new Class[] { Boolean.TYPE });
      method.invoke(this._outputFormat, new Object[] { Boolean.valueOf(omitXMLDeclaration) });
    } catch (Exception e) {
      String msg = "Problem invoking OutputFormat.setOmitXMLDeclaration()";
      LOG.error(msg, e);
      throw new RuntimeException(msg + e.getMessage());
    }
  }
  public void setOmitDocumentType(boolean omitDocumentType) {
    try {
      Method method = this._outputFormat.getClass().getMethod("setOmitDocumentType", new Class[] { Boolean.TYPE });
      method.invoke(this._outputFormat, new Object[] { Boolean.valueOf(omitDocumentType) });
    } catch (Exception e) {
      String msg = "Problem invoking OutputFormat.setOmitDocumentType()";
      LOG.error(msg, e);
      throw new RuntimeException(msg + e.getMessage());
    }
  }
  public void setEncoding(String encoding) {
    try {
      Method method = this._outputFormat.getClass().getMethod("setEncoding", new Class[] { String.class });
      method.invoke(this._outputFormat, new Object[] { encoding });
    } catch (Exception e) {
      String msg = "Problem invoking OutputFormat.setEncoding()";
      LOG.error(msg, e);
      throw new RuntimeException(msg + e.getMessage());
    }
  }
}
