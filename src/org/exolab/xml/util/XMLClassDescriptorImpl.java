package org.exolab.xml.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.exolab.mapping.AbstractFieldHandler;
import org.exolab.mapping.AccessMode;
import org.exolab.mapping.ClassDescriptor;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.FieldHandler;
import org.exolab.xml.FieldValidator;
import org.exolab.xml.NodeType;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.UnmarshalState;
import org.exolab.xml.ValidationContext;
import org.exolab.xml.ValidationException;
import org.exolab.xml.Validator;
import org.exolab.xml.XMLClassDescriptor;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.location.XPathLocation;
import org.exolab.xml.util.resolvers.ResolveHelpers;

public class XMLClassDescriptorImpl extends Validator implements XMLClassDescriptor {
  private static final short ALL = 0;
  private static final short CHOICE = 1;
  private static final short SEQUENCE = 2;
  private static final String NULL_CLASS_ERR = "The Class passed as an argument to the constructor of XMLClassDescriptorImpl may not be null.";
  private static final String WILDCARD = "*";
  private XMLFieldDescriptors _attributes = null;
  private XMLFieldDescriptor[] _attArray = null;
  private Class _class = null;
  private int _containerCount = 0;
  private XMLFieldDescriptor _contentDescriptor = null;
  private TypeValidator _validator = null;
  private XMLFieldDescriptors _elements = null;
  private XMLFieldDescriptor[] _elemArray = null;
  private String _nsPrefix = null;
  private String _nsURI = null;
  private String _xmlName;
  private boolean _elementDefinition = false;
  private XMLClassDescriptor _extends;
  private FieldDescriptor _identity;
  private AccessMode _accessMode;
  private boolean _introspected = false;
  private short _compositor = 0;
  private List _sequenceOfElements = new ArrayList();
  private List _substitutes = new LinkedList();
  private Map _properties = new HashMap();
  private Set _natures = new HashSet();
  
