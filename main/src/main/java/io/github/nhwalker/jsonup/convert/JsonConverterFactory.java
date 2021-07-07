package io.github.nhwalker.jsonup.convert;

import java.lang.reflect.Type;

public interface JsonConverterFactory extends JsonAdapter {

  default Class<?> type() {
    return Object.class;
  }
  
  <T> ToJsonConverter<T> toJson(ToJsonContext toJsonContext, Type type);
  
  <T> FromJsonConverter<T> fromJson(FromJsonContext toJsonContext, Type type);
}
