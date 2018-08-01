package org.exolab.util.dialog;

import java.io.IOException;

public class ConsoleDialog implements Dialog {
  public ConsoleDialog() {
  }

  public boolean confirm(String message) {
    try {
      while(true) {
        System.out.println();
        System.out.print(message);
        System.out.print("(y|n|?) : ");
        int ch = this.getChar();
        System.out.println();
        switch(ch) {
          case 63:
            System.out.println("y = yes, n = no");
            break;
          case 110:
            return false;
          case 121:
            return true;
          default:
            System.out.print("invalid input, expecting ");
            System.out.println("'y', 'n', or '?'.");
        }
      }
    } catch (IOException var3) {
      System.out.println(var3);
      return false;
    }
  }

  private int getChar() throws IOException {
    int ch = System.in.read();

    while(System.in.available() > 0) {
      switch(System.in.read()) {
        case 10:
        case 13:
          break;
        default:
          ch = 0;
      }
    }

    return ch;
  }

  public char confirm(String message, String values) {
    return this.confirm(message, values, "no help available...");
  }

  public char confirm(String message, String values, String help) {
    String prompt = this.makeList(values);

    try {
      while(true) {
        System.out.println();
        System.out.print(message + prompt);
        int ch = this.getChar();
        System.out.println();
        if (values.indexOf(ch) != -1) {
          return (char)ch;
        }

        if (ch == 63) {
          System.out.println(help);
        } else {
          System.out.print("invalid input, expecting ");
          System.out.println(this.listInput(values));
        }
      }
    } catch (IOException var6) {
      System.out.println(var6);
      return '\u0000';
    }
  }

  public void notify(String message) {
    System.out.println(message);
  }

  private String makeList(String values) {
    StringBuffer sb = new StringBuffer(values.length() * 2);
    sb.append('(');

    for(int i = 0; i < values.length(); ++i) {
      sb.append(values.charAt(i)).append('|');
    }

    sb.append("?)");
    return sb.toString();
  }

  private String listInput(String values) {
    StringBuffer sb = new StringBuffer(values.length() * 4);

    for(int i = 0; i < values.length(); ++i) {
      sb.append('\'').append(values.charAt(i)).append("', ");
    }

    sb.append("or '?'");
    return sb.toString();
  }
}
