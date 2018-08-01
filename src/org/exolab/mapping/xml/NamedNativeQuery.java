package org.exolab.mapping.xml;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import org.exolab.xml.MarshalException;
import org.exolab.xml.Marshaller;
import org.exolab.xml.Unmarshaller;
import org.exolab.xml.ValidationException;
import org.exolab.xml.Validator;
import org.xml.sax.ContentHandler;

public class NamedNativeQuery implements Serializable {
  private String _name;
  private String _resultClass;
  private String _resultSetMapping;
  private String _query;
  private List<QueryHint> _queryHintList;
  public NamedNativeQuery()
  {
    this._queryHintList = new ArrayList();
  }
  public void addQueryHint(QueryHint vQueryHint)
    throws IndexOutOfBoundsException {
    this._queryHintList.add(vQueryHint);
  }
  public void addQueryHint(int index, QueryHint vQueryHint)
    throws IndexOutOfBoundsException {
    this._queryHintList.add(index, vQueryHint);
  }
  public Enumeration<? extends QueryHint> enumerateQueryHint()
  {
    return Collections.enumeration(this._queryHintList);
  }
  public String getName()
  {
    return this._name;
  }
  public String getQuery()
  {
    return this._query;
  }
  public QueryHint getQueryHint(int index)
    throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._queryHintList.size())) {
      throw new IndexOutOfBoundsException("getQueryHint: Index value '" + index + "' not in range [0.." + (this._queryHintList.size() - 1) + "]");
    }
    return (QueryHint)this._queryHintList.get(index);
  }
  public QueryHint[] getQueryHint() {
    QueryHint[] array = new QueryHint[0];
    return (QueryHint[])this._queryHintList.toArray(array);
  }
  public int getQueryHintCount()
  {
    return this._queryHintList.size();
  }
  public String getResultClass()
  {
    return this._resultClass;
  }
  public String getResultSetMapping()
  {
    return this._resultSetMapping;
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
  public Iterator<? extends QueryHint> iterateQueryHint()
  {
    return this._queryHintList.iterator();
  }
  public void marshal(Writer out)
    throws MarshalException, ValidationException {
    Marshaller.marshal(this, out);
  }
  public void marshal(ContentHandler handler)
    throws IOException, MarshalException, ValidationException {
    Marshaller.marshal(this, handler);
  }
  public void removeAllQueryHint()
  {
    this._queryHintList.clear();
  }
  public boolean removeQueryHint(QueryHint vQueryHint) {
    boolean removed = this._queryHintList.remove(vQueryHint);
    return removed;
  }
  public QueryHint removeQueryHintAt(int index) {
    Object obj = this._queryHintList.remove(index);
    return (QueryHint)obj;
  }
  public void setName(String name)
  {
    this._name = name;
  }
  public void setQuery(String query)
  {
    this._query = query;
  }
  public void setQueryHint(int index, QueryHint vQueryHint)
    throws IndexOutOfBoundsException {
    if ((index < 0) || (index >= this._queryHintList.size())) {
      throw new IndexOutOfBoundsException("setQueryHint: Index value '" + index + "' not in range [0.." + (this._queryHintList.size() - 1) + "]");
    }
    this._queryHintList.set(index, vQueryHint);
  }
  public void setQueryHint(QueryHint[] vQueryHintArray) {
    this._queryHintList.clear();
    for (int i = 0; i < vQueryHintArray.length; i++) {
      this._queryHintList.add(vQueryHintArray[i]);
    }
  }
  public void setResultClass(String resultClass)
  {
    this._resultClass = resultClass;
  }
  public void setResultSetMapping(String resultSetMapping)
  {
    this._resultSetMapping = resultSetMapping;
  }
  public static NamedNativeQuery unmarshal(Reader reader)
    throws MarshalException, ValidationException {
    return (NamedNativeQuery)Unmarshaller.unmarshal(NamedNativeQuery.class, reader);
  }
  public void validate()
    throws ValidationException {
    Validator validator = new Validator();
    validator.validate(this);
  }
}
