package org.exolab.xml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.exec.xml.BackwardCompatibilityContext;
import org.exec.xml.InternalContext;
import org.exec.xml.JavaNaming;
import org.exolab.mapping.ClassDescriptor;
import org.exolab.mapping.CollectionHandler;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.MappingException;
import org.exolab.mapping.loader.CollectionHandlers;
import org.exolab.util.ReflectionUtil;

abstract class MarshalFramework {
  private static final double JDK_VERSION_1_5 = 1.5D;
  public static final String XSI_NAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";
  public static final String XSI_SCHEMA_LOCATION = "schemaLocation";
  public static final String XSI_NO_NAMESPACE_SCHEMA_LOCATION = "noNamespaceSchemaLocation";
  public static final String XML_LANG_ATTR = "xml:lang";
  public static final String LANG_ATTR = "lang";
  public static final String NIL_ATTR = "nil";
  public static final String XSI_NIL_ATTR = "xsi:nil";
  public static final String XML_SPACE_ATTR = "xml:space";
  public static final String SPACE_ATTR = "space";
  public static final String TYPE_ATTR = "type";
  public static final String TRUE_VALUE = "true";
  static final String INTERNAL_XML_NAME = "-error-if-this-is-used-";
  static final String JAVA_PREFIX = "java:";
  static final String QNAME_NAME = "QName";
  static final XMLFieldDescriptor[] NO_FIELD_DESCRIPTORS = new XMLFieldDescriptor[0];
  private InternalContext _internalContext;
  
  public MarshalFramework(InternalContext internalContext) {
    if (internalContext == null) {
      this._internalContext = new BackwardCompatibilityContext();
    } else {
      this._internalContext = internalContext;
    }
  }
  public JavaNaming getJavaNaming()
  {
    return this._internalContext.getJavaNaming();
  }
  private void setJavaNaming(JavaNaming javaNaming)
  {
    this._internalContext.setJavaNaming(javaNaming);
  }
  public InternalContext getInternalContext()
  {
    return this._internalContext;
  }
  public void setInternalContext(InternalContext internalContext)
  {
    this._internalContext = internalContext;
  }
  public static boolean isCollection(Class clazz)
  {
    return CollectionHandlers.hasHandler(clazz);
  }
  public CollectionHandler getCollectionHandler(Class clazz) {
    CollectionHandler handler = null;
    try {
      handler = CollectionHandlers.getHandler(clazz);
    }
    catch (MappingException mx) {}
    return handler;
  }
  static boolean isPrimitive(Class type) {
    if (type == null) {
      return false;
    }
    if (type.isPrimitive()) {
      return true;
    }
    if (type == String.class) {
      return true;
    }
    if ((type == Boolean.class) || (type == Character.class)) {
      return true;
    }
    Class superClass = type.getSuperclass();
    if (superClass == Number.class) {
      return true;
    }
    if (superClass != null) {
      return superClass.getName().equals("java.lang.Enum");
    }
    return false;
  }
  static boolean isEnum(Class type) {
    if (type == null) {
      return false;
    }
    float javaVersion = Float.valueOf(System.getProperty("java.specification.version")).floatValue();
    if (javaVersion >= 1.5D) {
      try {
        Boolean isEnum = ReflectionUtil.isEnumViaReflection(type);
        return isEnum.booleanValue();
      }
      catch (Exception e) {}
    }
    return false;
  }
  static final boolean hasFieldsAtLocation(String location, XMLClassDescriptor classDesc) {
    XMLFieldDescriptor[] descriptors = classDesc.getElementDescriptors();
    for (int i = 0; i < descriptors.length; i++) {
      if (descriptors[i] != null) {
        String tmpLocation = descriptors[i].getLocationPath();
        if ((tmpLocation != null) && (tmpLocation.startsWith(location))) {
          return true;
        }
      }
    }
    descriptors = classDesc.getAttributeDescriptors();
    for (int i = 0; i < descriptors.length; i++) {
      if (descriptors[i] != null) {
        String tmpLocation = descriptors[i].getLocationPath();
        if ((tmpLocation != null) && (tmpLocation.startsWith(location))) {
          return true;
        }
      }
    }
    XMLFieldDescriptor content = classDesc.getContentDescriptor();
    if (content != null)
    {
      String tmpLocation = content.getLocationPath();
      if ((tmpLocation != null) && (tmpLocation.startsWith(location))) {
        return true;
      }
    }
    return false;
  }
  public static boolean namespaceEquals(String ns1, String ns2)
  {
    if (ns1 == null) {
      return (ns2 == null) || (ns2.length() == 0);
    }
    if (ns2 == null) {
      return ns1.length() == 0;
    }
    return ns1.equals(ns2);
  }
  static boolean primitiveOrWrapperEquals(Class a, Class b)
  {
    if (!isPrimitive(a)) {
      return false;
    }
    if (!isPrimitive(b)) {
      return false;
    }
    if (a == b) {
      return true;
    }
    if ((a == Boolean.class) || (a == Boolean.TYPE)) {
      return (b == Boolean.class) || (b == Boolean.TYPE);
    }
    if ((a == Byte.class) || (a == Byte.TYPE)) {
      return (b == Byte.class) || (b == Byte.TYPE);
    }
    if ((a == Character.class) || (a == Character.TYPE)) {
      return (b == Character.class) || (b == Character.TYPE);
    }
    if ((a == Double.class) || (a == Double.TYPE)) {
      return (b == Double.class) || (b == Double.TYPE);
    }
    if ((a == Float.class) || (a == Float.TYPE)) {
      return (b == Float.class) || (b == Float.TYPE);
    }
    if ((a == Integer.class) || (a == Integer.TYPE)) {
      return (b == Integer.class) || (b == Integer.TYPE);
    }
    if ((a == Long.class) || (a == Long.TYPE)) {
      return (b == Long.class) || (b == Long.TYPE);
    }
    if ((a == Short.class) || (a == Short.TYPE)) {
      return (b == Short.class) || (b == Short.TYPE);
    }
    return false;
  }
  private static final InheritanceMatch[] NO_MATCH_ARRAY = new InheritanceMatch[0];
  
