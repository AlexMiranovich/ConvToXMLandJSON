package org.exolab.types;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Date extends DateTimeBase {
  private static final long serialVersionUID = -1634875709019365137L;
  private static final String BAD_DATE = "Bad Date format: ";
  public Date() {}
  public Date(short[] values)
  {
    setValues(values);
  }
  public Date(long dateAsLong)
  {
    this(new java.util.Date(dateAsLong));
  }
  public Date(java.util.Date dateRef) {
    GregorianCalendar tempCalendar = new GregorianCalendar();
    tempCalendar.setTime(dateRef);
    setCentury((short)(tempCalendar.get(1) / 100));
    setYear((short)(tempCalendar.get(1) % 100));
    setMonth((short)(tempCalendar.get(2) + 1));
    setDay((short)tempCalendar.get(5));
  }
  public Date(String date)
    throws ParseException {
    parseDateInternal(date, this);
  }
  public void setValues(short[] values) {
    if (values.length != 4) {
      throw new IllegalArgumentException("Date#setValues: not the right number of values");
    }
    setCentury(values[0]);
    setYear(values[1]);
    setMonth(values[2]);
    setDay(values[3]);
  }
  public short[] getValues() {
    short[] result = new short[4];
    result[0] = getCentury();
    result[1] = getYear();
    result[2] = getMonth();
    result[3] = getDay();
    return result;
  }
  public java.util.Date toDate() {
    Calendar calendar = new GregorianCalendar(getCentury() * 100 + getYear(), getMonth() - 1, getDay());
    setDateFormatTimeZone(calendar);
    return calendar.getTime();
  }
  public long toLong()
  {
    return toDate().getTime();
  }
  public String toString() {
    StringBuffer result = new StringBuffer();
    appendDateString(result);
    appendTimeZoneString(result);
    return result.toString();
  }
  public static Object parse(String str)
    throws ParseException {
    return parseDate(str);
  }
  public static Date parseDate(String str)
    throws ParseException {
    Date result = new Date();
    return parseDateInternal(str, result);
  }
  private static Date parseDateInternal(String str, Date result)
    throws ParseException {
    if (str == null) {
      throw new IllegalArgumentException("The string to be parsed must not be null.");
    }
    if (result == null) {
      result = new Date();
    }
    char[] chars = str.toCharArray();
    int idx = 0;
    if (chars[idx] == '-') {
      idx++;
      result.setNegative();
    }
    if ((!Character.isDigit(chars[idx])) || (!Character.isDigit(chars[(idx + 1)])) || (!Character.isDigit(chars[(idx + 2)])) || (!Character.isDigit(chars[(idx + 3)]))) {
      throw new ParseException("Bad Date format: '" + str + "'\nThe Year must be 4 digits long", idx);
    }
    short value1 = (short)((chars[idx] - '0') * 10 + (chars[(idx + 1)] - '0'));
    short value2 = (short)((chars[(idx + 2)] - '0') * 10 + (chars[(idx + 3)] - '0'));
    if ((value1 == 0) && (value2 == 0)) {
      throw new ParseException("Bad Date format: " + str + "\n'0000' is not allowed as a year.", idx);
    }
    result.setCentury(value1);
    result.setYear(value2);
    idx += 4;
    if (chars[idx] != '-') {
      throw new ParseException("Bad Date format: '" + str + "'\n '-' " + " is wrongly placed.", idx);
    }
    idx++;
    if ((!Character.isDigit(chars[idx])) || (!Character.isDigit(chars[(idx + 1)]))) {
      throw new ParseException("Bad Date format: '" + str + "'\nThe Month must be 2 digits long", idx);
    }
    value1 = (short)((chars[idx] - '0') * 10 + (chars[(idx + 1)] - '0'));
    result.setMonth(value1);
    idx += 2;
    if (chars[idx] != '-') {
      throw new ParseException("Bad Date format: '" + str + "'\n '-' " + " is wrongly placed.", idx);
    }
    idx++;
    if ((!Character.isDigit(chars[idx])) || (!Character.isDigit(chars[(idx + 1)]))) {
      throw new ParseException("Bad Date format: '" + str + "'\nThe Day must be 2 digits long", idx);
    }
    value1 = (short)((chars[idx] - '0') * 10 + (chars[(idx + 1)] - '0'));
    result.setDay(value1);
    idx += 2;
    parseTimeZone(str, result, chars, idx, "Bad Date format: ");
    
    return result;
  }
  public boolean hasHour()
  {
    return false;
  }
  public short getHour() {
    String err = "org.exolab.types.Date does not have an Hour field.";
    throw new UnsupportedOperationException(err);
  }
  public void setHour(short hour) {
    String err = "org.exolab.types.Date does not have an Hour field.";
    throw new UnsupportedOperationException(err);
  }
  public boolean hasMinute()
  {
    return false;
  }
  public short getMinute() {
    String err = "org.exolab.types.Date does not have a Minute field.";
    throw new UnsupportedOperationException(err);
  }
  public void setMinute(short minute) {
    String err = "org.exolab.types.Date does not have a Minute field.";
    throw new UnsupportedOperationException(err);
  }
  public boolean hasSeconds()
  {
    return false;
  }
  
  public short getSeconds()
  {
    String err = "org.exolab.types.Date does not have a Seconds field.";
    throw new UnsupportedOperationException(err);
  }
  
  public void setSecond(short second)
  {
    String err = "org.exolab.types.Date does not have a Seconds field.";
    throw new UnsupportedOperationException(err);
  }
  public boolean hasMilli()
  {
    return false;
  }
  public short getMilli() {
    String err = "org.exolab.types.Date does not have a Milliseconds field.";
    throw new UnsupportedOperationException(err);
  }
  public void setMilliSecond(short millisecond) {
    String err = "org.exolab.types.Date does not have a Milliseconds field.";
    throw new UnsupportedOperationException(err);
  }
}
