package org.exolab.types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;


public class Year extends TimePeriod {
  private static final long serialVersionUID = 8485456751196062574L;
  private static final boolean DEBUG = false;
  private static final String YEAR_FORMAT = "yyyy";
  public Year() {
    super("P1Y");
    int temp = TimeZone.getDefault().getRawOffset();
    if (temp < 0) {
      temp = -temp;
      try
      {
        setZoneNegative();
      }
      catch (UnsupportedOperationException e) {}
    }
    short zhour = (short)(temp / 3600000);
    temp %= 3600000;
    short zmin = (short)(temp / 60000);
    try
    {
      setZone(zhour, zmin);
    }
    catch (UnsupportedOperationException e) {}
  }
  
  public void setMonth(short month)
    throws UnsupportedOperationException
  {
    String err = "In a Year : the month field must not be changed";
    throw new UnsupportedOperationException(err);
  }
  
  public void setDay(short day)
    throws UnsupportedOperationException
  {
    String err = "In a Year : the day field must not be changed";
    throw new UnsupportedOperationException(err);
  }
  
  public void setHour(short hour)
    throws UnsupportedOperationException
  {
    String err = "In a Year : the hour field must not be changed";
    throw new UnsupportedOperationException(err);
  }
  
  public void setMinute(short minute)
    throws UnsupportedOperationException
  {
    String err = "In a Year : the minute field must not be changed";
    throw new UnsupportedOperationException(err);
  }
  
  public void setSecond(short second, short millsecond)
    throws UnsupportedOperationException
  {
    String err = "In a Year : the second fields must not be changed";
    throw new UnsupportedOperationException(err);
  }
  
  public void setZone(short hour, short minute)
    throws UnsupportedOperationException
  {
    String err = "In a Year : the time zone fields must not be changed";
    throw new UnsupportedOperationException(err);
  }
  
  public void setZoneNegative()
    throws UnsupportedOperationException
  {
    String err = "In a Year : the time zone fields must not be changed";
    throw new UnsupportedOperationException(err);
  }
  
  public String toString()
  {
    StringBuffer result = new StringBuffer();
    result.append(getCentury());
    if (result.length() == 1) {
      result.insert(0, 0);
    }
    if (getYear() / 10 == 0) {
      result.append(0);
    }
    result.append(getYear());
    if (isNegative()) {
      result.insert(0, '-');
    }
    return result.toString();
  }
  
  public static Object parse(String str)
    throws ParseException
  {
    return parseYear(str);
  }
  
  public static Year parseYear(String str)
    throws ParseException
  {
    Year result = new Year();
    if (str.startsWith("-"))
    {
      result.setNegative();
      str = str.substring(1);
    }
    if (str.length() != 4) {
      throw new ParseException(str + ": Bad XML Schema Year type format (CCYY)", 0);
    }
    result.setCentury(Short.parseShort(str.substring(0, 2)));
    try
    {
      result.setYear(Short.parseShort(str.substring(2, 4)));
    }
    catch (UnsupportedOperationException e) {}
    return result;
  }
  
  public Date toDate()
    throws ParseException
  {
    Date date = null;
    SimpleDateFormat df = new SimpleDateFormat("yyyy");
    SimpleTimeZone timeZone = new SimpleTimeZone(0, "UTC");
    if (!isUTC())
    {
      int offset = 0;
      offset = (getZoneMinute() + getZoneHour() * 60) * 60 * 1000;
      offset = isZoneNegative() ? -offset : offset;
      timeZone.setRawOffset(offset);
      timeZone.setID(TimeZone.getAvailableIDs(offset)[0]);
    }
    df.setTimeZone(timeZone);
    date = df.parse(toString());
    return date;
  }
}
