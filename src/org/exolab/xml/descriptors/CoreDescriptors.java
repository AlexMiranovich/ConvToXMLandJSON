package org.exolab.xml.descriptors;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Vector;
import org.exolab.xml.XMLClassDescriptor;

public final class CoreDescriptors {
  private static final String LIST_CLASS_NAME = "java.util.List";
  private static final String LIST_DESCRIPTOR_NAME = "org.exolab.xml.descriptors.ListClassDescriptor";
  private static final Class LIST_CLASS = getListClass();
  private static final XMLClassDescriptor DATE_DESCRIPTOR = new DateClassDescriptor();
  private static final XMLClassDescriptor ENUMERATION_DESCRIPTOR = new EnumerationDescriptor();
  private static final XMLClassDescriptor LIST_DESCRIPTOR = getListClassDescriptor();
  private static final XMLClassDescriptor LOCALE_DESCRIPTOR = new LocaleDescriptor();
  private static final XMLClassDescriptor SQL_DATE_DESCRIPTOR = new SQLDateClassDescriptor();
  private static final XMLClassDescriptor SQL_TIME_DESCRIPTOR = new SQLTimeClassDescriptor();
  private static final XMLClassDescriptor SQL_TIMESTAMP_DESCRIPTOR = new SQLTimestampClassDescriptor();
  private static final XMLClassDescriptor STRING_DESCRIPTOR = new StringClassDescriptor();
  private static final XMLClassDescriptor VECTOR_DESCRIPTOR = new VectorClassDescriptor();
  
  public static XMLClassDescriptor getDescriptor(Class clazz) {
    if (clazz == null) {
      return null;
    }
    if (clazz == String.class) {
      return STRING_DESCRIPTOR;
    }
    if (clazz == java.util.Date.class) {
      return DATE_DESCRIPTOR;
    }
    if (Enumeration.class.isAssignableFrom(clazz)) {
      return ENUMERATION_DESCRIPTOR;
    }
    if ((clazz == Vector.class) || (Vector.class.isAssignableFrom(clazz))) {
      return VECTOR_DESCRIPTOR;
    }
    if ((LIST_DESCRIPTOR != null) && ((LIST_CLASS == clazz) || (LIST_CLASS.isAssignableFrom(clazz)))) {
      return LIST_DESCRIPTOR;
    }
    if (clazz == Locale.class) {
      return LOCALE_DESCRIPTOR;
    }
    if (clazz == java.sql.Date.class) {
      return SQL_DATE_DESCRIPTOR;
    }
    if (clazz == Time.class) {
      return SQL_TIME_DESCRIPTOR;
    }
    if (clazz == Timestamp.class) {
      return SQL_TIMESTAMP_DESCRIPTOR;
    }
    return null;
  }
  private static XMLClassDescriptor getListClassDescriptor() {
    if (LIST_CLASS == null) {
      return null;
    }
    ClassLoader loader = CoreDescriptors.class.getClassLoader();
    Class descriptorClass = null;
    try {
      if (loader == null) {
        descriptorClass = Class.forName("org.exolab.xml.descriptors.ListClassDescriptor");
      } else {
        descriptorClass = loader.loadClass("org.exolab.xml.descriptors.ListClassDescriptor");
      }
    }
    catch (ClassNotFoundException cnfe) {}
    XMLClassDescriptor listDescriptor = null;
    if (descriptorClass != null) {
      try
      {
        listDescriptor = (XMLClassDescriptor)descriptorClass.newInstance();
      }
      catch (InstantiationException ie) {}catch (IllegalAccessException iae) {}
    } else {
      listDescriptor = null;
    }
    return listDescriptor;
  }
  private static Class getListClass() {
    Class listClass = null;
    ClassLoader loader = null;
    try {
      loader = Vector.class.getClassLoader();
      if (loader == null) {
        listClass = Class.forName("java.util.List");
      } else {
        listClass = loader.loadClass("java.util.List");
      }
    }
    catch (ClassNotFoundException cnfe) {}
    return listClass;
  }
}
