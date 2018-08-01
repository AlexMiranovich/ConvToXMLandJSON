package org.exolab.xml.descriptors;

import java.util.Enumeration;
import org.exolab.mapping.FieldHandler;
import org.exolab.xml.NodeType;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.util.XMLClassDescriptorImpl;
import org.exolab.xml.util.XMLFieldDescriptorImpl;

public class EnumerationDescriptor extends XMLClassDescriptorImpl {
  public EnumerationDescriptor() {
    super(Enumeration.class);
    XMLFieldDescriptorImpl desc = new XMLFieldDescriptorImpl(Object.class, "_elements", null, NodeType.Element);
    FieldHandler handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        return object;
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {}
      public Object newInstance(Object parent)
      {
        return null;
      }};
    desc.setHandler(handler);
    desc.setMultivalued(true);
    addFieldDescriptor(desc);
  }
}
