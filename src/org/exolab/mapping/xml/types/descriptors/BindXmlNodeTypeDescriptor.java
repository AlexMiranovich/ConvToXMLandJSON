package org.exolab.mapping.xml.types.descriptors;

import org.exolab.mapping.AccessMode;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.xml.types.BindXmlNodeType;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.util.XMLClassDescriptorImpl;

public class BindXmlNodeTypeDescriptor extends XMLClassDescriptorImpl {
  private boolean _elementDefinition;
  private String _nsPrefix;
  private String _nsURI;
  private String _xmlName;
  private XMLFieldDescriptor _identity;
  public BindXmlNodeTypeDescriptor() {
    this._nsURI = "http://exec.exolab.org/";
    this._xmlName = "BindXmlNodeType";
    this._elementDefinition = false;
  }
  public AccessMode getAccessMode()
  {
    return null;
  }
  public FieldDescriptor getIdentity()
  {
    return this._identity;
  }
  public Class getJavaClass()
  {
    return BindXmlNodeType.class;
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
