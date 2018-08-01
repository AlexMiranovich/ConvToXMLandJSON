package org.exolab.types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.StringTokenizer;
import org.exolab.xml.ValidationException;

public class RecurringDuration extends RecurringDurationBase {
  private static final long serialVersionUID = -6037158412155942249L;
  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
  private static final boolean DEBUG = false;
  private short _century = 0;
  private short _year = 0;
  private short _month = 0;
  private short _day = 0;
  private static final short OMITED = Short.parseShort("-1");
  public RecurringDuration() {}
  public RecurringDuration(TimeDuration duration, TimeDuration period)
  {
    super(duration, period);
  }
  
  public RecurringDuration(String duration, String period)
  {
    super(duration, period);
  }
  
  public RecurringDuration(String duration, String period, short[] values)
    throws UnsupportedOperationException
  {
    this(duration, period);
    if (values.length != 10) {
      throw new IllegalArgumentException("Wrong numbers of values");
    }
    setValues(values);
  }
  
  public void setCentury(short century)
  {
    String err = "";
    if (century < -1)
    {
      err = "century : " + century + " must not be a negative value.";
      throw new IllegalArgumentException(err);
    }
    this._century = century;
  }
  
  public void setYear(short year)
    throws UnsupportedOperationException
  {
    String err = "";
    if (year < -1)
    {
      err = "year : " + year + " must not be a negative value.";
      throw new IllegalArgumentException(err);
    }
    if ((year == -1) && (this._century != -1))
    {
      err = "year can not be omitted if century is not omitted.";
      throw new IllegalArgumentException(err);
    }
    if ((year == 0) && (this._century == 0))
    {
      err = "0000 is not an allowed year";
      throw new IllegalArgumentException(err);
    }
    this._year = year;
  }
  
  public void setMonth(short month)
    throws UnsupportedOperationException
  {
    String err = "";
    if (month == -1)
    {
      if (this._century != -1)
      {
        err = "month cannot be omitted if the previous component is not omitted.\nonly higher level components can be omitted.";
        
        throw new IllegalArgumentException(err);
      }
    }
    else
    {
      if (month < 1)
      {
        err = "month : " + month + " is not a correct value." + "\n 1<month<12";
        
        throw new IllegalArgumentException(err);
      }
      if (month > 12)
      {
        err = "month : " + month + " is not a correct value.";
        err = err + "\n 1<month<12";
        throw new IllegalArgumentException(err);
      }
    }
    this._month = month;
  }
  
  public void setDay(short day)
    throws UnsupportedOperationException
  {
    String err = "";
    if (day == -1)
    {
      if (this._month != -1)
      {
        err = "day cannot be omitted if the previous component is not omitted.\nonly higher level components can be omitted.";
        
        throw new IllegalArgumentException(err);
      }
    }
    else if (day < 1)
    {
      err = "day : " + day + " is not a correct value.";
      err = err + "\n 1<day";
      throw new IllegalArgumentException(err);
    }
    if (this._month == 2)
    {
      if (isLeap())
      {
        if (day > 29)
        {
          err = "day : " + day + " is not a correct value.";
          err = err + "\n day<30 (leap year and month is february)";
          throw new IllegalArgumentException(err);
        }
      }
      else if (day > 28)
      {
        err = "day : " + day + " is not a correct value.";
        err = err + "\n day<30 (not a leap year and month is february)";
        throw new IllegalArgumentException(err);
      }
    }
    else if ((this._month == 4) || (this._month == 6) || (this._month == 9) || (this._month == 11))
    {
      if (day > 30)
      {
        err = "day : " + day + " is not a correct value.";
        err = err + "\n day<31 ";
        throw new IllegalArgumentException(err);
      }
    }
    else if (day > 31)
    {
      err = "day : " + day + " is not a correct value.";
      err = err + "\n day<=31 ";
      throw new IllegalArgumentException(err);
    }
    this._day = day;
  }
  
  public boolean isLeap()
  {
    int temp = this._century * 100 + this._year;
    boolean result = (temp % 4 == 0) && (temp % 100 != 0);
    result = (result) || (temp % 400 == 0);
    return result;
  }
  
  public void setValues(short[] values)
    throws UnsupportedOperationException
  {
    setCentury(values[0]);
    setYear(values[1]);
    setMonth(values[2]);
    setDay(values[3]);
    setHour(values[4]);
    setMinute(values[5]);
    setSecond(values[6], values[7]);
    setZone(values[8], values[9]);
  }
  
  public short getCentury()
  {
    return this._century;
  }
  
  public short getYear()
  {
    return this._year;
  }
  
  public short getMonth()
  {
    return this._month;
  }
  
