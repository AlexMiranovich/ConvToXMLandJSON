package org.exolab.mapping.xml;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import org.exolab.mapping.xml.types.BindXmlAutoNamingType;
import org.exolab.mapping.xml.types.BindXmlNodeType;
import org.exolab.xml.MarshalException;
import org.exolab.xml.Marshaller;
import org.exolab.xml.Unmarshaller;
import org.exolab.xml.ValidationException;
import org.exolab.xml.Validator;
import org.xml.sax.ContentHandler;

public class BindXml implements Serializable {
  private String _name;
  private String _type;
  private BindXmlAutoNamingType _autoNaming;
  private String _location;
  private String _matches;
  private boolean _reference;
  private boolean _has_reference;
  private BindXmlNodeType _node;
  private String _QNamePrefix;
  private boolean _transient;
  private boolean _has_transient;
  private ClassMapping _classMapping;
  private List<Property> _propertyList;
  
  public BindXml()
  {
    this._propertyList = new ArrayList();
  }
  
  public void addProperty(Property vProperty)
    throws IndexOutOfBoundsException
  {
    this._propertyList.add(vProperty);
  }
  
  public void addProperty(int index, Property vProperty)
    throws IndexOutOfBoundsException
  {
    this._propertyList.add(index, vProperty);
  }
  
  public void deleteReference()
  {
    this._has_reference = false;
  }
  public void deleteTransient()
  {
    this._has_transient = false;
  }
  public Enumeration<? extends Property> enumerateProperty()
  {
    return Collections.enumeration(this._propertyList);
  }
  public BindXmlAutoNamingType getAutoNaming()
  {
    return this._autoNaming;
  }
  public ClassMapping getClassMapping()
  {
    return this._classMapping;
  }
  public String getLocation()
  {
    return this._location;
  }
  public String getMatches()
  {
    return this._matches;
  }
  public String getName()
  {
    return this._name;
  }
  public BindXmlNodeType getNode()
  {
    return this._node;
  }
  public Property getProperty(int index)
    throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._propertyList.size())) {
      throw new IndexOutOfBoundsException("getProperty: Index value '" + index + "' not in range [0.." + (this._propertyList.size() - 1) + "]");
    }
    return (Property)this._propertyList.get(index);
  }
  public Property[] getProperty() {
    Property[] array = new Property[0];
    return (Property[])this._propertyList.toArray(array);
  }
  public int getPropertyCount()
  {
    return this._propertyList.size();
  }
  public String getQNamePrefix()
  {
    return this._QNamePrefix;
  }
  public boolean getReference()
  {
    return this._reference;
  }
  public boolean getTransient()
  {
    return this._transient;
  }
  public String getType()
  {
    return this._type;
  }
  public boolean hasReference()
  {
    return this._has_reference;
  }
  public boolean hasTransient()
  {
    return this._has_transient;
  }
  public boolean isReference()
  {
    return this._reference;
  }
  public boolean isTransient()
  {
    return this._transient;
  }
  public boolean isValid() {
    try {
      validate();
    }
    catch (ValidationException vex) {
      return false;
    }
    return true;
  }
  public Iterator<? extends Property> iterateProperty()
  {
    return this._propertyList.iterator();
  }
  public void marshal(Writer out)
    throws MarshalException, ValidationException {
    Marshaller.marshal(this, out);
  }
  public void marshal(ContentHandler handler)
    throws IOException, MarshalException, ValidationException {
    Marshaller.marshal(this, handler);
  }
  public void removeAllProperty()
  {
    this._propertyList.clear();
  }
  public boolean removeProperty(Property vProperty) {
    boolean removed = this._propertyList.remove(vProperty);
    return removed;
  }
  public Property removePropertyAt(int index) {
    Object obj = this._propertyList.remove(index);
    return (Property)obj;
  }
  public void setAutoNaming(BindXmlAutoNamingType autoNaming)
  {
    this._autoNaming = autoNaming;
  }
  public void setClassMapping(ClassMapping classMapping)
  {
    this._classMapping = classMapping;
  }
  public void setLocation(String location)
  {
    this._location = location;
  }
  public void setMatches(String matches)
  {
    this._matches = matches;
  }
  public void setName(String name)
  {
    this._name = name;
  }
  public void setNode(BindXmlNodeType node)
  {
    this._node = node;
  }
  public void setProperty(int index, Property vProperty)
    throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._propertyList.size())) {
      throw new IndexOutOfBoundsException("setProperty: Index value '" + index + "' not in range [0.." + (this._propertyList.size() - 1) + "]");
    }
    this._propertyList.set(index, vProperty);
  }
  public void setProperty(Property[] vPropertyArray) {
    this._propertyList.clear();
    for (int i = 0; i < vPropertyArray.length; i++) {
      this._propertyList.add(vPropertyArray[i]);
    }
  }
  public void setQNamePrefix(String QNamePrefix)
  {
    this._QNamePrefix = QNamePrefix;
  }
  public void setReference(boolean reference) {
    this._reference = reference;
    this._has_reference = true;
  }
  public void setTransient(boolean _transient) {
    this._transient = _transient;
    this._has_transient = true;
  }
  public void setType(String type)
  {
    this._type = type;
  }
  public static BindXml unmarshal(Reader reader)
    throws MarshalException, ValidationException {
    return (BindXml)Unmarshaller.unmarshal(BindXml.class, reader);
  }
  public void validate() throws ValidationException {
    Validator validator = new Validator();
    validator.validate(this);
  }
}
