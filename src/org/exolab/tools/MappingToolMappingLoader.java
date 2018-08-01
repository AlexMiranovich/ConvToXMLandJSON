package org.exolab.tools;

import java.lang.reflect.Array;
import org.exec.xml.JavaNaming;
import org.exolab.mapping.loader.AbstractMappingLoader;

public final class MappingToolMappingLoader {
  private JavaNaming _javaNaming;
  public MappingToolMappingLoader(JavaNaming javaNaming)
  {
    this._javaNaming = javaNaming;
  }
  public boolean returnsArray(Class clazz, String fieldName, Class type) {
    try {
      Class array = null;
      if (type.isArray()) {
        array = type;
      } else {
        array = Array.newInstance(type, 0).getClass();
      }
      String method = this._javaNaming.getGetMethodNameForField(fieldName);
      boolean isGet = true;
      if (AbstractMappingLoader.findAccessor(clazz, method, array, isGet) != null) {
        return true;
      }
    }
    catch (Exception ex) {}
    return false;
  }
  public boolean canFindAccessors(Class clazz, String fieldName, Class type) {
    try {
      String methodName = null;
      methodName = this._javaNaming.getGetMethodNameForField(fieldName);
      boolean isGet = true;
      if (AbstractMappingLoader.findAccessor(clazz, methodName, type, isGet) != null) {
        return true;
      }
      isGet = false;
      methodName = this._javaNaming.getSetMethodNameForField(fieldName);
      if (AbstractMappingLoader.findAccessor(clazz, methodName, type, isGet) != null) {
        return true;
      }
      methodName = this._javaNaming.getAddMethodNameForField(fieldName);
      if (AbstractMappingLoader.findAccessor(clazz, methodName, type, isGet) != null) {
        return true;
      }
    } catch (Exception ex) {}
    return false;
  }
}
