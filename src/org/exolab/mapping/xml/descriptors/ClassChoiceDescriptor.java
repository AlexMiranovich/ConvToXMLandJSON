package org.exolab.mapping.xml.descriptors;

import org.exolab.mapping.AccessMode;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.FieldHandler;
import org.exolab.mapping.xml.ClassChoice;
import org.exolab.mapping.xml.Container;
import org.exolab.mapping.xml.FieldMapping;
import org.exolab.xml.FieldValidator;
import org.exolab.xml.NodeType;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.XMLFieldDescriptor;
import org.exolab.xml.XMLFieldHandler;
import org.exolab.xml.util.XMLClassDescriptorImpl;
import org.exolab.xml.util.XMLFieldDescriptorImpl;

public class ClassChoiceDescriptor extends XMLClassDescriptorImpl {
  private boolean _elementDefinition;
  private String _nsPrefix;
  private String _nsURI;
  private String _xmlName;
  private XMLFieldDescriptor _identity;
  public ClassChoiceDescriptor() {
    this._nsURI = "http://exec.exolab.org/";
    this._elementDefinition = false;
    setCompositorAsChoice();
    XMLFieldDescriptorImpl desc = null;
    FieldHandler handler = null;
    FieldValidator fieldValidator = null;
    desc = new XMLFieldDescriptorImpl(FieldMapping.class, "_fieldMappingList", "field", NodeType.Element);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        ClassChoice target = (ClassChoice)object;
        return target.getFieldMapping();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          ClassChoice target = (ClassChoice)object;
          target.addFieldMapping((FieldMapping)value);
        } catch (Exception ex) {
          throw new IllegalStateException(ex.toString());
        }
      }
      public void resetValue(Object object)
        throws IllegalStateException, IllegalArgumentException {
        try {
          ClassChoice target = (ClassChoice)object;
          target.removeAllFieldMapping();
        } catch (Exception ex) {
          throw new IllegalStateException(ex.toString());
        }
      }
      public Object newInstance(Object parent)
      {
        return null;
      }
    };
    desc.setSchemaType("list");
    desc.setComponentType("org.exolab.mapping.xml.FieldMapping");
    desc.setHandler(handler);
    desc.setNameSpaceURI("http://exec.exolab.org/");
    desc.setMultivalued(true);
    addFieldDescriptor(desc);
    addSequenceElement(desc);
    fieldValidator = new FieldValidator();
    fieldValidator.setMinOccurs(0);
    desc.setValidator(fieldValidator);
    desc = new XMLFieldDescriptorImpl(Container.class, "_containerList", "container", NodeType.Element);
    handler = new XMLFieldHandler() {
      public Object getValue(Object object)
        throws IllegalStateException {
        ClassChoice target = (ClassChoice)object;
        return target.getContainer();
      }
      public void setValue(Object object, Object value)
        throws IllegalStateException, IllegalArgumentException {
        try {
          ClassChoice target = (ClassChoice)object;
          target.addContainer((Container)value);
        } catch (Exception ex) { throw new IllegalStateException(ex.toString());
        }
      }
      public void resetValue(Object object)
        throws IllegalStateException, IllegalArgumentException {
        try {
          ClassChoice target = (ClassChoice)object;
          target.removeAllContainer();
        } catch (Exception ex) {
          throw new IllegalStateException(ex.toString());
        }
      }
      public Object newInstance(Object parent)
      {
        return null;
      }
    };
    desc.setSchemaType("list");
    desc.setComponentType("org.exolab.castor.mapping.xml.Container");
    desc.setHandler(handler);
    desc.setNameSpaceURI("http://castor.exolab.org/");
    desc.setMultivalued(true);
    addFieldDescriptor(desc);
    addSequenceElement(desc);
    fieldValidator = new FieldValidator();
    fieldValidator.setMinOccurs(0);
    desc.setValidator(fieldValidator);
  }
  public AccessMode getAccessMode()
  {
    return null;
  }
  public FieldDescriptor getIdentity()
  {
    return this._identity;
  }
  public Class getJavaClass()
  {
    return ClassChoice.class;
  }
  public String getNameSpacePrefix()
  {
    return this._nsPrefix;
  }
  public String getNameSpaceURI()
  {
    return this._nsURI;
  }
  public TypeValidator getValidator()
  {
    return this;
  }
  public String getXMLName()
  {
    return this._xmlName;
  }
  public boolean isElementDefinition()
  {
    return this._elementDefinition;
  }
}
