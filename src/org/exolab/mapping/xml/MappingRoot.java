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

public class MappingRoot implements Serializable {
  private String _description;
  private List<Include> _includeList;
  private List<ClassMapping> _classMappingList;
  private List<KeyGeneratorDef> _keyGeneratorDefList;
  private List<FieldHandlerDef> _fieldHandlerDefList;
  public MappingRoot() {
    this._includeList = new ArrayList();
    this._classMappingList = new ArrayList();
    this._keyGeneratorDefList = new ArrayList();
    this._fieldHandlerDefList = new ArrayList();
  }
  public void addClassMapping(ClassMapping vClassMapping)
    throws IndexOutOfBoundsException {
    this._classMappingList.add(vClassMapping);
  }
  public void addClassMapping(int index, ClassMapping vClassMapping)
    throws IndexOutOfBoundsException {
    this._classMappingList.add(index, vClassMapping);
  }
  public void addFieldHandlerDef(FieldHandlerDef vFieldHandlerDef)
    throws IndexOutOfBoundsException {
    this._fieldHandlerDefList.add(vFieldHandlerDef);
  }
  public void addFieldHandlerDef(int index, FieldHandlerDef vFieldHandlerDef)
    throws IndexOutOfBoundsException {
    this._fieldHandlerDefList.add(index, vFieldHandlerDef);
  }
  public void addInclude(Include vInclude)
    throws IndexOutOfBoundsException {
    this._includeList.add(vInclude);
  }
  public void addInclude(int index, Include vInclude)
    throws IndexOutOfBoundsException {
    this._includeList.add(index, vInclude);
  }
  public void addKeyGeneratorDef(KeyGeneratorDef vKeyGeneratorDef)
    throws IndexOutOfBoundsException {
    this._keyGeneratorDefList.add(vKeyGeneratorDef);
  }
  public void addKeyGeneratorDef(int index, KeyGeneratorDef vKeyGeneratorDef)
    throws IndexOutOfBoundsException {
    this._keyGeneratorDefList.add(index, vKeyGeneratorDef);
  }
  public Enumeration<? extends ClassMapping> enumerateClassMapping() {
    return Collections.enumeration(this._classMappingList);
  }
  public Enumeration<? extends FieldHandlerDef> enumerateFieldHandlerDef() {
    return Collections.enumeration(this._fieldHandlerDefList);
  }
  public Enumeration<? extends Include> enumerateInclude()
  {
    return Collections.enumeration(this._includeList);
  }
  public Enumeration<? extends KeyGeneratorDef> enumerateKeyGeneratorDef() {
    return Collections.enumeration(this._keyGeneratorDefList);
  }
  public ClassMapping getClassMapping(int index)
    throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._classMappingList.size())) {
      throw new IndexOutOfBoundsException("getClassMapping: Index value '" + index + "' not in range [0.." + (this._classMappingList.size() - 1) + "]");
    }
    return (ClassMapping)this._classMappingList.get(index);
  }
  public ClassMapping[] getClassMapping() {
    ClassMapping[] array = new ClassMapping[0];
    return (ClassMapping[])this._classMappingList.toArray(array);
  }
  public int getClassMappingCount()
  {
    return this._classMappingList.size();
  }
  public String getDescription()
  {
    return this._description;
  }
  public FieldHandlerDef getFieldHandlerDef(int index)
    throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._fieldHandlerDefList.size())) {
      throw new IndexOutOfBoundsException("getFieldHandlerDef: Index value '" + index + "' not in range [0.." + (this._fieldHandlerDefList.size() - 1) + "]");
    }
    return (FieldHandlerDef)this._fieldHandlerDefList.get(index);
  }
  public FieldHandlerDef[] getFieldHandlerDef() {
    FieldHandlerDef[] array = new FieldHandlerDef[0];
    return (FieldHandlerDef[])this._fieldHandlerDefList.toArray(array);
  }
  public int getFieldHandlerDefCount()
  {
    return this._fieldHandlerDefList.size();
  }
  public Include getInclude(int index)
    throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._includeList.size())) {
      throw new IndexOutOfBoundsException("getInclude: Index value '" + index + "' not in range [0.." + (this._includeList.size() - 1) + "]");
    }
    return (Include)this._includeList.get(index);
  }
  public Include[] getInclude() {
    Include[] array = new Include[0];
    return (Include[])this._includeList.toArray(array);
  }
  public int getIncludeCount()
  {
    return this._includeList.size();
  }
  public KeyGeneratorDef getKeyGeneratorDef(int index)
    throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._keyGeneratorDefList.size())) {
      throw new IndexOutOfBoundsException("getKeyGeneratorDef: Index value '" + index + "' not in range [0.." + (this._keyGeneratorDefList.size() - 1) + "]");
    }
    return (KeyGeneratorDef)this._keyGeneratorDefList.get(index);
  }
  public KeyGeneratorDef[] getKeyGeneratorDef() {
    KeyGeneratorDef[] array = new KeyGeneratorDef[0];
    return (KeyGeneratorDef[])this._keyGeneratorDefList.toArray(array);
  }
  public int getKeyGeneratorDefCount()
  {
    return this._keyGeneratorDefList.size();
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
  public Iterator<? extends ClassMapping> iterateClassMapping()
  {
    return this._classMappingList.iterator();
  }
  public Iterator<? extends FieldHandlerDef> iterateFieldHandlerDef()
  {
    return this._fieldHandlerDefList.iterator();
  }
  public Iterator<? extends Include> iterateInclude()
  {
    return this._includeList.iterator();
  }
  public Iterator<? extends KeyGeneratorDef> iterateKeyGeneratorDef()
  {
    return this._keyGeneratorDefList.iterator();
  }
  public void marshal(Writer out) throws MarshalException, ValidationException {
    Marshaller.marshal(this, out);
  }
  public void marshal(ContentHandler handler)
    throws IOException, MarshalException, ValidationException {
    Marshaller.marshal(this, handler);
  }
  public void removeAllClassMapping()
  {
    this._classMappingList.clear();
  }
  public void removeAllFieldHandlerDef()
  {
    this._fieldHandlerDefList.clear();
  }
  public void removeAllInclude()
  {
    this._includeList.clear();
  }
  public void removeAllKeyGeneratorDef()
  {
    this._keyGeneratorDefList.clear();
  }
  public boolean removeClassMapping(ClassMapping vClassMapping) {
    boolean removed = this._classMappingList.remove(vClassMapping);
    return removed;
  }
  public ClassMapping removeClassMappingAt(int index) {
    Object obj = this._classMappingList.remove(index);
    return (ClassMapping)obj;
  }
  public boolean removeFieldHandlerDef(FieldHandlerDef vFieldHandlerDef) {
    boolean removed = this._fieldHandlerDefList.remove(vFieldHandlerDef);
    return removed;
  }
  public FieldHandlerDef removeFieldHandlerDefAt(int index) {
    Object obj = this._fieldHandlerDefList.remove(index);
    return (FieldHandlerDef)obj;
  }
  public boolean removeInclude(Include vInclude) {
    boolean removed = this._includeList.remove(vInclude);
    return removed;
  }
  public Include removeIncludeAt(int index) {
    Object obj = this._includeList.remove(index);
    return (Include)obj;
  }
  public boolean removeKeyGeneratorDef(KeyGeneratorDef vKeyGeneratorDef) {
    boolean removed = this._keyGeneratorDefList.remove(vKeyGeneratorDef);
    return removed;
  }
  public KeyGeneratorDef removeKeyGeneratorDefAt(int index) {
    Object obj = this._keyGeneratorDefList.remove(index);
    return (KeyGeneratorDef)obj;
  }
  public void setClassMapping(int index, ClassMapping vClassMapping)
    throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._classMappingList.size())) {
      throw new IndexOutOfBoundsException("setClassMapping: Index value '" + index + "' not in range [0.." + (this._classMappingList.size() - 1) + "]");
    }
    this._classMappingList.set(index, vClassMapping);
  }
  public void setClassMapping(ClassMapping[] vClassMappingArray) {
    this._classMappingList.clear();
    for (int i = 0; i < vClassMappingArray.length; i++) {
      this._classMappingList.add(vClassMappingArray[i]);
    }
  }
  public void setDescription(String description) {
    this._description = description;
  }
  public void setFieldHandlerDef(int index, FieldHandlerDef vFieldHandlerDef)
    throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._fieldHandlerDefList.size())) {
      throw new IndexOutOfBoundsException("setFieldHandlerDef: Index value '" + index + "' not in range [0.." + (this._fieldHandlerDefList.size() - 1) + "]");
    }
    this._fieldHandlerDefList.set(index, vFieldHandlerDef);
  }
  public void setFieldHandlerDef(FieldHandlerDef[] vFieldHandlerDefArray) {
    this._fieldHandlerDefList.clear();
    for (int i = 0; i < vFieldHandlerDefArray.length; i++) {
      this._fieldHandlerDefList.add(vFieldHandlerDefArray[i]);
    }
  }
  public void setInclude(int index, Include vInclude)
    throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._includeList.size())) {
      throw new IndexOutOfBoundsException("setInclude: Index value '" + index + "' not in range [0.." + (this._includeList.size() - 1) + "]");
    }
    this._includeList.set(index, vInclude);
  }
  public void setInclude(Include[] vIncludeArray) {
    this._includeList.clear();
    for (int i = 0; i < vIncludeArray.length; i++) {
      this._includeList.add(vIncludeArray[i]);
    }
  }
  public void setKeyGeneratorDef(int index, KeyGeneratorDef vKeyGeneratorDef)
    throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._keyGeneratorDefList.size())) {
      throw new IndexOutOfBoundsException("setKeyGeneratorDef: Index value '" + index + "' not in range [0.." + (this._keyGeneratorDefList.size() - 1) + "]");
    }
    this._keyGeneratorDefList.set(index, vKeyGeneratorDef);
  }
  public void setKeyGeneratorDef(KeyGeneratorDef[] vKeyGeneratorDefArray) {
    this._keyGeneratorDefList.clear();
    for (int i = 0; i < vKeyGeneratorDefArray.length; i++) {
      this._keyGeneratorDefList.add(vKeyGeneratorDefArray[i]);
    }
  }
  public static MappingRoot unmarshal(Reader reader)
    throws MarshalException, ValidationException {
    return (MappingRoot)Unmarshaller.unmarshal(MappingRoot.class, reader);
  }
  public void validate()
    throws ValidationException {
    Validator validator = new Validator();
    validator.validate(this);
  }
}