  public XMLClassDescriptorImpl(Class type) {
    this();
    if (type == null) {
      throw new IllegalArgumentException("The Class passed as an argument to the constructor of XMLClassDescriptorImpl may not be null.");
    }
    this._class = type;
  }
  public XMLClassDescriptorImpl(Class type, String xmlName) {
    this();
    if (type == null) {
      throw new IllegalArgumentException("The Class passed as an argument to the constructor of XMLClassDescriptorImpl may not be null.");
    }
    this._class = type;
    setXMLName(xmlName);
  }
  protected XMLClassDescriptorImpl() {
    this._attributes = new XMLFieldDescriptors(5);
    this._elements = new XMLFieldDescriptors(7);
  }
  public void addFieldDescriptor(XMLFieldDescriptor descriptor)
  {
    addFieldDescriptor(descriptor, true);
  }
  public boolean contains(XMLFieldDescriptor descriptor) {
    if (descriptor == null) {
      return false;
    }
    if (this._attributes.contains(descriptor)) {
      return true;
    }
    if (this._elements.contains(descriptor)) {
      return true;
    }
    return descriptor.equals(this._contentDescriptor);
  }
  public XMLFieldDescriptor[] getAttributeDescriptors()
  {
    return (XMLFieldDescriptor[])getAttributeArray().clone();
  }
  public XMLFieldDescriptor getContentDescriptor()
  {
    return this._contentDescriptor;
  }
  public XMLFieldDescriptor[] getElementDescriptors()
  {
    return (XMLFieldDescriptor[])getElementArray().clone();
  }
  public void checkDescriptorForCorrectOrderWithinSequence(XMLFieldDescriptor elementDescriptor, UnmarshalState parentState, String xmlName)
    throws ValidationException {
    if ((this._compositor == 2) && (this._sequenceOfElements.size() > 0)) {
      if (parentState._expectedIndex == this._sequenceOfElements.size()) {
        throw new ValidationException("Element with name " + xmlName + " passed to type " + getXMLName() + " in incorrect order; It is not allowed to be the last element of this sequence!");
      }
      XMLFieldDescriptor expectedElementDescriptor = (XMLFieldDescriptor)this._sequenceOfElements.get(parentState._expectedIndex);
      String expectedElementName = expectedElementDescriptor.getXMLName();
      String elementName = xmlName;
      boolean anyNode = (expectedElementDescriptor.getFieldName().equals("_anyObject")) && (expectedElementName == null);
      if ((!anyNode) && (expectedElementDescriptor.getXMLName().equals("-error-if-this-is-used-"))) {
        ArrayList possibleNames = new ArrayList();
        fillPossibleNames(possibleNames, expectedElementDescriptor);
        if (!possibleNames.contains(elementName)) {
          if (!expectedElementDescriptor.isRequired()) {
            parentState._expectedIndex += 1;
            checkDescriptorForCorrectOrderWithinSequence(elementDescriptor, parentState, xmlName);
          }
          else {
            throw new ValidationException("Element with name " + elementName + " passed to type " + getXMLName() + " in incorrect order; expected element has to be member of the expected choice.");
          }
        } else {
          parentState._expectedIndex += 1;
        }
        return;
      }
      if ((expectedElementDescriptor.isMultivalued()) && (!parentState._withinMultivaluedElement)) {
        parentState._withinMultivaluedElement = true;
      }
      if ((!anyNode) && (!expectedElementName.equals(elementName))) {
        List substitutes = expectedElementDescriptor.getSubstitutes();
        if ((substitutes != null) && (!substitutes.isEmpty()) && 
          (substitutes.contains(elementName))) {
          if (!parentState._withinMultivaluedElement) {
            parentState._expectedIndex += 1;
          }
          return;
        }
        if (expectedElementDescriptor.isMultivalued())
        {
          parentState._withinMultivaluedElement = false;
          parentState._expectedIndex += 1;
          checkDescriptorForCorrectOrderWithinSequence(elementDescriptor, parentState, xmlName);
          return;
        }
        if (expectedElementDescriptor.isRequired()) {
          throw new ValidationException("Element with name " + elementName + " passed to type " + getXMLName() + " in incorrect order; expected element with name '" + expectedElementName + "' or any other optional element declared prior to it.");
        }
        parentState._expectedIndex += 1;
        checkDescriptorForCorrectOrderWithinSequence(elementDescriptor, parentState, xmlName);
        return;
      }
      if (!parentState._withinMultivaluedElement) {
        parentState._expectedIndex += 1;
      }
    }
  }
  private void fillPossibleNames(List possibleNames, XMLFieldDescriptor descriptor) {
    XMLFieldDescriptor[] descriptors = ((XMLClassDescriptor)descriptor.getClassDescriptor()).getElementDescriptors();
    if (descriptors.length == 0) {
      return;
    }
    for (int i = 0; i < descriptors.length; i++) {
      if (("_items".equals(descriptors[i].getFieldName())) || ("-error-if-this-is-used-".equals(descriptors[i].getXMLName()))) {
        fillPossibleNames(possibleNames, descriptors[i]);
      } else {
        possibleNames.add(descriptors[i].getXMLName());
      }
    }
  }
  
