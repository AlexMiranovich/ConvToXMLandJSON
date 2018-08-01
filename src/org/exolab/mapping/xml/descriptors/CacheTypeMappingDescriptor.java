package org.exolab.mapping.xml.descriptors;

import org.exolab.mapping.AccessMode;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.FieldHandler;
import org.exolab.mapping.xml.CacheTypeMapping;
import org.exolab.mapping.xml.Param;
import org.exolab.xml.FieldValidator;
import org.exolab.xml.NodeType;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.util.XMLClassDescriptorImpl;
import org.exolab.xml.util.XMLFieldDescriptorImpl;
import org.exolab.xml.validators.BooleanValidator;
import org.exolab.xml.validators.LongValidator;
import org.exolab.xml.validators.StringValidator;

public class CacheTypeMappingDescriptor extends XMLClassDescriptorImpl {
  private boolean _elementDefinition;
  private String _nsPrefix;
  private String _nsURI;
  private String _xmlName;
  private XMLFieldDescriptor _identity;
  public CacheTypeMappingDescriptor() {
    this._nsURI = "http://exec.exolab.org/";
    this._xmlName = "cache-type";
    this._elementDefinition = true;
    setCompositorAsSequence();
    XMLFieldDescriptorImpl desc = null;
    FieldHandler handler = null;
    FieldValidator fieldValidator = null;
    desc = new XMLFieldDescriptorImpl(String.class, "_type", "type", NodeType.Attribute);
    desc.setImmutable(true);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        CacheTypeMapping target = (CacheTypeMapping)object;
        return target.getType();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          CacheTypeMapping target = (CacheTypeMapping)object;
          target.setType((String)value);
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
    LongValidator typeValidator = new LongValidator();
    fieldValidator.setValidator(typeValidator);
    typeValidator.setWhiteSpace("preserve");
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(Boolean.TYPE, "_debug", "debug", NodeType.Attribute);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        CacheTypeMapping target = (CacheTypeMapping)object;
        if (!target.hasDebug()) {
          return null;
        }
        return target.getDebug() ? Boolean.TRUE : Boolean.FALSE;
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          CacheTypeMapping target = (CacheTypeMapping)object;
          if (value == null) {
            target.deleteDebug();
            return;
          }
          target.setDebug(((Boolean)value).booleanValue());
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
    typeValidator = new LongValidator();
    fieldValidator.setValidator(typeValidator);
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(Long.TYPE, "_capacity", "capacity", NodeType.Attribute);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        CacheTypeMapping target = (CacheTypeMapping)object;
        if (!target.hasCapacity()) {
          return null;
        }
        return new Long(target.getCapacity());
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          CacheTypeMapping target = (CacheTypeMapping)object;
          if (value == null) {
            target.deleteCapacity();
            return;
          }
          target.setCapacity(((Long)value).longValue());
        } catch (Exception ex) {
          throw new IllegalStateException(ex.toString());
        }
      }
      public Object newInstance(Object parent)
      {
        return null;
      }
    };
    desc.setSchemaType("integer");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    typeValidator = new LongValidator();
    fieldValidator.setValidator(typeValidator);
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(Param.class, "_paramList", "param", NodeType.Element);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        CacheTypeMapping target = (CacheTypeMapping)object;
        return target.getParam();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          CacheTypeMapping target = (CacheTypeMapping)object;
          target.addParam((Param)value);
        } catch (Exception ex) {
          throw new IllegalStateException(ex.toString());
        }
      }
      public void resetValue(Object object)
        throws IllegalStateException, IllegalArgumentException {
        try {
          CacheTypeMapping target = (CacheTypeMapping)object;
          target.removeAllParam();
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
    desc.setComponentType("org.exolab.mapping.xml.Param");
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
    return CacheTypeMapping.class;
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
