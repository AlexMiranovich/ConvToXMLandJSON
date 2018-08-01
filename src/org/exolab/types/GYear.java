package org.exolab.types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GYear extends DateTimeBase {
  private static final long serialVersionUID = -8977039151222106864L;
  private static final String YEAR_FORMAT = "yyyy";
  private static final String BAD_GYEAR = "Bad gYear format: ";
  public GYear() {}
  public GYear(short century, short year) {
    setCentury(century);
    setYear(year);
  }
  public GYear(int year) {
    short century = (short)(year / 100);
    year %= 100;
    setCentury(century);
    setYear((short)year);
  }
  public GYear(short[] values)
  {
    setValues(values);
  }
  public GYear(String gyear)
    throws ParseException {
    parseGYearInternal(gyear, this);
  }
  
  public void setValues(short[] values)
  {
    if (values.length != 2) {
      throw new IllegalArgumentException("GYear#setValues: not the right number of values");
    }
    setCentury(values[0]);
    setYear(values[1]);
  }
  
  public short[] getValues()
  {
    short[] result = new short[2];
    result[0] = getCentury();
    result[1] = getYear();
    return result;
  }
  
  public Date toDate()
  {
    SimpleDateFormat df = new SimpleDateFormat("yyyy");
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
    
    appendTimeZoneString(result);
    
    return result.toString();
  }
  
  public static Object parse(String str)
    throws ParseException
  {
    return parseGYear(str);
  }
  
  public static GYear parseGYear(String str)
    throws ParseException
  {
    return parseGYearInternal(str, null);
  }
  
  private static GYear parseGYearInternal(String str, GYear result)
    throws ParseException
  {
    if (str == null) {
      throw new IllegalArgumentException("The string to be parsed must not be null.");
    }
    if (result == null) {
      result = new GYear();
    }
    char[] chars = str.toCharArray();
    
    int idx = 0;
    if (chars[idx] == '-')
    {
      result.setNegative();
      idx++;
    }
    if ((!Character.isDigit(chars[idx])) || (!Character.isDigit(chars[(idx + 1)])) || (!Character.isDigit(chars[(idx + 2)])) || (!Character.isDigit(chars[(idx + 3)]))) {
      throw new ParseException("Bad gYear format: " + str + "\nA gYear must follow the pattern CCYY(Z|((+|-)hh:mm)).", idx);
    }
    short value1 = (short)((chars[idx] - '0') * 10 + (chars[(idx + 1)] - '0'));
    short value2 = (short)((chars[(idx + 2)] - '0') * 10 + (chars[(idx + 3)] - '0'));
    if ((value1 == 0) && (value2 == 0)) {
      throw new ParseException("Bad gYear format: " + str + "\n'0000' is not allowed as a year.", idx);
    }
    result.setCentury(value1);
    result.setYear(value2);
    
    idx += 4;
    
    parseTimeZone(str, result, chars, idx, "Bad gYear format: ");
    
    return result;
  }
  
  public boolean hasMonth()
  {
    return false;
  }
  
  public short getMonth()
  {
    String err = "org.exolab.types.GYear does not have a Month field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setMonth(short year)
  {
    String err = "org.exolab.types.GYear does not have a Month field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasDay()
  {
    return false;
  }
  
  public short getDay()
  {
    String err = "org.exolab.types.GYear does not have a Day field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setDay(short month)
  {
    String err = "org.exolab.types.GYear does not have a Day field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasHour()
  {
    return false;
  }
  
  public short getHour()
  {
    String err = "org.exolab.types.GYear does not have an Hour field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setHour(short hour)
  {
    String err = "org.exolab.types.GYear does not have an Hour field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasMinute()
  {
    return false;
  }
  
  public short getMinute()
  {
    String err = "org.exolab.types.GYear does not have a Minute field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setMinute(short minute)
  {
    String err = "org.exolab.types.GYear does not have a Minute field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasSeconds()
  {
    return false;
  }
  
  public short getSeconds()
  {
    String err = "org.exolab.types.GYear does not have a Seconds field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setSecond(short second)
  {
    String err = "org.exolab.types.GYear does not have a Seconds field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasMilli()
  {
    return false;
  }
  
  public short getMilli()
  {
    String err = "org.exolab.types.GYear does not have a Milliseconds field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setMilliSecond(short millisecond)
  {
    String err = "org.exolab.types.GYear does not have a Milliseconds field.";
    throw new UnsupportedOperationException(err);
  }
}
