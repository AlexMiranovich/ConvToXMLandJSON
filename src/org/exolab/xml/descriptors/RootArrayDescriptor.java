package org.exolab.xml.descriptors;

import org.exolab.mapping.AccessMode;
import org.exolab.mapping.ClassDescriptor;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.FieldHandler;
import org.exolab.xml.NodeType;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.util.XMLFieldDescriptorImpl;

public class RootArrayDescriptor extends BaseDescriptor {
  private static final XMLFieldDescriptor NO_CONTENT = null;
  private static final XMLFieldDescriptor[] NO_ATTRIBUTES = new XMLFieldDescriptor[0];
  private final FieldDescriptor[] _fields = new FieldDescriptor[1];
  private final XMLFieldDescriptor[] _elements = new XMLFieldDescriptor[1];
  private final Class _javaClass;
  private String _xmlName = "array";
  private String _nsURI = null;
  private TypeValidator _validator = null;
  public RootArrayDescriptor(Class array) {
    if (array == null) {
      String err = "Argument array must not be null.";
      throw new IllegalArgumentException(err);
    }
    if (!array.isArray()) {
      String err = "Argument array must be an array.";
      throw new IllegalArgumentException(err);
    }
    this._javaClass = array.getComponentType();
    XMLFieldDescriptorImpl desc = new XMLFieldDescriptorImpl(this._javaClass, "_elements", null, NodeType.Element);
    FieldHandler handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        return object;
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {}
      public Object newInstance(Object parent)
      {
        return null;
      }
    };
    desc.setHandler(handler);
    desc.setMultivalued(true);
    this._elements[0] = desc;
    this._fields[0] = desc;
  }
  public XMLFieldDescriptor[] getAttributeDescriptors()
  {
    return NO_ATTRIBUTES;
  }
  public Class getJavaClass()
  {
    return this._javaClass;
  }
  public XMLFieldDescriptor[] getElementDescriptors()
  {
    return this._elements;
  }
  public ClassDescriptor getExtends()
  {
    return null;
  }
  public FieldDescriptor[] getFields()
  {
    return this._fields;
  }
  public XMLFieldDescriptor getContentDescriptor()
  {
    return NO_CONTENT;
  }
  public XMLFieldDescriptor getFieldDescriptor(String name, String namespace, NodeType nodeType) {
    return this._elements[0];
  }
  public String getNameSpacePrefix()
  {
    return null;
  }
  public String getNameSpaceURI()
  {
    return this._nsURI;
  }
  public FieldDescriptor getIdentity()
  {
    return null;
  }
  public AccessMode getAccessMode()
  {
    return null;
  }
  public TypeValidator getValidator()
  {
    return this._validator;
  }
  public String getXMLName()
  {
    return this._xmlName;
  }
  public void setXMLName(String xmlName)
  {
    this._xmlName = xmlName;
  }
  public void setNameSpaceURI(String nsURI)
  {
    this._nsURI = nsURI;
  }
}
