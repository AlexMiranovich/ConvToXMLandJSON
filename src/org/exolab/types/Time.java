package org.exolab.types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Time extends DateTimeBase {
  private static final long serialVersionUID = -8268707778437931489L;
  private static final String TIME_FORMAT_MILLI = "HH:mm:ss.SSS";
  private static final String TIME_FORMAT_NO_MILLI = "HH:mm:ss";
  private static final String BAD_TIME = "Bad Time format: ";
  public Time() {}
  public Time(short[] values)
  {
    setValues(values);
  }
  public Time(long l)
  {
    this(l, false);
  }
  public Time(long l, boolean utc) {
    if (l > 86400000L) {
      throw new IllegalArgumentException("Bad Time: the long value can't represent more than 24h.");
    }
    setHour((short)(int)(l / 3600000L));
    l %= 3600000L;
    setMinute((short)(int)(l / 60000L));
    l %= 60000L;
    setSecond((short)(int)(l / 1000L), (short)(int)(l % 1000L));
    if (utc) {
      setZone((short)0, (short)0);
    }
  }
  public Time(String time)
    throws ParseException {
    parseTimeInternal(time, this);
  }
  public void setValues(short[] values) {
    if (values.length != 4) {
      throw new IllegalArgumentException("Time#setValues: not the right number of values");
    }
    setHour(values[0]);
    setMinute(values[1]);
    setSecond(values[2], values[3]);
  }
  public short[] getValues() {
    short[] result = new short[4];
    result[0] = getHour();
    result[1] = getMinute();
    result[2] = getSeconds();
    result[3] = getMilli();
    return result;
  }
  public long toLong() {
    int sign = isZoneNegative() ? 1 : -1;
    int hour = getHour() + sign * getZoneHour();
    int minute = getMinute() + sign * getZoneMinute();
    int second = getSeconds();
    int milli = getMilli();
    if (minute < 0) {
      minute += 60;
      hour -= 1;
    }
    else if (minute > 59) {
      minute -= 60;
      hour += 1;
    }
    if (hour < 0) {
      hour += 24;
    } else if (hour > 23) {
      hour -= 24;
    }
    return 3600000 * hour + 60000 * minute + 1000 * second + milli;
  }
  public Date toDate() {
    Date date = null;
    SimpleDateFormat df = null;
    String temp = toString();
    if (temp.indexOf('.') > 0) {
      df = new SimpleDateFormat("HH:mm:ss.SSS");
    } else {
      df = new SimpleDateFormat("HH:mm:ss");
    }
    setDateFormatTimeZone(df);
    try {
      date = df.parse(temp);
    }
    catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
    return date;
  }
  public String toString() {
    StringBuffer result = new StringBuffer();
    appendTimeString(result);
    appendTimeZoneString(result);
    return result.toString();
  }
  public static Object parse(String str)
    throws ParseException {
    return parseTime(str);
  }
  public static Time parseTime(String str)
    throws ParseException {
    return parseTimeInternal(str, null);
  }
  private static Time parseTimeInternal(String str, Time result)
    throws ParseException {
    if (str == null) {
      throw new IllegalArgumentException("The string to be parsed must not be null.");
    }
    if (result == null) {
      result = new Time();
    }
    char[] chars = str.toCharArray();
    int idx = parseTime(str, result, chars, 0, "Bad Time format: ");
    parseTimeZone(str, result, chars, idx, "Bad Time format: ");
    
    return result;
  }
  public boolean hasIsNegative()
  {
    return false;
  }
  public boolean isNegative() {
    String err = "org.exolab.types.Time does not have a 'negative' field.";
    throw new UnsupportedOperationException(err);
  }
  public void setNegative() {
    String err = "org.exolab.types.Time cannot be negative.";
    throw new UnsupportedOperationException(err);
  }
  public boolean hasCentury()
  {
    return false;
  }
  public short getCentury() {
    String err = "org.exolab.types.Time does not have a Century field.";
    throw new UnsupportedOperationException(err);
  }
  public void setCentury(short century) {
    String err = "org.exolab.types.Time does not have a Century field.";
    throw new UnsupportedOperationException(err);
  }
  public boolean hasYear()
  {
    return false;
  }
  public short getYear() {
    String err = "org.exolab.types.Time does not have a Year field.";
    throw new UnsupportedOperationException(err);
  }
  public void setYear(short year) {
    String err = "org.exolab.types.Time does not have a Year field.";
    throw new UnsupportedOperationException(err);
  }
  public boolean hasMonth()
  {
    return false;
  }
  public short getMonth() {
    String err = "org.exolab.types.Time does not have a Month field.";
    throw new UnsupportedOperationException(err);
  }
  public void setMonth(short month) {
    String err = "org.exolab.types.Time does not have a Month field.";
    throw new UnsupportedOperationException(err);
  }
  public boolean hasDay()
  {
    return false;
  }
  public short getDay() {
    String err = "org.exolab.types.Time does not have a Day field.";
    throw new UnsupportedOperationException(err);
  }
  public void setDay(short month) {
    String err = "org.exolab.types.Time does not have a Day field.";
    throw new UnsupportedOperationException(err);
  }
}
