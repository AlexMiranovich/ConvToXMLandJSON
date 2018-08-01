package org.exolab.xml;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import org.exec.xml.InternalContext;
import org.exec.xml.JavaNaming;
import org.exec.xml.XMLNaming;
import org.exec.xml.XMLNaming;
import org.exolab.mapping.CollectionHandler;
import org.exolab.mapping.FieldHandler;
import org.exolab.mapping.FieldHandlerFactory;
import org.exolab.mapping.GeneralizedFieldHandler;
import org.exolab.mapping.MappingException;
import org.exolab.mapping.TypeConvertor;
import org.exolab.mapping.loader.CollectionHandlers;
import org.exolab.mapping.loader.FieldHandlerImpl;
import org.exolab.mapping.loader.TypeInfo;
import org.exolab.util.ReflectionUtil;
import org.exolab.xml.descriptors.CoreDescriptors;
import org.exolab.xml.handlers.ContainerFieldHandler;
import org.exolab.xml.handlers.DateFieldHandler;
import org.exolab.xml.handlers.DefaultFieldHandlerFactory;
import org.exolab.xml.util.ContainerElement;
import org.exolab.xml.util.XMLClassDescriptorImpl;
import org.exolab.xml.util.XMLFieldDescriptorImpl;

public final class Introspector {
  private static final FieldHandlerFactory DEFAULT_HANDLER_FACTORY = new DefaultFieldHandlerFactory();
  private static final Class[] EMPTY_CLASS_ARGS = new Class[0];
  private static final String LIST = "java.util.List";
  private static final String MAP = "java.util.Map";
  private static final String SET_COLLECTION = "java.util.Set";
  private static final String COLLECTION_WRAPPER_PREFIX = "##container_for_";
  private static final boolean WRAP_COLLECTIONS_DEFAULT = false;
  private static final Class[] COLLECTIONS = loadCollections();
  private static XMLNaming _defaultNaming = null;
  private XMLNaming _xmlNaming;
  private NodeType _primitiveNodeType;
  private boolean _wrapCollectionsInContainer;
  private Vector _handlerFactoryList;
  private Hashtable _handlerFactoryMap;
  private boolean _saveMapKeys;
  private ClassLoader _classLoader;
  private JavaNaming _javaNaming;
  private InternalContext _internalContext;

  public Introspector() {
    this((ClassLoader)null);
  }

  public Introspector(ClassLoader classLoader) {
    this._xmlNaming = null;
    this._primitiveNodeType = null;
    this._wrapCollectionsInContainer = false;
    this._handlerFactoryList = null;
    this._handlerFactoryMap = null;
    this._saveMapKeys = true;
    this._classLoader = null;
    this._classLoader = classLoader;
    this.init();
  }

