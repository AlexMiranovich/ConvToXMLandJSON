package org.exolab.mapping.loader;

import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.Vector;
import org.exec.core.util.Messages;
import org.exolab.types.Date;
import org.exolab.types.Duration;
import org.exolab.types.Time;

public class Types {
  public static Class typeFromName(ClassLoader loader, String typeName) throws ClassNotFoundException {
    for (int i = 0; i < _typeInfos.length; i++) {
      if (typeName.equals(_typeInfos[i]._shortName)) {
        return _typeInfos[i]._primitive != null ? _typeInfos[i]._primitive : _typeInfos[i]._javaType;
      }
    }
    if (loader != null) {
      Class aClass = Class.forName(typeName, false, loader);
      
      return aClass;
    }
    return Class.forName(typeName);
  }
  public static Object getDefault(Class type) {
    for (int i = 0; i < _typeInfos.length; i++) {
      if ((_typeInfos[i]._primitive == type) || (_typeInfos[i]._javaType == type)) {
        return _typeInfos[i]._defaultValue;
      }
    }
    return null;
  }
  
  public static Class typeFromPrimitive(Class type) {
    if ((type != null) && (type.isArray()) && (!type.getComponentType().isPrimitive())) {
      return typeFromPrimitive(type.getComponentType());
    }
    for (int i = 0; i < _typeInfos.length; i++) {
      if (_typeInfos[i]._primitive == type) {
        return _typeInfos[i]._javaType;
      }
    }
    return type;
  }
  public static boolean isSimpleType(Class type) {
    for (int i = 0; i < _typeInfos.length; i++) {
      if ((_typeInfos[i]._javaType == type) || (_typeInfos[i]._primitive == type)) {
        return true;
      }
    }
    return false;
  }
  public static boolean isPrimitiveType(Class type) {
    for (int i = 0; i < _typeInfos.length; i++) {
      if ((_typeInfos[i]._primitive == type) || ((_typeInfos[i]._javaType == type) && (_typeInfos[i]._primitive != null))) {
        return true;
      }
    }
    return false;
  }
  private static final Vector ENUMS = new Vector();
  public static void addEnumType(Class type)
  {
    ENUMS.add(type);
  }
  public static boolean isEnumType(Class type)
  {
    return ENUMS.contains(type);
  }
  private static final Vector CONVERTIBLE = new Vector();
  public static void addConvertibleType(Class type)
  {
    CONVERTIBLE.add(type);
  }
  public static boolean isConvertibleType(Class type)
  {
    return CONVERTIBLE.contains(type);
  }
  public static Object newInstance(Class type)
    throws IllegalStateException {
    try {
      return type.newInstance();
    }
    catch (IllegalAccessException except) {
      throw new IllegalStateException(Messages.format("mapping.schemaNotConstructable", type.getName(), except.getMessage()));
    }
    catch (InstantiationException except) {
      throw new IllegalStateException(Messages.format("mapping.schemaNotConstructable", type.getName(), except.getMessage()));
    }
  }
  public static Object newInstance(Class type, Object[] args) throws IllegalStateException {
    if ((args == null) || (args.length == 0)) {
      return newInstance(type);
    }
    try {
      Constructor cons = findConstructor(type, args);
      return cons.newInstance(args);
    }
    catch (NoSuchMethodException except) {
      throw new IllegalStateException(Messages.format("mapping.constructorNotFound", type.getName(), except.getMessage()));
    }
    catch (InvocationTargetException except) {
      throw new IllegalStateException(Messages.format("mapping.schemaNotConstructable", type.getName(), except.getMessage()));
    }
    catch (IllegalAccessException except) {
      throw new IllegalStateException(Messages.format("mapping.schemaNotConstructable", type.getName(), except.getMessage()));
    }
    catch (InstantiationException except) {
      throw new IllegalStateException(Messages.format("mapping.schemaNotConstructable", type.getName(), except.getMessage()));
    }
  }
  public static boolean isConstructable(Class type)
  {
    return isConstructable(type, false);
  }
  public static boolean isConstructable(Class type, boolean allowAbstractOrInterface) {
    try {
      if ((type.getModifiers() & 0x1) == 0) {
        return false;
      }
      if ((!allowAbstractOrInterface) && ((type.getModifiers() & 0x600) != 0)) {
        return false;
      }
      if ((type.getConstructor(new Class[0]).getModifiers() & 0x1) != 0) {
        return true;
      }
    }
    catch (NoSuchMethodException except) {}catch (SecurityException except) {}
    return false;
  }
  public static boolean isSerializable(Class type)
  {
    return Serializable.class.isAssignableFrom(type);
  }
  public static boolean isImmutable(Class type) {
    for (int i = 0; i < _typeInfos.length; i++) {
      if ((_typeInfos[i]._javaType == type) || (_typeInfos[i]._primitive == type)) {
        return _typeInfos[i]._immutable;
      }
    }
    return false;
  }
  public static boolean isCloneable(Class type)
  {
    return Cloneable.class.isAssignableFrom(type);
  }
  private static Constructor findConstructor(Class type, Object[] args)
    throws NoSuchMethodException {
    Constructor[] constructors = type.getConstructors();
    Constructor cons = null;
    int rank = 0;
    for (int c = 0; c < constructors.length; c++) {
      Class[] paramTypes = constructors[c].getParameterTypes();
      if (paramTypes.length == args.length) {
        int tmpRank = 0;
        boolean matches = true;
        for (int p = 0; p < paramTypes.length; p++) {
          if (args[p] == null) {
            if (paramTypes[p].isPrimitive()) {
              matches = false;
              break;
            }
          }
          else if (paramTypes[p] == args[p].getClass()) {
            tmpRank++;
          } else if (!paramTypes[p].isAssignableFrom(args[p].getClass())) {
            if (paramTypes[p].isPrimitive())
            {
              Class pType = typeFromPrimitive(paramTypes[p]);
              if (pType.isAssignableFrom(args[p].getClass())) {}
            }
            else
            {
              matches = false;
              break;
            }
          }
        }
        if (matches) {
          if (tmpRank == paramTypes.length) {
            return constructors[c];
          }
          if ((cons == null) || (tmpRank > rank)) {
            cons = constructors[c];
            rank = tmpRank;
          }
        }
      }
    }
    if (cons == null) {
      throw new NoSuchMethodException();
    }
    return cons;
  }
  static class TypeInfo {
    final String _shortName;
    final Class _primitive;
    final Class _javaType;
    final boolean _immutable;
    final Object _defaultValue;
    TypeInfo(String shortName, Class primitive, Class javaType, boolean immutable, Object defaultValue) {
      this._shortName = shortName;
      this._primitive = primitive;
      this._javaType = javaType;
      this._immutable = immutable;
      this._defaultValue = defaultValue;
    }
  }
  static TypeInfo[] _typeInfos = { new TypeInfo("other", null, Object.class, false, null), new TypeInfo("string", null, String.class, true, null), new TypeInfo("integer", Integer.TYPE, Integer.class, true, new Integer(0)),
          new TypeInfo("int", Integer.TYPE, Integer.TYPE, true, new Integer(0)), new TypeInfo("long", Long.TYPE, Long.class, true, new Long(0L)), new TypeInfo("big-integer", null, BigInteger.class, true, BigInteger.valueOf(0L)),
          new TypeInfo("boolean", Boolean.TYPE, Boolean.class, true, Boolean.FALSE), new TypeInfo("double", Double.TYPE, Double.class, true, new Double(0.0D)), new TypeInfo("float", Float.TYPE, Float.class, true, new Float(0.0F)),
          new TypeInfo("big-decimal", null, BigDecimal.class, true, new BigDecimal(0.0D)), new TypeInfo("byte", Byte.TYPE, Byte.class, true, new Byte( (byte) 0 )), new TypeInfo("date", null, java.util.Date.class, true, null),
          new TypeInfo("timestamp", null, Timestamp.class, true, null), new TypeInfo("sqldate", null, java.sql.Date.class, true, null), new TypeInfo("sqltime", null, java.sql.Time.class, true, null),
          new TypeInfo("short", Short.TYPE, Short.class, true, new Short( (short) 0 )), new TypeInfo("char", Character.TYPE, Character.class, true, new Character('\000')), new TypeInfo("bytes", null, byte[].class, false, null),
          new TypeInfo("chars", null, char[].class, false, null), new TypeInfo("strings", null, String[].class, false, null), new TypeInfo("locale", null, Locale.class, true, null),
          new TypeInfo("stream", null, InputStream.class, true, null), new TypeInfo("clob", null, getClobClass(), true, null), new TypeInfo("serializable", null, Serializable.class, false, null),
          new TypeInfo("[Lbyte;", null, byte[].class, false, null), new TypeInfo("[Lchar;", null, char[].class, false, null), new TypeInfo("[Ldouble;", null, double[].class, false, null),
          new TypeInfo("[Lfloat;", null, float[].class, false, null), new TypeInfo("[Lint;", null, int[].class, false, null), new TypeInfo("[Llong;", null, long[].class, false, null),
          new TypeInfo("[Lshort;", null, int[].class, false, null), new TypeInfo("[Lboolean;", null, int[].class, false, null), new TypeInfo("duration", null, Duration.class, false, new Duration(0L)),
          new TypeInfo("xml-date", null, Date.class, false, new Date(0L)), new TypeInfo("xml-time",
          null, Time.class, false, new Time(0L)) };
  private static final Class getClobClass() {
    Class type = null;
    try {
      type = Class.forName("java.sql.Clob");
    }
    catch (ClassNotFoundException cnfe) {}
    return type;
  }
}
