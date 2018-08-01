
package org.exolab.xml;

public interface AttributeSetInterface {
    int getIndex(String paramString1, String paramString2);
    String getName(int paramInt);
    String getNamespace(int paramInt);
    int getSize();
    String getValue(int paramInt);
    String getValue(String paramString);
    String getValue(String paramString1, String paramString2);
}