  private void init() {
    if (this._internalContext != null) {
      this._javaNaming = this._internalContext.getJavaNaming();
      this._xmlNaming = this._internalContext.getXMLNaming();
      this.setPrimitiveNodeType(this._internalContext.getPrimitiveNodeType());
      this._wrapCollectionsInContainer = this._internalContext.getBooleanProperty("org.exolab.xml.introspector.wrapCollections");
      this._saveMapKeys = this._internalContext.getBooleanProperty("org.exolab.xml.saveMapKeys");
    }

  }
  public void setInternalContext(InternalContext internalContext) {
    this._internalContext = internalContext;
    this.init();
  }
  public synchronized void addFieldHandlerFactory(FieldHandlerFactory factory) {
    if (factory == null) {
      String err = "The argument 'factory' must not be null.";
      throw new IllegalArgumentException(err);
    } else {
      if (this._handlerFactoryList == null) {
        this._handlerFactoryList = new Vector();
      }

      this._handlerFactoryList.addElement(factory);
      this.registerHandlerFactory(factory);
    }
  }
  public NodeType getPrimitiveNodeType() {
    return this._primitiveNodeType;
  }
  public XMLClassDescriptor generateClassDescriptor(Class c) throws MarshalException {
    return this.generateClassDescriptor(c, (PrintWriter)null);
  }
  public XMLClassDescriptor generateClassDescriptor(Class c, PrintWriter errorWriter) throws MarshalException {
    if (c == null) {
      return null;
    } else if (c.isArray()) {
      return null;
    } else if (c != Void.class && c != Class.class && c != Object.class) {
      XMLClassDescriptor coreDesc = CoreDescriptors.getDescriptor(c);
      if (coreDesc != null) {
        return coreDesc;
      } else {
        XMLClassDescriptorImpl classDesc = new IntrospectedXMLClassDescriptor(c);
        Method[] methods = c.getMethods();
        List dateDescriptors = new ArrayList(3);
        Hashtable methodSets = new Hashtable();
        int methodCount = 0;
        Class superClass = c.getSuperclass();
        Class[] interfaces = c.getInterfaces();

        boolean isCollection;
        Method tmpMethod;
        Class type;
        for(int i = 0; i < methods.length; ++i) {
          Method method = methods[i];
          Class owner = method.getDeclaringClass();
          String fieldName;
          if (owner != c) {
            if (!owner.isInterface()) {
              continue;
            }

            if (interfaces.length > 0) {
              isCollection = false;

              for(int count = 0; count < interfaces.length; ++count) {
                if (interfaces[count] == owner) {
                  isCollection = true;
                  break;
                }
              }

              if (!isCollection) {
                continue;
              }
            }
          } else if (superClass != null) {
            Class[] args = method.getParameterTypes();
            fieldName = method.getName();
            tmpMethod = null;

            try {
              tmpMethod = superClass.getMethod(fieldName, args);
            } catch (NoSuchMethodException var31) {
              ;
            }

            if (tmpMethod != null) {
              continue;
            }
          }

          if ((method.getModifiers() & 8) == 0) {
            String methodName = method.getName();
            MethodSet methodSet;
            if (methodName.startsWith("get")) {
              if (method.getParameterTypes().length == 0) {
                ++methodCount;
                type = method.getReturnType();
                if (type != null && isDescriptable(type)) {
                  fieldName = methodName.substring(3);
                  fieldName = this._javaNaming.toJavaMemberName(fieldName);
                  methodSet = (MethodSet)methodSets.get(fieldName);
                  if (methodSet == null) {
                    methodSet = new MethodSet(fieldName);
                    methodSets.put(fieldName, methodSet);
                  }

                  methodSet._get = method;
                }
              }
            } else if (methodName.startsWith("is")) {
              if (method.getParameterTypes().length == 0) {
                type = method.getReturnType();
                if (type != null) {
                  if (type.isPrimitive()) {
                    if (type != Boolean.TYPE) {
                      continue;
                    }
                  } else if (type != Boolean.class) {
                    continue;
                  }

                  ++methodCount;
                  fieldName = methodName.substring("is".length());
                  fieldName = this._javaNaming.toJavaMemberName(fieldName);
                  methodSet = (MethodSet)methodSets.get(fieldName);
                  if (methodSet == null) {
                    methodSet = new MethodSet(fieldName);
                    methodSets.put(fieldName, methodSet);
                  }

                  methodSet._get = method;
                }
              }
            } else {
              if (methodName.startsWith("add")) {
                if (method.getParameterTypes().length == 1) {
                  ++methodCount;
                  if (isDescriptable(method.getParameterTypes()[0])) {
                    fieldName = methodName.substring(3);
                    fieldName = this._javaNaming.toJavaMemberName(fieldName);
                    methodSet = (MethodSet)methodSets.get(fieldName);
                    if (methodSet == null) {
                      methodSet = new MethodSet(fieldName);
                      methodSets.put(fieldName, methodSet);
                    }

                    methodSet._add = method;
                  }
                }
              } else if (methodName.startsWith("set")) {
                if (method.getParameterTypes().length == 1) {
                  ++methodCount;
                  if (isDescriptable(method.getParameterTypes()[0])) {
                    fieldName = methodName.substring(3);
                    fieldName = this._javaNaming.toJavaMemberName(fieldName);
                    methodSet = (MethodSet)methodSets.get(fieldName);
                    if (methodSet == null) {
                      methodSet = new MethodSet(fieldName);
                      methodSets.put(fieldName, methodSet);
                    }

                    methodSet._set = method;
                  }
                }
              } else if (methodName.startsWith("create") && method.getParameterTypes().length == 0) {
                type = method.getReturnType();
                if (isDescriptable(type)) {
                  fieldName = methodName.substring("create".length());
                  fieldName = this._javaNaming.toJavaMemberName(fieldName);
                  methodSet = (MethodSet)methodSets.get(fieldName);
                  if (methodSet == null) {
                    methodSet = new MethodSet(fieldName);
                    methodSets.put(fieldName, methodSet);
                  }

                  methodSet._create = method;
                }
              }
            }
          }
        }

        Enumeration enumeration = methodSets.elements();

        FieldHandlerFactory factory;
        ContainerFieldHandler cHandler;
        while(enumeration.hasMoreElements()) {
          MethodSet methodSet = (MethodSet)enumeration.nextElement();
          String xmlName = this._xmlNaming.toXMLName(methodSet._fieldName);
          isCollection = false;
          type = null;
          if (methodSet._add != null) {
            type = methodSet._add.getParameterTypes()[0];
            isCollection = true;
          }

          if (type == null) {
            if (methodSet._get != null) {
              type = methodSet._get.getReturnType();
            } else {
              if (methodSet._set == null) {
                continue;
              }

              type = methodSet._set.getParameterTypes()[0];
            }
          }

          isCollection = isCollection || isCollection(type);
          tmpMethod = null;
          CollectionHandler colHandler = null;
          if (isCollection && methodSet._add == null) {
            try {
              colHandler = CollectionHandlers.getHandler(type);
            } catch (MappingException var30) {
              ;
            }

            if (type.isArray()) {
              if (type.getComponentType() == Byte.TYPE) {
                colHandler = null;
              } else {
                type = type.getComponentType();
              }
            }
          }

          TypeInfo typeInfo = new TypeInfo(type, (TypeConvertor)null, (TypeConvertor)null, false, (Object)null, colHandler);
          FieldHandler handler = null;
          isCollection = false;

          try {
            handler = new FieldHandlerImpl(methodSet._fieldName, (Method[])null, (Method[])null, methodSet._get, methodSet._set, typeInfo);
            if (methodSet._add != null) {
              ((FieldHandlerImpl)handler).setAddMethod(methodSet._add);
            }

            if (methodSet._create != null) {
              ((FieldHandlerImpl)handler).setCreateMethod(methodSet._create);
            }

            if (isCollection && this._saveMapKeys && isMapCollection(type)) {
              ((FieldHandlerImpl)handler).setConvertFrom(new IdentityConvertor());
            }

            factory = this.getHandlerFactory(type);
            if (factory != null) {
              GeneralizedFieldHandler gfh = factory.createFieldHandler(type);
              if (gfh != null) {
                gfh.setFieldHandler((FieldHandler)handler);
                handler = gfh;
                isCollection = true;
                if (gfh.getFieldType() != null) {
                  type = gfh.getFieldType();
                }
              }
            }
          } catch (MappingException var33) {
            throw new MarshalException(var33);
          }

          XMLFieldDescriptorImpl fieldDesc = this.createFieldDescriptor(type, methodSet._fieldName, xmlName);
          if (isCollection) {
            fieldDesc.setMultivalued(true);
            fieldDesc.setNodeType(NodeType.Element);
          }

          if (Date.class.isAssignableFrom(type) && !isCollection) {
            dateDescriptors.add(fieldDesc);
          }

          fieldDesc.setHandler((FieldHandler)handler);
          fieldDesc.setUseParentsNamespace(true);
          if (isCollection && this._wrapCollectionsInContainer) {
            String fieldName = "##container_for_" + methodSet._fieldName;
            Class cType = ContainerElement.class;
            XMLClassDescriptorImpl containerClassDesc = new XMLClassDescriptorImpl(cType);
            containerClassDesc.addFieldDescriptor(fieldDesc);
            fieldDesc.setXMLName((String)null);
            fieldDesc.setMatches("*");
            cHandler = new ContainerFieldHandler((FieldHandler)handler);
            fieldDesc.setHandler(cHandler);
            fieldDesc = this.createFieldDescriptor(cType, fieldName, xmlName);
            fieldDesc.setClassDescriptor(containerClassDesc);
            fieldDesc.setHandler(cHandler);
            fieldDesc.setUseParentsNamespace(true);
          }

          classDesc.addFieldDescriptor(fieldDesc);
        }

        Class owner;
        if (methodCount == 0) {
          Field[] fields = c.getFields();
          Hashtable descriptors = new Hashtable();

          for(int i = 0; i < fields.length; ++i) {
            Field field = fields[i];
            owner = field.getDeclaringClass();
            if (owner != c) {
              if (!owner.isInterface()) {
                continue;
              }

              if (interfaces.length > 0) {
                boolean found = false;

                for(int count = 0; count < interfaces.length; ++count) {
                  if (interfaces[count] == owner) {
                    found = true;
                    break;
                  }
                }

                if (!found) {
                  continue;
                }
              }
            }

            int modifiers = field.getModifiers();
            if (!Modifier.isTransient(modifiers) && (!Modifier.isFinal(modifiers) || !Modifier.isStatic(modifiers))) {
              type = field.getType();
              if (isDescriptable(type)) {
                isCollection = isCollection(type);
                factory = null;
                CollectionHandler colHandler = null;
                if (isCollection) {
                  try {
                    colHandler = CollectionHandlers.getHandler(type);
                  } catch (MappingException var29) {
                    ;
                  }

                  if (type.isArray()) {
                    if (type.getComponentType() == Byte.TYPE) {
                      colHandler = null;
                    } else {
                      type = type.getComponentType();
                    }
                  }
                }

                String fieldName = field.getName();
                String xmlName = this._xmlNaming.toXMLName(fieldName);
                TypeInfo typeInfo = new TypeInfo(type, (TypeConvertor)null, (TypeConvertor)null, false, (Object)null, colHandler);
                cHandler = null;
                boolean customHandler = false;

                Object handler;
                try {
                  handler = new FieldHandlerImpl(field, typeInfo);
                  if (isCollection && this._saveMapKeys && isMapCollection(type)) {
                    ((FieldHandlerImpl)handler).setConvertFrom(new IdentityConvertor());
                  }

                  factory = this.getHandlerFactory( type );
                  if (factory != null) {
                    GeneralizedFieldHandler gfh = factory.createFieldHandler(type);
                    if (gfh != null) {
                      gfh.setFieldHandler((FieldHandler)handler);
                      handler = gfh;
                      customHandler = true;
                      if (gfh.getFieldType() != null) {
                        type = gfh.getFieldType();
                      }
                    }
                  }
                } catch (MappingException var32) {
                  throw new MarshalException(var32);
                }

                XMLFieldDescriptorImpl fieldDesc = this.createFieldDescriptor(type, fieldName, xmlName);
                if (isCollection) {
                  fieldDesc.setNodeType(NodeType.Element);
                  fieldDesc.setMultivalued(true);
                }

                descriptors.put(xmlName, fieldDesc);
                classDesc.addFieldDescriptor(fieldDesc);
                fieldDesc.setHandler((FieldHandler)handler);
                fieldDesc.setUseParentsNamespace(true);
                if (Date.class.isAssignableFrom(type) && !customHandler) {
                  dateDescriptors.add(fieldDesc);
                }
              }
            }
          }
        }

        if (dateDescriptors != null) {
          for(int i = 0; i < dateDescriptors.size(); ++i) {
            XMLFieldDescriptorImpl fieldDesc = (XMLFieldDescriptorImpl)dateDescriptors.get(i);
            FieldHandler handler = fieldDesc.getHandler();
            fieldDesc.setImmutable(true);
            DateFieldHandler dfh = new DateFieldHandler(handler);
            owner = fieldDesc.getFieldType();
            if (java.sql.Date.class.isAssignableFrom(owner)) {
              dfh.setUseSQLDate(true);
            }

            fieldDesc.setHandler(dfh);
          }
        }

        if (superClass != null && superClass != Void.class && superClass != Object.class && superClass != Class.class) {
          try {
            XMLClassDescriptor parent = this.generateClassDescriptor(superClass, errorWriter);
            if (parent != null) {
              classDesc.setExtends(parent);
            }
          } catch (MarshalException var28) {
            ;
          }
        }

        return classDesc;
      }
    } else {
      throw new MarshalException("The marshaller cannot marshal/unmarshal types of Void.class, Class.class or Object.class");
    }
  }

