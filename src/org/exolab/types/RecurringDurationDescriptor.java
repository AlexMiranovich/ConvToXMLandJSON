package org.exolab.types;

import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.ValidityException;
import org.exolab.xml.NodeType;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.util.XMLFieldDescriptorImpl;

public class RecurringDurationDescriptor extends BaseDescriptor {
  private static final String XML_NAME = "recurringDuration";
  private static final XMLFieldDescriptorImpl CONTENT_DESCRIPTOR;
  private static final FieldDescriptor[] FIELDS;

  public RecurringDurationDescriptor() {
    super("recurringDuration", RecurringDuration.class);
  }

  public XMLFieldDescriptor getContentDescriptor() {
    return CONTENT_DESCRIPTOR;
  }

  public FieldDescriptor[] getFields() {
    return FIELDS;
  }

  static {
    CONTENT_DESCRIPTOR = new XMLFieldDescriptorImpl(String.class, "content", "content", NodeType.Text);
    CONTENT_DESCRIPTOR.setHandler(new RecurringDurationDescriptor().new RecurringDurationFieldHandler());
    FIELDS = new FieldDescriptor[1];
    FIELDS[0] = CONTENT_DESCRIPTOR;
  }

  class RecurringDurationFieldHandler extends XMLFieldHandler {
    public RecurringDurationFieldHandler() {
    }

    public Object getValue(Object target) throws IllegalStateException {
      RecurringDuration recurr = (RecurringDuration)target;
      return recurr.toString();
    }

    public void setValue(Object target, Object value) throws IllegalStateException {
      if (!(target instanceof RecurringDuration)) {
        ;
      }

      RecurringDuration recurTarget = (RecurringDuration)target;
      if (value == null) {
        ;
      }

      try {
        RecurringDuration temp = RecurringDuration.parseRecurringDuration(value.toString());
        recurTarget.setCentury(temp.getCentury());
        recurTarget.setYear(temp.getYear());
        recurTarget.setMonth(temp.getMonth());
        recurTarget.setDay(temp.getDay());
        recurTarget.setHour(temp.getHour());
        recurTarget.setMinute(temp.getMinute());
        recurTarget.setSecond(temp.getSeconds(), temp.getMilli());
        recurTarget.setZone(temp.getZoneHour(), temp.getZoneMinute());
        recurTarget.isUTC();
      } catch (Exception var5) {
        ;
      }

    }

    public void resetValue(Object target) throws IllegalStateException {
    }

    public void checkValidity(Object object) throws ValidityException, IllegalStateException {
    }

    public Object newInstance(Object parent) throws IllegalStateException {
      return new RecurringDuration();
    }
  }
}
