package org.exolab.types;

import java.io.Serializable;
import java.text.ParseException;
import javax.naming.OperationNotSupportedException;
import org.exolab.xml.ValidationException;


public abstract class RecurringDurationBase implements Serializable {
  private TimeDuration _period = null;
  private TimeDuration _duration = null;
  private short _hour = 0;
  private short _minute = 0;
  private short _second = 0;
  private short _millsecond = 0;
  private short _zoneHour = 0;
  private short _zoneMinute = 0;
  private boolean _utc = false;
  private boolean _zoneNegative = false;
  private boolean _isNegative = false;
  
  protected RecurringDurationBase() {}
  
  protected RecurringDurationBase(TimeDuration duration, TimeDuration period)
  {
    try
    {
      setDuration(duration);
      setPeriodInternal(period);
    }
    catch (UnsupportedOperationException e)
    {
      String err = "Recurring Duration: " + e;
      throw new IllegalArgumentException(err);
    }
  }
  
  protected RecurringDurationBase(String duration, String period)
    throws IllegalArgumentException
  {
    try
    {
      setDuration(TimeDuration.parseTimeDuration(duration));
      setPeriodInternal(TimeDuration.parseTimeDuration(period));
    }
    catch (Exception e)
    {
      String err = "In RecurringDurationBase: " + e;
      throw new IllegalArgumentException(err);
    }
  }
  
  protected RecurringDurationBase(String duration, String period, short[] values)
    throws OperationNotSupportedException
  {
    new RecurringDuration(duration, period);
    if (values.length != 6) {
      throw new IllegalArgumentException("Wrong numbers of values");
    }
    setValues(values);
  }
  
  private void setPeriodInternal(TimeDuration period)
  {
    this._period = period;
  }
  
  public void setPeriod(TimeDuration period)
    throws UnsupportedOperationException
  {
    setPeriodInternal(period);
  }
  
  public void setPeriod(String period)
    throws UnsupportedOperationException
  {
    try
    {
      setPeriodInternal(TimeDuration.parseTimeDuration(period));
    }
    catch (ParseException e)
    {
      String err = "RecurringDuration, setPeriod: " + e;
      throw new IllegalArgumentException(err);
    }
  }
  
  public void setDuration(TimeDuration duration)
    throws UnsupportedOperationException
  {
    this._duration = duration;
  }
  
  public void setDuration(String duration)
    throws UnsupportedOperationException
  {
    try
    {
      this._duration = TimeDuration.parseTimeDuration(duration);
    }
    catch (ParseException e)
    {
      String err = "RecurringDuration, setDuration: " + e;
      throw new IllegalArgumentException(err);
    }
  }
  
  public void setHour(short hour)
    throws UnsupportedOperationException
  {
    String err = "";
    if (hour > 23)
    {
      err = "the hour field (" + hour + ")must be strictly lower than 24";
      throw new IllegalArgumentException(err);
    }
    this._hour = hour;
  }
  
  public void setMinute(short minute)
    throws UnsupportedOperationException
  {
    String err = "";
    if ((minute == -1) && (this._hour != -1))
    {
      err = "minute cannot be omitted if the previous field is not omitted.";
      throw new IllegalArgumentException(err);
    }
    if (minute > 59)
    {
      err = "the minute field (" + minute + ") must be lower than 59";
      throw new IllegalArgumentException(err);
    }
    this._minute = minute;
  }
  
  public void setSecond(short second, short millsecond)
    throws UnsupportedOperationException
  {
    String err = "";
    if ((second == -1) && (this._minute != -1))
    {
      err = "second cannot be omitted if the previous field is not omitted.";
      throw new IllegalArgumentException(err);
    }
    if (second > 60)
    {
      err = "the second field (" + second + ")must be lower than 60";
      throw new IllegalArgumentException(err);
    }
    this._second = second;
    this._millsecond = millsecond;
  }
  
  public void setZone(short hour, short minute)
    throws UnsupportedOperationException
  {
    String err = "";
    if (hour > 23)
    {
      err = "the zone hour field (" + hour + ")must be strictly lower than 24";
      throw new IllegalArgumentException(err);
    }
    this._zoneHour = hour;
    if (minute > 59)
    {
      err = "the minute field (" + minute + ")must be lower than 59";
      throw new IllegalArgumentException(err);
    }
    this._zoneMinute = minute;
  }
  
  public void setValues(short[] values)
    throws UnsupportedOperationException
  {
    setHour(values[0]);
    setMinute(values[1]);
    setSecond(values[2], values[3]);
    setZone(values[4], values[5]);
  }
  
  public void setNegative()
  {
    this._isNegative = true;
  }
  
  public void setZoneNegative()
    throws UnsupportedOperationException
  {
    this._zoneNegative = true;
  }
  
  public void setUTC()
  {
    this._utc = true;
  }
  
  public TimeDuration getPeriod()
  {
    return this._period;
  }
  
  public TimeDuration getDuration()
  {
    return this._duration;
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
  
  public short getMilli()
  {
    return this._millsecond;
  }
  
  public short getZoneHour()
  {
    return this._zoneHour;
  }
  
  public short getZoneMinute()
  {
    return this._zoneMinute;
  }
  
  public short[] getValues()
  {
    short[] result = new short[6];
    result[0] = getHour();
    result[1] = getMinute();
    result[2] = getSeconds();
    result[3] = getMilli();
    result[4] = getZoneHour();
    result[5] = getZoneMinute();
    return result;
  }
  
  public boolean isUTC()
  {
    this._utc = ((this._zoneHour == 0) && (this._zoneMinute == 0));
    return this._utc;
  }
  
  public boolean isNegative()
  {
    return this._isNegative;
  }
  
  public boolean isZoneNegative()
  {
    return this._zoneNegative;
  }
  
  public boolean equals(Object object)
  {
    if ((object instanceof RecurringDurationBase)) {
      try
      {
        return equal((RecurringDurationBase)object);
      }
      catch (ValidationException e)
      {
        e.printStackTrace();
        return false;
      }
    }
    return false;
  }
  
  public boolean equal(RecurringDurationBase reccD)
    throws ValidationException
  {
    boolean result = false;
    if ((!getPeriod().equals(reccD.getPeriod())) || (!getDuration().equals(reccD.getDuration())))
    {
      String err = "Recurring Duration which have different values for the duration and period can not be compared";
      
      throw new ValidationException(err);
    }
    result = getHour() == reccD.getHour();
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
  
  public boolean isGreater(RecurringDurationBase reccD)
    throws ValidationException
  {
    if ((!getPeriod().equals(reccD.getPeriod())) || (!getDuration().equals(reccD.getDuration())))
    {
      String err = "Recurring Duration which have different values for the duration and period can not be compared";
      
      throw new ValidationException(err);
    }
    boolean result = false;
    short[] val_this = getValues();
    short[] val_reccD = reccD.getValues();
    for (int i = 0; (result != true) || (i < val_this.length); i++)
    {
      result = val_this[i] > val_reccD[i];
      if (val_this[i] < val_reccD[i]) {
        return false;
      }
    }
    return result;
  }
}
