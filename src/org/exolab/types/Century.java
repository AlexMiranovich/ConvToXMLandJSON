package org.exolab.types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class Century extends TimePeriod {
  private static final long serialVersionUID = 88787876938390034L;
  private static final String YEAR_FORMAT = "yyyy";
  private static final boolean DEBUG = false;

  public Century() {
    super("P100Y");
    int temp = TimeZone.getDefault().getRawOffset();
    if (temp < 0) {
      temp = -temp;

      try {
        this.setZoneNegative();
      } catch (UnsupportedOperationException var5) {
        ;
      }
    }

    try {
      short zhour = (short)(temp / 3600000);
      temp %= 3600000;
      short zmin = (short)(temp / '\uea60');
      super.setZone(zhour, zmin);
    } catch (UnsupportedOperationException var4) {
      ;
    }

  }

  public String toString() {
    StringBuffer result = new StringBuffer();
    result.append(String.valueOf(this.getCentury()));
    if (result.length() == 1) {
      result.insert(0, 0);
    }

    if (this.isNegative()) {
      result.insert(0, "-");
    }

    return result.toString();
  }

  public static Object parse(String str) throws ParseException {
    return parseCentury(str);
  }

  public static Century parseCentury(String str) throws ParseException {
    Century result = new Century();
    if (str.startsWith("-")) {
      result.setNegative();
      str = str.substring(1);
    }

    if (str.length() != 2) {
      throw new ParseException(str + ": Bad XML Schema Century type format (CC)", 0);
    } else {
      result.setCentury(Short.parseShort(str));
      return result;
    }
  }

  public Date toDate() throws ParseException {
    Date date = null;
    SimpleDateFormat df = new SimpleDateFormat("yyyy");
    SimpleTimeZone timeZone = new SimpleTimeZone(0, "UTC");
    if (!this.isUTC()) {
      int offset = (this.getZoneMinute() + this.getZoneHour() * 60) * 60 * 1000;
      offset = this.isZoneNegative() ? -offset : offset;
      timeZone.setRawOffset(offset);
      timeZone.setID(TimeZone.getAvailableIDs(offset)[0]);
    }

    df.setTimeZone(timeZone);
    date = df.parse(this.toString());
    return date;
  }

  public void setYear(short year) throws UnsupportedOperationException {
    String err = "In a Century : the year field must not be changed";
    throw new UnsupportedOperationException(err);
  }

  public void setMonth(short month) throws UnsupportedOperationException {
    String err = "In a Century : the month field must not be changed";
    throw new UnsupportedOperationException(err);
  }

  public void setDay(short day) throws UnsupportedOperationException {
    String err = "In a Century : the day field must not be changed";
    throw new UnsupportedOperationException(err);
  }

  public void setHour(short hour) throws UnsupportedOperationException {
    String err = "In a Century : the hour field must not be changed";
    throw new UnsupportedOperationException(err);
  }

  public void setMinute(short minute) throws UnsupportedOperationException {
    String err = "In a Century : the minute field must not be changed";
    throw new UnsupportedOperationException(err);
  }

  public void setSecond(short second, short millsecond) throws UnsupportedOperationException {
    String err = "In a Century : the second field must not be changed";
    throw new UnsupportedOperationException(err);
  }

  public void setZone(short hour, short minute) throws UnsupportedOperationException {
    String err = "In a Century : the time zone field must not be changed";
    throw new UnsupportedOperationException(err);
  }

  public void setZoneNegative() throws UnsupportedOperationException {
    String err = "In a Century : the time zone field must not be changed";
    throw new UnsupportedOperationException(err);
  }
}
