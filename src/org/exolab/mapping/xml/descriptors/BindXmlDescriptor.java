package org.exolab.mapping.xml.descriptors;

import org.exolab.mapping.AccessMode;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.FieldHandler;
import org.exolab.mapping.xml.BindXml;
import org.exolab.mapping.xml.ClassMapping;
import org.exolab.mapping.xml.Property;
import org.exolab.mapping.xml.types.BindXmlAutoNamingType;
import org.exolab.mapping.xml.types.BindXmlNodeType;
import org.exolab.xml.FieldValidator;
import org.exolab.xml.NodeType;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.handlers.EnumFieldHandler;
import org.exolab.xml.util.XMLClassDescriptorImpl;
import org.exolab.xml.util.XMLFieldDescriptorImpl;
import org.exolab.xml.validators.NameValidator;
import org.exolab.xml.validators.StringValidator;

public class BindXmlDescriptor extends XMLClassDescriptorImpl {
  private boolean _elementDefinition;
  private String _nsPrefix;
  private String _nsURI;
  private String _xmlName;
  private XMLFieldDescriptor _identity;
  public BindXmlDescriptor() {
    this._nsURI = "http://exec.exolab.org/";
    this._xmlName = "bind-xml";
    this._elementDefinition = true;
    setCompositorAsSequence();
    XMLFieldDescriptorImpl desc = null;
    FieldHandler handler = null;
    FieldValidator fieldValidator = null;
    desc = new XMLFieldDescriptorImpl(String.class, "_name", "name", NodeType.Attribute);
    desc.setSchemaType("QName");
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        BindXml target = (BindXml)object;
        return target.getName();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          BindXml target = (BindXml)object;
          target.setName((String)value);
        } catch (Exception ex) {
          throw new IllegalStateException(ex.toString());
        }
      }
      public Object newInstance(Object parent)
      {
        return new String();
      }
    };
    desc.setSchemaType("QName");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    NameValidator typeValidator = new NameValidator((short)3);
    fieldValidator.setValidator(typeValidator);
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(String.class, "_type", "type", NodeType.Attribute);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        BindXml target = (BindXml)object;
        return target.getType();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          BindXml target = (BindXml)object;
          target.setType((String)value);
        }
        catch (Exception ex)
        {
          throw new IllegalStateException(ex.toString());
        }
      }
      public Object newInstance(Object parent)
      {
        return new String();
      }
    };
    desc.setSchemaType("NMTOKEN");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    typeValidator = new NameValidator( (short) 1 );
    
    fieldValidator.setValidator(typeValidator);
    typeValidator.addPattern("[\\w-._:]+");
    
    desc.setValidator(fieldValidator);
    
    desc = new XMLFieldDescriptorImpl(BindXmlAutoNamingType.class, "_autoNaming", "auto-naming", NodeType.Attribute);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        BindXml target = (BindXml)object;
        return target.getAutoNaming();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          BindXml target = (BindXml)object;
          target.setAutoNaming((BindXmlAutoNamingType)value);
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
    handler = new EnumFieldHandler(BindXmlAutoNamingType.class, handler);
    desc.setImmutable(true);
    desc.setSchemaType("BindXmlAutoNamingType");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(String.class, "_location", "location", NodeType.Attribute);
    desc.setImmutable(true);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        BindXml target = (BindXml)object;
        return target.getLocation();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          BindXml target = (BindXml)object;
          target.setLocation((String)value);
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
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    
    fieldValidator = new FieldValidator();
    
     typeValidator = (NameValidator) new StringValidator();
    fieldValidator.setValidator(typeValidator);
    typeValidator.setWhiteSpace("preserve");
    
    desc.setValidator(fieldValidator);
    
    desc = new XMLFieldDescriptorImpl(String.class, "_matches", "matches", NodeType.Attribute);
    desc.setImmutable(true);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        BindXml target = (BindXml)object;
        return target.getMatches();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          BindXml target = (BindXml)object;
          target.setMatches((String)value);
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
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    
    fieldValidator = new FieldValidator();
    
    typeValidator = (NameValidator) new StringValidator();
    fieldValidator.setValidator(typeValidator);
    typeValidator.setWhiteSpace("preserve");
    
    desc.setValidator(fieldValidator);
    
    desc = new XMLFieldDescriptorImpl(Boolean.TYPE, "_reference", "reference", NodeType.Attribute);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        BindXml target = (BindXml)object;
        if (!target.hasReference()) {
          return null;
        }
        return target.getReference() ? Boolean.TRUE : Boolean.FALSE;
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          BindXml target = (BindXml)object;
          if (value == null)
          {
            target.deleteReference();
            return;
          }
          target.setReference(((Boolean)value).booleanValue());
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
    desc.setSchemaType("boolean");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    
    fieldValidator = new FieldValidator();
    
    typeValidator = new NameValidator();
    fieldValidator.setValidator(typeValidator);
    
    desc.setValidator(fieldValidator);
    
    desc = new XMLFieldDescriptorImpl(BindXmlNodeType.class, "_node", "node", NodeType.Attribute);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        BindXml target = (BindXml)object;
        return target.getNode();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          BindXml target = (BindXml)object;
          target.setNode((BindXmlNodeType)value);
        } catch (Exception ex) {
          throw new IllegalStateException(ex.toString());
        }
      }
      public Object newInstance(Object parent)
      {
        return null;
      }
    };
    handler = new EnumFieldHandler(BindXmlNodeType.class, handler);
    desc.setImmutable(true);
    desc.setSchemaType("BindXmlNodeType");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(String.class, "_QNamePrefix", "QName-prefix", NodeType.Attribute);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        BindXml target = (BindXml)object;
        return target.getQNamePrefix();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          BindXml target = (BindXml)object;
          target.setQNamePrefix((String)value);
        } catch (Exception ex) {
          throw new IllegalStateException(ex.toString());
        }
      }
      public Object newInstance(Object parent)
      {
        return new String();
      }
    };
    desc.setSchemaType("NMTOKEN");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    typeValidator = new NameValidator((short)1);
    fieldValidator.setValidator(typeValidator);
    typeValidator.addPattern("[\\w-._:]+");
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(Boolean.TYPE, "_transient", "transient", NodeType.Attribute);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        BindXml target = (BindXml)object;
        if (!target.hasTransient()) {
          return null;
        }
        return target.getTransient() ? Boolean.TRUE : Boolean.FALSE;
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          BindXml target = (BindXml)object;
          if (value == null) {
            target.deleteTransient();
            return;
          }
          target.setTransient(((Boolean)value).booleanValue());
        } catch (Exception ex) {
          throw new IllegalStateException(ex.toString());
        }
      }
      public Object newInstance(Object parent)
      {
        return null;
      }
    };
    desc.setSchemaType("boolean");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    typeValidator = new NameValidator();
    fieldValidator.setValidator(typeValidator);
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(ClassMapping.class, "_classMapping", "class", NodeType.Element);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        BindXml target = (BindXml)object;
        return target.getClassMapping();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          BindXml target = (BindXml)object;
          target.setClassMapping((ClassMapping)value);
        } catch (Exception ex) {
          throw new IllegalStateException(ex.toString());
        }
      }
      public Object newInstance(Object parent)
      {
        return null;
      }
    };
    desc.setSchemaType("org.exolab.mapping.xml.ClassMapping");
    desc.setHandler(handler);
    desc.setNameSpaceURI("http://exec.exolab.org/");
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    addSequenceElement(desc);
    fieldValidator = new FieldValidator();
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(Property.class, "_propertyList", "property", NodeType.Element);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        BindXml target = (BindXml)object;
        return target.getProperty();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          BindXml target = (BindXml)object;
          target.addProperty((Property)value);
        }
        catch (Exception ex) {
          throw new IllegalStateException(ex.toString());
        }
      }
      public void resetValue(Object object)
        throws IllegalStateException, IllegalArgumentException {
        try {
          BindXml target = (BindXml)object;
          target.removeAllProperty();
        } catch (Exception ex) {
          throw new IllegalStateException(ex.toString());
        }
      }
      public Object newInstance(Object parent)
      {
        return null;
      }
    };
    desc.setSchemaType("list");
    desc.setComponentType("org.exolab.mapping.xml.Property");
    desc.setHandler(handler);
    desc.setMultivalued(true);
    addFieldDescriptor(desc);
    addSequenceElement(desc);
    fieldValidator = new FieldValidator();
    fieldValidator.setMinOccurs(0);
    desc.setValidator(fieldValidator);
  }
  public AccessMode getAccessMode()
  {
    return null;
  }
  public FieldDescriptor getIdentity() {
    return this._identity;
  }
  public Class getJavaClass() {
    return BindXml.class;
  }
  public String getNameSpacePrefix() {
    return this._nsPrefix;
  }
  public String getNameSpaceURI() {
    return this._nsURI;
  }
  public TypeValidator getValidator() {
    return this;
  }
  public String getXMLName() {
    return this._xmlName;
  }
  public boolean isElementDefinition() {
    return this._elementDefinition;
  }
}