  public XMLFieldDescriptor getFieldDescriptor(String name, String namespace, NodeType nodeType) {
    boolean wild = (nodeType == null) || (this._introspected);
    XMLFieldDescriptor result = null;
    XMLFieldDescriptor[] attributes = this._attArray;
    XMLFieldDescriptor[] elements = this._elemArray;
    String location = null;
    if (name != null) {
      int idx = name.lastIndexOf('/');
      if (idx >= 0) {
        location = name.substring(0, idx);
        name = name.substring(idx + 1);
      }
    }
    if ((wild) || (nodeType == NodeType.Element)) {
      if (elements == null) {
        elements = getElementArray();
      }
      for (int i = 0; i < elements.length; i++) {
        XMLFieldDescriptor desc = elements[i];
        if (desc != null) {
          if ((location == null) || 
            (location.equals(desc.getLocationPath()))){
            if (desc.matches(name)) {
              if (!desc.matches("*")) {
                if ((namespace == null) || (ResolveHelpers.namespaceEquals(namespace, desc.getNameSpaceURI()))) {
                  return desc;
                }
              }
              else {
                if (name.equals(desc.getXMLName())) {
                  return desc;
                }
                result = desc;
              }
            }
            if (desc.isContainer()) {
              XMLClassDescriptor xcd = (XMLClassDescriptor)desc.getClassDescriptor();
              if (xcd != this) {
                if (xcd.getFieldDescriptor(name, namespace, NodeType.Element) != null) {
                  result = desc;
                  break;
                }
              }
            }
          }
        }
      }
      if (result != null) {
        return result;
      }
    }
    if ((wild) || (nodeType == NodeType.Attribute)) {
      if (attributes == null) {
        attributes = getAttributeArray();
      }
      for (int i = 0; i < attributes.length; i++) {
        XMLFieldDescriptor desc = attributes[i];
        if ((desc != null) && 
          (desc.matches(name))) {
          return desc;
        }
      }
    }
    if (nodeType == NodeType.Namespace)
    {
      if (attributes == null) {
        attributes = getAttributeArray();
      }
      for (int i = 0; i < attributes.length; i++) {
        if ((attributes[i] != null) && 
          (attributes[i].getNodeType() == NodeType.Namespace)) {
          return attributes[i];
        }
      }
    }
    if (nodeType == NodeType.Attribute) {
      if (elements == null) {
        elements = getElementArray();
      }
      for (int i = 0; i < elements.length; i++) {
        XMLFieldDescriptor desc = elements[i];
        if (desc.isContainer()) {
          XMLClassDescriptor xcd = (XMLClassDescriptor)desc.getClassDescriptor();
          if (xcd != this) {
            XMLFieldDescriptor temp = xcd.getFieldDescriptor(name, namespace, NodeType.Attribute);
            if (temp != null) {
              return desc;
            }
          }
        }
      }
    }
    return null;
  }
  
  public String getNameSpacePrefix()
  {
    return this._nsPrefix;
  }
  public String getNameSpaceURI() {
    return this._nsURI;
  }
  public TypeValidator getValidator() {
    if (this._validator != null) {
      return this._validator;
    }
    return this;
  }
  public String getXMLName()
  {
    return this._xmlName;
  }
  public boolean isElementDefinition() {
    return this._elementDefinition;
  }
  public boolean hasContainerFields() {
    return this._containerCount > 0;
  }
  public boolean removeFieldDescriptor(XMLFieldDescriptor descriptor) {
    if (descriptor == null) {
      return false;
    }
    boolean removed = false;
    NodeType nodeType = descriptor.getNodeType();
    switch (nodeType.getType()) {
    case 0: 
    case 2: 
      if (this._attributes.remove(descriptor)) {
        removed = true;
        this._attArray = null;
      }
      break;
    case 3: 
      if (this._contentDescriptor == descriptor)
      {
        this._contentDescriptor = null;
        removed = true;
      }
      break;
    case 1: 
    default: 
      if (this._elements.remove(descriptor))
      {
        this._elemArray = null;
        removed = true;
        if (descriptor.isContainer()) {
          this._containerCount -= 1;
        }
      }
      break;
    }
    return removed;
  }
  public void setCompositorAsAll() {
    this._compositor = 0;
  }
  public void setCompositorAsChoice() {
    this._compositor = 1;
  }
  public void setCompositorAsSequence() {
    this._compositor = 2;
  }
  public void setExtends(XMLClassDescriptor classDesc)
  {
    FieldDescriptor[] fields = null;
    if (this._extends != null)
    {
      sortDescriptors();
      fields = this._extends.getFields();
      for (int i = 0; i < fields.length; i++) {
        removeFieldDescriptor((XMLFieldDescriptor)fields[i]);
      }
    }
    this._extends = classDesc;
    if (this._extends != null)
    {
      fields = classDesc.getFields();
      for (int i = 0; i < fields.length; i++) {
        addFieldDescriptor((XMLFieldDescriptor)fields[i], false);
      }
    }
  }
  
