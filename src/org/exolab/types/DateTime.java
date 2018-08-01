package org.exolab.types;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateTime extends DateTimeBase {
  private static final long serialVersionUID = 6278590966410879734L;
  private static final String BAD_DATE = "Bad DateTime format: ";
  public DateTime() {}
  public DateTime(short[] values)
  {
    setValues(values);
  }
  public DateTime(long dateAsLong)
  {
    this(new Date(dateAsLong));
  }
  public DateTime(Date dateRef) {
    GregorianCalendar tempCalendar = new GregorianCalendar();
    tempCalendar.setTime(dateRef);
    setCentury((short)(tempCalendar.get(1) / 100));
    setYear((short)(tempCalendar.get(1) % 100));
    setMonth((short)(tempCalendar.get(2) + 1));
    setDay((short)tempCalendar.get(5));
    setHour((short)tempCalendar.get(11));
    setMinute((short)tempCalendar.get(12));
    setSecond((short)tempCalendar.get(13), (short)tempCalendar.get(14));
  }
  public DateTime(String date)
    throws ParseException {
    parseDateTimeInternal(date, this);
  }
  public void setValues(short[] values) {
    if (values.length != 8) {
      throw new IllegalArgumentException("DateTime#setValues: Array length " + values.length + " != 8");
    }
    setCentury(values[0]);
    setYear(values[1]);
    setMonth(values[2]);
    setDay(values[3]);
    setHour(values[4]);
    setMinute(values[5]);
    setSecond(values[6], values[7]);
  }
  public short[] getValues() {
    short[] result = new short[8];
    result[0] = getCentury();
    result[1] = getYear();
    result[2] = getMonth();
    result[3] = getDay();
    result[4] = getHour();
    result[5] = getMinute();
    result[6] = getSeconds();
    result[7] = getMilli();
    return result;
  }
  public Date toDate() {
    Calendar calendar = new GregorianCalendar(getCentury() * 100 + getYear(), getMonth() - 1, getDay(), getHour(), getMinute(), getSeconds());
    calendar.set(14, getMilli());
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
    result.append('T');
    appendTimeString(result);
    appendTimeZoneString(result);
    return result.toString();
  }
  
  public static DateTime parse(String str)
    throws ParseException {
    return parseDateTime(str);
  }
  public static DateTime parseDateTime(String str)
    throws ParseException {
    return parseDateTimeInternal(str, new DateTime());
  }
  private static DateTime parseDateTimeInternal(String str, DateTime result)
    throws ParseException {
    if (str == null) {
      throw new IllegalArgumentException("The string to be parsed must not be null.");
    }
    if (result == null) {
      result = new DateTime();
    }
    char[] chars = str.toCharArray();
    if (chars.length < 19) {
      throw new ParseException("Bad DateTime format: " + str + "\nDateTime is not long enough", 0);
    }
    int idx = 0;
    idx = parseYear(str, result, chars, idx, "Bad DateTime format: ");
    idx = parseMonth(str, result, chars, idx, "Bad DateTime format: ");
    idx = parseDay(str, result, chars, idx, "Bad DateTime format: ");
    if (chars[idx] != 'T') {
      throw new ParseException("Bad DateTime format: " + str + "\n 'T' " + " is wrongly placed.", idx);
    }
    idx++;
    
    idx = parseTime(str, result, chars, idx, "Bad DateTime format: ");
    parseTimeZone(str, result, chars, idx, "Bad DateTime format: ");
    
    return result;
  }
}
