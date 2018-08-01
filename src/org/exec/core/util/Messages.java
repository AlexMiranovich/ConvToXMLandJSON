package org.exec.core.util;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class Messages {
  private static final Log LOG = LogFactory.getLog(Messages.class);
  public static final String RESOURCE_NAME = "org.exec.messages";
  private static ResourceBundle _messages;
  private static Hashtable _formats;
  static {
    setDefaultLocale();
  }

  public static void setDefaultLocale()
  {
    setLocale(Locale.getDefault());
  }
  public static void setLocale(Locale locale) {
    try {
      _messages = ResourceBundle.getBundle("org.exec.messages", locale);
    } catch (Exception except) {
      _messages = new EmptyResourceBundle(null);
      LOG.error("Failed to locate messages resource org.exec.messages");
    }
    _formats = new Hashtable();
  }
  public static String format(String message, Object arg1)
  {
    return format(message, new Object[] { arg1 });
  }
  public static String format(String message, Object arg1, Object arg2) {
    return format(message, new Object[] { arg1, arg2 });
  }
  public static String format(String message, Object arg1, Object arg2, Object arg3) {
    return format(message, new Object[] { arg1, arg2, arg3 });
  }
  public static String format(String message, Object[] args) {
    try {
      MessageFormat mf = (MessageFormat)_formats.get(message);
      if (mf == null) {
        String msg;
        try {
          msg = _messages.getString(message);
        } catch (MissingResourceException except) {
          return message;
        }
        mf = new MessageFormat(msg);
        _formats.put(message, mf);
      }
      return mf.format(args);
    } catch (Exception except) {}
    return "An internal error occured while processing message " + message;
  }
  public static String message(String message) {
    try
    {
      return _messages.getString(message);
    } catch (MissingResourceException except) {}
    return message;
  }
  private static class EmptyResourceBundle
    extends ResourceBundle
    implements Enumeration {
    public EmptyResourceBundle(Object o) {
    }

    public Enumeration getKeys()
    {
      return this;
    }
    protected Object handleGetObject(String name)
    {
      return "[Missing message " + name + "]";
    }
    public boolean hasMoreElements()
    {
      return false;
    }
    public Object nextElement()
    {
      return null;
    }
  }
}
