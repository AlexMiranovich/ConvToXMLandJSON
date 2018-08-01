package org.exolab.xml.location;

import java.io.Serializable;
import java.util.Vector;

public class XPathLocation implements Location, Serializable {
  private static final long serialVersionUID = 1L;
  private final Vector _path = new Vector();
  private boolean _allowChildrenOrAtts = true;
  public void addAttribute(String name) {
    if (this._allowChildrenOrAtts) {
      this._allowChildrenOrAtts = false;
      this._path.addElement("@" + name);
    }
  }
  public void addChild(String name) {
    if (this._allowChildrenOrAtts) {
      this._path.addElement(name);
    }
  }
  public void addParent(String name)
  {
    this._path.insertElementAt(name, 0);
  }
  public String toString() {
    StringBuffer buf = new StringBuffer("XPATH: ");
    for (int i = 0; i < this._path.size(); i++) {
      buf.append('/');
      buf.append((String)this._path.elementAt(i));
    }
    return buf.toString();
  }
}
