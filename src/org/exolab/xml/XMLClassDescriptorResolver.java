package org.exolab.xml;

import java.util.Iterator;
import org.exec.xml.InternalContext;
import org.exolab.mapping.ClassDescriptor;
import org.exolab.xml.util.ResolverStrategy;

public interface XMLClassDescriptorResolver
  extends ClassDescriptorResolver {
   void setInternalContext(InternalContext paramInternalContext);
   void setUseIntrospection(boolean paramBoolean);
   void setLoadPackageMappings(boolean paramBoolean);
   void setClassLoader(ClassLoader paramClassLoader);
   void setResolverStrategy(ResolverStrategy paramResolverStrategy);
   XMLClassDescriptor resolve(String paramString)
    throws ResolverException;
   XMLClassDescriptor resolve(String paramString, ClassLoader paramClassLoader)
    throws ResolverException;
   XMLClassDescriptor resolveByXMLName(String paramString1, String paramString2, ClassLoader paramClassLoader)
    throws ResolverException;
   Iterator<ClassDescriptor> resolveAllByXMLName(String paramString1, String paramString2, ClassLoader paramClassLoader)
    throws ResolverException;
  void addClass(String paramString)
    throws ResolverException;
   void addClasses(String[] paramArrayOfString)
    throws ResolverException;
   void addClass(Class<?> paramClass)
    throws ResolverException;
   void addClasses(Class<?>[] paramArrayOfClass)
    throws ResolverException;
   void addPackage(String paramString)
    throws ResolverException;
   void addPackages(String[] paramArrayOfString)
    throws ResolverException;
  /**
   * @deprecated
   */
   void loadClassDescriptors(String paramString)
    throws ResolverException;
   void cleanDescriptorCache();
    void setIntrospector(Introspector introspector);
}