  public short getDay()
  {
    return this._day;
  }
  
  public short[] getValues()
  {
    short[] result = null;
    result = new short[10];
    result[0] = getCentury();
    result[1] = getYear();
    result[2] = getMonth();
    result[3] = getDay();
    result[4] = getHour();
    result[5] = getMinute();
    result[6] = getSeconds();
    result[7] = getMilli();
    result[8] = getZoneHour();
    result[5] = getZoneMinute();
    return result;
  }
  
  public Date toDate()
    throws ParseException
  {
    Date date = null;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    SimpleTimeZone timeZone = new SimpleTimeZone(0, "UTC");
    if (!isUTC())
    {
      int offset = 0;
      offset = (getZoneMinute() + getZoneHour() * 60) * 60 * 1000;
      offset = isZoneNegative() ? -offset : offset;
      timeZone.setRawOffset(offset);
      timeZone.setID(java.util.TimeZone.getAvailableIDs(offset)[0]);
    }
    df.setTimeZone(timeZone);
    date = df.parse(toPrivateString());
    return date;
  }
  
  public String toString()
  {
    return toPrivateString();
  }
  
  private final String toPrivateString()
  {
    StringBuffer result = new StringBuffer();
    StringBuffer timeZone = null;
    if (getCentury() == -1)
    {
      result.append('-');
    }
    else
    {
      if (getCentury() / 10 == 0) {
        result.append(0);
      }
      result.append(getCentury());
      if (getYear() / 10 == 0) {
        result.append(0);
      }
      result.append(getYear());
    }
    result.append('-');
    if (getMonth() == -1)
    {
      result.append('-');
    }
    else
    {
      if (getMonth() / 10 == 0) {
        result.append(0);
      }
      result.append(getMonth());
    }
    result.append('-');
    if (getDay() == -1)
    {
      result.append('-');
    }
    else
    {
      if (getDay() / 10 == 0) {
        result.append(0);
      }
      result.append(getDay());
    }
    result.append("T");
    if (getHour() == -1)
    {
      result.append('-');
    }
    else
    {
      if (getHour() / 10 == 0) {
        result.append(0);
      }
      result.append(getHour());
    }
    result.append(':');
    if (getMinute() == -1)
    {
      result.append('-');
    }
    else
    {
      if (getMinute() / 10 == 0) {
        result.append(0);
      }
      result.append(getMinute());
    }
    result.append(':');
    if (getSeconds() == -1)
    {
      result.append('-');
    }
    else
    {
      if (getSeconds() / 10 == 0) {
        result.append(0);
      }
      result.append(getSeconds());
    }
    result.append('.');
    result.append(getMilli());
    if (isNegative()) {
      result.append('-');
    }
    if (!isUTC())
    {
      timeZone = new StringBuffer();
      if (getZoneHour() / 10 == 0) {
        timeZone.append(0);
      }
      timeZone.append(getZoneHour());
      
      timeZone.append(':');
      if (getZoneMinute() / 10 == 0) {
        timeZone.append(0);
      }
      timeZone.append(getZoneMinute());
      if (isZoneNegative()) {
        timeZone.insert(0, '-');
      } else {
        timeZone.insert(0, '+');
      }
      result.append(timeZone.toString());
    }
    if (isNegative()) {
      result.insert(0, '-');
    }
    return result.toString();
  }
  
  public static Object parse(String str)
    throws ParseException
  {
    return parseRecurringDuration(str);
  }
  
