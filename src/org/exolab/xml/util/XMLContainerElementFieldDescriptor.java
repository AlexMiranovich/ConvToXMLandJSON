package org.exolab.xml.util;

import org.exolab.mapping.MappingException;
import org.exolab.xml.NodeType;

public class XMLContainerElementFieldDescriptor extends XMLFieldDescriptorImpl {
  public XMLContainerElementFieldDescriptor(XMLFieldDescriptorImpl fieldDesc, NodeType primitiveNodeType)
    throws MappingException {
    super(fieldDesc, fieldDesc.getXMLName(), fieldDesc.getNodeType(), primitiveNodeType);
    setFieldType(ContainerElement.class);
    setNameSpaceURI(fieldDesc.getNameSpaceURI());
    setNameSpacePrefix(fieldDesc.getNameSpacePrefix());
  }
}
