package org.exolab.types;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public abstract class DateTimeBase implements Serializable, Cloneable {
  public static final int INDETERMINATE = -1;
  public static final int LESS_THAN = 0;
  public static final int EQUALS = 1;
  public static final int GREATER_THAN = 2;
  protected static final int MAX_TIME_ZONE_COMPARISON_OFFSET = 14;
  protected static final String WRONGLY_PLACED = " is wrongly placed.";
  private boolean _isNegative = false;
  private short _century = 0;
  private short _year = 0;
  private short _month = 0;
  private short _day = 0;
  private short _hour = 0;
  private short _minute = 0;
  private short _second = 0;
  private short _millsecond = 0;
  private boolean _zoneNegative = false;
  private boolean _utc = false;
  private short _zoneHour = 0;
  private short _zoneMinute = 0;
  public abstract Date toDate();
  public abstract void setValues(short[] paramArrayOfShort);
  public abstract short[] getValues();
  public final boolean isLeap(int year)
  {
    return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
  }
  private final boolean isLeap(short century, short year)
  {
    return isLeap(century * 100 + year);
  }
  public void setNegative()
    throws UnsupportedOperationException {
    this._isNegative = true;
  }
  public void setCentury(short century)
    throws UnsupportedOperationException {
    String err = "";
    if (century < 0) {
      err = "century " + century + " must not be negative.";
      throw new IllegalArgumentException(err);
    }
    if ((this._year == 0) && (century == 0) && (this._century != 0)) {
      err = "century:  0000 is not an allowed year.";
      throw new IllegalArgumentException(err);
    }
    this._century = century;
  }
  public void setYear(short year) throws UnsupportedOperationException {
    String err = "";
    if (year < 0) {
      err = "year " + year + " must not be negative.";
      throw new IllegalArgumentException(err);
    }
    if (year == -1) {
      if (this._century != -1)
      {
        err = "year can not be omitted unless century is also omitted.";
        throw new IllegalArgumentException(err);
      }
    }
    else {
      if ((year == 0) && (this._century == 0)) {
        err = "year:  0000 is not an allowed year";
        throw new IllegalArgumentException(err);
      }
      if (year > 99) {
        err = "year " + year + " is out of range:  0 <= year <= 99.";
        throw new IllegalArgumentException(err);
      }
    }
    this._year = year;
  }
  public void setMonth(short month)
    throws UnsupportedOperationException {
    String err = "";
    if (month == -1) {
      if (this._century != -1) {
        err = "month cannot be omitted unless the previous component is also omitted.\nonly higher level components can be omitted.";
        
        throw new IllegalArgumentException(err);
      }
    }
    else if ((month < 1) || (month > 12)) {
      err = "month " + month + " is out of range:  1 <= month <= 12";
      throw new IllegalArgumentException(err);
    }
    this._month = month;
  }
  public void setDay(short day) throws UnsupportedOperationException {
    String err = "";
    if (day == -1) {
      if (this._month != -1) {
        err = "day cannot be omitted unless the previous component is also omitted.\nonly higher level components can be omitted.";
        throw new IllegalArgumentException(err);
      }
    }
    else if (day < 1) {
      err = "day " + day + " cannot be negative.";
      throw new IllegalArgumentException(err);
    }
    short maxDay = maxDayInMonthFor(this._century, this._year, this._month);
    if (day > maxDay) {
      if (this._month != 2) {
        err = "day " + day + " is out of range for month " + this._month + ":  " + "1 <= day <= " + maxDay;
        
        throw new IllegalArgumentException(err);
      }
      if (isLeap(this._century, this._year)) {
        err = "day " + day + " is out of range for February in a leap year:  " + "1 <= day <= 29";
        
        throw new IllegalArgumentException(err);
      }
      err = "day " + day + " is out of range for February in a non-leap year:  " + "1 <= day <= 28";
      throw new IllegalArgumentException(err);
    }
    this._day = day;
  }
  
  public void setHour(short hour)
    throws UnsupportedOperationException
  {
    if (hour > 23)
    {
      String err = "hour " + hour + " must be strictly less than 24";
      throw new IllegalArgumentException(err);
    }
    if (hour < 0)
    {
      String err = "hour " + hour + " cannot be negative.";
      throw new IllegalArgumentException(err);
    }
    this._hour = hour;
  }
  public void setMinute(short minute)
    throws UnsupportedOperationException {
    if (minute > 59) {
      String err = "minute " + minute + " must be strictly less than 60.";
      throw new IllegalArgumentException(err);
    }
    if (minute < 0) {
      String err = "minute " + minute + " cannot be negative.";
      throw new IllegalArgumentException(err);
    }
    this._minute = minute;
  }
  public void setSecond(short second, short millsecond)
    throws UnsupportedOperationException {
    setSecond(second);
    setMilliSecond(millsecond);
  }
  public void setSecond(short second)
    throws UnsupportedOperationException
  {
    if (second > 59)
    {
      String err = "seconds " + second + " must be less than 60";
      throw new IllegalArgumentException(err);
    }
    if (second < 0)
    {
      String err = "seconds " + second + " cannot be negative.";
      throw new IllegalArgumentException(err);
    }
    this._second = second;
  }
  
  public void setMilliSecond(short millisecond)
    throws UnsupportedOperationException
  {
    if (millisecond < 0)
    {
      String err = "milliseconds " + millisecond + " cannot be negative.";
      throw new IllegalArgumentException(err);
    }
    if (millisecond > 999)
    {
      String err = "milliseconds " + millisecond + " is out of bounds: 0 <= milliseconds <= 999.";
      throw new IllegalArgumentException(err);
    }
    this._millsecond = millisecond;
  }
  
  public void setUTC()
  {
    this._utc = true;
  }
  
  public void setZoneNegative(boolean zoneNegative)
  {
    this._zoneNegative = zoneNegative;
  }
  
  public void setZone(short hour, short minute)
  {
    setZoneHour(hour);
    setZoneMinute(minute);
  }
  
  public void setZoneHour(short hour)
  {
    if (hour > 23)
    {
      String err = "time zone hour " + hour + " must be strictly less than 24";
      throw new IllegalArgumentException(err);
    }
    if (hour < 0)
    {
      String err = "time zone hour " + hour + " cannot be negative.";
      throw new IllegalArgumentException(err);
    }
    this._zoneHour = hour;
    
    setUTC();
  }
  
  public void setZoneMinute(short minute)
  {
    if (minute > 59)
    {
      String err = "time zone minute " + minute + " must be strictly lower than 60";
      throw new IllegalArgumentException(err);
    }
    if (minute < 0)
    {
      String err = "time zone minute " + minute + " cannot be negative.";
      throw new IllegalArgumentException(err);
    }
    this._zoneMinute = minute;
    
    setUTC();
  }
  
  public boolean isNegative()
    throws UnsupportedOperationException
  {
    return this._isNegative;
  }
  
  public short getCentury()
    throws UnsupportedOperationException
  {
    return this._century;
  }
  
  public short getYear()
    throws UnsupportedOperationException
  {
    return this._year;
  }
  
  public short getMonth()
    throws UnsupportedOperationException
  {
    return this._month;
  }
  
  public short getDay()
    throws UnsupportedOperationException
  {
    return this._day;
  }
  
  public short getHour()
    throws UnsupportedOperationException
  {
    return this._hour;
  }
  
  public short getMinute()
    throws UnsupportedOperationException
  {
    return this._minute;
  }
  
  public short getSeconds()
    throws UnsupportedOperationException
  {
    return this._second;
  }
  
  public short getMilli()
    throws UnsupportedOperationException
  {
    return this._millsecond;
  }
  
  public boolean isUTC()
  {
    return this._utc;
  }
  
  public boolean isZoneNegative()
  {
    return this._zoneNegative;
  }
  
  public short getZoneHour()
  {
    return this._zoneHour;
  }
  
  public short getZoneMinute()
  {
    return this._zoneMinute;
  }
  
  public boolean hasIsNegative()
  {
    return true;
  }
  
  public boolean hasCentury()
  {
    return true;
  }
  
  public boolean hasYear()
  {
    return true;
  }
  
  public boolean hasMonth()
  {
    return true;
  }
  
  public boolean hasDay()
  {
    return true;
  }
  
  public boolean hasHour()
  {
    return true;
  }
  
  public boolean hasMinute()
  {
    return true;
  }
  
  public boolean hasSeconds()
  {
    return true;
  }
  
  public boolean hasMilli()
  {
    return true;
  }
  
  public void addDuration(Duration duration)
  {
    int temp = 0;
    int carry = 0;
    int sign = duration.isNegative() ? -1 : 1;
    try
    {
      temp = this._month + sign * duration.getMonth();
      carry = fQuotient(temp - 1, 12);
      setMonth((short)(modulo(temp - 1, 12) + 1));
    }
    catch (UnsupportedOperationException e) {}
    try
    {
      temp = this._century * 100 + this._year + sign * duration.getYear() + carry;
      setCentury((short)(temp / 100));
      setYear((short)(temp % 100));
    }
    catch (UnsupportedOperationException e) {}
    int tempDay = this._day;
    if (tempDay < 1)
    {
      tempDay = 1;
    }
    else
    {
      int maxDay = maxDayInMonthFor(this._century, this._year, this._month);
      if (this._day > maxDay) {
        tempDay = maxDay;
      }
    }
    try
    {
      temp = this._millsecond + sign * (int)duration.getMilli();
      carry = fQuotient(temp, 1000);
      setMilliSecond((short)modulo(temp, 1000));
      
      temp = this._second + sign * duration.getSeconds() + carry;
      carry = fQuotient(temp, 60);
      setSecond((short)modulo(temp, 60));
    }
    catch (UnsupportedOperationException e) {}
    try
    {
      temp = this._minute + sign * duration.getMinute() + carry;
      carry = fQuotient(temp, 60);
      setMinute((short)modulo(temp, 60));
    }
    catch (UnsupportedOperationException e) {}
    try
    {
      temp = this._hour + sign * duration.getHour() + carry;
      carry = fQuotient(temp, 24);
      setHour((short)modulo(temp, 24));
    }
    catch (UnsupportedOperationException e) {}
    try
    {
      tempDay += sign * duration.getDay() + carry;
      for (;;)
      {
        short maxDay = maxDayInMonthFor(this._century, this._year, this._month);
        if (tempDay < 1)
        {
          tempDay = (short)(tempDay + maxDayInMonthFor(this._century, this._year, this._month - 1));
          carry = -1;
        }
        else
        {
          if (tempDay <= maxDay) {
            break;
          }
          tempDay = (short)(tempDay - maxDay);
          carry = 1;
        }
        try
        {
          temp = this._month + carry;
          setMonth((short)(modulo(temp - 1, 12) + 1));
          temp = getCentury() * 100 + getYear() + fQuotient(temp - 1, 12);
          setCentury((short)(temp / 100));
          setYear((short)(temp % 100));
        }
        catch (UnsupportedOperationException e) {}
      }
      setDay((short)tempDay);
    }
    catch (UnsupportedOperationException e) {}
  }
  
  private int fQuotient(int a, int b)
  {
    return (int)Math.floor(a / b);
  }
  
  private int modulo(int a, int b)
  {
    return a - fQuotient(a, b) * b;
  }
  
  private final short maxDayInMonthFor(short century, short year, int month)
  {
    if ((month == 4) || (month == 6) || (month == 9) || (month == 11)) {
      return 30;
    }
    if (month == 2) {
      return (short)(isLeap(century, year) ? 29 : 28);
    }
    return 31;
  }
  
  public void normalize()
  {
    if ((!isUTC()) || ((this._zoneHour == 0) && (this._zoneMinute == 0))) {
      return;
    }
    Duration temp = new Duration();
    temp.setHour(this._zoneHour);
    temp.setMinute(this._zoneMinute);
    if (!isZoneNegative()) {
      temp.setNegative();
    }
    addDuration(temp);
    
    setZone((short)0, (short)0);
    setZoneNegative(false);
  }
  
  public int compareTo(DateTimeBase dateTime)
  {
    if (dateTime == null) {
      throw new IllegalArgumentException("a Date/Time datatype cannot be compared with a null value");
    }
    DateTimeBase tempDate1;
    DateTimeBase tempDate2;
    try
    {
      tempDate1 = clone(this);
      if (tempDate1.isUTC()) {
        tempDate1.normalize();
      }
      tempDate2 = clone(dateTime);
      if (tempDate2.isUTC()) {
        tempDate2.normalize();
      }
    }
    catch (CloneNotSupportedException e)
    {
      throw new RuntimeException("Unexpected 'clone not supported' Exception");
    }
    if (tempDate1.isUTC() == tempDate2.isUTC()) {
      return compareFields(tempDate1, tempDate2);
    }
    if (tempDate1.isUTC())
    {
      tempDate2.setZone((short)14, (short)0);
      tempDate2.normalize();
      int result = compareFields(tempDate1, tempDate2);
      if (result == 0) {
        return result;
      }
      tempDate2.setZone((short)14, (short)0);
      tempDate2.setZoneNegative(true);
      tempDate2.normalize();
      
      tempDate2.setZone((short)14, (short)0);
      tempDate2.setZoneNegative(true);
      tempDate2.normalize();
      result = compareFields(tempDate1, tempDate2);
      if (result == 2) {
        return result;
      }
      return -1;
    }
    if (tempDate2.isUTC())
    {
      tempDate1.setZone((short)14, (short)0);
      tempDate1.normalize();
      int result = compareFields(tempDate1, tempDate2);
      if (result == 2) {
        return result;
      }
      tempDate1.setZone((short)14, (short)0);
      tempDate1.setZoneNegative(true);
      tempDate1.normalize();
      
      tempDate1.setZone((short)14, (short)0);
      tempDate1.setZoneNegative(true);
      tempDate1.normalize();
      result = compareFields(tempDate1, tempDate2);
      if (result == 0) {
        return result;
      }
      return -1;
    }
    return -1;
  }
  
  private DateTimeBase copyDateTimeInstance(DateTimeBase dateTime)
  {
    DateTimeBase newDateTime = new DateTime();
    newDateTime._isNegative = dateTime._isNegative;
    newDateTime._century = dateTime._century;
    newDateTime._year = dateTime._year;
    newDateTime._month = dateTime._month;
    newDateTime._day = dateTime._day;
    newDateTime._hour = dateTime._hour;
    newDateTime._minute = dateTime._minute;
    newDateTime._second = dateTime._second;
    newDateTime._millsecond = dateTime._millsecond;
    newDateTime._zoneNegative = dateTime._zoneNegative;
    newDateTime._utc = dateTime._utc;
    newDateTime._zoneHour = dateTime._zoneHour;
    newDateTime._zoneMinute = dateTime._zoneMinute;
    return newDateTime;
  }
  
  public DateTimeBase clone(DateTimeBase dateTime)
    throws CloneNotSupportedException
  {
    DateTimeBase newDateTime = (DateTimeBase)super.clone();
    
    newDateTime.setValues(dateTime.getValues());
    if ((dateTime.hasIsNegative()) && (dateTime.isNegative())) {
      newDateTime.setNegative();
    }
    if (dateTime.isUTC())
    {
      newDateTime.setUTC();
      newDateTime.setZone(dateTime.getZoneHour(), dateTime.getZoneMinute());
      newDateTime.setZoneNegative(dateTime.isZoneNegative());
    }
    return newDateTime;
  }
  
  private static int compareFields(DateTimeBase date1, DateTimeBase date2)
  {
    if (date1.hasCentury() != date2.hasCentury()) {
      return -1;
    }
    if ((date1.hasCentury()) && (date2.hasCentury()))
    {
      short field1 = date1.getCentury();
      short field2 = date2.getCentury();
      if (field1 < field2) {
        return 0;
      }
      if (field1 > field2) {
        return 2;
      }
    }
    if (date1.hasYear() != date2.hasYear()) {
      return -1;
    }
    if ((date1.hasYear()) && (date2.hasYear()))
    {
      short field1 = date1.getYear();
      short field2 = date2.getYear();
      if (field1 < field2) {
        return 0;
      }
      if (field1 > field2) {
        return 2;
      }
    }
    if (date1.hasMonth() != date2.hasMonth()) {
      return -1;
    }
    if ((date1.hasMonth()) && (date2.hasMonth()))
    {
      short field1 = date1.getMonth();
      short field2 = date2.getMonth();
      if (field1 < field2) {
        return 0;
      }
      if (field1 > field2) {
        return 2;
      }
    }
    if (date1.hasDay() != date2.hasDay()) {
      return -1;
    }
    if ((date1.hasDay()) && (date2.hasDay()))
    {
      short field1 = date1.getDay();
      short field2 = date2.getDay();
      if (field1 < field2) {
        return 0;
      }
      if (field1 > field2) {
        return 2;
      }
    }
    if (date1.hasHour() != date2.hasHour()) {
      return -1;
    }
    if ((date1.hasHour()) && (date2.hasHour())) {
      short field1 = date1.getHour();
      short field2 = date2.getHour();
      if (field1 < field2) {
        return 0;
      }
      if (field1 > field2) {
        return 2;
      }
    }
    if (date1.hasMinute() != date2.hasMinute()) {
      return -1;
    }
    if ((date1.hasMinute()) && (date2.hasMinute()))
    {
      short field1 = date1.getMinute();
      short field2 = date2.getMinute();
      if (field1 < field2) {
        return 0;
      }
      if (field1 > field2) {
        return 2;
      }
    }
    if (date1.hasSeconds() != date2.hasSeconds()) {
      return -1;
    }
    if ((date1.hasSeconds()) && (date2.hasSeconds())) {
      short field1 = date1.getSeconds();
      short field2 = date2.getSeconds();
      if (field1 < field2) {
        return 0;
      }
      if (field1 > field2) {
        return 2;
      }
    }
    if (date1.hasMilli() != date2.hasMilli()) {
      return -1;
    }
    if ((date1.hasMilli()) && (date2.hasMilli()))
    {
      short field1 = date1.getMilli();
      short field2 = date2.getMilli();
      if (field1 < field2) {
        return 0;
      }
      if (field1 > field2) {
        return 2;
      }
    }
    return 1;
  }
  public int hashCode() {
    return this._year ^ this._month ^ this._day ^ this._hour ^ this._minute ^ this._second ^ this._millsecond ^ this._zoneHour ^ this._zoneMinute;
  }
  public boolean equals(Object object) {
    if ((object instanceof DateTimeBase)) {
      return equal((DateTimeBase)object);
    }
    return false;
  }
  protected boolean equal(DateTimeBase dateTime)
  {
    return 1 == compareTo(dateTime);
  }
  public Calendar toCalendar() {
    Calendar result = new GregorianCalendar();
    result.setTime(toDate());
    return result;
  }
  protected static int parseYear(String str, DateTimeBase result, char[] chars, int index, String complaint)
    throws ParseException {
    int idx = index;
    if (chars[idx] == '-') {
      idx++;
      result.setNegative();
    }
    if ((str.length() < idx + 4) || (!Character.isDigit(chars[idx])) || (!Character.isDigit(chars[(idx + 1)])) || (!Character.isDigit(chars[(idx + 2)])) || (!Character.isDigit(chars[(idx + 3)]))) {
      throw new ParseException(complaint + str + "\nThe Year must be 4 digits long", idx);
    }
    short value1 = (short)((chars[idx] - '0') * 10 + (chars[(idx + 1)] - '0'));
    short value2 = (short)((chars[(idx + 2)] - '0') * 10 + (chars[(idx + 3)] - '0'));
    if ((value1 == 0) && (value2 == 0)) {
      throw new ParseException(complaint + str + "\n'0000' is not allowed as a year.", idx);
    }
    result.setCentury(value1);
    result.setYear(value2);
    idx += 4;
    return idx;
  }
  protected static int parseMonth(String str, DateTimeBase result, char[] chars, int index, String complaint)
    throws ParseException {
    int idx = index;
    if (chars[idx] != '-') {
      throw new ParseException(complaint + str + "\n '-' " + " is wrongly placed.", idx);
    }
    idx++;
    if ((str.length() < idx + 2) || (!Character.isDigit(chars[idx])) || (!Character.isDigit(chars[(idx + 1)]))) {
      throw new ParseException(complaint + str + "\nThe Month must be 2 digits long", idx);
    }
    short value1 = (short)((chars[idx] - '0') * 10 + (chars[(idx + 1)] - '0'));
    result.setMonth(value1);
    idx += 2;
    return idx;
  }
  protected static int parseDay(String str, DateTimeBase result, char[] chars, int index, String complaint)
    throws ParseException {
    int idx = index;
    if (chars[idx] != '-') {
      throw new ParseException(complaint + str + "\n '-' " + " is wrongly placed.", idx);
    }
    idx++;
    if ((str.length() < idx + 2) || (!Character.isDigit(chars[idx])) || (!Character.isDigit(chars[(idx + 1)]))) {
      throw new ParseException(complaint + str + "\nThe Day must be 2 digits long", idx);
    }
    short value1 = (short)((chars[idx] - '0') * 10 + (chars[(idx + 1)] - '0'));
    result.setDay(value1);
    idx += 2;
    return idx;
  }
  protected static int parseTime(String str, DateTimeBase result, char[] chars, int index, String complaint)
    throws ParseException {
    int idx = index;
    if (str.length() < idx + 8) {
      throw new ParseException(complaint + str + "\nA Time field must be at least 8 characters long", idx);
    }
    if ((!Character.isDigit(chars[idx])) || (!Character.isDigit(chars[(idx + 1)]))) {
      throw new ParseException(complaint + str + "\nThe Hour must be 2 digits long", idx);
    }
    short value1 = (short)((chars[idx] - '0') * 10 + (chars[(idx + 1)] - '0'));
    result.setHour(value1);
    idx += 2;
    if (chars[idx] != ':') {
      throw new ParseException(complaint + str + "\n ':#1' " + " is wrongly placed.", idx);
    }
    idx++;
    if ((!Character.isDigit(chars[idx])) || (!Character.isDigit(chars[(idx + 1)]))) {
      throw new ParseException(complaint + str + "\nThe Minute must be 2 digits long", idx);
    }
    value1 = (short)((chars[idx] - '0') * 10 + (chars[(idx + 1)] - '0'));
    result.setMinute(value1);
    idx += 2;
    if (chars[idx] != ':') {
      throw new ParseException(complaint + str + "\n ':#2' " + " is wrongly placed.", idx);
    }
    idx++;
    if ((!Character.isDigit(chars[idx])) || (!Character.isDigit(chars[(idx + 1)]))) {
      throw new ParseException(complaint + str + "\nThe Second must be 2 digits long", idx);
    }
    value1 = (short)((chars[idx] - '0') * 10 + (chars[(idx + 1)] - '0'));
    result.setSecond(value1);
    idx += 2;
    if ((idx < chars.length) && (chars[idx] == '.')) {
      idx++;
      long decimalValue = 0L;
      long powerOfTen = 1L;
      while ((idx < chars.length) && (Character.isDigit(chars[idx]))) {
        decimalValue = decimalValue * 10L + (chars[idx] - '0');
        powerOfTen *= 10L;
        idx++;
      }
      if (powerOfTen > 1000L) {
        decimalValue /= powerOfTen / 1000L;
        powerOfTen = 1000L;
      }
      else if (powerOfTen < 1000L) {
        decimalValue *= 1000L / powerOfTen;
        powerOfTen = 1000L;
      }
      result.setMilliSecond((short)(int)decimalValue);
    }
    return idx;
  }
  protected static int parseTimeZone(String str, DateTimeBase result, char[] chars, int index, String complaint)
    throws ParseException {
    if (index >= chars.length) {
      return index;
    }
    int idx = index;
    if (chars[idx] == 'Z') {
      result.setUTC();
      idx++;return idx;
    }
    if ((chars[idx] == '+') || (chars[idx] == '-')) {
      if (chars[idx] == '-') {
        result.setZoneNegative(true);
      }
      idx++;
      if ((idx + 5 > chars.length) || (chars[(idx + 2)] != ':') || (!Character.isDigit(chars[idx])) || (!Character.isDigit(chars[(idx + 1)])) || (!Character.isDigit(chars[(idx + 3)])) || (!Character.isDigit(chars[(idx + 4)]))) {
        throw new ParseException(complaint + str + "\nTimeZone must have the format (+/-)hh:mm", idx);
      }
      short value1 = (short)((chars[idx] - '0') * 10 + (chars[(idx + 1)] - '0'));
      short value2 = (short)((chars[(idx + 3)] - '0') * 10 + (chars[(idx + 4)] - '0'));
      result.setZone(value1, value2);
      idx += 5;
    }
    return idx;
  }
  protected void setDateFormatTimeZone(DateFormat df) {
    if (!isUTC()) {
      return;
    }
    int offset = (getZoneMinute() + getZoneHour() * 60) * 60 * 1000;
    offset = isZoneNegative() ? -offset : offset;
    
    SimpleTimeZone timeZone = new SimpleTimeZone(0, "UTC");
    timeZone.setRawOffset(offset);
    timeZone.setID(TimeZone.getAvailableIDs(offset)[0]);
    df.setTimeZone(timeZone);
  }
  protected void setDateFormatTimeZone(Calendar calendar) {
    if (!isUTC()) {
      return;
    }
    int offset = (getZoneMinute() + getZoneHour() * 60) * 60 * 1000;
    offset = isZoneNegative() ? -offset : offset;
    SimpleTimeZone timeZone = new SimpleTimeZone(0, "UTC");
    timeZone.setRawOffset(offset);
    String[] availableIDs = TimeZone.getAvailableIDs(offset);
    if ((availableIDs != null) && (availableIDs.length > 0)) {
      timeZone.setID(availableIDs[0]);
    }
    calendar.setTimeZone(timeZone);
  }
  protected void appendDateString(StringBuffer result) {
    if (isNegative()) {
      result.append('-');
    }
    if (getCentury() / 10 == 0) {
      result.append(0);
    }
    result.append(getCentury());
    if (getYear() / 10 == 0) {
      result.append(0);
    }
    result.append(getYear());
    result.append('-');
    if (getMonth() / 10 == 0) {
      result.append(0);
    }
    result.append(getMonth());
    result.append('-');
    if (getDay() / 10 == 0) {
      result.append(0);
    }
    result.append(getDay());
  }
  protected void appendTimeString(StringBuffer result) {
    if (getHour() / 10 == 0) {
      result.append(0);
    }
    result.append(getHour());
    result.append(':');
    if (getMinute() / 10 == 0) {
      result.append(0);
    }
    result.append(getMinute());
    result.append(':');
    if (getSeconds() / 10 == 0) {
      result.append(0);
    }
    result.append(getSeconds());
    if (getMilli() != 0) {
      result.append('.');
      if (getMilli() < 100) {
        result.append('0');
        if (getMilli() < 10) {
          result.append('0');
        }
      }
      result.append(getMilli());
    }
  }
  protected void appendTimeZoneString(StringBuffer result) {
    if (!isUTC()) {
      return;
    }
    if ((getZoneHour() == 0) && (getZoneMinute() == 0)) {
      result.append('Z');
      return;
    }
    if (isZoneNegative()) {
      result.append('-');
    } else {
      result.append('+');
    }
    if (getZoneHour() / 10 == 0) {
      result.append(0);
    }
    result.append(getZoneHour());
    result.append(':');
    if (getZoneMinute() / 10 == 0) {
      result.append(0);
    }
    result.append(getZoneMinute());
  }
}
