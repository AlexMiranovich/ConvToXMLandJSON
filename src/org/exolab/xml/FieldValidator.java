package org.exolab.xml;

import java.lang.reflect.Array;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import org.exec.xml.InternalContext;
import org.exolab.mapping.FieldHandler;
import org.exolab.xml.location.XPathLocation;

public class FieldValidator extends Validator
{
  private static final String ERROR_NAME = "-error-if-this-is-used-";
  private static final int DEFAULT_MIN = 0;
  private static final int DEFAULT_MAX = -1;
  private int _minOccurs = 0;
  private int _maxOccurs = -1;
  private XMLFieldDescriptor _descriptor = null;
  private TypeValidator _validator = null;
  public FieldValidator() {}
  public FieldValidator(TypeValidator validator)
  {
    this._validator = validator;
  }
  public int getMinOccurs()
  {
    return this._minOccurs;
  }
  public int getMaxOccurs()
  {
    return this._maxOccurs;
  }
  public TypeValidator getTypeValidator()
  {
    return this._validator;
  }
  public boolean hasTypeValidator()
  {
    return this._validator != null;
  }
  public void setMinOccurs(int minOccurs)
  {
    this._minOccurs = (minOccurs < 0 ? 0 : minOccurs);
  }
  public void setMaxOccurs(int maxOccurs)
  {
    this._maxOccurs = maxOccurs;
  }
  public void setDescriptor(XMLFieldDescriptor descriptor)
  {
    this._descriptor = descriptor;
  }
  public void setValidator(TypeValidator validator)
  {
    this._validator = validator;
  }
  public void validate(Object object, ValidationContext context)
    throws ValidationException {
    if ((this._descriptor == null) || (object == null) || (context.isValidated(object))) {
      return;
    }
    if (this._descriptor.isTransient()) {
      return;
    }
    FieldHandler handler = this._descriptor.getHandler();
    if (handler == null) {
      return;
    }
    Object value = handler.getValue(object);
    if (value == null) {
      if ((!this._descriptor.isRequired()) || (this._descriptor.isNillable())) {
        return;
      }
      if ((this._descriptor.isRequired()) && (this._descriptor.getSchemaType() != null) && (this._descriptor.getSchemaType().equals("IDREF")) && (context.getInternalContext().getLenientIdValidation())) {
        return;
      }
      StringBuffer buff = new StringBuffer();
      buff.append("The field '" + this._descriptor.getFieldName() + "' ");
      if (!"-error-if-this-is-used-".equals(this._descriptor.getXMLName())) {
        buff.append("(whose xml name is '" + this._descriptor.getXMLName() + "') ");
      }
      buff.append("is a required field of class '" + object.getClass().getName());
      throw new ValidationException(buff.toString());
    }
    if (this._descriptor.isReference()) {
      if (this._validator != null) {
        this._validator.validate(value, context);
      }
      return;
    }
    if (context != null) {
      if (context.isValidated(object)) {
        return;
      }
      context.addValidated(object);
    }
    Class type = value.getClass();
    int size = 1;
    long occurence = -1L;
    try {
      if (type.isArray()) {
        if (type.getComponentType() != Byte.TYPE) {
          size = Array.getLength(value);
          if (this._validator != null) {
            for (int i = 0; i < size; i++) {
              occurence = i + 1;
              this._validator.validate(Array.get(value, i), context);
            }
          } else {
            for (int i = 0; i < size; i++) {
              super.validate(Array.get(value, i), context);
            }
          }
        }
      }
      else if ((value instanceof Enumeration)) {
        size = 0;
        Enumeration enumeration = (Enumeration)value;
        while (enumeration.hasMoreElements()) {
          size++;
          validateInstance(context, enumeration.nextElement());
        }
      }
      else if ((value instanceof Vector)) {
        Vector vector = (Vector)value;
        size = vector.size();
        for (int i = 0; i < size; i++) {
          occurence = i + 1;
          validateInstance(context, vector.elementAt(i));
        }
      }
      else if ((value instanceof List)) {
        List list = (List)value;
        size = list.size();
        for (int i = 0; i < size; i++) {
          occurence = i + 1;
          validateInstance(context, list.get(i));
        }
      }
      else {
        validateInstance(context, value);
      }
    }
    catch (ValidationException vx) {
      String err = "The following exception occured while validating field: " + this._descriptor.getFieldName() + " of class: " + object.getClass().getName();
      
      ValidationException validationException = new ValidationException(err, vx);
      addLocationInformation(this._descriptor, validationException, occurence);
      throw validationException;
    }
    if ((size < this._minOccurs) && ((size != 0) || (this._descriptor.isRequired()))) {
      StringBuffer buff = new StringBuffer();
      buff.append("A minimum of " + this._minOccurs + " " + this._descriptor.getFieldName() + " object(s) ");
      if (!"-error-if-this-is-used-".equals(this._descriptor.getXMLName())) {
        buff.append("(whose xml name is '" + this._descriptor.getXMLName() + "') ");
      }
      buff.append("are required for class: " + object.getClass().getName());
      throw new ValidationException(buff.toString());
    }
    if ((this._maxOccurs >= 0) && (size > this._maxOccurs)) {
      StringBuffer buff = new StringBuffer();
      buff.append("A maximum of " + this._maxOccurs + " " + this._descriptor.getFieldName() + " object(s) ");
      if (!"-error-if-this-is-used-".equals(this._descriptor.getXMLName())) {
        buff.append("(whose xml name is '" + this._descriptor.getXMLName() + "') ");
      }
      buff.append("are allowed for class: " + object.getClass().getName() + ".");
      throw new ValidationException(buff.toString());
    }
    if (context != null) {
      context.removeValidated(object);
    }
  }
  private void validateInstance(ValidationContext context, Object value)
    throws ValidationException {
    if (this._validator != null) {
      this._validator.validate(value, context);
    } else {
      super.validate(value, context);
    }
  }
  private void addLocationInformation(XMLFieldDescriptor fieldDescriptor, ValidationException e, long occurence) {
    XPathLocation loc = (XPathLocation)e.getLocation();
    if (loc == null) {
      loc = new XPathLocation();
      e.setLocation(loc);
      String xmlName = fieldDescriptor.getXMLName();
      if (occurence > 0L) {
        xmlName = xmlName + "[" + occurence + "]";
      }
      if (fieldDescriptor.getNodeType() == NodeType.Attribute) {
        loc.addAttribute(xmlName);
      } else {
        loc.addChild(xmlName);
      }
    }
  }
}
