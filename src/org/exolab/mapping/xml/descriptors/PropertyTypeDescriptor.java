package org.exolab.mapping.xml.descriptors;

import org.exolab.mapping.AccessMode;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.FieldHandler;
import org.exolab.mapping.xml.PropertyType;
import org.exolab.xml.FieldValidator;
import org.exolab.xml.NodeType;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.util.XMLClassDescriptorImpl;
import org.exolab.xml.util.XMLFieldDescriptorImpl;
import org.exolab.xml.validators.StringValidator;

public class PropertyTypeDescriptor extends XMLClassDescriptorImpl {
  private boolean _elementDefinition;
  private String _nsPrefix;
  private String _nsURI;
  private String _xmlName;
  private XMLFieldDescriptor _identity;
  public PropertyTypeDescriptor() {
    this._nsURI = "http://exec.exolab.org/";
    this._xmlName = "propertyType";
    this._elementDefinition = false;
    setCompositorAsSequence();
    XMLFieldDescriptorImpl desc = null;
    FieldHandler handler = null;
    FieldValidator fieldValidator = null;
    desc = new XMLFieldDescriptorImpl(String.class, "_name", "name", NodeType.Element);
    desc.setImmutable(true);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        PropertyType target = (PropertyType)object;
        return target.getName();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try
        {
          PropertyType target = (PropertyType)object;
          target.setName((String)value);
        }
        catch (Exception ex)
        {
          throw new IllegalStateException(ex.toString());
        }
      }
      
      public Object newInstance(Object parent)
      {
        return null;
      }
    };
    desc.setSchemaType("string");
    desc.setHandler(handler);
    desc.setRequired(true);
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    addSequenceElement(desc);
    fieldValidator = new FieldValidator();
    fieldValidator.setMinOccurs(1);
    StringValidator typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    typeValidator.setWhiteSpace("preserve");
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(String.class, "_value", "value", NodeType.Element);
    desc.setImmutable(true);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        PropertyType target = (PropertyType)object;
        return target.getValue();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          PropertyType target = (PropertyType)object;
          target.setValue((String)value);
        } catch (Exception ex) {
          throw new IllegalStateException(ex.toString());
        }
      }
      public Object newInstance(Object parent)
      {
        return null;
      }
    };
    desc.setSchemaType("string");
    desc.setHandler(handler);
    desc.setRequired(true);
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    addSequenceElement(desc);
    fieldValidator = new FieldValidator();
    fieldValidator.setMinOccurs(1);
    typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    typeValidator.setWhiteSpace("preserve");
    desc.setValidator(fieldValidator);
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
    return PropertyType.class;
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
