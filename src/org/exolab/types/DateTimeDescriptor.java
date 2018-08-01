package org.exolab.types;

import java.text.ParseException;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.ValidityException;
import org.exolab.xml.NodeType;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.util.XMLFieldDescriptorImpl;

public class DateTimeDescriptor extends BaseDescriptor {
  private static final String XML_NAME = "dateTime";
  private static final XMLFieldDescriptorImpl CONTENT_DESCRIPTOR;
  private static final FieldDescriptor[] FIELDS;

  public DateTimeDescriptor() {
    super("dateTime", DateTime.class);
  }

  public XMLFieldDescriptor getContentDescriptor() {
    return CONTENT_DESCRIPTOR;
  }

  public FieldDescriptor[] getFields() {
    return FIELDS;
  }

  static {
    CONTENT_DESCRIPTOR = new XMLFieldDescriptorImpl(String.class, "content", "content", NodeType.Text);
    CONTENT_DESCRIPTOR.setHandler(new DateTimeDescriptor().new DateTimeFieldHandler());
    FIELDS = new FieldDescriptor[1];
    FIELDS[0] = CONTENT_DESCRIPTOR;
  }

  class DateTimeFieldHandler extends XMLFieldHandler {
    public DateTimeFieldHandler() {
    }

    public Object getValue(Object target) throws IllegalStateException {
      DateTime date = (DateTime)target;
      return date.toString();
    }

    public void setValue(Object target, Object value) throws IllegalStateException {
      if (!(target instanceof DateTime)) {
        String err = "DateTimeDescriptor#setValue: expected DateTime, received instead: " + target.getClass();
        throw new IllegalStateException(err);
      } else {
        DateTime dateTarget = (DateTime)target;
        if (value == null) {
          String errx = "DateTimeDescriptor#setValue: null value.";
          throw new IllegalStateException(errx);
        } else {
          try {
            DateTime temp = DateTime.parseDateTime(value.toString());
            dateTarget.setCentury(temp.getCentury());
            dateTarget.setYear(temp.getYear());
            dateTarget.setMonth(temp.getMonth());
            dateTarget.setDay(temp.getDay());
            dateTarget.setHour(temp.getHour());
            dateTarget.setMinute(temp.getMinute());
            dateTarget.setSecond(temp.getSeconds(), temp.getMilli());
            if (temp.isUTC()) {
              dateTarget.setUTC();
              dateTarget.setZone(temp.getZoneHour(), temp.getZoneMinute());
              dateTarget.setZoneNegative(temp.isZoneNegative());
            }

          } catch (ParseException var6) {
            String errxx = "DateDescriptor#setValue: wrong value\n" + var6.getMessage();
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
      return new DateTime();
    }
  }
}
