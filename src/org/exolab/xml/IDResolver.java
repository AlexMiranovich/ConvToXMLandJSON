package org.exolab.xml;

import org.exolab.mapping.xml.ClassMapping;

public  interface IDResolver {
  ClassMapping resolve(String paramString);
}