  public synchronized boolean removeFieldHandlerFactory(FieldHandlerFactory factory) {
    if (factory == null) {
      String err = "The argument 'factory' must not be null.";
      throw new IllegalArgumentException(err);
    } else if (this._handlerFactoryList == null) {
      return false;
    } else if (!this._handlerFactoryList.removeElement(factory)) {
      return false;
    } else {
      this._handlerFactoryMap.clear();

      for(int i = 0; i < this._handlerFactoryList.size(); ++i) {
        FieldHandlerFactory tmp = (FieldHandlerFactory)this._handlerFactoryList.elementAt(i);
        this.registerHandlerFactory(tmp);
      }

      return true;
    }
  }

  public void setWrapCollections(boolean wrapCollections) {
    this._wrapCollectionsInContainer = wrapCollections;
  }

  public static boolean introspected(XMLClassDescriptor descriptor) {
    return descriptor instanceof IntrospectedXMLClassDescriptor;
  }

  public static boolean marshallable(Class type) {
    if (type != Void.class && type != Class.class) {
      if ((!type.isInterface() || type == Object.class) && !isPrimitive(type) && !type.isArray()) {
        try {
          type.getConstructor(EMPTY_CLASS_ARGS);
        } catch (NoSuchMethodException var2) {
          return CoreDescriptors.getDescriptor(type) != null;
        }
      }

      return true;
    } else {
      return false;
    }
  }

