package org.exolab.types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.StringTokenizer;
import java.util.TimeZone;


public class Month extends TimePeriod {
  private static final long serialVersionUID = 8102039626686892932L;
  private static final boolean DEBUG = false;
  private static final String MONTH_FORMAT = "yyyy-MM";
  public Month() {
    super("P1M");
    int temp = TimeZone.getDefault().getRawOffset();
    if (temp < 0) {
      temp = -temp;
      try
      {
        super.setZoneNegative();
      }
      catch (UnsupportedOperationException e) {}
    }
    short zhour = (short)(temp / 3600000);
    temp %= 3600000;
    short zmin = (short)(temp / 60000);
    try
    {
      super.setZone(zhour, zmin);
    }
    catch (UnsupportedOperationException e) {}
  }
  
  public void setDay(short day)
    throws UnsupportedOperationException
  {
    String err = "In a Month : the day field must not be changed";
    throw new UnsupportedOperationException(err);
  }
  
  public void setHour(short hour)
    throws UnsupportedOperationException
  {
    String err = "In a Month : the hour field must not be changed";
    throw new UnsupportedOperationException(err);
  }
  
  public void setMinute(short minute)
    throws UnsupportedOperationException
  {
    String err = "In a Month : the minute field must not be changed";
    throw new UnsupportedOperationException(err);
  }
  
  public void setSecond(short second, short millsecond)
    throws UnsupportedOperationException
  {
    String err = "In a Month : the second fields must not be changed";
    throw new UnsupportedOperationException(err);
  }
  
  public void setZone(short hour, short minute)
    throws UnsupportedOperationException
  {
    String err = "In a Month : the time zone fields must not be changed";
    throw new UnsupportedOperationException(err);
  }
  
  public void setZoneNegative()
    throws UnsupportedOperationException
  {
    String err = "In a Month : the tinme zone fields must not be changed";
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
    
    result.append('-');
    if (getMonth() / 10 == 0) {
      result.append(0);
    }
    result.append(getMonth());
    if (isNegative()) {
      result.insert(0, '-');
    }
    return result.toString();
  }
  
  public static Object parse(String str)
    throws ParseException
  {
    return parseMonth(str);
  }
  
  public static Month parseMonth(String str)
    throws ParseException
  {
    Month result = new Month();
    if (str.startsWith("-")) {
      result.setNegative();
    }
    StringTokenizer token = new StringTokenizer(str, "-");
    if (token.countTokens() != 2) {
      throw new ParseException(str + ": Bad XML Schema Month type format (CCYY-MM)", 0);
    }
    String temp = token.nextToken();
    if (temp.length() != 4) {
      throw new ParseException(str + ": Bad year format", 1);
    }
    result.setCentury(Short.parseShort(temp.substring(0, 2)));
    try
    {
      result.setYear(Short.parseShort(temp.substring(2, 4)));
    }
    catch (UnsupportedOperationException e) {}
    temp = token.nextToken();
    if (temp.length() != 2) {
      throw new ParseException(str + ": Bad month format", 5);
    }
    try
    {
      result.setMonth(Short.parseShort(temp));
    }
    catch (UnsupportedOperationException e) {}
    temp = null;
    return result;
  }
  
  public Date toDate()
    throws ParseException
  {
    Date date = null;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
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
