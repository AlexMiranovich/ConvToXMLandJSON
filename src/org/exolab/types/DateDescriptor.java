package org.exolab.types;

import java.text.ParseException;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.ValidityException;
import org.exolab.xml.NodeType;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.util.XMLFieldDescriptorImpl;

public class DateDescriptor extends BaseDescriptor {
  private static final String XML_NAME = "date";
  private static final XMLFieldDescriptorImpl CONTENT_DESCRIPTOR;
  private static final FieldDescriptor[] FIELDS;

  public DateDescriptor() {
    super("date", Date.class);
  }

  public XMLFieldDescriptor getContentDescriptor() {
    return CONTENT_DESCRIPTOR;
  }

  public FieldDescriptor[] getFields() {
    return FIELDS;
  }

  static {
    CONTENT_DESCRIPTOR = new XMLFieldDescriptorImpl(String.class, "content", "content", NodeType.Text);
    CONTENT_DESCRIPTOR.setHandler(new DateDescriptor().new DateFieldHandler());
    FIELDS = new FieldDescriptor[1];
    FIELDS[0] = CONTENT_DESCRIPTOR;
  }

  class DateFieldHandler extends XMLFieldHandler {
    public DateFieldHandler() {
    }

    public Object getValue(Object target) throws IllegalStateException {
      Date date = (Date)target;
      return date.toString();
    }

    public void setValue(Object target, Object value) throws IllegalStateException {
      if (!(target instanceof Date)) {
        String err = "DateDescriptor#setValue: expected Date, received instead: " + target.getClass();
        throw new IllegalStateException(err);
      } else {
        Date dateTarget = (Date)target;
        if (value == null) {
          String errx = "DateDescriptor#setValue: null value.";
          throw new IllegalStateException(errx);
        } else {
          try {
            Date temp = Date.parseDate(value.toString());
            dateTarget.setCentury(temp.getCentury());
            dateTarget.setYear(temp.getYear());
            dateTarget.setMonth(temp.getMonth());
            dateTarget.setDay(temp.getDay());
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
      return new Date();
    }
  }
}
