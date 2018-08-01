package org.exolab.xml;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exec.mapping.BindingType;
import org.exec.xml.InternalContext;
import org.exec.xml.JavaNaming;
import org.exec.xml.XMLNaming;
import org.exolab.mapping.AbstractFieldHandler;
import org.exolab.mapping.ClassDescriptor;
import org.exolab.mapping.CollectionHandler;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.FieldHandler;
import org.exolab.mapping.MapItem;
import org.exolab.mapping.MappingException;
import org.exolab.mapping.TypeConvertor;
import org.exolab.mapping.loader.AbstractMappingLoader;
import org.exolab.mapping.loader.CollectionHandlers;
import org.exolab.mapping.loader.FieldDescriptorImpl;
import org.exolab.mapping.loader.FieldHandlerImpl;
import org.exolab.mapping.loader.TypeInfo;
import org.exolab.mapping.loader.Types;
import org.exolab.mapping.xml.BindXml;
import org.exolab.mapping.xml.ClassChoice;
import org.exolab.mapping.xml.ClassMapping;
import org.exolab.mapping.xml.FieldMapping;
import org.exolab.mapping.xml.MapTo;
import org.exolab.mapping.xml.MappingRoot;
import org.exolab.mapping.xml.Property;
import org.exolab.mapping.xml.types.BindXmlAutoNamingType;
import org.exolab.mapping.xml.types.BindXmlNodeType;
import org.exolab.mapping.xml.types.FieldMappingCollectionType;
import org.exolab.xml.handlers.ContainerFieldHandler;
import org.exolab.xml.handlers.ToStringFieldHandler;
import org.exolab.xml.util.ContainerElement;
import org.exolab.xml.util.XMLClassDescriptorAdapter;
import org.exolab.xml.util.XMLClassDescriptorImpl;
import org.exolab.xml.util.XMLContainerElementFieldDescriptor;
import org.exolab.xml.util.XMLFieldDescriptorImpl;
import org.exolab.xml.validators.IdRefValidator;
import org.exolab.xml.validators.NameValidator;