  protected InheritanceMatch[] searchInheritance(String name, String namespace, XMLClassDescriptor classDesc)
    throws MarshalException
  {
    Iterator classDescriptorIterator = null;
    try
    {
      String className = getJavaNaming().toJavaClassName(name);
      
      Class clazz = classDesc.getJavaClass();
      String pkg = null;
      if (clazz != null)
      {
        while (clazz.getDeclaringClass() != null) {
          clazz = clazz.getDeclaringClass();
        }
        pkg = clazz.getName();
        int idx = pkg.lastIndexOf('.');
        if (idx >= 0)
        {
          pkg = pkg.substring(0, idx + 1);
          className = pkg + className;
        }
      }
      getInternalContext().getXMLClassDescriptorResolver().resolve(className, classDesc.getClass().getClassLoader());
      classDescriptorIterator = getInternalContext().getXMLClassDescriptorResolver().resolveAllByXMLName(name, namespace, null);
    }
    catch (ResolverException rx)
    {
      Throwable actual = rx.getCause();
      if ((actual instanceof MarshalException)) {
        throw ((MarshalException)actual);
      }
      if (actual != null) {
        throw new MarshalException(actual);
      }
      throw new MarshalException(rx);
    }
    Vector inheritanceList = null;
    XMLFieldDescriptor descriptor = null;
    XMLFieldDescriptor[] descriptors = classDesc.getElementDescriptors();
    XMLClassDescriptor cdInherited = null;
    if (classDescriptorIterator.hasNext())
    {
      while ((classDescriptorIterator.hasNext()) && (descriptor == null))
      {
        cdInherited = (XMLClassDescriptor)classDescriptorIterator.next();
        Class subclass = cdInherited.getJavaClass();
        for (int i = 0; i < descriptors.length; i++) {
          if (descriptors[i] != null) {
            if (!"-error-if-this-is-used-".equals(descriptors[i].getXMLName()))
            {
              Class superclass = descriptors[i].getFieldType();
              if ((superclass.isAssignableFrom(subclass)) && (superclass != Object.class))
              {
                descriptor = descriptors[i];
                if (inheritanceList == null) {
                  inheritanceList = new Vector(3);
                }
                inheritanceList.addElement(new InheritanceMatch(descriptor, cdInherited));
              }
            }
          }
        }
      }
      if (descriptor == null) {
        cdInherited = null;
      }
    }
    if (inheritanceList != null)
    {
      InheritanceMatch[] result = new InheritanceMatch[inheritanceList.size()];
      inheritanceList.toArray(result);
      return result;
    }
    return NO_MATCH_ARRAY;
  }
  public static class InheritanceMatch
  {
    public XMLFieldDescriptor parentFieldDesc;
    public XMLClassDescriptor inheritedClassDesc;
    
