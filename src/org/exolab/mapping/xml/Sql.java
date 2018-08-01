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
import org.exolab.mapping.xml.types.SqlDirtyType;
import org.exolab.xml.MarshalException;
import org.exolab.xml.Marshaller;
import org.exolab.xml.Unmarshaller;
import org.exolab.xml.ValidationException;
import org.exolab.xml.Validator;
import org.xml.sax.ContentHandler;

public class Sql implements Serializable {
  private List<String> _name;
  private String _type;
  private String _manyTable;
  private List<String> _manyKey;
  private boolean _readOnly = false;
  private boolean _has_readOnly;
  private boolean _transient;
  private boolean _has_transient;
  private SqlDirtyType _dirty = SqlDirtyType.valueOf("check");
  public Sql() {
    this._name = new ArrayList();
    this._manyKey = new ArrayList();
    setDirty(SqlDirtyType.valueOf("check"));
  }
  
  public void addManyKey(String vManyKey)
    throws IndexOutOfBoundsException {
    this._manyKey.add(vManyKey);
  }
  public void addManyKey(int index, String vManyKey)
    throws IndexOutOfBoundsException
  {
    this._manyKey.add(index, vManyKey);
  }
  
  public void addName(String vName)
    throws IndexOutOfBoundsException {
    this._name.add(vName);
  }
  public void addName(int index, String vName)
    throws IndexOutOfBoundsException {
    this._name.add(index, vName);
  }
  public void deleteReadOnly()
  {
    this._has_readOnly = false;
  }
  public void deleteTransient()
  {
    this._has_transient = false;
  }
  public Enumeration<? extends String> enumerateManyKey()
  {
    return Collections.enumeration(this._manyKey);
  }
  public Enumeration<? extends String> enumerateName()
  {
    return Collections.enumeration(this._name);
  }
  public SqlDirtyType getDirty()
  {
    return this._dirty;
  }
  public String getManyKey(int index)
    throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._manyKey.size())) {
      throw new IndexOutOfBoundsException("getManyKey: Index value '" + index + "' not in range [0.." + (this._manyKey.size() - 1) + "]");
    }
    return (String)this._manyKey.get(index);
  }
  public String[] getManyKey() {
    String[] array = new String[0];
    return (String[])this._manyKey.toArray(array);
  }
  public int getManyKeyCount()
  {
    return this._manyKey.size();
  }
  public String getManyTable()
  {
    return this._manyTable;
  }
  public String getName(int index)
    throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._name.size())) {
      throw new IndexOutOfBoundsException("getName: Index value '" + index + "' not in range [0.." + (this._name.size() - 1) + "]");
    }
    return (String)this._name.get(index);
  }
  public String[] getName() {
    String[] array = new String[0];
    return (String[])this._name.toArray(array);
  }
  public int getNameCount()
  {
    return this._name.size();
  }
  public boolean getReadOnly()
  {
    return this._readOnly;
  }
  public boolean getTransient()
  {
    return this._transient;
  }
  public String getType()
  {
    return this._type;
  }
  public boolean hasReadOnly()
  {
    return this._has_readOnly;
  }
  public boolean hasTransient()
  {
    return this._has_transient;
  }
  public boolean isReadOnly()
  {
    return this._readOnly;
  }
  public boolean isTransient()
  {
    return this._transient;
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
  public Iterator<? extends String> iterateManyKey()
  {
    return this._manyKey.iterator();
  }
  public Iterator<? extends String> iterateName()
  {
    return this._name.iterator();
  }
  public void marshal(Writer out)
    throws MarshalException, ValidationException {
    Marshaller.marshal(this, out);
  }
  public void marshal(ContentHandler handler)
    throws IOException, MarshalException, ValidationException {
    Marshaller.marshal(this, handler);
  }
  public void removeAllManyKey()
  {
    this._manyKey.clear();
  }
  public void removeAllName()
  {
    this._name.clear();
  }
  public boolean removeManyKey(String vManyKey) {
    boolean removed = this._manyKey.remove(vManyKey);
    return removed;
  }
  public String removeManyKeyAt(int index) {
    Object obj = this._manyKey.remove(index);
    return (String)obj;
  }
  public boolean removeName(String vName) {
    boolean removed = this._name.remove(vName);
    return removed;
  }
  public String removeNameAt(int index) {
    Object obj = this._name.remove(index);
    return (String)obj;
  }
  public void setDirty(SqlDirtyType dirty)
  {
    this._dirty = dirty;
  }
  public void setManyKey(int index, String vManyKey)
    throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._manyKey.size())) {
      throw new IndexOutOfBoundsException("setManyKey: Index value '" + index + "' not in range [0.." + (this._manyKey.size() - 1) + "]");
    }
    this._manyKey.set(index, vManyKey);
  }
  public void setManyKey(String[] vManyKeyArray) {
    this._manyKey.clear();
    for (int i = 0; i < vManyKeyArray.length; i++) {
      this._manyKey.add(vManyKeyArray[i]);
    }
  }
  public void setManyTable(String manyTable)
  {
    this._manyTable = manyTable;
  }
  public void setName(int index, String vName)
    throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._name.size())) {
      throw new IndexOutOfBoundsException("setName: Index value '" + index + "' not in range [0.." + (this._name.size() - 1) + "]");
    }
    this._name.set(index, vName);
  }
  public void setName(String[] vNameArray) {
    this._name.clear();
    for (int i = 0; i < vNameArray.length; i++) {
      this._name.add(vNameArray[i]);
    }
  }
  public void setReadOnly(boolean readOnly) {
    this._readOnly = readOnly;
    this._has_readOnly = true;
  }
  public void setTransient(boolean _transient) {
    this._transient = _transient;
    this._has_transient = true;
  }
  public void setType(String type) {
    this._type = type;
  }
  public static Sql unmarshal(Reader reader)
    throws MarshalException, ValidationException {
    return (Sql)Unmarshaller.unmarshal(Sql.class, reader);
  }
  public void validate()
    throws ValidationException {
    Validator validator = new Validator();
    validator.validate(this);
  }
}
