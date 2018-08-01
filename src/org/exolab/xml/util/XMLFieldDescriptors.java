package org.exolab.xml.util;

import org.exolab.xml.XMLFieldDescriptor;

public class XMLFieldDescriptors {
  private static final int DEFAULT_SIZE = 11;
  private XMLFieldDescriptor[] _elements;
  private int _elementCount = 0;
  public XMLFieldDescriptors()
  {
    this._elements = new XMLFieldDescriptor[11];
  }
  public XMLFieldDescriptors(int size)
  {
    this._elements = new XMLFieldDescriptor[size];
  }
  public boolean add(XMLFieldDescriptor descriptor) {
    for (int i = 0; i < this._elementCount; i++) {
      if (this._elements[i] == descriptor) {
        return false;
      }
    }
    if (this._elementCount == this._elements.length) {
      increaseSize();
    }
    this._elements[(this._elementCount++)] = descriptor;
    return true;
  }
  public void clear() {
    for (int i = 0; i < this._elementCount; i++) {
      this._elements[i] = null;
    }
    this._elementCount = 0;
  }
  public Object clone() {
    Object obj = null;
    try {
      obj = super.clone();
    }
    catch (CloneNotSupportedException e) {}
    return obj;
  }
  public boolean contains(XMLFieldDescriptor descriptor)
  {
    return indexOf(descriptor) >= 0;
  }
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof XMLFieldDescriptors)) {
      return false;
    }
    XMLFieldDescriptors descs = (XMLFieldDescriptors)obj;
    if (descs._elementCount != this._elementCount) {
      return false;
    }
    for (int i = 0; i < this._elementCount; i++) {
      Object e1 = get(i);
      Object e2 = descs._elements[i];
      if (e1 == null ? e2 != null : !e1.equals(e2)) {
        return false;
      }
    }
    return true;
  }
  public XMLFieldDescriptor get(int index)
    throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._elementCount)) {
      throw new IndexOutOfBoundsException();
    }
    return this._elements[index];
  }
  public int hashCode() {
    int hashCode = 1;
    for (int i = 0; i < this._elementCount; i++) {
      Object obj = this._elements[i];
      hashCode = 31 * hashCode + (obj == null ? 0 : obj.hashCode());
    }
    return hashCode;
  }
  public int indexOf(XMLFieldDescriptor descriptor) {
    if (descriptor == null) {
      for (int i = 0; i < this._elementCount; i++) {
        if (this._elements[i] == null) {
          return i;
        }
      }
    } else {
      for (int i = 0; i < this._elementCount; i++) {
        if (descriptor.equals(this._elements[i])) {
          return i;
        }
      }
    }
    return -1;
  }
  public boolean isEmpty()
  {
    return this._elementCount == 0;
  }
  public XMLFieldDescriptor remove(int index) {
    if ((index < 0) || (index > this._elementCount)) {
      return null;
    }
    XMLFieldDescriptor desc = this._elements[index];
    shiftDown(index + 1);
    this._elementCount -= 1;
    return desc;
  }
  public boolean remove(XMLFieldDescriptor descriptor) {
    int index = indexOf(descriptor);
    if (index > -1) {
      remove(index);
      return true;
    }
    return false;
  }
  public void trimToSize() {
    if (this._elements.length == this._elementCount) {
      return;
    }
    XMLFieldDescriptor[] pointer = this._elements;
    this._elements = new XMLFieldDescriptor[this._elementCount];
    System.arraycopy(pointer, 0, this._elements, 0, this._elementCount);
    pointer = null;
  }
  public int size()
  {
    return this._elementCount;
  }
  public XMLFieldDescriptor[] toArray() {
    XMLFieldDescriptor[] objArray = new XMLFieldDescriptor[this._elementCount];
    System.arraycopy(this._elements, 0, objArray, 0, this._elementCount);
    return objArray;
  }
  public XMLFieldDescriptor[] toArray(XMLFieldDescriptor[] dst)
  {
    return toArray(dst, 0);
  }
  public XMLFieldDescriptor[] toArray(XMLFieldDescriptor[] dst, int offset) {
    XMLFieldDescriptor[] objArray = null;
    if (dst.length >= this._elementCount) {
      objArray = dst;
    } else {
      objArray = new XMLFieldDescriptor[this._elementCount];
    }
    System.arraycopy(this._elements, 0, objArray, offset, this._elementCount);
    return objArray;
  }
  private void increaseSize() {
    XMLFieldDescriptor[] pointer = this._elements;
    int length = pointer.length > 0 ? pointer.length : 1;
    this._elements = new XMLFieldDescriptor[length * 3 / 2 + 1];
    System.arraycopy(pointer, 0, this._elements, 0, pointer.length);
    pointer = null;
  }
  private void shiftDown(int index) {
    if ((index <= 0) || (index >= this._elementCount)) {
      return;
    }
    System.arraycopy(this._elements, index, this._elements, index - 1, this._elementCount - index);
    this._elements[(this._elementCount - 1)] = null;
  }
}
