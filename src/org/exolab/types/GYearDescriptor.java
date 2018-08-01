package org.exolab.types;

import java.text.ParseException;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.ValidityException;
import org.exolab.xml.NodeType;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.util.XMLFieldDescriptorImpl;

public class GYearDescriptor extends BaseDescriptor {
  private static final String XML_NAME = "gYear";
  private static final XMLFieldDescriptorImpl CONTENT_DESCRIPTOR;
  private static final FieldDescriptor[] FIELDS;

  public GYearDescriptor() {
    super("gYear", GYear.class);
  }

  public XMLFieldDescriptor getContentDescriptor() {
    return CONTENT_DESCRIPTOR;
  }

  public FieldDescriptor[] getFields() {
    return FIELDS;
  }

  static {
    CONTENT_DESCRIPTOR = new XMLFieldDescriptorImpl(String.class, "content", "content", NodeType.Text);
    CONTENT_DESCRIPTOR.setHandler(new GYearDescriptor().new GYearFieldHandler());
    FIELDS = new FieldDescriptor[1];
    FIELDS[0] = CONTENT_DESCRIPTOR;
  }

  class GYearFieldHandler extends XMLFieldHandler {
    public GYearFieldHandler() {
    }

    public Object getValue(Object target) throws IllegalStateException {
      Object result = null;
      if (target instanceof GYear) {
        result = (GYear)target;
      }

      return result;
    }

    public void setValue(Object target, Object value) throws IllegalStateException {
      if (!(target instanceof GYear)) {
        String err = "GYearDescriptor#setValue: expected GYear, received instead: " + target.getClass();
        throw new IllegalStateException(err);
      } else {
        GYear GYearTarget = (GYear)target;
        if (value == null) {
          String errx = "GYearDescriptor#setValue: null value";
          throw new IllegalStateException(errx);
        } else {
          try {
            GYear temp = GYear.parseGYear(value.toString());
            GYearTarget.setCentury(temp.getCentury());
            GYearTarget.setYear(temp.getYear());
            if (temp.isUTC()) {
              GYearTarget.setUTC();
              GYearTarget.setZone(temp.getZoneHour(), temp.getZoneMinute());
              GYearTarget.setZoneNegative(temp.isZoneNegative());
            }

            temp = null;
          } catch (ParseException var6) {
            String errxx = "GYearDescriptor#setValue: wrong value\n" + var6.getMessage();
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
      return new GYear();
    }
  }
}
