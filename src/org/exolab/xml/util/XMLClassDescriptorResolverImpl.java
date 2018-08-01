package org.exolab.xml.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exec.xml.InternalContext;
import org.exolab.mapping.ClassDescriptor;
import org.exolab.mapping.MappingLoader;
import org.exolab.xml.Introspector;
import org.exolab.xml.ResolverException;
import org.exolab.xml.XMLClassDescriptor;
import org.exolab.xml.XMLClassDescriptorResolver;
import org.exolab.xml.util.resolvers.ResolveHelpers;

public class XMLClassDescriptorResolverImpl implements XMLClassDescriptorResolver {
  private static final Log LOG = LogFactory.getLog(XMLClassDescriptorResolverImpl.class);
  private DescriptorCacheImpl _descriptorCache;
  private MappingLoader _mappingLoader;
  private ClassLoader _classLoader;
  private Boolean _useIntrospector;
  private Boolean _loadPackageMappings;
  private Introspector _introspector;
  private ResolverStrategy _resolverStrategy;
  public XMLClassDescriptorResolverImpl()
  {
    this._descriptorCache = new DescriptorCacheImpl();
  }
  public void setInternalContext(InternalContext internalContext) {
    this._mappingLoader = internalContext.getMappingLoader();
    this._classLoader = internalContext.getClassLoader();
    this._useIntrospector = internalContext.getUseIntrospector();
    this._loadPackageMappings = internalContext.getLoadPackageMapping();
    this._introspector = internalContext.getIntrospector();
    this._resolverStrategy = internalContext.getResolverStrategy();
  }
  public MappingLoader getMappingLoader()
  {
    return this._mappingLoader;
  }
  public void setClassLoader(ClassLoader loader)
  {
    this._classLoader = loader;
  }
  public void setUseIntrospection(boolean enable)
  {
    this._useIntrospector = Boolean.valueOf(enable);
  }
  public void setLoadPackageMappings(boolean loadPackageMappings){
    this._loadPackageMappings = Boolean.valueOf(loadPackageMappings);
  }
  public void setMappingLoader(MappingLoader mappingLoader) {
    this._mappingLoader = mappingLoader;
    if (mappingLoader != null) {
      Iterator<ClassDescriptor> descriptors = mappingLoader.descriptorIterator();
      while (descriptors.hasNext())
      {
        XMLClassDescriptor descriptor = (XMLClassDescriptor)descriptors.next();
        this._descriptorCache.addDescriptor(descriptor.getJavaClass().getName(), descriptor);
      }
    }
  }
  public void setIntrospector(Introspector introspector)
  {
    this._introspector = introspector;
  }
  public void setResolverStrategy(ResolverStrategy resolverStrategy)
  {
    this._resolverStrategy = resolverStrategy;
  }
  private ResolverStrategy getResolverStrategy() {
    setAttributesIntoStrategy();
    return this._resolverStrategy;
  }
  public ClassDescriptor resolve(Class type)
    throws ResolverException {
    if (type == null) {
      String message = "Type argument must not be null for resolve";
      LOG.warn(message);
      throw new IllegalArgumentException(message);
    }
    if (this._descriptorCache.isMissingDescriptor(type.getName())) {
      if (LOG.isTraceEnabled()) {
        LOG.trace("Descriptor for " + type.getName() + " already marked as *MISSING*.");
      }
      return null;
    }
    if (this._descriptorCache.getDescriptor(type.getName()) != null) {
      return this._descriptorCache.getDescriptor(type.getName());
    }
    ClassLoader l = this._classLoader;
    if (l == null) {
      l = type.getClassLoader();
    }
    if (l == null) {
      l = Thread.currentThread().getContextClassLoader();
    }
    return resolve(type.getName(), l);
  }
  public XMLClassDescriptor resolve(String className)
    throws ResolverException {
    if ((className == null) || (className.length() == 0)) {
      String message = "Cannot resolve a null or zero-length class name.";
      LOG.warn(message);
      throw new IllegalArgumentException(message);
    }
    if (this._descriptorCache.isMissingDescriptor(className)) {
      if (LOG.isTraceEnabled()) {
        LOG.trace("Descriptor for " + className + " already marked as *MISSING*.");
      }
      return null;
    }
    if (this._descriptorCache.getDescriptor(className) != null) {
      return this._descriptorCache.getDescriptor(className);
    }
    ClassLoader l = this._classLoader;
    if (l == null) {
      l = Thread.currentThread().getContextClassLoader();
    }
    return resolve(className, l);
  }
  public XMLClassDescriptor resolve(String className, ClassLoader loader)
    throws ResolverException {
    if ((className == null) || (className.length() == 0)) {
      String message = "Cannot resolve a null or zero-length class name.";
      LOG.warn(message);
      throw new IllegalArgumentException(message);
    }
    if (this._descriptorCache.isMissingDescriptor(className)) {
      if (LOG.isTraceEnabled()) {
        LOG.trace("Descriptor for " + className + " already marked as *MISSING*.");
      }
      return null;
    }
    if (this._descriptorCache.getDescriptor(className) != null) {
      return this._descriptorCache.getDescriptor(className);
    }
    ClassLoader l = loader;
    if (l == null) {
      l = this._classLoader;
    }
    if (l == null) {
      l = Thread.currentThread().getContextClassLoader();
    }
    getResolverStrategy().setProperty("org.exolab.xml.util.ResolverStrategy.ClassLoader", l);
    return (XMLClassDescriptor)getResolverStrategy().resolveClass(this._descriptorCache, className);
  }
  public XMLClassDescriptor resolveByXMLName(String xmlName, String namespaceURI, ClassLoader loader) {
    if ((xmlName == null) || (xmlName.length() == 0)) {
      String message = "Cannot resolve a null or zero-length class name.";
      LOG.warn(message);
      throw new IllegalArgumentException(message);
    }
    List<ClassDescriptor> possibleMatches = this._descriptorCache.getDescriptors(xmlName);
    if (possibleMatches.size() == 0) {
      return null;
    }
    if (possibleMatches.size() == 1) {
      return (XMLClassDescriptor)possibleMatches.get(0);
    }
    for (Iterator<ClassDescriptor> i = possibleMatches.iterator(); i.hasNext();) {
      XMLClassDescriptor descriptor = (XMLClassDescriptor)i.next();
      if (ResolveHelpers.namespaceEquals(namespaceURI, descriptor.getNameSpaceURI())) {
        return descriptor;
      }
    }
    return null;
  }
  public Iterator<ClassDescriptor> resolveAllByXMLName(String xmlName, String namespaceURI, ClassLoader loader) {
    if ((xmlName == null) || (xmlName.length() == 0)) {
      String message = "Cannot resolve a null or zero-length xml name.";
      LOG.warn(message);
      throw new IllegalArgumentException(message);
    }
    return this._descriptorCache.getDescriptors(xmlName).iterator();
  }
  public void addClass(String className) throws ResolverException {
    resolve(className);
  }
  public void addClasses(String[] classNames) throws ResolverException {
    for (int i = 0; i < classNames.length; i++) {
      String className = classNames[i];
      addClass(className);
    }
  }
  public void addClass(Class<?> clazz) throws ResolverException {
    resolve(clazz);
  }
  public void addClasses(Class<?>[] clazzes) throws ResolverException {
    for (int i = 0; i < clazzes.length; i++) {
      Class<?> clazz = clazzes[i];
      addClass(clazz);
    }
  }
  public void addPackage(String packageName) throws ResolverException {
    if ((packageName == null) || (packageName.length() == 0)) {
      String message = "Cannot resolve a null or zero-length package name.";
      LOG.warn(message);
      throw new IllegalArgumentException(message);
    }
    getResolverStrategy().resolvePackage(this._descriptorCache, packageName);
  }
  public void addPackages(String[] packageNames) throws ResolverException {
    for (int i = 0; i < packageNames.length; i++) {
      String packageName = packageNames[i];
      addPackage(packageName);
    }
  }
  public void loadClassDescriptors(String packageName) throws ResolverException {
    String message = "Already deprecated in the interface!";
    LOG.warn(message);
    throw new UnsupportedOperationException();
  }
  private void setAttributesIntoStrategy() {
    ResolverStrategy strategy = this._resolverStrategy;
    strategy.setProperty("org.exolab.xml.util.ResolverStrategy.LoadPackageMappings", this._loadPackageMappings);
    strategy.setProperty("org.exolab.xml.util.ResolverStrategy.useIntrospection", this._useIntrospector);
    strategy.setProperty("org.exolab.xml.util.ResolverStrategy.MappingLoader", this._mappingLoader);
    strategy.setProperty("org.exolab.xml.util.ResolverStrategy.Introspector", this._introspector);
  }
  private static class DescriptorCacheImpl implements ResolverStrategy.ResolverResults {
    private static final Log LOG2 = LogFactory.getLog(DescriptorCacheImpl.class);
    private static final String INTERNAL_CONTAINER_NAME = "-error-if-this-is-used-";
    private final List<String> _missingTypes;
    private final Map<String, ClassDescriptor> _typeMap;
    private final Map<String, List<ClassDescriptor>> _xmlNameMap;
    private final ReentrantReadWriteLock _lock;
    public DescriptorCacheImpl() {
      LOG2.debug("New instance!");
      this._typeMap = new HashMap();
      this._xmlNameMap = new HashMap();
      this._missingTypes = new ArrayList();
      this._lock = new ReentrantReadWriteLock();
    }
    public void addDescriptor(String className, XMLClassDescriptor descriptor) {
      if ((className == null) || (className.length() == 0)) {
        String message = "Class name to insert ClassDescriptor must not be null";
        LOG2.warn(message);
        throw new IllegalArgumentException(message);
      }
      this._lock.writeLock().lock();
      try {
        if (descriptor == null) {
          if (LOG2.isDebugEnabled()) {
            LOG2.debug("Adding class name to missing classes: " + className);
          }
          this._missingTypes.add(className);
        }
        else {
          if (LOG2.isDebugEnabled()) {
            LOG2.debug("Adding descriptor class for: " + className + " descriptor: " + descriptor);
          }
          this._typeMap.put(className, descriptor);
          
          String xmlName = descriptor.getXMLName();
          if ((xmlName == null) || (xmlName.length() == 0)) {
            return;
          }
          if ("-error-if-this-is-used-".equals(xmlName)) {
            return;
          }
          List<ClassDescriptor> descriptorList = (List)this._xmlNameMap.get(xmlName);
          if (descriptorList == null)
          {
            descriptorList = new ArrayList();
            this._xmlNameMap.put(xmlName, descriptorList);
          }
          if (!descriptorList.contains(descriptor)) {
            descriptorList.add(descriptor);
          }
          this._missingTypes.remove(className);
        }
      } finally
      { this._lock.writeLock().unlock();
      }
    }
    public XMLClassDescriptor getDescriptor(String className) {
      this._lock.readLock().lock();
      try {
        if ((className == null) || ("".equals(className)) || (this._missingTypes.contains(className))) {
          return null;
        }
        XMLClassDescriptor ret = (XMLClassDescriptor)this._typeMap.get(className);
        if (LOG2.isDebugEnabled()) {
          LOG2.debug("Get descriptor for: " + className + " found: " + ret);
        }
        return ret;
      }
      finally {
        this._lock.readLock().unlock();
      }
    }
    public List<ClassDescriptor> getDescriptors(String xmlName) {
      this._lock.readLock().lock();
      List<ClassDescriptor> list = (List)this._xmlNameMap.get(xmlName);
      this._lock.readLock().unlock();
      if (list == null) {
        list = new ArrayList();
      } else {
        list = new ArrayList(list);
      }
      return list;
    }
    public boolean isMissingDescriptor(String className) {
      this._lock.readLock().lock();
      try {
        return this._missingTypes.contains(className);
      } finally {
        this._lock.readLock().unlock();
      }
    }
    public void addAllDescriptors(Map descriptors) {
      if ((descriptors == null) || (descriptors.isEmpty())) {
        LOG2.debug("Called addAllDescriptors with null or empty descriptor map");
        return;
      }
      for (Iterator<String> iter = descriptors.keySet().iterator(); iter.hasNext();)
      {
        String clsName = (String)iter.next();
        addDescriptor(clsName, (XMLClassDescriptor)descriptors.get(clsName));
      }
    }
  }
  public void cleanDescriptorCache()
  {
    this._descriptorCache = new DescriptorCacheImpl();
  }
}
