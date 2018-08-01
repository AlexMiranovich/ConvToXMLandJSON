package org.exolab.xml;

public  interface OutputFormat {
  String XML = "xml";
  void setMethod(String paramString);
  void setIndenting(boolean paramBoolean);
  void setPreserveSpace(boolean paramBoolean);
  Object getFormat();
  void setDoctype(String paramString1, String paramString2);
  void setOmitXMLDeclaration(boolean paramBoolean);
  void setOmitDocumentType(boolean paramBoolean);
  void setEncoding(String paramString);
}
