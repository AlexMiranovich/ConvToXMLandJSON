package org.exolab.xml;

import org.exolab.xml.util.XMLClassDescriptorImpl;

class IntrospectedXMLClassDescriptor extends XMLClassDescriptorImpl {
  IntrospectedXMLClassDescriptor(Class type) {
    super(type);
    setIntrospected(true);
  }
  public IntrospectedXMLClassDescriptor(Class type, String xmlName) {
    super(type, xmlName);
    setIntrospected(true);
  }
}
