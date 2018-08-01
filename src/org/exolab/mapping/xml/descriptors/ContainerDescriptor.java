package org.exolab.mapping.xml.descriptors;

import org.exolab.mapping.AccessMode;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.FieldHandler;
import org.exolab.mapping.xml.Container;
import org.exolab.mapping.xml.FieldMapping;
import org.exolab.xml.FieldValidator;
import org.exolab.xml.NodeType;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.util.XMLClassDescriptorImpl;
import org.exolab.xml.util.XMLFieldDescriptorImpl;
import org.exolab.xml.validators.BooleanValidator;
import org.exolab.xml.validators.NameValidator;
import org.exolab.xml.validators.StringValidator;

public class ContainerDescriptor extends XMLClassDescriptorImpl {
  private boolean _elementDefinition;
  private String _nsPrefix;
  private String _nsURI;
  private String _xmlName;
  private XMLFieldDescriptor _identity;
  
  public ContainerDescriptor()
  {
    this._nsURI = "http://exec.exolab.org/";
    this._xmlName = "container";
    this._elementDefinition = true;
    
    setCompositorAsSequence();
    XMLFieldDescriptorImpl desc = null;
    FieldHandler handler = null;
    FieldValidator fieldValidator = null;
    
    desc = new XMLFieldDescriptorImpl(String.class, "_name", "name", NodeType.Attribute);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        Container target = (Container)object;
        return target.getName();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          Container target = (Container)object;
          target.setName((String)value);
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
    desc.setRequired(true);
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    
    fieldValidator = new FieldValidator();
    fieldValidator.setMinOccurs(1);
    
    NameValidator typeValidator = new NameValidator((short)1);
    
    fieldValidator.setValidator(typeValidator);
    typeValidator.addPattern("[\\w-._:]+");
    
    desc.setValidator(fieldValidator);
    
    desc = new XMLFieldDescriptorImpl(String.class, "_type", "type", NodeType.Attribute);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        Container target = (Container)object;
        return target.getType();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          Container target = (Container)object;
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
    
    desc = new XMLFieldDescriptorImpl(Boolean.TYPE, "_required", "required", NodeType.Attribute);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        Container target = (Container)object;
        if (!target.hasRequired()) {
          return null;
        }
        return target.getRequired() ? Boolean.TRUE : Boolean.FALSE;
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          Container target = (Container)object;
          if (value == null)
          {
            target.deleteRequired();
            return;
          }
          target.setRequired(((Boolean)value).booleanValue());
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
    
    desc = new XMLFieldDescriptorImpl(Boolean.TYPE, "_direct", "direct", NodeType.Attribute);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        Container target = (Container)object;
        if (!target.hasDirect()) {
          return null;
        }
        return target.getDirect() ? Boolean.TRUE : Boolean.FALSE;
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          Container target = (Container)object;
          if (value == null)
          {
            target.deleteDirect();
            return;
          }
          target.setDirect(((Boolean)value).booleanValue());
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
    
    desc = new XMLFieldDescriptorImpl(String.class, "_getMethod", "get-method", NodeType.Attribute);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        Container target = (Container)object;
        return target.getGetMethod();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          Container target = (Container)object;
          target.setGetMethod((String)value);
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
    
    typeValidator = new NameValidator((short)1);
    
    fieldValidator.setValidator(typeValidator);
    typeValidator.addPattern("[\\w-._:]+");
    
    desc.setValidator(fieldValidator);
    
    desc = new XMLFieldDescriptorImpl(String.class, "_setMethod", "set-method", NodeType.Attribute);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        Container target = (Container)object;
        return target.getSetMethod();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          Container target = (Container)object;
          target.setSetMethod((String)value);
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
    
    typeValidator = new NameValidator((short)1);
    
    fieldValidator.setValidator(typeValidator);
    typeValidator.addPattern("[\\w-._:]+");
    
    desc.setValidator(fieldValidator);
    
    desc = new XMLFieldDescriptorImpl(String.class, "_createMethod", "create-method", NodeType.Attribute);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        Container target = (Container)object;
        return target.getCreateMethod();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          Container target = (Container)object;
          target.setCreateMethod((String)value);
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
    typeValidator = new NameValidator((short)1);
    fieldValidator.setValidator(typeValidator);
    typeValidator.addPattern("[\\w-._:]+");
    desc.setValidator(fieldValidator);
    
    desc = new XMLFieldDescriptorImpl(String.class, "_description", "description", NodeType.Element);
    desc.setImmutable(true);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        Container target = (Container)object;
        return target.getDescription();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          Container target = (Container)object;
          target.setDescription((String)value);
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
    addSequenceElement(desc);
    
    fieldValidator = new FieldValidator();
    
    typeValidator = (NameValidator) new StringValidator();
    fieldValidator.setValidator(typeValidator);
    typeValidator.setWhiteSpace("preserve");
    
    desc.setValidator(fieldValidator);
    
    desc = new XMLFieldDescriptorImpl(FieldMapping.class, "_fieldMapping", "field", NodeType.Element);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        Container target = (Container)object;
        return target.getFieldMapping();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          Container target = (Container)object;
          target.setFieldMapping((FieldMapping)value);
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
    desc.setSchemaType("org.exolab.mapping.xml.FieldMapping");
    desc.setHandler(handler);
    desc.setNameSpaceURI("http://exec.exolab.org/");
    desc.setRequired(true);
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    addSequenceElement(desc);
    
    fieldValidator = new FieldValidator();
    fieldValidator.setMinOccurs(1);
    
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
    return Container.class;
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
