package io.github.nhwalker.jsonup.adapters.helpers;

import java.lang.reflect.Type;

import io.github.nhwalker.jsonup.convert.FromJsonContext;
import io.github.nhwalker.jsonup.convert.FullJsonConverter;
import io.github.nhwalker.jsonup.convert.ToJsonContext;
import io.github.nhwalker.jsonup.elements.JsonElement;
import io.github.nhwalker.jsonup.elements.JsonString;

public interface StringFormatConverter<T> extends FullJsonConverter<T> {
  @Override
  default T fromJson(FromJsonContext fromJsonContext, Type type, JsonElement json) {
    return fromString(json.asStringLenient().stringValue());
  }

  @Override
  default JsonElement toJson(ToJsonContext toJsonContext, Type type, T value) {
    return new JsonString(toString(value));
  }

  T fromString(String value);

  default String toString(T value) {
    return value.toString();
  }
}
