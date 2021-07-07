package io.github.nhwalker.jsonup.convert;

import java.lang.reflect.Type;

import io.github.nhwalker.jsonup.elements.JsonElement;

/**
 * Parse a type from a json representation
 * 
 * @param <T> the type to convert
 */
public interface FromJsonConverter<T> extends ParentJsonConverter {

  T fromJson(FromJsonContext fromJsonContext, Type type, JsonElement json);

}