  public void setIdentity(XMLFieldDescriptor fieldDesc)
  {
    if ((fieldDesc != null) && 
      (!this._attributes.contains(fieldDesc)) && (!this._elements.contains(fieldDesc))) {
      addFieldDescriptor(fieldDesc);
    }
    this._identity = fieldDesc;
  }
  
  public void setNameSpacePrefix(String nsPrefix)
  {
    this._nsPrefix = nsPrefix;
  }
  
  public void setNameSpaceURI(String nsURI)
  {
    this._nsURI = nsURI;
  }
  
  public void setXMLName(String xmlName)
  {
    if (xmlName == null)
    {
      if ((this._xmlName == null) && (this._class != null)) {
        this._xmlName = this._class.getName();
      }
    }
    else {
      this._xmlName = xmlName;
    }
  }
  
  public void setElementDefinition(boolean elementDefinition)
  {
    this._elementDefinition = elementDefinition;
  }
  
  public void sortDescriptors()
  {
    XMLFieldDescriptor[] descriptors = getAttributeArray();
    for (int i = 0; i < descriptors.length; i++)
    {
      XMLFieldDescriptor fieldDesc = descriptors[i];
      switch (fieldDesc.getNodeType().getType())
      {
      case 1: 
        this._elements.add(fieldDesc);
        this._attributes.remove(fieldDesc);
        this._attArray = null;
        break;
      case 3: 
        this._attributes.remove(fieldDesc);
        this._attArray = null;
      }
    }
    descriptors = getElementArray();
    for (int i = 0; i < descriptors.length; i++)
    {
      XMLFieldDescriptor fieldDesc = descriptors[i];
      switch (fieldDesc.getNodeType().getType())
      {
      case 0: 
      case 2: 
        this._attributes.add(fieldDesc);
        this._elements.remove(fieldDesc);
        this._elemArray = null;
        break;
      case 3: 
        this._elements.remove(fieldDesc);
        this._elemArray = null;
      }
    }
  }
  
  public String toString()
  {
    String str = super.toString() + "; descriptor for class: ";
    if (this._class != null) {
      str = str + this._class.getName();
    } else {
      str = str + "[null]";
    }
    str = str + "; xml name: " + this._xmlName;
    
    return str;
  }
  
  public void validate(Object object)
    throws ValidationException
  {
    validate(object, (ValidationContext)null);
  }
  
