package org.exolab.mapping.loader;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Iterator;
import org.exec.core.util.Messages;
import org.exolab.exceptions.ExecIllegalStateException;
import org.exolab.mapping.AbstractFieldHandler;
import org.exolab.mapping.CollectionHandler;
import org.exolab.mapping.ExtendedFieldHandler;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.FieldHandler;
import org.exolab.mapping.GeneralizedFieldHandler;
import org.exolab.mapping.MappingException;
import org.exolab.mapping.MappingRuntimeException;
import org.exolab.mapping.TypeConvertor;
import org.exolab.util.IteratorEnumeration;

public final class FieldHandlerImpl extends AbstractFieldHandler {
  private static final String ADD_PREFIX = "add";
  private static final String ENUM_PREFIX = "enum";
  private static final String ITER_PREFIX = "iter";
  private final FieldHandler _handler;
  private final Field _field;
  private Method[] _getSequence;
  private Method[] _setSequence;
  private Method _addMethod;
  private Method _enumMethod;
  private Method _iterMethod;
  private Method _getMethod;
  private Method _setMethod;
  private Method _hasMethod;
  private Method _deleteMethod;
  private Method _createMethod;
  private final String _fieldName;
  private final Class _fieldType;
  private final boolean _immutable;
  private final Object _default;
  private TypeConvertor _convertTo = null;
  private TypeConvertor _convertFrom = null;
  private final CollectionHandler _colHandler;
  
  public FieldHandlerImpl(FieldHandler handler, TypeInfo typeInfo) {
    this._handler = handler;
    this._field = null;
    this._fieldName = handler.toString();
    this._fieldType = Types.typeFromPrimitive(typeInfo.getFieldType());
    this._immutable = typeInfo.isImmutable();
    this._default = typeInfo.getDefaultValue();
    this._convertTo = typeInfo.getConvertorTo();
    this._convertFrom = typeInfo.getConvertorFrom();
    this._colHandler = typeInfo.getCollectionHandler();
  }
  
  public FieldHandlerImpl(Field field, TypeInfo typeInfo)
    throws MappingException
  {
    if ((field.getModifiers() != 1) && (field.getModifiers() != 65)) {
      throw new MappingException("mapping.fieldNotAccessible", field.getName(), field.getDeclaringClass().getName());
    }
    this._handler = null;
    this._field = field;
    this._fieldType = Types.typeFromPrimitive(typeInfo.getFieldType());
    this._fieldName = (field.getName() + "(" + field.getType().getName() + ")");
    this._immutable = typeInfo.isImmutable();
    if (this._field.getType().isPrimitive()) {
      this._default = typeInfo.getDefaultValue();
    } else {
      this._default = null;
    }
    this._convertTo = typeInfo.getConvertorTo();
    this._convertFrom = typeInfo.getConvertorFrom();
    this._colHandler = typeInfo.getCollectionHandler();
  }
  
  public FieldHandlerImpl(String fieldName, Method[] getSequence, Method[] setSequence, Method getMethod, Method setMethod, TypeInfo typeInfo)
    throws MappingException
  {
    this._handler = null;
    this._field = null;
    if (fieldName == null) {
      throw new IllegalArgumentException("Argument 'fieldName' is null");
    }
    this._getSequence = getSequence;
    this._setSequence = setSequence;
    if (setMethod != null) {
      if (setMethod.getName().startsWith("add"))
      {
        Class pType = setMethod.getParameterTypes()[0];
        if (pType != typeInfo.getFieldType()) {
          setAddMethod(setMethod);
        } else {
          setWriteMethod(setMethod);
        }
      }
      else
      {
        setWriteMethod(setMethod);
      }
    }
    if (getMethod != null) {
      if (getMethod.getName().startsWith("enum"))
      {
        Class rType = getMethod.getReturnType();
        if (Enumeration.class.isAssignableFrom(rType)) {
          setEnumMethod(getMethod);
        } else {
          setReadMethod(getMethod);
        }
      }
      else if (getMethod.getName().startsWith("iter"))
      {
        Class rType = getMethod.getReturnType();
        if (Iterator.class.isAssignableFrom(rType)) {
          setIterMethod(getMethod);
        } else {
          setReadMethod(getMethod);
        }
      }
      else
      {
        setReadMethod(getMethod);
      }
    }
    this._fieldType = Types.typeFromPrimitive(typeInfo.getFieldType());
    this._fieldName = (fieldName + "(" + this._fieldType.getName() + ")");
    this._immutable = typeInfo.isImmutable();
    if ((setMethod != null) && (setMethod.getParameterTypes()[0].isPrimitive())) {
      this._default = typeInfo.getDefaultValue();
    } else {
      this._default = null;
    }
    this._convertTo = typeInfo.getConvertorTo();
    this._convertFrom = typeInfo.getConvertorFrom();
    this._colHandler = typeInfo.getCollectionHandler();
  }
  
