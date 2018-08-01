package org.exolab.types;

import java.text.ParseException;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.ValidityException;
import org.exolab.xml.NodeType;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.util.XMLFieldDescriptorImpl;

public class DurationDescriptor extends BaseDescriptor {
  private static final String XML_NAME = "duration";
  private static final XMLFieldDescriptorImpl CONTENT_DESCRIPTOR;
  private static final FieldDescriptor[] FIELDS;

  public DurationDescriptor() {
    super("duration", Duration.class);
  }

  public XMLFieldDescriptor getContentDescriptor() {
    return CONTENT_DESCRIPTOR;
  }

  public FieldDescriptor[] getFields() {
    return FIELDS;
  }

  static {
    CONTENT_DESCRIPTOR = new XMLFieldDescriptorImpl(String.class, "content", "content", NodeType.Text);
    CONTENT_DESCRIPTOR.setHandler(new DurationDescriptor().new DurationFieldHandler());
    FIELDS = new FieldDescriptor[1];
    FIELDS[0] = CONTENT_DESCRIPTOR;
  }

  class DurationFieldHandler extends XMLFieldHandler {
    public DurationFieldHandler() {
    }

    public Object getValue(Object target) throws IllegalStateException {
      Object td = null;
      if (target instanceof Duration) {
        td = (Duration)target;
      }

      return td;
    }

    public void setValue(Object target, Object value) throws IllegalStateException {
      if (!(target instanceof Duration)) {
        String err = "DurationDescriptor#setValue: expected Duration, received instead: " + target.getClass();
        throw new IllegalStateException(err);
      } else {
        Duration time = (Duration)target;
        if (value == null) {
          String errx = "DurationDescriptor#setValue: null value";
          throw new IllegalStateException(errx);
        } else {
          try {
            Duration temp = Duration.parseDuration(value.toString());
            time.setYear(temp.getYear());
            time.setMonth(temp.getMonth());
            time.setDay(temp.getDay());
            time.setHour(temp.getHour());
            time.setMinute(temp.getMinute());
            time.setSeconds(temp.getSeconds());
            time.setMilli(temp.getMilli());
          } catch (ParseException var5) {
            throw new IllegalStateException();
          }
        }
      }
    }

    public void resetValue(Object target) throws IllegalStateException {
    }

    public void checkValidity(Object object) throws ValidityException, IllegalStateException {
    }

    public Object newInstance(Object parent) throws IllegalStateException {
      return new Duration();
    }
  }
}
