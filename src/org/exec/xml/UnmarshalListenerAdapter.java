package org.exec.xml;

public class UnmarshalListenerAdapter implements UnmarshalListener {
  private UnmarshalListener _oldListener;
  public void setOldListener(UnmarshalListener exolabListener) {
    this._oldListener = (UnmarshalListener) exolabListener;
  }
  public void attributesProcessed(Object target, Object parent) {
    if (this._oldListener != null) {
      this._oldListener.attributesProcessed(target, parent);
    }
  }
  public void fieldAdded(String fieldName, Object parent, Object child) {
    if (this._oldListener != null) {
      this._oldListener.fieldAdded(fieldName, parent, child);
    }
  }
  public void initialized(Object target, Object parent) {
    if (this._oldListener != null) {
      this._oldListener.initialized(target, parent);
    }
  }
  public void unmarshalled(Object target, Object parent) {
    if (this._oldListener != null) {
      this._oldListener.unmarshalled(target, parent);
    }
  }

  public void setUnmarshalListener(UnmarshalListener listener) {

  }
}
