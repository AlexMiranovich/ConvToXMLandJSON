package org.exolab.mapping.loader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.exolab.mapping.ClassDescriptor;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.FieldHandler;

public class FieldDescriptorImpl implements FieldDescriptor {
  private ClassDescriptor _parent;
  private String _fieldName;
  private Class _fieldType;
  private ClassDescriptor _classDescriptor;
  private FieldHandler _handler;
  private boolean _transient;
  private boolean _immutable;
  private boolean _required;
  private boolean _multivalued;
  private boolean _identity;
  private Map _properties = new HashMap();
  private Set _natures = new HashSet();
  protected FieldDescriptorImpl() {}
  public FieldDescriptorImpl(String fieldName, TypeInfo typeInfo, FieldHandler handler, boolean isTransitive) {
    if (fieldName == null) {
      throw new IllegalArgumentException("Internal error. Field name not specified.");
    }
    if (handler == null) {
      throw new IllegalArgumentException("Internal error. No FieldHandler provided.");
    }
    setFieldName(fieldName);
    setFieldType(typeInfo.getFieldType());
    setHandler(handler);
    setTransient(isTransitive);
    setImmutable(typeInfo.isImmutable());
    setRequired(typeInfo.isRequired());
    setMultivalued(typeInfo.getCollectionHandler() != null);
  }
  public final void setContainingClassDescriptor(ClassDescriptor parent)
  {
    this._parent = parent;
  }
  public final ClassDescriptor getContainingClassDescriptor()
  {
    return this._parent;
  }
  public final void setFieldName(String fieldName)
  {
    this._fieldName = fieldName;
  }
  public final String getFieldName()
  {
    return this._fieldName;
  }
  public final void setFieldType(Class fieldType)
  {
    this._fieldType = fieldType;
  }
  public final Class getFieldType()
  {
    return this._fieldType;
  }
  public final void setClassDescriptor(ClassDescriptor classDescriptor)
  {
    this._classDescriptor = classDescriptor;
  }
  public final ClassDescriptor getClassDescriptor()
  {
    return this._classDescriptor;
  }
  public final void setHandler(FieldHandler handler)
  {
    this._handler = handler;
  }
  public final FieldHandler getHandler()
  {
    return this._handler;
  }
  public final void setTransient(boolean isTransient)
  {
    this._transient = isTransient;
  }
  public final boolean isTransient()
  {
    return this._transient;
  }
  public final void setImmutable(boolean immutable)
  {
    this._immutable = immutable;
  }
  public final boolean isImmutable()
  {
    return this._immutable;
  }
  public final void setRequired(boolean required)
  {
    this._required = required;
  }
  public final boolean isRequired()
  {
    return this._required;
  }
  public final void setMultivalued(boolean multivalued)
  {
    this._multivalued = multivalued;
  }
  public final boolean isMultivalued()
  {
    return this._multivalued;
  }
  public final void setIdentity(boolean identity)
  {
    this._identity = identity;
  }
  public final boolean isIdentity()
  {
    return this._identity;
  }
  public Object getProperty(String name)
  {
    return this._properties.get(name);
  }
  public void setProperty(String name, Object value)
  {
    this._properties.put(name, value);
  }
  public void addNature(String nature)
  {
    this._natures.add(nature);
  }
  public boolean hasNature(String nature)
  {
    return this._natures.contains(nature);
  }
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(getFieldName() + "(" + getFieldType().getName() + ")");
    return buffer.toString();
  }
}