public final class XMLMappingLoader extends AbstractMappingLoader {
  private static final Log LOG = LogFactory.getLog(XMLMappingLoader.class);
  private static final String XML_PREFIX = "xml:";
  private static final Class[] EMPTY_ARGS = new Class[0];
  private static final String NCNAME = "NCName";
  private static final Class[] STRING_ARG = { String.class };
  private static final String VALUE_OF = "valueOf";
  public XMLMappingLoader(ClassLoader loader)
  {
    super(loader);
  }
  public BindingType getBindingType()
  {
    return BindingType.XML;
  }
  public void loadMapping(MappingRoot mapping, Object param) throws MappingException {
    if (loadMapping())
    {
      createFieldHandlers(mapping);
      createClassDescriptors(mapping);
    }
  }
  protected ClassDescriptor createClassDescriptor(ClassMapping classMapping) throws MappingException {
    if ((getInternalContext() == null) || (getInternalContext().getXMLClassDescriptorResolver() == null))
    {
      String message = "Internal context or class descriptor resolver within are not valid";
      LOG.warn(message);
      throw new IllegalStateException(message);
    }
    XMLClassDescriptorAdapter xmlClassDesc = new XMLClassDescriptorAdapter();
    
    getInternalContext().getXMLClassDescriptorResolver().setUseIntrospection(false);
    getInternalContext().getXMLClassDescriptorResolver().setLoadPackageMappings(false);
    try
    {
      if ((classMapping.getAutoComplete()) && 
        (classMapping.getMapTo() == null) && ((classMapping.getClassChoice() == null) || (classMapping.getClassChoice().getFieldMappingCount() == 0)) && (classMapping.getIdentityCount() == 0)) {
        try
        {
          ClassDescriptor clsDesc = getInternalContext().getXMLClassDescriptorResolver().resolve(classMapping.getName());
          if (clsDesc != null) {
            return clsDesc;
          }
        }
        catch (ResolverException e)
        {
          if (LOG.isDebugEnabled())
          {
            String message = "Ignoring exception: " + e + " at resolving: " + classMapping.getName();
            
            LOG.debug(message);
          }
        }
      }
      Class javaClass = resolveType(classMapping.getName());
      if ((classMapping.getVerifyConstructable()) && 
        (!Types.isConstructable(javaClass, true))) {
        throw new MappingException("mapping.classNotConstructable", javaClass.getName());
      }
      xmlClassDesc.setJavaClass(javaClass);
      
      MapTo mapTo = classMapping.getMapTo();
      String xmlName;
      if ((mapTo != null) && (mapTo.getXml() != null))
      {
        xmlName = mapTo.getXml();
      }
      else
      {
        String clsName = getInternalContext().getJavaNaming().getClassName(javaClass);
        xmlName = getInternalContext().getXMLNaming().toXMLName(clsName);
      }
      xmlClassDesc.setXMLName(xmlName);
      
      ClassDescriptor extDesc = getExtended(classMapping, javaClass);
      xmlClassDesc.setExtends((XMLClassDescriptor)extDesc);
      
      FieldDescriptorImpl[] allFields = createFieldDescriptors(classMapping, javaClass);
      
      checkFieldNameDuplicates(allFields, javaClass);
      
      List fieldList = new ArrayList(allFields.length);
      List idList = new ArrayList();
      if (extDesc == null)
      {
        for (int i = 0; i < allFields.length; i++) {
          if (!allFields[i].isIdentity()) {
            fieldList.add(allFields[i]);
          } else {
            idList.add(allFields[i]);
          }
        }
        if (idList.size() == 0)
        {
          String[] idNames = classMapping.getIdentity();
          for (int i = 0; i < idNames.length; i++)
          {
            FieldDescriptor identity = findIdentityByName(fieldList, idNames[i], javaClass);
            if (identity != null) {
              idList.add(identity);
            } else {
              throw new MappingException("mapping.identityMissing", idNames[i], javaClass.getName());
            }
          }
        }
      }
      else
      {
        for (int i = 0; i < allFields.length; i++) {
          fieldList.add(allFields[i]);
        }
        if (extDesc.getIdentity() != null) {
          idList.add(extDesc.getIdentity());
        }
        for (int i = 0; i < idList.size(); i++)
        {
          String idname = ((FieldDescriptor)idList.get(i)).getFieldName();
          FieldDescriptor identity = findIdentityByName(fieldList, idname, javaClass);
          if (identity != null) {
            idList.set(i, identity);
          }
        }
      }
      FieldDescriptor xmlId = null;
      if (idList.size() != 0) {
        xmlId = (FieldDescriptor)idList.get(0);
      }
      if (xmlId != null) {
        xmlClassDesc.setIdentity((XMLFieldDescriptorImpl)xmlId);
      }
      for (int i = 0; i < fieldList.size(); i++)
      {
        FieldDescriptor fieldDesc = (FieldDescriptor)fieldList.get(i);
        if (fieldDesc != null) {
          xmlClassDesc.addFieldDescriptor((XMLFieldDescriptorImpl)fieldDesc);
        }
      }
      if (classMapping.getAutoComplete())
      {
        XMLClassDescriptor referenceDesc = null;
        
        Class type = xmlClassDesc.getJavaClass();
        if ((getInternalContext() == null) || (getInternalContext().getXMLClassDescriptorResolver() == null))
        {
          String message = "Internal context or class descriptor resolver within are not valid";
          LOG.warn(message);
          throw new IllegalStateException(message);
        }
        try
        {
          referenceDesc = (XMLClassDescriptor)getInternalContext().getXMLClassDescriptorResolver().resolve(type);
        }
        catch (ResolverException rx)
        {
          throw new MappingException(rx);
        }
        if (referenceDesc == null)
        {
          Introspector introspector = getInternalContext().getIntrospector();
          try
          {
            referenceDesc = introspector.generateClassDescriptor(type);
            if (classMapping.getExtends() != null) {
              ((XMLClassDescriptorImpl)referenceDesc).setExtends(null);
            }
          }
          catch (MarshalException mx)
          {
            String error = "unable to introspect class '" + type.getName() + "' for auto-complete: ";
            
            throw new MappingException(error + mx.getMessage());
          }
        }
        String identity = "";
        if (classMapping.getIdentityCount() > 0) {
          identity = classMapping.getIdentity(0);
        }
        FieldDescriptor[] xmlFields2 = xmlClassDesc.getFields();
        
        XMLFieldDescriptor[] introFields = referenceDesc.getAttributeDescriptors();
        for (int i = 0; i < introFields.length; i++) {
          if (!isMatchFieldName(xmlFields2, introFields[i].getFieldName())) {
            if (introFields[i].getFieldName().equals(identity)) {
              xmlClassDesc.setIdentity(introFields[i]);
            } else {
              xmlClassDesc.addFieldDescriptor(introFields[i]);
            }
          }
        }
        introFields = referenceDesc.getElementDescriptors();
        for (int i = 0; i < introFields.length; i++) {
          if (!isMatchFieldName(xmlFields2, introFields[i].getFieldName())) {
            if (introFields[i].getFieldName().equals(identity)) {
              xmlClassDesc.setIdentity(introFields[i]);
            } else {
              xmlClassDesc.addFieldDescriptor(introFields[i]);
            }
          }
        }
        XMLFieldDescriptor field = referenceDesc.getContentDescriptor();
        if ((field != null) && 
          (!isMatchFieldName(xmlFields2, field.getFieldName()))) {
          xmlClassDesc.addFieldDescriptor(field);
        }
      }
      if (mapTo != null)
      {
        xmlClassDesc.setNameSpacePrefix(mapTo.getNsPrefix());
        xmlClassDesc.setNameSpaceURI(mapTo.getNsUri());
        xmlClassDesc.setElementDefinition(mapTo.getElementDefinition());
      }
    }
    finally
    {
      getInternalContext().getXMLClassDescriptorResolver().setUseIntrospection(true);
      getInternalContext().getXMLClassDescriptorResolver().setLoadPackageMappings(true);
    }
    return xmlClassDesc;
  }
  
