package org.exolab.xml.descriptors;

import java.util.Locale;
import org.exolab.mapping.AccessMode;
import org.exolab.mapping.ClassDescriptor;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.xml.NodeType;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.util.XMLFieldDescriptorImpl;

public class LocaleDescriptor extends BaseDescriptor {
  private static final XMLFieldDescriptor[] NO_FIELDS = new XMLFieldDescriptor[0];
  private static final XMLFieldDescriptorImpl NO_CONTENT = null;
  private static final String XML_NAME = "locale";
  private static final XMLFieldDescriptor[] FIELDS;
  private static final TypeValidator VALIDATOR = null;
  static {
    XMLFieldDescriptorImpl fdLanguage = new XMLFieldDescriptorImpl(String.class, "language", "language", NodeType.Attribute);
    fdLanguage.setConstructorArgumentIndex(0);
    fdLanguage.setHandler(new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        return ((Locale)object).getLanguage();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {}
      public Object newInstance(Object parent)
      {
        return null;
      }});
    XMLFieldDescriptorImpl fdCountry = new XMLFieldDescriptorImpl(String.class, "country", "country", NodeType.Attribute);
    fdCountry.setConstructorArgumentIndex(1);
    fdCountry.setHandler(new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        return ((Locale)object).getCountry();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {}
      public Object newInstance(Object parent)
      {
        return null;
      }
    });
    FIELDS = new XMLFieldDescriptor[2];
    FIELDS[0] = fdCountry;
    FIELDS[1] = fdLanguage;
  }
  public XMLFieldDescriptor[] getAttributeDescriptors()
  {
    return FIELDS;
  }
  public XMLFieldDescriptor getContentDescriptor()
  {
    return NO_CONTENT;
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
    return "locale";
  }
  public String toString() {
    return super.toString() + "; descriptor for class: " + Locale.class.getName() + "; xml name: " + "locale";
  }
  public Class getJavaClass()
  {
    return Locale.class;
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
