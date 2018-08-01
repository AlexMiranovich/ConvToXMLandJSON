package org.exolab.xml.validators;

import org.exolab.xml.ValidationContext;
import org.exolab.xml.ValidationException;

public class NameValidator extends StringValidator {
  public static final short NCNAME = 0;
  public static final short NMTOKEN = 1;
  public static final short CDATA = 2;
  private short _type = 0;
  public NameValidator() {}
  public NameValidator(short type)
  {
    this._type = type;
  }
  public void setRequired(boolean required) {}
  public void validate(String value, ValidationContext context)
    throws ValidationException {
    super.validate(value, context);
    switch (this._type) {
    case 2: 
      if (!ValidationUtils.isCDATA(value)) {
        String err = "Name '" + value + "' is not a valid CDATA.";
        throw new ValidationException(err);
      }
      break;
    case 1: 
      if (!ValidationUtils.isNMToken(value))
      {
        String err = "Name '" + value + "' is not a valid NMToken.";
        throw new ValidationException(err);
      }
      break;
    case 3: 
      if (!ValidationUtils.isQName(value))
      {
        String err = "Name '" + value + "' is not a valid QName.";
        throw new ValidationException(err);
      }
      break;
    case 0: 
    default: 
      if (!ValidationUtils.isNCName(value))
      {
        String err = "Name '" + value + "' is not a valid NCName.";
        throw new ValidationException(err);
      }
      break;
    }
  }
  
  public void validate(Object object)
    throws ValidationException
  {
    validate(object, (ValidationContext)null);
  }
  
  public void validate(Object object, ValidationContext context)
    throws ValidationException
  {
    if (object != null) {
      validate(object.toString(), context);
    } else {
      validate(null, context);
    }
  }
}
