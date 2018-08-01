package org.exolab.xml.validators;

import org.exolab.xml.TypeValidator;
import org.exolab.xml.ValidationContext;
import org.exolab.xml.ValidationException;

public class IdRefsValidator implements TypeValidator {
  private IdRefValidator _idRefValidator;
  public IdRefsValidator()
  {
    this._idRefValidator = new IdRefValidator();
  }
  public void validate(Object object, ValidationContext context)
    throws ValidationException {
    if (object == null) {
      String err = "The object of type IDREFS is null!";
      throw new ValidationException(err);
    }
    Object[] objects = (Object[])object;
    for (int i = 0; i < objects.length; i++) {
      this._idRefValidator.validate(objects[i], context);
    }
  }
}
