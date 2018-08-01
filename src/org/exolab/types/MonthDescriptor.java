package org.exolab.types;

import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.ValidityException;
import org.exolab.xml.NodeType;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.util.XMLFieldDescriptorImpl;


public class MonthDescriptor extends BaseDescriptor {
  private static final String XML_NAME = "month";
  private static final XMLFieldDescriptorImpl CONTENT_DESCRIPTOR;
  private static final FieldDescriptor[] FIELDS;

  public MonthDescriptor() {
    super("month", Month.class);
  }

  public XMLFieldDescriptor getContentDescriptor() {
    return CONTENT_DESCRIPTOR;
  }

  public FieldDescriptor[] getFields() {
    return FIELDS;
  }

  static {
    CONTENT_DESCRIPTOR = new XMLFieldDescriptorImpl(String.class, "content", "content", NodeType.Text);
    CONTENT_DESCRIPTOR.setHandler(new MonthDescriptor().new MonthFieldHandler());
    FIELDS = new FieldDescriptor[1];
    FIELDS[0] = CONTENT_DESCRIPTOR;
  }

  class MonthFieldHandler extends XMLFieldHandler {
    public MonthFieldHandler() {
    }

    public Object getValue(Object target) throws IllegalStateException {
      Month month = (Month)target;
      return month.toString();
    }

    public void setValue(Object target, Object value) throws IllegalStateException {
      if (!(target instanceof Month)) {
        ;
      }

      Month monthTarget = (Month)target;
      if (value == null) {
        ;
      }

      try {
        Month temp = Month.parseMonth(value.toString());
        monthTarget.setCentury(temp.getCentury());
        monthTarget.setYear(temp.getYear());
        monthTarget.setMonth(temp.getMonth());
      } catch (Exception var5) {
        ;
      }

    }

    public void resetValue(Object target) throws IllegalStateException {
    }

    public void checkValidity(Object object) throws ValidityException, IllegalStateException {
    }

    public Object newInstance(Object parent) throws IllegalStateException {
      return new Month();
    }
  }
}