    public InheritanceMatch(XMLFieldDescriptor fieldDesc, XMLClassDescriptor classDesc)
    {
      this.parentFieldDesc = fieldDesc;
      this.inheritedClassDesc = classDesc;
    }
  }
  class InternalXMLClassDescriptor
    implements XMLClassDescriptor
  {
    private XMLClassDescriptor _classDesc = null;
    private XMLFieldDescriptor[] _attributes = null;
    private XMLFieldDescriptor[] _elements = null;
    private FieldDescriptor[] _fields = null;
    private Map _properties = new HashMap();
    private Set _natures = new HashSet();

    protected InternalXMLClassDescriptor(XMLClassDescriptor classDesc)
    {
      if (classDesc == null)
      {
        String err = "The argument 'classDesc' must not be null.";
        throw new IllegalArgumentException(err);
      }
      while ((classDesc instanceof InternalXMLClassDescriptor)) {
        classDesc = ((InternalXMLClassDescriptor)classDesc).getClassDescriptor();
      }
      this._classDesc = classDesc;
    }

    public InternalXMLClassDescriptor(Marshaller marshaller, XMLClassDescriptor classDesc) {

    }

      public XMLClassDescriptor getClassDescriptor()
    {
      return this._classDesc;
    }
    
    public XMLFieldDescriptor[] getAttributeDescriptors()
    {
      if (this._attributes == null) {
        this._attributes = this._classDesc.getAttributeDescriptors();
      }
      return this._attributes;
    }
    
    public XMLFieldDescriptor getContentDescriptor()
    {
      return this._classDesc.getContentDescriptor();
    }

    public XMLFieldDescriptor getFieldDescriptor(String name, String namespace, NodeType nodeType)
    {
      return this._classDesc.getFieldDescriptor(name, namespace, nodeType);
    }
    
    public XMLFieldDescriptor[] getElementDescriptors()
    {
      if (this._elements == null) {
        this._elements = this._classDesc.getElementDescriptors();
      }
      return this._elements;
    }
    
    public String getNameSpacePrefix()
    {
      return this._classDesc.getNameSpacePrefix();
    }
    
    public String getNameSpaceURI()
    {
      return this._classDesc.getNameSpaceURI();
    }
    
    public TypeValidator getValidator()
    {
      return this._classDesc.getValidator();
    }
    
    public String getXMLName()
    {
      return this._classDesc.getXMLName();
    }
    
    public boolean introspected()
    {
      return Introspector.introspected(this._classDesc);
    }
    
    public boolean canAccept(String name, String namespace, Object object)
    {
      return this._classDesc.canAccept(name, namespace, object);
    }
    
    public void checkDescriptorForCorrectOrderWithinSequence(XMLFieldDescriptor elementDescriptor, UnmarshalState parentState, String xmlName)
      throws ValidationException
    {
      this._classDesc.checkDescriptorForCorrectOrderWithinSequence(elementDescriptor, parentState, xmlName);
    }
    
    public Class getJavaClass()
    {
      return this._classDesc.getJavaClass();
    }
    
    public FieldDescriptor[] getFields()
    {
      if (this._fields == null) {
        this._fields = this._classDesc.getFields();
      }
      return this._fields;
    }
    
    public ClassDescriptor getExtends()
    {
      return this._classDesc.getExtends();
    }
    
    public FieldDescriptor getIdentity()
    {
      return this._classDesc.getIdentity();
    }
    
    public boolean isChoice()
    {
      return false;
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
  }
}
