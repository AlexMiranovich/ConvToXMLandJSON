package org.exolab.types;

import java.text.ParseException;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.ValidityException;
import org.exolab.xml.NodeType;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.util.XMLFieldDescriptorImpl;


public class TimePeriodDescriptor extends BaseDescriptor {
  private static final String XML_NAME = "timePeriod";
  private static final XMLFieldDescriptorImpl CONTENT_DESCRIPTOR;
  private static final FieldDescriptor[] FIELDS;

  public TimePeriodDescriptor() {
    super("timePeriod", TimePeriod.class);
  }

  public XMLFieldDescriptor getContentDescriptor() {
    return CONTENT_DESCRIPTOR;
  }

  public FieldDescriptor[] getFields() {
    return FIELDS;
  }

  static {
    CONTENT_DESCRIPTOR = new XMLFieldDescriptorImpl(String.class, "content", "content", NodeType.Text);
    CONTENT_DESCRIPTOR.setHandler(new TimePeriodDescriptor().new TimePeriodFieldHandler());
    FIELDS = new FieldDescriptor[1];
    FIELDS[0] = CONTENT_DESCRIPTOR;
  }

  class TimePeriodFieldHandler extends XMLFieldHandler {
    public TimePeriodFieldHandler() {
    }

    public Object getValue(Object target) throws IllegalStateException {
      TimePeriod timeP = (TimePeriod)target;
      return timeP.toString();
    }

    public void setValue(Object target, Object value) throws IllegalStateException {
      if (!(target instanceof TimePeriod)) {
        ;
      }

      TimePeriod timePTarget = (TimePeriod)target;
      if (value == null) {
        ;
      }

      try {
        timePTarget.setFields(value.toString());
      } catch (ParseException var5) {
        ;
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
