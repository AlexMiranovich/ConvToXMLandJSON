package org.exolab.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.EventListener;

public class EventListenerList implements Serializable {
  private static final long serialVersionUID = 4472874989562384564L;
  private static final EventListener[] NULL_ARRAY = new EventListener[0];
  protected transient EventListener[] listenerList = NULL_ARRAY;
  public EventListener[] getListenerList()
  {
    return this.listenerList;
  }
  public int getListenerCount()
  {
    return this.listenerList.length;
  }
  public synchronized void add(EventListener newListener) {
    if (newListener == null) {
      throw new IllegalArgumentException("Listener to add must not be null.");
    }
    if (this.listenerList == NULL_ARRAY) {
      this.listenerList = new EventListener[] { newListener };
    } else {
      int oldLength = this.listenerList.length;
      EventListener[] tmp = new EventListener[oldLength + 1];
      System.arraycopy(this.listenerList, 0, tmp, 0, oldLength);
      tmp[oldLength] = newListener;
      this.listenerList = tmp;
    }
  }

  public synchronized void add(EventListener newListener, int index)
  {
    if (newListener == null) {
      throw new IllegalArgumentException("Listener to add must not be null.");
    }
    if ((index < 0) || (index > this.listenerList.length)) {
      throw new IllegalArgumentException("Index to add listener (" + index + ") is out of bounds. List length is " + this.listenerList.length);
    }
    if (this.listenerList == NULL_ARRAY)
    {
      this.listenerList = new EventListener[] { newListener };
    }
    else
    {
      int oldLength = this.listenerList.length;
      EventListener[] tmp = new EventListener[oldLength + 1];
      
      System.arraycopy(this.listenerList, 0, tmp, 0, index);
      
      System.arraycopy(this.listenerList, index, tmp, index + 1, oldLength - index);
      
      tmp[index] = newListener;
      
      this.listenerList = tmp;
    }
  }
  
  public synchronized boolean remove(EventListener listenerToRemove)
  {
    if (listenerToRemove == null) {
      throw new IllegalArgumentException("Listener to remove must not be null.");
    }
    int index = -1;
    for (int i = this.listenerList.length - 1; i >= 0; i--) {
      if (this.listenerList[i].equals(listenerToRemove) == true)
      {
        index = i;
        break;
      }
    }
    if (index != -1)
    {
      EventListener[] tmp = new EventListener[this.listenerList.length - 1];
      
      System.arraycopy(this.listenerList, 0, tmp, 0, index);
      if (index < tmp.length) {
        System.arraycopy(this.listenerList, index + 1, tmp, index, tmp.length - index);
      }
      this.listenerList = (tmp.length == 0 ? NULL_ARRAY : tmp);
      return true;
    }
    return false;
  }
  
  private void writeObject(ObjectOutputStream s) throws IOException {
    Object[] lList = this.listenerList;
    s.defaultWriteObject();
    for (int i = 0; i < lList.length; i++) {
      EventListener l = (EventListener)lList[i];
      if ((l != null) && ((l instanceof Serializable))) {
        s.writeObject(l);
      }
    }
    s.writeObject(null);
  }
  private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    this.listenerList = NULL_ARRAY;
    s.defaultReadObject();
    EventListener listenerOrNull;
    while (null != (listenerOrNull = (EventListener)s.readObject())) {
      add(listenerOrNull);
    }
  }
  public String toString() {
    Object[] lList = this.listenerList;
    String s = "EventListenerList: ";
    s = s + lList.length + " listeners: ";
    for (int i = 0; i < lList.length; i++) {
      s = s + " listener " + lList[(i + 1)];
    }
    return s;
  }
}