  public void validate(Object object, ValidationContext context)
    throws ValidationException
  {
    if (object == null) {
      throw new ValidationException("Cannot validate a null object.");
    }
    Class a = getJavaClass();
    ClassLoader acl = a.getClassLoader();
    Class b = object.getClass();
    ClassLoader bcl = b.getClassLoader();
    if (!getJavaClass().isAssignableFrom(object.getClass()))
    {
      String err = "The given object is not an instance of the class described by this ClassDecriptor.";
      
      throw new ValidationException(err);
    }
    XMLFieldDescriptor[] localElements = getElementArray();
    XMLFieldDescriptor[] localAttributes = getAttributeArray();
    if (this._extends != null)
    {
      if ((this._extends instanceof XMLClassDescriptorImpl))
      {
        ((XMLClassDescriptorImpl)this._extends).validate(object, context);
      }
      else
      {
        TypeValidator baseValidator = this._extends.getValidator();
        if (baseValidator != null) {
          baseValidator.validate(object, context);
        }
      }
      XMLFieldDescriptor[] inheritedElements = this._extends.getElementDescriptors();
      XMLFieldDescriptor[] allElements = localElements;
      localElements = new XMLFieldDescriptor[allElements.length - inheritedElements.length];
      int localIdx = 0;
      for (int i = 0; i < allElements.length; i++)
      {
        XMLFieldDescriptor desc = allElements[i];
        boolean isInherited = false;
        for (int idx = 0; idx < inheritedElements.length; idx++) {
          if (inheritedElements[idx].equals(desc))
          {
            isInherited = true;
            break;
          }
        }
        if (!isInherited)
        {
          localElements[localIdx] = desc;
          localIdx++;
        }
      }
      XMLFieldDescriptor[] inheritedAttributes = this._extends.getAttributeDescriptors();
      XMLFieldDescriptor[] allAttributes = localAttributes;
      localAttributes = new XMLFieldDescriptor[allAttributes.length - inheritedAttributes.length];
      
      localIdx = 0;
      for (int i = 0; i < allAttributes.length; i++)
      {
        XMLFieldDescriptor desc = allAttributes[i];
        boolean isInherited = false;
        for (int idx = 0; idx < inheritedAttributes.length; idx++) {
          if (inheritedAttributes[idx].equals(desc))
          {
            isInherited = true;
            break;
          }
        }
        if (!isInherited)
        {
          localAttributes[localIdx] = desc;
          localIdx++;
        }
      }
    }
    switch (this._compositor)
    {
    case 1: 
      boolean found = false;
      boolean hasLocalDescs = localElements.length > 0;
      XMLFieldDescriptor fieldDesc = null;
      for (int i = 0; i < localElements.length; i++)
      {
        XMLFieldDescriptor desc = localElements[i];
        if (desc != null)
        {
          FieldHandler handler = desc.getHandler();
          if (handler.getValue(object) != null) {
            if (desc.isMultivalued())
            {
              Object temp = handler.getValue(object);
              if (Array.getLength(temp) == 0) {
                temp = null;
              } else {
                temp = null;
              }
            }
            else
            {
              if (found)
              {
                String err = null;
                if (desc.isContainer())
                {
                  err = "The group '" + desc.getFieldName();
                  err = err + "' cannot exist at the same time that ";
                  if (fieldDesc.isContainer()) {
                    err = err + "the group '" + fieldDesc.getFieldName();
                  } else {
                    err = err + "the element '" + fieldDesc.getXMLName();
                  }
                  err = err + "' also exists.";
                }
                else
                {
                  err = "The element '" + desc.getXMLName();
                  err = err + "' cannot exist at the same time that ";
                  err = err + "element '" + fieldDesc.getXMLName() + "' also exists.";
                }
                throw new ValidationException(err);
              }
              found = true;
              fieldDesc = desc;
              
              FieldValidator fieldValidator = desc.getValidator();
              if (fieldValidator != null) {
                fieldValidator.validate(object, context);
              }
            }
          }
        }
      }
      if ((!found) && (hasLocalDescs))
      {
        StringBuffer buffer = new StringBuffer(40);
        boolean existsOptionalElement = false;
        buffer.append('(');
        String sep = " | ";
        for (int i = 0; i < localElements.length; i++)
        {
          XMLFieldDescriptor desc = localElements[i];
          if (desc != null)
          {
            FieldValidator fieldValidator = desc.getValidator();
            if (fieldValidator.getMinOccurs() == 0)
            {
              existsOptionalElement = true;
              break;
            }
            buffer.append(sep);
            buffer.append(desc.getXMLName());
          }
        }
        buffer.append(')');
        if (!existsOptionalElement)
        {
          String err = "In the choice contained in <" + getXMLName() + ">, at least one of these elements must appear:\n" + buffer.toString();
          
          throw new ValidationException(err);
        }
      }
      for (int i = 0; i < localAttributes.length; i++) {
        validateField(object, context, localAttributes[i]);
      }
      if (this._contentDescriptor != null) {
        validateField(object, context, this._contentDescriptor);
      }
      break;
    case 2: 
    default: 
      for (int i = 0; i < localElements.length; i++)
      {
        XMLFieldDescriptor fieldDescriptor = localElements[i];
        if (fieldDescriptor != null) {
          validateField(object, context, fieldDescriptor);
        }
      }
      for (int i = 0; i < localAttributes.length; i++) {
        validateField(object, context, localAttributes[i]);
      }
      if (this._contentDescriptor != null) {
        validateField(object, context, this._contentDescriptor);
      }
      break;
    }
  }
  