  protected final FieldDescriptor findIdentityByName(List fldList, String idName, Class javaClass)
  {
    for (int i = 0; i < fldList.size(); i++)
    {
      FieldDescriptor field = (FieldDescriptor)fldList.get(i);
      if (idName.equals(field.getFieldName()))
      {
        fldList.remove(i);
        return field;
      }
    }
    return null;
  }
  
  protected final void resolveRelations(ClassDescriptor clsDesc)
  {
    FieldDescriptor[] fields = clsDesc.getFields();
    for (int i = 0; i < fields.length; i++) {
      if (fields[i].getClassDescriptor() == null)
      {
        Class fieldType = fields[i].getFieldType();
        if (fieldType != null)
        {
          ClassDescriptor relDesc = getDescriptor(fieldType.getName());
          if ((relDesc != null) && ((relDesc instanceof XMLClassDescriptor)) && ((fields[i] instanceof XMLFieldDescriptorImpl))) {
            ((XMLFieldDescriptorImpl)fields[i]).setClassDescriptor(relDesc);
          }
        }
      }
    }
    if ((clsDesc instanceof XMLClassDescriptorImpl)) {
      ((XMLClassDescriptorImpl)clsDesc).sortDescriptors();
    }
  }
  
  private boolean isMatchFieldName(FieldDescriptor[] fields, String fieldName)
  {
    for (int i = 0; i < fields.length; i++) {
      if (fields[i].getFieldName().equals(fieldName)) {
        return true;
      }
    }
    return false;
  }
  
