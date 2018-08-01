package org.exolab.xml.descriptors;

import org.exolab.mapping.AccessMode;
import org.exolab.mapping.ClassDescriptor;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.xml.NodeType;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.handlers.DateFieldHandler;
import org.exolab.xml.util.XMLFieldDescriptorImpl;

public class SQLDateClassDescriptor extends BaseDescriptor {
  private static final XMLFieldDescriptor[] NO_FIELDS = new XMLFieldDescriptor[0];
  private static final String XML_NAME = "sql-date";
  private static final XMLFieldDescriptorImpl CONTENT_DESCRIPTOR;
  private static final FieldDescriptor[] FIELDS;
  private static final TypeValidator VALIDATOR = null;
  static {
    CONTENT_DESCRIPTOR = new XMLFieldDescriptorImpl(String.class, "content", "content", NodeType.Text);
    CONTENT_DESCRIPTOR.setImmutable(true);
    DateFieldHandler dfh = new DateFieldHandler(new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        return object;
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        if (java.sql.Date.class == object.getClass()) {
          java.sql.Date target = (java.sql.Date)object;
          if (java.util.Date.class.isAssignableFrom(value.getClass())) {
            target.setTime(((java.sql.Date)value).getTime());
          }
        }
      }
      public Object newInstance(Object parent)
      {
        return null;
      }
    });
    dfh.setUseSQLDate(true);
    CONTENT_DESCRIPTOR.setHandler(dfh);
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
    return "sql-date";
  }
  public String toString() {
    return super.toString() + "; descriptor for class: java.sql.Date; xml name: " + "sql-date";
  }
  public Class getJavaClass()
  {
    return java.sql.Date.class;
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
