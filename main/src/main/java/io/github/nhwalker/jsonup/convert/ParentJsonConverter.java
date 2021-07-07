package io.github.nhwalker.jsonup.convert;

/**
 * Shared methods of {@link ToJsonConverter} and {@link FromJsonConverter}
 * 
 * @see ToJsonConverter
 * @see FromJsonConverter
 */
public interface ParentJsonConverter extends JsonAdapter {
  enum NullSupport {
    DEFAULT_SUPPORT, CONSUMES_NULL, NO_NULL
  }

  default NullSupport nullSupport() {
    return NullSupport.DEFAULT_SUPPORT;
  }
}
