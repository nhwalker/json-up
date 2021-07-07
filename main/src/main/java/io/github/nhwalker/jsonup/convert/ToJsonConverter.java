package io.github.nhwalker.jsonup.convert;

import java.lang.reflect.Type;

import io.github.nhwalker.jsonup.elements.JsonElement;

/**
 * Converts a type into a json representation
 *
 * @param <T> the type to convert
 */
public interface ToJsonConverter<T> extends ParentJsonConverter {

  JsonElement toJson(ToJsonContext toJsonContext, Type type, T value);
  
}
