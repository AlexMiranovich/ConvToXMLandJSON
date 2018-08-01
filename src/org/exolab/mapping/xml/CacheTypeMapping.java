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
import org.exolab.xml.MarshalException;
import org.exolab.xml.Marshaller;
import org.exolab.xml.Unmarshaller;
import org.exolab.xml.ValidationException;
import org.exolab.xml.Validator;
import org.xml.sax.ContentHandler;

public class CacheTypeMapping implements Serializable {
  private String _type = "count-limited";
  private boolean _debug = false;
  private boolean _has_debug;
  private long _capacity;
  private boolean _has_capacity;
  private List<Param> _paramList;
  public CacheTypeMapping() {
    setType("count-limited");
    this._paramList = new ArrayList();
  }
  public void addParam(Param vParam) throws IndexOutOfBoundsException {
    this._paramList.add(vParam);
  }
  public void addParam(int index, Param vParam) throws IndexOutOfBoundsException {
    this._paramList.add(index, vParam);
  }
  public void deleteCapacity()
  {
    this._has_capacity = false;
  }
  public void deleteDebug()
  {
    this._has_debug = false;
  }
  public Enumeration<? extends Param> enumerateParam()
  {
    return Collections.enumeration(this._paramList);
  }
  public long getCapacity()
  {
    return this._capacity;
  }
  public boolean getDebug()
  {
    return this._debug;
  }
  public Param getParam(int index) throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._paramList.size())) {
      throw new IndexOutOfBoundsException("getParam: Index value '" + index + "' not in range [0.." + (this._paramList.size() - 1) + "]");
    }
    return (Param)this._paramList.get(index);
  }
  public Param[] getParam() {
    Param[] array = new Param[0];
    return (Param[])this._paramList.toArray(array);
  }
  public int getParamCount() {
    return this._paramList.size();
  }
  public String getType() { return this._type; }
  public boolean hasCapacity() { return this._has_capacity; }
  public boolean hasDebug() { return this._has_debug; }
  public boolean isDebug() { return this._debug; }
  public boolean isValid() {
    try { validate(); }
    catch (ValidationException vex) {
      return false;
    }
    return true;
  }
  public Iterator<? extends Param> iterateParam() { return this._paramList.iterator(); }
  public void marshal(Writer out) throws MarshalException, ValidationException {
    Marshaller.marshal(this, out);
  }
  
  public void marshal(ContentHandler handler)
    throws IOException, MarshalException, ValidationException {
    Marshaller.marshal(this, handler);
  }
  public void removeAllParam() {
    this._paramList.clear();
  }
  public boolean removeParam(Param vParam) {
    boolean removed = this._paramList.remove(vParam);
    return removed;
  }
  public Param removeParamAt(int index) {
    Object obj = this._paramList.remove(index);
    return (Param)obj;
  }
  public void setCapacity(long capacity) {
    this._capacity = capacity;
    this._has_capacity = true;
  }
  public void setDebug(boolean debug) {
    this._debug = debug;
    this._has_debug = true;
  }
  public void setParam(int index, Param vParam)
    throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._paramList.size())) {
      throw new IndexOutOfBoundsException("setParam: Index value '" + index + "' not in range [0.." + (this._paramList.size() - 1) + "]");
    }
    this._paramList.set(index, vParam);
  }
  public void setParam(Param[] vParamArray) {
    this._paramList.clear();
    for (int i = 0; i < vParamArray.length; i++) {
      this._paramList.add(vParamArray[i]);
    }
  }
  public void setType(String type) {
    this._type = type;
  }
  public static CacheTypeMapping unmarshal(Reader reader)
    throws MarshalException, ValidationException {
    return (CacheTypeMapping)Unmarshaller.unmarshal(CacheTypeMapping.class, reader);
  }
  public void validate() throws ValidationException {
    Validator validator = new Validator();
    validator.validate(this);
  }
}
