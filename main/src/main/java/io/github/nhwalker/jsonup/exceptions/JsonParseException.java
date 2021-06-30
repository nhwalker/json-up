package io.github.nhwalker.jsonup.exceptions;

public class JsonParseException extends Exception {
  private static final long serialVersionUID = 6067214457074387338L;

  private final int line;
  private final int column;

  public JsonParseException(int line, int column) {
    super("[line: " + line + " column:" + column + "]");
    this.line = line;
    this.column = column;
  }

  public JsonParseException(int line, int column, Throwable cause) {
    super("[line: " + line + " column:" + column + "]", cause);
    this.line = line;
    this.column = column;
  }

  public JsonParseException(String message, int line, int column) {
    super(message + " [line: " + line + " column:" + column + "]");
    this.line = line;
    this.column = column;
  }

  public JsonParseException(String message, int line, int column, Throwable cause) {
    super(message + " [line: " + line + " column:" + column + "]");
    this.line = line;
    this.column = column;
  }

  public int getLine() {
    return line;
  }

  public int getColumn() {
    return column;
  }
}
