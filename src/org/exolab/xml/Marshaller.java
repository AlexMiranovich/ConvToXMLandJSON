package org.exolab.xml;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exec.core.util.Base64Encoder;
import org.exec.core.util.HexDecoder;
import org.exec.core.util.Messages;
import org.exec.mapping.BindingType;
import org.exec.mapping.MappingUnmarshaller;
import org.exec.xml.InternalContext;
import org.exolab.mapping.CollectionHandler;
import org.exolab.mapping.FieldHandler;
import org.exolab.mapping.MapHandler;
import org.exolab.mapping.MapItem;
import org.exolab.mapping.Mapping;
import org.exolab.mapping.MappingException;
import org.exolab.mapping.MappingLoader;
import org.exolab.mapping.handlers.MapHandlers;
import org.exolab.mapping.loader.CollectionHandlers;
import org.exolab.types.AnyNode;
import org.exolab.util.SafeStack;
import org.exolab.xml.descriptors.RootArrayDescriptor;
import org.exolab.xml.descriptors.StringClassDescriptor;
import org.exolab.xml.handlers.DateFieldHandler;
import org.exolab.xml.handlers.EnumFieldHandler;
import org.exolab.xml.util.AnyNode2SAX2;
import org.exolab.xml.util.DocumentHandlerAdapter;
import org.exolab.xml.util.SAX2DOMHandler;
import org.exolab.xml.util.XMLClassDescriptorAdapter;
import org.exolab.xml.util.XMLClassDescriptorImpl;
import org.exolab.xml.util.XMLFieldDescriptorImpl;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class Marshaller extends MarshalFramework {
  private static final Log LOG = LogFactory.getLog(Marshaller.class);
  private static final String CDATA = "CDATA";
  private static final String DEFAULT_PREFIX = "ns";
  private static final String SERIALIZER_NOT_SAX_CAPABLE = "conf.serializerNotSaxCapable";
  private static final String XSI_PREFIX = "xsi";
  private static final String XSI_TYPE = "xsi:type";
  private int _namespaceCounter = 0;
  private static final StringClassDescriptor STRING_CLASS_DESCRIPTOR = new StringClassDescriptor();
  private boolean _asDocument = true;
  private int _depth = 0;
  private OutputFormat _format = null;
  private ContentHandler _handler = null;
  private boolean _marshalExtendedType = true;
  private MarshalListener _marshalListener = null;
  private Namespaces _namespaces = new Namespaces();
  private List _packages = new ArrayList();
  private Stack _parents = new SafeStack();
  private List _processingInstructions = new ArrayList();
  private String _rootElement = null;
  private boolean _saveMapKeys = true;
  private Serializer _serializer = null;
  private boolean _suppressNamespaces = false;
  private boolean _suppressXSIType = false;
  private boolean _useXSITypeAtRoot = false;
  private org.exolab.xml.util.AttributeSetImpl _topLevelAtts = new org.exolab.xml.util.AttributeSetImpl();
  private AttributesImpl _attributes = new AttributesImpl();
  private boolean _validate = false;
  private final Set _proxyInterfaces = new HashSet();

  public Marshaller(DocumentHandler handler) {
    super((InternalContext)null);
    if (handler == null) {
      throw new IllegalArgumentException("The given 'org.sax.DocumentHandler' instance is null.");
    } else {
      this.setContentHandler(new DocumentHandlerAdapter(handler));
    }
  }

  public void setDocumentHandler(DocumentHandler handler) {
    if (handler == null) {
      throw new IllegalArgumentException("The given 'org.sax.DocumentHandler' instance is null.");
    } else {
      this.setContentHandler(new DocumentHandlerAdapter(handler));
    }
  }

  public Marshaller(ContentHandler contentHandler) {
    super((InternalContext)null);
    if (contentHandler == null) {
      throw new IllegalArgumentException("The given 'org.sax.ContentHandler' is null.");
    } else {
      this.setContentHandler(contentHandler);
    }
  }

  public Marshaller(InternalContext internalContext) {
    super(internalContext);
  }

  public Marshaller() {
    super((InternalContext)null);
  }

  public Marshaller(Writer out) throws IOException {
    super((InternalContext)null);
    this.setWriter(out);
  }

  public void setWriter(Writer out) throws IOException {
    if (out == null) {
      throw new IllegalArgumentException("The given 'java.io.Writer instance' is null.");
    } else {
      this.configureSerializer(out);
    }
  }

  private void configureSerializer(Writer out) throws IOException {
    this._serializer = this.getInternalContext().getSerializer();
    if (this._serializer == null) {
      throw new RuntimeException("Unable to obtain serializer");
    } else {
      this._serializer.setOutputCharStream(out);
      this._handler = new DocumentHandlerAdapter(this._serializer.asDocumentHandler());
      if (this._handler == null) {
        String err = Messages.format("conf.serializerNotSaxCapable", this._serializer.getClass().getName());
        throw new RuntimeException(err);
      }
    }
  }

  public Marshaller(Node node) {
    super((InternalContext)null);
    if (node == null) {
      throw new IllegalArgumentException("The given org.w3c.dom.Node instance is null.");
    } else {
      this.setContentHandler(new DocumentHandlerAdapter(new SAX2DOMHandler(node)));
    }
  }

  public void setNode(Node node) {
    if (node == null) {
      throw new IllegalArgumentException("The given org.w3c.dom.Node instance is null.");
    } else {
      this.setContentHandler(new DocumentHandlerAdapter(new SAX2DOMHandler(node)));
    }
  }

  public void setInternalContext(InternalContext internalContext) {
    super.setInternalContext(internalContext);
    this.deriveProperties();
  }

  private void deriveProperties() {
    this._validate = this.getInternalContext().marshallingValidation();
    this._saveMapKeys = this.getInternalContext().getBooleanProperty("org.exolab.xml.saveMapKeys");
    String prop = this.getInternalContext().getStringProperty("org.exolab.xml.proxyInterfaces");
    if (prop != null) {
      StringTokenizer tokenizer = new StringTokenizer(prop, ", ");

      while(tokenizer.hasMoreTokens()) {
        this._proxyInterfaces.add(tokenizer.nextToken());
      }
    }

  }

  public void addProcessingInstruction(String target, String data) {
    String err;
    if (target != null && target.length() != 0) {
      if (data == null) {
        err = "the argument 'data' must not be null.";
        throw new IllegalArgumentException(err);
      } else {
        this._processingInstructions.add(new ProcessingInstruction(target, data));
      }
    } else {
      err = "the argument 'target' must not be null or empty.";
      throw new IllegalArgumentException(err);
    }
  }

  public void setDoctype(String publicId, String systemId) {
    if (this._serializer == null) {
      String error = "doctype cannot be set if you've passed in your own DocumentHandler";
      throw new IllegalStateException(error);
    } else {
      if (this._format == null) {
        this._format = this.getInternalContext().getOutputFormat();
      }

      this._format.setDoctype(publicId, systemId);
      this._serializer.setOutputFormat(this._format);

      try {
        this._handler = new DocumentHandlerAdapter(this._serializer.asDocumentHandler());
      } catch (IOException var4) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Error setting up document handler", var4);
        }
      }

    }
  }

  public void setSupressXMLDeclaration(boolean supressXMLDeclaration) {
    this.setMarshalAsDocument(!supressXMLDeclaration);
  }

  public void setMarshalAsDocument(boolean asDocument) {
    this._asDocument = asDocument;
    if (this._serializer != null) {
      if (this._format == null) {
        this._format = this.getInternalContext().getOutputFormat();
      }

      this._format.setOmitXMLDeclaration(!asDocument);
      this._format.setOmitDocumentType(!asDocument);
      this._serializer.setOutputFormat(this._format);

      try {
        this._handler = new DocumentHandlerAdapter(this._serializer.asDocumentHandler());
      } catch (IOException var3) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Error setting up document handler", var3);
        }
      }
    }

  }

  public void setMapping(Mapping mapping) throws MappingException {
    if (this.getInternalContext() != null && this.getInternalContext().getXMLClassDescriptorResolver() != null) {
      MappingUnmarshaller mum = new MappingUnmarshaller();
      MappingLoader resolver = mum.getMappingLoader(mapping, BindingType.XML);
      this.getInternalContext().getXMLClassDescriptorResolver().setMappingLoader(resolver);
    } else {
      String message = "No internal context or no class descriptor in context.";
      LOG.warn(message);
      throw new IllegalStateException(message);
    }
  }

  public void setMarshalListener(MarshalListener listener) {
    this._marshalListener = listener;
  }

  public void setNamespaceMapping(String nsPrefix, String nsURI) {
    if (nsURI != null && nsURI.length() != 0) {
      this._namespaces.addNamespace(nsPrefix, nsURI);
    } else {
      String err = "namespace URI must not be null.";
      throw new IllegalArgumentException(err);
    }
  }

  public void setRootElement(String rootElement) {
    this._rootElement = rootElement;
  }

  public String getRootElement() {
    return this._rootElement;
  }

  public void setNSPrefixAtRoot(boolean nsPrefixAtRoot) {
  }

  public boolean getNSPrefixAtRoot() {
    return true;
  }

  public XMLClassDescriptorResolver getResolver() {
    if (this.getInternalContext() != null && this.getInternalContext().getXMLClassDescriptorResolver() != null) {
      return this.getInternalContext().getXMLClassDescriptorResolver();
    } else {
      String message = "No internal context or no class descriptor in context.";
      LOG.warn(message);
      throw new IllegalStateException(message);
    }
  }

  public void setResolver(XMLClassDescriptorResolver cdr) {
    if (cdr != null) {
      this.getInternalContext().setXMLClassDescriptorResolver(cdr);
    }

  }

  public void setValidation(boolean validate) {
    this._validate = validate;
  }

  public boolean getValidation() {
    return this._validate;
  }

  public void setMarshalExtendedType(boolean marshalExtendedType) {
    this._marshalExtendedType = marshalExtendedType;
  }

  public boolean getMarshalExtendedType() {
    return this._marshalExtendedType;
  }

  public static void marshalXML(Object object, Writer out) throws MarshalException, ValidationException {
    try {
      staticMarshal(object, new Marshaller(out));
    } catch (IOException var3) {
      throw new MarshalException(var3);
    }
  }

  public static void marshalJSON(Object object) {
    Gson gson = new Gson();

    try {
      FileWriter fileWriter = new FileWriter(object.toString());
      String string = gson.toJson(object);
      fileWriter.write(string);
      fileWriter.close();
    } catch (IOException var4) {
      System.out.println(var4.getMessage());
    }

  }

  public static void unmarshalJSON(String name, Object object) {
    Gson gson = new Gson();

    try {
      BufferedReader bufferedReader = new BufferedReader(new FileReader(name));
      gson.fromJson(bufferedReader, object.getClass());
      bufferedReader.close();
    } catch (FileNotFoundException var4) {
      System.out.println(var4.getMessage());
    } catch (IOException var5) {
      System.out.println(var5.getMessage());
    }

  }

  public static void marshalXML(Object object, DocumentHandler handler) throws MarshalException, ValidationException {
    staticMarshal(object, new Marshaller(handler));
  }

  public static void marshalXML(Object object, ContentHandler handler) throws MarshalException, ValidationException, IOException {
    staticMarshal(object, new Marshaller(handler));
  }

  public static void marshalXML(Object object, Node node) throws MarshalException, ValidationException {
    staticMarshal(object, new Marshaller(node));
  }

  private static void staticMarshal(Object object, Marshaller marshaller) throws MarshalException, ValidationException {
    if (object == null) {
      throw new MarshalException("object must not be null");
    } else {
      if (LOG.isInfoEnabled()) {
        LOG.info("Marshaller called using one of the *static*  marshal(Object, *) methods. This will ignore any  mapping files as specified. Please consider switching to  using Marshaller instances and calling one of the marshal(*) methods.");
      }

      marshaller.marshalXML(object);
    }
  }

  public void marshalXML(Object object) throws MarshalException, ValidationException {
    if (object == null) {
      throw new MarshalException("object must not be null");
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Marshalling " + object.getClass().getName());
      }

      if (object instanceof AnyNode) {
        try {
          AnyNode2SAX2.fireEvents((AnyNode)object, this._handler, this._namespaces);
        } catch (SAXException var5) {
          throw new MarshalException(var5);
        }
      } else {
        this.validate(object);
        MarshalState mstate = new MarshalState(object, "root");
        if (this._asDocument) {
          try {
            this._handler.startDocument();

            for(int i = 0; i < this._processingInstructions.size(); ++i) {
              ProcessingInstruction pi = (ProcessingInstruction)this._processingInstructions.get(i);
              this._handler.processingInstruction(pi.getTarget(), pi.getData());
            }

            this.marshalXML(object, (XMLFieldDescriptor)null, this._handler, mstate);
            this._handler.endDocument();
          } catch (SAXException var6) {
            throw new MarshalException(var6);
          }
        } else {
          this.marshalXML(object, (XMLFieldDescriptor)null, this._handler, mstate);
        }
      }

    }
  }

  private void marshalXML(Object object, XMLFieldDescriptor descriptor, ContentHandler handler, MarshalState mstate) throws MarshalException, ValidationException {
    if (object == null) {
      String err = "Marshaller#marshal: null parameter: 'object'";
      throw new MarshalException(err);
    } else {
      if ((descriptor == null || !((XMLFieldDescriptor)descriptor).isTransient()) && (this._marshalListener == null || this._marshalListener.preMarshal(object))) {
        if (object instanceof AnyNode) {
          try {
            AnyNode2SAX2.fireEvents((AnyNode)object, handler, this._namespaces);
          } catch (SAXException var53) {
            throw new MarshalException(var53);
          }
        } else {
          boolean containerField = false;
          if (descriptor != null && ((XMLFieldDescriptor)descriptor).isContainer()) {
            containerField = true;
          }

          if (this._parents.search(object) < 0) {
            this._parents.push(object);
            boolean isNil = object instanceof NilObject;
            Class cls = null;
            boolean byteArray;
            if (isNil) {
              cls = ((NilObject)object).getClassDescriptor().getJavaClass();
            } else {
              cls = object.getClass();
              if (this._proxyInterfaces.size() > 0) {
                byteArray = false;
                Class[] interfaces = cls.getInterfaces();

                for(int i = 0; i < interfaces.length; ++i) {
                  if (this._proxyInterfaces.contains(interfaces[i].getName())) {
                    byteArray = true;
                  }
                }

                if (byteArray) {
                  cls = cls.getSuperclass();
                }
              }
            }

            byteArray = false;
            if (cls.isArray()) {
              byteArray = cls.getComponentType() == Byte.TYPE;
            }

            boolean atRoot = false;
            if (descriptor == null) {
              descriptor = new XMLFieldDescriptorImpl(cls, "root", (String)null, (NodeType)null);
              atRoot = true;
            }

            String name = ((XMLFieldDescriptor)descriptor).getXMLName();
            if (atRoot && this._rootElement != null) {
              name = this._rootElement;
            }

            boolean autoNameByClass = false;
            if (name == null) {
              autoNameByClass = true;
              name = cls.getName();
              int idx = name.lastIndexOf(46);
              if (idx >= 0) {
                name = name.substring(idx + 1);
              }

              name = this.getInternalContext().getXMLNaming().toXMLName(name);
            }

            XMLClassDescriptor classDesc = null;
            boolean saveType = false;
            if (object instanceof NilObject) {
              classDesc = ((NilObject)object).getClassDescriptor();
            } else if (cls == ((XMLFieldDescriptor)descriptor).getFieldType()) {
              classDesc = (XMLClassDescriptor)((XMLFieldDescriptor)descriptor).getClassDescriptor();
            }

            String defaultNamespace;
            String localName;
            XMLClassDescriptor tmpDesc;
            String nsPrefix;
            if (classDesc == null) {
              if (!isPrimitive(cls) && !byteArray) {
                saveType = cls.isArray();
                nsPrefix = cls.getName();
                int idx = nsPrefix.lastIndexOf(".");
                defaultNamespace = null;
                if (idx > 0) {
                  defaultNamespace = nsPrefix.substring(0, idx + 1);
                  if (!this._packages.contains(defaultNamespace)) {
                    this._packages.add(defaultNamespace);
                  }
                }

                if (!this._marshalExtendedType) {
                  cls = ((XMLFieldDescriptor)descriptor).getFieldType();
                  classDesc = this.getClassDescriptor(cls);
                } else {
                  if (cls != ((XMLFieldDescriptor)descriptor).getFieldType() || atRoot) {
                    saveType = true;
                    boolean containsDesc = false;
                    if (!atRoot) {
                      localName = ((XMLFieldDescriptor)descriptor).getNameSpaceURI();
                      tmpDesc = null;

                      try {
                        tmpDesc = this.getResolver().resolveByXMLName(name, localName, (ClassLoader)null);
                      } catch (ResolverException var63) {
                        if (LOG.isDebugEnabled()) {
                          LOG.debug("Error resolving", var63);
                        }
                      }

                      if (tmpDesc != null) {
                        Class tmpType = tmpDesc.getJavaClass();
                        if (tmpType == cls) {
                          containsDesc = !tmpType.isInterface();
                        }
                      }
                    }

                    if (!containsDesc) {
                      if (atRoot) {
                        if (this._useXSITypeAtRoot) {
                          XMLMappingLoader ml = (XMLMappingLoader)this.getResolver().getMappingLoader();
                          if (ml != null) {
                            containsDesc = ml.getDescriptor(cls.getName()) != null;
                          }
                        } else {
                          containsDesc = true;
                        }
                      }

                      if (!containsDesc && defaultNamespace == null) {
                        classDesc = this.getClassDescriptor(cls);
                        if (classDesc != null) {
                          localName = ((XMLClassDescriptor)classDesc).getXMLName();
                          if (name.equals(localName)) {
                            saveType = false;
                          }
                        }
                      }
                    }

                    if (containsDesc) {
                      saveType = false;
                    }
                  }

                  if (classDesc == null) {
                    classDesc = this.getClassDescriptor(cls);
                  }
                }

                if (classDesc == null && cls.isArray()) {
                  classDesc = new RootArrayDescriptor(cls);
                  if (atRoot) {
                    containerField = !this._asDocument;
                  }
                }
              } else {
                classDesc = STRING_CLASS_DESCRIPTOR;
                Class fieldType = ((XMLFieldDescriptor)descriptor).getFieldType();
                if (cls != fieldType) {
                  while(fieldType.isArray()) {
                    fieldType = fieldType.getComponentType();
                  }

                  saveType = !primitiveOrWrapperEquals(cls, fieldType);
                }
              }

              if (classDesc == null) {
                if (cls != Void.class && cls != Object.class && cls != Class.class) {
                  this._parents.pop();
                  return;
                }

                throw new MarshalException("The marshaller cannot marshal/unmarshal types of Void.class, Class.class or Object.class");
              }
            }

            if (autoNameByClass && ((XMLClassDescriptor)classDesc).getXMLName() != null) {
              name = ((XMLClassDescriptor)classDesc).getXMLName();
            }

            if (atRoot) {
              mstate._xmlName = name;
            }

            saveType = saveType && !this._suppressXSIType;
            if (saveType) {
              if (((XMLFieldDescriptor)descriptor).getHandler() instanceof DateFieldHandler) {
                saveType = false;
              } else if (((XMLFieldDescriptor)descriptor).getHandler() instanceof EnumFieldHandler) {
                saveType = false;
              } else if (isNil) {
                saveType = false;
              }
            }

            String nsURI;
            if (saveType) {
              nsPrefix = name;
              nsURI = ((XMLFieldDescriptor)descriptor).getNameSpaceURI();
              XMLClassDescriptor xmlElementNameClassDesc = null;

              try {
                xmlElementNameClassDesc = this.getResolver().resolveByXMLName(nsPrefix, (String)null, (ClassLoader)null);
              } catch (ResolverException var62) {
                if (LOG.isDebugEnabled()) {
                  LOG.debug("Error resolving " + name, var62);
                }
              }

              if (name != null && xmlElementNameClassDesc != null) {
                try {
                  for(Iterator classDescriptorIter = this.getResolver().resolveAllByXMLName(nsPrefix, (String)null, (ClassLoader)null); classDescriptorIter.hasNext(); xmlElementNameClassDesc = null) {
                    xmlElementNameClassDesc = (XMLClassDescriptor)classDescriptorIter.next();
                    if (cls == xmlElementNameClassDesc.getJavaClass()) {
                      break;
                    }
                  }
                } catch (ResolverException var61) {
                  if (LOG.isDebugEnabled()) {
                    LOG.debug("Error resolving " + name, var61);
                  }

                  xmlElementNameClassDesc = null;
                }

                if (xmlElementNameClassDesc instanceof XMLClassDescriptorAdapter) {
                  XMLClassDescriptor tempContaining = (XMLClassDescriptor)((XMLFieldDescriptor)descriptor).getContainingClassDescriptor();
                  if (tempContaining != null) {
                    XMLFieldDescriptor fieldDescMatch = tempContaining.getFieldDescriptor(name, nsURI, NodeType.Element);
                    InheritanceMatch[] matches = this.searchInheritance(name, (String)null, tempContaining);
                    if (matches.length == 1) {
                      boolean foundTheRightClass = xmlElementNameClassDesc != null && cls == xmlElementNameClassDesc.getJavaClass();
                      boolean oneAndOnlyOneMatchedField = fieldDescMatch != null || matches[0].parentFieldDesc == descriptor;
                      if (foundTheRightClass && oneAndOnlyOneMatchedField) {
                        saveType = false;
                      }
                    }
                  }
                }
              }
            }

            if (!atRoot) {
              this._namespaces = this._namespaces.createNamespaces();
            }

            nsPrefix = "";
            nsURI = "";
            if (!this._suppressNamespaces) {
              nsPrefix = ((XMLFieldDescriptor)descriptor).getNameSpacePrefix();
              if (nsPrefix == null) {
                nsPrefix = ((XMLClassDescriptor)classDesc).getNameSpacePrefix();
              }

              nsURI = ((XMLFieldDescriptor)descriptor).getNameSpaceURI();
              if (nsURI == null) {
                nsURI = ((XMLClassDescriptor)classDesc).getNameSpaceURI();
              }

              if (nsURI == null && nsPrefix != null) {
                nsURI = this._namespaces.getNamespaceURI(nsPrefix);
              } else if (nsPrefix == null && nsURI != null) {
                nsPrefix = this._namespaces.getNamespacePrefix(nsURI);
              }

              if (nsURI == null) {
                nsURI = "";
                defaultNamespace = this._namespaces.getNamespaceURI("");
                if (defaultNamespace != null && !"".equals(defaultNamespace)) {
                  this._namespaces.addNamespace("", "");
                }
              } else {
                defaultNamespace = this._namespaces.getNamespaceURI("");
                if (nsPrefix == null && !nsURI.equals(defaultNamespace)) {
                  if (defaultNamespace == null && atRoot) {
                    nsPrefix = "";
                  } else {
                    nsPrefix = "ns" + ++this._namespaceCounter;
                  }
                }

                this.declareNamespace(nsPrefix, nsURI);
              }
            }

            AttributesImpl atts = new AttributesImpl();
            int nestedAttCount;
            String attValue;
            String typeName;
            if (atRoot) {
              if (this._topLevelAtts.getSize() > 0) {
                this._namespaces.addNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
              }

              for(nestedAttCount = 0; nestedAttCount < this._topLevelAtts.getSize(); ++nestedAttCount) {
                localName = this._topLevelAtts.getName(nestedAttCount);
                String qName = localName;
                attValue = "";
                if (!this._suppressNamespaces) {
                  attValue = this._topLevelAtts.getNamespace(nestedAttCount);
                  typeName = null;
                  if (attValue != null && attValue.length() > 0) {
                    typeName = this._namespaces.getNonDefaultNamespacePrefix(attValue);
                  }

                  if (typeName != null && typeName.length() > 0) {
                    qName = typeName + ':' + localName;
                  }

                  if (attValue == null) {
                    attValue = "";
                  }
                }

                atts.addAttribute(attValue, localName, qName, "CDATA", this._topLevelAtts.getValue(nestedAttCount));
              }
            }

            nestedAttCount = 0;
            XMLFieldDescriptor[] nestedAtts = null;
            tmpDesc = null;
            XMLFieldDescriptor[] descriptors;
            if (!((XMLFieldDescriptor)descriptor).isReference() && !isNil) {
              descriptors = ((XMLClassDescriptor)classDesc).getAttributeDescriptors();
            } else {
              descriptors = NO_FIELD_DESCRIPTORS;
            }

            String qName;
            int i;
            XMLFieldDescriptor attributeDescriptor;
            for(i = 0; i < descriptors.length; ++i) {
              attributeDescriptor = descriptors[i];
              if (attributeDescriptor != null) {
                qName = attributeDescriptor.getLocationPath();
                if (qName != null && qName.length() > 0) {
                  if (nestedAtts == null) {
                    nestedAtts = new XMLFieldDescriptor[descriptors.length - i];
                  }

                  nestedAtts[nestedAttCount] = attributeDescriptor;
                  ++nestedAttCount;
                } else {
                  this.processAttribute(object, attributeDescriptor, atts);
                }
              }
            }

            if (mstate._nestedAttCount > 0) {
              for(i = 0; i < mstate._nestedAtts.length; ++i) {
                attributeDescriptor = mstate._nestedAtts[i];
                if (attributeDescriptor != null) {
                  qName = attributeDescriptor.getLocationPath();
                  if (name.equals(qName)) {
                    mstate._nestedAtts[i] = null;
                    mstate._nestedAttCount--;
                    this.processAttribute(mstate.getOwner(), attributeDescriptor, atts);
                  }
                }
              }
            }

            if (!isNil) {
              this.processContainerAttributes(object, (XMLClassDescriptor)classDesc, atts);
            }

            attValue = ((XMLFieldDescriptor)descriptor).getXMLProperty("xml:space");
            if (attValue != null) {
              atts.addAttribute("http://www.w3.org/XML/1998/namespace", "space", "xml:space", "CDATA", attValue);
            }

            attValue = ((XMLFieldDescriptor)descriptor).getXMLProperty("xml:lang");
            if (attValue != null) {
              atts.addAttribute("http://www.w3.org/XML/1998/namespace", "lang", "xml:lang", "CDATA", attValue);
            }

            String err;
            String obj;
            if (saveType) {
              this.declareNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
              typeName = ((XMLClassDescriptor)classDesc).getXMLName();
              boolean introspected = false;
              if (classDesc instanceof InternalXMLClassDescriptor) {
                introspected = ((InternalXMLClassDescriptor)classDesc).introspected();
              } else {
                introspected = Introspector.introspected((XMLClassDescriptor)classDesc);
              }

              boolean useJavaPrefix = false;
              if (typeName != null && !introspected) {
                if (classDesc instanceof RootArrayDescriptor) {
                  typeName = "java:" + cls.getName();
                  useJavaPrefix = true;
                } else {
                  String dcn = classDesc.getClass().getName();
                  if (dcn.equals(XMLClassDescriptorImpl.class.getName())) {
                    typeName = "java:" + cls.getName();
                    useJavaPrefix = true;
                  } else {
                    err = ((XMLClassDescriptor)classDesc).getNameSpaceURI();
                    obj = null;
                    if (err != null && err.length() > 0) {
                      obj = this._namespaces.getNamespacePrefix(err);
                      if (obj != null && obj.length() > 0) {
                        typeName = obj + ':' + typeName;
                      }
                    }
                  }
                }
              } else {
                typeName = "java:" + cls.getName();
                useJavaPrefix = true;
              }

              atts.addAttribute("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi:type", "CDATA", typeName);
              if (useJavaPrefix) {
                this.declareNamespace("java", "http://java.sun.com");
              }
            }

            if (isNil && !this._suppressXSIType) {
              this.declareNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
              atts.addAttribute("http://www.w3.org/2001/XMLSchema-instance", "nil", "xsi:nil", "CDATA", "true");
            }

            typeName = ((XMLFieldDescriptor)descriptor).getSchemaType();
            if (typeName != null && typeName.equals("QName")) {
              object = this.resolveQName(object, (XMLFieldDescriptor)descriptor);
            }

            qName = null;
            if (nsPrefix != null) {
              int len = nsPrefix.length();
              if (len > 0) {
                StringBuffer sb = new StringBuffer(len + name.length() + 1);
                sb.append(nsPrefix);
                sb.append(':');
                sb.append(name);
                qName = sb.toString();
              } else {
                qName = name;
              }
            } else {
              qName = name;
            }

            Object firstNonNullValue = null;
            int firstNonNullIdx = 0;

            int na;

            boolean popStack;

            try {

              if (!containerField) {
                if (!isNil && ((XMLFieldDescriptor)descriptor).isNillable()) {
                  XMLFieldDescriptor desc = ((XMLClassDescriptor)classDesc).getContentDescriptor();
                  descriptors = ((XMLClassDescriptor)classDesc).getElementDescriptors();
                  i = descriptors.length;
                  popStack = i > 0 || desc != null;
                  if (desc != null) {
                    obj = (String) desc.getHandler().getValue(object);
                    if (obj != null) {
                      popStack = false;
                      i = 0;
                    } else if (desc.isNillable() && desc.isRequired()) {
                      popStack = false;
                      i = 0;
                    }
                  }

                  for(na = 0; na < i; ++na) {
                    Object elemName;
                    desc = descriptors[na];
                    if (desc != null) {
                      elemName = desc.getHandler().getValue(object);
                      if (elemName != null) {
                        popStack = false;
                        firstNonNullIdx = na;
                        firstNonNullValue = elemName;
                        break;
                      }

                      if (desc.isNillable() && desc.isRequired()) {
                        popStack = false;
                        firstNonNullIdx = na;
                        firstNonNullValue = new NilObject((XMLClassDescriptor)classDesc, desc);
                        break;
                      }
                    }
                  }

                  if (popStack) {
                    this.declareNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
                    atts.addAttribute("http://www.w3.org/2001/XMLSchema-instance", "nil", "xsi:nil", "CDATA", "true");
                  }
                }

                this._namespaces.sendStartEvents(handler);
                if (qName == null) {
                  err = "Error in deriving name for type: " + cls.getName() + ", please report bug to: http://exec.exolab.org.";
                  throw new IllegalStateException(err);
                }

                handler.startElement(nsURI, name, qName, atts);
              }
            } catch (SAXException var60) {
              throw new MarshalException(var60);
            }

            Stack wrappers = null;
            String currentLoc = null;
            String tmpPath;
            XMLFieldDescriptor elemDescriptor;
            String elemName;
            if (!isNil) {
              elemDescriptor = null;
              if (!((XMLFieldDescriptor)descriptor).isReference()) {
                elemDescriptor = ((XMLClassDescriptor)classDesc).getContentDescriptor();
              }

              String path;
              char[] chars;
              if (elemDescriptor != null) {
                obj = null;
                obj = null;
                try {
                  obj = (String) elemDescriptor.getHandler().getValue(object);
                } catch (IllegalStateException var52) {
                  LOG.warn("Error getting value from: " + object, var52);
                }

                if (obj != null) {
                  path = elemDescriptor.getLocationPath();
                  currentLoc = null;
                  if (path != null) {
                    this._attributes.clear();
                    if (wrappers == null) {
                      wrappers = new SafeStack();
                    }

                    try {
                      for(; path != null; handler.startElement(nsURI, elemName, elemName, this._attributes)) {
                        elemName = null;
                        int idx = path.indexOf(47);
                        if (idx > 0) {
                          elemName = path.substring(0, idx);
                          path = path.substring(idx + 1);
                        } else {
                          elemName = path;
                          path = null;
                        }

                        if (currentLoc == null) {
                          currentLoc = elemName;
                        } else {
                          currentLoc = currentLoc + "/" + elemName;
                        }
                        elemName = elemName;
                        if (nsPrefix != null && nsPrefix.length() > 0) {
                          elemName = nsPrefix + ':' + elemName;
                        }
                        wrappers.push(new WrapperInfo(elemName, elemName, currentLoc));
                        this._attributes.clear();
                        if (nestedAttCount > 0) {
                          for(na = 0; na < nestedAtts.length; ++na) {
                            if (nestedAtts[na] != null) {
                              tmpPath = nestedAtts[na].getLocationPath();
                              if (tmpPath.equals(currentLoc)) {
                                this.processAttribute(object, nestedAtts[na], this._attributes);
                                nestedAtts[na] = null;
                                --nestedAttCount;
                              }
                            }
                          }
                        }
                      }
                    } catch (SAXException var59) {
                      throw new MarshalException(var59);
                    }
                  }

                  chars = null;
                  Class objType = obj.getClass();
                  if (objType.isArray() && objType.getComponentType() == Byte.TYPE) {
                    elemName = ((XMLFieldDescriptor)descriptor).getSchemaType();
                    if ("hexBinary".equals(elemName)) {
                      chars = (new String(HexDecoder.encode((byte[])((byte[])((byte[])obj))))).toCharArray();
                    } else {
                      chars = Base64Encoder.encode((byte[])((byte[])((byte[]obj)));
                    }
                  } else {
                    elemName = obj.toString();
                    if (elemName != null && elemName.length() > 0) {
                      chars = elemName.toCharArray();
                    }
                  }

                  if (chars != null && chars.length > 0) {
                    try {
                      handler.characters(chars, 0, chars.length);
                    } catch (SAXException var51) {
                      throw new MarshalException(var51);
                    }
                  }
                }
              } else if (((XMLFieldDescriptor)descriptor).isReference()) {
                obj = (String) this.getObjectID(object);
                if (obj != null) {
                  chars = obj.toString().toCharArray();

                  try {
                    handler.characters(chars, 0, chars.length);
                  } catch (SAXException var50) {
                    throw new MarshalException(var50);
                  }
                }
              } else if (!byteArray) {
                if (isPrimitive(cls)) {
                  if (cls == BigDecimal.class) {
                    chars = this.convertBigDecimalToString(object).toCharArray();
                  } else {
                    chars = object.toString().toCharArray();
                  }

                  try {
                    handler.characters(chars, 0, chars.length);
                  } catch (SAXException var48) {
                    throw new MarshalException(var48);
                  }
                } else if (isEnum(cls)) {
                  chars = object.toString().toCharArray();

                  try {
                    handler.characters(chars, 0, chars.length);
                  } catch (SAXException var47) {
                    throw new MarshalException(var47);
                  }
                }
              } else {
                String schemaType = ((XMLFieldDescriptor)descriptor).getSchemaType();
                path = ((XMLFieldDescriptor)descriptor).getComponentType();
                chars = new char[0];
                if ((!((XMLFieldDescriptor)descriptor).isMultivalued() || !"hexBinary".equals(path)) && !"hexBinary".equals(schemaType)) {
                  chars = Base64Encoder.encode((byte[])((byte[])((byte[])object)));
                } else {
                  chars = (new String(HexDecoder.encode((byte[])((byte[])((byte[])object))))).toCharArray();
                }

                try {
                  handler.characters(chars, 0, chars.length);
                } catch (SAXException var49) {
                  throw new MarshalException(var49);
                }
              }
            }

            if (!isNil && !((XMLFieldDescriptor)descriptor).isReference()) {
              descriptors = ((XMLClassDescriptor)classDesc).getElementDescriptors();
            } else {
              descriptors = NO_FIELD_DESCRIPTORS;
            }

            ++this._depth;

            for(i = firstNonNullIdx; i < descriptors.length; ++i) {
              elemDescriptor = descriptors[i];
              obj = null;
              boolean nil = false;
              if (i == firstNonNullIdx && firstNonNullValue != null) {
                obj = (String) firstNonNullValue;
              } else {
                try {
                  obj = (String) elemDescriptor.getHandler().getValue(object);
                } catch (IllegalStateException var56) {
                  LOG.warn("Error marshalling " + object, var56);
                  continue;
                }
              }

              if (obj == null || obj instanceof Enumeration && !((Enumeration) obj).hasMoreElements()) {
                if (!elemDescriptor.isNillable() || !elemDescriptor.isRequired()) {
                  continue;
                }

                nil = true;
              }

              elemName = elemDescriptor.getLocationPath();
              if (wrappers != null) {
                try {
                  while(!wrappers.empty()) {
                    WrapperInfo wInfo = (WrapperInfo)wrappers.peek();
                    if (elemName != null) {
                      if (wInfo._location.equals(elemName)) {
                        elemName = null;
                        break;
                      }

                      if (elemName.startsWith(wInfo._location + "/")) {
                        elemName = elemName.substring(wInfo._location.length() + 1);
                        currentLoc = wInfo._location;
                        break;
                      }
                    }

                    handler.endElement(nsURI, wInfo._localName, wInfo._qName);
                    wrappers.pop();
                  }
                } catch (SAXException var58) {
                  throw new MarshalException(var58);
                }
              }

              if (elemName != null) {
                this._attributes.clear();
                if (wrappers == null) {
                  wrappers = new SafeStack();
                }

                try {
                  for(; elemName != null; handler.startElement(nsURI, elemName, tmpPath, this._attributes)) {
                    elemName = null;
                    na = ((String)elemName).indexOf(47);
                    if (na > 0) {
                      elemName = ((String)elemName).substring(0, na);
                      elemName = elemName.substring(na + 1);
                    } else {
                      elemName = null;
                    }

                    if (currentLoc == null) {
                      currentLoc = elemName;
                    } else {
                      currentLoc = currentLoc + "/" + elemName;
                    }

                    tmpPath = elemName;
                    if (nsPrefix != null && nsPrefix.length() > 0) {
                      tmpPath = nsPrefix + ':' + elemName;
                    }

                    wrappers.push(new WrapperInfo(elemName, tmpPath, currentLoc));
                    this._attributes.clear();
                    if (nestedAttCount > 0) {
                      for(na = 0; na < nestedAtts.length; ++na) {
                        if (nestedAtts[na] != null) {
                          tmpPath = nestedAtts[na].getLocationPath();
                          if (tmpPath.equals(currentLoc)) {
                            this.processAttribute(object, nestedAtts[na], this._attributes);
                            nestedAtts[na] = null;
                            --nestedAttCount;
                          }
                        }
                      }
                    }
                  }
                } catch (SAXException var57) {
                  throw new MarshalException(var57);
                }
              }

              if (nil) {
                obj = String.valueOf( new NilObject((XMLClassDescriptor)classDesc, elemDescriptor) );
              }

              Class type = obj.getClass();
              MarshalState myState = mstate.createMarshalState(object, name);
              myState._nestedAtts = nestedAtts;
              myState._nestedAttCount = nestedAttCount;
              if (type.isArray() && type.getComponentType() == Byte.TYPE) {
                this.marshalXML(obj, elemDescriptor, handler, myState);
              } else if (type.isArray() && elemDescriptor.isDerivedFromXSList()) {
                Object buffer = this.processXSListType(obj, elemDescriptor);
                elemName = elemDescriptor.getXMLName();
                tmpPath = elemName;
                if (nsPrefix != null && nsPrefix.length() > 0) {
                  tmpPath = nsPrefix + ':' + elemName;
                }

                char[] chars = buffer.toString().toCharArray();

                try {
                  handler.startElement(nsURI, elemName, tmpPath, this._attributes);
                  handler.characters(chars, 0, chars.length);
                  handler.endElement(nsURI, elemName, tmpPath);
                } catch (SAXException var46) {
                  throw new MarshalException(var46);
                }
              } else if (isCollection(type)) {
                boolean processCollection = true;
                if (this._saveMapKeys) {
                  MapHandler mapHandler = MapHandlers.getHandler(type);
                  if (mapHandler != null) {
                    processCollection = false;
                    MapItem item = new MapItem();
                    Enumeration keys = mapHandler.keys(obj);

                    while(keys.hasMoreElements()) {
                      item.setKey(keys.nextElement());
                      item.setValue(mapHandler.get(obj, item.getKey()));
                      this.marshalXML(item, elemDescriptor, handler, myState);
                    }
                  }
                }

                if (processCollection) {
                  CollectionHandler colHandler = this.getCollectionHandler(type);
                  Enumeration enumeration = colHandler.elements(obj);

                  while(enumeration.hasMoreElements()) {
                    Object item = enumeration.nextElement();
                    if (item != null) {
                      this.marshalXML(item, elemDescriptor, handler, myState);
                    }
                  }
                }
              } else {
                this.marshalXML(obj, elemDescriptor, handler, myState);
              }

              if (nestedAttCount > 0) {
                nestedAttCount = myState._nestedAttCount;
              }
            }

            if (wrappers != null) {
              try {
                while(!wrappers.empty()) {
                  WrapperInfo wInfo = (WrapperInfo)wrappers.peek();
                  popStack = true;
                  if (nestedAttCount > 0) {
                    for(na = 0; na < nestedAtts.length; ++na) {
                      if (nestedAtts[na] != null) {
                        currentLoc = nestedAtts[na].getLocationPath();
                        if (currentLoc.startsWith(wInfo._location + "/")) {
                          popStack = false;
                          break;
                        }
                      }
                    }
                  }

                  if (!popStack) {
                    break;
                  }

                  handler.endElement(nsURI, wInfo._localName, wInfo._qName);
                  wrappers.pop();
                }
              } catch (SAXException var55) {
                throw new MarshalException(var55);
              }
            }

            if (wrappers != null && !wrappers.isEmpty()) {
              this.dealWithNestedAttributesNested(object, handler, nsPrefix, nsURI, nestedAttCount, nestedAtts, wrappers);
            }

            this.dealWithNestedAttributes(object, handler, nsPrefix, nsURI, nestedAttCount, nestedAtts, new SafeStack());

            try {
              if (!containerField) {
                handler.endElement(nsURI, name, qName);
                this._namespaces.sendEndEvents(handler);
              }
            } catch (SAXException var54) {
              throw new MarshalException(var54);
            }

            --this._depth;
            this._parents.pop();
            if (!atRoot) {
              this._namespaces = this._namespaces.getParent();
            }

            if (this._marshalListener != null) {
              this._marshalListener.postMarshal(object);
            }
          }
        }
      }

    }
  }

  private void dealWithNestedAttributes(Object object, ContentHandler handler, String nsPrefix, String nsURI, int nestedAttCount, XMLFieldDescriptor[] nestedAtts, Stack wrappers) throws MarshalException {
    if (nestedAttCount > 0) {
      for(int i = 0; i < nestedAtts.length; ++i) {
        if (nestedAtts[i] != null) {
          String path = nestedAtts[i].getLocationPath();
          String currentLoc = null;
          if (nestedAtts[i].getHandler().getValue(object) == null) {
            nestedAtts[i] = null;
            --nestedAttCount;
          } else {
            try {
              String elemName;
              String elemQName;
              for(; path != null; handler.startElement(nsURI, elemName, elemQName, this._attributes)) {
                int idx = path.indexOf(47);
                elemName = null;
                if (idx > 0) {
                  elemName = path.substring(0, idx);
                  path = path.substring(idx + 1);
                } else {
                  elemName = path;
                  path = null;
                }

                if (currentLoc == null) {
                  currentLoc = elemName;
                } else {
                  currentLoc = currentLoc + "/" + elemName;
                }

                elemQName = elemName;
                if (nsPrefix != null && nsPrefix.length() > 0) {
                  elemQName = nsPrefix + ':' + elemName;
                }

                wrappers.push(new WrapperInfo(elemName, elemQName, (String)null));
                this._attributes.clear();
                if (path == null) {
                  this.processAttribute(object, nestedAtts[i], this._attributes);
                  nestedAtts[i] = null;
                  --nestedAttCount;
                }

                if (nestedAttCount > 0) {
                  for(int na = i + 1; na < nestedAtts.length; ++na) {
                    if (nestedAtts[na] != null) {
                      String tmpPath = nestedAtts[na].getLocationPath();
                      if (tmpPath.equals(currentLoc)) {
                        this.processAttribute(object, nestedAtts[na], this._attributes);
                        nestedAtts[na] = null;
                        --nestedAttCount;
                      }
                    }
                  }
                }
              }

              while(!wrappers.empty()) {
                WrapperInfo wInfo = (WrapperInfo)wrappers.pop();
                handler.endElement(nsURI, wInfo._localName, wInfo._qName);
              }
            } catch (Exception var16) {
              throw new MarshalException(var16);
            }
          }
        }
      }
    }

  }

  private void dealWithNestedAttributesNested(Object object, ContentHandler handler, String nsPrefix, String nsURI, int nestedAttCount, XMLFieldDescriptor[] nestedAtts, Stack wrappers) throws MarshalException {
    WrapperInfo wrapperInfo = (WrapperInfo)wrappers.peek();
    String currentLocation = wrapperInfo._location;
    if (nestedAttCount > 0) {
      for(int i = 0; i < nestedAtts.length; ++i) {
        if (nestedAtts[i] != null) {
          String nestedAttributePath = nestedAtts[i].getLocationPath();
          if (nestedAttributePath.startsWith(currentLocation + "/")) {
            nestedAttributePath = nestedAttributePath.substring(wrapperInfo._location.length() + 1);
            String currentLoc = currentLocation;
            if (nestedAtts[i].getHandler().getValue(object) == null) {
              nestedAtts[i] = null;
              --nestedAttCount;
            } else {
              try {
                String elemName;
                String elemQName;
                for(; nestedAttributePath != null; handler.startElement(nsURI, elemName, elemQName, this._attributes)) {
                  int idx = nestedAttributePath.indexOf(47);
                  elemName = null;
                  if (idx > 0) {
                    elemName = nestedAttributePath.substring(0, idx);
                    nestedAttributePath = nestedAttributePath.substring(idx + 1);
                  } else {
                    elemName = nestedAttributePath;
                    nestedAttributePath = null;
                  }

                  if (currentLoc == null) {
                    currentLoc = elemName;
                  } else {
                    currentLoc = currentLoc + "/" + elemName;
                  }

                  elemQName = elemName;
                  if (nsPrefix != null && nsPrefix.length() > 0) {
                    elemQName = nsPrefix + ':' + elemName;
                  }

                  wrappers.push(new WrapperInfo(elemName, elemQName, (String)null));
                  this._attributes.clear();
                  if (nestedAttributePath == null) {
                    this.processAttribute(object, nestedAtts[i], this._attributes);
                    nestedAtts[i] = null;
                    --nestedAttCount;
                  }

                  if (nestedAttCount > 0) {
                    for(int na = i + 1; na < nestedAtts.length; ++na) {
                      if (nestedAtts[na] != null) {
                        String tmpPath = nestedAtts[na].getLocationPath();
                        if (tmpPath.equals(currentLoc)) {
                          this.processAttribute(object, nestedAtts[na], this._attributes);
                          nestedAtts[na] = null;
                          --nestedAttCount;
                        }
                      }
                    }
                  }
                }

                while(!wrappers.empty()) {
                  WrapperInfo wInfo = (WrapperInfo)wrappers.pop();
                  handler.endElement(nsURI, wInfo._localName, wInfo._qName);
                }
              } catch (Exception var18) {
                throw new MarshalException(var18);
              }
            }
          }
        }
      }
    }

  }

  private String convertBigDecimalToString(Object object) throws MarshalException {
    float javaVersion = Float.parseFloat(System.getProperty("java.specification.version"));
    String stringValue;
    if ((double)javaVersion >= 1.5D) {
      try {
        Method method = BigDecimal.class.getMethod("toPlainString", (Class[])null);
        stringValue = (String)method.invoke(object, (Object[])null);
      } catch (Exception var5) {
        LOG.error("Problem accessing java.math.BigDecimal.toPlainString().", var5);
        throw new MarshalException("Problem accessing java.math.BigDecimal.toPlainString().", var5);
      }
    } else {
      stringValue = object.toString();
    }

    return stringValue;
  }

  private Object getObjectID(Object object) throws MarshalException {
    if (object == null) {
      return null;
    } else {
      Object id = null;
      XMLClassDescriptor cd = this.getClassDescriptor(object.getClass());
      String err = null;
      if (cd != null) {
        XMLFieldDescriptor fieldDesc = (XMLFieldDescriptor)cd.getIdentity();
        if (fieldDesc != null) {
          FieldHandler fieldHandler = fieldDesc.getHandler();
          if (fieldHandler != null) {
            try {
              id = fieldHandler.getValue(object);
            } catch (IllegalStateException var8) {
              err = var8.toString();
            }
          } else {
            err = "FieldHandler for Identity descriptor is null.";
          }
        } else {
          err = "No identity descriptor available";
        }
      } else {
        err = "Unable to resolve ClassDescriptor.";
      }

      if (err != null) {
        String errMsg = "Unable to resolve ID for instance of class '";
        errMsg = errMsg + object.getClass().getName();
        errMsg = errMsg + "' due to the following error: ";
        throw new MarshalException(errMsg + err);
      } else {
        return id;
      }
    }
  }

  private boolean declareNamespace(String nsPrefix, String nsURI) {
    boolean declared = false;
    if (nsURI != null && nsURI.length() != 0) {
      String tmpURI = this._namespaces.getNamespaceURI(nsPrefix);
      if (tmpURI != null && tmpURI.equals(nsURI)) {
        return declared;
      }

      String tmpPrefix = this._namespaces.getNamespacePrefix(nsURI);
      if (tmpPrefix == null || !tmpPrefix.equals(nsPrefix)) {
        this._namespaces.addNamespace(nsPrefix, nsURI);
        declared = true;
      }
    }

    return declared;
  }

  public void setLogWriter(PrintWriter printWriter) {
  }

  public void setEncoding(String encoding) {
    if (this._serializer == null) {
      String error = "encoding cannot be set if you've passed in your own DocumentHandler";
      throw new IllegalStateException(error);
    } else {
      if (this._format == null) {
        this._format = this.getInternalContext().getOutputFormat();
      }

      this._format.setEncoding(encoding);
      this._serializer.setOutputFormat(this._format);

      try {
        this._handler = new DocumentHandlerAdapter(this._serializer.asDocumentHandler());
      } catch (IOException var3) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Error setting encoding to " + encoding, var3);
        }
      }

    }
  }

  public void setNoNamespaceSchemaLocation(String schemaLocation) {
    if (schemaLocation != null) {
      this._topLevelAtts.setAttribute("noNamespaceSchemaLocation", schemaLocation, "http://www.w3.org/2001/XMLSchema-instance");
    }

  }

  public void setSchemaLocation(String schemaLocation) {
    if (schemaLocation != null) {
      this._topLevelAtts.setAttribute("schemaLocation", schemaLocation, "http://www.w3.org/2001/XMLSchema-instance");
    }

  }

  public void setSuppressNamespaces(boolean suppressNamespaces) {
    this._suppressNamespaces = suppressNamespaces;
  }

  public void setSuppressXSIType(boolean suppressXSIType) {
    this._suppressXSIType = suppressXSIType;
  }

  public void setUseXSITypeAtRoot(boolean useXSITypeAtRoot) {
    this._useXSITypeAtRoot = useXSITypeAtRoot;
  }

  private XMLClassDescriptor getClassDescriptor(Class cls) throws MarshalException {
    Object classDesc = null;

    try {
      if (!isPrimitive(cls)) {
        classDesc = (XMLClassDescriptor)this.getResolver().resolve(cls);
      }
    } catch (ResolverException var5) {
      Throwable actual = var5.getCause();
      if (actual instanceof MarshalException) {
        throw (MarshalException)actual;
      }

      if (actual != null) {
        throw new MarshalException(actual);
      }

      throw new MarshalException(var5);
    }

    if (classDesc != null) {
      classDesc = new InternalXMLClassDescriptor(this, (XMLClassDescriptor)classDesc);
    }

    return (XMLClassDescriptor)classDesc;
  }

  private void processAttribute(Object object, XMLFieldDescriptor attDescriptor, AttributesImpl atts) throws MarshalException {
    if (attDescriptor != null) {
      Object value;
      Object val;
      if (attDescriptor.getNodeType() == NodeType.Namespace) {
        if (!this._suppressNamespaces) {
          Object map = attDescriptor.getHandler().getValue(object);
          MapHandler mapHandler = MapHandlers.getHandler(map);
          if (mapHandler != null) {
            Enumeration keys = mapHandler.keys(map);

            while(keys.hasMoreElements()) {
              value = keys.nextElement();
              val = mapHandler.get(map, value);
              this.declareNamespace(value.toString(), val.toString());
            }
          }
        }
      } else {
        String localName = attDescriptor.getXMLName();
        String qName = localName;
        String namespace = "";
        if (!this._suppressNamespaces) {
          namespace = attDescriptor.getNameSpaceURI();
          if (namespace != null && namespace.length() > 0) {
            String prefix = attDescriptor.getNameSpacePrefix();
            if (prefix == null || prefix.length() == 0) {
              prefix = this._namespaces.getNonDefaultNamespacePrefix(namespace);
            }

            if (prefix == null || prefix.length() == 0) {
              prefix = "ns" + ++this._namespaceCounter;
            }

            this.declareNamespace(prefix, namespace);
            qName = prefix + ':' + localName;
          } else {
            namespace = "";
          }
        }

        val = null;

        try {
          value = attDescriptor.getHandler().getValue(object);
        } catch (IllegalStateException var14) {
          LOG.warn("Error getting value from " + object, var14);
          return;
        }

        Class objType;
        if (attDescriptor.isReference() && value != null) {
          if (!attDescriptor.isMultivalued()) {
            value = this.getObjectID(value);
          } else {
            objType = null;
            Enumeration enumeration;
            if (value instanceof Enumeration) {
              enumeration = (Enumeration)value;
            } else {
              CollectionHandler colHandler = null;

              try {
                colHandler = CollectionHandlers.getHandler(value.getClass());
              } catch (MappingException var13) {
                throw new MarshalException(var13);
              }

              enumeration = colHandler.elements(value);
            }

            if (!enumeration.hasMoreElements()) {
              value = null;
            } else {
              StringBuffer sb = new StringBuffer();

              for(int v = 0; enumeration.hasMoreElements(); ++v) {
                if (v > 0) {
                  sb.append(' ');
                }

                sb.append(this.getObjectID(enumeration.nextElement()).toString());
              }

              value = sb;
            }
          }
        } else if (attDescriptor.isMultivalued() && value != null) {
          value = this.processXSListType(value, attDescriptor);
        } else if (value != null) {
          objType = value.getClass();
          if (objType.isArray() && objType.getComponentType() == Byte.TYPE) {
            value = this.encodeBinaryData(value, attDescriptor.getSchemaType());
          }
        }

        if (value != null) {
          String valueType = attDescriptor.getSchemaType();
          if (valueType != null && valueType.equals("QName")) {
            value = this.resolveQName(value, attDescriptor);
          }

          atts.addAttribute(namespace, localName, qName, "CDATA", value.toString());
        }
      }
    }

  }

  private Object processXSListType(Object value, XMLFieldDescriptor descriptor) throws MarshalException {
    Object returnValue = null;
    Enumeration enumeration = null;
    if (value instanceof Enumeration) {
      enumeration = (Enumeration)value;
    } else {
      CollectionHandler colHandler = null;

      try {
        colHandler = CollectionHandlers.getHandler(value.getClass());
      } catch (MappingException var9) {
        throw new MarshalException(var9);
      }

      enumeration = colHandler.elements(value);
    }

    if (enumeration.hasMoreElements()) {
      StringBuffer sb = new StringBuffer();

      for(int v = 0; enumeration.hasMoreElements(); ++v) {
        if (v > 0) {
          sb.append(' ');
        }

        Object collectionValue = enumeration.nextElement();
        Class objType = collectionValue.getClass();
        if (objType.isArray() && objType.getComponentType() == Byte.TYPE) {
          collectionValue = this.encodeBinaryData(collectionValue, descriptor.getComponentType());
        }

        sb.append(collectionValue.toString());
      }

      returnValue = sb;
    }

    return returnValue;
  }

  private Object encodeBinaryData(Object valueToEncode, String componentType) {
    String encodedValue;
    if ("hexBinary".equals(componentType)) {
      encodedValue = HexDecoder.encode((byte[])((byte[])((byte[])valueToEncode)));
    } else {
      encodedValue = new String(Base64Encoder.encode((byte[])((byte[])((byte[])valueToEncode))));
    }

    return encodedValue;
  }

  private void processContainerAttributes(Object target, XMLClassDescriptor classDesc, AttributesImpl atts) throws MarshalException {
    if (!(classDesc instanceof XMLClassDescriptorImpl) || ((XMLClassDescriptorImpl)classDesc).hasContainerFields()) {
      XMLFieldDescriptor[] elemDescriptors = classDesc.getElementDescriptors();

      for(int i = 0; i < elemDescriptors.length; ++i) {
        if (elemDescriptors[i] != null && elemDescriptors[i].isContainer()) {
          this.processContainerAttributes(target, elemDescriptors[i], atts);
        }
      }
    }

  }

  private void processContainerAttributes(Object target, XMLFieldDescriptor containerFieldDesc, AttributesImpl atts) throws MarshalException {
    if (target.getClass().isArray()) {
      int length = Array.getLength(target);

      for(int j = 0; j < length; ++j) {
        Object item = Array.get(target, j);
        if (item != null) {
          this.processContainerAttributes(item, containerFieldDesc, atts);
        }
      }
    } else if (target instanceof Enumeration) {
      Enumeration enumeration = (Enumeration)target;

      while(enumeration.hasMoreElements()) {
        Object item = enumeration.nextElement();
        if (item != null) {
          this.processContainerAttributes(item, containerFieldDesc, atts);
        }
      }
    } else {
      Object containerObject = containerFieldDesc.getHandler().getValue(target);
      if (containerObject != null) {
        XMLClassDescriptor containerClassDesc = (XMLClassDescriptor)containerFieldDesc.getClassDescriptor();
        if (containerClassDesc == null) {
          containerClassDesc = this.getClassDescriptor(containerFieldDesc.getFieldType());
          if (containerClassDesc == null) {
            return;
          }
        }

        XMLFieldDescriptor[] attrDescriptors = containerClassDesc.getAttributeDescriptors();

        for(int idx = 0; idx < attrDescriptors.length; ++idx) {
          if (attrDescriptors[idx] != null && (attrDescriptors[idx].getLocationPath() == null || attrDescriptors[idx].getLocationPath().length() == 0)) {
            this.processAttribute(containerObject, attrDescriptors[idx], atts);
          }
        }

        this.processContainerAttributes(containerObject, containerClassDesc, atts);
      }
    }

  }

  private Object resolveQName(Object value, XMLFieldDescriptor fieldDesc) {
    if (value != null && value instanceof String) {
      if (!(fieldDesc instanceof XMLFieldDescriptorImpl)) {
        return value;
      } else {
        String result = (String)value;
        String nsURI = null;
        if (result.length() > 0 && result.charAt(0) == '{') {
          int idx = result.indexOf(125);
          String prefix;
          if (idx <= 0) {
            prefix = "Bad QName value :'" + result + "', it should follow the pattern '{URI}value'";
            throw new IllegalArgumentException(prefix);
          } else {
            nsURI = result.substring(1, idx);
            prefix = ((XMLFieldDescriptorImpl)fieldDesc).getQNamePrefix();
            if (prefix == null) {
              prefix = this._namespaces.getNamespacePrefix(nsURI);
            }

            if (prefix == null) {
              prefix = "ns" + ++this._namespaceCounter;
            }

            result = prefix.length() != 0 ? prefix + ":" + result.substring(idx + 1) : result.substring(idx + 1);
            this.declareNamespace(prefix, nsURI);
            return result;
          }
        } else {
          return value;
        }
      }
    } else {
      return value;
    }
  }

  private void validate(Object object) throws ValidationException {
    if (this._validate) {
      Validator validator = new Validator();
      ValidationContext context = new ValidationContext();
      context.setInternalContext(this.getInternalContext());
      validator.validate(object, context);
    }

  }

  public String getProperty(String name) {
    return this.getInternalContext().getStringProperty(name);
  }

  public void setProperty(String name, String value) {
    this.getInternalContext().setProperty(name, value);
    this.deriveProperties();
  }

  public void setContentHandler(ContentHandler contentHandler) {
    this._handler = contentHandler;
  }

  static class WrapperInfo {
    private String _localName = null;
    private String _qName = null;
    private String _location = null;

    WrapperInfo(String localName, String qName, String location) {
      this._localName = localName;
      this._qName = qName;
      this._location = location;
    }
  }

  static class MarshalState {
    private String _xpath = null;
    private XMLFieldDescriptor[] _nestedAtts = null;
    private int _nestedAttCount = 0;
    private MarshalState _parent = null;
    private Object _owner = null;
    private String _xmlName = null;

    MarshalState(Object owner, String xmlName) {
      String err;
      if (owner == null) {
        err = "The argument 'owner' must not be null";
        throw new IllegalArgumentException(err);
      } else if (xmlName == null) {
        err = "The argument 'xmlName' must not be null";
        throw new IllegalArgumentException(err);
      } else {
        this._owner = owner;
        this._xmlName = xmlName;
      }
    }

    MarshalState createMarshalState(Object owner, String xmlName) {
      MarshalState ms = new MarshalState(owner, xmlName);
      ms._parent = this;
      return ms;
    }

    String getXPath() {
      if (this._xpath == null) {
        if (this._parent != null) {
          this._xpath = this._parent.getXPath() + "/" + this._xmlName;
        } else {
          this._xpath = this._xmlName;
        }
      }

      return this._xpath;
    }

    Object getOwner() {
      return this._owner;
    }

    MarshalState getParent() {
      return this._parent;
    }
  }

  public static class NilObject {
    private XMLClassDescriptor _classDesc = null;
    private XMLFieldDescriptor _fieldDesc = null;

    NilObject(XMLClassDescriptor classDesc, XMLFieldDescriptor fieldDesc) {
      this._classDesc = classDesc;
      this._fieldDesc = fieldDesc;
    }

    public XMLClassDescriptor getClassDescriptor() {
      return this._classDesc;
    }

    public XMLFieldDescriptor getFieldDescriptor() {
      return this._fieldDesc;
    }
  }
}