  protected FieldDescriptorImpl createFieldDesc(Class javaClass, FieldMapping fieldMap)
    throws MappingException
  {
    FieldMappingCollectionType colType = fieldMap.getCollection();
    String xmlName = null;
    NodeType nodeType = null;
    String match = null;
    
    boolean isReference = false;
    boolean isXMLTransient = false;
    if ((fieldMap.getType() == null) && (colType != null) && (
      (colType == FieldMappingCollectionType.HASHTABLE) || (colType == FieldMappingCollectionType.MAP) || (colType == FieldMappingCollectionType.SORTEDMAP))) {
      fieldMap.setType(MapItem.class.getName());
    }
    FieldDescriptor fieldDesc = super.createFieldDesc(javaClass, fieldMap);
    
    BindXml xml = fieldMap.getBindXml();
    
    boolean deriveNameByClass = false;
    if (xml != null)
    {
      xmlName = xml.getName();
      if (xml.getNode() != null) {
        nodeType = NodeType.getNodeType(xml.getNode().toString());
      }
      match = xml.getMatches();
      
      isReference = xml.getReference();
      
      isXMLTransient = xml.getTransient();
      
      BindXmlAutoNamingType autoName = xml.getAutoNaming();
      if (autoName != null) {
        deriveNameByClass = autoName == BindXmlAutoNamingType.DERIVEBYCLASS;
      }
    }
    isXMLTransient = (isXMLTransient) || (fieldDesc.isTransient());
    
    String namespace = null;
    if ((xmlName != null) && (xmlName.length() > 0)) {
      if (xmlName.charAt(0) == '{')
      {
        int idx = xmlName.indexOf('}');
        if (idx < 0) {
          throw new MappingException("Invalid QName: " + xmlName);
        }
        namespace = xmlName.substring(1, idx);
        xmlName = xmlName.substring(idx + 1);
      }
      else if (xmlName.startsWith("xml:"))
      {
        namespace = "http://www.w3.org/XML/1998/namespace";
        xmlName = xmlName.substring(4);
      }
    }
    if (nodeType == null) {
      if (isPrimitive(javaClass)) {
        nodeType = getInternalContext().getPrimitiveNodeType();
      } else {
        nodeType = NodeType.Element;
      }
    }
    if ((!deriveNameByClass) && (xmlName == null) && (match == null))
    {
      xmlName = getInternalContext().getXMLNaming().toXMLName(fieldDesc.getFieldName());
      match = xmlName + ' ' + fieldDesc.getFieldName();
    }
    XMLFieldDescriptorImpl xmlDesc = new XMLFieldDescriptorImpl(fieldDesc, xmlName, nodeType, getInternalContext().getPrimitiveNodeType());
    if ((xmlDesc.getHandler() != null) && ((xmlDesc.getHandler() instanceof AbstractFieldHandler)))
    {
      AbstractFieldHandler handler = (AbstractFieldHandler)xmlDesc.getHandler();
      handler.setFieldDescriptor(xmlDesc);
    }
    xmlDesc.setTransient(isXMLTransient);
    
    xmlDesc.setValidator(new FieldValidator());
    
    xmlDesc.setUseParentsNamespace(true);
    if (deriveNameByClass) {
      xmlDesc.setXMLName(null);
    }
    if (namespace != null) {
      xmlDesc.setNameSpaceURI(namespace);
    }
    if (match != null)
    {
      xmlDesc.setMatches(match);
      if (xmlName == null) {
        xmlDesc.setXMLName(null);
      }
    }
    xmlDesc.setReference(isReference);
    if ((isReference) && 
      (colType == null))
    {
      FieldValidator fieldValidator = new FieldValidator();
      fieldValidator.setValidator(new IdRefValidator());
      xmlDesc.setValidator(fieldValidator);
    }
    xmlDesc.setContainer(fieldMap.getContainer());
    
    xmlDesc.setNillable(fieldMap.isNillable());
    if (xml != null)
    {
      if (xml.getClassMapping() != null)
      {
        ClassDescriptor cd = createClassDescriptor(xml.getClassMapping());
        xmlDesc.setClassDescriptor(cd);
      }
      if (xml.getLocation() != null) {
        xmlDesc.setLocationPath(xml.getLocation());
      }
      String xmlType = xml.getType();
      xmlDesc.setSchemaType(xmlType);
      xmlDesc.setQNamePrefix(xml.getQNamePrefix());
      TypeValidator validator = null;
      if ("NCName".equals(xmlType))
      {
        validator = new NameValidator((short)0);
        xmlDesc.setValidator(new FieldValidator(validator));
      }
      Property[] props = xml.getProperty();
      if ((props != null) && (props.length > 0)) {
        for (int pIdx = 0; pIdx < props.length; pIdx++)
        {
          Property prop = props[pIdx];
          xmlDesc.setXMLProperty(prop.getName(), prop.getValue());
        }
      }
    }
    if (colType == null)
    {
      Class type = fieldDesc.getFieldType();
      if ((type != null) && (CollectionHandlers.hasHandler(type)))
      {
        String typeName = CollectionHandlers.getCollectionName(type);
        colType = FieldMappingCollectionType.valueOf(typeName);
      }
    }
    if (colType != null)
    {
      if ((colType == FieldMappingCollectionType.HASHTABLE) || (colType == FieldMappingCollectionType.MAP) || (colType == FieldMappingCollectionType.SORTEDMAP))
      {
        String methodName = fieldMap.getSetMethod();
        if (methodName != null)
        {
          if (!methodName.startsWith("add")) {
            xmlDesc.setMapped(true);
          }
        }
        else {
          xmlDesc.setMapped(true);
        }
      }
      if ((nodeType == NodeType.Namespace) || (xmlDesc.isMapped()))
      {
        Object handler = xmlDesc.getHandler();
        if ((handler instanceof FieldHandlerImpl))
        {
          FieldHandlerImpl handlerImpl = (FieldHandlerImpl)handler;
          handlerImpl.setConvertFrom(new IdentityConvertor());
        }
      }
      if ((nodeType == NodeType.Element) && 
        (fieldMap.hasContainer()) && (!fieldMap.getContainer())) {
        xmlDesc = wrapCollection(xmlDesc);
      }
    }
    else if ((!isReference) && (!isXMLTransient))
    {
      Class fieldType = xmlDesc.getFieldType();
      if (!isPrimitive(fieldType))
      {
        Constructor cons = null;
        try
        {
          cons = fieldType.getConstructor(EMPTY_ARGS);
          if (!Modifier.isPublic(cons.getModifiers())) {
            cons = null;
          }
        }
        catch (NoSuchMethodException nsmx) {}
        try
        {
          if (cons == null)
          {
            Method method = fieldType.getMethod("valueOf", STRING_ARG);
            Class returnType = method.getReturnType();
            if ((returnType != null) && (fieldType.isAssignableFrom(returnType)) && 
              (fieldMap.getHandler() == null))
            {
              FieldHandler handler = new ToStringFieldHandler(fieldType, xmlDesc.getHandler());
              
              xmlDesc.setHandler(handler);
              xmlDesc.setImmutable(true);
            }
          }
        }
        catch (NoSuchMethodException nsmx) {}
      }
    }
    String setter = fieldMap.getSetMethod();
    if ((setter != null) && (setter.startsWith("%")))
    {
      String parameterNumberAsString = setter.substring(1).trim();
      int index = Integer.parseInt(parameterNumberAsString);
      if (index < 1) {
        throw new MappingException("mapper.invalidParameterIndex", parameterNumberAsString);
      }
      xmlDesc.setConstructorArgumentIndex(--index);
    }
    return xmlDesc;
  }
  