  public void setNaming(XMLNaming naming) {
    if (naming == null) {
      this._xmlNaming = _defaultNaming;
    } else {
      this._xmlNaming = naming;
    }

  }

  public void setPrimitiveNodeType(NodeType nodeType) {
    if (nodeType == NodeType.Element) {
      this._primitiveNodeType = nodeType;
    } else {
      this._primitiveNodeType = NodeType.Attribute;
    }

  }

  public void setSaveMapKeys(boolean saveMapKeys) {
    this._saveMapKeys = saveMapKeys;
  }

  public static String toJavaName(String name, boolean upperFirst) {
    int size = name.length();
    char[] ncChars = name.toCharArray();
    int next = 0;
    boolean uppercase = upperFirst;

    for(int i = 0; i < size; ++i) {
      char ch = ncChars[i];
      switch(ch) {
        case '-':
        case ':':
          uppercase = true;
          break;
        default:
          if (uppercase) {
            ncChars[next] = Character.toUpperCase(ch);
            uppercase = false;
          } else {
            ncChars[next] = ch;
          }

          ++next;
      }
    }

    return new String(ncChars, 0, next);
  }

  private XMLFieldDescriptorImpl createFieldDescriptor(Class type, String fieldName, String xmlName) {
    XMLFieldDescriptorImpl fieldDesc = new XMLFieldDescriptorImpl(type, fieldName, xmlName, (NodeType)null);
    if (type.isArray()) {
      fieldDesc.setNodeType(NodeType.Element);
    } else if (type.isPrimitive()) {
      fieldDesc.setNodeType(this._primitiveNodeType);
    } else {
      fieldDesc.setNodeType(NodeType.Element);
    }

    if (type == Object.class) {
      fieldDesc.setMatches(xmlName + " *");
    }

    return fieldDesc;
  }

