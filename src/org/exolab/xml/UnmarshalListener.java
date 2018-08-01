package org.exolab.xml;

public  interface UnmarshalListener {
  void initialized(Object paramObject);
  void attributesProcessed(Object paramObject);
  void fieldAdded(String paramString, Object paramObject1, Object paramObject2);
  void unmarshalled(Object paramObject);
  void setUnmarshalListener(UnmarshalListener listener);
}
