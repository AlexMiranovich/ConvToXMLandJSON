package org.exolab.xml.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import org.exolab.mapping.ClassDescriptor;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.FieldHandler;
import org.exolab.mapping.MapItem;
import org.exolab.mapping.MappingException;
import org.exolab.mapping.loader.FieldDescriptorImpl;
import org.exolab.mapping.loader.FieldHandlerImpl;
import org.exolab.types.AnyNode;
import org.exolab.xml.FieldValidator;
import org.exolab.xml.NodeType;
import org.exolab.xml.XMLClassDescriptor;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.descriptors.CoreDescriptors;
import org.exolab.xml.handlers.DateFieldHandler;

public class XMLFieldDescriptorImpl extends FieldDescriptorImpl implements XMLFieldDescriptor {
  private static final String WILD_CARD = "*";
  private static final String NULL_CLASS_ERR = "The 'type' argument passed to the constructor of XMLFieldDescriptorImpl may not be null.";
  private static final String NULL_NAME_ERR = "The 'fieldName' argument passed to the constructor of XMLFieldDescriptorImpl may not be null.";
  private int _argIndex = -1;
  private boolean _container = false;
  private boolean _incremental = false;
  public boolean _isReference = false;
  private boolean _isWild = false;
  private boolean _mapped = false;
  private String[] _matches = null;
  private boolean _nillable = false;
  private NodeType _nodeType = null;
  private String _nsPrefix = null;
  private String _nsURI = null;
  private Properties _xmlProperties = null;
  private String _schemaType = null;
  private String _componentType = null;
  private String _qNamePrefix = null;
  private boolean _useParentClassNamespace = false;
  private FieldValidator _validator = null;
  private String _xmlName = null;
  private String _xmlPath = null;
  private List _substitutes;
  private boolean _derivedFromXSList;
  
  public XMLFieldDescriptorImpl(Class fieldType, String fieldName, String xmlName, NodeType nodeType)
  {
    this._matches = new String[0];
    if (fieldName == null) {
      throw new IllegalArgumentException("The 'fieldName' argument passed to the constructor of XMLFieldDescriptorImpl may not be null.");
    }
    if (fieldType == null) {
      throw new IllegalArgumentException("The 'type' argument passed to the constructor of XMLFieldDescriptorImpl may not be null.");
    }
    setFieldName(fieldName);
    if (fieldType == AnyNode.class) {
      setFieldType(Object.class);
    } else {
      setFieldType(fieldType);
    }
    this._nodeType = (nodeType == null ? NodeType.Attribute : nodeType);
    
    setXMLName(xmlName);
  }
  
