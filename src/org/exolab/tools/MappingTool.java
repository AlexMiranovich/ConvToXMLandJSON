package org.exolab.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import org.exec.xml.InternalContext;
import org.exolab.mapping.FieldDescriptor;
import org.exolab.mapping.MappingException;
import org.exolab.mapping.loader.CollectionHandlers;
import org.exolab.mapping.loader.Types;
import org.exolab.mapping.xml.BindXml;
import org.exolab.mapping.xml.ClassChoice;
import org.exolab.mapping.xml.ClassMapping;
import org.exolab.mapping.xml.FieldMapping;
import org.exolab.mapping.xml.MapTo;
import org.exolab.mapping.xml.MappingRoot;
import org.exolab.mapping.xml.types.BindXmlNodeType;
import org.exolab.mapping.xml.types.FieldMappingCollectionType;
import org.exolab.util.CommandLineOptions;
import org.exolab.util.dialog.ConsoleDialog;
import org.exolab.xml.Introspector;
import org.exolab.xml.Marshaller;
import org.exolab.xml.NodeType;
import org.exolab.xml.XMLClassDescriptor;
import org.exolab.xml.XMLClassDescriptorResolver;
import org.exolab.xml.XMLContext;
import org.exolab.xml.XMLFieldDescriptor;

public class MappingTool
{
  private static final String UNDERSCORE = "_";
  private Hashtable _mappings;
  private MappingToolMappingLoader _mappingLoader;
  private boolean _forceIntrospection = false;
  private InternalContext _internalContext;
  
  public static void main(String[] args) {
    CommandLineOptions allOptions = new CommandLineOptions();
    allOptions.addFlag("i", "classname", "Sets the input class");
    String desc = "Sets the output mapping filename";
    allOptions.addFlag("o", "filename", desc, true);
    desc = "Force overwriting of files.";
    allOptions.addFlag("f", "", desc, true);
    desc = "Displays this help screen.";
    allOptions.addFlag("h", "", desc, true);
    Properties options = allOptions.getOptions(args);
    if (options.getProperty("h") != null)
    {
      PrintWriter pw = new PrintWriter(System.out, true);
      allOptions.printHelp(pw);
      pw.flush();
      return;
    }
    String classname = options.getProperty("i");
    String mappingName = options.getProperty("o");
    boolean force = options.getProperty("f") != null;
    if (classname == null)
    {
      PrintWriter pw = new PrintWriter(System.out, true);
      allOptions.printUsage(pw);
      pw.flush();
      return;
    }
    try
    {
      XMLContext xmlContext = new XMLContext();
      MappingTool tool = xmlContext.createMappingTool();
      tool.addClass(classname);
      
      Writer writer = null;
      if ((mappingName == null) || (mappingName.length() == 0))
      {
        writer = new PrintWriter(System.out, true);
      }
      else
      {
        File file = new File(mappingName);
        if ((file.exists()) && (!force))
        {
          ConsoleDialog dialog = new ConsoleDialog();
          String message = "The file already exists. Do you wish to overwrite '" + mappingName + "'?";
          if (!dialog.confirm(message)) {
            return;
          }
        }
        writer = new FileWriter(file);
      }
      tool.write(writer);
    }
    catch (Exception except)
    {
      System.out.println(except);
      except.printStackTrace();
    }
  }
  
  public void addClass(String name)
    throws MappingException
  {
    addClass(name, true);
  }
  
  public void addClass(String name, boolean deep)
    throws MappingException
  {
    if (name == null) {
      throw new MappingException("Cannot introspect a null class.");
    }
    try
    {
      addClass(Class.forName(name), deep);
    }
    catch (ClassNotFoundException except)
    {
      throw new MappingException(except);
    }
  }
  
  public void addClass(Class cls)
    throws MappingException
  {
    addClass(cls, true);
  }
  
