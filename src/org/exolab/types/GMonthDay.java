package org.exolab.types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GMonthDay extends DateTimeBase {
  private static final long serialVersionUID = -6351252242146921258L;
  private static final String MONTHDAY_FORMAT = "--MM-dd";
  private static final String BAD_GMONTHDAY = "Bad gMonthDay format: ";
  public GMonthDay() {}
  public GMonthDay(short month, short day) {
    setMonth(month);
    setDay(day);
  }
  public GMonthDay(int month, int day)
  {
    setMonth((short)month);
    setDay((short)day);
  }
  
  public GMonthDay(short[] values)
  {
    setValues(values);
  }
  
  public GMonthDay(String gmonthDay)
    throws ParseException
  {
    parseGMonthDayInternal(gmonthDay, this);
  }
  
  public void setValues(short[] values)
  {
    if (values.length != 2) {
      throw new IllegalArgumentException("GMonthDay#setValues: not the right number of values");
    }
    setMonth(values[0]);
    setDay(values[1]);
  }
  
  public short[] getValues()
  {
    short[] result = new short[2];
    result[0] = getMonth();
    result[1] = getDay();
    return result;
  }
  
  public Date toDate()
  {
    SimpleDateFormat df = new SimpleDateFormat("--MM-dd");
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
    
    result.append("-");
    if (getDay() / 10 == 0) {
      result.append(0);
    }
    result.append(getDay());
    
    appendTimeZoneString(result);
    
    return result.toString();
  }
  
  public static Object parse(String str)
    throws ParseException
  {
    return parseGMonthDay(str);
  }
  
  public static GMonthDay parseGMonthDay(String str)
    throws ParseException
  {
    return parseGMonthDayInternal(str, null);
  }
  
  private static GMonthDay parseGMonthDayInternal(String str, GMonthDay result)
    throws ParseException
  {
    if (str == null) {
      throw new IllegalArgumentException("The string to be parsed must not be null.");
    }
    if (result == null) {
      result = new GMonthDay();
    }
    char[] chars = str.toCharArray();
    
    int idx = 0;
    if ((chars[idx] != '-') || (chars[(idx + 1)] != '-')) {
      throw new ParseException("Bad gMonthDay format: " + str + "\nA gMonthDay must follow the pattern --MM-DD(Z|((+|-)hh:mm)).", 0);
    }
    idx += 2;
    if ((!Character.isDigit(chars[idx])) || (!Character.isDigit(chars[(idx + 1)]))) {
      throw new ParseException("Bad gMonthDay format: " + str + "\nThe Month must be 2 digits long", idx);
    }
    short value1 = (short)((chars[idx] - '0') * 10 + (chars[(idx + 1)] - '0'));
    result.setMonth(value1);
    
    idx += 2;
    if (chars[idx] != '-') {
      throw new ParseException("Bad gMonthDay format: " + str + "\nA gMonthDay must follow the pattern --MM-DD(Z|((+|-)hh:mm)).", 0);
    }
    idx++;
    if ((!Character.isDigit(chars[idx])) || (!Character.isDigit(chars[(idx + 1)]))) {
      throw new ParseException("Bad gMonthDay format: " + str + "\nThe Day must be 2 digits long", idx);
    }
    value1 = (short)((chars[idx] - '0') * 10 + (chars[(idx + 1)] - '0'));
    result.setDay(value1);
    
    idx += 2;
    
    parseTimeZone(str, result, chars, idx, "Bad gMonthDay format: ");
    
    return result;
  }
  
  public boolean hasIsNegative()
  {
    return false;
  }
  
  public boolean isNegative()
  {
    String err = "org.exolab.types.GMonthDay does not have a 'negative' field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setNegative()
  {
    String err = "org.exolab.types.GMonthDay cannot be negative.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasCentury()
  {
    return false;
  }
  
  public short getCentury()
  {
    String err = "org.exolab.types.GMonthDay does not have a Century field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setCentury(short century)
  {
    String err = "org.exolab.types.GMonthDay does not have a Century field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasYear()
  {
    return false;
  }
  
  public short getYear()
  {
    String err = "org.exolab.types.GMonthDay does not have a Year field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setYear(short year)
  {
    String err = "org.exolab.types.GMonthDay does not have a Year field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasHour()
  {
    return false;
  }
  
  public short getHour()
  {
    String err = "org.exolab.types.GMonthDay does not have an Hour field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setHour(short hour)
  {
    String err = "org.exolab.types.GMonthDay does not have an Hour field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasMinute()
  {
    return false;
  }
  
  public short getMinute()
  {
    String err = "org.exolab.types.GMonthDay does not have a Minute field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setMinute(short minute)
  {
    String err = "org.exolab.types.GMonthDay does not have a Minute field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasSeconds()
  {
    return false;
  }
  
  public short getSeconds()
  {
    String err = "org.exolab.types.GMonthDay does not have a Seconds field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setSecond(short second)
  {
    String err = "org.exolab.types.GMonthDay does not have a Seconds field.";
    throw new UnsupportedOperationException(err);
  }
  
  public boolean hasMilli()
  {
    return false;
  }
  
  public short getMilli()
  {
    String err = "org.exolab.types.GMonthDay does not have a Milliseconds field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setMilliSecond(short millisecond)
  {
    String err = "org.exolab.types.GMonthDay does not have a Milliseconds field.";
    throw new UnsupportedOperationException(err);
  }
}
