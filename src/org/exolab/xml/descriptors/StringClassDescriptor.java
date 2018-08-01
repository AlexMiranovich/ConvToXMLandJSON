package org.exolab.xml.descriptors;

import org.exolab.mapping.AccessMode;
import org.exolab.mapping.ClassDescriptor;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.xml.NodeType;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.validators.StringValidator;

public class StringClassDescriptor extends BaseDescriptor {
  private static final XMLFieldDescriptor[] NO_ELEMENTS = new XMLFieldDescriptor[0];
  private static final XMLFieldDescriptor[] NO_ATTRIBUTES = new XMLFieldDescriptor[0];
  private static final XMLFieldDescriptor NO_CONTENT = null;
  private static final FieldDescriptor[] NO_FIELDS = new FieldDescriptor[0];
  private String _xmlName = null;
  private String _nsURI = null;
  private StringValidator _validator = null;
  public XMLFieldDescriptor[] getAttributeDescriptors()
  {
    return NO_ATTRIBUTES;
  }
  public Class getJavaClass()
  {
    return String.class;
  }
  public XMLFieldDescriptor[] getElementDescriptors()
  {
    return NO_ELEMENTS;
  }
  public ClassDescriptor getExtends()
  {
    return null;
  }
  public FieldDescriptor[] getFields()
  {
    return NO_FIELDS;
  }
  public XMLFieldDescriptor getContentDescriptor()
  {
    return NO_CONTENT;
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
  public void setValidator(StringValidator validator)
  {
    this._validator = validator;
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
