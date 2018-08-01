package org.exolab.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exec.xml.BackwardCompatibilityContext;
import org.exec.xml.InternalContext;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.xml.location.XPathLocation;
import org.exolab.xml.validators.ClassValidator;

public class Validator implements ClassValidator {
  private static final Log LOG = LogFactory.getLog(Validator.class);
  public void validate(Object object)
    throws ValidationException {
    validate(object, (ValidationContext)null);
  }
  public void validate(Object object, ValidationContext context)
    throws ValidationException {
    if (object == null) {
      throw new ValidationException("Cannot validate a null Object.");
    }
    if (context == null) {
      ValidationContext v2 = new ValidationContext();
      InternalContext ic = new BackwardCompatibilityContext();
      ic.setClassLoader(object.getClass().getClassLoader());
      v2.setInternalContext(ic);
      validate(object, v2);
      return;
    }
    if (context.getClassDescriptorResolver() == null) {
      String message = "ClassDescriptorResolver from context must not be null!";
      LOG.warn(message);
      throw new IllegalStateException(message);
    }
    XMLClassDescriptor classDesc = null;
    if (!MarshalFramework.isPrimitive(object.getClass())) {
      try {
        classDesc = (XMLClassDescriptor)context.getClassDescriptorResolver().resolve(object.getClass());
      }
      catch (ResolverException rx) {
        throw new ValidationException(rx);
      }
    }
    if (classDesc == null) {
      return;
    }
    XMLFieldDescriptor fieldDesc = null;
    try {
      TypeValidator validator = classDesc.getValidator();
      if (validator != null) {
        validator.validate(object, context);
      }
      else {
        FieldDescriptor[] fields = classDesc.getFields();
        if (fields != null) {
          for (int i = 0; i < fields.length; i++) {
            fieldDesc = (XMLFieldDescriptor)fields[i];
            if (fieldDesc != null) {
              FieldValidator fieldValidator = fieldDesc.getValidator();
              if (fieldValidator != null) {
                fieldValidator.validate(object, context);
              }
            }
          }
        }
      }
    }
    catch (ValidationException vx) {
      XPathLocation loc = (XPathLocation)vx.getLocation();
      if (loc == null) {
        loc = new XPathLocation();
        vx.setLocation(loc);
        if (fieldDesc != null) {
          if (fieldDesc.getNodeType() == NodeType.Attribute) {
            loc.addAttribute(fieldDesc.getXMLName());
          } else {
            loc.addChild(fieldDesc.getXMLName());
          }
        }
      }
      if (classDesc.getXMLName() != null) {
        loc.addParent(classDesc.getXMLName());
      }
      throw vx;
    }
  }
  public void checkUnresolvedIdrefs(ValidationContext context)
    throws ValidationException {
    if (context.getUnresolvedIdRefs().size() > 0)
    {
      String err = "Unresolved IDREfs: " + context.getUnresolvedIdRefs().toString();
      throw new ValidationException(err);
    }
  }
}