  public XMLFieldDescriptorImpl(FieldDescriptor fieldDesc, String xmlName, NodeType nodeType, NodeType primitiveNodeType)
    throws MappingException
  {
    this._matches = new String[0];
    if ((fieldDesc instanceof XMLFieldDescriptor)) {
      setContainingClassDescriptor(fieldDesc.getContainingClassDescriptor());
    }
    setFieldName(fieldDesc.getFieldName());
    if (fieldDesc.getFieldType() == AnyNode.class) {
      setFieldType(Object.class);
    } else {
      setFieldType(fieldDesc.getFieldType());
    }
    ClassDescriptor cd = fieldDesc.getClassDescriptor();
    if (cd != null) {
      if ((cd instanceof XMLClassDescriptor)) {
        setClassDescriptor(cd);
      } else {
        setClassDescriptor(new XMLClassDescriptorAdapter(cd, null, primitiveNodeType));
      }
    }
    setHandler(fieldDesc.getHandler());
    if ((getFieldType() != null) && 
      (Date.class.isAssignableFrom(getFieldType())) && 
      ((getHandler() instanceof FieldHandlerImpl))) {
      setHandler(new DateFieldHandler(getHandler()));
    }
    setTransient(fieldDesc.isTransient());
    setImmutable(fieldDesc.isImmutable());
    setRequired(fieldDesc.isRequired());
    setMultivalued(fieldDesc.isMultivalued());
    if (xmlName == null) {
      setXMLName(getFieldName());
    } else {
      setXMLName(xmlName);
    }
    if (nodeType == null)
    {
      if (isMultivalued()) {
        this._nodeType = NodeType.Element;
      } else {
        this._nodeType = NodeType.Attribute;
      }
    }
    else {
      this._nodeType = nodeType;
    }
    if (isRequired())
    {
      this._validator = new FieldValidator();
      this._validator.setMinOccurs(1);
      this._validator.setDescriptor(this);
    }
  }
  public void setConstructorArgumentIndex(int index) {
    if (this._nodeType != NodeType.Attribute) {
      String err = "constructor arguments only valid for attribute mapped fields.";
      throw new IllegalStateException(err);
    }
    this._argIndex = index;
  }
  public int getConstructorArgumentIndex()
  {
    return this._argIndex;
  }
  public boolean isConstructorArgument()
  {
    return this._argIndex >= 0;
  }
  public void setLocationPath(String path)
  {
    this._xmlPath = path;
  }
  public String getLocationPath()
  {
    return this._xmlPath;
  }
  public void setNameSpacePrefix(String nsPrefix)
  {
    this._nsPrefix = nsPrefix;
  }
  public String getNameSpacePrefix()
  {
    return this._nsPrefix;
  }
  public void setUseParentsNamespace(boolean useParentsNamespace) {
    this._useParentClassNamespace = useParentsNamespace;
  }
  public void setNameSpaceURI(String nsURI)
  {
    this._nsURI = nsURI;
  }
  public String getNameSpaceURI() {
    ClassDescriptor parent = getContainingClassDescriptor();
    if ((this._nsURI == null) && (parent != null) && (this._useParentClassNamespace)) {
      Class type = getFieldType();
      boolean test = !isAnyNode(type);
      if ((this._nodeType == NodeType.Element) && (test) && 
        ((parent instanceof XMLClassDescriptor))) {
        return ((XMLClassDescriptor)parent).getNameSpaceURI();
      }
    }
    return this._nsURI;
  }
  public void setNodeType(NodeType nodeType)
  {
    this._nodeType = (nodeType == null ? NodeType.Attribute : nodeType);
  }
  public NodeType getNodeType()
  {
    return this._nodeType;
  }
  public void setXMLProperty(String propertyName, String value) {
    if (propertyName == null) {
      String err = "The argument 'propertyName' must not be null.";
      throw new IllegalArgumentException(err);
    }
    if (this._xmlProperties == null) {
      this._xmlProperties = new Properties();
    }
    if (value == null) {
      this._xmlProperties.remove(propertyName);
    } else {
      this._xmlProperties.put(propertyName, value);
    }
  }
  public String getXMLProperty(String propertyName) {
    if ((this._xmlProperties == null) || (propertyName == null)) {
      return null;
    }
    return this._xmlProperties.getProperty(propertyName);
  }
  public void setSchemaType(String schemaType)
  {
    this._schemaType = schemaType;
  }
  public void setComponentType(String componentType)
  {
    this._componentType = componentType;
  }
  public String getSchemaType()
  {
    return this._schemaType;
  }
  public String getComponentType()
  {
    return this._componentType;
  }
  public void setValidator(FieldValidator validator) {
    if (this._validator != null) {
      this._validator.setDescriptor(null);
    }
    this._validator = validator;
    if (this._validator != null) {
      this._validator.setDescriptor(this);
    }
  }
  public FieldValidator getValidator()
  {
    return this._validator;
  }
  public void setXMLName(String xmlName)
  {
    this._xmlName = xmlName;
  }
  public String getXMLName()
  {
    return this._xmlName;
  }
  public void setContainer(boolean isContainer)
  {
    this._container = isContainer;
  }
  public boolean isContainer()
  {
    return this._container;
  }
  public void setIncremental(boolean incremental)
  {
    this._incremental = incremental;
  }
  public boolean isIncremental()
  {
    return this._incremental;
  }
  public void setMapped(boolean mapped)
  {
    this._mapped = mapped;
  }
  public boolean isMapped()
  {
    return this._mapped;
  }
  public void setNillable(boolean nillable)
  {
    this._nillable = nillable;
  }
  public boolean isNillable()
  {
    return this._nillable;
  }
  public void setReference(boolean isReference)
  {
    this._isReference = isReference;
  }
  public boolean isReference()
  {
    return this._isReference;
  }
  public void setQNamePrefix(String qNamePrefix)
  {
    this._qNamePrefix = qNamePrefix;
  }
  public String getQNamePrefix()
  {
    return this._qNamePrefix;
  }
  public void setMatches(String matchExpr) {
    this._isWild = false;
    if ((matchExpr == null) || (matchExpr.length() == 0)) {
      return;
    }
    StringTokenizer st = new StringTokenizer(matchExpr);
    ArrayList names = new ArrayList();
    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      if ("*".equals(token))
      {
        this._isWild = true;
        break;
      }
      names.add(token);
    }
    this._matches = new String[names.size()];
    names.toArray(this._matches);
  }
  public boolean matches(String xmlName) {
    if (xmlName != null) {
      if (this._isWild) {
        return true;
      }
      if (this._matches.length > 0) {
        for (int i = 0; i < this._matches.length; i++) {
          if (xmlName.equals(this._matches[i])) {
            return true;
          }
        }
      } else {
        return xmlName.equals(this._xmlName);
      }
    }
    return false;
  }
  public boolean matches(String xmlName, String namespace) {
    if (namespace == null) {
      if ((this._nsURI != null) && (this._nsURI.length() > 0)) {
        return false;
      }
    }
    else if (this._nsURI == null) {
      if ((namespace.length() > 0) && (!this._isWild)) {
        return false;
      }
    }
    else if (!this._nsURI.equals(namespace)) {
      return false;
    }
    return matches(xmlName);
  }
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if ((obj == null) || (!(obj instanceof XMLFieldDescriptor))) {
      return false;
    }
    XMLFieldDescriptor descriptor = (XMLFieldDescriptor)obj;
    if (!getFieldName().equals(descriptor.getFieldName())) {
      return false;
    }
    if (!getFieldType().equals(descriptor.getFieldType())) {
      return false;
    }
    FieldHandler tmpHandler = descriptor.getHandler();
    if (getHandler() == null) {
      return tmpHandler == null;
    }
    if (tmpHandler == null) {
      return false;
    }
    return getHandler().getClass().isInstance(tmpHandler);
  }
  public int hashCode() {
    int hash = 17;
    hash = 17 * getFieldName().hashCode();
    hash = hash * 17 * getFieldType().hashCode();
    if (getHandler() != null) {
      hash = hash * 17 * getHandler().hashCode();
    }
    return hash;
  }
  public String toString() {
    StringBuffer buffer = new StringBuffer(32);
    buffer.append("XMLFieldDesciptor: ");
    buffer.append(getFieldName());
    buffer.append(" AS ");
    buffer.append(this._xmlName);
    if (getNameSpaceURI() != null) {
      buffer.append("{" + getNameSpaceURI() + "}");
    }
    return buffer.toString();
  }
  private static boolean isPrimitive(Class type) {
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
    return type.getSuperclass() == Number.class;
  }
  private static boolean isBuiltInType(Class type) {
    if (type == null) {
      return false;
    }
    return CoreDescriptors.getDescriptor(type) != null;
  }
  private static boolean isMappedItem(Class fieldType)
  {
    return fieldType == MapItem.class;
  }
  private static boolean isAnyNode(Class type)
  {
    return type == Object.class;
  }
  public List getSubstitutes()
  {
    return this._substitutes;
  }
  public void setSubstitutes(List substitutes)
  {
    this._substitutes = substitutes;
  }
  public void setDerivedFromXSList(boolean derivedFromXSList)
  {
    this._derivedFromXSList = derivedFromXSList;
  }
  public boolean isDerivedFromXSList()
  {
    return this._derivedFromXSList;
  }
}
