package org.exolab.xml.descriptors;

import java.sql.Time;
import java.util.Date;
import org.exolab.mapping.AccessMode;
import org.exolab.mapping.ClassDescriptor;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.xml.NodeType;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.handlers.SQLTimeFieldHandler;
import org.exolab.xml.util.XMLFieldDescriptorImpl;

public class SQLTimeClassDescriptor extends BaseDescriptor {
  private static final XMLFieldDescriptor[] NO_FIELDS = new XMLFieldDescriptor[0];
  private static final String XML_NAME = "sql-time";
  private static final XMLFieldDescriptorImpl CONTENT_DESCRIPTOR;
  private static final FieldDescriptor[] FIELDS;
  private static final TypeValidator VALIDATOR = null;
  static {
    CONTENT_DESCRIPTOR = new XMLFieldDescriptorImpl(String.class, "content", "content", NodeType.Text);
    CONTENT_DESCRIPTOR.setImmutable(true);
    SQLTimeFieldHandler sqlTimeHandler = new SQLTimeFieldHandler();
    XMLFieldHandler handler = new XMLFieldHandler() {
      public Object getValue(Object object)
      {
        return object;
      }
      public void setValue(Object object, Object value) {
        if (value == null) {
          return;
        }
        if (Time.class == object.getClass()) {
          Time target = (Time)object;
          if (Date.class.isAssignableFrom(value.getClass())) {
            target.setTime(((Date)value).getTime());
          }
        }
      }
      public Object newInstance(Object parent)
      {
        return new Time(0L);
      }
    };
    sqlTimeHandler.setFieldHandler(handler);
    CONTENT_DESCRIPTOR.setHandler(sqlTimeHandler);
    FIELDS = new FieldDescriptor[1];
    FIELDS[0] = CONTENT_DESCRIPTOR;
  }
  public XMLFieldDescriptor[] getAttributeDescriptors() {
    return NO_FIELDS;
  }
  public XMLFieldDescriptor getContentDescriptor() {
    return CONTENT_DESCRIPTOR;
  }
  public XMLFieldDescriptor[] getElementDescriptors() {
    return NO_FIELDS;
  }
  public XMLFieldDescriptor getFieldDescriptor(String name, String namespace, NodeType nodeType) {
    return null;
  }
  public String getNameSpacePrefix() {
    return null;
  }
  public String getNameSpaceURI() {
    return null;
  }
  public TypeValidator getValidator() {
    return VALIDATOR;
  }
  public String getXMLName() {
    return "sql-time";
  }
  public String toString() {
    return super.toString() + "; descriptor for class: java.sql.Time; xml name: " + "sql-time";
  }
  public Class getJavaClass() {
    return Time.class;
  }
  public FieldDescriptor[] getFields() {
    return FIELDS;
  }
  public ClassDescriptor getExtends() {
    return null;
  }
  public FieldDescriptor getIdentity() {
    return null;
  }
  public AccessMode getAccessMode() {
    return null;
  }
}
