package io.github.nhwalker.jsonup.exceptions;

public class JsonFormatException extends RuntimeException {
  private static final long serialVersionUID = 6718304544299215373L;

  public JsonFormatException() {
  }

  /**
   * @param message
   * @param cause
   */
  public JsonFormatException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   */
  public JsonFormatException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public JsonFormatException(Throwable cause) {
    super(cause);
  }

}
