package org.exolab.types;

import java.io.Serializable;
import java.text.ParseException;


public class TimeDuration implements Serializable {
  private static final long serialVersionUID = -3080457339689062021L;
  private static final boolean DEBUG = false;
  private static final int TIME_FLAG = 8;
  private short _year = 0;
  private short _month = 0;
  private short _day = 0;
  private short _hour = 0;
  private short _minute = 0;
  private short _second = 0;
  private short _millisecond = 0;
  private boolean _isNegative = false;

  public TimeDuration() {
  }

  public TimeDuration(long l) {
    long refSecond = 1000L;
    long refMinute = 60L * refSecond;
    long refHour = 60L * refMinute;
    long refDay = 24L * refHour;
    long refMonth = (long)(30.42D * (double)refDay);
    long refYear = 12L * refMonth;
    if (l < 0L) {
      this.setNegative();
      l = -l;
    }

    short year = (short)((int)(l / refYear));
    l %= refYear;
    short month = (short)((int)(l / refMonth));
    l %= refMonth;
    short day = (short)((int)(l / refDay));
    l %= refDay;
    short hour = (short)((int)(l / refHour));
    l %= refHour;
    short minute = (short)((int)(l / refMinute));
    l %= refMinute;
    short seconds = (short)((int)(l / refSecond));
    l %= refSecond;
    short milliseconds = (short)((int)l);
    this.setValue(year, month, day, hour, minute, seconds, milliseconds);
  }

  public void setYear(short year) {
    this._year = year;
  }

  public void setMonth(short month) {
    this._month = month;
  }

  public void setDay(short day) {
    this._day = day;
  }

  public void setHour(short hour) {
    this._hour = hour;
  }

  public void setMinute(short minute) {
    this._minute = minute;
  }

  public void setSeconds(short second) {
    this._second = second;
  }

  public void setMilli(short milli) {
    this._millisecond = milli;
  }

  public void setNegative() {
    this._isNegative = true;
  }

  public void setValue(short year, short month, short day, short hour, short minute, short second, short millisecond) {
    this.setYear(year);
    this.setMonth(month);
    this.setDay(day);
    this.setHour(hour);
    this.setMinute(minute);
    this.setSeconds(second);
    this.setMilli(millisecond);
  }

  public short getYear() {
    return this._year;
  }

  public short getMonth() {
    return this._month;
  }

  public short getDay() {
    return this._day;
  }

  public short getHour() {
    return this._hour;
  }

  public short getMinute() {
    return this._minute;
  }

  public short getSeconds() {
    return this._second;
  }

  public short getMilli() {
    return this._millisecond;
  }

  public boolean isNegative() {
    return this._isNegative;
  }

  public long toLong() {
    long result = 0L;
    result = (long)((((((double)((long)this._year * 12L + (long)this._month) * 30.42D + (double)this._day) * 24.0D + (double)this._hour) * 60.0D + (double)this._minute) * 60.0D + (double)this._second) * 1000.0D + (double)this._millisecond);
    result = this.isNegative() ? -result : result;
    return result;
  }

  public String toString() {
    StringBuffer result = new StringBuffer();
    result.append("P");
    if (this._year != 0) {
      result.append(this._year);
      result.append("Y");
    }

    if (this._month != 0) {
      result.append(this._month);
      result.append("M");
    }

    if (this._day != 0) {
      result.append(this._day);
      result.append("D");
    }

    boolean isThereTime = this._hour != 0 || this._minute != 0 || this._second != 0;
    if (isThereTime) {
      result.append("T");
      if (this._hour != 0) {
        result.append(this._hour);
        result.append("H");
      }

      if (this._minute != 0) {
        result.append(this._minute);
        result.append("M");
      }

      if (this._second != 0) {
        result.append(this._second);
        if (this._millisecond != 0) {
          result.append('.');
          if (this._millisecond < 100) {
            result.append('0');
            if (this._millisecond < 10) {
              result.append('0');
            }
          }

          result.append(this._millisecond);
        }

        result.append('S');
      }
    }

    if (this._isNegative) {
      result.insert(0, '-');
    }

    return result.toString();
  }

  public static Object parse(String str) throws ParseException {
    return parseTimeDuration(str);
  }

