package org.exolab.mapping.xml.descriptors;

import org.exolab.mapping.AccessMode;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.FieldHandler;
import org.exolab.mapping.xml.Sql;
import org.exolab.mapping.xml.types.SqlDirtyType;
import org.exolab.xml.FieldValidator;
import org.exolab.xml.NodeType;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.handlers.CollectionFieldHandler;
import org.exolab.xml.handlers.EnumFieldHandler;
import org.exolab.xml.util.XMLClassDescriptorImpl;
import org.exolab.xml.util.XMLFieldDescriptorImpl;
import org.exolab.xml.validators.BooleanValidator;
import org.exolab.xml.validators.NameValidator;
import org.exolab.xml.validators.StringValidator;

public class SqlDescriptor extends XMLClassDescriptorImpl {
  private boolean _elementDefinition;
  private String _nsPrefix;
  private String _nsURI;
  private String _xmlName;
  private XMLFieldDescriptor _identity;
  public SqlDescriptor() {
    this._nsURI = "http://exec.exolab.org/";
    this._xmlName = "sql";
    this._elementDefinition = true;
    XMLFieldDescriptorImpl desc = null;
    FieldHandler handler = null;
    FieldValidator fieldValidator = null;
    desc = new XMLFieldDescriptorImpl(String.class, "_name", "name", NodeType.Attribute);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        Sql target = (Sql)object;
        return target.getName();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          Sql target = (Sql)object;
          target.addName((String)value);
        } catch (Exception ex) {
          throw new IllegalStateException(ex.toString());
        }
      }
      public void resetValue(Object object)
        throws IllegalStateException, IllegalArgumentException {
        try {
          Sql target = (Sql)object;
          target.removeAllName();
        } catch (Exception ex) {
          throw new IllegalStateException(ex.toString());
        }
      }
      public Object newInstance(Object parent)
      {
        return new String();
      }
    };
    handler = new CollectionFieldHandler(handler, new NameValidator((short)1));
    desc.setSchemaType("list");
    desc.setComponentType("NMTOKEN");
    desc.setHandler(handler);
    desc.setMultivalued(true);
    addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    fieldValidator.setMinOccurs(0);
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(String.class, "_type", "type", NodeType.Attribute);
    desc.setImmutable(true);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        Sql target = (Sql)object;
        return target.getType();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          Sql target = (Sql)object;
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
    StringValidator typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    typeValidator.setWhiteSpace("preserve");
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(String.class, "_manyTable", "many-table", NodeType.Attribute);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        Sql target = (Sql)object;
        return target.getManyTable();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          Sql target = (Sql)object;
          target.setManyTable((String)value);
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
    desc = new XMLFieldDescriptorImpl(String.class, "_manyKey", "many-key", NodeType.Attribute);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        Sql target = (Sql)object;
        return target.getManyKey();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          Sql target = (Sql)object;
          target.addManyKey((String)value);
        } catch (Exception ex) {
          throw new IllegalStateException(ex.toString());
        }
      }
      public void resetValue(Object object)
        throws IllegalStateException, IllegalArgumentException {
        try {
          Sql target = (Sql)object;
          target.removeAllManyKey();
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
    handler = new CollectionFieldHandler(handler, new NameValidator((short)1));
    desc.setSchemaType("list");
    desc.setComponentType("NMTOKEN");
    desc.setHandler(handler);
    desc.setMultivalued(true);
    addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    fieldValidator.setMinOccurs(0);
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(Boolean.TYPE, "_readOnly", "read-only", NodeType.Attribute);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        Sql target = (Sql)object;
        if (!target.hasReadOnly()) {
          return null;
        }
        return target.getReadOnly() ? Boolean.TRUE : Boolean.FALSE;
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          Sql target = (Sql)object;
          if (value == null) {
            target.deleteReadOnly();
            return;
          }
          target.setReadOnly(((Boolean)value).booleanValue());
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
    typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);

    desc.setValidator(fieldValidator);

    desc = new XMLFieldDescriptorImpl(Boolean.TYPE, "_transient", "transient", NodeType.Attribute);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        Sql target = (Sql)object;
        if (!target.hasTransient()) {
          return null;
        }
        return target.getTransient() ? Boolean.TRUE : Boolean.FALSE;
      }

      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          Sql target = (Sql)object;
          if (value == null)
          {
            target.deleteTransient();
            return;
          }
          target.setTransient(((Boolean)value).booleanValue());
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
    desc = new XMLFieldDescriptorImpl(SqlDirtyType.class, "_dirty", "dirty", NodeType.Attribute);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        Sql target = (Sql)object;
        return target.getDirty();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          Sql target = (Sql)object;
          target.setDirty((SqlDirtyType)value);
        } catch (Exception ex) {
          throw new IllegalStateException(ex.toString());
        }
      }
      public Object newInstance(Object parent)
      {
        return null;
      }
    };
    handler = new EnumFieldHandler(SqlDirtyType.class, handler);
    desc.setImmutable(true);
    desc.setSchemaType("SqlDirtyType");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
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
    return Sql.class;
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