  public TypeConvertor getConvertFrom()
  {
    return this._convertFrom;
  }
  
  public TypeConvertor getConvertTo()
  {
    return this._convertTo;
  }
  
  public Object getValue(Object object) {
    Object value;
    try {

      if (this._handler != null) {
        value = this._handler.getValue(object);
      }
      else {

        if (this._field != null) {
          value = this._field.get(object);
        }
        else {

          if (this._enumMethod != null) {
            value = this._enumMethod.invoke(object, (Object[])null);
          }
          else {

            if (this._iterMethod != null) {
              value = new IteratorEnumeration((Iterator)this._iterMethod.invoke(object, (Object[])null));
            }
            else {

              if (this._getMethod != null) {
                if (this._getSequence != null) {
                  for (int i = 0; i < this._getSequence.length; i++) {
                    object = this._getSequence[i].invoke(object, (Object[])null);
                    if (object == null) {
                      break;
                    }
                  }
                }
                if ((object == null) || ((this._hasMethod != null) && (!((Boolean)this._hasMethod.invoke(object, (Object[])null)).booleanValue()))) {
                  value = null;
                } else {
                  value = this._getMethod.invoke(object, (Object[])null);
                }
              }
              else {
                value = null;
              }
            }
          }
        }
      }
    }
    catch (IllegalAccessException except) {
      throw new ExecIllegalStateException(Messages.format("mapping.schemaChangeNoAccess", toString()), except);
    }
    catch (InvocationTargetException except) {
      throw new ExecIllegalStateException(Messages.format("mapping.schemaChangeInvocation", toString(), except), except);
    }
    if ((this._colHandler != null) && (this._enumMethod == null) && (this._iterMethod == null) && (this._convertFrom == null)) {
      if (value == null) {
        return new CollectionHandlers.EmptyEnumerator();
      }
      return this._colHandler.elements(value);
    }
    if ((this._convertFrom == null) || (value == null)) {
      return value;
    }
    try {
      return this._convertFrom.convert(value);
    }
    catch (ClassCastException except)
    {
      String errorMessage = Messages.format("mapping.wrongConvertor", value.getClass().getName());
      
      throw new IllegalArgumentException(errorMessage, except);
    }
  }
  public void setValue(Object object, Object value) {
    if ((this._colHandler == null) || (this._addMethod != null)) {
      if ((value != null) && (this._convertTo != null)) {
        try {
          value = this._convertTo.convert(value);
        }
        catch (ClassCastException except) {
          String errorMessage = Messages.format("mapping.wrongConvertor", value.getClass().getName());
          
          throw new IllegalArgumentException(errorMessage, except);
        }
      }
      try {
        if (this._handler != null) {
          this._handler.setValue(object, value);
        }
        else if (this._field != null) {
          this._field.set(object, value == null ? this._default : value);
        }
        else {
          Method setter = selectWriteMethod(value);
          if (setter != null)
          {
            if (this._getSequence != null) {
              for (int i = 0; i < this._getSequence.length; i++)
              {
                Object last = object;
                object = this._getSequence[i].invoke(object, (Object[])null);
                if (object == null)
                {
                  if ((value == null) || (this._setSequence[i] == null)) {
                    break;
                  }
                  object = Types.newInstance(this._getSequence[i].getReturnType());
                  this._setSequence[i].invoke(last, new Object[] { object });
                }
              }
            }
            if (object != null) {
              if ((value == null) && (this._deleteMethod != null)) {
                this._deleteMethod.invoke(object, (Object[])null);
              } else {
                setter.invoke(object, new Object[] { value == null ? this._default : value });
              }
            }
          }
        }
      }
      catch (IllegalArgumentException except)
      {
        if (value == null)
        {
          String errorMessage = Messages.format("mapping.typeConversionNull", toString());
          throw new IllegalArgumentException(errorMessage);
        }
        String errorMessage = Messages.format("mapping.typeConversion", toString(), value.getClass().getName());
        
        throw new IllegalArgumentException(errorMessage, except);
      }
      catch (IllegalAccessException except)
      {
        String errorMessage = Messages.format("mapping.schemaChangeNoAccess", toString());
        
        throw new ExecIllegalStateException(errorMessage, except);
      }
      catch (InvocationTargetException except)
      {
        throw new MappingRuntimeException(except.getTargetException());
      }
    }
    else if (value != null)
    {
      try
      {
        if (this._handler != null)
        {
          Object collect = this._handler.getValue(object);
          collect = this._colHandler.add(collect, value);
          if (collect != null) {
            this._handler.setValue(object, collect);
          }
        }
        else if (this._field != null)
        {
          Object collect = this._field.get(object);
          if (collect == null)
          {
            Class type = this._field.getType();
            if (type.isArray())
            {
              Class componentType = type.getComponentType();
              Class valueType = value.getClass();
              if ((componentType.isPrimitive()) || ((!valueType.isArray()) && (valueType != componentType))) {
                try
                {
                  collect = Array.newInstance(componentType, 0);
                }
                catch (Exception e)
                {
                  String err = "Unable to instantiate an array of '" + componentType + "' : " + e;
                  
                  throw new ExecIllegalStateException(err, e);
                }
              }
            }
          }
          collect = this._colHandler.add(collect, value);
          if (collect != null) {
            this._field.set(object, collect);
          }
        }
        else if (this._getMethod != null)
        {
          if (this._getSequence != null) {
            for (int i = 0; i < this._getSequence.length; i++) {
              object = this._getSequence[i].invoke(object, (Object[])null);
            }
          }
          Object collect = this._getMethod.invoke(object, (Object[])null);
          
          boolean setCollection = false;
          if (collect == null) {
            Class type = this._getMethod.getReturnType();
            if (type.isArray()) {
              Class componentType = type.getComponentType();
              Class valueType = value.getClass();
              if ((componentType.isPrimitive()) || ((!valueType.isArray()) && (valueType != componentType))) {
                try {
                  collect = Array.newInstance(componentType, 0);
                }
                catch (Exception e) {
                  String err = "Unable to instantiate an array of '" + componentType + "' : " + e;
                  
                  throw new ExecIllegalStateException(err, e);
                }
              }
            }
            setCollection = true;
          }
          else {
            setCollection = collect.getClass().isArray();
          }
          Object tmp = this._colHandler.add(collect, value);
          if (tmp != null) {
            collect = tmp;
          }
          if ((setCollection) && (this._setMethod != null)) {
            this._setMethod.invoke(object, new Object[] { collect });
          }
        }
      }
      catch (IllegalAccessException except) {
        throw new IllegalStateException(Messages.format("mapping.schemaChangeNoAccess", toString()));
      }
      catch (InvocationTargetException except) {
        throw new MappingRuntimeException(except.getTargetException());
      }
    }
  }
  public void resetValue(Object object) {
    if (this._colHandler == null) {
      try {
        if (this._handler != null) {
          this._handler.resetValue(object);
        }
        else if (this._field != null) {
          this._field.set(object, this._default);
        }
        else if (this._setMethod != null) {
          if (this._getSequence != null) {
            for (int i = 0; i < this._getSequence.length; i++) {
              object = this._getSequence[i].invoke(object, (Object[])null);
              if (object == null) {
                break;
              }
            }
          }
          if (object != null) {
            if (this._deleteMethod != null) {
              this._deleteMethod.invoke(object, (Object[])null);
            } else {
              this._setMethod.invoke(object, new Object[] { this._default });
            }
          }
        }
      }
      catch (IllegalArgumentException except) {
        throw new IllegalArgumentException(Messages.format("mapping.typeConversionNull", toString()));
      } catch (IllegalAccessException except){
        throw new IllegalStateException(Messages.format("mapping.schemaChangeNoAccess", toString()));
      }
      catch (InvocationTargetException except) {
        throw new MappingRuntimeException(except.getTargetException());
      }
    } else {
      try {
        if (this._handler != null) {
          this._handler.resetValue(object);
        }
        else if (this._field != null) {
          Object collect = this._field.get(object);
          collect = this._colHandler.clear(collect);
          if (collect != null) {
            this._field.set(object, collect);
          }
        }
        else if (this._getMethod != null) {
          if (this._getSequence != null) {
            for (int i = 0; i < this._getSequence.length; i++) {
              object = this._getSequence[i].invoke(object, (Object[])null);
            }
          }
          Object collect = this._getMethod.invoke(object, (Object[])null);
          collect = this._colHandler.clear(collect);
          if ((collect != null) && (this._setMethod != null)) {
            this._setMethod.invoke(object, new Object[] { collect });
          }
        }
      }
      catch (IllegalAccessException except) {
        throw new IllegalStateException(Messages.format("mapping.schemaChangeNoAccess", toString()));
      }
      catch (InvocationTargetException except) {
        throw new MappingRuntimeException(except.getTargetException());
      }
    }
  }
  
