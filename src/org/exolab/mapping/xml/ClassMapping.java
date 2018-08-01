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
import org.exolab.mapping.xml.types.ClassMappingAccessType;
import org.exolab.xml.MarshalException;
import org.exolab.xml.Marshaller;
import org.exolab.xml.Unmarshaller;
import org.exolab.xml.ValidationException;
import org.exolab.xml.Validator;
import org.xml.sax.ContentHandler;

public class ClassMapping implements Serializable {
  private String _name;
  private Object _extends;
  private Object _depends;
  private List<String> _identity;
  private ClassMappingAccessType _access = ClassMappingAccessType.valueOf("shared");
  private String _keyGenerator;
  private boolean _autoComplete = false;
  private boolean _has_autoComplete;
  private boolean _verifyConstructable = true;
  private boolean _has_verifyConstructable;
  private String _version;
  private String _description;
  private CacheTypeMapping _cacheTypeMapping;
  private MapTo _mapTo;
  private List<NamedQuery> _namedQueryList;
  private List<NamedNativeQuery> _namedNativeQueryList;
  private ClassChoice _classChoice;
  public ClassMapping() {
    this._identity = new ArrayList();
    setAccess(ClassMappingAccessType.valueOf("shared"));
    this._namedQueryList = new ArrayList();
    this._namedNativeQueryList = new ArrayList();
  }
  public void addIdentity(String vIdentity)
    throws IndexOutOfBoundsException {
    this._identity.add(vIdentity);
  }
  public void addIdentity(int index, String vIdentity)
    throws IndexOutOfBoundsException {
    this._identity.add(index, vIdentity);
  }
  public void addNamedNativeQuery(NamedNativeQuery vNamedNativeQuery)
    throws IndexOutOfBoundsException {
    this._namedNativeQueryList.add(vNamedNativeQuery);
  }
  public void addNamedNativeQuery(int index, NamedNativeQuery vNamedNativeQuery)
    throws IndexOutOfBoundsException {
    this._namedNativeQueryList.add(index, vNamedNativeQuery);
  }
  public void addNamedQuery(NamedQuery vNamedQuery)
    throws IndexOutOfBoundsException {
    this._namedQueryList.add(vNamedQuery);
  }
  public void addNamedQuery(int index, NamedQuery vNamedQuery)
    throws IndexOutOfBoundsException
  {
    this._namedQueryList.add(index, vNamedQuery);
  }
  
  public void deleteAutoComplete()
  {
    this._has_autoComplete = false;
  }
  
  public void deleteVerifyConstructable()
  {
    this._has_verifyConstructable = false;
  }
  
  public Enumeration<? extends String> enumerateIdentity()
  {
    return Collections.enumeration(this._identity);
  }
  
  public Enumeration<? extends NamedNativeQuery> enumerateNamedNativeQuery()
  {
    return Collections.enumeration(this._namedNativeQueryList);
  }
  
  public Enumeration<? extends NamedQuery> enumerateNamedQuery()
  {
    return Collections.enumeration(this._namedQueryList);
  }
  
  public ClassMappingAccessType getAccess()
  {
    return this._access;
  }
  public boolean getAutoComplete()
  {
    return this._autoComplete;
  }
  public CacheTypeMapping getCacheTypeMapping()
  {
    return this._cacheTypeMapping;
  }
  public ClassChoice getClassChoice()
  {
    return this._classChoice;
  }
  public Object getDepends()
  {
    return this._depends;
  }
  public String getDescription()
  {
    return this._description;
  }
  public Object getExtends()
  {
    return this._extends;
  }
  public String getIdentity(int index)
    throws IndexOutOfBoundsException
  {
    if ((index < 0) || (index >= this._identity.size())) {
      throw new IndexOutOfBoundsException("getIdentity: Index value '" + index + "' not in range [0.." + (this._identity.size() - 1) + "]");
    }
    return (String)this._identity.get(index);
  }
  
