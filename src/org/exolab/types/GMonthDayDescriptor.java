package org.exolab.types;

import java.text.ParseException;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.ValidityException;
import org.exolab.xml.NodeType;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.util.XMLFieldDescriptorImpl;

public class GMonthDayDescriptor extends BaseDescriptor {
  private static final String XML_NAME = "gMonthDay";
  private static final XMLFieldDescriptorImpl CONTENT_DESCRIPTOR;
  private static final FieldDescriptor[] FIELDS;

  public GMonthDayDescriptor() {
    super("gMonthDay", GMonthDay.class);
  }

  public XMLFieldDescriptor getContentDescriptor() {
    return CONTENT_DESCRIPTOR;
  }

  public FieldDescriptor[] getFields() {
    return FIELDS;
  }

  static {
    CONTENT_DESCRIPTOR = new XMLFieldDescriptorImpl(String.class, "content", "content", NodeType.Text);
    CONTENT_DESCRIPTOR.setHandler(new GMonthDayDescriptor().new GMonthDayFieldHandler());
    FIELDS = new FieldDescriptor[1];
    FIELDS[0] = CONTENT_DESCRIPTOR;
  }

  class GMonthDayFieldHandler extends XMLFieldHandler {
    public GMonthDayFieldHandler() {
    }

    public Object getValue(Object target) throws IllegalStateException {
      Object result = null;
      if (target instanceof GMonthDay) {
        result = (GMonthDay)target;
      }

      return result;
    }

    public void setValue(Object target, Object value) throws IllegalStateException {
      if (!(target instanceof GMonthDay)) {
        String err = "GMonthDayDescriptor#setValue: expected GMonthDay, received instead: " + target.getClass();
        throw new IllegalStateException(err);
      } else {
        GMonthDay gMonthDayTarget = (GMonthDay)target;
        if (value == null) {
          String errx = "GMonthDayDescriptor#setValue: null value";
          throw new IllegalStateException(errx);
        } else {
          try {
            GMonthDay temp = GMonthDay.parseGMonthDay(value.toString());
            gMonthDayTarget.setMonth(temp.getMonth());
            gMonthDayTarget.setDay(temp.getDay());
            if (temp.isUTC()) {
              gMonthDayTarget.setUTC();
              gMonthDayTarget.setZone(temp.getZoneHour(), temp.getZoneMinute());
              gMonthDayTarget.setZoneNegative(temp.isZoneNegative());
            }

            temp = null;
          } catch (ParseException var6) {
            String errxx = "GMonthDayDescriptor#setValue: wrong value\n" + var6.getMessage();
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
      return new GMonthDay();
    }
  }
}
