package org.exolab.xml.handlers;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import org.exolab.mapping.GeneralizedFieldHandler;

public class SQLTimestampFieldHandler extends GeneralizedFieldHandler {
  private static final int OFFSET_OF_LITERAL_T = 10;
  public Object convertUponGet(Object value)
  {
    return value;
  }
  public Object convertUponSet(Object value) {
    if (value == null) {
      return null;
    }
    Timestamp timestamp = null;
    String str = value.toString();
    if (str.indexOf('T') == 10) {
      try {
        Date date = DateFieldHandler.parse(str);
        timestamp = new Timestamp(date.getTime());
      }
      catch (ParseException px) {
        throw new IllegalStateException(px.getMessage());
      }
    } else {
      timestamp = Timestamp.valueOf(str);
    }
    return timestamp;
  }
  public Class getFieldType()
  {
    return Timestamp.class;
  }
  public Object newInstance(Object parent)
    throws IllegalStateException {
    return new Timestamp(0L);
  }
}