  private void validateField(Object object, ValidationContext context, XMLFieldDescriptor fieldDescriptor)
    throws ValidationException
  {
    FieldValidator fieldValidator = fieldDescriptor.getValidator();
    if (fieldValidator != null) {
      try
      {
        fieldValidator.validate(object, context);
      }
      catch (ValidationException e)
      {
        if ((fieldDescriptor.getNodeType() == NodeType.Attribute) || (fieldDescriptor.getNodeType() == NodeType.Element)) {
          addLocationInformation(fieldDescriptor, e);
        }
        throw e;
      }
    }
  }
  
  private void addLocationInformation(XMLFieldDescriptor localElement, ValidationException e)
  {
    XPathLocation loc = (XPathLocation)e.getLocation();
    if (loc == null)
    {
      loc = new XPathLocation();
      e.setLocation(loc);
      if (localElement.getNodeType() == NodeType.Attribute) {
        loc.addAttribute(localElement.getXMLName());
      } else {
        loc.addChild(localElement.getXMLName());
      }
    }
  }
  
  public Class getJavaClass()
  {
    return this._class;
  }
  
  public FieldDescriptor[] getFields()
  {
    int size = this._attributes.size();
    size += this._elements.size();
    if (this._contentDescriptor != null) {
      size++;
    }
    XMLFieldDescriptor[] fields = new XMLFieldDescriptor[size];
    
    this._attributes.toArray(fields);
    this._elements.toArray(fields, this._attributes.size());
    if (this._contentDescriptor != null) {
      fields[(size - 1)] = this._contentDescriptor;
    }
    return fields;
  }
  
  public ClassDescriptor getExtends()
  {
    return this._extends;
  }
  
  public FieldDescriptor getIdentity()
  {
    return this._identity;
  }
  
  public AccessMode getAccessMode()
  {
    return this._accessMode;
  }
  
  public boolean canAccept(String name, String namespace, Object object)
  {
    boolean result = false;
    boolean hasValue = false;
    XMLFieldDescriptor[] fields = null;
    int i = 0;
    
    XMLFieldDescriptor fieldDesc = getFieldDescriptor(name, namespace, NodeType.Element);
    if (fieldDesc == null) {
      fieldDesc = getFieldDescriptor(name, namespace, NodeType.Attribute);
    }
    if (fieldDesc == null) {
      return false;
    }
    if (fieldDesc.isMultivalued())
    {
      FieldValidator validator = fieldDesc.getValidator();
      if (validator != null)
      {
        if (validator.getMaxOccurs() < 0)
        {
          result = true;
        }
        else
        {
          Object tempObject = fieldDesc.getHandler().getValue(object);
          int current = Array.getLength(tempObject);
          int newTotal = current + 1;
          result = newTotal <= validator.getMaxOccurs();
        }
      }
      else {
        result = true;
      }
    }
    else if (fieldDesc.isContainer())
    {
      Object tempObject = fieldDesc.getHandler().getValue(object);
      if (tempObject == null) {
        result = true;
      } else {
        result = ((XMLClassDescriptor)fieldDesc.getClassDescriptor()).canAccept(name, namespace, tempObject);
      }
    }
    else
    {
      FieldHandler handler = fieldDesc.getHandler();
      
      boolean checkPrimitiveValue = true;
      if ((handler instanceof AbstractFieldHandler)) {
        hasValue = ((AbstractFieldHandler)handler).hasValue(object);
      } else {
        hasValue = handler.getValue(object) != null;
      }
      if ((hasValue) && (checkPrimitiveValue) && (fieldDesc.getFieldType().isPrimitive())) {
        if (isDefaultPrimitiveValue(handler.getValue(object))) {
          hasValue = false;
        }
      }
      result = !hasValue;
    }
    if ((result) && (this._compositor == 1) && (fieldDesc.getNodeType() == NodeType.Element))
    {
      fields = getElementArray();
      i = 0;
      while ((result) && (i < fields.length))
      {
        XMLFieldDescriptor desc = fields[i];
        if ((desc != fieldDesc) && (object != null))
        {
          Object tempObj = desc.getHandler().getValue(object);
          hasValue = tempObj != null;
          if (hasValue)
          {
            result = false;
            if (tempObj.getClass().isArray()) {
              result = Array.getLength(tempObj) == 0;
            }
            if ((tempObj instanceof Collection)) {
              result = ((Collection)tempObj).isEmpty();
            }
          }
        }
        i++;
      }
    }
    return result;
  }
  
