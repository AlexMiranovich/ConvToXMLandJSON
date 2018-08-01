package org.exolab.mapping.xml.descriptors;

import org.exolab.mapping.AccessMode;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.FieldHandler;
import org.exolab.mapping.xml.*;
import org.exolab.mapping.xml.types.FieldMappingCollectionType;
import org.exolab.xml.FieldValidator;
import org.exolab.xml.NodeType;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.handlers.EnumFieldHandler;
import org.exolab.xml.util.XMLClassDescriptorImpl;
import org.exolab.xml.util.XMLFieldDescriptorImpl;
import org.exolab.xml.validators.BooleanValidator;
import org.exolab.xml.validators.IdValidator;
import org.exolab.xml.validators.StringValidator;

public class FieldMappingDescriptor extends XMLClassDescriptorImpl {
  private boolean _elementDefinition = true;
  private String _nsPrefix;
  private String _nsURI = "http://exec.exolab.org/";
  private String _xmlName = "field";
  private XMLFieldDescriptor _identity;

  public FieldMappingDescriptor() {
    this.setCompositorAsSequence();
    XMLFieldDescriptorImpl desc = null;
    FieldHandler handler = null;
    FieldValidator fieldValidator = null;
    desc = new XMLFieldDescriptorImpl(String.class, "_name", "name", NodeType.Attribute);
    desc.setImmutable(true);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object) throws IllegalStateException {
        FieldMapping target = (FieldMapping)object;
        return target.getName();
      }

      public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {
        try {
          FieldMapping target = (FieldMapping)object;
          target.setName((String)value);
        } catch (Exception var4) {
          throw new IllegalStateException(var4.toString());
        }
      }

