package org.exolab.mapping.loader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.exolab.mapping.ClassDescriptor;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.xml.ClassMapping;

public class ClassDescriptorImpl implements ClassDescriptor {
  private ClassMapping _mapping;
  private Class _javaClass;
  private ClassDescriptor _extends;
  private ClassDescriptor _depends;
  private FieldDescriptor[] _fields;
  private Map _properties = new HashMap();
  private Set _natures = new HashSet();
  private FieldDescriptor[] _identities;
  public void setMapping(ClassMapping mapping)
  {
    this._mapping = mapping;
  }
  public ClassMapping getMapping()
  {
    return this._mapping;
  }
  public void setJavaClass(Class javaClass)
  {
    this._javaClass = javaClass;
  }
  public Class getJavaClass()
  {
    return this._javaClass;
  }
  public void setExtends(ClassDescriptor extend)
  {
    this._extends = extend;
  }
  public ClassDescriptor getExtends()
  {
    return this._extends;
  }
  public void setDepends(ClassDescriptor depends)
  {
    this._depends = depends;
  }
  public ClassDescriptor getDepends()
  {
    return this._depends;
  }
  public void setFields(FieldDescriptor[] fields)
  {
    this._fields = fields;
  }
  public FieldDescriptor[] getFields()
  {
    return this._fields;
  }
  public String toString()
  {
    return this._javaClass.getName() + "[" + this._natures.toString() + "]";
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
  public void setIdentities(FieldDescriptor[] identities)
  {
    this._identities = identities;
  }
  public FieldDescriptor[] getIdentities()
  {
    return this._identities;
  }
  public FieldDescriptor getIdentity() {
    FieldDescriptor[] identities = getIdentities();
    if (identities == null) {
      return null;
    }
    return identities[0];
  }
}
