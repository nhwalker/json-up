package io.github.nhwalker.jsonup.exceptions;

public class JsonConversionException extends RuntimeException {

  private static final long serialVersionUID = 6393193950132515757L;

  public JsonConversionException() {
  }

  /**
   * @param message
   * @param cause
   */
  public JsonConversionException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   */
  public JsonConversionException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public JsonConversionException(Throwable cause) {
    super(cause);
  }
}
