package org.exolab.types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GYearMonth extends DateTimeBase {
  private static final long serialVersionUID = -8864050276805766473L;
  private static final String YEARMONTH_FORMAT = "yyyy-MM";
  private static final String BAD_GYEARMONTH = "Bad gYearMonth format: ";
  public GYearMonth() {}
  public GYearMonth(short century, short year, short month)
  {
    setCentury(century);
    setYear(year);
    setMonth(month);
  }
  
  public GYearMonth(int year, int month)
  {
    short century = (short)(year / 100);
    year %= 100;
    setCentury(century);
    setYear((short)year);
    setMonth((short)month);
  }
  
  public GYearMonth(short[] values)
  {
    setValues(values);
  }
  
  public GYearMonth(String gyearMonth)
    throws ParseException
  {
    parseGYearMonthInternal(gyearMonth, this);
  }
  
  public void setValues(short[] values)
  {
    if (values.length != 3) {
      throw new IllegalArgumentException("GYearMonth#setValues: not the right number of values");
    }
    setCentury(values[0]);
    setYear(values[1]);
    setMonth(values[2]);
  }
  
  public short[] getValues()
  {
    short[] result = new short[3];
    result[0] = getCentury();
    result[1] = getYear();
    result[2] = getMonth();
    return result;
  }
  
  public Date toDate()
  {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
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
    StringBuffer result = new StringBuffer();
    if (isNegative()) {
      result.append('-');
    }
    if (getCentury() / 10 == 0) {
      result.append(0);
    }
    result.append(getCentury());
    if (getYear() / 10 == 0) {
      result.append(0);
    }
    result.append(getYear());
    
    result.append('-');
    if (getMonth() / 10 == 0) {
      result.append(0);
    }
    result.append(getMonth());
    
    appendTimeZoneString(result);
    
    return result.toString();
  }
  
  public static Object parse(String str)
    throws ParseException
  {
    return parseGYearMonth(str);
  }
  
  public static GYearMonth parseGYearMonth(String str)
    throws ParseException
  {
    GYearMonth result = new GYearMonth();
    return parseGYearMonthInternal(str, result);
  }
  
  private static GYearMonth parseGYearMonthInternal(String str, GYearMonth result)
    throws ParseException
  {
    if (str == null) {
      throw new IllegalArgumentException("The string to be parsed must not be null.");
    }
    if (result == null) {
      result = new GYearMonth();
    }
    char[] chars = str.toCharArray();
    int idx = 0;
    if (chars[idx] == '-')
    {
      result.setNegative();
      idx++;
    }
    if ((!Character.isDigit(chars[idx])) || (!Character.isDigit(chars[(idx + 1)])) || (!Character.isDigit(chars[(idx + 2)])) || (!Character.isDigit(chars[(idx + 3)]))) {
      throw new ParseException("Bad gYearMonth format: " + str + "\nA gYearMonth must follow the pattern CCYY(Z|((+|-)hh:mm)).", idx);
    }
    short value1 = (short)((chars[idx] - '0') * 10 + (chars[(idx + 1)] - '0'));
    short value2 = (short)((chars[(idx + 2)] - '0') * 10 + (chars[(idx + 3)] - '0'));
    if ((value1 == 0) && (value2 == 0)) {
      throw new ParseException("Bad gYearMonth format: " + str + "\n'0000' is not allowed as a year.", idx);
    }
    result.setCentury(value1);
    result.setYear(value2);
    
    idx += 4;
    if (chars[idx] != '-') {
      throw new ParseException("Bad gYearMonth format: " + str + "\nA gYearMonth must follow the pattern CCYY(Z|((+|-)hh:mm)).", idx);
    }
    idx++;
    if ((!Character.isDigit(chars[idx])) || (!Character.isDigit(chars[(idx + 1)]))) {
      throw new ParseException("Bad gYearMonth format: " + str + "\nThe Month must be 2 digits long", idx);
    }
    value1 = (short)((chars[idx] - '0') * 10 + (chars[(idx + 1)] - '0'));
    result.setMonth(value1);
    
    idx += 2;
    
    parseTimeZone(str, result, chars, idx, "Bad gYearMonth format: ");
    
    return result;
  }
  
  public boolean hasDay()
  {
    return false;
  }
  
  public short getDay()
  {
    String err = "org.exolab.types.GYearMonth does not have a Day field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setDay(short day)
  {
    String err = "org.exolab.types.GYearMonth does not have a Day field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasHour()
  {
    return false;
  }
  
  public short getHour()
  {
    String err = "org.exolab.caor.types.GYearMonth does not have an Hour field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setHour(short hour)
  {
    String err = "org.exolab.types.GYearMonth does not have an Hour field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasMinute()
  {
    return false;
  }
  
  public short getMinute()
  {
    String err = "org.exolab.types.GYearMonth does not have a Minute field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setMinute(short minute)
  {
    String err = "org.exolab.types.GYearMonth does not have a Minute field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasSeconds()
  {
    return false;
  }
  
  public short getSeconds()
  {
    String err = "org.exolab.types.GYearMonth does not have a Seconds field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setSecond(short second)
  {
    String err = "org.exolab.types.GYearMonth does not have a Seconds field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasMilli()
  {
    return false;
  }
  
  public short getMilli()
  {
    String err = "org.exolab.types.GYearMonth does not have a Milliseconds field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setMilliSecond(short millisecond)
  {
    String err = "org.exolab.types.GYearMonth does not have a Milliseconds field.";
    throw new UnsupportedOperationException(err);
  }
}
