package org.exolab.types;

import java.io.Serializable;
import java.text.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Duration implements Serializable {
  private static final long serialVersionUID = -6475091654291323029L;
  private static final Log LOG = LogFactory.getLog(Duration.class);
  private static final boolean DEBUG = false;
  private static final int TIME_FLAG = 8;
  private short _year = 0;
  private short _month = 0;
  private short _day = 0;
  private short _hour = 0;
  private short _minute = 0;
  private short _second = 0;
  private long _millisecond = 0L;
  private boolean _isNegative = false;
  
  public Duration() {}
  public Duration(String duration)
    throws ParseException {
    parseDurationInternal(duration, this);
  }
  public Duration(long l) {
    long refSecond = 1000L;
    long refMinute = 60L * refSecond;
    long refHour = 60L * refMinute;
    long refDay = 24L * refHour;
    long refMonth = (long) (30.42D * refDay);
    long refYear = 12L * refMonth;
    if (l < 0L) {
      setNegative();
      l = -l;
    }
    short year = (short)(int)(l / refYear);
    l %= refYear;
    short month = (short)(int)(l / refMonth);
    l %= refMonth;
    short day = (short)(int)(l / refDay);
    l %= refDay;
    short hour = (short)(int)(l / refHour);
    l %= refHour;
    short minute = (short)(int)(l / refMinute);
    l %= refMinute;
    short seconds = (short)(int)(l / refSecond);
    l %= refSecond;
    long milliseconds = l;
    setValue(year, month, day, hour, minute, seconds, milliseconds);
  }
  public void setYear(short year) {
    if (year < 0) {
      String err = "In a duration all fields have to be positive.";
      throw new IllegalArgumentException(err);
    }
    this._year = year;
  }
  public void setMonth(short month) {
    if (month < 0) {
      String err = "In a duration all fields have to be positive.";
      throw new IllegalArgumentException(err);
    }
    this._month = month;
  }
  public void setDay(short day) {
    if (day < 0)
    {
      String err = "In a duration all fields have to be positive.";
      throw new IllegalArgumentException(err);
    }
    this._day = day;
  }
  
  public void setHour(short hour)
  {
    if (hour < 0)
    {
      String err = "In a duration all fields have to be positive.";
      throw new IllegalArgumentException(err);
    }
    this._hour = hour;
  }
  
  public void setMinute(short minute)
  {
    if (minute < 0)
    {
      String err = "In a duration all fields have to be positive.";
      throw new IllegalArgumentException(err);
    }
    this._minute = minute;
  }
  
  public void setSeconds(short second)
  {
    if (second < 0)
    {
      String err = "In a duration all fields have to be positive.";
      throw new IllegalArgumentException(err);
    }
    this._second = second;
  }
  
  public void setMilli(long milli)
  {
    if (milli < 0L)
    {
      String err = "In a duration all fields have to be positive.";
      throw new IllegalArgumentException(err);
    }
    this._millisecond = milli;
  }
  
  public void setNegative()
  {
    this._isNegative = true;
  }
  
  public void setValue(short year, short month, short day, short hour, short minute, short second, long millisecond)
  {
    setYear(year);
    setMonth(month);
    setDay(day);
    setHour(hour);
    setMinute(minute);
    setSeconds(second);
    setMilli(millisecond);
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
  
  public short getHour()
  {
    return this._hour;
  }
  
  public short getMinute()
  {
    return this._minute;
  }
  
  public short getSeconds()
  {
    return this._second;
  }
  
  public long getMilli()
  {
    return this._millisecond;
  }
  
  public boolean isNegative()
  {
    return this._isNegative;
  }
  
  public long toLong()
  {
    long result = 0L;
    
    result = (long) ((((((this._year * 12L + this._month) * 30.42D + this._day) * 24.0D + this._hour) * 60.0D + this._minute) * 60.0D + this._second) * 1000.0D + this._millisecond);
    
    result = isNegative() ? -result : result;
    return result;
  }
  
  public String toString()
  {
    if (toLong() == 0L) {
      return "PT0S";
    }
    StringBuffer result = new StringBuffer();
    if (this._isNegative) {
      result.append('-');
    }
    result.append("P");
    if (this._year != 0) {
      result.append(this._year);
      result.append('Y');
    }
    if (this._month != 0) {
      result.append(this._month);
      result.append('M');
    }
    if (this._day != 0) {
      result.append(this._day);
      result.append('D');
    }
    boolean isThereTime = (this._hour != 0) || (this._minute != 0) || (this._second != 0) || (this._millisecond != 0L);
    if (isThereTime) {
      result.append('T');
      if (this._hour != 0) {
        result.append(this._hour);
        result.append('H');
      }
      if (this._minute != 0) {
        result.append(this._minute);
        result.append('M');
      }
      if ((this._second != 0) || (this._millisecond != 0L)) {
        result.append(this._second);
        if (this._millisecond != 0L) {
          result.append('.');
          if (this._millisecond < 100L) {
            result.append('0');
            if (this._millisecond < 10L) {
              result.append('0');
            }
          }
          result.append(this._millisecond);
        }
        result.append('S');
      }
    }
    return result.toString();
  }
  
  public static Object parse(String str) throws ParseException {
    return parseDuration(str);
  }
  public static Duration parseDuration(String str)
    throws ParseException {
    Duration result = new Duration();
    return parseDurationInternal(str, result);
  }
  private static Duration parseDurationInternal(String str, Duration result) throws ParseException {
    boolean isMilli = false;
    if (str == null) {
      throw new IllegalArgumentException("the string to be parsed must not be null");
    }
    if (str.length() == 0) {
      return null;
    }
    if (result == null) {
      result = new Duration();
    }
    char[] chars = str.toCharArray();
    int idx = 0;
    if (chars[idx] == '-') {
      idx++;
      result.setNegative();
      if (idx >= chars.length) {
        throw new ParseException("'-' is wrongly placed", 0);
      }
    }
    if (chars[idx] != 'P') {
      throw new ParseException("Missing 'P' delimiter", idx);
    }
    idx++;
    if (idx == chars.length) {
      throw new ParseException("Bad format for a duration:" + str, idx);
    }
    int number = 0;
    boolean hasNumber = false;
    int flags = 0;
    while (idx < chars.length) {
      char ch = chars[(idx++)];
      switch (ch) {
      case 'Y': 
        if (flags > 0) {
          String err = str + ":Syntax error, 'Y' must proceed all other delimiters.";
          throw new ParseException(err, idx);
        }
        flags = 64;
        if (hasNumber) {
          result.setYear((short)number);
          hasNumber = false;
        }
        else {
          String err = str + ":missing number of years before 'Y'";
          throw new ParseException(err, idx);
        }
        break;
      case 'M': 
        if ((flags & 0x8) == 8) {
          if ((flags & 0x3) > 0) {
            throw new ParseException(str + ": Syntax Error...", idx);
          }
          flags |= 0x2;
          if (hasNumber) {
            result.setMinute((short)number);
            hasNumber = false;
          }
          else
          {
            String err = str + ": missing number of minutes before 'M'";
            throw new ParseException(err, idx);
          }
        }
        else {
          if ((flags & 0x3F) > 0) {
            throw new ParseException(str + ":Syntax Error...", idx);
          }
          flags |= 0x20;
          if (hasNumber) {
            result.setMonth((short)number);
            hasNumber = false;
          }
          else {
            String err = str + ":missing number of months before 'M'";
            throw new ParseException(err, idx);
          }
        }
        break;
      case 'D': 
        if ((flags & 0x1F) > 0) {
          throw new ParseException(str + ":Syntax Error...", idx);
        }
        flags |= 0x10;
        if (hasNumber) {
          result.setDay((short)number);
          hasNumber = false;
        }
        else {
          String err = str + ":missing number of days before 'D'";
          throw new ParseException(err, idx);
        }
        break;
      case 'T': 
        if ((flags & 0x8) == 8) {
          String err = str + ":Syntax error, 'T' may not " + "exist more than once.";
          
          throw new ParseException(err, idx);
        }
        flags |= 0x8;
        break;
      case 'H': 
        if ((flags & 0xF) != 8) {
          String err = null;
          if ((flags & 0x8) != 8) {
            err = str + ": Missing 'T' before 'H'";
          } else {
            err = str + ": Syntax Error, 'H' must appear for 'M' or 'S'";
          }
          throw new ParseException(err, idx);
        }
        flags |= 0x4;
        if (hasNumber) {
          result.setHour((short)number);
          hasNumber = false;
        }
        else {
          String err = str + ":missing number of hours before 'H'";
          throw new ParseException(err, idx);
        }
        break;
      case 'S': 
        if (flags != 0) {
          if ((flags & 0x8) != 8) {
            String err = str + ": Missing 'T' before 'S'";
            throw new ParseException(err, idx);
          }
          if ((flags & 0x1) == 1) {
            String err = str + ": Syntax error 'S' may not exist more than once.";
            throw new ParseException(err, idx);
          }
          flags |= 0x1;
          if (hasNumber) {
            result.setSeconds((short)number);
            hasNumber = false;
          }
          else {
            String err = str + ": missing number of seconds before 'S'";
            throw new ParseException(err, idx);
          }
        }
        else if (hasNumber) {
          String numb = Integer.toString(number).replaceFirst("1", "");
          
          number = Integer.parseInt(numb);
          if (numb.length() < 3) {
            if (numb.length() < 2) {
              number *= 10;
            }
            number *= 10;
          }
          result.setMilli(number);
          hasNumber = false;
        }
        else {
          String err = str + ": missing number of milliseconds before 'S'";
          throw new ParseException(err, idx);
        }
        break;
      case '.': 
        if ((flags & 0x8) != 8) {
          String err = str + ": Missing 'T' before 'S'";
          throw new ParseException(err, idx);
        }
        if ((flags | 0x1) == 1){
          String err = str + ": Syntax error '.' may not exist more than once.";
          throw new ParseException(err, idx);
        }
        flags = 0;
        if (hasNumber) {
          result.setSeconds((short)number);
          hasNumber = false;
        }
        else {
          String err = str + ": missing number of seconds before 'S'";
          throw new ParseException(err, idx);
        }
        isMilli = true;
        break;
      default: 
        if (('0' <= ch) && (ch <= '9')) {
          if (hasNumber) {
            number = number * 10 + (ch - '0');
          }
          else {
            hasNumber = true;
            if (isMilli) {
              number = Integer.parseInt("1" + (ch - '0'));
            } else {
              number = ch - '0';
            }
          }
        }
        else {
          throw new ParseException(str + ":Invalid character: " + ch, idx);
        }
        break;
      }
    }
    if ((flags & 0xF) == 8) {
      LOG.warn("Warning: " + str + ": T shall be omitted");
    }
    if (hasNumber) {
      throw new ParseException(str + ": expecting ending delimiter", idx);
    }
    return result;
  }
  public int hashCode() {
    return 37 * (this._year ^ this._month ^ this._day ^ this._hour ^ this._minute ^ this._second);
  }
  public boolean equals(Object object) {
    if ((object instanceof Duration)) {
      return equal((Duration)object);
    }
    return false;
  }
  public boolean equal(Duration duration) {
    boolean result = false;
    if (duration == null) {
      return result;
    }
    result = this._year == duration.getYear();
    result = (result) && (this._month == duration.getMonth());
    result = (result) && (this._day == duration.getDay());
    result = (result) && (this._hour == duration.getHour());
    result = (result) && (this._minute == duration.getMinute());
    result = (result) && (this._second == duration.getSeconds());
    result = (result) && (this._millisecond == duration.getMilli());
    result = (result) && (isNegative() == duration.isNegative());
    return result;
  }
  public boolean isGreater(Duration duration) {
    boolean result = false;
    result = toLong() > duration.toLong();
    return result;
  }
}
