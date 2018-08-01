package org.exolab.xml.util;

import java.util.ArrayList;
import java.util.List;

public class AttributeSetImpl
  implements AttributeSet
{
  public static final String XMLNS = "xmlns";
  private static final String EMPTY_STRING = "";
  private List _attributes = null;
  
  public AttributeSetImpl()
  {
    this._attributes = new ArrayList();
  }
  
  public AttributeSetImpl(int size)
  {
    if (size < 0)
    {
      String err = "size cannot be less than zero";
      throw new IllegalArgumentException(err);
    }
    this._attributes = new ArrayList(size);
  }
  
  public void clear()
  {
    this._attributes.clear();
  }
  
  public int getIndex(String name, String namespace)
  {
    if (namespace == null) {
      namespace = "";
    }
    for (int i = 0; i < this._attributes.size(); i++)
    {
      Attribute attr = (Attribute)this._attributes.get(i);
      if ((namespace.equals(attr.namespace)) && 
        (attr.name.equals(name))) {
        return i;
      }
    }
    return -1;
  }
  
  public String getName(int index)
  {
    Attribute attr = (Attribute)this._attributes.get(index);
    return attr.name;
  }
  
  public String getNamespace(int index)
  {
    Attribute attr = (Attribute)this._attributes.get(index);
    return attr.namespace;
  }
  
  public int getSize()
  {
    return this._attributes.size();
  }
  
  public String getValue(int index)
  {
    Attribute attr = (Attribute)this._attributes.get(index);
    return attr.value;
  }
  
  public String getValue(String name)
  {
    if (name == null) {
      return null;
    }
    Attribute attr = getAttribute(name, "");
    if (attr != null) {
      return attr.value;
    }
    return null;
  }
  
  public String getValue(String name, String namespace)
  {
    if (name == null) {
      return null;
    }
    Attribute attr = getAttribute(name, namespace);
    if (attr != null) {
      return attr.value;
    }
    return null;
  }
  
  public void setAttribute(String name, String value)
  {
    setAttribute(name, value, "");
  }
  
  public void setAttribute(String name, String value, String namespace)
  {
    if ((name == null) || (name.length() == 0)) {
      throw new IllegalArgumentException("name must not be null");
    }
    if ("xmlns".equals(name))
    {
      String err = "'xmlns' is a reserved word for use with XML namespace declarations. It may not be used as an attribute name.";
      
      throw new IllegalArgumentException(err);
    }
    if (namespace == null) {
      namespace = "";
    }
    Attribute attr = getAttribute(name, namespace);
    if (attr == null) {
      this._attributes.add(new Attribute(name, value, namespace));
    } else {
      attr.value = value;
    }
  }
  
  private Attribute getAttribute(String name, String namespace)
  {
    if (namespace == null) {
      namespace = "";
    }
    for (int i = 0; i < this._attributes.size(); i++)
    {
      Attribute attr = (Attribute)this._attributes.get(i);
      if ((namespace.equals(attr.namespace)) && 
        (attr.name.equals(name))) {
        return attr;
      }
    }
    return null;
  }
  
  class Attribute
  {
    String name = null;
    String value = null;
    String namespace = null;
    
    public Attribute() {}
    
    public Attribute(String name, String value, String namespace)
    {
      this.name = name;
      this.value = value;
      this.namespace = namespace;
    }
  }
}
