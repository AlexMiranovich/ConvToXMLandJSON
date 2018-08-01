package org.exec.core.util;

import java.util.IdentityHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CycleBreaker {
  private static final IdentityHashMap<Thread, IdentityHashMap<Object, Object>> _threadHash = new IdentityHashMap();
  private static final ReentrantReadWriteLock _lock = new ReentrantReadWriteLock();
  public static boolean startingToCycle(Object beingHashed) {
    if (beingHashed == null) {
      return false;
    }
    _lock.readLock().lock();
    IdentityHashMap<Object, Object> hthr = (IdentityHashMap)_threadHash.get(Thread.currentThread());
    _lock.readLock().unlock();
    if (hthr == null) {
      hthr = new IdentityHashMap();
      hthr.put(beingHashed, beingHashed);
      _lock.writeLock().lock();
      _threadHash.put(Thread.currentThread(), hthr);
      _lock.writeLock().unlock();
      return false;
    }
    Object objhandle = hthr.get(beingHashed);
    if (objhandle == null) {
      hthr.put(beingHashed, beingHashed);
      return false;
    }
    return true;
  }
  public static void releaseCycleHandle(Object beingHashed) {
    if (beingHashed == null) {
      return;
    }
    Thread currentThread = Thread.currentThread();
    _lock.readLock().lock();
    IdentityHashMap<Object, Object> hthr = (IdentityHashMap)_threadHash.get(currentThread);
    _lock.readLock().unlock();
    if (hthr != null) {
      if (hthr.containsKey(beingHashed)) {
        hthr.remove(beingHashed);
        if (hthr.size() == 0) {
          _lock.writeLock().lock();
          _threadHash.remove(currentThread);
          _lock.writeLock().unlock();
        }
      }
    }
  }
}
