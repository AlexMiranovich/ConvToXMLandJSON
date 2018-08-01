package org.exolab.xml.descriptors;

import java.sql.Timestamp;
import java.util.Date;
import org.exolab.mapping.AccessMode;
import org.exolab.mapping.ClassDescriptor;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.xml.NodeType;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.handlers.SQLTimestampFieldHandler;
import org.exolab.xml.util.XMLFieldDescriptorImpl;

public class SQLTimestampClassDescriptor extends BaseDescriptor {
  private static final XMLFieldDescriptor[] NO_FIELDS = new XMLFieldDescriptor[0];
  private static final String XML_NAME = "sql-timestamp";
  private static final XMLFieldDescriptorImpl CONTENT_DESCRIPTOR;
  private static final FieldDescriptor[] FIELDS;
  private static final TypeValidator VALIDATOR = null;
  static {
    CONTENT_DESCRIPTOR = new XMLFieldDescriptorImpl(String.class, "content", "content", NodeType.Text);
    CONTENT_DESCRIPTOR.setImmutable(true);
    SQLTimestampFieldHandler handler = new SQLTimestampFieldHandler();
    handler.setFieldHandler(new XMLFieldHandler() {
      public Object getValue(Object object)
      {
        return object;
      }
      public void setValue(Object object, Object value) {
        if (value == null) {
          return;
        }
        if (Timestamp.class == object.getClass()) {
          Class type = value.getClass();
          Timestamp target = (Timestamp)object;
          if (Timestamp.class.isAssignableFrom(type)) {
            Timestamp temp = (Timestamp)value;
            target.setTime(temp.getTime());
            target.setNanos(temp.getNanos());
          }
          else if (Date.class.isAssignableFrom(type)) {
            target.setTime(((Date)value).getTime());
          }
        }
      }
      public Object newInstance(Object parent)
      {
        return new Timestamp(0L);
      }
    });
    CONTENT_DESCRIPTOR.setHandler(handler);
    FIELDS = new FieldDescriptor[1];
    FIELDS[0] = CONTENT_DESCRIPTOR;
  }
  public XMLFieldDescriptor[] getAttributeDescriptors()
  {
    return NO_FIELDS;
  }
  public XMLFieldDescriptor getContentDescriptor()
  {
    return CONTENT_DESCRIPTOR;
  }
  public XMLFieldDescriptor[] getElementDescriptors()
  {
    return NO_FIELDS;
  }
  public XMLFieldDescriptor getFieldDescriptor(String name, String namespace, NodeType nodeType)
  {
    return null;
  }
  public String getNameSpacePrefix()
  {
    return null;
  }
  public String getNameSpaceURI()
  {
    return null;
  }
  public TypeValidator getValidator()
  {
    return VALIDATOR;
  }
  public String getXMLName()
  {
    return "sql-timestamp";
  }
  public String toString() {
    return super.toString() + "; descriptor for class: java.sql.Timestamp; xml name: " + "sql-timestamp";
  }
  public Class getJavaClass()
  {
    return Timestamp.class;
  }
  public FieldDescriptor[] getFields()
  {
    return FIELDS;
  }
  public ClassDescriptor getExtends()
  {
    return null;
  }
  public FieldDescriptor getIdentity()
  {
    return null;
  }
  public AccessMode getAccessMode()
  {
    return null;
  }
}
