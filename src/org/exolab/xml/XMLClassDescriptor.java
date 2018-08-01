package org.exolab.xml;

import org.exolab.mapping.ClassDescriptor;

public interface XMLClassDescriptor extends ClassDescriptor {
    XMLFieldDescriptor[] getAttributeDescriptors();
    XMLFieldDescriptor getContentDescriptor();
    XMLFieldDescriptor[] getElementDescriptors();
    XMLFieldDescriptor getFieldDescriptor(String paramString1, String paramString2, NodeType paramNodeType);
    String getNameSpacePrefix();
    String getNameSpaceURI();
    TypeValidator getValidator();
    String getXMLName();
    boolean canAccept(String paramString1, String paramString2, Object paramObject);
    void checkDescriptorForCorrectOrderWithinSequence(XMLFieldDescriptor paramXMLFieldDescriptor, UnmarshalState paramUnmarshalState, String paramString)
    throws ValidationException;
    boolean isChoice();
}