      public Object newInstance(Object parent) {
        return null;
      }
    };
    desc.setSchemaType("string");
    desc.setHandler(handler);
    desc.setRequired(true);
    desc.setMultivalued(false);
    this.addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    fieldValidator.setMinOccurs(1);
    StringValidator typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    typeValidator.setWhiteSpace("preserve");
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(String.class, "_type", "type", NodeType.Attribute);
    desc.setImmutable(true);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object) throws IllegalStateException {
        FieldMapping target = (FieldMapping)object;
        return target.getType();
      }

      public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {
        try {
          FieldMapping target = (FieldMapping)object;
          target.setType((String)value);
        } catch (Exception var4) {
          throw new IllegalStateException(var4.toString());
        }
      }

      public Object newInstance(Object parent) {
        return null;
      }
    };
    desc.setSchemaType("string");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    this.addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    typeValidator.setWhiteSpace("preserve");
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(Boolean.TYPE, "_required", "required", NodeType.Attribute);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object) throws IllegalStateException {
        FieldMapping target = (FieldMapping)object;
        if (!target.hasRequired()) {
          return null;
        } else {
          return target.getRequired() ? Boolean.TRUE : Boolean.FALSE;
        }
      }

      public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {
        try {
          FieldMapping target = (FieldMapping)object;
          if (value == null) {
            target.deleteRequired();
          } else {
            target.setRequired((Boolean)value);
          }
        } catch (Exception var4) {
          throw new IllegalStateException(var4.toString());
        }
      }

      public Object newInstance(Object parent) {
        return null;
      }
    };
    desc.setSchemaType("boolean");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    this.addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(Boolean.TYPE, "_transient", "transient", NodeType.Attribute);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object) throws IllegalStateException {
        FieldMapping target = (FieldMapping)object;
        if (!target.hasTransient()) {
          return null;
        } else {
          return target.getTransient() ? Boolean.TRUE : Boolean.FALSE;
        }
      }

      public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {
        try {
          FieldMapping target = (FieldMapping)object;
          if (value == null) {
            target.deleteTransient();
          } else {
            target.setTransient((Boolean)value);
          }
        } catch (Exception var4) {
          throw new IllegalStateException(var4.toString());
        }
      }

      public Object newInstance(Object parent) {
        return null;
      }
    };
    desc.setSchemaType("boolean");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    this.addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(Boolean.TYPE, "_nillable", "nillable", NodeType.Attribute);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object) throws IllegalStateException {
        FieldMapping target = (FieldMapping)object;
        if (!target.hasNillable()) {
          return null;
        } else {
          return target.getNillable() ? Boolean.TRUE : Boolean.FALSE;
        }
      }

      public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {
        try {
          FieldMapping target = (FieldMapping)object;
          if (value == null) {
            target.deleteNillable();
          } else {
            target.setNillable((Boolean)value);
          }
        } catch (Exception var4) {
          throw new IllegalStateException(var4.toString());
        }
      }

      public Object newInstance(Object parent) {
        return null;
      }
    };
    desc.setSchemaType("boolean");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    this.addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(Boolean.TYPE, "_direct", "direct", NodeType.Attribute);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object) throws IllegalStateException {
        FieldMapping target = (FieldMapping)object;
        if (!target.hasDirect()) {
          return null;
        } else {
          return target.getDirect() ? Boolean.TRUE : Boolean.FALSE;
        }
      }

      public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {
        try {
          FieldMapping target = (FieldMapping)object;
          if (value == null) {
            target.deleteDirect();
          } else {
            target.setDirect((Boolean)value);
          }
        } catch (Exception var4) {
          throw new IllegalStateException(var4.toString());
        }
      }

      public Object newInstance(Object parent) {
        return null;
      }
    };
    desc.setSchemaType("boolean");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    this.addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(Boolean.TYPE, "_lazy", "lazy", NodeType.Attribute);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object) throws IllegalStateException {
        FieldMapping target = (FieldMapping)object;
        if (!target.hasLazy()) {
          return null;
        } else {
          return target.getLazy() ? Boolean.TRUE : Boolean.FALSE;
        }
      }

      public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {
        try {
          FieldMapping target = (FieldMapping)object;
          if (value == null) {
            target.deleteLazy();
          } else {
            target.setLazy((Boolean)value);
          }
        } catch (Exception var4) {
          throw new IllegalStateException(var4.toString());
        }
      }

      public Object newInstance(Object parent) {
        return null;
      }
    };
    desc.setSchemaType("boolean");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    this.addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(Boolean.TYPE, "_container", "container", NodeType.Attribute);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object) throws IllegalStateException {
        FieldMapping target = (FieldMapping)object;
        if (!target.hasContainer()) {
          return null;
        } else {
          return target.getContainer() ? Boolean.TRUE : Boolean.FALSE;
        }
      }

      public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {
        try {
          FieldMapping target = (FieldMapping)object;
          if (value == null) {
            target.deleteContainer();
          } else {
            target.setContainer((Boolean)value);
          }
        } catch (Exception var4) {
          throw new IllegalStateException(var4.toString());
        }
      }

      public Object newInstance(Object parent) {
        return null;
      }
    };
    desc.setSchemaType("boolean");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    this.addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(String.class, "_getMethod", "get-method", NodeType.Attribute);
    desc.setImmutable(true);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object) throws IllegalStateException {
        FieldMapping target = (FieldMapping)object;
        return target.getGetMethod();
      }

      public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {
        try {
          FieldMapping target = (FieldMapping)object;
          target.setGetMethod((String)value);
        } catch (Exception var4) {
          throw new IllegalStateException(var4.toString());
        }
      }

      public Object newInstance(Object parent) {
        return null;
      }
    };
    desc.setSchemaType("string");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    this.addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    typeValidator.setWhiteSpace("preserve");
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(String.class, "_hasMethod", "has-method", NodeType.Attribute);
    desc.setImmutable(true);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object) throws IllegalStateException {
        FieldMapping target = (FieldMapping)object;
        return target.getHasMethod();
      }

      public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {
        try {
          FieldMapping target = (FieldMapping)object;
          target.setHasMethod((String)value);
        } catch (Exception var4) {
          throw new IllegalStateException(var4.toString());
        }
      }

      public Object newInstance(Object parent) {
        return null;
      }
    };
    desc.setSchemaType("string");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    this.addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    typeValidator.setWhiteSpace("preserve");
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(String.class, "_setMethod", "set-method", NodeType.Attribute);
    desc.setImmutable(true);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object) throws IllegalStateException {
        FieldMapping target = (FieldMapping)object;
        return target.getSetMethod();
      }

      public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {
        try {
          FieldMapping target = (FieldMapping)object;
          target.setSetMethod((String)value);
        } catch (Exception var4) {
          throw new IllegalStateException(var4.toString());
        }
      }

      public Object newInstance(Object parent) {
        return null;
      }
    };
    desc.setSchemaType("string");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    this.addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    typeValidator.setWhiteSpace("preserve");
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(String.class, "_createMethod", "create-method", NodeType.Attribute);
    desc.setImmutable(true);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object) throws IllegalStateException {
        FieldMapping target = (FieldMapping)object;
        return target.getCreateMethod();
      }

      public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {
        try {
          FieldMapping target = (FieldMapping)object;
          target.setCreateMethod((String)value);
        } catch (Exception var4) {
          throw new IllegalStateException(var4.toString());
        }
      }

      public Object newInstance(Object parent) {
        return null;
      }
    };
    desc.setSchemaType("string");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    this.addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    typeValidator.setWhiteSpace("preserve");
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(String.class, "_handler", "handler", NodeType.Attribute);
    desc.setImmutable(true);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object) throws IllegalStateException {
        FieldMapping target = (FieldMapping)object;
        return target.getHandler();
      }

      public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {
        try {
          FieldMapping target = (FieldMapping)object;
          target.setHandler((String)value);
        } catch (Exception var4) {
          throw new IllegalStateException(var4.toString());
        }
      }

      public Object newInstance(Object parent) {
        return null;
      }
    };
    desc.setSchemaType("string");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    this.addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    typeValidator.setWhiteSpace("preserve");
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(FieldMappingCollectionType.class, "_collection", "collection", NodeType.Attribute);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object) throws IllegalStateException {
        FieldMapping target = (FieldMapping)object;
        return target.getCollection();
      }

      public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {
        try {
          FieldMapping target = (FieldMapping)object;
          target.setCollection((FieldMappingCollectionType)value);
        } catch (Exception var4) {
          throw new IllegalStateException(var4.toString());
        }
      }

      public Object newInstance(Object parent) {
        return null;
      }
    };
    handler = new EnumFieldHandler(FieldMappingCollectionType.class, handler);
    desc.setImmutable(true);
    desc.setSchemaType("FieldMappingCollectionType");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    this.addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(String.class, "_comparator", "comparator", NodeType.Attribute);
    desc.setImmutable(true);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object) throws IllegalStateException {
        FieldMapping target = (FieldMapping)object;
        return target.getComparator();
      }

      public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {
        try {
          FieldMapping target = (FieldMapping)object;
          target.setComparator((String)value);
        } catch (Exception var4) {
          throw new IllegalStateException(var4.toString());
        }
      }

      public Object newInstance(Object parent) {
        return null;
      }
    };
    desc.setSchemaType("string");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    this.addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    typeValidator.setWhiteSpace("preserve");
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(Boolean.TYPE, "_identity", "identity", NodeType.Attribute);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object) throws IllegalStateException {
        FieldMapping target = (FieldMapping)object;
        if (!target.hasIdentity()) {
          return null;
        } else {
          return target.getIdentity() ? Boolean.TRUE : Boolean.FALSE;
        }
      }

      public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {
        try {
          FieldMapping target = (FieldMapping)object;
          if (value == null) {
            target.deleteIdentity();
          } else {
            target.setIdentity((Boolean)value);
          }
        } catch (Exception var4) {
          throw new IllegalStateException(var4.toString());
        }
      }

      public Object newInstance(Object parent) {
        return null;
      }
    };
    desc.setSchemaType("boolean");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    this.addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(String.class, "_description", "description", NodeType.Element);
    desc.setImmutable(true);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object) throws IllegalStateException {
        FieldMapping target = (FieldMapping)object;
        return target.getDescription();
      }

      public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {
        try {
          FieldMapping target = (FieldMapping)object;
          target.setDescription((String)value);
        } catch (Exception var4) {
          throw new IllegalStateException(var4.toString());
        }
      }

      public Object newInstance(Object parent) {
        return null;
      }
    };
    desc.setSchemaType("string");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    this.addFieldDescriptor(desc);
    this.addSequenceElement(desc);
    fieldValidator = new FieldValidator();
    typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    typeValidator.setWhiteSpace("preserve");
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(Sql.class, "_sql", "sql", NodeType.Element);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object) throws IllegalStateException {
        FieldMapping target = (FieldMapping)object;
        return target.getSql();
      }

      public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {
        try {
          FieldMapping target = (FieldMapping)object;
          target.setSql((Sql)value);
        } catch (Exception var4) {
          throw new IllegalStateException(var4.toString());
        }
      }

      public Object newInstance(Object parent) {
        return null;
      }
    };
    desc.setSchemaType("org.exolab.mapping.xml.Sql");
    desc.setHandler(handler);
    desc.setNameSpaceURI("http://exec.exolab.org/");
    desc.setMultivalued(false);
    this.addFieldDescriptor(desc);
    this.addSequenceElement(desc);
    fieldValidator = new FieldValidator();
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(BindXml.class, "_bindXml", "bind-xml", NodeType.Element);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object) throws IllegalStateException {
        FieldMapping target = (FieldMapping)object;
        return target.getBindXml();
      }

      public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {
        try {
          FieldMapping target = (FieldMapping)object;
          target.setBindXml((BindXml)value);
        } catch (Exception var4) {
          throw new IllegalStateException(var4.toString());
        }
      }

      public Object newInstance(Object parent) {
        return null;
      }
    };
    desc.setSchemaType("org.exolab.mapping.xml.BindXml");
    desc.setHandler(handler);
    desc.setNameSpaceURI("http://exec.exolab.org/");
    desc.setMultivalued(false);
    this.addFieldDescriptor(desc);
    this.addSequenceElement(desc);
    fieldValidator = new FieldValidator();
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(Ldap.class, "_ldap", "ldap", NodeType.Element);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object) throws IllegalStateException {
        FieldMapping target = (FieldMapping)object;
        return target.getLdap();
      }

      public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {
        try {
          FieldMapping target = (FieldMapping)object;
          target.setLdap((Ldap)value);
        } catch (Exception var4) {
          throw new IllegalStateException(var4.toString());
        }
      }

      public Object newInstance(Object parent) {
        return null;
      }
    };
    desc.setSchemaType("org.exolab.mapping.xml.Ldap");
    desc.setHandler(handler);
    desc.setNameSpaceURI("http://exec.exolab.org/");
    desc.setMultivalued(false);
    this.addFieldDescriptor(desc);
    this.addSequenceElement(desc);
    fieldValidator = new FieldValidator();
    desc.setValidator(fieldValidator);
  }

  public AccessMode getAccessMode() {
    return null;
  }

  public FieldDescriptor getIdentity() {
    return this._identity;
  }

  public Class getJavaClass() {
    return FieldMapping.class;
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
