package org.exolab.xml.location;

import java.io.Serializable;

public class FileLocation implements Location, Serializable {
  private static final long serialVersionUID = 7112551880124131785L;
  private static final String NOT_AVAILABLE = "[not available]";
  private String _filename = null;
  private int _line = -1;
  private int _col = -1;
  public FileLocation() {}
  public FileLocation(String filename)
  {
    this._filename = filename;
  }
  public FileLocation(int line, int column) {
    this._line = line;
    this._col = column;
  }
  public FileLocation(String filename, int line, int column) {
    this._filename = filename;
    this._line = line;
    this._col = column;
  }
  public int getColumnNumber()
  {
    return this._col;
  }
  public String getFilename()
  {
    return this._filename;
  }
  public int getLineNumber()
  {
    return this._line;
  }
  public void setColumnNumber(int column)
  {
    this._col = column;
  }
  public void setFilename(String filename)
  {
    this._filename = filename;
  }
  public void setLineNumber(int line)
  {
    this._line = line;
  }
  public String toString() {
    StringBuffer sb = new StringBuffer("File: ");
    if (this._filename != null) {
      sb.append(this._filename);
    } else {
      sb.append("[not available]");
    }
    sb.append("; line: ");
    if (this._line >= 0) {
      sb.append(this._line);
    } else {
      sb.append("[not available]");
    }
    sb.append("; column: ");
    if (this._col >= 0) {
      sb.append(this._col);
    } else {
      sb.append("[not available]");
    }
    return sb.toString();
  }
}
