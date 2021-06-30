package io.github.nhwalker.jsonup.elements;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

import io.github.nhwalker.jsonup.exceptions.JsonParseException;

public abstract class PeekingReader {

  public static PeekingReader newReader(String toRead) {
    return new StringPeekingReader(toRead);
  }

  public static PeekingReader newReader(Reader reader) {
    return new GenericPeekingReader(reader);
  }

  public static final int EOF = -1;

  public abstract int peek() throws IOException;

  public final int skipWhitespaceAndPeek() throws IOException {
    skipWhitespace();
    return peek();
  };

  public abstract int read() throws IOException;

  public abstract char[] read(int len) throws IOException;

  protected abstract String tryRead(int len) throws IOException;

  public final void readAndAssert(String literal) throws IOException, JsonParseException {
    String next = String.valueOf(tryRead(literal.length()));
    if (!next.equals(literal)) {
      throw newException("Expected \"" + literal + "\" but got \"" + next + "\"");
    }
  };

  public final void readAndAssert(char literal) throws IOException, JsonParseException {
    int next = read();
    if (next == EOF) {
      throw newException("Expected '" + literal + "' but got End Of Input");
    } else if (next != literal) {
      throw newException("Expected '" + literal + "' but got '" + (char) next + "'");
    }

  }

  /**
   * The tab character (U+0009), carriage return (U+000D), line feed (U+000A), and
   * space (U+0020) characters are the only valid whitespace characters in JSON.
   * 
   */
  public final void skipWhitespace() throws IOException {
    int next = peek();
    while (next != EOF && isWhitespace(next)) {
      read();
      next = peek();
    }
  }

  private static boolean isWhitespace(int x) {
    switch (x) {
    case '\t':
    case '\r':
    case '\n':
    case ' ':
      return true;
    default:
      return false;
    }
  }

  protected abstract Position position();

  public final JsonParseException newException(String message, Throwable cause) {
    Position p = position();
    return new JsonParseException(message, p.line(), p.column(), cause);
  }

  public final JsonParseException newException(String message) {
    Position p = position();
    return new JsonParseException(message, p.line(), p.column());
  }

  public final JsonParseException newException(Throwable cause) {
    Position p = position();
    return new JsonParseException(p.line(), p.column(), cause);
  }

  public final JsonParseException newException() {
    Position p = position();
    return new JsonParseException(p.line(), p.column());
  }

  protected static class Position {
    private final int line;
    private final int column;

    public Position(int line, int column) {
      this.line = line;
      this.column = column;
    }

    public int line() {
      return line;
    }

    public int column() {
      return column;
    }
  }

  public static class StringPeekingReader extends PeekingReader {

    private final String string;
    private int nextIndex;

    public StringPeekingReader(String str) {
      this.string = str;
    }

    @Override
    public int peek() throws IOException {
      if (nextIndex < string.length()) {
        return string.charAt(nextIndex);
      }
      return EOF;
    }

    @Override
    public int read() throws IOException {
      if (nextIndex < string.length()) {
        return string.charAt(nextIndex++);
      }
      return EOF;
    }

    @Override
    public char[] read(int len) throws IOException {
      int remaining = string.length() - nextIndex;
      if (remaining < len) {
        nextIndex = string.length();
        throw new EOFException("Expected " + len + " more characters. Got " + remaining);
      }
      String next = string.substring(nextIndex, nextIndex + len);
      nextIndex += len;
      return next.toCharArray();
    }

    @Override
    protected String tryRead(int len) throws IOException {
      if (nextIndex < string.length()) {
        String next = string.substring(nextIndex, Math.min(string.length(), nextIndex + len));
        nextIndex += next.length();
        return next;
      }
      return "";
    }

    @Override
    protected Position position() {
      int line = 0;
      int column = 0;
      for (int i = 0; i < nextIndex; i++) {
        char c = string.charAt(i);
        switch (c) {
        case '\n':
          line++;
          column = 0;
          break;
        case '\r':
          column = 0;
          break;
        default:
          column++;
          break;
        }
      }
      return new Position(line, column);
    }
  }

  public static class GenericPeekingReader extends PeekingReader {
    private static final int UNSET = -2;
    private final Reader reader;
    private int peek = UNSET;
    private int line = 0;
    private int column = 0;

    public GenericPeekingReader(Reader reader) {
      this.reader = reader;
    }

    private void updatePosition(int c) {
      switch (c) {
      case '\n':
        line++;
        column = 0;
        break;
      case '\r':
        column = 0;
        break;
      case -1:
        // ignore
        break;
      default:
        column++;
        break;
      }
    }

    @Override
    public int peek() throws IOException {
      if (peek == UNSET) {
        peek = reader.read();
      }
      return peek;
    }

    @Override
    public int read() throws IOException {
      int next;
      if (peek == UNSET) {
        next = reader.read();
      } else {
        next = peek;
        peek = UNSET;
      }
      updatePosition(next);
      return next;
    }

    @Override
    public char[] read(int len) throws IOException {
      char[] load = new char[len];
      for (int i = 0; i < load.length; i++) {
        int x = read();
        if (x == EOF) {
          throw new EOFException("Expected " + len + " more characters. Got " + i);
        }
        load[i] = (char) x;
      }
      return load;
    }

    @Override
    protected String tryRead(int len) throws IOException {
      char[] load = new char[len];
      for (int i = 0; i < load.length; i++) {
        int x = read();
        if (x == EOF) {
          return String.valueOf(load, 0, i);
        }
        load[i] = (char) x;
      }
      return String.valueOf(load);
    }

    @Override
    protected Position position() {
      return new Position(line, column);
    }

  }

}