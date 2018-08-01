package org.exolab.types;

import java.text.ParseException;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.ValidityException;
import org.exolab.xml.NodeType;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.util.XMLFieldDescriptorImpl;


public class CenturyDescriptor extends BaseDescriptor {
  private static final String XML_NAME = "century";
  private static final XMLFieldDescriptorImpl CONTENT_DESCRIPTOR;
  private static final FieldDescriptor[] FIELDS;

  public CenturyDescriptor() {
    super("century", Century.class);
  }

  public XMLFieldDescriptor getContentDescriptor() {
    return CONTENT_DESCRIPTOR;
  }

  public FieldDescriptor[] getFields() {
    return FIELDS;
  }

  static {
    CONTENT_DESCRIPTOR = new XMLFieldDescriptorImpl(String.class, "content", "content", NodeType.Text);
    CONTENT_DESCRIPTOR.setHandler(new CenturyDescriptor().new CenturyFieldHandler());
    FIELDS = new FieldDescriptor[1];
    FIELDS[0] = CONTENT_DESCRIPTOR;
  }

  class CenturyFieldHandler extends XMLFieldHandler {
    public CenturyFieldHandler() {
    }

    public Object getValue(Object target) throws IllegalStateException {
      Century century = (Century)target;
      return century.toString();
    }

    public void setValue(Object target, Object value) throws IllegalStateException {
      if (!(target instanceof Century)) {
        ;
      }

      Century centuryTarget = (Century)target;
      if (value == null) {
        ;
      }

      try {
        Century temp = Century.parseCentury(value.toString());
        centuryTarget.setCentury(temp.getCentury());
      } catch (ParseException var5) {
        ;
      }

    }

    public void resetValue(Object target) throws IllegalStateException {
    }

    public void checkValidity(Object object) throws ValidityException, IllegalStateException {
    }

    public Object newInstance(Object parent) throws IllegalStateException {
      return new Century();
    }
  }
}