  private FieldHandlerFactory getHandlerFactory(Class type) {
    if (this._handlerFactoryMap != null) {
      for(Class tmp = type; tmp != null; tmp = tmp.getSuperclass()) {
        Object obj = this._handlerFactoryMap.get(tmp);
        if (obj != null) {
          return (FieldHandlerFactory)obj;
        }
      }
    }

    return DEFAULT_HANDLER_FACTORY.isSupportedType(type) ? DEFAULT_HANDLER_FACTORY : null;
  }

  private void registerHandlerFactory(FieldHandlerFactory factory) {
    if (this._handlerFactoryMap == null) {
      this._handlerFactoryMap = new Hashtable();
    }

    Class[] types = factory.getSupportedTypes();

    for(int i = 0; i < types.length; ++i) {
      this._handlerFactoryMap.put(types[i], factory);
    }

  }

  public static boolean isCollection(Class clazz) {
    if (clazz.isArray()) {
      return true;
    } else {
      for(int i = 0; i < COLLECTIONS.length; ++i) {
        if (clazz == COLLECTIONS[i] || COLLECTIONS[i].isAssignableFrom(clazz)) {
          return true;
        }
      }

      return false;
    }
  }

  public static boolean isMapCollection(Class clazz) {
    if (clazz.isArray()) {
      return false;
    } else {
      for(int i = 0; i < COLLECTIONS.length; ++i) {
        if (clazz == COLLECTIONS[i] || COLLECTIONS[i].isAssignableFrom(clazz)) {
          if (COLLECTIONS[i] == Hashtable.class) {
            return true;
          }

          if (COLLECTIONS[i].getName().equals("java.util.Map")) {
            return true;
          }
        }
      }

      return false;
    }
  }

