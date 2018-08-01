package org.exec.core.nature;

public  interface PropertyHolder extends NatureExtendable {
    Object getProperty(String paramString);
    void setProperty(String paramString, Object paramObject);
}
