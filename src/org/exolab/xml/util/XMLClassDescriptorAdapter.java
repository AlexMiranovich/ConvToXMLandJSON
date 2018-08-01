package org.exolab.xml.util;

import org.exec.xml.BackwardCompatibilityContext;
import org.exec.xml.InternalContext;
import org.exec.xml.JavaNaming;
import org.exec.xml.XMLNaming;
import org.exolab.mapping.ClassDescriptor;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.MappingException;
import org.exolab.mapping.loader.ClassDescriptorImpl;
import org.exolab.xml.NodeType;
import org.exolab.xml.XMLClassDescriptor;
import org.exolab.xml.XMLFieldDescriptor;

public class XMLClassDescriptorAdapter extends XMLClassDescriptorImpl {
  private InternalContext _internalContext;
  public XMLClassDescriptorAdapter()
  {
    this._internalContext = new BackwardCompatibilityContext();
  }
  public XMLClassDescriptorAdapter(ClassDescriptor classDesc, String xmlName)
    throws MappingException {
    this(classDesc, xmlName, null);
  }
  public XMLClassDescriptorAdapter(ClassDescriptor classDesc, String xmlName, NodeType primitiveNodeType)
    throws MappingException {
    this();
    if (classDesc == null) {
      String err = "The ClassDescriptor argument to XMLClassDescriptorAdapter must not be null.";
      throw new IllegalArgumentException(err);
    }
    if (primitiveNodeType == null) {
      primitiveNodeType = new BackwardCompatibilityContext().getPrimitiveNodeType();
    }
    if (primitiveNodeType == null) {
      primitiveNodeType = NodeType.Attribute;
    }
    process(classDesc, primitiveNodeType);
    setJavaClass(classDesc.getJavaClass());
    if (xmlName == null) {
      if ((classDesc instanceof XMLClassDescriptor)) {
        xmlName = ((XMLClassDescriptor)classDesc).getXMLName();
      }
      else {
        String clsName = this._internalContext.getJavaNaming().getClassName(classDesc.getJavaClass());
        xmlName = this._internalContext.getXMLNaming().toXMLName(clsName);
      }
    }
    setXMLName(xmlName);
  }
  private void process(ClassDescriptor classDesc, NodeType primitiveNodeType)
    throws MappingException {
    if ((classDesc instanceof XMLClassDescriptor)) {
      process((XMLClassDescriptor)classDesc);
      return;
    }
    XMLClassDescriptor xmlClassDesc = null;
    ClassDescriptor extendsDesc = classDesc.getExtends();
    if (extendsDesc != null) {
      if ((extendsDesc instanceof XMLClassDescriptor)) {
        xmlClassDesc = (XMLClassDescriptor)extendsDesc;
      } else {
        xmlClassDesc = new XMLClassDescriptorAdapter(extendsDesc, null, primitiveNodeType);
      }
    }
    setExtends(xmlClassDesc);
    FieldDescriptor identity = classDesc.getIdentity();
    FieldDescriptor[] fields = classDesc.getFields();
    if ((classDesc instanceof ClassDescriptorImpl)) {
      ClassDescriptorImpl cdImpl = (ClassDescriptorImpl)classDesc;
      FieldDescriptor[] identities = cdImpl.getIdentities();
      if ((identities != null) && (identities.length > 1)) {
        int size = fields.length + identities.length;
        FieldDescriptor[] newFields = new FieldDescriptor[size];
        System.arraycopy(fields, 0, newFields, 0, fields.length);
        System.arraycopy(identities, 0, newFields, fields.length, identities.length);
        fields = newFields;
      }
    }
    for (int i = 0; i < fields.length; i++) {
      FieldDescriptor fieldDesc = fields[i];
      if (fieldDesc != null) {
        if ((fieldDesc instanceof XMLFieldDescriptorImpl)) {
          if (identity == fieldDesc) {
            setIdentity((XMLFieldDescriptorImpl)fieldDesc);
            identity = null;
          }
          else {
            addFieldDescriptor((XMLFieldDescriptorImpl)fieldDesc);
          }
        }
        else {
          String name = fieldDesc.getFieldName();
          String xmlFieldName = this._internalContext.getXMLNaming().toXMLName(name);
          if (identity == fieldDesc) {
            setIdentity(new XMLFieldDescriptorImpl(fieldDesc, xmlFieldName, NodeType.Attribute, primitiveNodeType));
            
            identity = null;
          }
          else {
            NodeType nodeType = NodeType.Element;
            if (isPrimitive(fieldDesc.getFieldType())) {
              nodeType = primitiveNodeType;
            }
            addFieldDescriptor(new XMLFieldDescriptorImpl(fieldDesc, xmlFieldName, nodeType, primitiveNodeType));
          }
        }
      }
    }
    if (identity != null) {
      if ((identity instanceof XMLFieldDescriptor)) {
        setIdentity((XMLFieldDescriptor)identity);
      }
      else {
        String xmlFieldName = this._internalContext.getXMLNaming().toXMLName(identity.getFieldName());
        setIdentity(new XMLFieldDescriptorImpl(identity, xmlFieldName, NodeType.Attribute, primitiveNodeType));
      }
    }
  }
  private void process(XMLClassDescriptor classDesc) {
    FieldDescriptor identity = classDesc.getIdentity();
    FieldDescriptor[] fields = classDesc.getFields();
    for (int i = 0; i < fields.length; i++) {
      if (identity == fields[i]) {
        setIdentity((XMLFieldDescriptor)fields[i]);
        identity = null;
      }
      else {
        addFieldDescriptor((XMLFieldDescriptor)fields[i]);
      }
    }
    if (identity != null) {
      setIdentity((XMLFieldDescriptor)identity);
    }
    setXMLName(classDesc.getXMLName());
    setExtendsWithoutFlatten((XMLClassDescriptor)classDesc.getExtends());
  }
}