  public void setLoadPackageMappings(boolean loadPackageMappings)
  {
    if ((getInternalContext() == null) || (getInternalContext().getXMLClassDescriptorResolver() == null))
    {
      String message = "Internal context or class descriptor resolver within are not valid";
      LOG.warn(message);
      throw new IllegalStateException(message);
    }
    getInternalContext().getXMLClassDescriptorResolver().setLoadPackageMappings(loadPackageMappings);
  }
  
  protected TypeInfo getTypeInfo(Class fieldType, CollectionHandler colHandler, FieldMapping fieldMap)
    throws MappingException
  {
    return new TypeInfo(fieldType, null, null, fieldMap.getRequired(), null, colHandler, false);
  }
  
  private XMLFieldDescriptorImpl wrapCollection(XMLFieldDescriptorImpl fieldDesc)
    throws MappingException
  {
    Class type = ContainerElement.class;
    XMLClassDescriptorImpl classDesc = new XMLClassDescriptorImpl(type);
    
    XMLFieldDescriptorImpl newFieldDesc = new XMLFieldDescriptorImpl(fieldDesc, fieldDesc.getXMLName(), fieldDesc.getNodeType(), getInternalContext().getPrimitiveNodeType());
    
    newFieldDesc.setXMLName(null);
    newFieldDesc.setMatches("*");
    
    classDesc.addFieldDescriptor(newFieldDesc);
    
    fieldDesc.setClassDescriptor(classDesc);
    
    FieldHandler handler = new ContainerFieldHandler(fieldDesc.getHandler());
    newFieldDesc.setHandler(handler);
    fieldDesc.setHandler(handler);
    
    return new XMLContainerElementFieldDescriptor(fieldDesc, getInternalContext().getPrimitiveNodeType());
  }
  
  class IdentityConvertor
    implements TypeConvertor
  {
    IdentityConvertor() {}
    
    public Object convert(Object object)
    {
      return object;
    }
  }
}
