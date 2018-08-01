package org.exolab.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class XercesRegExpEvaluator implements RegExpEvaluator {
  private static final Log LOG = LogFactory.getLog(XercesRegExpEvaluator.class);
  private static final String BOL = "^";
  private static final String EOL = "$";
  private static final String CLASS_NAME = "org.apache.xerces.utils.regex.RegularExpression";
  Object _regexp = null;
  private Constructor _constructor;
  public XercesRegExpEvaluator() {
    try {
      Class regexpClass = Class.forName("org.apache.xerces.utils.regex.RegularExpression");
      this._constructor = regexpClass.getConstructor(new Class[] { String.class });
    } catch (ClassNotFoundException e) {
      LOG.error("Problem loading class org.apache.xerces.utils.regex.RegularExpression", e);
      throw new IllegalAccessError("Problem loading class org.apache.xerces.utils.regex.RegularExpression: " + e.getMessage());
    } catch (SecurityException e) {
      LOG.error("Problem accessing constructor of class org.apache.xerces.utils.regex.RegularExpression", e);
      throw new IllegalAccessError("Problem accessnig constructor of class org.apache.xerces.utils.regex.RegularExpression: " + e.getMessage());
    } catch (NoSuchMethodException e) {
      LOG.error("Problem locating constructor of class org.apache.xerces.utils.regex.RegularExpression", e);
      throw new IllegalAccessError("class org.apache.xerces.utils.regex.RegularExpression: " + e.getMessage());
    }
  }
  public void setExpression(String rexpr) {
    if (rexpr != null) {
      try {
        this._regexp = this._constructor.newInstance(new Object[] { "^" + rexpr + "$" });
      } catch (Exception e) {
        LOG.error("Problem invoking constructor on org.apache.xerces.utils.regex.RegularExpression", e);
        String err = "XercesRegExp Syntax error: " + e.getMessage() + " ; error occured with the following " + "regular expression: " + rexpr;
        
        throw new IllegalArgumentException(err);
      }
    } else {
      this._regexp = null;
    }
  }
  public boolean matches(String value) {
    if (this._regexp != null) {
      try {
        Method method = this._regexp.getClass().getMethod("matches", new Class[] { String.class });
        return ((Boolean)method.invoke(this._regexp, new Object[] { value })).booleanValue();
      } catch (SecurityException e) {
        LOG.error("Security problem accessing matches(String) method of class org.apache.xerces.utils.regex.RegularExpression", e);
      } catch (NoSuchMethodException e) {
        LOG.error("Method matches(String) of class org.apache.xerces.utils.regex.RegularExpression could not be found.", e);
      } catch (IllegalArgumentException e) {
        LOG.error("Invalid argument provided to method matches(String) of class org.apache.xerces.utils.regex.RegularExpression", e);
      } catch (IllegalAccessException e) {
        LOG.error("Illegal acces to method matches(String) of class org.apache.xerces.utils.regex.RegularExpression", e);
      } catch (InvocationTargetException e) {
        LOG.error("Invalid invocation of method matches(String) of class org.apache.xerces.utils.regex.RegularExpression", e);
      }
    }
    return true;
  }
}
