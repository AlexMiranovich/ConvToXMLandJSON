package org.exolab.xml.validators;

public final class ValidationUtils {
  public static boolean isCombiningChar(char ch)
  {
    return false;
  }
  public static boolean isDigit(char ch)
  {
    return Character.isDigit(ch);
  }
  public static boolean isLetter(char ch)
  {
    return Character.isLetter(ch);
  }
  public static boolean isNCName(String str) {
    if ((str == null) || (str.length() == 0)) {
      return false;
    }
    char[] chars = str.toCharArray();
    
    char ch = chars[0];
    if ((!isLetter(ch)) && (ch != '_')) {
      return false;
    }
    for (int i = 1; i < chars.length; i++) {
      if (!isNCNameChar(chars[i])) {
        return false;
      }
    }
    return true;
  }
  
  public static boolean isNCNameChar(char ch)
  {
    if ((isLetter(ch)) || (isDigit(ch))) {
      return true;
    }
    if ((isExtender(ch)) || (isCombiningChar(ch))) {
      return true;
    }
    switch (ch)
    {
    case '-': 
    case '.': 
    case '_': 
      return true;
    }
    return false;
  }
  
  public static boolean isNMToken(String str)
  {
    if (str == null) {
      return false;
    }
    char[] chars = str.toCharArray();
    for (int i = 0; i < chars.length; i++)
    {
      char ch = chars[i];
      if ((!isLetter(ch)) && (!isDigit(ch)) && (!isExtender(ch)) && (!isCombiningChar(ch))) {
        switch (ch)
        {
        case '-': 
        case '.': 
        case ':': 
        case '_': 
          break;
        default: 
          return false;
        }
      }
    }
    return true;
  }
  
  public static boolean isCDATA(String str)
  {
    if (str == null) {
      return false;
    }
    char[] chars = str.toCharArray();
    for (int i = 0; i < chars.length; i++)
    {
      char ch = chars[i];
      switch (ch)
      {
      case '\t': 
      case '\n': 
      case '\r': 
        return false;
      }
    }
    return true;
  }
  
  public static boolean isExtender(char ch)
  {
    if ((ch >= '〱') && (ch <= '〵')) {
      return true;
    }
    if ((ch >= 'ー') && (ch <= 'ヾ')) {
      return true;
    }
    switch (ch)
    {
    case '·': 
    case 'ː': 
    case 'ˑ': 
    case '·': 
    case 'ـ': 
    case 'ๆ': 
    case 'ໆ': 
    case '々': 
    case 'ゝ': 
    case 'ゞ': 
      return true;
    }
    return false;
  }
  
  public static boolean isQName(String str)
  {
    if ((str == null) || (str.length() == 0)) {
      return false;
    }
    char[] chars = str.toCharArray();
    
    char ch = chars[0];
    if ((!isLetter(ch)) && (ch != '_')) {
      return false;
    }
    for (int i = 1; i < chars.length; i++) {
      if (chars[i] != ':') {
        if (!isNCNameChar(chars[i])) {
          return false;
        }
      }
    }
    return true;
  }
}
