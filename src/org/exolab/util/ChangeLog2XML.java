package org.exolab.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import org.exec.xml.InternalContext;
import org.exolab.xml.Marshaller;
import org.exolab.xml.XMLContext;

public class ChangeLog2XML {
  private static final String DEFAULT_FILE = "CHANGELOG";
  private static final String DEFAULT_OUTPUT = "changelog.xml";
  private static final String L_PAREN = "(";
  private static final String R_PAREN = ")";
  private static final String DETAILS_TOKEN = "Details:";
  private static final String VERSION_SEPARATOR = "---";
  private static final String VERSION_TOKEN = "Version";
  private InternalContext _internalContext;
  
  public void setInternalContext(InternalContext internalContext)
  {
    this._internalContext = internalContext;
  }
  private Changelog parse(File file)
    throws FileNotFoundException, IOException, ParseException {
    if (!file.exists()) {
      throw new IllegalArgumentException("The argument 'file' must not be null!");
    }
    FileReader fileReader = new FileReader(file);
    BufferedReader reader = new BufferedReader(fileReader);
    String line = null;
    boolean checkForVersion = false;
    boolean inEntry = false;
    String prevLine = null;
    StringBuffer buffer = null;
    String details = null;
    Changelog changelog = new Changelog();
    Release release = null;
    while ((line = reader.readLine()) != null) {
      if (checkForVersion)
      {
        checkForVersion = false;
        if (line.startsWith("---"))
        {
          release = new Release();
          String version = prevLine;
          
          version = version.substring("Version".length()).trim();
          release.setVersion(version);
          changelog.addRelease(release);
        }
        else
        {
          inEntry = true;
          buffer = new StringBuffer();
          buffer.append(prevLine.trim());
        }
      }
      else if (line.length() == 0)
      {
        if (inEntry)
        {
          Entry entry = new Entry();
          String value = buffer.toString();
          if (value.startsWith("-")) {
            value = value.substring(1);
          }
          int idx = value.indexOf(':');
          if ((idx >= 0) && (idx < 10))
          {
            String component = value.substring(0, idx);
            entry.setComponent(component);
            value = value.substring(idx + 1);
          }
          if (details != null) {
            entry.setDetails(details);
          }
          int lastLength = prevLine.length();
          if ((prevLine.startsWith("(")) && (prevLine.endsWith(")")))
          {
            prevLine = prevLine.substring(1, lastLength - 1);
            idx = prevLine.indexOf('-');
            if (idx >= 0)
            {
              entry.setCommitter(prevLine.substring(0, idx).trim());
              entry.setDate(prevLine.substring(idx + 1).trim());
              
              value = value.substring(0, value.length() - lastLength);
            }
          }
          entry.setValue(value.trim());
          
          release.addEntry(entry);
          inEntry = false;
          details = null;
        }
      }
      else if (!inEntry)
      {
        if (line.startsWith("Version"))
        {
          checkForVersion = true;
          prevLine = line;
        }
        else
        {
          inEntry = true;
          buffer = new StringBuffer();
          buffer.append(line.trim());
        }
      }
      else
      {
        line = line.trim();
        if (line.startsWith("Details:"))
        {
          details = line.substring("Details:".length() + 1);
        }
        else
        {
          if (buffer.length() > 0) {
            buffer.append(' ');
          }
          buffer.append(line);
        }
        prevLine = line;
      }
    }
    fileReader.close();
    
    return changelog;
  }
  
  public static void main(String[] args)
  {
    XMLContext xmlContext = new XMLContext();
    ChangeLog2XML parser = xmlContext.createChangeLog2XML();
    try
    {
      File file = new File("CHANGELOG");
      Changelog changelog = parser.parse(file);
      
      file = new File("changelog.xml");
      FileWriter writer = new FileWriter(file);
      
      xmlContext.setProperty("org.exolab.indent", true);
      Marshaller marshaller = xmlContext.createMarshaller();
      marshaller.setWriter(writer);
      
      marshaller.setRootElement("changelog");
      marshaller.setSuppressXSIType(true);
      marshaller.marshal(changelog);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
  
  public static class Changelog
  {
    private ArrayList _releases = new ArrayList();
    
    public void addRelease(Release release)
    {
      if (release != null) {
        this._releases.add(release);
      }
    }
    
    public Release[] getRelease()
    {
      Release[] relArray = new Release[this._releases.size()];
      this._releases.toArray(relArray);
      return relArray;
    }
  }
  
  public static class Release
  {
    private String _version = null;
    private ArrayList _entries = new ArrayList();
    
    public void addEntry(Entry entry)
    {
      if (entry != null) {
        this._entries.add(entry);
      }
    }
    
    public Entry[] getEntry()
    {
      Entry[] entryArray = new Entry[this._entries.size()];
      this._entries.toArray(entryArray);
      return entryArray;
    }
    
    public String getVersion()
    {
      return this._version;
    }
    
    public void setVersion(String version)
    {
      this._version = version;
    }
  }
  
  public static class Entry
  {
    private String _component = null;
    private String _details = null;
    private String _value = null;
    private String _committer = null;
    private String _date = null;
    
    public String getCommitter()
    {
      return this._committer;
    }
    
    public String getComponent()
    {
      return this._component;
    }
    
    public String getDate()
    {
      return this._date;
    }
    
    public String getDetails()
    {
      return this._details;
    }
    
    public String getValue()
    {
      return this._value;
    }
    
    public void setCommitter(String committer)
    {
      this._committer = committer;
    }
    
    public void setComponent(String component)
    {
      this._component = component;
    }
    
    public void setDate(String date)
    {
      this._date = date;
    }
    
    public void setDetails(String details)
    {
      this._details = details;
    }
    
    public void setValue(String value)
    {
      this._value = value;
    }
  }
}
