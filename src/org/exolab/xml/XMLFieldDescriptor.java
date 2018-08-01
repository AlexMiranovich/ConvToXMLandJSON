package org.exolab.xml;

import java.util.List;
import org.exolab.mapping.FieldDescriptor;

public interface XMLFieldDescriptor extends FieldDescriptor {
   String PROPERTY_XML_SPACE = "xml:space";
   String PROPERTY_XML_LANG = "xml:lang";
   int getConstructorArgumentIndex();
   boolean isConstructorArgument();
   String getLocationPath();
   String getNameSpacePrefix();
   String getNameSpaceURI();
   NodeType getNodeType();
   String getXMLProperty(String paramString);
   String getSchemaType();
   FieldValidator getValidator();
   String getXMLName();
   boolean isContainer();
   boolean isIncremental();
   boolean isMapped();
   boolean isMultivalued();
   boolean isNillable();
   boolean isReference();
   boolean matches(String paramString);
   boolean matches(String paramString1, String paramString2);
   List getSubstitutes();
   void setSubstitutes(List paramList);
   void setDerivedFromXSList(boolean paramBoolean);
   boolean isDerivedFromXSList();
   String getComponentType();
}
