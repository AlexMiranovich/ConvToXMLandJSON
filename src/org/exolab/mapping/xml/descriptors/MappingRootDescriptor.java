package org.exolab.mapping.xml.descriptors;

import org.exolab.mapping.AccessMode;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.FieldHandler;
import org.exolab.mapping.xml.ClassMapping;
import org.exolab.mapping.xml.FieldHandlerDef;
import org.exolab.mapping.xml.Include;
import org.exolab.mapping.xml.KeyGeneratorDef;
import org.exolab.mapping.xml.MappingRoot;
import org.exolab.xml.FieldValidator;
import org.exolab.xml.NodeType;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.util.XMLClassDescriptorImpl;
import org.exolab.xml.util.XMLFieldDescriptorImpl;
import org.exolab.xml.validators.StringValidator;

public class MappingRootDescriptor extends XMLClassDescriptorImpl {
  private boolean _elementDefinition;
  private String _nsPrefix;
  private String _nsURI;
  private String _xmlName;
  private XMLFieldDescriptor _identity;
  public MappingRootDescriptor() {
    this._nsURI = "http://exec.exolab.org/";
    this._xmlName = "mapping";
    this._elementDefinition = true;
    setCompositorAsSequence();
    XMLFieldDescriptorImpl desc = null;
    FieldHandler handler = null;
    FieldValidator fieldValidator = null;
    desc = new XMLFieldDescriptorImpl(String.class, "_description", "description", NodeType.Element);
    desc.setImmutable(true);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        MappingRoot target = (MappingRoot)object;
        return target.getDescription();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try
        {
          MappingRoot target = (MappingRoot)object;
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
    StringValidator typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    typeValidator.setWhiteSpace("preserve");
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(Include.class, "_includeList", "include", NodeType.Element);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        MappingRoot target = (MappingRoot)object;
        return target.getInclude();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          MappingRoot target = (MappingRoot)object;
          target.addInclude((Include)value);
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
          MappingRoot target = (MappingRoot)object;
          target.removeAllInclude();
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
    desc.setComponentType("org.exolab.mapping.xml.Include");
    desc.setHandler(handler);
    desc.setNameSpaceURI("http://exec.exolab.org/");
    desc.setMultivalued(true);
    addFieldDescriptor(desc);
    addSequenceElement(desc);
    fieldValidator = new FieldValidator();
    fieldValidator.setMinOccurs(0);
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(ClassMapping.class, "_classMappingList", "class", NodeType.Element);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        MappingRoot target = (MappingRoot)object;
        return target.getClassMapping();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          MappingRoot target = (MappingRoot)object;
          target.addClassMapping((ClassMapping)value);
        }
        catch (Exception ex)
        {
          throw new IllegalStateException(ex.toString());
        }
      }
      public void resetValue(Object object) throws IllegalStateException, IllegalArgumentException {
        try
        {
          MappingRoot target = (MappingRoot)object;
          target.removeAllClassMapping();
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
    desc.setComponentType("org.exolab.mapping.xml.ClassMapping");
    desc.setHandler(handler);
    desc.setNameSpaceURI("http://exec.exolab.org/");
    desc.setMultivalued(true);
    addFieldDescriptor(desc);
    addSequenceElement(desc);
    fieldValidator = new FieldValidator();
    fieldValidator.setMinOccurs(0);
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(KeyGeneratorDef.class, "_keyGeneratorDefList", "key-generator", NodeType.Element);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        MappingRoot target = (MappingRoot)object;
        return target.getKeyGeneratorDef();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          MappingRoot target = (MappingRoot)object;
          target.addKeyGeneratorDef((KeyGeneratorDef)value);
        } catch (Exception ex) {
          throw new IllegalStateException(ex.toString());
        }
      }
      public void resetValue(Object object)
        throws IllegalStateException, IllegalArgumentException {
        try {
          MappingRoot target = (MappingRoot)object;
          target.removeAllKeyGeneratorDef();
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
    desc.setComponentType("org.exolab.mapping.xml.KeyGeneratorDef");
    desc.setHandler(handler);
    desc.setNameSpaceURI("http://exec.exolab.org/");
    desc.setMultivalued(true);
    addFieldDescriptor(desc);
    addSequenceElement(desc);
    fieldValidator = new FieldValidator();
    fieldValidator.setMinOccurs(0);
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(FieldHandlerDef.class, "_fieldHandlerDefList", "field-handler", NodeType.Element);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        MappingRoot target = (MappingRoot)object;
        return target.getFieldHandlerDef();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          MappingRoot target = (MappingRoot)object;
          target.addFieldHandlerDef((FieldHandlerDef)value);
        } catch (Exception ex) {
          throw new IllegalStateException(ex.toString());
        }
      }
      public void resetValue(Object object)
        throws IllegalStateException, IllegalArgumentException {
        try {
          MappingRoot target = (MappingRoot)object;
          target.removeAllFieldHandlerDef();
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
    desc.setComponentType("org.exolab.mapping.xml.FieldHandlerDef");
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
    return MappingRoot.class;
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