  static boolean isPrimitive(Class type)
  {
    if (type == null) {
      return false;
    }
    if (type.isPrimitive()) {
      return true;
    }
    if ((type == Boolean.class) || (type == Character.class)) {
      return true;
    }
    return type.getSuperclass() == Number.class;
  }
  
  static boolean isDefaultPrimitiveValue(Object value)
  {
    if (value == null) {
      return false;
    }
    Class type = value.getClass();
    if (type.isPrimitive())
    {
      try
      {
        return value.equals(type.newInstance());
      }
      catch (IllegalAccessException iax) {}catch (InstantiationException ix) {}
    }
    else
    {
      if (type.getSuperclass() == Number.class) {
        return ((Number)value).intValue() == 0;
      }
      if (type == Boolean.class) {
        return value.equals(Boolean.FALSE);
      }
      if (type == Character.class) {
        return ((Character)value).charValue() == 0;
      }
    }
    return false;
  }
  
  public void setJavaClass(Class type)
  {
    this._class = type;
  }
  
  protected void setExtendsWithoutFlatten(XMLClassDescriptor classDesc)
  {
    this._extends = classDesc;
  }
  
  protected void setIntrospected(boolean introspected)
  {
    this._introspected = introspected;
  }
  
  private void addFieldDescriptor(XMLFieldDescriptor descriptor, boolean relink)
  {
    if (descriptor == null) {
      return;
    }
    boolean added = false;
    
    NodeType nodeType = descriptor.getNodeType();
    switch (nodeType.getType())
    {
    case 0: 
    case 2: 
      added = this._attributes.add(descriptor);
      if (added) {
        this._attArray = null;
      }
      break;
    case 3: 
      this._contentDescriptor = descriptor;
      added = true;
      break;
    case 1: 
    default: 
      added = this._elements.add(descriptor);
      if (added)
      {
        this._elemArray = null;
        if (descriptor.isContainer()) {
          this._containerCount += 1;
        }
      }
      break;
    }
    if ((added) && (relink)) {
      descriptor.setContainingClassDescriptor(this);
    }
  }
  
  private XMLFieldDescriptor[] getAttributeArray()
  {
    XMLFieldDescriptor[] descriptors = this._attArray;
    if (descriptors == null)
    {
      descriptors = this._attributes.toArray();
      this._attArray = descriptors;
    }
    return descriptors;
  }
  
  private XMLFieldDescriptor[] getElementArray()
  {
    XMLFieldDescriptor[] descriptors = this._elemArray;
    if (descriptors == null)
    {
      descriptors = this._elements.toArray();
      this._elemArray = descriptors;
    }
    return descriptors;
  }
  
  protected void addSequenceElement(XMLFieldDescriptor element)
  {
    this._sequenceOfElements.add(element);
  }
  
  public List getSubstitutes()
  {
    return this._substitutes;
  }
  
  public void setSubstitutes(List substitutes)
  {
    this._substitutes = substitutes;
  }
  
  public boolean isChoice()
  {
    return this._compositor == 1;
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
