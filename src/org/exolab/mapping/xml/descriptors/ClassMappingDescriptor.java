package org.exolab.mapping.xml.descriptors;

import org.exolab.mapping.AccessMode;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.FieldHandler;
import org.exolab.mapping.xml.CacheTypeMapping;
import org.exolab.mapping.xml.ClassChoice;
import org.exolab.mapping.xml.ClassMapping;
import org.exolab.mapping.xml.MapTo;
import org.exolab.mapping.xml.NamedNativeQuery;
import org.exolab.mapping.xml.NamedQuery;
import org.exolab.mapping.xml.types.ClassMappingAccessType;
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
import org.exolab.xml.validators.IdRefValidator;
import org.exolab.xml.validators.NameValidator;
import org.exolab.xml.validators.StringValidator;

public class ClassMappingDescriptor extends XMLClassDescriptorImpl {
  private boolean _elementDefinition;
  private String _nsPrefix;
  private String _nsURI;
  private String _xmlName;
  private XMLFieldDescriptor _identity;
  public ClassMappingDescriptor() {
    this._nsURI = "http://exec.exolab.org/";
    this._xmlName = "class";
    this._elementDefinition = true;
    setCompositorAsSequence();
    XMLFieldDescriptorImpl desc = null;
    FieldHandler handler = null;
    FieldValidator fieldValidator = null;
    desc = new XMLFieldDescriptorImpl(String.class, "_name", "name", NodeType.Attribute);
    this._identity = desc;
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        ClassMapping target = (ClassMapping)object;
        return target.getName();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try
        {
          ClassMapping target = (ClassMapping)object;
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
    desc.setSchemaType("ID");
    desc.setHandler(handler);
    desc.setRequired(true);
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    fieldValidator.setMinOccurs(1);
    StringValidator typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(Object.class, "_extends", "extends", NodeType.Attribute);
    desc.setReference(true);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        ClassMapping target = (ClassMapping)object;
        return target.getExtends();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          ClassMapping target = (ClassMapping)object;
          target.setExtends(value);
        }
        catch (Exception ex)
        {
          throw new IllegalStateException(ex.toString());
        }
      }
      
