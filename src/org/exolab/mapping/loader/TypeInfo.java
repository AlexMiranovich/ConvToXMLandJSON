package org.exolab.mapping.loader;

import org.exolab.mapping.CollectionHandler;
import org.exolab.mapping.TypeConvertor;

public class TypeInfo {
  private Class _fieldType;
  private TypeConvertor _convertorTo;
  private TypeConvertor _convertorFrom;
  private boolean _immutable = false;
  private boolean _required = false;
  private Object _default;
  private CollectionHandler _colHandler;
  public TypeInfo(Class fieldType)
  {
    this(fieldType, null, null, false, null, null, true);
  }
  public TypeInfo(Class fieldType, TypeConvertor convertorTo, TypeConvertor convertorFrom, boolean required, Object defaultValue, CollectionHandler colHandler) {
    this(fieldType, convertorTo, convertorFrom, required, defaultValue, colHandler, true);
  }
  public TypeInfo(Class fieldType, TypeConvertor convertorTo, TypeConvertor convertorFrom, boolean required, Object defaultValue, CollectionHandler colHandler, boolean checkForCollection) {
    if ((colHandler == null) && (checkForCollection)) {
      if (fieldType.isArray()) {
        if (fieldType.getComponentType() != Byte.TYPE) {
          try {
            colHandler = CollectionHandlers.getHandler(Object[].class);
          }
          catch (Exception e) {
            throw new NullPointerException("Impossible to locate CollectionHandler for array.");
          }
        }
      }
      else {
        try {
          colHandler = CollectionHandlers.getHandler(fieldType);
        }
        catch (Exception e) {}
      }
    }
    this._fieldType = fieldType;
    this._convertorTo = convertorTo;
    this._convertorFrom = convertorFrom;
    this._immutable = Types.isImmutable(fieldType);
    this._required = required;
    this._default = (defaultValue == null ? Types.getDefault(fieldType) : defaultValue);
    this._colHandler = colHandler;
  }
  public Class getFieldType()
  {
    return this._fieldType;
  }
  public TypeConvertor getConvertorTo()
  {
    return this._convertorTo;
  }
  public TypeConvertor getConvertorFrom()
  {
    return this._convertorFrom;
  }
  public boolean isImmutable()
  {
    return this._immutable;
  }
  public boolean isRequired()
  {
    return this._required;
  }
  public Object getDefaultValue()
  {
    return this._default;
  }
  public CollectionHandler getCollectionHandler()
  {
    return this._colHandler;
  }
  public void setRequired(boolean required)
  {
    this._required = required;
  }
  public void setCollectionHandler(CollectionHandler handler)
  {
    this._colHandler = handler;
  }
  public void setImmutable(boolean immutable)
  {
    this._immutable = immutable;
  }
}
