package org.exolab.net.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.exolab.net.URILocation;

public final class URILocationImpl extends URILocation {
  private String _documentBase = null;
  private String _absoluteURI = null;
  private String _relativeURI = null;
  private Reader _reader = null;
  private InputStream _is = null;
  public URILocationImpl(String href)
  {
    this(href, null);
  }
  public URILocationImpl(String href, String documentBase) {
    if (href == null) {
      throw new IllegalStateException("href must not be null");
    }
    this._absoluteURI = URIUtils.resolveAsString(href, documentBase);
  }
  public URILocationImpl(Reader reader, String href) {
    this(href, null);
    this._reader = reader;
  }
  public URILocationImpl(InputStream is, String href) {
    this(href, null);
    this._is = is;
  }
  public String getAbsoluteURI()
  {
    return this._absoluteURI;
  }
  public String getBaseURI() {
    if (this._documentBase == null) {
      this._documentBase = URIUtils.getDocumentBase(this._absoluteURI);
    }
    return this._documentBase;
  }
  public Reader getReader() throws IOException {
    if (this._reader != null) {
      return this._reader;
    }
    if (this._is != null) {
      return new InputStreamReader(this._is);
    }
    return URIUtils.getReader(this._absoluteURI, null);
  }
  public String getRelativeURI() {
    if (this._relativeURI == null) {
      int idx = getBaseURI().length();
      this._relativeURI = this._absoluteURI.substring(idx);
    }
    return this._relativeURI;
  }
  public String toString()
  {
    return getAbsoluteURI();
  }
}
