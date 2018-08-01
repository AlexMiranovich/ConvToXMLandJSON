package org.exolab.types;

import java.text.ParseException;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.ValidityException;
import org.exolab.xml.NodeType;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.util.XMLFieldDescriptorImpl;

public class GYearMonthDescriptor extends BaseDescriptor {
  private static final String XML_NAME = "gYearMonth";
  private static final XMLFieldDescriptorImpl CONTENT_DESCRIPTOR;
  private static final FieldDescriptor[] FIELDS;

  public GYearMonthDescriptor() {
    super("gYearMonth", GYearMonth.class);
  }

  public XMLFieldDescriptor getContentDescriptor() {
    return CONTENT_DESCRIPTOR;
  }

  public FieldDescriptor[] getFields() {
    return FIELDS;
  }

  static {
    CONTENT_DESCRIPTOR = new XMLFieldDescriptorImpl(String.class, "content", "content", NodeType.Text);
    CONTENT_DESCRIPTOR.setHandler(new GYearMonthDescriptor().new GYearMonthFieldHandler());
    FIELDS = new FieldDescriptor[1];
    FIELDS[0] = CONTENT_DESCRIPTOR;
  }

  class GYearMonthFieldHandler extends XMLFieldHandler {
    public GYearMonthFieldHandler() {
    }

    public Object getValue(Object target) throws IllegalStateException {
      Object result = null;
      if (target instanceof GYearMonth) {
        result = (GYearMonth)target;
      }

      return result;
    }

    public void setValue(Object target, Object value) throws IllegalStateException {
      if (!(target instanceof GYearMonth)) {
        String err = "GYearMonthDescriptor#setValue: expected GYearMonth, received instead: " + target.getClass();
        throw new IllegalStateException(err);
      } else {
        GYearMonth GYearMonthTarget = (GYearMonth)target;
        if (value == null) {
          String errx = "GYearMonthDescriptor#setValue: null value";
          throw new IllegalStateException(errx);
        } else {
          try {
            GYearMonth temp = GYearMonth.parseGYearMonth(value.toString());
            GYearMonthTarget.setCentury(temp.getCentury());
            GYearMonthTarget.setYear(temp.getYear());
            GYearMonthTarget.setMonth(temp.getMonth());
            if (temp.isUTC()) {
              GYearMonthTarget.setUTC();
              GYearMonthTarget.setZone(temp.getZoneHour(), temp.getZoneMinute());
              GYearMonthTarget.setZoneNegative(temp.isZoneNegative());
            }

            temp = null;
          } catch (ParseException var6) {
            String errxx = "GYearMonthDescriptor#setValue: wrong value\n" + var6.getMessage();
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
      return new GYearMonth();
    }
  }
}
