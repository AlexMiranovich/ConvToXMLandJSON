package org.exolab.types;

import java.text.ParseException;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.ValidityException;
import org.exolab.xml.NodeType;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.util.XMLFieldDescriptorImpl;

public class TimeDescriptor extends BaseDescriptor {
  private static final String XML_NAME = "time";
  private static final XMLFieldDescriptorImpl CONTENT_DESCRIPTOR;
  private static final FieldDescriptor[] FIELDS;

  public TimeDescriptor() {
    super("time", Time.class);
  }

  public XMLFieldDescriptor getContentDescriptor() {
    return CONTENT_DESCRIPTOR;
  }

  public FieldDescriptor[] getFields() {
    return FIELDS;
  }

  static {
    CONTENT_DESCRIPTOR = new XMLFieldDescriptorImpl(String.class, "content", "content", NodeType.Text);
    CONTENT_DESCRIPTOR.setHandler(new TimeDescriptor().new TimeFieldHandler());
    FIELDS = new FieldDescriptor[1];
    FIELDS[0] = CONTENT_DESCRIPTOR;
  }

  class TimeFieldHandler extends XMLFieldHandler {
    public TimeFieldHandler() {
    }

    public Object getValue(Object target) throws IllegalStateException {
      Time time = (Time)target;
      return time.toString();
    }

    public void setValue(Object target, Object value) throws IllegalStateException {
      if (!(target instanceof Time)) {
        String err = "TimeDescriptor#setValue: expected Date, received instead: " + target.getClass();
        throw new IllegalStateException(err);
      } else {
        Time timeTarget = (Time)target;
        if (value == null) {
          String errx = "TimeDescriptor#setValue: null value.";
          throw new IllegalStateException(errx);
        } else {
          try {
            Time temp = Time.parseTime(value.toString());
            timeTarget.setHour(temp.getHour());
            timeTarget.setMinute(temp.getMinute());
            timeTarget.setSecond(temp.getSeconds(), temp.getMilli());
            if (temp.isUTC()) {
              timeTarget.setUTC();
              timeTarget.setZone(temp.getZoneHour(), temp.getZoneMinute());
              timeTarget.setZoneNegative(temp.isZoneNegative());
            }

          } catch (ParseException var6) {
            String errxx = "TimeDescriptor#setValue: wrong value\n" + var6.getMessage();
            throw new IllegalStateException(errxx);
          }
        }
      }
    }

    public void resetValue(Object target) throws IllegalStateException {
    }

    public void checkValidity(Object object) throws ValidityException, IllegalStateException {
    }

    public Object newInstance(Object parent) throws IllegalStateException {
      return new Time();
    }
  }
}
