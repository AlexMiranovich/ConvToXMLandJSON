package org.exec.xml;

public interface UnmarshalListener {
  void initialized(Object paramObject1, Object paramObject2);
  void attributesProcessed(Object paramObject1, Object paramObject2);
  void fieldAdded(String paramString, Object paramObject1, Object paramObject2);
  void unmarshalled(Object paramObject1, Object paramObject2);
  void setUnmarshalListener(UnmarshalListener listener);

}
