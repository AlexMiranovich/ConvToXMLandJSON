package org.exolab.util;

import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import org.exec.core.util.Messages;

public class CommandLineOptions {
  private Vector _flags = null;
  private Hashtable _optionInfo = null;
  private PrintWriter _errorWriter = null;
  public CommandLineOptions() {
    this._flags = new Vector();
    this._optionInfo = new Hashtable();
    this._errorWriter = new PrintWriter(System.out);
  }
  
  public void addFlag(String flag)
  {
    addFlag(flag, null, null);
  }
  public void addFlag(String flag, String comment)
  {
    addFlag(flag, null, comment, false);
  }
  public void addFlag(String flag, String usageText, String comment)
  {
    addFlag(flag, usageText, comment, false);
  }
  public void addFlag(String flag, String usageText, String comment, boolean optional)
  {
    if (flag == null) {
      return;
    }
    this._flags.addElement(flag);
    
    CmdLineOption opt = new CmdLineOption(flag);
    opt.setComment(comment);
    opt.setUsageText(usageText);
    opt.setOptional(optional);
    this._optionInfo.put(flag, opt);
  }
  
  public Properties getOptions(String[] args)
  {
    Properties options = new Properties();
    String flag = null;
    for (int i = 0; i < args.length; i++) {
      if (args[i].startsWith("-"))
      {
        if (flag != null)
        {
          options.put(flag, args[i]);
          options.put(new Integer(i), args[i]);
        }
        flag = args[i].substring(1);
        if (!this._flags.contains(flag))
        {
          int idx = 1;
          while (idx <= flag.length())
          {
            if (this._flags.contains(flag.substring(0, idx)))
            {
              if (idx < flag.length())
              {
                options.put(flag.substring(0, idx), flag.substring(idx));
                
                break;
              }
            }
            else if (idx == flag.length())
            {
              this._errorWriter.println(Messages.format("misc.invalidCLIOption", "-" + flag));
              
              printUsage(this._errorWriter);
            }
            idx++;
          }
        }
      }
      else
      {
        if (flag != null) {
          options.put(flag, args[i]);
        }
        options.put(new Integer(i), args[i]);
        flag = null;
      }
    }
    if (flag != null) {
      options.put(flag, "no value");
    }
    return options;
  }
  
  public void setComment(String flag, String comment)
  {
    if (flag == null) {
      return;
    }
    CmdLineOption opt = (CmdLineOption)this._optionInfo.get(flag);
    if (opt != null) {
      opt.setComment(comment);
    }
  }
  
  public void setOptional(String flag, boolean optional)
  {
    if (flag == null) {
      return;
    }
    CmdLineOption opt = (CmdLineOption)this._optionInfo.get(flag);
    if (opt != null) {
      opt.setOptional(optional);
    }
  }
  
  public void setUsageInfo(String flag, String usage)
  {
    if (flag == null) {
      return;
    }
    CmdLineOption opt = (CmdLineOption)this._optionInfo.get(flag);
    if (opt != null) {
      opt.setUsageText(usage);
    }
  }
  
  public void printUsage(PrintWriter pw)
  {
    pw.println();
    pw.print(Messages.message("misc.CLIUsage"));
    for (int i = 0; i < this._flags.size(); i++)
    {
      String flag = (String)this._flags.elementAt(i);
      CmdLineOption opt = (CmdLineOption)this._optionInfo.get(flag);
      if (opt.getOptional()) {
        pw.print(" [-");
      } else {
        pw.print(" -");
      }
      pw.print(flag);
      String usage = opt.getUsageText();
      if (usage != null)
      {
        pw.print(' ');
        pw.print(usage);
      }
      if (opt.getOptional()) {
        pw.print(']');
      }
    }
    pw.println();
    pw.flush();
  }
  
  public void printHelp(PrintWriter pw)
  {
    printUsage(pw);
    pw.println();
    if (this._flags.size() > 0)
    {
      pw.println("Flag               Description");
      pw.println("----------------------------------------------");
    }
    for (int i = 0; i < this._flags.size(); i++)
    {
      String flag = (String)this._flags.elementAt(i);
      CmdLineOption opt = (CmdLineOption)this._optionInfo.get(flag);
      
      pw.print('-');
      pw.print(flag);
      
      pw.print(' ');
      
      int spaces = 17 - flag.length();
      while (spaces > 0)
      {
        pw.print(' ');
        spaces--;
      }
      pw.print(opt.getComment());
      
      pw.println();
    }
    pw.println();
    pw.flush();
  }
}
