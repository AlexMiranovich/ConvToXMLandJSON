package org.exolab.xml.descriptors;

import java.util.ArrayList;
import java.util.List;
import org.exolab.mapping.AccessMode;
import org.exolab.mapping.ClassDescriptor;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.util.IteratorEnumeration;
import org.exolab.xml.NodeType;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.util.XMLFieldDescriptorImpl;

public class ListClassDescriptor extends BaseDescriptor {
  private static final XMLFieldDescriptor[] NO_ATTRIBUTES = new XMLFieldDescriptor[0];
  private static final XMLFieldDescriptorImpl NO_CONTENT = null;
  private final XMLFieldDescriptor[] _elements;
  private final FieldDescriptor[] _fields;
  private final XMLFieldDescriptorImpl _desc = null;
  private String _xmlName = null;
  private String _nsURI = null;
  private TypeValidator _validator = null;
  public ListClassDescriptor()
  {
    this(null);
  }
  public ListClassDescriptor(String xmlName) {
    this._xmlName = xmlName;
    XMLFieldDescriptorImpl desc = new XMLFieldDescriptorImpl(Object.class, "item", this._xmlName, NodeType.Element);
    desc.setMultivalued(true);
    desc.setMatches("*");
    desc.setHandler(new XMLFieldHandler() {
      public Object getValue(Object object) throws IllegalStateException {
        List list = (List)object;
        return new IteratorEnumeration(list.iterator());
      }
      public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {
        try {
          ((List)object).add(value);
        }
        catch (Exception ex) {
          throw new IllegalStateException(ex.toString());
        }
      }
      public Object newInstance(Object parent)
      {
        return new ArrayList();
      }
    });
    this._fields = new FieldDescriptor[1];
    this._fields[0] = desc;
    this._elements = new XMLFieldDescriptor[1];
    this._elements[0] = desc;
  }
  public XMLFieldDescriptor[] getAttributeDescriptors()
  {
    return NO_ATTRIBUTES;
  }
  public Class getJavaClass()
  {
    return List.class;
  }
  public XMLFieldDescriptor[] getElementDescriptors()
  {
    return this._elements;
  }
  public ClassDescriptor getExtends()
  {
    return null;
  }
  public FieldDescriptor[] getFields()
  {
    return this._fields;
  }
  public XMLFieldDescriptor getContentDescriptor()
  {
    return NO_CONTENT;
  }
  public XMLFieldDescriptor getFieldDescriptor(String name, String namespace, NodeType nodeType) {
    if ((nodeType == null) || (nodeType == NodeType.Element)) {
      for (int i = 0; i < this._elements.length; i++) {
        XMLFieldDescriptor desc = this._elements[i];
        if ((desc != null) && (desc.matches(name))) {
          return desc;
        }
      }
    }
    return null;
  }
  public String getNameSpacePrefix()
  {
    return null;
  }
  public String getNameSpaceURI()
  {
    return this._nsURI;
  }
  public FieldDescriptor getIdentity()
  {
    return null;
  }
  public AccessMode getAccessMode()
  {
    return null;
  }
  public TypeValidator getValidator()
  {
    return this._validator;
  }
  public String getXMLName()
  {
    return this._xmlName;
  }
  public void setValidator(TypeValidator validator)
  {
    this._validator = validator;
  }
  public void setXMLName(String xmlName) {
    if ((xmlName != null) && (xmlName.length() > 0))
    {
      this._xmlName = xmlName;
      this._desc.setXMLName(xmlName);
    }
  }
  public void setNameSpaceURI(String nsURI)
  {
    this._nsURI = nsURI;
  }
  public boolean canAccept(String name, String namespace, Object object)
  {
    return true;
  }
}
