package org.exolab.xml.validators;

import org.exolab.xml.TypeValidator;
import org.exolab.xml.ValidationContext;
import org.exolab.xml.ValidationException;

public class IdValidator extends StringValidator implements TypeValidator {
  public void validate(String value, ValidationContext context)
    throws ValidationException {
    super.validate(value, context);
  }
  public void validate(Object object)
    throws ValidationException {
    validate(object, (ValidationContext)null);
  }
  public void validate(Object object, ValidationContext context)
    throws ValidationException {
    if (object == null) {
      String err = "IdValidator cannot validate a null object.";
      throw new ValidationException(err);
    }
    String value = null;
    if (!(object instanceof String)) {
      throw new ValidationException("IDs should be of type String");
    }
    value = (String)object;
    if (value.equals("")) {
      String err = "Invalid ID value: '' is not a valid value.";
      throw new ValidationException(err);
    }
    context.addID(value);
  }
}
