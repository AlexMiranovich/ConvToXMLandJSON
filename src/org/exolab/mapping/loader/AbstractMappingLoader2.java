package org.exolab.mapping.loader;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.exolab.mapping.ClassDescriptor;
import org.exolab.mapping.MappingException;
import org.exolab.mapping.MappingLoader;

public abstract class AbstractMappingLoader2 implements MappingLoader {
  private ClassLoader _loader;
  private boolean _allowRedefinitions = false;
  private boolean _loaded = false;
  private List<ClassDescriptor> _descriptors = new Vector();
  private Map<String, ClassDescriptor> _descriptorsByClassname = new Hashtable();
  public AbstractMappingLoader2(ClassLoader loader)
  {
    setClassLoader(loader);
  }
  public final void clear() {
    this._allowRedefinitions = false;
    this._loaded = false;
    this._descriptors.clear();
    this._descriptorsByClassname.clear();
  }
  public final void setClassLoader(ClassLoader loader) {
    if (loader == null) {
      this._loader = getClass().getClassLoader();
    } else {
      this._loader = loader;
    }
  }
  public final ClassLoader getClassLoader()
  {
    return this._loader;
  }
  public final void setAllowRedefinitions(boolean allow)
  {
    this._allowRedefinitions = allow;
  }
  public final boolean isAllowRedefinition()
  {
    return this._allowRedefinitions;
  }
  protected final void addDescriptor(ClassDescriptor descriptor)
    throws MappingException {
    String classname = descriptor.getJavaClass().getName();
    if (this._descriptorsByClassname.containsKey(classname)) {
      if (!isAllowRedefinition()) {
        throw new MappingException("mapping.duplicateDescriptors", classname);
      }
      for (Iterator<ClassDescriptor> iterator = this._descriptors.iterator(); iterator.hasNext();) {
        ClassDescriptor d = (ClassDescriptor)iterator.next();
        if (classname.equals(d.getJavaClass().getName())) {
          iterator.remove();
        }
      }
      this._descriptors.add(descriptor);
    }
    else {
      this._descriptors.add(descriptor);
    }
    this._descriptorsByClassname.put(classname, descriptor);
  }
  public final ClassDescriptor getDescriptor(String classname) {
    if (classname == null) {
      return null;
    }
    return (ClassDescriptor)this._descriptorsByClassname.get(classname);
  }
  public final Iterator<ClassDescriptor> descriptorIterator()
  {
    return this._descriptors.iterator();
  }
  public final List<ClassDescriptor> getDescriptors()
  {
    return this._descriptors;
  }
  protected final boolean loadMapping() {
    if (this._loaded) {
      return false;
    }
    this._loaded = true;
    return true;
  }
}
