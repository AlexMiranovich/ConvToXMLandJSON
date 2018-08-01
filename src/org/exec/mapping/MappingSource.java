package org.exec.mapping;

import org.exolab.util.DTDResolver;
import org.xml.sax.InputSource;

public final class MappingSource {
  private final InputSource _source;
  private final String _type;
  private final DTDResolver _resolver;
  public MappingSource(InputSource source, String type, DTDResolver resolver) {
    this._source = source;
    this._type = type;
    this._resolver = resolver;
  }
  public InputSource getSource()
  {
    return this._source;
  }
  public String getType()
  {
    return this._type;
  }
  public DTDResolver getResolver()
  {
    return this._resolver;
  }
}
