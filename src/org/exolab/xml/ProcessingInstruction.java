package org.exolab.xml;

public class ProcessingInstruction {
  private final String _target;
  private final String _data;
  public ProcessingInstruction(String target, String data) {
    this._target = target;
    this._data = data;
  }
  public String getTarget()
  {
    return this._target;
  }
  public String getData()
  {
    return this._data;
  }
}
