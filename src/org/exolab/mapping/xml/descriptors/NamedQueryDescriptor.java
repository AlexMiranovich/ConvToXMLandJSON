package org.exolab.mapping.xml.descriptors;

import org.exolab.mapping.AccessMode;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.FieldHandler;
import org.exolab.mapping.xml.NamedQuery;
import org.exolab.mapping.xml.QueryHint;
import org.exolab.xml.FieldValidator;
import org.exolab.xml.NodeType;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.util.XMLClassDescriptorImpl;
import org.exolab.xml.util.XMLFieldDescriptorImpl;
import org.exolab.xml.validators.StringValidator;

public class NamedQueryDescriptor
  extends XMLClassDescriptorImpl
{
  private boolean _elementDefinition;
  private String _nsPrefix;
  private String _nsURI;
  private String _xmlName;
  private XMLFieldDescriptor _identity;
  public NamedQueryDescriptor() {
    this._nsURI = "http://exec.exolab.org/";
    this._xmlName = "named-query";
    this._elementDefinition = true;
    setCompositorAsSequence();
    XMLFieldDescriptorImpl desc = null;
    FieldHandler handler = null;
    FieldValidator fieldValidator = null;
    desc = new XMLFieldDescriptorImpl(String.class, "_name", "name", NodeType.Attribute);
    desc.setImmutable(true);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        NamedQuery target = (NamedQuery)object;
        return target.getName();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          NamedQuery target = (NamedQuery)object;
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
    fieldValidator = new FieldValidator();
    fieldValidator.setMinOccurs(1);
    StringValidator typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    typeValidator.setWhiteSpace("preserve");
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(String.class, "_query", "query", NodeType.Element);
    desc.setImmutable(true);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        NamedQuery target = (NamedQuery)object;
        return target.getQuery();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          NamedQuery target = (NamedQuery)object;
          target.setQuery((String)value);
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
    typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    typeValidator.setWhiteSpace("preserve");
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(QueryHint.class, "_queryHintList", "query-hint", NodeType.Element);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        NamedQuery target = (NamedQuery)object;
        return target.getQueryHint();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          NamedQuery target = (NamedQuery)object;
          target.addQueryHint((QueryHint)value);
        }
        catch (Exception ex)
        {
          throw new IllegalStateException(ex.toString());
        }
      }
      
      public void resetValue(Object object)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          NamedQuery target = (NamedQuery)object;
          target.removeAllQueryHint();
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
    desc.setSchemaType("list");
    desc.setComponentType("org.exolab.mapping.xml.QueryHint");
    desc.setHandler(handler);
    desc.setNameSpaceURI("http://exec.exolab.org/");
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
  
  public FieldDescriptor getIdentity()
  {
    return this._identity;
  }
  
  public Class getJavaClass()
  {
    return NamedQuery.class;
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