  public static RecurringDuration parseRecurringDuration(String str)
    throws ParseException
  {
    RecurringDuration result = new RecurringDuration();
    if (str.endsWith("Z")) {
      str = str.substring(0, str.indexOf("Z"));
    }
    if ((str.startsWith("-")) && (!str.startsWith("--"))) {
      result.setNegative();
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
    if (token.countTokens() > 3) {
      throw new ParseException(str + ": Bad date format", 0);
    }
    String temp;
    try
    {
      boolean process = false;
      if (token.countTokens() == 3)
      {
        temp = token.nextToken();
        if (temp.length() != 4) {
          throw new ParseException(str + ":Bad year format", 1);
        }
        result.setCentury(Short.parseShort(temp.substring(0, 2)));
        
        result.setYear(Short.parseShort(temp.substring(2, 4)));
        process = true;
      }
      if (!process) {
        result.setCentury(OMITED);
      }
      if (token.countTokens() == 2)
      {
         temp = token.nextToken();
        if (temp.length() != 2) {
          throw new ParseException(str + ": Bad month format", 5);
        }
        result.setMonth(Short.parseShort(temp));
        process = true;
      }
      if (!process) {
        result.setMonth(OMITED);
      }
      if (token.countTokens() == 1)
      {
        temp = token.nextToken();
        if (temp.length() != 2) {
          throw new ParseException(str + ":Bad day format", 8);
        }
        result.setDay(Short.parseShort(temp));
        process = true;
      }
      if (!process) {
        result.setDay(OMITED);
      }
      token = new StringTokenizer(time, ":");
      process = false;
      if (token.countTokens() > 5) {
        throw new ParseException(str + ": Bad time format", 11);
      }
      if (token.countTokens() == 3)
      {
        temp = token.nextToken();
        temp = temp.substring(temp.indexOf("T") + 1);
        if (temp.length() != 2) {
          throw new ParseException(str + ": Bad hour format", 11);
        }
        result.setHour(Short.parseShort(temp));
        process = true;
      }
      if (!process) {
        if (result.getDay() == OMITED) {
          result.setHour(OMITED);
        } else {
          throw new IllegalArgumentException("hour cannot be omitted");
        }
      }
      if (token.countTokens() == 2)
      {
        temp = token.nextToken();
        if (temp.length() != 2) {
          throw new ParseException(str + ": Bad minute format", 14);
        }
        result.setMinute(Short.parseShort(temp));
        process = true;
      }
      if (!process) {
        if (result.getDay() == OMITED)
        {
          result.setHour(OMITED);
          result.setMinute(OMITED);
        }
        else
        {
          throw new IllegalArgumentException("hour cannot be omitted");
        }
      }
      if (token.countTokens() == 1)
      {
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
        result.setSecond(Short.parseShort(temp.substring(0, 2)), Short.parseShort(milsecond));
        
        process = true;
      }
      if (!process) {
        if (result.getDay() == OMITED)
        {
          result.setHour(OMITED);
          result.setMinute(OMITED);
          result.setSecond(OMITED, OMITED);
        }
        else
        {
          throw new IllegalArgumentException("hour cannot be omitted");
        }
      }
      if (timeZone)
      {
        if (zoneStr.startsWith("-")) {
          result.setZoneNegative();
        }
        if (zoneStr.length() != 6) {
          throw new ParseException(str + ": Bad time zone format", 20);
        }
        result.setZone(Short.parseShort(zoneStr.substring(1, 3)), Short.parseShort(zoneStr.substring(4, 6)));
      }
      else
      {
        result.isUTC();
      }
      temp = null;
    }
    catch (UnsupportedOperationException e) {}
    return result;
  }
  
  public boolean equals(Object object)
  {
    if ((object instanceof RecurringDuration)) {
      try
      {
        return equal((RecurringDuration)object);
      }
      catch (ValidationException e)
      {
        e.printStackTrace();
        return false;
      }
    }
    return false;
  }
  
  public boolean equal(RecurringDuration reccD)
    throws ValidationException
  {
    boolean result = false;
    if (reccD == null) {
      return false;
    }
    if ((!getPeriod().equals(reccD.getPeriod())) || (!getDuration().equals(reccD.getDuration())))
    {
      String err = " Recurring Duration which have different values for the duration and period can not be compared";
      
      throw new ValidationException(err);
    }
    result = getCentury() == reccD.getCentury();
    result = (result) && (getYear() == reccD.getYear());
    result = (result) && (getMonth() == reccD.getMonth());
    result = (result) && (getDay() == reccD.getDay());
    result = (result) && (getHour() == reccD.getHour());
    result = (result) && (getMinute() == reccD.getMinute());
    result = (result) && (getSeconds() == reccD.getSeconds());
    result = (result) && (getMilli() == reccD.getMilli());
    result = (result) && (isNegative() == isNegative());
    if (!reccD.isUTC())
    {
      result = (result) && (!isUTC());
      result = (result) && (getZoneHour() == reccD.getZoneHour());
      result = (result) && (getZoneMinute() == reccD.getZoneMinute());
    }
    return result;
  }
  
  public boolean isGreater(RecurringDuration reccD)
    throws ValidationException
  {
    boolean result = false;
    if ((!getPeriod().equals(reccD.getPeriod())) || (!getDuration().equals(reccD.getDuration())))
    {
      String err = " Recurring Duration which have different values for the duration and period can not be compared";
      
      throw new ValidationException(err);
    }
    short[] val_this = getValues();
    short[] val_reccD = reccD.getValues();
    int i = 0;
    while ((result != true) && (i < val_this.length - 1))
    {
      result = val_this[i] > val_reccD[i];
      if (val_this[i] < val_reccD[i]) {
        return false;
      }
      i++;
    }
    return result;
  }
}
