package org.exolab.xml;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exec.core.util.Base64Decoder;
import org.exec.core.util.HexDecoder;
import org.exec.xml.InternalContext;
import org.exec.xml.UnmarshalListenerAdapter;
import org.exolab.mapping.ClassDescriptor;
import org.exolab.mapping.ExtendedFieldHandler;
import org.exolab.mapping.FieldHandler;
import org.exolab.mapping.MapItem;
import org.exolab.mapping.loader.FieldHandlerImpl;
import org.exolab.types.AnyNode;
import org.exolab.util.DefaultObjectFactory;
import org.exolab.util.ObjectFactory;
import org.exolab.xml.descriptors.PrimitivesClassDescriptor;
import org.exolab.xml.descriptors.StringClassDescriptor;
//import org.exolab.xml.util.AttributeSetImpl;
import org.exolab.xml.util.ContainerElement;
import org.exolab.xml.util.SAX2ANY;
import org.exolab.xml.util.XMLClassDescriptorImpl;
import org.exolab.xml.util.XMLFieldDescriptorImpl;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public final class UnmarshalHandler extends MarshalFramework
  implements ContentHandler, DocumentHandler, ErrorHandler {
  private static final Log LOG = LogFactory.getLog(UnmarshalHandler.class);
  private static final String ERROR_DID_NOT_FIND_CLASSDESCRIPTOR = "unable to find or create a ClassDescriptor for class: ";
  private static final String XML_PREFIX = "xml";
  private static final String XMLNS = "xmlns";
  private static final String XMLNS_PREFIX = "xmlns:";
  private static final int XMLNS_PREFIX_LENGTH = "xmlns:".length();
  private static final String XSI_TYPE = "type";
  private static final String XML_SPACE = "space";
  private static final String XML_SPACE_WITH_PREFIX = "xml:space";
  private static final String PRESERVE = "preserve";
  private Stack _stateInfo = null;
  private UnmarshalState _topState = null;
  private Class _topClass = null;
  private Object _topObject = null;
  private boolean _clearCollections = false;
  private Locator _locator = null;
  private IDResolver _idResolver = null;
  private UnmarshalListener _unmarshalListener = null;
  private boolean _validate = true;
  private Hashtable _resolveTable = new Hashtable();
  private Map _javaPackages = null;
  private ClassLoader _loader = null;
  private static final StringClassDescriptor STRING_DESCRIPTOR = new StringClassDescriptor();
  private SAX2ANY _anyUnmarshaller = null;
  private int _depth = 0;
  private AnyNode _node = null;
  private Namespaces _namespaces = null;
  private HashMap _namespaceToPackage = null;
  private ObjectFactory _objectFactory = new DefaultObjectFactory();
  private boolean _reuseObjects = false;
  private boolean _strictAttributes = false;
  private boolean _strictElements = true;
  private int _ignoreElementDepth = 0;
  private boolean _createNamespaceScope = true;
  private ElementInfo _elemInfo = null;
  private AttributeSet _reusableAtts = null;
  private boolean _wsPreserve = false;
  protected UnmarshalHandler()
  {
    this(null);
  }
  protected UnmarshalHandler(Class<?> topClass)
  {
    this(null, topClass);
  }
  protected UnmarshalHandler(InternalContext internalContext, Class<?> topClass) {
    super(internalContext);
    this._stateInfo = new Stack();
    this._idResolver = new IDResolverImpl();
    this._javaPackages = new HashMap();
    this._topClass = topClass;
    this._namespaces = new Namespaces();
    this._namespaceToPackage = new HashMap();
  }
  public void addNamespaceToPackageMapping(String nsURI, String packageName) {
    if (nsURI == null) {
      nsURI = "";
    }
    if (packageName == null) {
      packageName = "";
    }
    this._namespaceToPackage.put(nsURI, packageName);
  }
  public Object getCurrentObject() {
    if (!this._stateInfo.isEmpty()) {
      UnmarshalState state = (UnmarshalState)this._stateInfo.peek();
      if (state != null) {
        return state._object;
      }
    }
    return null;
  }
  public Object getObject() {
    if (this._topState != null) {
      return this._topState._object;
    }
    return null;
  }
  public void setClassLoader(ClassLoader loader)
  {
    this._loader = loader;
  }
  public void setClearCollections(boolean clear)
  {
    this._clearCollections = clear;
  }
  public void setDebug(boolean debug) {}
  public void setIDResolver(IDResolver idResolver)
  {
    ((IDResolverImpl)this._idResolver).setResolver(idResolver);
  }
  public void setIgnoreExtraAttributes(boolean ignoreExtraAtts)
  {
    this._strictAttributes = (!ignoreExtraAtts);
  }
  public void setIgnoreExtraElements(boolean ignoreExtraElems)
  {
    this._strictElements = (!ignoreExtraElems);
  }
  public void setLogWriter(PrintWriter printWriter) {}
  public void setReuseObjects(boolean reuse)
  {
    this._reuseObjects = reuse;
  }
  public void setRootObject(Object root)
  {
    this._topObject = root;
  }
  public void setUnmarshalListener(UnmarshalListener listener) {
    if (listener == null) {
      listener = null;
    }
    else {
      UnmarshalListenerAdapter adapter = new UnmarshalListenerAdapter();
      adapter.setOldListener( (org.exec.xml.UnmarshalListener) listener );
      this._unmarshalListener = (UnmarshalListener) adapter;
    }
  }
  
  public void setUnmarshalListener(UnmarshalListener listener) {
    this._unmarshalListener = listener;
  }
  public void setValidation(boolean validate)
  {
    this._validate = validate;
  }
  public void setWhitespacePreserve(boolean preserve) {
    this._wsPreserve = preserve;
  }
  public void characters(char[] ch, int start, int length)
    throws SAXException {
    if (LOG.isTraceEnabled()) {
      StringBuffer sb = new StringBuffer(21 + length);
      sb.append("#characters: ");
      sb.append(ch, start, length);
      LOG.trace(sb.toString());
    }
    if (this._ignoreElementDepth > 0) {
      return;
    }
    if (this._stateInfo.empty()) {
      return;
    }
    if (this._anyUnmarshaller != null)
    {
      this._anyUnmarshaller.characters(ch, start, length);
    }
    else {
      UnmarshalState state = (UnmarshalState)this._stateInfo.peek();
      boolean removedTrailingWhitespace = false;
      boolean removedLeadingWhitespace = false;
      if (!state._wsPreserve) {
        while (length > 0) {
          boolean whitespace = false;
          switch (ch[start])
          {
          case '\t': 
          case '\n': 
          case '\r': 
          case ' ': 
            whitespace = true;
            break;
          }
          if (!whitespace) {
            break;
          }
          removedLeadingWhitespace = true;
          start++;
          length--;
        }
        if (length == 0) {
          removedTrailingWhitespace = removedLeadingWhitespace;
        } else {
          while (length > 0)
          {
            boolean whitespace = false;
            switch (ch[(start + length - 1)])
            {
            case '\t': 
            case '\n': 
            case '\r': 
            case ' ': 
              whitespace = true;
              break;
            }
            if (!whitespace) {
              break;
            }
            removedTrailingWhitespace = true;
            length--;
          }
        }
      }
      if (state._buffer == null) {
        state._buffer = new StringBuffer();
      } else if ((!state._wsPreserve) && (length > 0) && (
        (state._trailingWhitespaceRemoved) || (removedLeadingWhitespace))) {
        state._buffer.append(' ');
      }
      state._trailingWhitespaceRemoved = removedTrailingWhitespace;
      state._buffer.append(ch, start, length);
    }
  }
  
  public void endDocument()
    throws SAXException
  {}
  
  public void endElement(String name)
    throws SAXException
  {
    if (LOG.isTraceEnabled()) {
      LOG.trace("#endElement: " + name);
    }
    if (this._ignoreElementDepth > 0)
    {
      this._ignoreElementDepth -= 1;
      return;
    }
    if (this._anyUnmarshaller != null)
    {
      this._anyUnmarshaller.endElement(name);
      this._depth -= 1;
      if (this._depth == 0)
      {
        this._node = this._anyUnmarshaller.getStartingNode();
        this._anyUnmarshaller = null;
      }
      else
      {
        return;
      }
    }
    if (this._stateInfo.empty()) {
      throw new SAXException("missing start element: " + name);
    }
    int idx = name.indexOf(':');
    if (idx >= 0) {
      name = name.substring(idx + 1);
    }
    UnmarshalState state = (UnmarshalState)this._stateInfo.pop();
    
    XMLFieldDescriptor descriptor = state._fieldDesc;
    if (!state._elementName.equals(name))
    {
      if (descriptor.isContainer())
      {
        this._stateInfo.push(state);
        
        StringBuffer tmpBuffer = null;
        if ((state._buffer != null) && 
          (!isWhitespace(state._buffer)) && 
          (state._classDesc.getContentDescriptor() == null))
        {
          tmpBuffer = state._buffer;
          state._buffer = null;
        }
        endElement(state._elementName);
        if (tmpBuffer != null)
        {
          state = (UnmarshalState)this._stateInfo.peek();
          if (state._buffer == null) {
            state._buffer = tmpBuffer;
          } else {
            state._buffer.append(tmpBuffer.toString());
          }
        }
        endElement(name);
        return;
      }
      String err = "error in xml, expecting </" + state._elementName;
      err = err + ">, but received </" + name + "> instead.";
      throw new SAXException(err);
    }
    Class type = state._type;
    if (type == null)
    {
      if (!state._wrapper) {
        LOG.info("Ignoring " + state._elementName + " no descriptor was found");
      }
      StringBuffer tmpBuffer = null;
      if ((state._buffer != null) && 
        (!isWhitespace(state._buffer)))
      {
        tmpBuffer = state._buffer;
        state._buffer = null;
      }
      if (tmpBuffer != null)
      {
        UnmarshalState targetState = state;
        String locPath = targetState._elementName;
        while ((targetState = targetState._parent) != null) {
          if ((targetState._wrapper) || (targetState._classDesc == null))
          {
            locPath = targetState._elementName + "/" + locPath;
          }
          else
          {
            XMLFieldDescriptor tmpDesc = targetState._classDesc.getContentDescriptor();
            if ((tmpDesc != null) && (locPath.equals(tmpDesc.getLocationPath()))) {
              if (targetState._buffer == null) {
                targetState._buffer = tmpBuffer;
              } else {
                targetState._buffer.append(tmpBuffer.toString());
              }
            }
          }
        }
      }
      this._namespaces = this._namespaces.getParent();
      return;
    }
    boolean byteArray = false;
    if (type.isArray()) {
      byteArray = type.getComponentType() == Byte.TYPE;
    }
    if ((state._object == null) && (!state._primitiveOrImmutable))
    {
      this._namespaces = this._namespaces.getParent();
      return;
    }
    if (state._primitiveOrImmutable)
    {
      String str = null;
      if (state._buffer != null)
      {
        str = state._buffer.toString();
        state._buffer.setLength(0);
      }
      if ((type == String.class) && (!((XMLFieldDescriptorImpl)descriptor).isDerivedFromXSList()))
      {
        if (str != null) {
          state._object = str;
        } else if (state._nil) {
          state._object = null;
        } else {
          state._object = "";
        }
      }
      else if ((byteArray) && (!descriptor.isDerivedFromXSList()))
      {
        if (str == null) {
          state._object = new byte[0];
        } else {
          state._object = decodeBinaryData(descriptor, str);
        }
      }
      else if (state._args != null)
      {
        state._object = createInstance(state._type, state._args);
      }
      else if ((descriptor.isMultivalued()) && (descriptor.getSchemaType() != null) && (descriptor.getSchemaType().equals("list")) && (((XMLFieldDescriptorImpl)descriptor).isDerivedFromXSList()))
      {
        StringTokenizer attrValueTokenizer = new StringTokenizer(str);
        List primitives = new ArrayList();
        while (attrValueTokenizer.hasMoreTokens())
        {
          String tokenValue = attrValueTokenizer.nextToken();
          if (isPrimitive(descriptor.getFieldType()))
          {
            primitives.add(toPrimitiveObject(type, tokenValue, state._fieldDesc));
          }
          else
          {
            Class valueType = descriptor.getFieldType();
            if ((valueType.isArray()) && (valueType.getComponentType() == Byte.TYPE)) {
              primitives.add(decodeBinaryData(descriptor, tokenValue));
            }
          }
        }
        state._object = primitives;
      }
      else if (state._nil)
      {
        state._object = null;
      }
      else
      {
        state._object = toPrimitiveObject(type, str, state._fieldDesc);
      }
    }
    else if (ArrayHandler.class.isAssignableFrom(state._type))
    {
      state._object = ((ArrayHandler)state._object).getObject();
      state._type = state._object.getClass();
    }
    if ((state._buffer != null) && (state._buffer.length() > 0) && (state._classDesc != null))
    {
      XMLFieldDescriptor cdesc = state._classDesc.getContentDescriptor();
      if (cdesc != null)
      {
        Object value = state._buffer.toString();
        if (isPrimitive(cdesc.getFieldType()))
        {
          value = toPrimitiveObject(cdesc.getFieldType(), (String)value, state._fieldDesc);
        }
        else
        {
          Class valueType = cdesc.getFieldType();
          if ((valueType.isArray()) && (valueType.getComponentType() == Byte.TYPE)) {
            value = decodeBinaryData(descriptor, (String)value);
          }
        }
        try
        {
          FieldHandler handler = cdesc.getHandler();
          boolean addObject = true;
          if (this._reuseObjects)
          {
            Object tmp = handler.getValue(state._object);
            if (tmp != null) {
              addObject = !tmp.equals(value);
            }
          }
          if (addObject) {
            handler.setValue(state._object, value);
          }
        }
        catch (IllegalStateException ise)
        {
          String err = "unable to add text content to ";
          err = err + descriptor.getXMLName();
          err = err + " due to the following error: " + ise;
          throw new SAXException(err, ise);
        }
      }
      else
      {
        if (descriptor.isReference())
        {
          UnmarshalState pState = (UnmarshalState)this._stateInfo.peek();
          processIDREF(state._buffer.toString(), descriptor, pState._object);
          this._namespaces = this._namespaces.getParent();
          return;
        }
        if (!isWhitespace(state._buffer))
        {
          String err = "Illegal Text data found as child of: " + name;
          
          err = err + "\n  value: \"" + state._buffer + "\"";
          throw new SAXException(err);
        }
      }
    }
    if ((this._unmarshalListener != null) && (state._object != null)) {
      this._unmarshalListener.unmarshalled(state._object);
    }
    if (this._stateInfo.empty())
    {
      if (isValidating())
      {
        ValidationException first = null;
        ValidationException last = null;
        if ((this._resolveTable != null) && (!getInternalContext().getLenientIdValidation()))
        {
          Enumeration enumeration = this._resolveTable.keys();
          while (enumeration.hasMoreElements())
          {
            Object ref = enumeration.nextElement();
            
            String msg = "unable to resolve reference: " + ref;
            if (first == null)
            {
              first = new ValidationException(msg);
              last = first;
            }
            else
            {
              last.setNext(new ValidationException(msg));
              last = last.getNext();
            }
          }
        }
        try
        {
          Validator validator = new Validator();
          ValidationContext context = new ValidationContext();
          context.setInternalContext(getInternalContext());
          validator.validate(state._object, context);
          if (!getInternalContext().getLenientIdValidation()) {
            validator.checkUnresolvedIdrefs(context);
          }
          context.cleanup();
        }
        catch (ValidationException vEx)
        {
          if (first == null) {
            first = vEx;
          } else {
            last.setNext(vEx);
          }
        }
        if (first != null) {
          throw new SAXException(first);
        }
      }
      return;
    }
    if (descriptor.isIncremental())
    {
      this._namespaces = this._namespaces.getParent();
      return;
    }
    Object val = state._object;
    if (this._node != null)
    {
      val = this._node;
      this._node = null;
    }
    UnmarshalState fieldState = state;
    
    boolean firstOccurance = false;
    
    state = (UnmarshalState)this._stateInfo.peek();
    if (state._wrapper) {
      state = fieldState._targetState;
    }
    if (!descriptor.isMultivalued())
    {
      if (state.isUsed(descriptor))
      {
        String err = "element \"" + name;
        err = err + "\" occurs more than once. (parent class: " + state._type.getName() + ")";
        
        String location = name;
        while (!this._stateInfo.isEmpty())
        {
          UnmarshalState tmpState = (UnmarshalState)this._stateInfo.pop();
          if ((tmpState._wrapper) || 
            (!tmpState._fieldDesc.isContainer())) {
            location = state._elementName + "/" + location;
          }
        }
        err = err + "\n location: /" + location;
        
        ValidationException vx = new ValidationException(err);
        
        throw new SAXException(vx);
      }
      state.markAsUsed(descriptor);
      if (state._classDesc.getIdentity() == descriptor) {
        state._key = val;
      }
    }
    else
    {
      if (!state.isUsed(descriptor)) {
        firstOccurance = true;
      }
      state.markAsUsed(descriptor);
    }
    try
    {
      FieldHandler handler = descriptor.getHandler();
      
      String valueType = descriptor.getSchemaType();
      if ((valueType != null) && (valueType.equals("QName"))) {
        val = resolveNamespace(val);
      }
      boolean addObject = true;
      if ((this._reuseObjects) && (fieldState._primitiveOrImmutable))
      {
        Object tmp = handler.getValue(state._object);
        if (tmp != null) {
          addObject = !tmp.equals(val);
        }
      }
      if (descriptor.isMapped()) {
        if (!(val instanceof MapItem))
        {
          MapItem mapItem = new MapItem(fieldState._key, val);
          val = mapItem;
        }
        else
        {
          MapItem mapItem = (MapItem)val;
          if (mapItem.getValue() == null)
          {
            addObject = false;
            addReference(mapItem.toString(), state._object, descriptor);
          }
        }
      }
      if (addObject)
      {
        if ((firstOccurance) && (this._clearCollections)) {
          handler.resetValue(state._object);
        }
        Iterator iterator;
        if ((descriptor.isMultivalued()) && (descriptor.getSchemaType() != null) && (descriptor.getSchemaType().equals("list")) && (((XMLFieldDescriptorImpl)descriptor).isDerivedFromXSList()))
        {
          List values = (List)val;
          for (iterator = values.iterator(); iterator.hasNext();)
          {
            Object value = iterator.next();
            handler.setValue(state._object, value);
            if (this._unmarshalListener != null) {
              this._unmarshalListener.fieldAdded(descriptor.getFieldName(), state._object, fieldState._object);
            }
          }
        }
        else
        {
          handler.setValue(state._object, val);
          if (this._unmarshalListener != null) {
            this._unmarshalListener.fieldAdded(descriptor.getFieldName(), state._object, fieldState._object);
          }
        }
      }
    }
    catch (Exception ex)
    {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      ex.printStackTrace(pw);
      pw.flush();
      String err = "unable to add '" + name + "' to <";
      err = err + state._fieldDesc.getXMLName();
      err = err + "> due to the following exception: \n";
      err = err + ">>>--- Begin Exception ---<<< \n";
      err = err + sw.toString();
      err = err + ">>>---- End Exception ----<<< \n";
      throw new SAXException(err, ex);
    }
    this._namespaces = this._namespaces.getParent();
    if ((state._fieldDesc.isContainer()) && (state._classDesc.isChoice()) && (!state._fieldDesc.isMultivalued())) {
      endElement(state._elementName);
    }
  }
  private byte[] decodeBinaryData(XMLFieldDescriptor descriptor, String binaryData) {
    byte[] decodedValue;
    if (((descriptor.isMultivalued()) && ("hexBinary".equals(descriptor.getComponentType()))) || ("hexBinary".equals(descriptor.getSchemaType()))) {
      decodedValue = HexDecoder.decode(binaryData);
    } else {
      decodedValue = Base64Decoder.decode(binaryData);
    }
    return decodedValue;
  }
  public void endElement(String namespaceURI, String localName, String qName)
    throws SAXException {
    if ((qName == null) || (qName.length() == 0)) {
      if ((localName == null) || (localName.length() == 0)) {
        String error = "Missing either 'qName' or 'localName', both cannot be null or emtpy.";
        throw new SAXException(error);
      }
      qName = localName;
      if ((namespaceURI != null) && (namespaceURI.length() > 0))
      {
        String prefix = this._namespaces.getNamespacePrefix(namespaceURI);
        if ((prefix != null) && (prefix.length() > 0)) {
          qName = prefix + ":" + localName;
        }
      }
    }
    endElement(qName);
  }
  
  public void endPrefixMapping(String prefix)
    throws SAXException
  {
    if (this._anyUnmarshaller != null) {
      this._anyUnmarshaller.endPrefixMapping(prefix);
    }
  }
  
  public void ignorableWhitespace(char[] ch, int start, int length)
    throws SAXException
  {
    if (this._ignoreElementDepth > 0) {
      return;
    }
    if (this._stateInfo.empty()) {
      return;
    }
    if (this._anyUnmarshaller != null)
    {
      this._anyUnmarshaller.ignorableWhitespace(ch, start, length);
    }
    else
    {
      UnmarshalState state = (UnmarshalState)this._stateInfo.peek();
      if (state._wsPreserve)
      {
        if (state._buffer == null) {
          state._buffer = new StringBuffer();
        }
        state._buffer.append(ch, start, length);
      }
    }
  }
  
  public void processingInstruction(String target, String data)
    throws SAXException
  {}
  
  public void setDocumentLocator(Locator locator)
  {
    this._locator = locator;
  }
  
  public Locator getDocumentLocator()
  {
    return this._locator;
  }
  
  public void skippedEntity(String name)
    throws SAXException
  {}
  
  public void startDocument()
    throws SAXException
  {}
  
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
    throws SAXException
  {
    if (LOG.isTraceEnabled()) {
      if ((qName != null) && (qName.length() > 0)) {
        LOG.trace("#startElement: " + qName);
      } else {
        LOG.trace("#startElement: " + localName);
      }
    }
    if ((!this._strictElements) && (this._ignoreElementDepth > 0))
    {
      this._ignoreElementDepth += 1;
      return;
    }
    if (this._anyUnmarshaller != null)
    {
      this._depth += 1;
      this._anyUnmarshaller.startElement(namespaceURI, localName, qName, atts);
      return;
    }
    if (this._createNamespaceScope) {
      this._namespaces = this._namespaces.createNamespaces();
    } else {
      this._createNamespaceScope = true;
    }
    if (this._reusableAtts == null)
    {
      if (atts != null) {
        this._reusableAtts = new AttributeSetImpl(atts.getLength());
      } else {
        this._reusableAtts = new AttributeSetImpl();
      }
    }
    else {
      this._reusableAtts.clear();
    }
    boolean hasQNameAtts = false;
    if ((atts != null) && (atts.getLength() > 0)) {
      for (int i = 0; i < atts.getLength(); i++)
      {
        String attName = atts.getQName(i);
        if ((attName != null) && (attName.length() > 0))
        {
          if (attName.equals("xmlns"))
          {
            this._namespaces.addNamespace("", atts.getValue(i));
          }
          else if (attName.startsWith("xmlns:"))
          {
            String prefix = attName.substring("xmlns:".length());
            this._namespaces.addNamespace(prefix, atts.getValue(i));
          }
          else if (attName.indexOf(':') < 0)
          {
            this._reusableAtts.setAttribute(attName, atts.getValue(i), atts.getURI(i));
          }
          else
          {
            hasQNameAtts = true;
          }
        }
        else
        {
          attName = atts.getLocalName(i);
          if ("xmlns".equals(attName)) {
            this._namespaces.addNamespace("", atts.getValue(i));
          } else {
            this._reusableAtts.setAttribute(attName, atts.getValue(i), atts.getURI(i));
          }
        }
      }
    }
    if (hasQNameAtts) {
      for (int i = 0; i < atts.getLength(); i++)
      {
        String attName = atts.getQName(i);
        if ((attName != null) && (attName.length() > 0)) {
          if ((!attName.equals("xmlns")) && (!attName.startsWith("xmlns:")))
          {
            int idx = attName.indexOf(':');
            if (idx >= 0)
            {
              String prefix = attName.substring(0, idx);
              attName = attName.substring(idx + 1);
              String nsURI = atts.getURI(i);
              if ((nsURI == null) || (nsURI.length() == 0)) {
                nsURI = this._namespaces.getNamespaceURI(prefix);
              }
              this._reusableAtts.setAttribute(attName, atts.getValue(i), nsURI);
            }
          }
        }
      }
    }
    if (this._elemInfo == null)
    {
      this._elemInfo = new ElementInfo(null, atts);
    }
    else
    {
      this._elemInfo.clear();
      this._elemInfo._attributes = atts;
    }
    if ((localName == null) || (localName.length() == 0))
    {
      if ((qName == null) || (qName.length() == 0))
      {
        String error = "Missing either 'localName' or 'qName', both cannot be emtpy or null.";
        throw new SAXException(error);
      }
      localName = qName;
      this._elemInfo._qName = qName;
    }
    else if ((qName == null) || (qName.length() == 0))
    {
      if ((namespaceURI == null) || (namespaceURI.length() == 0))
      {
        this._elemInfo._qName = localName;
      }
      else
      {
        String prefix = this._namespaces.getNamespacePrefix(namespaceURI);
        if ((prefix != null) && (prefix.length() > 0)) {
          this._elemInfo._qName = (prefix + ":" + localName);
        }
      }
    }
    else
    {
      this._elemInfo._qName = qName;
    }
    int idx = localName.indexOf(':');
    if (idx >= 0)
    {
      String prefix = localName.substring(0, idx);
      localName = localName.substring(idx + 1);
      if ((namespaceURI == null) || (namespaceURI.length() == 0)) {
        namespaceURI = this._namespaces.getNamespaceURI(prefix);
      }
    }
    else
    {
      String defaultNamespace = this._namespaces.getNamespaceURI("");
      if ((defaultNamespace != null) && (!defaultNamespace.equals("http://exec.exolab.org"))) {
        namespaceURI = defaultNamespace;
      }
      if ((namespaceURI != null) && (namespaceURI.length() == 0)) {
        namespaceURI = null;
      }
    }
    startElement(localName, namespaceURI, this._reusableAtts);
  }
  
  public void startElement(String name, AttributeList attList)
    throws SAXException
  {
    if (LOG.isTraceEnabled()) {
      LOG.trace("#startElement: " + name);
    }
    if ((!this._strictElements) && (this._ignoreElementDepth > 0))
    {
      this._ignoreElementDepth += 1;
      return;
    }
    if (this._anyUnmarshaller != null)
    {
      this._depth += 1;
      this._anyUnmarshaller.startElement(name, attList);
      return;
    }
    if (this._elemInfo == null)
    {
      this._elemInfo = new ElementInfo(name, attList);
    }
    else
    {
      this._elemInfo.clear();
      this._elemInfo._qName = name;
      this._elemInfo._attributeList = attList;
    }
    String namespace = null;
    
    this._namespaces = this._namespaces.createNamespaces();
    
    AttributeSet atts = processAttributeList(attList);
    
    String prefix = "";
    
    int idx = name.indexOf(':');
    if (idx >= 0)
    {
      prefix = name.substring(0, idx);
      name = name.substring(idx + 1);
    }
    namespace = this._namespaces.getNamespaceURI(prefix);
    
    startElement(name, namespace, atts);
  }
  
  private void startElement(String name, String namespace, AttributeSet atts)
    throws SAXException
  {
    UnmarshalState state = null;
    String xmlSpace = null;
    if (atts != null)
    {
      xmlSpace = atts.getValue("space", "http://www.w3.org/XML/1998/namespace");
      if (xmlSpace == null) {
        xmlSpace = atts.getValue("xml:space", "");
      }
    }
    if (this._stateInfo.empty())
    {
      if ((this._topClass == null) && 
        (this._topObject != null)) {
        this._topClass = this._topObject.getClass();
      }
      if (getInternalContext().getXMLClassDescriptorResolver() == null)
      {
        String message = "XMLClassDescriptorResolver is not set!";
        LOG.warn(message);
        throw new IllegalStateException(message);
      }
      this._topState = new UnmarshalState();
      this._topState._elementName = name;
      this._topState._wsPreserve = (xmlSpace != null ? "preserve".equals(xmlSpace) : this._wsPreserve);
      
      XMLClassDescriptor classDesc = null;
      
      String instanceClassname = null;
      if (this._topClass == null)
      {
        instanceClassname = getInstanceType(atts, null);
        if (instanceClassname != null)
        {
          try
          {
            this._topClass = loadClass(instanceClassname, null);
          }
          catch (ClassNotFoundException cnfe) {}
          if (this._topClass == null)
          {
            classDesc = getClassDescriptor(instanceClassname);
            if (classDesc != null) {
              this._topClass = classDesc.getJavaClass();
            }
            if (this._topClass == null) {
              throw new SAXException("Class not found: " + instanceClassname);
            }
          }
        }
        else
        {
          classDesc = resolveByXMLName(name, namespace, null);
          if (classDesc == null)
          {
            classDesc = getClassDescriptor(name, this._loader);
            if (classDesc == null) {
              classDesc = getClassDescriptor(getJavaNaming().toJavaClassName(name));
            }
          }
          if (classDesc != null) {
            this._topClass = classDesc.getJavaClass();
          }
        }
        if (this._topClass == null)
        {
          String err = "The class for the root element '" + name + "' could not be found.";
          
          throw new SAXException(err);
        }
      }
      XMLFieldDescriptorImpl fieldDesc = new XMLFieldDescriptorImpl(this._topClass, name, name, NodeType.Element);
      
      this._topState._fieldDesc = fieldDesc;
      if (classDesc == null) {
        classDesc = getClassDescriptor(this._topClass);
      }
      if ((classDesc == null) && 
        (isPrimitive(this._topClass)))
      {
        classDesc = new PrimitivesClassDescriptor(this._topClass);
        fieldDesc.setIncremental(false);
        this._topState._primitiveOrImmutable = true;
      }
      fieldDesc.setClassDescriptor(classDesc);
      if (classDesc == null)
      {
        if ((!isPrimitive(this._topClass)) && (!Serializable.class.isAssignableFrom(this._topClass))) {
          throw new SAXException("The marshaller cannot unmarshal non primitive types that do not implement java.io.Serializable");
        }
        String err = "unable to create XMLClassDescriptor for class: " + this._topClass.getName();
        
        throw new SAXException(err);
      }
      this._topState._classDesc = classDesc;
      this._topState._type = this._topClass;
      if ((this._topObject == null) && (!this._topState._primitiveOrImmutable))
      {
        String topPackage = getJavaPackage(this._topClass);
        if (instanceClassname == null) {
          instanceClassname = getInstanceType(atts, topPackage);
        } else {
          instanceClassname = null;
        }
        if (instanceClassname != null)
        {
          Class instanceClass = null;
          try
          {
            XMLClassDescriptor xcd = getClassDescriptor(instanceClassname);
            
            boolean loadClass = true;
            if (xcd != null)
            {
              instanceClass = xcd.getJavaClass();
              if (instanceClass != null) {
                loadClass = !instanceClassname.equals(instanceClass.getName());
              }
            }
            if (loadClass) {
              try
              {
                instanceClass = loadClass(instanceClassname, null);
              }
              catch (ClassNotFoundException cnfe)
              {
                if (xcd != null) {
                  instanceClass = xcd.getJavaClass();
                }
              }
            }
            if (instanceClass == null) {
              throw new SAXException("Class not found: " + instanceClassname);
            }
            if (!this._topClass.isAssignableFrom(instanceClass))
            {
              String err = instanceClass + " is not a subclass of " + this._topClass;
              
              throw new SAXException(err);
            }
          }
          catch (Exception ex)
          {
            String msg = "unable to instantiate " + instanceClassname + "; ";
            
            throw new SAXException(msg + ex, ex);
          }
          Arguments args = processConstructorArgs(atts, classDesc);
          this._topState._object = createInstance(instanceClass, args);
        }
        else
        {
          Arguments args = processConstructorArgs(atts, classDesc);
          this._topState._object = createInstance(this._topClass, args);
        }
      }
      else
      {
        this._topState._object = this._topObject;
      }
      this._stateInfo.push(this._topState);
      if (!this._topState._primitiveOrImmutable)
      {
        if (this._unmarshalListener != null) {
          this._unmarshalListener.initialized(this._topState._object, this._topState._parent == null ? null : this._topState._parent._object);
        }
        processAttributes(atts, classDesc);
        if (this._unmarshalListener != null) {
          this._unmarshalListener.attributesProcessed(this._topState._object, this._topState._parent == null ? null : this._topState._parent._object);
        }
        processNamespaces(classDesc);
      }
      String pkg = getJavaPackage(this._topClass);
      if (getMappedPackage(namespace) == null) {
        addNamespaceToPackageMapping(namespace, pkg);
      }
      return;
    }
    UnmarshalState parentState = (UnmarshalState)this._stateInfo.peek();
    
    boolean canAccept = false;
    while ((parentState._fieldDesc != null) && (parentState._fieldDesc.isContainer()) && (!canAccept))
    {
      XMLClassDescriptor tempClassDesc = parentState._classDesc;
      if (tempClassDesc == null)
      {
        tempClassDesc = (XMLClassDescriptor)parentState._fieldDesc.getClassDescriptor();
        if (tempClassDesc == null) {
          tempClassDesc = getClassDescriptor(parentState._object.getClass());
        }
      }
      canAccept = tempClassDesc.canAccept(name, namespace, parentState._object);
      if (!canAccept)
      {
        if ((tempClassDesc.getFieldDescriptor(name, namespace, NodeType.Element) != null) && 
          (!parentState._fieldDesc.isMultivalued()))
        {
          String error = "The container object (" + tempClassDesc.getJavaClass().getName();
          error = error + ") cannot accept the child object associated with the element '" + name + "'";
          error = error + " because the container is already full!";
          ValidationException vx = new ValidationException(error);
          throw new SAXException(vx);
        }
        endElement(parentState._elementName);
        parentState = (UnmarshalState)this._stateInfo.peek();
      }
      tempClassDesc = null;
    }
    state = new UnmarshalState();
    state._elementName = name;
    state._parent = parentState;
    if (xmlSpace != null) {
      state._wsPreserve = "preserve".equals(xmlSpace);
    } else {
      state._wsPreserve = parentState._wsPreserve;
    }
    this._stateInfo.push(state);
    if ((parentState._object == null) && 
      (!parentState._wrapper)) {
      return;
    }
    Class cls = null;
    
    XMLClassDescriptor classDesc = parentState._classDesc;
    if (classDesc == null)
    {
      classDesc = (XMLClassDescriptor)parentState._fieldDesc.getClassDescriptor();
      if (classDesc == null) {
        classDesc = getClassDescriptor(parentState._object.getClass());
      }
    }
    XMLFieldDescriptor descriptor = null;
    
    XMLClassDescriptor cdInherited = null;
    
    UnmarshalState targetState = parentState;
    String path = "";
    StringBuffer pathBuf = null;
    int count = 0;
    boolean isWrapper = false;
    XMLClassDescriptor oldClassDesc = classDesc;
    while (descriptor == null)
    {
      if (path.length() > 0)
      {
        String tmpName = path + "/" + name;
        descriptor = classDesc.getFieldDescriptor(tmpName, namespace, NodeType.Element);
      }
      if (descriptor == null) {
        descriptor = classDesc.getFieldDescriptor(name, namespace, NodeType.Element);
      }
      if ((descriptor != null) && (!descriptor.isContainer()) && 
        (namespace != null) && (namespace.length() > 0) && 
        (!namespaceEquals(namespace, descriptor.getNameSpaceURI()))) {
        if ((descriptor.getNameSpaceURI() != null) || (!descriptor.matches("*"))) {
          descriptor = null;
        }
      }
      if ((descriptor == null) && (!targetState._wrapper))
      {
        InheritanceMatch[] matches = null;
        try
        {
          matches = searchInheritance(name, namespace, classDesc);
        }
        catch (MarshalException rx) {}
        if (matches.length != 0)
        {
          InheritanceMatch match = null;
          for (int i = 0; i < matches.length; i++) {
            if (parentState._elementName.equals(matches[i].parentFieldDesc.getLocationPath()))
            {
              match = matches[i];
              break;
            }
          }
          if (match == null) {
            match = matches[0];
          }
          descriptor = match.parentFieldDesc;
          cdInherited = match.inheritedClassDesc;
          break;
        }
        String tmpLocation = name;
        if (count > 0) {
          tmpLocation = path + "/" + name;
        }
        isWrapper = (isWrapper) || (hasFieldsAtLocation(tmpLocation, classDesc));
      }
      else if (descriptor != null)
      {
        String tmpPath = descriptor.getLocationPath();
        if (tmpPath == null) {
          tmpPath = "";
        }
        if (path.equals(tmpPath)) {
          break;
        }
        descriptor = null;
      }
      else
      {
        if (pathBuf == null) {
          pathBuf = new StringBuffer();
        } else {
          pathBuf.setLength(0);
        }
        pathBuf.append(path);
        pathBuf.append('/');
        pathBuf.append(name);
        isWrapper = (isWrapper) || (hasFieldsAtLocation(pathBuf.toString(), classDesc));
      }
      if (targetState == this._topState) {
        break;
      }
      if (count == 0)
      {
        path = targetState._elementName;
      }
      else
      {
        if (pathBuf == null) {
          pathBuf = new StringBuffer();
        } else {
          pathBuf.setLength(0);
        }
        pathBuf.append(targetState._elementName);
        pathBuf.append('/');
        pathBuf.append(path);
        path = pathBuf.toString();
      }
      targetState = targetState._parent;
      classDesc = targetState._classDesc;
      count++;
    }
    if ((descriptor != null) && (isValidating()) && (!getInternalContext().getLenientSequenceOrder())) {
      try
      {
        classDesc.checkDescriptorForCorrectOrderWithinSequence(descriptor, parentState, name);
      }
      catch (ValidationException e)
      {
        throw new SAXException(e);
      }
    }
    if (descriptor == null)
    {
      classDesc = oldClassDesc;
      if (isWrapper)
      {
        state._classDesc = new XMLClassDescriptorImpl(ContainerElement.class, name);
        state._wrapper = true;
        if (LOG.isDebugEnabled()) {
          LOG.debug("wrapper-element: " + name);
        }
        processWrapperAttributes(atts);
        return;
      }
      String mesg = "unable to find FieldDescriptor for '" + name;
      mesg = mesg + "' in ClassDescriptor of " + classDesc.getXMLName();
      if ((classDesc instanceof InternalXMLClassDescriptor)) {
        classDesc = ((InternalXMLClassDescriptor)classDesc).getClassDescriptor();
      }
      boolean lenientElementStrictnessForIntrospection = getInternalContext().getBooleanProperty("org.exolab.castor.xml.lenient.introspected.element.strictness").booleanValue();
      if (!this._strictElements)
      {
        this._ignoreElementDepth += 1;
        
        this._stateInfo.pop();
        
        this._namespaces = this._namespaces.getParent();
        if (LOG.isDebugEnabled()) {
          LOG.debug(mesg + " - ignoring extra element.");
        }
        return;
      }
      if ((lenientElementStrictnessForIntrospection) && (Introspector.introspected(classDesc)))
      {
        LOG.warn(mesg);
        return;
      }
      throw new SAXException(mesg);
    }
    if (targetState != parentState)
    {
      state._targetState = targetState;
      parentState = targetState;
    }
    Object object = parentState._object;
    if (descriptor.isContainer())
    {
      if (LOG.isDebugEnabled()) {
        LOG.debug("#container: " + descriptor.getFieldName());
      }
      state.clear();
      
      state._wsPreserve = parentState._wsPreserve;
      state._parent = parentState;
      
      state._elementName = descriptor.getFieldName();
      state._fieldDesc = descriptor;
      state._classDesc = ((XMLClassDescriptor)descriptor.getClassDescriptor());
      Object containerObject = null;
      if (!descriptor.isMultivalued())
      {
        FieldHandler handler = descriptor.getHandler();
        containerObject = handler.getValue(object);
        if (containerObject != null)
        {
          if (state._classDesc != null)
          {
            if (state._classDesc.canAccept(name, namespace, containerObject)) {
              parentState.markAsNotUsed(descriptor);
            }
          }
          else {
            parentState.markAsNotUsed(descriptor);
          }
        }
        else {
          containerObject = handler.newInstance(object);
        }
      }
      else
      {
        Class containerClass = descriptor.getFieldType();
        try
        {
          containerObject = containerClass.newInstance();
        }
        catch (Exception ex)
        {
          throw new SAXException(ex);
        }
      }
      state._object = containerObject;
      state._type = containerObject.getClass();
      
      this._namespaces = this._namespaces.createNamespaces();
      startElement(name, namespace, atts);
      return;
    }
    state._fieldDesc = descriptor;
    
    classDesc = null;
    if (cdInherited != null) {
      classDesc = cdInherited;
    } else if (!name.equals(descriptor.getXMLName())) {
      classDesc = resolveByXMLName(name, namespace, null);
    }
    if (classDesc == null) {
      classDesc = (XMLClassDescriptor)descriptor.getClassDescriptor();
    }
    FieldHandler handler = descriptor.getHandler();
    boolean useHandler = true;
    try
    {
      if (classDesc != null)
      {
        cls = classDesc.getJavaClass();
        if (descriptor.getFieldType() != cls) {
          state._derived = true;
        }
      }
      else
      {
        cls = descriptor.getFieldType();
      }
      if (cls == null) {
        cls = Object.class;
      }
      String currentPackage = getJavaPackage(parentState._type);
      String instanceType = getInstanceType(atts, currentPackage);
      if (instanceType != null)
      {
        Class instanceClass = null;
        try
        {
          XMLClassDescriptor instanceDesc = getClassDescriptor(instanceType, this._loader);
          
          boolean loadClass = true;
          if (instanceDesc != null)
          {
            instanceClass = instanceDesc.getJavaClass();
            classDesc = instanceDesc;
            if (instanceClass != null) {
              loadClass = !instanceClass.getName().equals(instanceType);
            }
          }
          if (loadClass)
          {
            instanceClass = loadClass(instanceType, null);
            
            FieldHandler tempHandler = descriptor.getHandler();
            
            boolean collection = false;
            if ((tempHandler instanceof FieldHandlerImpl)) {
              collection = ((FieldHandlerImpl)tempHandler).isCollection();
            } else {
              collection = Introspector.isCollection(instanceClass);
            }
            if ((!collection) && (!cls.isAssignableFrom(instanceClass))) {
              if (!isPrimitive(cls))
              {
                String err = instanceClass.getName() + " is not a subclass of " + cls.getName();
                
                throw new SAXException(err);
              }
            }
          }
          cls = instanceClass;
          useHandler = false;
        }
        catch (Exception ex)
        {
          String msg = "unable to instantiate " + instanceType;
          throw new SAXException(msg + "; " + ex, ex);
        }
      }
      if ((cls == Object.class) && 
        ((parentState._object instanceof ArrayHandler))) {
        cls = ((ArrayHandler)parentState._object).componentType();
      }
      if (cls == Object.class)
      {
        Class pClass = parentState._type;
        ClassLoader loader = pClass.getClassLoader();
        
        classDesc = resolveByXMLName(name, namespace, loader);
        
        String cname = null;
        if (classDesc == null)
        {
          cname = getJavaNaming().toJavaClassName(name);
          classDesc = getClassDescriptor(cname, loader);
        }
        if (classDesc == null)
        {
          String pkg = pClass.getName();
          int idx = pkg.lastIndexOf('.');
          if (idx > 0)
          {
            pkg = pkg.substring(0, idx + 1);
            cname = pkg + cname;
            classDesc = getClassDescriptor(cname, loader);
          }
        }
        if (classDesc != null)
        {
          cls = classDesc.getJavaClass();
          useHandler = false;
        }
        else
        {
          this._anyUnmarshaller = new SAX2ANY(this._namespaces, state._wsPreserve);
          if (this._elemInfo._attributeList != null) {
            this._anyUnmarshaller.startElement(this._elemInfo._qName, this._elemInfo._attributeList);
          } else {
            this._anyUnmarshaller.startElement(namespace, name, this._elemInfo._qName, this._elemInfo._attributes);
          }
          this._depth = 1;
          state._object = this._anyUnmarshaller.getStartingNode();
          state._type = cls;
          
          return;
        }
      }
      boolean byteArray = false;
      if (cls.isArray()) {
        byteArray = cls.getComponentType() == Byte.TYPE;
      }
      if ((isPrimitive(cls)) || (descriptor.isImmutable()) || (byteArray))
      {
        state._object = null;
        state._primitiveOrImmutable = true;
        if (descriptor.isImmutable())
        {
          if (classDesc == null) {
            classDesc = getClassDescriptor(cls);
          }
          state._classDesc = classDesc;
          Arguments args = processConstructorArgs(atts, classDesc);
          if ((args != null) && (args.size() > 0)) {
            state._args = args;
          }
        }
      }
      else
      {
        if (classDesc == null) {
          classDesc = getClassDescriptor(cls);
        }
        if ((!state._derived) && (useHandler))
        {
          boolean create = true;
          if (this._reuseObjects)
          {
            state._object = handler.getValue(parentState._object);
            create = state._object == null;
          }
          if (create)
          {
            Arguments args = processConstructorArgs(atts, classDesc);
            if ((args._values != null) && (args._values.length > 0))
            {
              if ((handler instanceof ExtendedFieldHandler))
              {
                ExtendedFieldHandler efh = (ExtendedFieldHandler)handler;
                
                state._object = efh.newInstance(parentState._object, args._values);
              }
              else
              {
                String err = "constructor arguments can only be used with an ExtendedFieldHandler.";
                
                throw new SAXException(err);
              }
            }
            else {
              state._object = handler.newInstance(parentState._object);
            }
          }
        }
        if (state._object != null)
        {
          cls = state._object.getClass();
          if ((classDesc != null) && 
            (classDesc.getJavaClass() != cls)) {
            classDesc = null;
          }
        }
        else
        {
          try
          {
            if (cls.isArray())
            {
              state._object = new ArrayHandler(cls.getComponentType());
              cls = ArrayHandler.class;
            }
            else
            {
              Arguments args = processConstructorArgs(atts, classDesc);
              state._object = createInstance(cls, args);
            }
          }
          catch (Exception ex)
          {
            String err = "unable to instantiate a new type of: ";
            err = err + className(cls);
            err = err + "; " + ex.getMessage();
            
            SAXException sx = new SAXException(err, ex);
            
            throw sx;
          }
        }
      }
      state._type = cls;
    }
    catch (IllegalStateException ise)
    {
      LOG.error(ise.toString());
      throw new SAXException(ise);
    }
    if (classDesc == null) {
      classDesc = getClassDescriptor(cls);
    }
    state._classDesc = classDesc;
    if ((state._object == null) && (!state._primitiveOrImmutable))
    {
      String err = "unable to unmarshal: " + name + "\n";
      err = err + " - unable to instantiate: " + className(cls);
      throw new SAXException(err);
    }
    if (descriptor.isIncremental())
    {
      if (LOG.isDebugEnabled()) {
        LOG.debug("debug: Processing incrementally for element: " + name);
      }
      try
      {
        handler.setValue(parentState._object, state._object);
      }
      catch (IllegalStateException ise)
      {
        String err = "unable to add \"" + name + "\" to ";
        err = err + parentState._fieldDesc.getXMLName();
        err = err + " due to the following error: " + ise;
        throw new SAXException(err, ise);
      }
    }
    if (state._object != null)
    {
      if (this._unmarshalListener != null) {
        this._unmarshalListener.initialized(state._object );
      }
      processAttributes(atts, classDesc);
      if (this._unmarshalListener != null) {
        this._unmarshalListener.attributesProcessed(state._object );
      }
      processNamespaces(classDesc);
    }
    else if ((state._type != null) && (!state._primitiveOrImmutable))
    {
      if (atts != null)
      {
        processWrapperAttributes(atts);
        StringBuffer buffer = new StringBuffer();
        buffer.append("The current object for element '");
        buffer.append(name);
        buffer.append("' is null. Processing attributes as location");
        buffer.append("/wrapper only and ignoring all other attribtes.");
        LOG.warn(buffer.toString());
      }
    }
    else if (atts != null)
    {
      String nil = atts.getValue("nil", "http://www.w3.org/2001/XMLSchema-instance");
      state._nil = "true".equals(nil);
      processWrapperAttributes(atts);
    }
  }
  
  private boolean isValidating()
  {
    return this._validate;
  }
  
  public void startPrefixMapping(String prefix, String uri)
    throws SAXException
  {
    if (("xml".equals(prefix)) && ("http://www.w3.org/XML/1998/namespace".equals(uri))) {
      return;
    }
    if ("xmlns".equals(prefix)) {
      return;
    }
    if (this._anyUnmarshaller != null)
    {
      this._anyUnmarshaller.startPrefixMapping(prefix, uri);
    }
    else if (this._createNamespaceScope)
    {
      this._namespaces = this._namespaces.createNamespaces();
      this._createNamespaceScope = false;
    }
    this._namespaces.addNamespace(prefix, uri);
  }
  
  public void error(SAXParseException exception)
    throws SAXException
  {
    String err = "Parsing Error : " + exception.getMessage() + '\n' + "Line : " + exception.getLineNumber() + '\n' + "Column : " + exception.getColumnNumber() + '\n';
    
    throw new SAXException(err, exception);
  }
  
  public void fatalError(SAXParseException exception)
    throws SAXException
  {
    String err = "Parsing Error : " + exception.getMessage() + '\n' + "Line : " + exception.getLineNumber() + '\n' + "Column : " + exception.getColumnNumber() + '\n';
    
    throw new SAXException(err, exception);
  }
  
  public void warning(SAXParseException exception)
    throws SAXException
  {
    String err = "Parsing Error : " + exception.getMessage() + '\n' + "Line : " + exception.getLineNumber() + '\n' + "Column : " + exception.getColumnNumber() + '\n';
    
    throw new SAXException(err, exception);
  }
  private void addReference(String idRef, Object parent, XMLFieldDescriptor descriptor) {
    ReferenceInfo refInfo = new ReferenceInfo(idRef, parent, descriptor);
    refInfo.setNext((ReferenceInfo)this._resolveTable.get(idRef));
    this._resolveTable.put(idRef, refInfo);
  }
  
  private Object createInstance(Class type, Arguments args)
    throws SAXException
  {
    Object instance = null;
    try
    {
      if (args == null) {
        instance = this._objectFactory.createInstance(type);
      } else {
        instance = this._objectFactory.createInstance(type, args._types, args._values);
      }
    }
    catch (Exception ex)
    {
      String msg = "Unable to instantiate " + type.getName() + "; ";
      throw new SAXException(msg, ex);
    }
    return instance;
  }
  
  private String getInstanceType(AttributeSet atts, String currentPackage)
    throws SAXException
  {
    if (atts == null) {
      return null;
    }
    String type = atts.getValue("type", "http://www.w3.org/2001/XMLSchema-instance");
    if (type != null)
    {
      if (type.startsWith("java:")) {
        return type.substring("java:".length());
      }
      int idx = type.indexOf(':');
      String typeNamespaceURI = null;
      if (idx >= 0)
      {
        String prefix = type.substring(0, idx);
        type = type.substring(idx + 1);
        typeNamespaceURI = this._namespaces.getNamespaceURI(prefix);
      }
      XMLClassDescriptor classDesc = null;
      try
      {
        classDesc = getInternalContext().getXMLClassDescriptorResolver().resolveByXMLName(type, typeNamespaceURI, this._loader);
        if (classDesc != null) {
          return classDesc.getJavaClass().getName();
        }
        String className = getJavaNaming().toJavaClassName(type);
        
        String adjClassName = className;
        String mappedPackage = getMappedPackage(typeNamespaceURI);
        if ((mappedPackage != null) && (mappedPackage.length() > 0)) {
          adjClassName = mappedPackage + "." + className;
        }
        classDesc = getInternalContext().getXMLClassDescriptorResolver().resolve(adjClassName, this._loader);
        if (classDesc != null) {
          return classDesc.getJavaClass().getName();
        }
        if ((currentPackage != null) && (currentPackage.length() > 0)) {
          adjClassName = currentPackage + '.' + className;
        }
        classDesc = getInternalContext().getXMLClassDescriptorResolver().resolve(adjClassName, this._loader);
        if (classDesc != null) {
          return classDesc.getJavaClass().getName();
        }
        classDesc = getInternalContext().getXMLClassDescriptorResolver().resolve(type, this._loader);
        if (classDesc != null) {
          return classDesc.getJavaClass().getName();
        }
      }
      catch (ResolverException rx)
      {
        throw new SAXException(rx);
      }
    }
    return null;
  }
  
  private String getMappedPackage(String namespace)
  {
    String lookUpKey = namespace != null ? namespace : "";
    return (String)this._namespaceToPackage.get(lookUpKey);
  }
  
  private void processAttributes(AttributeSet atts, XMLClassDescriptor classDesc)
    throws SAXException
  {
    if ((atts == null) || (atts.getSize() == 0))
    {
      if (classDesc != null)
      {
        XMLFieldDescriptor[] descriptors = classDesc.getAttributeDescriptors();
        for (int i = 0; i < descriptors.length; i++)
        {
          XMLFieldDescriptor descriptor = descriptors[i];
          if (descriptor != null) {
            if ((descriptor.isRequired()) && ((isValidating()) || (LOG.isDebugEnabled())))
            {
              String err = classDesc.getXMLName() + " is missing " + "required attribute: " + descriptor.getXMLName();
              if (this._locator != null) {
                err = err + "\n  - line: " + this._locator.getLineNumber() + " column: " + this._locator.getColumnNumber();
              }
              if (isValidating()) {
                throw new SAXException(err);
              }
              LOG.debug(err);
            }
          }
        }
      }
      return;
    }
    UnmarshalState state = (UnmarshalState)this._stateInfo.peek();
    Object object = state._object;
    if (classDesc == null)
    {
      classDesc = state._classDesc;
      if (classDesc == null)
      {
        processWrapperAttributes(atts);
        return;
      }
    }
    boolean[] processedAtts = new boolean[atts.getSize()];
    XMLFieldDescriptor[] descriptors = classDesc.getAttributeDescriptors();
    for (XMLFieldDescriptor descriptor : descriptors)
    {
      String name = descriptor.getXMLName();
      String namespace = descriptor.getNameSpaceURI();
      String path = descriptor.getLocationPath();
      String fullAttributePath = "";
      if ((path != null) && (path.length() > 0)) {
        fullAttributePath = fullAttributePath + path + "/";
      }
      fullAttributePath = fullAttributePath + name;
      if ((fullAttributePath != null) && (!name.equals(fullAttributePath)))
      {
        int index = atts.getIndex(name, namespace);
        if (index >= 0) {
          processedAtts[index] = true;
        }
      }
      else
      {
        int index = atts.getIndex(name, namespace);
        
        String attValue = null;
        if (index >= 0)
        {
          attValue = atts.getValue(index);
          processedAtts[index] = true;
        }
        try
        {
          processAttribute(name, namespace, attValue, descriptor, classDesc, object);
        }
        catch (IllegalStateException ise)
        {
          String err = "unable to add attribute \"" + name + "\" to '";
          err = err + state._classDesc.getJavaClass().getName();
          err = err + "' due to the following error: " + ise;
          throw new SAXException(err, ise);
        }
      }
    }
    for (int i = 0; i < processedAtts.length; i++) {
      if (processedAtts[i] == 0)
      {
        String namespace = atts.getNamespace(i);
        String name = atts.getName(i);
        if ("http://www.w3.org/2001/XMLSchema-instance".equals(namespace))
        {
          if ("nil".equals(name))
          {
            String value = atts.getValue(i);
            state._nil = "true".equals(value);
          }
        }
        else if (name.startsWith("xml:"))
        {
          if (LOG.isDebugEnabled())
          {
            String msg = "ignoring attribute '" + name + "' for class: " + state._classDesc.getJavaClass().getName();
            
            LOG.debug(msg);
          }
        }
        else
        {
          XMLFieldDescriptor descriptor = classDesc.getFieldDescriptor(name, namespace, NodeType.Attribute);
          if (descriptor == null)
          {
            int pIdx = this._stateInfo.size() - 2;
            String path = state._elementName;
            StringBuffer pathBuf = null;
            while (pIdx >= 0)
            {
              UnmarshalState targetState = (UnmarshalState)this._stateInfo.elementAt(pIdx);
              pIdx--;
              if (targetState._wrapper)
              {
                if (pathBuf == null) {
                  pathBuf = new StringBuffer();
                } else {
                  pathBuf.setLength(0);
                }
                pathBuf.append(targetState._elementName);
                pathBuf.append('/');
                pathBuf.append(path);
                path = pathBuf.toString();
              }
              else
              {
                classDesc = targetState._classDesc;
                descriptor = classDesc.getFieldDescriptor(name, namespace, NodeType.Attribute);
                if (descriptor != null)
                {
                  String tmpPath = descriptor.getLocationPath();
                  if (tmpPath == null) {
                    tmpPath = "";
                  }
                  if (path.equals(tmpPath)) {
                    break;
                  }
                }
                if (pathBuf == null) {
                  pathBuf = new StringBuffer();
                } else {
                  pathBuf.setLength(0);
                }
                pathBuf.append(targetState._elementName);
                pathBuf.append('/');
                pathBuf.append(path);
                path = pathBuf.toString();
                
                descriptor = null;
              }
            }
          }
          if (descriptor == null)
          {
            if (this._strictAttributes)
            {
              String error = "The attribute '" + name + "' appears illegally on element '" + state._elementName + "'.";
              
              throw new SAXException(error);
            }
          }
          else {
            try
            {
              processAttribute(name, namespace, atts.getValue(i), descriptor, classDesc, object);
            }
            catch (IllegalStateException ise)
            {
              String err = "unable to add attribute \"" + name + "\" to '";
              err = err + state._classDesc.getJavaClass().getName();
              err = err + "' due to the following error: " + ise;
              throw new SAXException(err, ise);
            }
          }
        }
      }
    }
  }
  
  private void processWrapperAttributes(AttributeSet atts)
    throws SAXException
  {
    UnmarshalState state = (UnmarshalState)this._stateInfo.peek();
    for (int i = 0; i < atts.getSize(); i++)
    {
      String name = atts.getName(i);
      String namespace = atts.getNamespace(i);
      if (!"http://www.w3.org/2001/XMLSchema-instance".equals(namespace))
      {
        XMLFieldDescriptor descriptor = null;
        XMLClassDescriptor classDesc = null;
        
        int pIdx = this._stateInfo.size() - 2;
        String path = state._elementName;
        StringBuffer pathBuf = null;
        UnmarshalState targetState = null;
        while (pIdx >= 0)
        {
          targetState = (UnmarshalState)this._stateInfo.elementAt(pIdx);
          pIdx--;
          if (targetState._wrapper)
          {
            if (pathBuf == null) {
              pathBuf = new StringBuffer();
            } else {
              pathBuf.setLength(0);
            }
            pathBuf.append(targetState._elementName);
            pathBuf.append('/');
            pathBuf.append(path);
            path = pathBuf.toString();
          }
          else
          {
            classDesc = targetState._classDesc;
            
            XMLFieldDescriptor[] descriptors = classDesc.getAttributeDescriptors();
            boolean found = false;
            for (int a = 0; a < descriptors.length; a++)
            {
              descriptor = descriptors[a];
              if (descriptor != null) {
                if (descriptor.matches(name))
                {
                  String tmpPath = descriptor.getLocationPath();
                  if (tmpPath == null) {
                    tmpPath = "";
                  }
                  if (path.equals(tmpPath))
                  {
                    found = true;
                    break;
                  }
                }
              }
            }
            if (found) {
              break;
            }
            if (pathBuf == null) {
              pathBuf = new StringBuffer();
            } else {
              pathBuf.setLength(0);
            }
            pathBuf.append(targetState._elementName);
            pathBuf.append('/');
            pathBuf.append(path);
            path = pathBuf.toString();
            
            descriptor = null;
          }
        }
        if (descriptor != null) {
          try
          {
            processAttribute(name, namespace, atts.getValue(i), descriptor, classDesc, targetState._object);
          }
          catch (IllegalStateException ise)
          {
            String err = "unable to add attribute \"" + name + "\" to '";
            err = err + state._classDesc.getJavaClass().getName();
            err = err + "' due to the following error: " + ise;
            throw new SAXException(err, ise);
          }
        }
      }
    }
  }
  
  private void processAttribute(String attName, String attNamespace, String attValue, XMLFieldDescriptor descriptor, XMLClassDescriptor classDesc, Object parent)
    throws SAXException
  {
    while (descriptor.isContainer())
    {
      FieldHandler handler = descriptor.getHandler();
      Object containerObject = handler.getValue(parent);
      if (containerObject == null)
      {
        containerObject = handler.newInstance(parent);
        handler.setValue(parent, containerObject);
      }
      ClassDescriptor containerClassDesc = ((XMLFieldDescriptorImpl)descriptor).getClassDescriptor();
      
      descriptor = ((XMLClassDescriptor)containerClassDesc).getFieldDescriptor(attName, attNamespace, NodeType.Attribute);
      
      parent = containerObject;
    }
    if (attValue == null)
    {
      if ((descriptor.isRequired()) && (isValidating()))
      {
        String err = classDesc.getXMLName() + " is missing " + "required attribute: " + attName;
        if (this._locator != null) {
          err = err + "\n  - line: " + this._locator.getLineNumber() + " column: " + this._locator.getColumnNumber();
        }
        throw new SAXException(err);
      }
      return;
    }
    if (classDesc.getIdentity() == descriptor)
    {
      try
      {
        ((IDResolverImpl)this._idResolver).bind(attValue, parent, (isValidating()) && (!getInternalContext().getLenientIdValidation()));
      }
      catch (ValidationException e)
      {
        throw new SAXException("Duplicate ID " + attValue + " encountered.", e);
      }
      UnmarshalState state = (UnmarshalState)this._stateInfo.peek();
      state._key = attValue;
      
      resolveReferences(attValue, parent);
    }
    else if (descriptor.isReference())
    {
      if (descriptor.isMultivalued())
      {
        StringTokenizer st = new StringTokenizer(attValue);
        while (st.hasMoreTokens()) {
          processIDREF(st.nextToken(), descriptor, parent);
        }
      }
      else
      {
        processIDREF(attValue, descriptor, parent);
      }
      return;
    }
    if (descriptor.isConstructorArgument()) {
      return;
    }
    FieldHandler handler = descriptor.getHandler();
    if (handler == null) {
      return;
    }
    Class type = descriptor.getFieldType();
    String valueType = descriptor.getSchemaType();
    boolean isPrimative = isPrimitive(type);
    boolean isQName = (valueType != null) && (valueType.equals("QName"));
    
    boolean isByteArray = false;
    if (type.isArray()) {
      isByteArray = type.getComponentType() == Byte.TYPE;
    }
    if (descriptor.isMultivalued())
    {
      StringTokenizer attrValueTokenizer = new StringTokenizer(attValue);
      while (attrValueTokenizer.hasMoreTokens())
      {
        attValue = attrValueTokenizer.nextToken();
        setAttributeValueOnObject(attValue, descriptor, parent, handler, type, isPrimative, isQName, isByteArray);
      }
    }
    else
    {
      setAttributeValueOnObject(attValue, descriptor, parent, handler, type, isPrimative, isQName, isByteArray);
    }
  }
  
  private void setAttributeValueOnObject(String attValue, XMLFieldDescriptor descriptor, Object parent, FieldHandler handler, Class type, boolean isPrimitive, boolean isQName, boolean isByteArray)
    throws SAXException
  {
    Object value = attValue;
    if (isPrimitive) {
      value = toPrimitiveObject(type, attValue, descriptor);
    }
    if (isByteArray) {
      if (attValue == null) {
        value = new byte[0];
      } else if ("hexBinary".equals(descriptor.getComponentType())) {
        value = HexDecoder.decode(attValue);
      } else {
        value = Base64Decoder.decode(attValue);
      }
    }
    if (isQName) {
      value = resolveNamespace(value);
    }
    handler.setValue(parent, value);
  }
  
  private Arguments processConstructorArgs(AttributeSet atts, XMLClassDescriptor classDesc)
    throws SAXException
  {
    if (classDesc == null) {
      return new Arguments();
    }
    int count = 0;
    XMLFieldDescriptor[] descriptors = classDesc.getAttributeDescriptors();
    for (XMLFieldDescriptor fieldDescriptor : descriptors) {
      if (fieldDescriptor != null) {
        if (fieldDescriptor.isConstructorArgument()) {
          count++;
        }
      }
    }
    Arguments args = new Arguments();
    if (count == 0) {
      return args;
    }
    args._values = new Object[count];
    args._types = new Class[count];
    for (XMLFieldDescriptor descriptor : descriptors) {
      if (descriptor != null) {
        if (descriptor.isConstructorArgument())
        {
          int argIndex = descriptor.getConstructorArgumentIndex();
          if (argIndex >= count)
          {
            String err = "argument index out of bounds: " + argIndex;
            throw new SAXException(err);
          }
          args._types[argIndex] = descriptor.getFieldType();
          String name = descriptor.getXMLName();
          String namespace = descriptor.getNameSpaceURI();
          
          int index = atts.getIndex(name, namespace);
          if (index >= 0)
          {
            Object value = atts.getValue(index);
            if (isPrimitive(args._types[argIndex])) {
              value = toPrimitiveObject(args._types[argIndex], (String)value, descriptor);
            } else {
              value = convertToEnumObject(descriptor, value);
            }
            String valueType = descriptor.getSchemaType();
            if ((valueType != null) && (valueType.equals("QName"))) {
              value = resolveNamespace(value);
            }
            args._values[argIndex] = value;
          }
          else if (isPrimitive(args._types[argIndex]))
          {
            args._values[argIndex] = toPrimitiveObject(args._types[argIndex], null, descriptor);
          }
          else
          {
            args._values[argIndex] = null;
          }
        }
      }
    }
    return args;
  }
  
  private Object convertToEnumObject(XMLFieldDescriptor descriptor, Object value)
  {
    Class fieldType = descriptor.getFieldType();
    try
    {
      Method valueOfMethod = fieldType.getMethod("valueOf", new Class[] { String.class });
      if ((valueOfMethod != null) && (Modifier.isStatic(valueOfMethod.getModifiers())))
      {
        Class returnType = valueOfMethod.getReturnType();
        if (returnType.isAssignableFrom(fieldType))
        {
          Object enumObject = valueOfMethod.invoke(null, new Object[] { value });
          value = enumObject;
        }
      }
    }
    catch (SecurityException e) {}catch (NoSuchMethodException e) {}catch (IllegalArgumentException e) {}catch (IllegalAccessException e) {}catch (InvocationTargetException e) {}
    return value;
  }
  
  private boolean processIDREF(String idRef, XMLFieldDescriptor descriptor, Object parent)
  {
    Object value = this._idResolver.resolve(idRef);
    if (value == null)
    {
      addReference(idRef, parent, descriptor);
    }
    else
    {
      FieldHandler handler = descriptor.getHandler();
      if (handler != null) {
        handler.setValue(parent, value);
      }
    }
    return value != null;
  }
  
  private AttributeSet processAttributeList(AttributeList atts)
    throws SAXException
  {
    if (atts == null) {
      return new AttributeSetImpl(0);
    }
    int attCount = 0;
    boolean[] validAtts = new boolean[atts.getLength()];
    for (int i = 0; i < validAtts.length; i++)
    {
      String attName = atts.getName(i);
      if (attName.equals("xmlns"))
      {
        this._namespaces.addNamespace("", atts.getValue(i));
      }
      else if (attName.startsWith("xmlns:"))
      {
        String prefix = attName.substring(XMLNS_PREFIX_LENGTH);
        this._namespaces.addNamespace(prefix, atts.getValue(i));
      }
      else
      {
        validAtts[i] = true;
        attCount++;
      }
    }
    AttributeSetImpl attSet = null;
    if (attCount > 0)
    {
      attSet = new AttributeSetImpl(attCount);
      for (int i = 0; i < validAtts.length; i++) {
        if (validAtts[i] != 0)
        {
          String namespace = null;
          String attName = atts.getName(i);
          int idx = attName.indexOf(':');
          if (idx > 0)
          {
            String prefix = attName.substring(0, idx);
            if (!prefix.equals("xml"))
            {
              attName = attName.substring(idx + 1);
              namespace = this._namespaces.getNamespaceURI(prefix);
              if (namespace == null)
              {
                String error = "The namespace associated with the prefix '" + prefix + "' could not be resolved.";
                
                throw new SAXException(error);
              }
            }
          }
          attSet.setAttribute(attName, atts.getValue(i), namespace);
        }
      }
    }
    else
    {
      attSet = new AttributeSetImpl(0);
    }
    return attSet;
  }
  
  private void processNamespaces(XMLClassDescriptor classDesc)
  {
    if (classDesc == null) {
      return;
    }
    XMLFieldDescriptor nsDescriptor = classDesc.getFieldDescriptor(null, null, NodeType.Namespace);
    if (nsDescriptor != null)
    {
      UnmarshalState state = (UnmarshalState)this._stateInfo.peek();
      FieldHandler handler = nsDescriptor.getHandler();
      if (handler != null)
      {
        Enumeration enumeration = this._namespaces.getLocalNamespacePrefixes();
        while (enumeration.hasMoreElements())
        {
          String nsPrefix = (String)enumeration.nextElement();
          if (nsPrefix == null) {
            nsPrefix = "";
          }
          String nsURI = this._namespaces.getNamespaceURI(nsPrefix);
          if (nsURI == null) {
            nsURI = "";
          }
          MapItem mapItem = new MapItem(nsPrefix, nsURI);
          handler.setValue(state._object, mapItem);
        }
      }
    }
  }
  
  private Object resolveNamespace(Object value)
    throws SAXException
  {
    if ((value == null) || (!(value instanceof String))) {
      return value;
    }
    String result = (String)value;
    int idx = result.indexOf(':');
    String prefix = null;
    if (idx > 0)
    {
      prefix = result.substring(0, idx);
      if ("xml".equals(prefix)) {
        return value;
      }
      result = result.substring(idx + 1);
    }
    String namespace = this._namespaces.getNamespaceURI(prefix);
    if ((namespace != null) && (namespace.length() > 0))
    {
      result = '{' + namespace + '}' + result;
      return result;
    }
    if ((namespace == null) && (prefix != null)) {
      throw new SAXException("The namespace associated with the prefix: '" + prefix + "' is null.");
    }
    return result;
  }
  
  private XMLClassDescriptor getClassDescriptor(String className)
    throws SAXException
  {
    Class type = null;
    try
    {
      if (this._loader != null) {
        type = this._loader.loadClass(className);
      } else {
        type = Class.forName(className);
      }
    }
    catch (ClassNotFoundException cnfe)
    {
      return null;
    }
    return getClassDescriptor(type);
  }
  
  private XMLClassDescriptor getClassDescriptor(Class cls)
    throws SAXException
  {
    if (cls == null) {
      return null;
    }
    if (cls == String.class) {
      return STRING_DESCRIPTOR;
    }
    if (cls.isArray()) {
      return null;
    }
    if (isPrimitive(cls)) {
      return null;
    }
    XMLClassDescriptor classDesc = null;
    try
    {
      InternalContext ctx = getInternalContext();
      classDesc = (XMLClassDescriptor)ctx.getXMLClassDescriptorResolver().resolve(cls);
    }
    catch (ResolverException rx) {}
    if (classDesc != null) {
      return new InternalXMLClassDescriptor(this, classDesc);
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("unable to find or create a ClassDescriptor for class: " + cls.getName());
    }
    return classDesc;
  }
  
  private XMLClassDescriptor getClassDescriptor(String className, ClassLoader loader)
    throws SAXException
  {
    XMLClassDescriptor classDesc = null;
    try
    {
      classDesc = getInternalContext().getXMLClassDescriptorResolver().resolve(className, loader);
    }
    catch (ResolverException rx)
    {
      throw new SAXException(rx);
    }
    if (classDesc != null) {
      return new InternalXMLClassDescriptor(this, classDesc);
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("unable to find or create a ClassDescriptor for class: " + className);
    }
    return classDesc;
  }
  
  private XMLClassDescriptor resolveByXMLName(String name, String namespace, ClassLoader loader)
    throws SAXException
  {
    try
    {
      return getInternalContext().getXMLClassDescriptorResolver().resolveByXMLName(name, namespace, loader);
    }
    catch (ResolverException rx)
    {
      throw new SAXException(rx);
    }
  }
  
  private String getJavaPackage(Class type)
  {
    if (type == null) {
      return null;
    }
    String pkg = (String)this._javaPackages.get(type);
    if (pkg == null)
    {
      pkg = type.getName();
      int idx = pkg.lastIndexOf('.');
      if (idx > 0) {
        pkg = pkg.substring(0, idx);
      } else {
        pkg = "";
      }
      this._javaPackages.put(type, pkg);
    }
    return pkg;
  }
  
  private String className(Class type)
  {
    if (type.isArray()) {
      return className(type.getComponentType()) + "[]";
    }
    return type.getName();
  }
  
  private static boolean isWhitespace(StringBuffer sb)
  {
    for (int i = 0; i < sb.length(); i++)
    {
      char ch = sb.charAt(i);
      switch (ch)
      {
      case '\t': 
      case '\n': 
      case '\r': 
      case ' ': 
        break;
      default: 
        return false;
      }
    }
    return true;
  }
  
  private Class loadClass(String className, ClassLoader loader)
    throws ClassNotFoundException
  {
    if (loader != null) {
      return loader.loadClass(className);
    }
    if (this._loader != null) {
      return this._loader.loadClass(className);
    }
    return Class.forName(className);
  }
  
  private void resolveReferences(String id, Object value)
    throws SAXException
  {
    if ((id == null) || (value == null)) {
      return;
    }
    if (this._resolveTable == null) {
      return;
    }
    ReferenceInfo refInfo = (ReferenceInfo)this._resolveTable.remove(id);
    while (refInfo != null)
    {
      try
      {
        FieldHandler handler = refInfo.getDescriptor().getHandler();
        if (handler != null) {
          handler.setValue(refInfo.getTarget(), value);
        }
        if ((refInfo.getTarget() instanceof MapItem)) {
          resolveReferences(refInfo.getTarget().toString(), refInfo.getTarget());
        }
      }
      catch (IllegalStateException ise)
      {
        String err = "Attempting to resolve an IDREF: " + id + "resulted in the following error: " + ise.toString();
        
        throw new SAXException(err, ise);
      }
      refInfo = refInfo.getNext();
    }
  }
  
  private Object toPrimitiveObject(Class type, String value, XMLFieldDescriptor fieldDesc)
    throws SAXException
  {
    try
    {
      return toPrimitiveObject(type, value);
    }
    catch (Exception ex)
    {
      String err = "The following error occured while trying to ";
      err = err + "unmarshal field " + fieldDesc.getFieldName();
      UnmarshalState state = (UnmarshalState)this._stateInfo.peek();
      if ((state != null) && 
        (state._object != null)) {
        err = err + " of class " + state._object.getClass().getName();
      }
      err = err + ".";
      SAXException saxException = new SAXException(err);
      saxException.initCause(ex);
      throw saxException;
    }
  }
  
  public static Object toPrimitiveObject(Class type, String value)
  {
    Object primitive = value;
    if (value != null) {
      if ((type != Character.TYPE) && (type != Character.class)) {
        value = value.trim();
      }
    }
    boolean isNull = (value == null) || (value.length() == 0);
    if ((type == Integer.TYPE) || (type == Integer.class))
    {
      if (isNull) {
        primitive = new Integer(0);
      } else {
        primitive = new Integer(value);
      }
    }
    else if ((type == Boolean.TYPE) || (type == Boolean.class))
    {
      if (isNull) {
        primitive = Boolean.FALSE;
      } else if ((value.equals("1")) || (value.toLowerCase().equals("true"))) {
        primitive = Boolean.TRUE;
      } else if ((value.equals("0")) || (value.toLowerCase().equals("false"))) {
        primitive = Boolean.FALSE;
      } else {
        throw new IllegalArgumentException(" A value of >" + value + "< cannot be converted to a boolean value.");
      }
    }
    else if ((type == Double.TYPE) || (type == Double.class))
    {
      if (isNull) {
        primitive = new Double(0.0D);
      } else {
        primitive = new Double(value);
      }
    }
    else if ((type == Long.TYPE) || (type == Long.class))
    {
      if (isNull) {
        primitive = new Long(0L);
      } else {
        primitive = new Long(value);
      }
    }
    else if ((type == Character.TYPE) || (type == Character.class))
    {
      if (!isNull) {
        primitive = new Character(value.charAt(0));
      } else {
        primitive = new Character('\000');
      }
    }
    else if ((type == Short.TYPE) || (type == Short.class))
    {
      if (isNull) {
        primitive = new Short((short)0);
      } else {
        primitive = new Short(value);
      }
    }
    else if ((type == Float.TYPE) || (type == Float.class))
    {
      if (isNull) {
        primitive = new Float(0.0F);
      } else {
        primitive = new Float(value);
      }
    }
    else if ((type == Byte.TYPE) || (type == Byte.class))
    {
      if (isNull) {
        primitive = new Byte((byte)0);
      } else {
        primitive = new Byte(value);
      }
    }
    else if (type == BigDecimal.class)
    {
      if (isNull) {
        primitive = new BigDecimal(0);
      } else {
        primitive = new BigDecimal(value);
      }
    }
    else if (type == BigInteger.class)
    {
      if (isNull) {
        primitive = BigInteger.valueOf(0L);
      } else {
        primitive = new BigInteger(value);
      }
    }
    else if (type.getSuperclass().getName().equals("java.lang.Enum")) {
      if (isNull)
      {
        primitive = null;
      }
      else
      {
        try
        {
          Method valueOfMethod = type.getMethod("fromValue", new Class[] { String.class });
          
          return valueOfMethod.invoke(null, new Object[] { value });
        }
        catch (NoSuchMethodException e) {}catch (IllegalArgumentException e)
        {
          throw new IllegalStateException(e.toString());
        }
        catch (IllegalAccessException e)
        {
          throw new IllegalStateException(e.toString());
        }
        catch (InvocationTargetException e)
        {
          if ((e.getTargetException() instanceof RuntimeException)) {
            throw ((RuntimeException)e.getTargetException());
          }
        }
        try
        {
          Method valueOfMethod = type.getMethod("valueOf", new Class[] { String.class });
          
          primitive = valueOfMethod.invoke(null, new Object[] { value });
        }
        catch (IllegalAccessException e)
        {
          throw new IllegalStateException(e.toString());
        }
        catch (InvocationTargetException e)
        {
          if ((e.getTargetException() instanceof RuntimeException)) {
            throw ((RuntimeException)e.getTargetException());
          }
        }
        catch (NoSuchMethodException e)
        {
          String err = type.getName() + " does not contain the required method: public static " + type.getName() + " valueOf(String);";
          
          throw new IllegalArgumentException(err);
        }
      }
    }
    return primitive;
  }
  
  class ElementInfo
  {
    private String _qName = null;
    private Attributes _attributes = null;
    private AttributeList _attributeList = null;
    
    ElementInfo() {}
    
    ElementInfo(String qName, Attributes atts)
    {
      this._qName = qName;
      this._attributes = atts;
    }
    
    ElementInfo(String qName, AttributeList atts)
    {
      this._qName = qName;
      this._attributeList = atts;
    }
    
    void clear()
    {
      this._qName = null;
      this._attributes = null;
      this._attributeList = null;
    }
  }
  
  class Arguments
  {
    private Object[] _values = null;
    private Class[] _types = null;
    
    Arguments() {}
    
    public int size()
    {
      if (this._values == null) {
        return 0;
      }
      return this._values.length;
    }
  }
  
  public static class ArrayHandler
  {
    Class _componentType = null;
    ArrayList<Object> _items = null;
    
    ArrayHandler(Class componentType)
    {
      if (componentType == null)
      {
        String err = "The argument 'componentType' may not be null.";
        throw new IllegalArgumentException(err);
      }
      this._componentType = componentType;
      this._items = new ArrayList();
    }
    
    public void addObject(Object obj)
    {
      if (obj == null) {
        return;
      }
      this._items.add(obj);
    }
    
    public Object getObject()
    {
      int size = this._items.size();
      Object array = Array.newInstance(this._componentType, size);
      for (int i = 0; i < size; i++) {
        Array.set(array, i, this._items.get(i));
      }
      return array;
    }
    
    public Class componentType()
    {
      return this._componentType;
    }
  }
  
  public ObjectFactory getObjectFactory()
  {
    return this._objectFactory;
  }
  
  public void setObjectFactory(ObjectFactory objectFactory)
  {
    this._objectFactory = objectFactory;
  }
}
