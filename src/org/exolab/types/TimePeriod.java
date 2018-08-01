package org.exolab.types;

import java.text.ParseException;
import java.util.StringTokenizer;

public class TimePeriod extends RecurringDuration {
  private static final long serialVersionUID = -7057026912711829943L;
  private static final boolean DEBUG = false;
  public TimePeriod()
  {
    super("", "P0Y");
  }
  public TimePeriod(String duration)
  {
    super(duration, "P0Y");
  }
  public void setFields(String str)
    throws ParseException
  {
    if (str.endsWith("Z")) {
      str = str.substring(0, str.indexOf("Z"));
    }
    if (str.startsWith("-")) {
      setNegative();
    }
    String zoneStr = str.substring(str.length() - 6, str.length());
    boolean timeZone = ((zoneStr.lastIndexOf("-") != -1) || (zoneStr.lastIndexOf("+") != -1)) && (zoneStr.lastIndexOf(":") != -1);
    if (!timeZone)
    {
      zoneStr = null;
    }
    else
    {
      int index = str.lastIndexOf("+") != -1 ? str.lastIndexOf("+") : str.lastIndexOf("-");
      
      str = str.substring(0, index);
    }
    if (str.indexOf('T') == -1) {
      throw new ParseException("The 'T' element is required", 0);
    }
    String date = str.substring(0, str.indexOf("T"));
    String time = str.substring(str.indexOf("T"));
    
    StringTokenizer token = new StringTokenizer(date, "-");
    if (token.countTokens() != 3) {
      throw new ParseException(str + ": Bad date format", 0);
    }
    String temp = token.nextToken();
    if (temp.length() != 4) {
      throw new ParseException(str + ":Bad year format", 1);
    }
    setCentury(Short.parseShort(temp.substring(0, 2)));
    try
    {
      setYear(Short.parseShort(temp.substring(2, 4)));
    }
    catch (UnsupportedOperationException e) {}
    temp = token.nextToken();
    if (temp.length() != 2) {
      throw new ParseException(str + ": Bad month format", 5);
    }
    try
    {
      setMonth(Short.parseShort(temp));
    }
    catch (UnsupportedOperationException e) {}
    temp = token.nextToken();
    if (temp.length() != 2) {
      throw new ParseException(str + ":Bad day format", 8);
    }
    try
    {
      setDay(Short.parseShort(temp));
    }
    catch (UnsupportedOperationException e) {}
    token = new StringTokenizer(time, ":");
    if ((token.countTokens() < 3) && (token.countTokens() > 5)) {
      throw new ParseException(str + ": Bad time format", 11);
    }
    temp = token.nextToken();
    temp = temp.substring(temp.indexOf("T") + 1);
    if (temp.length() != 2) {
      throw new ParseException(str + ": Bad hour format", 11);
    }
    try
    {
      setHour(Short.parseShort(temp));
    }
    catch (UnsupportedOperationException e) {}
    temp = token.nextToken();
    if (temp.length() != 2) {
      throw new ParseException(str + ": Bad minute format", 14);
    }
    try
    {
      setMinute(Short.parseShort(temp));
    }
    catch (UnsupportedOperationException e) {}
    temp = token.nextToken();
    String milsecond = "0";
    if (temp.indexOf(".") != -1)
    {
      milsecond = temp.substring(temp.indexOf(".") + 1);
      temp = temp.substring(0, temp.indexOf("."));
    }
    if (temp.length() != 2) {
      throw new ParseException(str + ": Bad second format", 17);
    }
    try
    {
      setSecond(Short.parseShort(temp.substring(0, 2)), Short.parseShort(milsecond));
    }
    catch (UnsupportedOperationException e) {}
    if (timeZone)
    {
      try
      {
        if (zoneStr.startsWith("-")) {
          setZoneNegative();
        }
      }
      catch (UnsupportedOperationException e) {}
      if (zoneStr.length() != 6) {
        throw new ParseException(str + ": Bad time zone format", 20);
      }
      try
      {
        setZone(Short.parseShort(zoneStr.substring(1, 3)), Short.parseShort(zoneStr.substring(4, 6)));
      }
      catch (UnsupportedOperationException e) {}
    }
    else
    {
      isUTC();
    }
    temp = null;
  }
  
  public void setPeriod(TimeDuration period)
    throws UnsupportedOperationException
  {
    throw new UnsupportedOperationException("in a time period type,the period must not be changed");
  }
}
