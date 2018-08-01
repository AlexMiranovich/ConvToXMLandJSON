package org.exec.core.util;

import java.util.Enumeration;
import java.util.Iterator;

public final class EnumerationIterator implements Iterator {
  private final Enumeration _enumeration;
  public EnumerationIterator(Enumeration enumeration)
  {
    this._enumeration = enumeration;
  }
  public boolean hasNext()
  {
    return this._enumeration.hasMoreElements();
  }
  public Object next()
  {
    return this._enumeration.nextElement();
  }
  public void remove()
  {
    throw new UnsupportedOperationException();
  }
}
