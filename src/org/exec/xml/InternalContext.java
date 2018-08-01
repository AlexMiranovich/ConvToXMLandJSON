package org.exec.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.exolab.mapping.Mapping;
import org.exolab.mapping.MappingException;
import org.exolab.mapping.MappingLoader;
import org.exolab.util.RegExpEvaluator;
import org.exolab.xml.*;
import org.exolab.xml.OutputFormat;
import org.exolab.xml.Serializer;
import org.exolab.xml.util.ResolverStrategy;
import org.xml.sax.DocumentHandler;
import org.xml.sax.Parser;
import org.xml.sax.XMLReader;

public interface InternalContext {
    void addMapping(Mapping paramMapping)
    throws MappingException;
    void addClass(Class paramClass)
    throws ResolverException;
    void addClasses(Class[] paramArrayOfClass)
    throws ResolverException;
   void addPackage(String paramString)
    throws ResolverException;
   void addPackages(String[] paramArrayOfString)
    throws ResolverException;
   void setResolver(XMLClassDescriptorResolver paramXMLClassDescriptorResolver);
   void setProperty(String paramString, Object paramObject);
   Object getProperty(String paramString);
   XMLNaming getXMLNaming();
  XMLNaming getXMLNaming(ClassLoader paramClassLoader);
  JavaNaming getJavaNaming();
   Parser getParser();
   Parser getParser(String paramString);
  XMLReader getXMLReader();
   XMLReader getXMLReader(String paramString);
   NodeType getPrimitiveNodeType();
   RegExpEvaluator getRegExpEvaluator();
   Serializer getSerializer();
   OutputFormat getOutputFormat();
   DocumentHandler getSerializer(OutputStream paramOutputStream)
    throws IOException;
   DocumentHandler getSerializer(Writer paramWriter)
    throws IOException;
   XMLClassDescriptorResolver getXMLClassDescriptorResolver();
   Introspector getIntrospector();
   ResolverStrategy getResolverStrategy();
   void setResolverStrategy(ResolverStrategy paramResolverStrategy);
  void setMappingLoader(MappingLoader paramMappingLoader);
   MappingLoader getMappingLoader();
  void setJavaNaming(JavaNaming paramJavaNaming);
   void setXMLNaming(XMLNaming paramXMLNaming);
   void setProperty(String paramString, boolean paramBoolean);
   Boolean getBooleanProperty(String paramString);
   String getStringProperty(String paramString);
   void setClassLoader(ClassLoader paramClassLoader);
   void setXMLClassDescriptorResolver(XMLClassDescriptorResolver paramXMLClassDescriptorResolver);
   void setIntrospector(Introspector paramIntrospector);
   ClassLoader getClassLoader();
   boolean getLenientIdValidation();
   boolean getLenientSequenceOrder();
   Boolean getLoadPackageMapping();
   void setLoadPackageMapping(Boolean paramBoolean);
   Boolean getUseIntrospector();
   void setUseIntrospector(Boolean paramBoolean);
   boolean marshallingValidation();
  boolean strictElements();
}
