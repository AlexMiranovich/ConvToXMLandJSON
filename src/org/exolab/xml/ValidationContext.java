package org.exolab.xml;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exec.xml.InternalContext;

public class ValidationContext {
  private static final Log LOG = LogFactory.getLog(ValidationContext.class);
  private InternalContext _internalContext = null;
  private boolean _failFast = true;
  private final Set _validated = new HashSet();
  private final Set _ids = new HashSet();
  private final Set _unresolvedIdrefs = new HashSet();
  public InternalContext getInternalContext()
  {
    return this._internalContext;
  }
  public void setInternalContext(InternalContext internalContext)
  {
    this._internalContext = internalContext;
  }
  public XMLClassDescriptorResolver getClassDescriptorResolver() {
    return this._internalContext.getXMLClassDescriptorResolver();
  }
  public boolean isFailFast()
  {
    return this._failFast;
  }
  public void setFailFast(boolean failFast)
  {
    this._failFast = failFast;
  }
  protected boolean isValidated(Object object) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("Called isValidated(" + object + ")");
    }
    return this._validated.contains(object);
  }
  protected void addValidated(Object object) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("Called addValidated(" + object + ")");
    }
    this._validated.add(object);
  }
  protected void removeValidated(Object object) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("Called removeValidated(" + object + ")");
    }
    this._validated.remove(object);
  }
  public void addID(String id)
    throws ValidationException {
    if (!this._ids.contains(id)) {
      this._ids.add(id);
      this._unresolvedIdrefs.remove(id);
    }
    else if (!this._internalContext.getLenientIdValidation()) {
      throw new ValidationException("ID " + id + " is already used within current document.");
    }
  }
  public boolean checkIdRef(String id) { if (!this._ids.contains(id)) {
      this._unresolvedIdrefs.add(id);
      return false;
    }
    return true;
  }
  public Set getUnresolvedIdRefs()
  {
    return Collections.unmodifiableSet(this._unresolvedIdrefs);
  }
  public void cleanup() {
    this._ids.clear();
    this._validated.clear();
    this._unresolvedIdrefs.clear();
  }
}
