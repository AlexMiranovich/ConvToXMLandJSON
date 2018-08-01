package org.exolab.xml.descriptors;

import java.util.Date;
import org.exolab.mapping.AccessMode;
import org.exolab.mapping.ClassDescriptor;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.xml.NodeType;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.handlers.DateFieldHandler;
import org.exolab.xml.util.XMLFieldDescriptorImpl;

public class DateClassDescriptor extends BaseDescriptor {
  private static final XMLFieldDescriptor[] NO_FIELDS = new XMLFieldDescriptor[0];
  private static final String XML_NAME = "date";
  private static final XMLFieldDescriptorImpl CONTENT_DESCRIPTOR;
  private static final FieldDescriptor[] FIELDS;
  private static final TypeValidator VALIDATOR = null;
  static {
    CONTENT_DESCRIPTOR = new XMLFieldDescriptorImpl(String.class, "content", "content", NodeType.Text);
    CONTENT_DESCRIPTOR.setImmutable(true);
    CONTENT_DESCRIPTOR.setHandler(new DateFieldHandler(new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        return object;
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        if (object.getClass() == Date.class) {
          Date target = (Date)object;
          if (value.getClass() == Date.class) {
            target.setTime(((Date)value).getTime());
          }
        }
      }
      public Object newInstance(Object parent)
      {
        return null;
      }}));
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
    return "date";
  }
  public String toString()
  {
    return super.toString() + "; descriptor for class: Date" + "; xml name: " + "date";
  }
  public Class getJavaClass()
  {
    return Date.class;
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
