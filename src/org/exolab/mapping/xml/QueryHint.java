package org.exolab.mapping.xml;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.xml.MarshalException;
import org.exolab.xml.*;
import org.xml.sax.ContentHandler;

public class QueryHint implements Serializable {
  private String _name;
  private String _value;
  public String getName()
  {
    return this._name;
  }
  public String getValue()
  {
    return this._value;
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
  public void setName(String name)
  {
    this._name = name;
  }
  public void setValue(String value)
  {
    this._value = value;
  }
  public static QueryHint unmarshal(Reader reader)
    throws MarshalException, ValidationException {
    return (QueryHint)Unmarshaller.unmarshal(QueryHint.class, reader);
  }
  public void validate() throws ValidationException {
    Validator validator = new Validator();
    validator.validate(this);
  }
}
