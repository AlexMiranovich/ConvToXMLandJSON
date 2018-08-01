package org.exolab.mapping.xml.descriptors;

import org.exolab.mapping.AccessMode;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.xml.Property;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.XMLFieldDescriptor;

public class PropertyDescriptor extends PropertyTypeDescriptor {
  private boolean _elementDefinition;
  private String _nsPrefix;
  private String _nsURI;
  private String _xmlName;
  private XMLFieldDescriptor _identity;
  public PropertyDescriptor() {
    setExtendsWithoutFlatten(new PropertyTypeDescriptor());
    this._xmlName = "property";
    this._elementDefinition = true;
  }
  public AccessMode getAccessMode()
  {
    return null;
  }
  public FieldDescriptor getIdentity() {
    if (this._identity == null) {
      return super.getIdentity();
    }
    return this._identity;
  }
  public Class getJavaClass()
  {
    return Property.class;
  }
  public String getNameSpacePrefix()
  {
    return this._nsPrefix;
  }
  public String getNameSpaceURI()
  {
    return this._nsURI;
  }
  public TypeValidator getValidator()
  {
    return this;
  }
  public String getXMLName()
  {
    return this._xmlName;
  }
  public boolean isElementDefinition()
  {
    return this._elementDefinition;
  }
}
