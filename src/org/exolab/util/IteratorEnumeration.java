package org.exolab.util;

import java.util.Enumeration;
import java.util.Iterator;

public class IteratorEnumeration implements Enumeration {
  private Iterator _iter;
  public IteratorEnumeration(Iterator iter) {
    if (iter == null) {
      throw new IllegalArgumentException("Argument 'iter' is null");
    }
    this._iter = iter;
  }
  public boolean hasMoreElements()
  {
    return this._iter.hasNext();
  }
  public Object nextElement()
  {
    return this._iter.next();
  }
}
