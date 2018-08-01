package org.exolab.types;

import java.text.ParseException;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.ValidityException;
import org.exolab.xml.NodeType;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.util.XMLFieldDescriptorImpl;

public class GDayDescriptor extends BaseDescriptor {
  private static final String XML_NAME = "gDay";
  private static final XMLFieldDescriptorImpl CONTENT_DESCRIPTOR;
  private static final FieldDescriptor[] FIELDS;

  public GDayDescriptor() {
    super("gDay", GDay.class);
  }

  public XMLFieldDescriptor getContentDescriptor() {
    return CONTENT_DESCRIPTOR;
  }

  public FieldDescriptor[] getFields() {
    return FIELDS;
  }

  static {
    CONTENT_DESCRIPTOR = new XMLFieldDescriptorImpl(String.class, "content", "content", NodeType.Text);
    CONTENT_DESCRIPTOR.setHandler(new GDayDescriptor().new GDayFieldHandler());
    FIELDS = new FieldDescriptor[1];
    FIELDS[0] = CONTENT_DESCRIPTOR;
  }

  class GDayFieldHandler extends XMLFieldHandler {
    public GDayFieldHandler() {
    }

    public Object getValue(Object target) throws IllegalStateException {
      Object result = null;
      if (target instanceof GDay) {
        result = (GDay)target;
      }

      return result;
    }

    public void setValue(Object target, Object value) throws IllegalStateException {
      if (!(target instanceof GDay)) {
        String err = "GDayDescriptor#setValue: expected GDay, received instead: " + target.getClass();
        throw new IllegalStateException(err);
      } else {
        GDay gDayTarget = (GDay)target;
        if (value == null) {
          String errx = "GDayDescriptor#setValue: null value";
          throw new IllegalStateException(errx);
        } else {
          try {
            GDay temp = GDay.parseGDay(value.toString());
            gDayTarget.setDay(temp.getDay());
            if (temp.isUTC()) {
              gDayTarget.setUTC();
              gDayTarget.setZone(temp.getZoneHour(), temp.getZoneMinute());
              gDayTarget.setZoneNegative(temp.isZoneNegative());
            }

            temp = null;
          } catch (ParseException var6) {
            String errxx = "GDayDescriptor#setValue: wrong value\n" + var6.getMessage();
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
      return new GDay();
    }
  }
}
