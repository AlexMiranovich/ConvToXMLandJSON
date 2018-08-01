package org.exolab.mapping.xml.descriptors;

import org.exolab.mapping.AccessMode;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.FieldHandler;
import org.exolab.mapping.xml.MapTo;
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

public class MapToDescriptor extends XMLClassDescriptorImpl {
  private boolean _elementDefinition;
  private String _nsPrefix;
  private String _nsURI;
  private String _xmlName;
  private XMLFieldDescriptor _identity;
  public MapToDescriptor()
  {
    this._nsURI = "http://exec.exolab.org/";
    this._xmlName = "map-to";
    this._elementDefinition = true;
    XMLFieldDescriptorImpl desc = null;
    FieldHandler handler = null;
    FieldValidator fieldValidator = null;
    
    desc = new XMLFieldDescriptorImpl(String.class, "_table", "table", NodeType.Attribute);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        MapTo target = (MapTo)object;
        return target.getTable();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          MapTo target = (MapTo)object;
          target.setTable((String)value);
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
    StringValidator typeValidator = new NameValidator((short)1);
    fieldValidator.setValidator(typeValidator);
    typeValidator.addPattern("[\\w-._:]+");
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(String.class, "_xml", "xml", NodeType.Attribute);
    desc.setImmutable(true);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        MapTo target = (MapTo)object;
        return target.getXml();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          MapTo target = (MapTo)object;
          target.setXml((String)value);
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
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    typeValidator.setWhiteSpace("preserve");
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(String.class, "_nsUri", "ns-uri", NodeType.Attribute);
    desc.setImmutable(true);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        MapTo target = (MapTo)object;
        return target.getNsUri();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          MapTo target = (MapTo)object;
          target.setNsUri((String)value);
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
    typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    typeValidator.setWhiteSpace("preserve");
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(String.class, "_nsPrefix", "ns-prefix", NodeType.Attribute);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        MapTo target = (MapTo)object;
        return target.getNsPrefix();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          MapTo target = (MapTo)object;
          target.setNsPrefix((String)value);
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
    desc = new XMLFieldDescriptorImpl(Boolean.TYPE, "_elementDefinition", "element-definition", NodeType.Attribute);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        MapTo target = (MapTo)object;
        if (!target.hasElementDefinition()) {
          return null;
        }
        return target.getElementDefinition() ? Boolean.TRUE : Boolean.FALSE;
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          MapTo target = (MapTo)object;
          if (value == null)
          {
            target.deleteElementDefinition();
            return;
          }
          target.setElementDefinition(((Boolean)value).booleanValue());
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
    typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(String.class, "_ldapDn", "ldap-dn", NodeType.Attribute);
    desc.setImmutable(true);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        MapTo target = (MapTo)object;
        return target.getLdapDn();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          MapTo target = (MapTo)object;
          target.setLdapDn((String)value);
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
    typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    typeValidator.setWhiteSpace("preserve");
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(String.class, "_ldapOc", "ldap-oc", NodeType.Attribute);
    desc.setImmutable(true);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        MapTo target = (MapTo)object;
        return target.getLdapOc();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          MapTo target = (MapTo)object;
          target.setLdapOc((String)value);
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
    return MapTo.class;
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
