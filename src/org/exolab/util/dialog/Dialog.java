package org.exolab.util.dialog;

public interface Dialog {
   boolean confirm(String paramString);
   char confirm(String paramString1, String paramString2);
   char confirm(String paramString1, String paramString2, String paramString3);
   void notify(String paramString);
}