  private static boolean isDescriptable(Class type) {
    if (type != Void.class && type != Class.class) {
      float javaVersion = Float.valueOf(System.getProperty("java.specification.version"));
      if ((double)javaVersion >= 1.5D) {
        try {
          Boolean isEnum = ReflectionUtil.isEnumViaReflection(type);
          if (isEnum) {
            return true;
          }
        } catch (Exception var3) {
          ;
        }
      }

      if (!type.isInterface() && type != Object.class && !isPrimitive(type) && !type.isArray()) {
        try {
          type.getConstructor(EMPTY_CLASS_ARGS);
        } catch (NoSuchMethodException var4) {
          return CoreDescriptors.getDescriptor(type) != null;
        }
      }

      return true;
    } else {
      return false;
    }
  }

  private static boolean isPrimitive(Class type) {
    if (type.isPrimitive()) {
      return true;
    } else if (type != Boolean.class && type != Character.class) {
      Class superClass = type.getSuperclass();
      if (superClass == Number.class) {
        return true;
      } else {
        return superClass != null ? superClass.getName().equals("java.lang.Enum") : false;
      }
    } else {
      return true;
    }
  }

  private static Class[] loadCollections() {
    Vector collections = new Vector(6);
    collections.addElement(Vector.class);
    collections.addElement(Enumeration.class);
    collections.addElement(Hashtable.class);
    ClassLoader loader = Vector.class.getClassLoader();
    Class clazz = null;

    try {
      if (loader != null) {
        clazz = loader.loadClass("java.util.List");
      } else {
        clazz = Class.forName("java.util.List");
      }
    } catch (ClassNotFoundException var5) {
      ;
    }

    if (clazz != null) {
      collections.addElement(clazz);
      clazz = null;

      try {
        if (loader != null) {
          clazz = loader.loadClass("java.util.Map");
        } else {
          clazz = Class.forName("java.util.Map");
        }

        if (clazz != null) {
          collections.addElement(clazz);
        }

        if (loader != null) {
          clazz = loader.loadClass("java.util.Set");
        } else {
          clazz = Class.forName("java.util.Set");
        }

        if (clazz != null) {
          collections.addElement(clazz);
        }
      } catch (ClassNotFoundException var4) {
        ;
      }
    }

    Class[] classes = new Class[collections.size()];
    collections.copyInto(classes);
    return classes;
  }

  class MethodSet {
    Method _add = null;
    Method _create = null;
    Method _get = null;
    Method _set = null;
    String _fieldName = null;

    MethodSet(String fieldName) {
      this._fieldName = fieldName;
    }
  }

  class IdentityConvertor implements TypeConvertor {
    IdentityConvertor() {
    }

    public Object convert(Object object) {
      return object;
    }
  }
}
