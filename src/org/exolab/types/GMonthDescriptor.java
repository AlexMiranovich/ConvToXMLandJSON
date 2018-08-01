package org.exolab.types;

import java.text.ParseException;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.ValidityException;
import org.exolab.xml.NodeType;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.util.XMLFieldDescriptorImpl;

public class GMonthDescriptor extends BaseDescriptor {
  private static final String XML_NAME = "gMonth";
  private static final XMLFieldDescriptorImpl CONTENT_DESCRIPTOR;
  private static final FieldDescriptor[] FIELDS;

  public GMonthDescriptor() {
    super("gMonth", GMonth.class);
  }

  public XMLFieldDescriptor getContentDescriptor() {
    return CONTENT_DESCRIPTOR;
  }

  public FieldDescriptor[] getFields() {
    return FIELDS;
  }

  static {
    CONTENT_DESCRIPTOR = new XMLFieldDescriptorImpl(String.class, "content", "content", NodeType.Text);
    CONTENT_DESCRIPTOR.setHandler(new GMonthDescriptor().new GMonthFieldHandler());
    FIELDS = new FieldDescriptor[1];
    FIELDS[0] = CONTENT_DESCRIPTOR;
  }

  class GMonthFieldHandler extends XMLFieldHandler {
    public GMonthFieldHandler() {
    }

    public Object getValue(Object target) throws IllegalStateException {
      Object result = null;
      if (target instanceof GMonth) {
        result = (GMonth)target;
      }

      return result;
    }

    public void setValue(Object target, Object value) throws IllegalStateException {
      if (!(target instanceof GMonth)) {
        String err = "GMonthDescriptor#setValue: expected GMonth, received instead: " + target.getClass();
        throw new IllegalStateException(err);
      } else {
        GMonth GMonthTarget = (GMonth)target;
        if (value == null) {
          String errx = "GMonthDescriptor#setValue: null value";
          throw new IllegalStateException(errx);
        } else {
          try {
            GMonth temp = GMonth.parseGMonth(value.toString());
            GMonthTarget.setMonth(temp.getMonth());
            if (temp.isUTC()) {
              GMonthTarget.setUTC();
              GMonthTarget.setZone(temp.getZoneHour(), temp.getZoneMinute());
              GMonthTarget.setZoneNegative(temp.isZoneNegative());
            }

            temp = null;
          } catch (ParseException var6) {
            String errxx = "GMonthDescriptor#setValue: wrong value\n" + var6.getMessage();
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
      return new GMonth();
    }
  }
}
