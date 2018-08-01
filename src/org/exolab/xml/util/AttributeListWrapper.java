package org.exolab.xml.util;

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.AttributeList;

public class AttributeListWrapper implements AttributeList {
  NamedNodeMap _map = null;
  public AttributeListWrapper(NamedNodeMap namedNodeMap) {
    this._map = namedNodeMap;
  }
  public int getLength() {
    if (this._map == null) {
      return 0;
    }
    return this._map.getLength();
  }
  public String getName(int i) {
    if (this._map != null) {
      Attr attr = (Attr)this._map.item(i);
      return attr.getName();
    }
    return null;
  }
  public String getType(int i) {
    return null;
  }
  public String getValue(int i) {
    if (this._map != null) {
      Attr attr = (Attr)this._map.item(i);
      return attr.getValue();
    }
    return null;
  }
  public String getType(String name) {
    return null;
  }
  public String getValue(String name) {
    if (this._map != null) {
      Attr attr = (Attr)this._map.getNamedItem(name);
      if (attr != null) {
        return attr.getValue();
      }
    }
    return null;
  }
}