  public void addClass(Class cls, boolean deep)
    throws MappingException
  {
    if (cls == null) {
      throw new MappingException("Cannot introspect a null class.");
    }
    if (this._mappings.get(cls) != null) {
      return;
    }
    if (cls.isArray())
    {
      Class cType = cls.getComponentType();
      if (this._mappings.get(cType) != null) {
        return;
      }
      if (Types.isSimpleType(cType)) {
        return;
      }
      addClass(cType);
    }
    if ((this._forceIntrospection) && (!Types.isConstructable(cls))) {
      throw new MappingException("mapping.classNotConstructable", cls.getName());
    }
    boolean introspected = false;
    XMLClassDescriptor xmlClass;
    try
    {
      if (this._forceIntrospection)
      {
        xmlClass = this._internalContext.getIntrospector().generateClassDescriptor( cls );
        introspected = true;
      }
      else
      {
        xmlClass = (XMLClassDescriptor)this._internalContext.getXMLClassDescriptorResolver().resolve(cls);
        this._internalContext.getIntrospector();introspected = Introspector.introspected(xmlClass);
      }
    }
    catch (Exception except)
    {
      throw new MappingException(except);
    }
    ClassMapping classMap = new ClassMapping();
    classMap.setName(cls.getName());
    classMap.setDescription("Default mapping for class " + cls.getName());
    
    classMap.setAccess(null);
    
    MapTo mapTo = new MapTo();
    mapTo.setXml(xmlClass.getXMLName());
    mapTo.setNsUri(xmlClass.getNameSpaceURI());
    mapTo.setNsPrefix(xmlClass.getNameSpacePrefix());
    classMap.setMapTo(mapTo);
    
    this._mappings.put(cls, classMap);
    
    FieldDescriptor[] fields = xmlClass.getFields();
    for (int i = 0; i < fields.length; i++)
    {
      FieldDescriptor fdesc = fields[i];
      
      String fieldName = fdesc.getFieldName();
      
      boolean isContainer = false;
      if ((introspected) && (fieldName.startsWith("##container")))
      {
        fdesc = fdesc.getClassDescriptor().getFields()[0];
        fieldName = fdesc.getFieldName();
        isContainer = true;
      }
      Class fieldType = fdesc.getFieldType();
      if ((!introspected) && (fieldName.startsWith("_")))
      {
        if (!this._mappingLoader.canFindAccessors(cls, fieldName, fieldType)) {
          fieldName = fieldName.substring(1);
        }
        if ((!this._mappingLoader.canFindAccessors(cls, fieldName, fieldType)) && 
          (fieldName.endsWith("List")))
        {
          int len = fieldName.length() - 4;
          String tmpName = fieldName.substring(0, len);
          if (this._mappingLoader.canFindAccessors(cls, tmpName, fieldType)) {
            fieldName = tmpName;
          }
        }
      }
      FieldMapping fieldMap = new FieldMapping();
      fieldMap.setName(fieldName);
      
      boolean isArray = fieldType.isArray();
      while (fieldType.isArray()) {
        fieldType = fieldType.getComponentType();
      }
      if (fdesc.isRequired()) {
        fieldMap.setRequired(true);
      }
      if (fdesc.isTransient()) {
        fieldMap.setTransient(true);
      }
      if (fdesc.isMultivalued())
      {
        if (isContainer) {
          fieldMap.setContainer(false);
        }
        if (isArray)
        {
          fieldMap.setCollection(FieldMappingCollectionType.ARRAY);
        }
        else
        {
          String colName = CollectionHandlers.getCollectionName(fieldType);
          if (colName != null)
          {
            fieldMap.setCollection(FieldMappingCollectionType.valueOf(colName));
            fieldType = Object.class;
          }
          else if (this._mappingLoader.returnsArray(cls, fieldName, fieldType))
          {
            fieldMap.setCollection(FieldMappingCollectionType.ARRAY);
          }
          else
          {
            fieldMap.setCollection(FieldMappingCollectionType.ENUMERATE);
          }
        }
      }
      fieldMap.setType(fieldType.getName());
      
      fieldMap.setBindXml(new BindXml());
      fieldMap.getBindXml().setName(((XMLFieldDescriptor)fdesc).getXMLName());
      fieldMap.getBindXml().setNode(BindXmlNodeType.valueOf(((XMLFieldDescriptor)fields[i]).getNodeType().toString()));
      if (classMap.getClassChoice() == null) {
        classMap.setClassChoice(new ClassChoice());
      }
      classMap.getClassChoice().addFieldMapping(fieldMap);
      if ((deep) && 
        (this._mappings.get(fieldType) == null)) {
        if (!Types.isSimpleType(fieldType)) {
          addClass(fieldType);
        }
      }
    }
  }
  
  public void setForceIntrospection(boolean force)
  {
    this._forceIntrospection = force;
  }
  
  public void write(Writer writer)
    throws MappingException
  {
    try
    {
      MappingRoot mapping = new MappingRoot();
      mapping.setDescription("Exec generated mapping file");
      Enumeration enumeration = this._mappings.elements();
      while (enumeration.hasMoreElements()) {
        mapping.addClassMapping((ClassMapping)enumeration.nextElement());
      }
      Marshaller marshal = new Marshaller(writer);
      marshal.setNamespaceMapping(null, "http://exec.exolab.org/");
      marshal.setNamespaceMapping("cst", "http://exec.exolab.org/");
      marshal.marshal(mapping);
    }
    catch (Exception except)
    {
      throw new MappingException(except);
    }
  }
  
  public void setInternalContext(InternalContext internalContext)
  {
    this._internalContext = internalContext;
    this._mappings = new Hashtable();
    this._mappingLoader = new MappingToolMappingLoader(this._internalContext.getJavaNaming());
  }
}
