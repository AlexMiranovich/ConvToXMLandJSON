package org.exolab.xml;

import java.util.HashSet;

public class UnmarshalState {
  UnmarshalHandler.Arguments _args = null;
  String _location = "";
  boolean _nil = false;
  String _elementName = null;
  StringBuffer _buffer = null;
  Object _key = null;
  Object _object = null;
  Class _type = null;
  XMLFieldDescriptor _fieldDesc = null;
  XMLClassDescriptor _classDesc = null;
  boolean _primitiveOrImmutable = false;
  private HashSet _markedList = null;
  boolean _derived = false;
  boolean _wrapper = false;
  boolean _wsPreserve = false;
  boolean _trailingWhitespaceRemoved = false;
  public int _expectedIndex = 0;
  public boolean _withinMultivaluedElement = false;
  UnmarshalState _targetState = null;
  UnmarshalState _parent = null;
  
  void clear()
  {
    this._args = null;
    this._location = "";
    this._elementName = null;
    this._buffer = null;
    this._key = null;
    this._nil = false;
    this._object = null;
    this._type = null;
    this._fieldDesc = null;
    this._classDesc = null;
    this._primitiveOrImmutable = false;
    if (this._markedList != null) {
      this._markedList.clear();
    }
    this._derived = false;
    this._wrapper = false;
    this._targetState = null;
    this._wsPreserve = false;
    this._parent = null;
    this._trailingWhitespaceRemoved = false;
  }
  
  void markAsUsed(XMLFieldDescriptor descriptor)
  {
    if (this._markedList == null) {
      this._markedList = new HashSet(5);
    }
    this._markedList.add(descriptor);
  }
  
  void markAsNotUsed(XMLFieldDescriptor descriptor)
  {
    if (this._markedList == null) {
      return;
    }
    this._markedList.remove(descriptor);
  }
  
  boolean isUsed(XMLFieldDescriptor descriptor)
  {
    if (this._markedList == null) {
      return false;
    }
    return this._markedList.contains(descriptor);
  }
}