      public Object newInstance(Object parent)
      {
        return new Object();
      }
    };
    desc.setSchemaType("IDREF");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    
    fieldValidator = new FieldValidator();
    typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(Object.class, "_depends", "depends", NodeType.Attribute);
    desc.setReference(true);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        ClassMapping target = (ClassMapping)object;
        return target.getDepends();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          ClassMapping target = (ClassMapping)object;
          target.setDepends(value);
        }
        catch (Exception ex) {
          throw new IllegalStateException(ex.toString());
        }
      }
      public Object newInstance(Object parent)
      {
        return new Object();
      }
    };
    desc.setSchemaType("IDREF");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    typeValidator = new IdRefValidator();
    fieldValidator.setValidator(typeValidator);
    
    desc.setValidator(fieldValidator);
    
    desc = new XMLFieldDescriptorImpl(String.class, "_identity", "identity", NodeType.Attribute);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        ClassMapping target = (ClassMapping)object;
        return target.getIdentity();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          ClassMapping target = (ClassMapping)object;
          target.addIdentity((String)value);
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
          ClassMapping target = (ClassMapping)object;
          target.removeAllIdentity();
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
    
    desc = new XMLFieldDescriptorImpl(ClassMappingAccessType.class, "_access", "access", NodeType.Attribute);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        ClassMapping target = (ClassMapping)object;
        return target.getAccess();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          ClassMapping target = (ClassMapping)object;
          target.setAccess((ClassMappingAccessType)value);
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
    handler = new EnumFieldHandler(ClassMappingAccessType.class, handler);
    desc.setImmutable(true);
    desc.setSchemaType("ClassMappingAccessType");
    desc.setHandler(handler);
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    fieldValidator = new FieldValidator();
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(String.class, "_keyGenerator", "key-generator", NodeType.Attribute);
    desc.setImmutable(true);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        ClassMapping target = (ClassMapping)object;
        return target.getKeyGenerator();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          ClassMapping target = (ClassMapping)object;
          target.setKeyGenerator((String)value);
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

    desc = new XMLFieldDescriptorImpl(Boolean.TYPE, "_autoComplete", "auto-complete", NodeType.Attribute);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        ClassMapping target = (ClassMapping)object;
        if (!target.hasAutoComplete()) {
          return null;
        }
        return target.getAutoComplete() ? Boolean.TRUE : Boolean.FALSE;
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          ClassMapping target = (ClassMapping)object;
          if (value == null)
          {
            target.deleteAutoComplete();
            return;
          }
          target.setAutoComplete(((Boolean)value).booleanValue());
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
    
    desc = new XMLFieldDescriptorImpl(Boolean.TYPE, "_verifyConstructable", "verify-constructable", NodeType.Attribute);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        ClassMapping target = (ClassMapping)object;
        if (!target.hasVerifyConstructable()) {
          return null;
        }
        return target.getVerifyConstructable() ? Boolean.TRUE : Boolean.FALSE;
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          ClassMapping target = (ClassMapping)object;
          if (value == null)
          {
            target.deleteVerifyConstructable();
            return;
          }
          target.setVerifyConstructable(((Boolean)value).booleanValue());
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
    
    desc = new XMLFieldDescriptorImpl(String.class, "_version", "version", NodeType.Attribute);
    desc.setImmutable(true);
    handler = new XMLFieldHandler()
    {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        ClassMapping target = (ClassMapping)object;
        return target.getVersion();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          ClassMapping target = (ClassMapping)object;
          target.setVersion((String)value);
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
    desc = new XMLFieldDescriptorImpl(String.class, "_description", "description", NodeType.Element);
    desc.setImmutable(true);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        ClassMapping target = (ClassMapping)object;
        return target.getDescription();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          ClassMapping target = (ClassMapping)object;
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
    typeValidator = new StringValidator();
    fieldValidator.setValidator(typeValidator);
    typeValidator.setWhiteSpace("preserve");
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(CacheTypeMapping.class, "_cacheTypeMapping", "cache-type", NodeType.Element);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        ClassMapping target = (ClassMapping)object;
        return target.getCacheTypeMapping();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          ClassMapping target = (ClassMapping)object;
          target.setCacheTypeMapping((CacheTypeMapping)value);
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
    desc.setSchemaType("org.exolab.mapping.xml.CacheTypeMapping");
    desc.setHandler(handler);
    desc.setNameSpaceURI("http://exec.exolab.org/");
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    addSequenceElement(desc);
    fieldValidator = new FieldValidator();
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(MapTo.class, "_mapTo", "map-to", NodeType.Element);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException
      {
        ClassMapping target = (ClassMapping)object;
        return target.getMapTo();
      }
      
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException
      {
        try
        {
          ClassMapping target = (ClassMapping)object;
          target.setMapTo((MapTo)value);
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
    desc.setSchemaType("org.exolab.mapping.xml.MapTo");
    desc.setHandler(handler);
    desc.setNameSpaceURI("http://exec.exolab.org/");
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    addSequenceElement(desc);
    fieldValidator = new FieldValidator();
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(NamedQuery.class, "_namedQueryList", "named-query", NodeType.Element);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        ClassMapping target = (ClassMapping)object;
        return target.getNamedQuery();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          ClassMapping target = (ClassMapping)object;
          target.addNamedQuery((NamedQuery)value);
        }
        catch (Exception ex)
        {
          throw new IllegalStateException(ex.toString());
        }
      }
      public void resetValue(Object object)
        throws IllegalStateException, IllegalArgumentException {
        try
        {
          ClassMapping target = (ClassMapping)object;
          target.removeAllNamedQuery();
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
    desc.setComponentType("org.exolab.mapping.xml.NamedQuery");
    desc.setHandler(handler);
    desc.setNameSpaceURI("http://exec.exolab.org/");
    desc.setMultivalued(true);
    addFieldDescriptor(desc);
    addSequenceElement(desc);
    fieldValidator = new FieldValidator();
    fieldValidator.setMinOccurs(0);
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(NamedNativeQuery.class, "_namedNativeQueryList", "named-native-query", NodeType.Element);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        ClassMapping target = (ClassMapping)object;
        return target.getNamedNativeQuery();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try
        {
          ClassMapping target = (ClassMapping)object;
          target.addNamedNativeQuery((NamedNativeQuery)value);
        }
        catch (Exception ex)
        {
          throw new IllegalStateException(ex.toString());
        }
      }
      public void resetValue(Object object)
        throws IllegalStateException, IllegalArgumentException {
        try {
          ClassMapping target = (ClassMapping)object;
          target.removeAllNamedNativeQuery();
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
    desc.setComponentType("org.exolab.mapping.xml.NamedNativeQuery");
    desc.setHandler(handler);
    desc.setNameSpaceURI("http://exec.exolab.org/");
    desc.setMultivalued(true);
    addFieldDescriptor(desc);
    addSequenceElement(desc);
    fieldValidator = new FieldValidator();
    fieldValidator.setMinOccurs(0);
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(ClassChoice.class, "_classChoice", "-error-if-this-is-used-", NodeType.Element);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        ClassMapping target = (ClassMapping)object;
        return target.getClassChoice();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          ClassMapping target = (ClassMapping)object;
          target.setClassChoice((ClassChoice)value);
        }
        catch (Exception ex)
        {
          throw new IllegalStateException(ex.toString());
        }
      }
      
      public Object newInstance(Object parent)
      {
        return new ClassChoice();
      }
    };
    desc.setSchemaType("org.exolab.mapping.xml.ClassChoice");
    desc.setHandler(handler);
    desc.setContainer(true);
    desc.setClassDescriptor(new ClassChoiceDescriptor());
    desc.setNameSpaceURI("http://exec.exolab.org/");
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    addSequenceElement(desc);
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
    return ClassMapping.class;
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
