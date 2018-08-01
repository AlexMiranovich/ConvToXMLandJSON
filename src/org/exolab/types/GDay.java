package org.exolab.types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GDay extends DateTimeBase {
  private static final long serialVersionUID = 8571596440117087631L;
  private static final String DAY_FORMAT = "---dd";
  private static final String BAD_GDAY = "Bad gDay format: ";
  public GDay() {}
  public GDay(short day)
  {
    setDay(day);
  }
  public GDay(int day)
  {
    setDay((short)day);
  }
  public GDay(String gday)
    throws ParseException {
    parseGDayInternal(gday, this);
  }
  public void setValues(short[] values) {
    if (values.length != 1) {
      throw new IllegalArgumentException("GDay#setValues: not the right number of values");
    }
    setDay(values[0]);
  }
  public short[] getValues() {
    short[] result = new short[1];
    result[0] = getDay();
    return result;
  }
  public Date toDate() {
    SimpleDateFormat df = new SimpleDateFormat("---dd");
    setDateFormatTimeZone(df);
    Date date = null;
    try {
      date = df.parse(toString());
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
    return date;
  }
  public String toString() {
    StringBuffer result = new StringBuffer("---");
    if (getDay() / 10 == 0) {
      result.append(0);
    }
    result.append(getDay());
    appendTimeZoneString(result);
    return result.toString();
  }
  public static Object parse(String str)
    throws ParseException {
    return parseGDay(str);
  }
  public static GDay parseGDay(String str)
    throws ParseException
  {
    GDay result = new GDay();
    return parseGDayInternal(str, result);
  }
  
  private static GDay parseGDayInternal(String str, GDay result)
    throws ParseException
  {
    if (str == null) {
      throw new IllegalArgumentException("The string to be parsed must not be null.");
    }
    if (result == null) {
      result = new GDay();
    }
    char[] chars = str.toCharArray();
    if ((chars[0] != '-') || (chars[1] != '-') || (chars[2] != '-')) {
      throw new ParseException("Bad gDay format: " + str + "\nA gDay must follow the pattern ---DD(Z|((+|-)hh:mm)).", 0);
    }
    int idx = 2;
    idx = parseDay(str, result, chars, idx, "Bad gDay format: ");
    parseTimeZone(str, result, chars, idx, "Bad gDay format: ");
    
    return result;
  }
  
  public boolean hasIsNegative()
  {
    return false;
  }
  
  public boolean isNegative()
  {
    String err = "org.exolab.types.GDay does not have a 'negative' field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setNegative()
  {
    String err = "org.exolab.types.GDay cannot be negative.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasCentury()
  {
    return false;
  }
  
  public short getCentury()
  {
    String err = "org.exolab.types.GDay does not have a Century field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setCentury(short century)
  {
    String err = "org.exolab.types.GDay does not have a Century field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasYear()
  {
    return false;
  }
  
  public short getYear()
  {
    String err = "org.exolab.types.GDay does not have a Year field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setYear(short year)
  {
    String err = "org.exolab.types.GDay does not have a Year field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasMonth()
  {
    return false;
  }
  
  public short getMonth()
  {
    String err = "org.exolab.types.GDay does not have a Month field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setMonth(short month)
  {
    String err = "org.exolab.types.GDay does not have a Month field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasHour()
  {
    return false;
  }
  
  public short getHour()
  {
    String err = "org.exolab.types.GDay does not have an Hour field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setHour(short hour)
  {
    String err = "org.exolab.types.GDay does not have an Hour field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasMinute()
  {
    return false;
  }
  
  public short getMinute()
  {
    String err = "org.exolab.types.GDay does not have a Minute field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setMinute(short minute)
  {
    String err = "org.exolab.types.GDay does not have a Minute field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasSeconds()
  {
    return false;
  }
  
  public short getSeconds()
  {
    String err = "org.exolab.types.GDay does not have a Seconds field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setSecond(short second)
  {
    String err = "org.exolab.types.GDay does not have a Seconds field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasMilli()
  {
    return false;
  }
  
  public short getMilli()
  {
    String err = "org.exolab.types.GDay does not have a Milliseconds field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setMilliSecond(short millisecond)
  {
    String err = "org.exolab.types.GDay does not have a Milliseconds field.";
    throw new UnsupportedOperationException(err);
  }
}
