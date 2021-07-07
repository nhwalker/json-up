package io.github.nhwalker.jsonup.adapters.core;

import java.lang.reflect.Type;

import io.github.nhwalker.jsonup.Types;
import io.github.nhwalker.jsonup.convert.FromJsonContext;
import io.github.nhwalker.jsonup.convert.FromJsonConverter;
import io.github.nhwalker.jsonup.convert.JsonConverterFactory;
import io.github.nhwalker.jsonup.convert.ToJsonContext;
import io.github.nhwalker.jsonup.convert.ToJsonConverter;
import io.github.nhwalker.jsonup.elements.JsonElement;
import io.github.nhwalker.jsonup.elements.JsonString;

@SuppressWarnings({ "rawtypes", "unchecked" })
public enum EnumConverterFactory implements JsonConverterFactory {
  DEFAULT;

  @Override
  public Class<?> type() {
    return Enum.class;
  }

  @Override
  public <T> ToJsonConverter<T> toJson(ToJsonContext toJsonContext, Type type) {
    Class<?> raw = Types.rawType(type);
    if (raw.isEnum()) {
      return (ToJsonConverter<T>) EnumToJson.DEFAULT;
    }
    return null;
  }

  @Override
  public <T> FromJsonConverter<T> fromJson(FromJsonContext toJsonContext, Type type) {
    Class<?> raw = Types.rawType(type);
    if (raw.isEnum()) {
      return new EnumFromJson<>((Class) raw);
    }
    return null;
  }

  private enum EnumToJson implements ToJsonConverter<Enum<?>> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return Enum.class;
    }

    @Override
    public JsonElement toJson(ToJsonContext toJsonContext, Type type, Enum<?> value) {
      return new JsonString(value.name());
    }

  }

  private class EnumFromJson<T extends Enum<T>> implements FromJsonConverter<T> {
    private final Class<T> type;

    EnumFromJson(Class<T> type) {
      this.type = type;
    }

    @Override
    public Class<?> type() {
      return Enum.class;
    }

    @Override
    public T fromJson(FromJsonContext fromJsonContext, Type type, JsonElement json) {
      return Enum.valueOf(this.type, json.asStringLenient().stringValue());
    }

  }
}
