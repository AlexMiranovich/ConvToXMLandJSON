
package org.exolab.mapping.loader;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.exec.core.util.Messages;
import org.exec.xml.InternalContext;
import org.exolab.mapping.ClassDescriptor;
import org.exolab.mapping.CollectionHandler;
import org.exolab.mapping.ConfigurableFieldHandler;
import org.exolab.mapping.ExtendedFieldHandler;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.FieldHandler;
import org.exolab.mapping.GeneralizedFieldHandler;
import org.exolab.mapping.MapItem;
import org.exolab.mapping.MappingException;
import org.exolab.mapping.ValidityException;
import org.exolab.mapping.handlers.EnumFieldHandler;
import org.exolab.mapping.handlers.TransientFieldHandler;
import org.exolab.mapping.loader.*;
import org.exolab.mapping.xml.ClassChoice;
import org.exolab.mapping.xml.ClassMapping;
import org.exolab.mapping.xml.FieldHandlerDef;
import org.exolab.mapping.xml.FieldMapping;
import org.exolab.mapping.xml.MappingRoot;
import org.exolab.mapping.xml.Param;
import org.exolab.mapping.xml.types.FieldMappingCollectionType;

public abstract class AbstractMappingLoader extends AbstractMappingLoader2 {
  private static final String ADD_METHOD_PREFIX = "add";
  private static final String ENUM_METHOD_PREFIX = "enum";
  private static final String ITER_METHOD_PREFIX = "iterate";
  private static final String GET_METHOD_PREFIX = "get";
  private static final String IS_METHOD_PREFIX = "is";
  private static final String SET_METHOD_PREFIX = "set";
  private static final String CREATE_METHOD_PREFIX = "create";
  private static final String HAS_METHOD_PREFIX = "has";
  private static final String DELETE_METHOD_PREFIX = "delete";
  protected static final Class[] EMPTY_ARGS = new Class[0];
  protected static final Class[] STRING_ARG = { String.class };
  protected static final String VALUE_OF = "valueOf";
  private InternalContext _internalContext;
  private final Map _fieldHandlers = new HashMap();
  protected AbstractMappingLoader(ClassLoader loader)
  {
    super(loader);
  }
  public final String getSourceType()
  {
    return "ExecXmlMapping";
  }
  public abstract void loadMapping(MappingRoot paramMappingRoot, Object paramObject)
    throws MappingException;
  protected void createFieldHandlers(MappingRoot mapping)
    throws MappingException {
    Enumeration enumeration = mapping.enumerateFieldHandlerDef();
    while (enumeration.hasMoreElements()) {
      FieldHandlerDef def = (FieldHandlerDef)enumeration.nextElement();
      String name = def.getName();
      if (this._fieldHandlers.containsKey(name)) {
        throw new MappingException(Messages.format("mapping.dupFieldHandler", name));
      }
      Class clazz = resolveType(def.getClazz());
      FieldHandler fieldHandler = null;
      try {
        if (!FieldHandler.class.isAssignableFrom(clazz)) {
          throw new MappingException(Messages.format("mapping.classNotFieldHandler", name, def.getClazz()));
        }
        fieldHandler = (FieldHandler)clazz.newInstance();
        this._fieldHandlers.put(name, fieldHandler);
      } catch (InstantiationException e) {
        throw new MappingException(e);
      } catch (IllegalAccessException e) {
        throw new MappingException(e);
      }
      configureFieldHandler(def, fieldHandler);
    }
  }
  private void configureFieldHandler(FieldHandlerDef def, FieldHandler fieldHandler)
    throws MappingException {
    Properties params = new Properties();
    Enumeration enumerateParam = def.enumerateParam();
    while (enumerateParam.hasMoreElements()) {
      Param par = (Param)enumerateParam.nextElement();
      params.put(par.getName(), par.getValue());
    }
    if (params.size() > 0) {
      if (!ConfigurableFieldHandler.class.isAssignableFrom(fieldHandler.getClass())) {
        throw new MappingException(Messages.format("mapping.classNotConfigurableFieldHandler", def.getName(), def.getClazz()));
      }try {
          ((ConfigurableFieldHandler)fieldHandler).setConfiguration(params);
      }
      catch (ValidityException e)
      {
        throw new MappingException(Messages.format("mapping.invalidFieldHandlerConfig", def.getName(), e.getMessage()), e);
      }
    }
  }
  protected final void createClassDescriptors(MappingRoot mapping)
    throws MappingException {
    Enumeration enumeration = mapping.enumerateClassMapping();
    List retryList = new ArrayList();
    while (enumeration.hasMoreElements()) {
      ClassMapping clsMap = (ClassMapping)enumeration.nextElement();
      try {
        ClassDescriptor clsDesc = createClassDescriptor(clsMap);
        if (clsDesc != null) {
          addDescriptor(clsDesc);
        }
      } catch (MappingException mx) {
        retryList.add(clsMap);
      }
    }
    for (Iterator i = retryList.iterator(); i.hasNext();) {
      ClassMapping clsMap = (ClassMapping)i.next();
      ClassDescriptor clsDesc = createClassDescriptor(clsMap);
      if (clsDesc != null) {
        addDescriptor(clsDesc);
      }
    }
    for (Iterator i = descriptorIterator(); i.hasNext();) {
      resolveRelations((ClassDescriptor)i.next());
    }
  }
  protected abstract ClassDescriptor createClassDescriptor(ClassMapping paramClassMapping)
    throws MappingException;
  protected final ClassDescriptor getExtended(ClassMapping clsMap, Class javaClass)
    throws MappingException {
    if (clsMap.getExtends() == null) {
      return null;
    }
    ClassMapping mapping = (ClassMapping)clsMap.getExtends();
    Class type = resolveType(mapping.getName());
    ClassDescriptor result = getDescriptor(type.getName());
    if (result == null) {
      throw new MappingException("mapping.extendsMissing", mapping, javaClass.getName());
    }
    if (!result.getJavaClass().isAssignableFrom(javaClass)) {
      throw new MappingException("mapping.classDoesNotExtend", javaClass.getName(), result.getJavaClass().getName());
    }
    return result;
  }
  protected final ClassDescriptor getDepended(ClassMapping clsMap, Class javaClass)
    throws MappingException {
    if (clsMap.getDepends() == null) {
      return null;
    }
    ClassMapping mapping = (ClassMapping)clsMap.getDepends();
    Class type = resolveType(mapping.getName());
    ClassDescriptor result = getDescriptor(type.getName());
    if (result == null) {
      throw new MappingException("Depends not found: " + mapping + " " + javaClass.getName());
    }
    return result;
  }
  protected final void checkFieldNameDuplicates(FieldDescriptor[] fields, Class cls)
    throws MappingException {
    for (int i = 0; i < fields.length - 1; i++)
    {
      String fieldName = fields[i].getFieldName();
      for (int j = i + 1; j < fields.length; j++) {
        if (fieldName.equals(fields[j].getFieldName())) {
          throw new MappingException("The field " + fieldName + " appears twice in the descriptor for " + cls.getName());
        }
      }
    }
  }
  protected abstract void resolveRelations(ClassDescriptor paramClassDescriptor);
  protected final Class resolveType(String typeName)
    throws MappingException {
    try {
      return Types.typeFromName(getClassLoader(), typeName);
    } catch (ClassNotFoundException ex) {
      throw new MappingException("mapping.classNotFound", typeName);
    }
  }
  protected final FieldDescriptorImpl[] createFieldDescriptors(ClassMapping clsMap, Class javaClass)
    throws MappingException {
    FieldMapping[] fldMap = null;
    if (clsMap.getClassChoice() != null) {
      fldMap = clsMap.getClassChoice().getFieldMapping();
    }
    if ((fldMap == null) || (fldMap.length == 0)) {
      return new FieldDescriptorImpl[0];
    }
    FieldDescriptorImpl[] fields = new FieldDescriptorImpl[fldMap.length];
    for (int i = 0; i < fldMap.length; i++) {
      fields[i] = createFieldDesc(javaClass, fldMap[i]);
      fields[i].setIdentity(fldMap[i].getIdentity());
    }
    return fields;
  }
  protected final ClassMapping getOrigin(ClassMapping clsMap) {
    ClassMapping result = clsMap;
    while (result.getExtends() != null) {
      result = (ClassMapping)result.getExtends();
    }
    return result;
  }
  protected final FieldDescriptor[] divideFieldDescriptors(FieldDescriptor[] fields, String[] ids, FieldDescriptor[] identities) {
    List fieldList = new ArrayList(fields.length);
    for (int i = 0; i < fields.length; i++) {
      FieldDescriptor field = fields[i];
      int index = getIdColumnIndex(field, ids);
      if (index == -1) {
        fieldList.add(field);
      }
      else {
        if ((field instanceof FieldDescriptorImpl)) {
          ((FieldDescriptorImpl)field).setRequired(true);
        }
        if ((field.getHandler() instanceof FieldHandlerImpl)) {
          ((FieldHandlerImpl)field.getHandler()).setRequired(true);
        }
        identities[index] = field;
      }
    }
    FieldDescriptor[] result = new FieldDescriptor[fieldList.size()];
    return (FieldDescriptor[])fieldList.toArray(result);
  }
  protected int getIdColumnIndex(FieldDescriptor field, String[] ids) {
    for (int i = 0; i < ids.length; i++) {
      if (field.getFieldName().equals(ids[i])) {
        return i;
      }
    }
    return -1;
  }
  protected FieldDescriptorImpl createFieldDesc(Class javaClass, FieldMapping fieldMap)
    throws MappingException {
    String fieldName = fieldMap.getName();
    Class fieldType = null;
    if (fieldMap.getType() != null) {
      fieldType = resolveType(fieldMap.getType());
    }
    CollectionHandler colHandler = null;
    if (fieldMap.getCollection() != null) {
      String colTypeName = fieldMap.getCollection().toString();
      Class colType = CollectionHandlers.getCollectionType(colTypeName);
      colHandler = CollectionHandlers.getHandler(colType);
    }
    TypeInfo typeInfo = getTypeInfo(fieldType, colHandler, fieldMap);
    ExtendedFieldHandler exfHandler = null;
    FieldHandler handler = null;
    if (fieldMap.getHandler() != null) {
      handler = getFieldHandler(fieldMap);
      if ((handler instanceof ExtendedFieldHandler)) {
        exfHandler = (ExtendedFieldHandler)handler;
      }
      colHandler = typeInfo.getCollectionHandler();
      typeInfo.setCollectionHandler(null);
      handler = new FieldHandlerImpl(handler, typeInfo);
      typeInfo.setCollectionHandler(colHandler);
    }
    boolean generalized = exfHandler instanceof GeneralizedFieldHandler;
    if (generalized) {
      fieldType = ((GeneralizedFieldHandler)exfHandler).getFieldType();
    }
    if ((generalized) || (handler == null))
    {
      FieldHandler custom = handler;
      TypeInfoReference typeInfoRef = new TypeInfoReference();
      typeInfoRef.typeInfo = typeInfo;
      handler = createFieldHandler(javaClass, fieldType, fieldMap, typeInfoRef);
      if (custom != null) {
        ((GeneralizedFieldHandler)exfHandler).setFieldHandler(handler);
        handler = custom;
      } else {
        boolean isTypeSafeEnum = false;
        if ((fieldType != null) && (!isPrimitive(fieldType)) && 
          (!hasPublicDefaultConstructor(fieldType)))
        {
          Method method = getStaticValueOfMethod(fieldType);
          if (method != null)
          {
            handler = new EnumFieldHandler(fieldType, handler, method);
            typeInfo.setImmutable(true);
            isTypeSafeEnum = true;
          }
        }
        if (!isTypeSafeEnum) {
          typeInfo = typeInfoRef.typeInfo;
        }
      }
    }
    FieldDescriptorImpl fieldDesc = new FieldDescriptorImpl(fieldName, typeInfo, handler, fieldMap.getTransient());
    
    fieldDesc.setRequired(fieldMap.getRequired());
    if (exfHandler != null) {
      exfHandler.setFieldDescriptor(fieldDesc);
    }
    return fieldDesc;
  }
  private FieldHandler getFieldHandler(FieldMapping fieldMap)
    throws MappingException {
    FieldHandler handler = (FieldHandler)this._fieldHandlers.get(fieldMap.getHandler());
    if (handler != null) {
      return handler;
    }
    Class handlerClass = null;
    handlerClass = resolveType(fieldMap.getHandler());
    if (!FieldHandler.class.isAssignableFrom(handlerClass)) {
      String err = "The class '" + fieldMap.getHandler() + "' must implement " + FieldHandler.class.getName();
      throw new MappingException(err);
    }try {
      Constructor constructor = handlerClass.getConstructor(new Class[0]);
      return (FieldHandler)constructor.newInstance(new Object[0]);
    } catch (Exception ex) {
      String err = "The class '" + handlerClass.getName() + "' must have a default public constructor.";
      throw new MappingException(err);
    }
  }
  private boolean hasPublicDefaultConstructor(Class type) {
    try {
      Constructor cons = type.getConstructor(EMPTY_ARGS);
      return Modifier.isPublic(cons.getModifiers());
    }
    catch (NoSuchMethodException ex) {}
    return false;
  }
  private Method getStaticValueOfMethod(Class type) {
    try {
      Method method = type.getMethod("valueOf", STRING_ARG);
      Class returnType = method.getReturnType();
      if (returnType == null) {
        return null;
      }
      if (!type.isAssignableFrom(returnType)) {
        return null;
      }
      if (!Modifier.isStatic(method.getModifiers())) {
        return null;
      }
      return method;
    }
    catch (NoSuchMethodException ex) {}
    return null;
  }
  protected final FieldHandler createFieldHandler(Class javaClass, Class fldType, FieldMapping fldMap, TypeInfoReference typeInfoRef)
    throws MappingException {
    if (fldMap.getTransient()) {
      return new TransientFieldHandler();
    }
    Class colType = null;
    CollectionHandler colHandler = null;
    boolean colRequireGetSet = true;
    String fieldName = fldMap.getName();
    if (fldMap.getCollection() != null) {
      String colTypeName = fldMap.getCollection().toString();
      colType = CollectionHandlers.getCollectionType(colTypeName);
      colHandler = CollectionHandlers.getHandler(colType);
      colRequireGetSet = CollectionHandlers.isGetSetCollection(colType);
      if (colType == Object[].class) {
        if (fldType == null) {
          String msg = "'type' is a required attribute for field that are array collections: " + fieldName;
          
          throw new MappingException(msg);
        }
        Object obj = Array.newInstance(fldType, 0);
        colType = obj.getClass();
      }
    }
    FieldHandlerImpl handler = null;
    if (fldMap.getDirect()) {
      Field field = findField(javaClass, fieldName, colType == null ? fldType : colType);
      if (field == null) {
        throw new MappingException("mapping.fieldNotAccessible", fieldName, javaClass.getName());
      }
      if (fldType == null) {
        fldType = field.getType();
      }
      typeInfoRef.typeInfo = getTypeInfo(fldType, colHandler, fldMap);
      handler = new FieldHandlerImpl(field, typeInfoRef.typeInfo);
    }
    else if ((fldMap.getGetMethod() == null) && (fldMap.getSetMethod() == null)) {
      if (fieldName == null) {
        throw new MappingException("mapping.missingFieldName", javaClass.getName());
      }
      List getSequence = new ArrayList();
      List setSequence = new ArrayList();
      Method getMethod = null;
      Method setMethod = null;
      try {
        for (;;) {
          int point = fieldName.indexOf('.');
          if (point < 0) {
            break;
          }
          String parentField = fieldName.substring(0, point);
          String methodName = "get" + capitalize(parentField);
          Method method = javaClass.getMethod(methodName, (Class[])null);
          if (isAbstractOrStatic(method)) {
            throw new MappingException("mapping.accessorNotAccessible", methodName, javaClass.getName());
          }
          getSequence.add(method);
          Class nextClass = method.getReturnType();
          try {
            methodName = "set" + capitalize(parentField);
            Class[] types = { nextClass };
            method = javaClass.getMethod(methodName, types);
            if (isAbstractOrStatic(method)) {
              method = null;
            }
          }
          catch (Exception ex) {
            method = null;
          }
          setSequence.add(method);
          javaClass = nextClass;
          fieldName = fieldName.substring(point + 1);
        }
        String methodName = "get" + capitalize(fieldName);
        Class returnType = colType == null ? fldType : colType;
        getMethod = findAccessor(javaClass, methodName, returnType, true);
        if ((getMethod == null) && (
          (fldType == Boolean.class) || (fldType == Boolean.TYPE))) {
          methodName = "is" + capitalize(fieldName);
          getMethod = findAccessor(javaClass, methodName, returnType, true);
        }
      } catch (MappingException ex) {
        throw ex;
      }
      catch (Exception ex) {}
      if (getMethod == null) {
        String getAccessor = "get" + capitalize(fieldName);
        String isAccessor = "is" + capitalize(fieldName);
        throw new MappingException("mapping.accessorNotFound", getAccessor + "/" + isAccessor, colType == null ? fldType : colType, javaClass.getName());
      }
      if ((fldType == null) && (colType == null)) {
        fldType = getMethod.getReturnType();
      }
      String methodName = "set" + capitalize(fieldName);
      setMethod = findAccessor(javaClass, methodName, colType == null ? fldType : colType, false);
      if ((setMethod == null) && (colType != null) && (colRequireGetSet)) {
        throw new MappingException("mapping.accessorNotFound", methodName, colType == null ? fldType : colType, javaClass.getName());
      }
      typeInfoRef.typeInfo = getTypeInfo(fldType, colHandler, fldMap);
      fieldName = fldMap.getName();
      if (fieldName == null) {
        if (getMethod == null) {
          fieldName = setMethod.getName();
        } else {
          fieldName = getMethod.getName();
        }
      }
      Method[] getArray = null;
      Method[] setArray = null;
      if (getSequence.size() > 0) {
        getArray = new Method[getSequence.size()];
        getArray = (Method[])getSequence.toArray(getArray);
        setArray = new Method[setSequence.size()];
        setArray = (Method[])setSequence.toArray(setArray);
      }
      handler = new FieldHandlerImpl(fieldName, getArray, setArray, getMethod, setMethod, typeInfoRef.typeInfo);
      if ((setMethod != null) && 
        (setMethod.getName().startsWith("add"))) {
        handler.setAddMethod(setMethod);
      }
    }
    else {
      Method getMethod = null;
      Method setMethod = null;
      if (fldMap.getGetMethod() != null)
      {
        Class rtype = fldType;
        if (colType != null)
        {
          String methodName = fldMap.getGetMethod();
          if (methodName.startsWith("enum")) {
            rtype = Enumeration.class;
          } else if (methodName.startsWith("iterate")) {
            rtype = Iterator.class;
          } else {
            rtype = colType;
          }
        }
        getMethod = findAccessor(javaClass, fldMap.getGetMethod(), rtype, true);
        if (getMethod == null) {
          throw new MappingException("mapping.accessorNotFound", fldMap.getGetMethod(), rtype, javaClass.getName());
        }
        if ((fldType == null) && (colType == null)) {
          fldType = getMethod.getReturnType();
        }
      }
      if (fldMap.getSetMethod() != null) {
        String methodName = fldMap.getSetMethod();
        Class type = fldType;
        if ((colType != null) && 
          (!methodName.startsWith("add"))) {
          type = colType;
        }
        if (methodName.startsWith("%")) {
          int index = 0;
          String temp = methodName.substring(1);
          try {
            index = Integer.parseInt(temp);
          } catch (NumberFormatException ex) {
            throw new MappingException("mapping.invalidParameterIndex", temp);
          }
          if ((index < 1) || (index > 9)) {
            throw new MappingException("mapping.invalidParameterIndex", temp);
          }
        } else {
          setMethod = findAccessor(javaClass, methodName, type, false);
          if (setMethod == null) {
            throw new MappingException("mapping.accessorNotFound", methodName, type, javaClass.getName());
          }
          if (fldType == null) {
            fldType = setMethod.getParameterTypes()[0];
          }
        }
      }
      typeInfoRef.typeInfo = getTypeInfo(fldType, colHandler, fldMap);
      fieldName = fldMap.getName();
      if (fieldName == null) {
        if (getMethod == null) {
          fieldName = setMethod.getName();
        } else {
          fieldName = getMethod.getName();
        }
      }
      handler = new FieldHandlerImpl(fieldName, null, null, getMethod, setMethod, typeInfoRef.typeInfo);
      if ((setMethod != null) && 
        (setMethod.getName().startsWith("add"))) {
        handler.setAddMethod(setMethod);
      }
    }
    String methodName = fldMap.getCreateMethod();
    if (methodName != null) {
      try {
        Method method = javaClass.getMethod(methodName, (Class[])null);
        handler.setCreateMethod(method);
      } catch (Exception ex) {
        throw new MappingException("mapping.createMethodNotFound", methodName, javaClass.getName());
      }
    } else if ((fieldName != null) && (!Types.isSimpleType(fldType))) {
      try {
        methodName = "create" + capitalize(fieldName);
        Method method = javaClass.getMethod(methodName, (Class[])null);
        handler.setCreateMethod(method);
      }
      catch (Exception ex) {}
    }
    if (fieldName != null) {
      try {
        methodName = fldMap.getHasMethod();
        if (methodName == null) {
          methodName = "has" + capitalize(fieldName);
        }
        Method hasMethod = javaClass.getMethod(methodName, (Class[])null);
        if ((hasMethod.getModifiers() & 0x8) != 0) {
          hasMethod = null;
        }
        Method deleteMethod = null;
        try
        {
          methodName = "delete" + capitalize(fieldName);
          deleteMethod = javaClass.getMethod(methodName, (Class[])null);
          if ((deleteMethod.getModifiers() & 0x8) != 0) {
            deleteMethod = null;
          }
        }
        catch (Exception ex) {}
        handler.setHasDeleteMethod(hasMethod, deleteMethod);
      }
      catch (Exception ex) {}
    }
    return handler;
  }
  private static boolean isAbstract(Class cls)
  {
    return (cls.getModifiers() & 0x400) != 0;
  }
  private static boolean isAbstractOrStatic(Method method) {
    return ((method.getModifiers() & 0x400) != 0) || ((method.getModifiers() & 0x8) != 0);
  }
  protected TypeInfo getTypeInfo(Class fieldType, CollectionHandler colHandler, FieldMapping fieldMap)
    throws MappingException {
    return new TypeInfo(Types.typeFromPrimitive(fieldType), null, null, fieldMap.getRequired(), null, colHandler, false);
  }
  private final Field findField(Class javaClass, String fieldName, Class fieldType)
    throws MappingException {
    try {
      Field field = javaClass.getField(fieldName);
      if ((field.getModifiers() != 1) && (field.getModifiers() != 65)) {
        throw new MappingException("mapping.fieldNotAccessible", fieldName, javaClass.getName());
      }
      if (fieldType == null) {
        fieldType = Types.typeFromPrimitive(field.getType());
      }
      else
      {
        Class ft1 = Types.typeFromPrimitive(fieldType);
        Class ft2 = Types.typeFromPrimitive(field.getType());
        if ((ft1 != ft2) && (fieldType != Serializable.class)) {
          throw new MappingException("mapping.fieldTypeMismatch", field, fieldType.getName());
        }
      }
      return field;
    } catch (NoSuchFieldException ex) {
      return null;
    }
    catch (SecurityException ex) {}
    return null;
  }
  public static final Method findAccessor(Class javaClass, String methodName, Class fieldType, boolean getMethod) throws MappingException {
    try {
      Method method = null;
      if (getMethod) {
        method = javaClass.getMethod(methodName, new Class[0]);
        if (javaClass == MapItem.class) {
          if (methodName.equals("getKey")) {
            return method;
          }
          if (methodName.equals("getValue")) {
            return method;
          }
        }
        if (fieldType == null) {
          fieldType = Types.typeFromPrimitive(method.getReturnType());
        } else {
          fieldType = Types.typeFromPrimitive(fieldType);
          Class returnType = Types.typeFromPrimitive(method.getReturnType());
          if ((fieldType.isInterface()) || ((fieldType.getModifiers() & 0x400) != 0) || (fieldType == Serializable.class))
          {
            if (!fieldType.isAssignableFrom(returnType)) {
              throw new MappingException("mapping.accessorReturnTypeMismatch", method, fieldType.getName());
            }
          }
          else if (!returnType.isAssignableFrom(fieldType)) {
            throw new MappingException("mapping.accessorReturnTypeMismatch", method, fieldType.getName());
          }
        }
      } else {
        Class fieldTypePrimitive = null;
        if (fieldType != null) {
          fieldTypePrimitive = Types.typeFromPrimitive(fieldType);
          try {
            method = javaClass.getMethod(methodName, new Class[] { fieldType });
          }
          catch (Exception ex) {
            try {
              method = javaClass.getMethod(methodName, new Class[] { fieldTypePrimitive });
            }
            catch (Exception ex2) {}
          }
        }
        if (method == null) {
          Method[] methods = javaClass.getMethods();
          for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals(methodName)) {
              Class[] paramTypes = methods[i].getParameterTypes();
              if (paramTypes.length == 1) {
                Class paramType = Types.typeFromPrimitive(paramTypes[0]);
                if (fieldType == null) {
                  method = methods[i];
                  break;
                }if (paramType.isAssignableFrom(fieldTypePrimitive)) {
                  method = methods[i];
                  break;
                }
                if (((fieldType.isInterface()) || (isAbstract(fieldType))) && 
                  (fieldTypePrimitive.isAssignableFrom(paramType)))
                {
                  method = methods[i];
                  break;
                }
              }
            }
          }
          if (method == null) {
            return null;
          }
        }
      }
      if ((method.getModifiers() & 0x8) != 0) {
        throw new MappingException("mapping.accessorNotAccessible", methodName, javaClass.getName());
      }
      return method;
    } catch (MappingException ex) {
      throw ex;
    }
    catch (Exception ex) {}
    return null;
  }
  private static final String capitalize(String name) {
    char first = name.charAt(0);
    if (Character.isUpperCase(first)) {
      return name;
    }
    return Character.toUpperCase(first) + name.substring(1);
  }
  public static final String[] getIdentityColumnNames(String[] ids, ClassMapping clsMap) {
    String[] idNames = ids;
    if ((ids == null) || (ids.length == 0))
    {
      ClassChoice classChoice = clsMap.getClassChoice();
      if (classChoice == null) {
        classChoice = new ClassChoice();
      }
      FieldMapping[] fieldMappings = classChoice.getFieldMapping();
      
      List idNamesList = new ArrayList();
      for (int i = 0; i < fieldMappings.length; i++) {
        if (fieldMappings[i].getIdentity() == true) {
          idNamesList.add(fieldMappings[i].getName());
        }
      }
      if (idNamesList.size() > 0)
      {
        idNames = new String[idNamesList.size()];
        idNames = (String[])idNamesList.toArray(idNames);
      }
    }
    return idNames;
  }
  protected static final boolean isPrimitive(Class type) {
    if (type.isPrimitive()) {
      return true;
    }
    if ((type == Boolean.class) || (type == Character.class)) {
      return true;
    }
    return type.getSuperclass() == Number.class;
  }
  
  public class TypeInfoReference {
    public TypeInfo typeInfo = null;
    public TypeInfoReference() {}
  }
  public void setInternalContext(InternalContext internalContext)
  {
    this._internalContext = internalContext;
  }
  public InternalContext getInternalContext()
  {
    return this._internalContext;
  }
}
