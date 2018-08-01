package org.exolab.xml.validators;

import org.exolab.xml.TypeValidator;
import org.exolab.xml.ValidationContext;
import org.exolab.xml.ValidationException;

public interface ClassValidator extends TypeValidator {
  void validate(Object paramObject, ValidationContext paramValidationContext)
    throws ValidationException;
}
