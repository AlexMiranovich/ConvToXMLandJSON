package org.exolab.types;

import java.text.ParseException;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.ValidityException;
import org.exolab.xml.NodeType;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.util.XMLFieldDescriptorImpl;

public class TimeDurationDescriptor extends BaseDescriptor {
  private static final String XML_NAME = "timeDuration";
  private static final XMLFieldDescriptorImpl CONTENT_DESCRIPTOR;
  private static final FieldDescriptor[] FIELDS;

  public TimeDurationDescriptor() {
    super("timeDuration", TimeDuration.class);
  }

  public XMLFieldDescriptor getContentDescriptor() {
    return CONTENT_DESCRIPTOR;
  }

  public FieldDescriptor[] getFields() {
    return FIELDS;
  }

  static {
    CONTENT_DESCRIPTOR = new XMLFieldDescriptorImpl(String.class, "content", "content", NodeType.Text);
    CONTENT_DESCRIPTOR.setHandler(new TimeDurationDescriptor().new TimeDurationFieldHandler());
    FIELDS = new FieldDescriptor[1];
    FIELDS[0] = CONTENT_DESCRIPTOR;
  }

  class TimeDurationFieldHandler extends XMLFieldHandler {
    public TimeDurationFieldHandler() {
    }

    public Object getValue(Object target) throws IllegalStateException {
      TimeDuration td = (TimeDuration)target;
      return td.toString();
    }

    public void setValue(Object target, Object value) throws IllegalStateException {
      if (!(target instanceof TimeDuration)) {
        ;
      }

      TimeDuration time = (TimeDuration)target;
      if (value == null) {
        ;
      }

      try {
        TimeDuration temp = TimeDuration.parseTimeDuration(value.toString());
        time.setYear(temp.getYear());
        time.setMonth(temp.getMonth());
        time.setDay(temp.getDay());
        time.setHour(temp.getHour());
        time.setMinute(temp.getMinute());
        time.setSeconds(temp.getSeconds());
      } catch (ParseException var5) {
        ;
      }

    }

    public void resetValue(Object target) throws IllegalStateException {
    }

    public void checkValidity(Object object) throws ValidityException, IllegalStateException {
    }

    public Object newInstance(Object parent) throws IllegalStateException {
      return new TimeDuration();
    }
  }
}
