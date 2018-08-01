package org.exolab.util;

import java.util.Stack;

public class SafeStack<E> extends Stack<E> {
  private static final long serialVersionUID = 4964881847051572321L;
  public synchronized int search(Object object) {
    for (int i = 0; i < size(); i++) {
      if (object == get(i)) {
        return i + 1;
      }
    }
    return -1;
  }
}
