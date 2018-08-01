package org.exolab.xml.descriptors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.exolab.xml.UnmarshalState;
import org.exolab.xml.ValidationException;
import org.exolab.xml.XMLClassDescriptor;
import org.exolab.xml.XMLFieldDescriptor;

public abstract class BaseDescriptor implements XMLClassDescriptor {
  private Map _properties = new HashMap();
  private Set _natures = new HashSet();
  public boolean canAccept(String name, String namespace, Object object)
  {
    return false;
  }
  public void checkDescriptorForCorrectOrderWithinSequence(XMLFieldDescriptor elementDescriptor, UnmarshalState parentState, String xmlName)
    throws ValidationException {}
  public boolean isChoice()
  {
    return false;
  }
  public Object getProperty(String name)
  {
    return this._properties.get(name);
  }
  public void setProperty(String name, Object value)
  {
    this._properties.put(name, value);
  }
  public void addNature(String nature)
  {
    this._natures.add(nature);
  }
  public boolean hasNature(String nature)
  {
    return this._natures.contains(nature);
  }
}
