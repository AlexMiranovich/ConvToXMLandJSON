package org.exolab.util;

class CmdLineOption {
  private boolean _optional = false;
  private String _usageText = null;
  private String _comment = null;
  private String _flag = null;
  CmdLineOption(String flag)
  {
    this._flag = flag;
  }
  public String getFlag()
  {
    return this._flag;
  }
  public boolean getOptional()
  {
    return this._optional;
  }
  public String getComment()
  {
    return this._comment;
  }
  public String getUsageText()
  {
    return this._usageText;
  }
  public void setOptional(boolean optional)
  {
    this._optional = optional;
  }
  public void setComment(String comment)
  {
    this._comment = comment;
  }
  public void setUsageText(String usageText)
  {
    this._usageText = usageText;
  }
}
