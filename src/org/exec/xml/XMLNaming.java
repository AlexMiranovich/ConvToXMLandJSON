package org.exec.xml;

public interface XMLNaming {
    String createXMLName(Class paramClass);
    String toXMLName(String paramString);
    void setNaming(XMLNaming xmlNaming);
}
