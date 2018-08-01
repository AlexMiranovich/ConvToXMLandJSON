package org.exolab.mapping.xml;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.xml.MarshalException;
import org.exolab.xml.Marshaller;
import org.exolab.xml.Unmarshaller;
import org.exolab.xml.ValidationException;
import org.exolab.xml.Validator;
import org.xml.sax.ContentHandler;

public class Include implements Serializable {
  private String _href;
  public String getHref()
  {
    return this._href;
  }
  public boolean isValid() {
    try {
      validate();
    }
    catch (ValidationException vex) {
      return false;
    }
    return true;
  }
  public void marshal(Writer out)
    throws MarshalException, ValidationException {
    Marshaller.marshal(this, out);
  }
  public void marshal(ContentHandler handler)
    throws IOException, MarshalException, ValidationException {
    Marshaller.marshal(this, handler);
  }
  public void setHref(String href)
  {
    this._href = href;
  }
  public static Include unmarshal(Reader reader)
    throws MarshalException, ValidationException {
    return (Include)Unmarshaller.unmarshal(Include.class, reader);
  }
  public void validate()
    throws ValidationException {
    Validator validator = new Validator();
    validator.validate(this);
  }
}
