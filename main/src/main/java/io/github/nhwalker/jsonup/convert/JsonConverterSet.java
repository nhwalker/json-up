package io.github.nhwalker.jsonup.convert;

public interface JsonConverterSet extends JsonAdapter {

  default Class<?> type() {
    return Object.class;
  }

  public Iterable<? extends JsonAdapter> converters();

}
