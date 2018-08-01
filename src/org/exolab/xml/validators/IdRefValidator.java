package org.exolab.xml.validators;

import org.exolab.mapping.ClassDescriptor;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.FieldHandler;
import org.exolab.xml.ClassDescriptorResolver;
import org.exolab.xml.TypeValidator;
import org.exolab.xml.ValidationContext;
import org.exolab.xml.ValidationException;

public class IdRefValidator extends StringValidator implements TypeValidator {
  public void validate(Object object, ValidationContext context)
    throws ValidationException {
    if (object == null) {
      String err = "The object associated with IDREF \"" + object + "\" is null!";
      throw new ValidationException(err);
    }
    String id = null;
    try {
      ClassDescriptorResolver classDescriptorResolver = context.getClassDescriptorResolver();
      ClassDescriptor classDescriptor = classDescriptorResolver.resolve(object.getClass());
      FieldDescriptor fieldDescriptor = classDescriptor.getIdentity();
      FieldHandler fieldHandler = fieldDescriptor.getHandler();
      id = (String)fieldHandler.getValue(object);
    }
    catch (Exception e) {
      String err = "The object associated with IDREF \"" + object + "\" of type " + object.getClass() + " has no ID!";
      throw new ValidationException(err);
    }
    if (id == null) {
      String err = "The object associated with IDREF \"" + object + "\" has no ID!";
      throw new ValidationException(err);
    }
    context.checkIdRef(id);
  }
}
