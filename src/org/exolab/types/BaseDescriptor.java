package org.exolab.types;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.exolab.mapping.AccessMode;
import org.exolab.mapping.ClassDescriptor;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.xml.NodeType;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.UnmarshalState;
import org.exolab.xml.ValidationException;
import org.exolab.xml.XMLClassDescriptor;
import org.exolab.xml.XMLFieldDescriptor;

public class BaseDescriptor implements XMLClassDescriptor {
  protected static final XMLFieldDescriptor[] noXMLFields = new XMLFieldDescriptor[0];
  private static final FieldDescriptor[] noJavaFields = new FieldDescriptor[0];
  private String _xmlName = null;
  private Class _class = null;
  private Map _properties = new HashMap();
  private Set _natures = new HashSet();
  protected BaseDescriptor(String xmlName, Class type) {
    this._xmlName = xmlName;
    this._class = type;
  }
  public XMLFieldDescriptor[] getAttributeDescriptors()
  {
    return noXMLFields;
  }
  public XMLFieldDescriptor getContentDescriptor()
  {
    return null;
  }
  public XMLFieldDescriptor[] getElementDescriptors()
  {
    return noXMLFields;
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
    return null;
  }
  public String getXMLName()
  {
    return this._xmlName;
  }
  public String toString() {
    String className = null;
    Class type = getJavaClass();
    if (type != null) {
      className = type.getName();
    } else {
      className = "unspecified";
    }
    return super.toString() + "; descriptor for class: " + className + "; xml name: " + getXMLName();
  }
  public Class getJavaClass()
  {
    return this._class;
  }
  public FieldDescriptor[] getFields()
  {
    return noJavaFields;
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
  public boolean canAccept(String name, String namespace, Object object)
  {
    return false;
  }
  public void checkDescriptorForCorrectOrderWithinSequence(XMLFieldDescriptor elementDescriptor, UnmarshalState parentState, String xmlName) throws ValidationException
  {}
  public boolean isChoice()
  {
    return false;
  }
  public Object getProperty(String name)
  {
    return this._properties.get(name);
  }
  public void setProperty(String name, Object value)
  {
    this._properties.put(name, value);
  }
  public void addNature(String nature)
  {
    this._natures.add(nature);
  }
  public boolean hasNature(String nature)
  {
    return this._natures.contains(nature);
  }
}