  public static TimeDuration parseTimeDuration(String str) throws ParseException {
    if (str == null) {
      throw new IllegalArgumentException("the string to be parsed must not be null");
    } else {
      TimeDuration result = new TimeDuration();
      char[] chars = str.toCharArray();
      int idx = 0;
      if (chars.length == 0) {
        return null;
      } else {
        if (chars[idx] == '-') {
          ++idx;
          result.setNegative();
          if (idx >= chars.length) {
            throw new ParseException("'-' is wrong placed", 0);
          }
        }

        if (chars[idx] != 'P') {
          throw new ParseException("Missing 'P' delimiter", idx);
        } else {
          ++idx;
          int number = 0;
          boolean hasNumber = false;
          int flags = 0;

          while(true) {
            while(idx < chars.length) {
              char ch = chars[idx++];
              String err;
              switch(ch) {
                case '.':
                  if ((flags | 1) == 1) {
                    err = str + ": Syntax error '.' may not exist more than once.";
                    throw new ParseException(err, idx);
                  }

                  if ((flags & 8) != 8) {
                    err = str + ": Missing 'T' before 'S'";
                    throw new ParseException(err, idx);
                  }

                  flags = 0;
                  if (!hasNumber) {
                    err = str + ": missing number of seconds before 'S'";
                    throw new ParseException(err, idx);
                  }

                  result.setSeconds((short)number);
                  hasNumber = false;
                  break;
                case 'D':
                  if ((flags & 31) > 0) {
                    throw new ParseException(str + ":Syntax Error...", idx);
                  }

                  flags |= 16;
                  if (!hasNumber) {
                    err = str + ":missing number of days before 'D'";
                    throw new ParseException(err, idx);
                  }

                  result.setDay((short)number);
                  hasNumber = false;
                  break;
                case 'H':
                  if ((flags & 15) != 8) {
                    err = null;
                    if ((flags & 8) != 8) {
                      err = str + ": Missing 'T' before 'H'";
                    } else {
                      err = str + ": Syntax Error, 'H' must appear for 'M' or 'S'";
                    }

                    throw new ParseException(err, idx);
                  }

                  flags |= 4;
                  if (!hasNumber) {
                    err = str + ":missing number of hours before 'H'";
                    throw new ParseException(err, idx);
                  }

                  result.setHour((short)number);
                  hasNumber = false;
                  break;
                case 'M':
                  if ((flags & 8) == 8) {
                    if ((flags & 3) > 0) {
                      throw new ParseException(str + ": Syntax Error...", idx);
                    }

                    flags |= 2;
                    if (!hasNumber) {
                      err = str + ": missing number of minutes " + "before 'M'";
                      throw new ParseException(err, idx);
                    }

                    result.setMinute((short)number);
                    hasNumber = false;
                  } else {
                    if ((flags & 63) > 0) {
                      throw new ParseException(str + ":Syntax Error...", idx);
                    }

                    flags |= 32;
                    if (!hasNumber) {
                      err = str + ":missing number of months before 'M'";
                      throw new ParseException(err, idx);
                    }

                    result.setMonth((short)number);
                    hasNumber = false;
                  }
                  break;
                case 'S':
                  if (flags != 0) {
                    if ((flags & 8) != 8) {
                      err = str + ": Missing 'T' before 'S'";
                      throw new ParseException(err, idx);
                    }

                    if ((flags & 1) == 1) {
                      err = str + ": Syntax error 'S' may not exist more than once.";
                      throw new ParseException(err, idx);
                    }

                    flags |= 1;
                    if (!hasNumber) {
                      err = str + ": missing number of seconds before 'S'";
                      throw new ParseException(err, idx);
                    }

                    result.setSeconds((short)number);
                    hasNumber = false;
                  } else {
                    if (!hasNumber) {
                      err = str + ": missing number of milliseconds before 'S'";
                      throw new ParseException(err, idx);
                    }

                    err = Integer.toString(number);
                    if (err.length() < 3) {
                      if (err.length() < 2) {
                        number *= 10;
                      }

                      number *= 10;
                    }

                    result.setMilli((short)number);
                    hasNumber = false;
                  }
                  break;
                case 'T':
                  if ((flags & 8) == 8) {
                    err = str + ":Syntax error, 'T' may not " + "exist more than once.";
                    throw new ParseException(err, idx);
                  }

                  flags |= 8;
                  break;
                case 'Y':
                  if (flags > 0) {
                    err = str + ":Syntax error, 'Y' must " + "proceed all other delimiters.";
                    throw new ParseException(err, idx);
                  }

                  flags = 64;
                  if (!hasNumber) {
                    err = str + ":missing number of years before 'Y'";
                    throw new ParseException(err, idx);
                  }

                  result.setYear((short)number);
                  hasNumber = false;
                  break;
                default:
                  if ('0' > ch || ch > '9') {
                    throw new ParseException(str + ":Invalid character: " + ch, idx);
                  }

                  if (hasNumber) {
                    number = number * 10 + (ch - 48);
                  } else {
                    hasNumber = true;
                    number = ch - 48;
                  }
              }
            }

            if ((flags & 15) == 8) {
              throw new ParseException(str + ": T must be omitted", idx);
            }

            if (hasNumber) {
              throw new ParseException(str + ": expecting ending delimiter", idx);
            }

            return result;
          }
        }
      }
    }
  }

  public boolean equals(Object object) {
    return object instanceof TimeDuration ? this.equal((TimeDuration)object) : false;
  }

  public boolean equal(TimeDuration timeD) {
    boolean result = false;
    if (timeD == null) {
      return result;
    } else {
      result = this._year == timeD.getYear();
      result = result && this._month == timeD.getMonth();
      result = result && this._day == timeD.getDay();
      result = result && this._hour == timeD.getHour();
      result = result && this._minute == timeD.getMinute();
      result = result && this._second == timeD.getSeconds();
      result = result && this.isNegative() == timeD.isNegative();
      return result;
    }
  }

  public boolean isGreater(TimeDuration timeD) {
    boolean result = false;
    result = this.toLong() > timeD.toLong();
    return result;
  }
}
