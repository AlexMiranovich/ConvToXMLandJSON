package org.exolab.xml;

public final class DescriptorType {
  public static final short ATTRIBUTE = 0;
  public static final short ELEMENT = 1;
  public static final DescriptorType attribute = new DescriptorType((short)0);
  public static final DescriptorType element = new DescriptorType((short)1);
  private short _type = -1;
  private DescriptorType(short type)
  {
    this._type = type;
  }
  public short getType()
  {
    return this._type;
  }
}
