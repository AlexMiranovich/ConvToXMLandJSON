package org.exolab.types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GMonth extends DateTimeBase {
  private static final long serialVersionUID = -1950758441188466762L;
  private static final String MONTH_FORMAT = "--MM--";
  private static final String BAD_GMONTH = "Bad gMonth format: ";
  public GMonth() {}
  public GMonth(short month)
  {
    setMonth(month);
  }
  public GMonth(int month)
  {
    setMonth((short)month);
  }
  public GMonth(String gmonth)
    throws ParseException {
    parseGMonthInternal(gmonth, this);
  }
  public void setValues(short[] values) {
    if (values.length != 1) {
      throw new IllegalArgumentException("GMonth#setValues: not the right number of values");
    }
    setMonth(values[0]);
  }
  public short[] getValues() {
    short[] result = new short[1];
    result[0] = getMonth();
    return result;
  }
  
  public Date toDate()
  {
    SimpleDateFormat df = new SimpleDateFormat("--MM--");
    setDateFormatTimeZone(df);
    
    Date date = null;
    try
    {
      date = df.parse(toString());
    }
    catch (ParseException e)
    {
      e.printStackTrace();
      return null;
    }
    return date;
  }
  
  public String toString()
  {
    StringBuffer result = new StringBuffer("--");
    if (getMonth() / 10 == 0) {
      result.append(0);
    }
    result.append(getMonth());
    
    result.append("--");
    
    appendTimeZoneString(result);
    
    return result.toString();
  }
  
  public static Object parse(String str)
    throws ParseException
  {
    return parseGMonth(str);
  }
  
  public static GMonth parseGMonth(String str)
    throws ParseException
  {
    GMonth result = new GMonth();
    return parseGMonthInternal(str, result);
  }
  
  private static GMonth parseGMonthInternal(String str, GMonth result)
    throws ParseException
  {
    if (str == null) {
      throw new IllegalArgumentException("The string to be parsed must not be null.");
    }
    if (result == null) {
      result = new GMonth();
    }
    char[] chars = str.toCharArray();
    
    int idx = 0;
    if ((chars[0] != '-') || (chars[1] != '-')) {
      throw new ParseException("Bad gMonth format: " + str + "\nA gMonth must follow the pattern --DD--(Z|((+|-)hh:mm)).", 0);
    }
    idx += 2;
    if ((!Character.isDigit(chars[idx])) || (!Character.isDigit(chars[(idx + 1)]))) {
      throw new ParseException("Bad gMonth format: " + str + "\nThe Month must be 2 digits long", idx);
    }
    short value1 = (short)((chars[idx] - '0') * 10 + (chars[(idx + 1)] - '0'));
    result.setMonth(value1);
    
    idx += 2;
    if ((chars[idx] != '-') || (chars[(idx + 1)] != '-')) {
      throw new ParseException("Bad gMonth format: " + str + "\nA gMonth must follow the pattern --DD--(Z|((+|-)hh:mm)).", 0);
    }
    idx += 2;
    
    parseTimeZone(str, result, chars, idx, "Bad gMonth format: ");
    
    return result;
  }
  
  public boolean hasIsNegative()
  {
    return false;
  }
  
  public boolean isNegative()
  {
    String err = "org.exolab.types.GMonth does not have a 'negative' field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setNegative()
  {
    String err = "org.exolab.types.GMonth cannot be negative.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasCentury()
  {
    return false;
  }
  
  public short getCentury()
  {
    String err = "org.exolab.types.GMonth does not have a Century field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setCentury(short century)
  {
    String err = "org.exolab.types.GMonth does not have a Century field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasYear()
  {
    return false;
  }
  
  public short getYear()
  {
    String err = "org.exolab.types.GMonth does not have a Year field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setYear(short year)
  {
    String err = "org.exolab.types.GMonth does not have a Year field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasDay()
  {
    return false;
  }
  
  public short getDay()
  {
    String err = "org.exolab.types.GMonth does not have a Day field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setDay(short month)
  {
    String err = "org.exolab.types.GMonth does not have a Day field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasHour()
  {
    return false;
  }
  
  public short getHour()
  {
    String err = "org.exolab.types.GMonth does not have an Hour field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setHour(short hour)
  {
    String err = "org.exolab.types.GMonth does not have an Hour field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasMinute()
  {
    return false;
  }
  
  public short getMinute()
  {
    String err = "org.exolab.types.GMonth does not have a Minute field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setMinute(short minute)
  {
    String err = "org.exolab.types.GMonth does not have a Minute field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasSeconds()
  {
    return false;
  }
  
  public short getSeconds()
  {
    String err = "org.exolab.types.GMonth does not have a Seconds field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setSecond(short second)
  {
    String err = "org.exolab.types.GMonth does not have a Seconds field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasMilli()
  {
    return false;
  }
  
  public short getMilli()
  {
    String err = "org.exolab.types.GMonth does not have a Milliseconds field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setMilliSecond(short millisecond)
  {
    String err = "org.exolab..types.GMonth does not have a Milliseconds field.";
    throw new UnsupportedOperationException(err);
  }
}
