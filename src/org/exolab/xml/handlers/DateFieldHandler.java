package org.exolab.xml.handlers;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Vector;
import org.exolab.mapping.FieldHandler;
import org.exolab.types.Date;
import org.exolab.types.DateTime;
import org.exolab.xml.XMLFieldHandler;

public class DateFieldHandler extends XMLFieldHandler {
  private static final byte DEFAULT_DATE_LENGTH = 25;
  private static final String INVALID_DATE = "Invalid dateTime format: ";
  private static final ParseOptions DEFAULT_PARSE_OPTIONS = new ParseOptions();
  private static TimeZone _timezone = TimeZone.getDefault();
  private static boolean _allowTimeZoneSuppression = false;
  private static boolean _suppressMillis = false;
  private final FieldHandler _handler;
  private ParseOptions _options = new ParseOptions();
  private boolean _useSQLDate = false;
  public DateFieldHandler(FieldHandler fieldHandler) {
    if (fieldHandler == null) {
      String err = "The FieldHandler argument passed to the constructor of DateFieldHandler must not be null.";
      throw new IllegalArgumentException(err);
    }
    this._handler = fieldHandler;
  }
  public Object getValue(Object target) {
    Object val = this._handler.getValue(target);
    if (val == null) {
      return val;
    }
    Object formatted = null;
    Class type = val.getClass();
    if (java.util.Date.class.isAssignableFrom(type)) {
      formatted = format((java.util.Date)val);
    }
    else if (type.isArray()) {
      int size = Array.getLength(val);
      String[] values = new String[size];
      for (int i = 0; i < size; i++) {
        values[i] = format(Array.get(val, i));
      }
      formatted = values;
    }
    else if (Enumeration.class.isAssignableFrom(type)) {
      Enumeration enumeration = (Enumeration)val;
      Vector values = new Vector();
      while (enumeration.hasMoreElements()) {
        values.addElement(format(enumeration.nextElement()));
      }
      String[] valuesArray = new String[values.size()];
      values.copyInto(valuesArray);
      formatted = valuesArray;
    }
    else {
      formatted = val.toString();
    }
    return formatted;
  }
  public void setValue(Object target, Object value)
    throws IllegalStateException {
    java.util.Date date = null;
    if ((value == null) || ((value instanceof java.util.Date))) {
      date = (java.util.Date)value;
    } else {
      try {
        date = parse(value.toString(), this._options);
        if ((this._useSQLDate) && (date != null)) {
          date = new java.sql.Date(date.getTime());
        }
      }
      catch (ParseException px) {
        throw new IllegalStateException(px.getMessage());
      }
    }
    this._handler.setValue(target, date);
  }
  public void resetValue(Object target)
    throws IllegalStateException {
    this._handler.resetValue(target);
  }
  public Object newInstance(Object parent)
    throws IllegalStateException {
    Object obj = this._handler.newInstance(parent);
    if (obj == null) {
      obj = new java.util.Date();
    }
    return obj;
  }
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof FieldHandler)) {
      return false;
    }
    return (this._handler.getClass().isInstance(obj));
  }
  public static void setAllowTimeZoneSuppression(boolean allowTimeZoneSuppression) {
    _allowTimeZoneSuppression = allowTimeZoneSuppression;
  }
  public static void setDefaultTimeZone(TimeZone timeZone) {
    if (timeZone == null) {
      _timezone = TimeZone.getDefault();
    } else {
      _timezone = (TimeZone)timeZone.clone();
    }
  }
  public static void setSuppressMillis(boolean suppressMillis)
  {
    _suppressMillis = suppressMillis;
  }
  public void setUseSQLDate(boolean useSQLDate) {
    this._useSQLDate = useSQLDate;
    this._options._allowNoTime = this._useSQLDate;
  }
  protected static java.util.Date parse(String dateTime)
    throws ParseException {
    return parse(dateTime, DEFAULT_PARSE_OPTIONS);
  }
  protected static java.util.Date parse(String dateTime, ParseOptions options)
    throws ParseException {
    if (dateTime == null) {
      throw new ParseException("Invalid dateTime format: null", 0);
    }
    ParseOptions pOptions = options != null ? options : DEFAULT_PARSE_OPTIONS;
    String trimmed = dateTime.trim();
    if ((pOptions._allowNoTime) && (trimmed.indexOf('T') == -1)) {
      Date parsedDate = new Date(trimmed);
      return parsedDate.toDate();
    }
    DateTime parsedDateTime = new DateTime(trimmed);
    return parsedDateTime.toDate();
  }
  protected static String format(java.util.Date date) {
    SimpleDateFormat formatter;
    if (_suppressMillis) {
      formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    } else {
      formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    }
    formatter.setTimeZone(_timezone);
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(date);
    cal.setTimeZone(_timezone);
    StringBuffer buffer = new StringBuffer(25);
    if (cal.get(0) == 0) {
      buffer.append("-");
    }
    buffer.append(formatter.format(date));
    formatTimeZone(cal, buffer);
    return buffer.toString();
  }
  private static void formatTimeZone(Calendar cal, StringBuffer buffer) {
    int value = cal.get(15);
    int dstOffset = cal.get(16);
    if ((value == 0) && (dstOffset == 0)) {
      buffer.append('Z');
      return;
    }
    if ((_allowTimeZoneSuppression) && (value == _timezone.getRawOffset())) {
      return;
    }
    value += dstOffset;
    if (value > 0) {
      buffer.append('+');
    }
    else {
      value = -value;
      buffer.append('-');
    }
    int minutes = value / 60000;
    value = minutes / 60;
    if (value < 10) {
      buffer.append('0');
    }
    buffer.append(value);
    buffer.append(':');
    value = minutes % 60;
    if (value < 10) {
      buffer.append('0');
    }
    buffer.append(value);
  }
  private static String format(Object object) {
    if (object == null) {
      return null;
    }
    if ((object instanceof java.util.Date)) {
      return format((java.util.Date)object);
    }
    return object.toString();
  }
  static class ParseOptions {
    public boolean _allowNoTime = false;
  }
}