  public String[] getIdentity()
  {
    String[] array = new String[0];
    return (String[])this._identity.toArray(array);
  }
  public int getIdentityCount()
  {
    return this._identity.size();
  }
  public String getKeyGenerator()
  {
    return this._keyGenerator;
  }
  public MapTo getMapTo()
  {
    return this._mapTo;
  }
  public String getName()
  {
    return this._name;
  }
  public NamedNativeQuery getNamedNativeQuery(int index)
    throws IndexOutOfBoundsException
  {
    if ((index < 0) || (index >= this._namedNativeQueryList.size())) {
      throw new IndexOutOfBoundsException("getNamedNativeQuery: Index value '" + index + "' not in range [0.." + (this._namedNativeQueryList.size() - 1) + "]");
    }
    return (NamedNativeQuery)this._namedNativeQueryList.get(index);
  }
  
  public NamedNativeQuery[] getNamedNativeQuery()
  {
    NamedNativeQuery[] array = new NamedNativeQuery[0];
    return (NamedNativeQuery[])this._namedNativeQueryList.toArray(array);
  }
  public int getNamedNativeQueryCount()
  {
    return this._namedNativeQueryList.size();
  }
  public NamedQuery getNamedQuery(int index)
    throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._namedQueryList.size())) {
      throw new IndexOutOfBoundsException("getNamedQuery: Index value '" + index + "' not in range [0.." + (this._namedQueryList.size() - 1) + "]");
    }
    return (NamedQuery)this._namedQueryList.get(index);
  }
  public NamedQuery[] getNamedQuery() {
    NamedQuery[] array = new NamedQuery[0];
    return (NamedQuery[])this._namedQueryList.toArray(array);
  }
  public int getNamedQueryCount()
  {
    return this._namedQueryList.size();
  }
  public boolean getVerifyConstructable()
  {
    return this._verifyConstructable;
  }
  public String getVersion()
  {
    return this._version;
  }
  public boolean hasAutoComplete()
  {
    return this._has_autoComplete;
  }
  public boolean hasVerifyConstructable()
  {
    return this._has_verifyConstructable;
  }
  public boolean isAutoComplete()
  {
    return this._autoComplete;
  }
  public boolean isValid() {
    try {
      validate();
    }
    catch (ValidationException vex)
    {
      return false;
    }
    return true;
  }
  public boolean isVerifyConstructable()
  {
    return this._verifyConstructable;
  }
  public Iterator<? extends String> iterateIdentity()
  {
    return this._identity.iterator();
  }
  public Iterator<? extends NamedNativeQuery> iterateNamedNativeQuery() {
    return this._namedNativeQueryList.iterator();
  }
  public Iterator<? extends NamedQuery> iterateNamedQuery()
  {
    return this._namedQueryList.iterator();
  }
  public void marshal(Writer out)
    throws MarshalException, ValidationException {
    Marshaller.marshal(this, out);
  }
  public void marshal(ContentHandler handler)
    throws IOException, MarshalException, ValidationException {
    Marshaller.marshal(this, handler);
  }
  public void removeAllIdentity()
  {
    this._identity.clear();
  }
  public void removeAllNamedNativeQuery()
  {
    this._namedNativeQueryList.clear();
  }
  public void removeAllNamedQuery()
  {
    this._namedQueryList.clear();
  }
  public boolean removeIdentity(String vIdentity) {
    boolean removed = this._identity.remove(vIdentity);
    return removed;
  }
  public String removeIdentityAt(int index) {
    Object obj = this._identity.remove(index);
    return (String)obj;
  }
  public boolean removeNamedNativeQuery(NamedNativeQuery vNamedNativeQuery) {
    boolean removed = this._namedNativeQueryList.remove(vNamedNativeQuery);
    return removed;
  }
  public NamedNativeQuery removeNamedNativeQueryAt(int index) {
    Object obj = this._namedNativeQueryList.remove(index);
    return (NamedNativeQuery)obj;
  }
  public boolean removeNamedQuery(NamedQuery vNamedQuery) {
    boolean removed = this._namedQueryList.remove(vNamedQuery);
    return removed;
  }
  public NamedQuery removeNamedQueryAt(int index) {
    Object obj = this._namedQueryList.remove(index);
    return (NamedQuery)obj;
  }
  public void setAccess(ClassMappingAccessType access)
  {
    this._access = access;
  }
  public void setAutoComplete(boolean autoComplete)
  {
    this._autoComplete = autoComplete;
    this._has_autoComplete = true;
  }
  public void setCacheTypeMapping(CacheTypeMapping cacheTypeMapping)
  {
    this._cacheTypeMapping = cacheTypeMapping;
  }
  public void setClassChoice(ClassChoice classChoice)
  {
    this._classChoice = classChoice;
  }
  public void setDepends(Object depends)
  {
    this._depends = depends;
  }
  public void setDescription(String description)
  {
    this._description = description;
  }
  public void setExtends(Object _extends)
  {
    this._extends = _extends;
  }
  public void setIdentity(int index, String vIdentity)
    throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._identity.size())) {
      throw new IndexOutOfBoundsException("setIdentity: Index value '" + index + "' not in range [0.." + (this._identity.size() - 1) + "]");
    }
    this._identity.set(index, vIdentity);
  }
  public void setIdentity(String[] vIdentityArray) {
    this._identity.clear();
    for (int i = 0; i < vIdentityArray.length; i++) {
      this._identity.add(vIdentityArray[i]);
    }
  }
  public void setKeyGenerator(String keyGenerator)
  {
    this._keyGenerator = keyGenerator;
  }
  public void setMapTo(MapTo mapTo)
  {
    this._mapTo = mapTo;
  }
  public void setName(String name)
  {
    this._name = name;
  }
  public void setNamedNativeQuery(int index, NamedNativeQuery vNamedNativeQuery)
    throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._namedNativeQueryList.size())) {
      throw new IndexOutOfBoundsException("setNamedNativeQuery: Index value '" + index + "' not in range [0.." + (this._namedNativeQueryList.size() - 1) + "]");
    }
    this._namedNativeQueryList.set(index, vNamedNativeQuery);
  }
  
  public void setNamedNativeQuery(NamedNativeQuery[] vNamedNativeQueryArray) {
    this._namedNativeQueryList.clear();
    for (int i = 0; i < vNamedNativeQueryArray.length; i++) {
      this._namedNativeQueryList.add(vNamedNativeQueryArray[i]);
    }
  }
  public void setNamedQuery(int index, NamedQuery vNamedQuery)
    throws IndexOutOfBoundsException
  {
    if ((index < 0) || (index >= this._namedQueryList.size())) {
      throw new IndexOutOfBoundsException("setNamedQuery: Index value '" + index + "' not in range [0.." + (this._namedQueryList.size() - 1) + "]");
    }
    this._namedQueryList.set(index, vNamedQuery);
  }
  public void setNamedQuery(NamedQuery[] vNamedQueryArray) {
    this._namedQueryList.clear();
    for (int i = 0; i < vNamedQueryArray.length; i++) {
      this._namedQueryList.add(vNamedQueryArray[i]);
    }
  }
  public void setVerifyConstructable(boolean verifyConstructable) {
    this._verifyConstructable = verifyConstructable;
    this._has_verifyConstructable = true;
  }
  public void setVersion(String version)
  {
    this._version = version;
  }
  public static ClassMapping unmarshal(Reader reader)
    throws MarshalException, ValidationException {
    return (ClassMapping)Unmarshaller.unmarshal(ClassMapping.class, reader);
  }
  public void validate() throws ValidationException {
    Validator validator = new Validator();
    validator.validate(this);
  }
}