  public Object newInstance(Object parent)
    throws IllegalStateException {
    return newInstance(parent, null);
  }
  
  public Object newInstance(Object parent, Object[] args)
    throws IllegalStateException {
    if ((this._fieldType.isInterface()) && (this._createMethod == null)) {
      return null;
    }
    if ((this._immutable) && ((args == null) || (args.length == 0))) {
      throw new IllegalStateException(Messages.format("mapping.classNotConstructable", this._fieldType));
    }
    if (this._handler != null) {
      if ((this._handler instanceof ExtendedFieldHandler)) {
        return ((ExtendedFieldHandler)this._handler).newInstance(parent, args);
      }
      return this._handler.newInstance(parent);
    }
    if ((this._createMethod != null) && (parent != null)) {
      try {
        return this._createMethod.invoke(parent, args);
      }
      catch (IllegalAccessException except) {
        throw new IllegalStateException(Messages.format("mapping.schemaChangeNoAccess", toString()));
      }
      catch (InvocationTargetException except) {
        throw new MappingRuntimeException(except.getTargetException());
      }
    }
    return Types.newInstance(this._fieldType, args);
  }
  
  public void setRequired(boolean required) {}
  public void setConvertFrom(TypeConvertor convertor)
  {
    this._convertFrom = convertor;
  }
  public void setConvertTo(TypeConvertor convertor)
  {
    this._convertTo = convertor;
  }
  public void setCreateMethod(Method method) throws MappingException {
    if (((method.getModifiers() & 0x1) == 0) || ((method.getModifiers() & 0x8) != 0)) {
      throw new MappingException("mapping.accessorNotAccessible", method, method.getDeclaringClass().getName());
    }
    if (method.getParameterTypes().length != 0) {
      throw new MappingException("mapping.createMethodNoParam", method, method.getDeclaringClass().getName());
    }
    this._createMethod = method;
  }
  public void setHasDeleteMethod(Method hasMethod, Method deleteMethod)
    throws MappingException {
    if (hasMethod != null) {
      if (((hasMethod.getModifiers() & 0x1) == 0) || ((hasMethod.getModifiers() & 0x8) != 0)) {
        throw new MappingException("mapping.accessorNotAccessible", hasMethod, hasMethod.getDeclaringClass().getName());
      }
      if (hasMethod.getParameterTypes().length != 0) {
        throw new MappingException("mapping.createMethodNoParam", hasMethod, hasMethod.getDeclaringClass().getName());
      }
      this._hasMethod = hasMethod;
    }
    if (deleteMethod != null) {
      if (((deleteMethod.getModifiers() & 0x1) == 0) || ((deleteMethod.getModifiers() & 0x8) != 0)) {
        throw new MappingException("mapping.accessorNotAccessible", deleteMethod, deleteMethod.getDeclaringClass().getName());
      }
      if (deleteMethod.getParameterTypes().length != 0) {
        throw new MappingException("mapping.createMethodNoParam", deleteMethod, deleteMethod.getDeclaringClass().getName());
      }
      this._deleteMethod = deleteMethod;
    }
  }
  public void setReadMethod(Method method) throws MappingException {
    if (((method.getModifiers() & 0x1) == 0) || ((method.getModifiers() & 0x8) != 0)) {
      throw new MappingException("mapping.accessorNotAccessible", method, method.getDeclaringClass().getName());
    }
    if (method.getParameterTypes().length != 0) {
      throw new MappingException("mapping.readMethodHasParam", method, method.getDeclaringClass().getName());
    }
    this._getMethod = method;
  }
  public void setWriteMethod(Method method) throws MappingException {
    if (((method.getModifiers() & 0x1) == 0) || ((method.getModifiers() & 0x8) != 0)) {
      throw new MappingException("mapping.accessorNotAccessible", method, method.getDeclaringClass().getName());
    }
    if (method.getParameterTypes().length != 1) {
      throw new MappingException("mapping.writeMethodNoParam", method, method.getDeclaringClass().getName());
    }
    this._setMethod = method;
  }
  public void setAddMethod(Method method)
    throws MappingException {
    if (((method.getModifiers() & 0x1) == 0) || ((method.getModifiers() & 0x8) != 0)) {
      throw new MappingException("mapping.accessorNotAccessible", method, method.getDeclaringClass().getName());
    }
    if (method.getParameterTypes().length != 1) {
      throw new MappingException("mapping.writeMethodNoParam", method, method.getDeclaringClass().getName());
    }
    this._addMethod = method;
    if (this._addMethod == this._setMethod) {
      this._setMethod = null;
    }
  }
  public void setEnumMethod(Method method)
    throws MappingException {
    if (((method.getModifiers() & 0x1) == 0) || ((method.getModifiers() & 0x8) != 0)) {
      throw new MappingException("mapping.accessorNotAccessible", method, method.getDeclaringClass().getName());
    }
    if (method.getParameterTypes().length != 0) {
      throw new MappingException("mapping.readMethodHasParam", method, method.getDeclaringClass().getName());
    }
    this._enumMethod = method;
  }
  public void setIterMethod(Method method)
    throws MappingException {
    if (((method.getModifiers() & 0x1) == 0) || ((method.getModifiers() & 0x8) != 0)) {
      throw new MappingException("mapping.accessorNotAccessible", method, method.getDeclaringClass().getName());
    }
    if (method.getParameterTypes().length != 0) {
      throw new MappingException("mapping.readMethodHasParam", method, method.getDeclaringClass().getName());
    }
    this._iterMethod = method;
  }
  private Method selectWriteMethod(Object value) {
    if (this._setMethod != null) {
      if (this._addMethod == null) {
        return this._setMethod;
      }
      if (value == null) {
        if (this._default != null) {
          value = this._default;
        } else {
          return this._setMethod;
        }
      }
      Class paramType = this._setMethod.getParameterTypes()[0];
      if (paramType.isAssignableFrom(value.getClass())) {
        return this._setMethod;
      }
    }
    return this._addMethod;
  }
  public boolean isCollection()
  {
    return this._colHandler != null;
  }
  public String toString()
  {
    return this._fieldName;
  }
  public void setFieldDescriptor(FieldDescriptor fieldDesc) {
    super.setFieldDescriptor(fieldDesc);
    if ((this._handler != null) && 
      ((this._handler instanceof GeneralizedFieldHandler))) {
      ((GeneralizedFieldHandler)this._handler).setFieldDescriptor(fieldDesc);
    }
  }
}
