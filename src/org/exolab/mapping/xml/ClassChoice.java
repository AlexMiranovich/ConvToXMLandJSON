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

public class ClassChoice implements Serializable {
  private List<FieldMapping> _fieldMappingList;
  private List<Container> _containerList;
  public ClassChoice() {
    this._fieldMappingList = new ArrayList();
    this._containerList = new ArrayList();
  }
  public void addContainer(Container vContainer)
    throws IndexOutOfBoundsException {
    this._containerList.add(vContainer);
  }
  public void addContainer(int index, Container vContainer)
    throws IndexOutOfBoundsException {
    this._containerList.add(index, vContainer);
  }
  public void addFieldMapping(FieldMapping vFieldMapping)
    throws IndexOutOfBoundsException {
    this._fieldMappingList.add(vFieldMapping);
  }
  public void addFieldMapping(int index, FieldMapping vFieldMapping)
    throws IndexOutOfBoundsException {
    this._fieldMappingList.add(index, vFieldMapping);
  }
  public Enumeration<? extends Container> enumerateContainer()
  {
    return Collections.enumeration(this._containerList);
  }
  public Enumeration<? extends FieldMapping> enumerateFieldMapping() {
    return Collections.enumeration(this._fieldMappingList);
  }
  public Container getContainer(int index)
    throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._containerList.size())) {
      throw new IndexOutOfBoundsException("getContainer: Index value '" + index + "' not in range [0.." + (this._containerList.size() - 1) + "]");
    }
    return (Container)this._containerList.get(index);
  }
  public Container[] getContainer() {
    Container[] array = new Container[0];
    return (Container[])this._containerList.toArray(array);
  }
  public int getContainerCount()
  {
    return this._containerList.size();
  }
  public FieldMapping getFieldMapping(int index)
    throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._fieldMappingList.size())) {
      throw new IndexOutOfBoundsException("getFieldMapping: Index value '" + index + "' not in range [0.." + (this._fieldMappingList.size() - 1) + "]");
    }
    return (FieldMapping)this._fieldMappingList.get(index);
  }
  public FieldMapping[] getFieldMapping() {
    FieldMapping[] array = new FieldMapping[0];
    return (FieldMapping[])this._fieldMappingList.toArray(array);
  }
  public int getFieldMappingCount()
  {
    return this._fieldMappingList.size();
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
  public Iterator<? extends Container> iterateContainer()
  {
    return this._containerList.iterator();
  }
  public Iterator<? extends FieldMapping> iterateFieldMapping()
  {
    return this._fieldMappingList.iterator();
  }
  public void marshal(Writer out)
    throws MarshalException, ValidationException {
    Marshaller.marshal(this, out);
  }
  public void marshal(ContentHandler handler)
    throws IOException, MarshalException, ValidationException {
    Marshaller.marshal(this, handler);
  }
  public void removeAllContainer()
  {
    this._containerList.clear();
  }
  public void removeAllFieldMapping()
  {
    this._fieldMappingList.clear();
  }
  public boolean removeContainer(Container vContainer) {
    boolean removed = this._containerList.remove(vContainer);
    return removed;
  }
  public Container removeContainerAt(int index) {
    Object obj = this._containerList.remove(index);
    return (Container)obj;
  }
  public boolean removeFieldMapping(FieldMapping vFieldMapping) {
    boolean removed = this._fieldMappingList.remove(vFieldMapping);
    return removed;
  }
  public FieldMapping removeFieldMappingAt(int index) {
    Object obj = this._fieldMappingList.remove(index);
    return (FieldMapping)obj;
  }
  public void setContainer(int index, Container vContainer)
    throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._containerList.size())) {
      throw new IndexOutOfBoundsException("setContainer: Index value '" + index + "' not in range [0.." + (this._containerList.size() - 1) + "]");
    }
    this._containerList.set(index, vContainer);
  }
  public void setContainer(Container[] vContainerArray) {
    this._containerList.clear();
    for (int i = 0; i < vContainerArray.length; i++) {
      this._containerList.add(vContainerArray[i]);
    }
  }
  public void setFieldMapping(int index, FieldMapping vFieldMapping)
    throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._fieldMappingList.size())) {
      throw new IndexOutOfBoundsException("setFieldMapping: Index value '" + index + "' not in range [0.." + (this._fieldMappingList.size() - 1) + "]");
    }
    this._fieldMappingList.set(index, vFieldMapping);
  }
  public void setFieldMapping(FieldMapping[] vFieldMappingArray) {
    this._fieldMappingList.clear();
    for (int i = 0; i < vFieldMappingArray.length; i++) {
      this._fieldMappingList.add(vFieldMappingArray[i]);
    }
  }
  public static ClassChoice unmarshal(Reader reader)
    throws MarshalException, ValidationException {
    return (ClassChoice)Unmarshaller.unmarshal(ClassChoice.class, reader);
  }
  public void validate() throws ValidationException {
    Validator validator = new Validator();
    validator.validate(this);
  }
}
