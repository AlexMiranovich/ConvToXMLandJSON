package org.exolab.types;

import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.ValidityException;
import org.exolab.xml.NodeType;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.util.XMLFieldDescriptorImpl;


public class YearDescriptor extends BaseDescriptor {
  private static final String XML_NAME = "year";
  private static final XMLFieldDescriptorImpl CONTENT_DESCRIPTOR;
  private static final FieldDescriptor[] FIELDS;

  public YearDescriptor() {
    super("year", Year.class);
  }

  public XMLFieldDescriptor getContentDescriptor() {
    return CONTENT_DESCRIPTOR;
  }

  public FieldDescriptor[] getFields() {
    return FIELDS;
  }

  static {
    CONTENT_DESCRIPTOR = new XMLFieldDescriptorImpl(String.class, "content", "content", NodeType.Text);
    CONTENT_DESCRIPTOR.setHandler(new YearDescriptor().new YearFieldHandler());
    FIELDS = new FieldDescriptor[1];
    FIELDS[0] = CONTENT_DESCRIPTOR;
  }

  class YearFieldHandler extends XMLFieldHandler {
    public YearFieldHandler() {
    }

    public Object getValue(Object target) throws IllegalStateException {
      Year year = (Year)target;
      return year.toString();
    }

    public void setValue(Object target, Object value) throws IllegalStateException {
      if (!(target instanceof Year)) {
        ;
      }

      Year yearTarget = (Year)target;
      if (value == null) {
        ;
      }

      try {
        Year temp = Year.parseYear(value.toString());
        yearTarget.setCentury(temp.getCentury());
        yearTarget.setYear(temp.getYear());
      } catch (Exception var5) {
        ;
      }

    }

    public void resetValue(Object target) throws IllegalStateException {
    }

    public void checkValidity(Object object) throws ValidityException, IllegalStateException {
    }

    public Object newInstance(Object parent) throws IllegalStateException {
      return new Year();
    }
  }
}
