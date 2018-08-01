package org.exolab.xml.util;

import java.util.Map;
import org.exolab.xml.ResolverException;

public interface ResolverPackageCommand {
  Map resolve(String paramString, Map paramMap)
    throws ResolverException;
}
