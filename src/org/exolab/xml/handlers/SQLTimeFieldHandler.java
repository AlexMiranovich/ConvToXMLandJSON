package org.exolab.xml.handlers;

import java.sql.Time;
import java.text.ParseException;
import java.util.Date;
import org.exolab.mapping.GeneralizedFieldHandler;

public class SQLTimeFieldHandler extends GeneralizedFieldHandler {
  public Object convertUponGet(Object value)
  {
    return value;
  }
  public Object convertUponSet(Object value) {
    if (value == null) {
      return null;
    }
    String str = value.toString();
    Time time = null;
    if (str.indexOf(':') == 2) {
      time = Time.valueOf(str);
    } else {
      try {
        Date date = DateFieldHandler.parse(str);
        time = new Time(date.getTime());
      }
      catch (ParseException px) {
        throw new IllegalStateException(px.getMessage());
      }
    }
    return time;
  }
  public Class getFieldType()
  {
    return Time.class;
  }
  public Object newInstance(Object parent)
    throws IllegalStateException {
    return new Time(0L);
  }
}
