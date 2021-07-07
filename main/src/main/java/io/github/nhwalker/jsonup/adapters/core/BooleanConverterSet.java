package io.github.nhwalker.jsonup.adapters.core;

import java.lang.reflect.Type;
import java.util.List;

import io.github.nhwalker.jsonup.convert.FromJsonContext;
import io.github.nhwalker.jsonup.convert.JsonAdapter;
import io.github.nhwalker.jsonup.convert.FullJsonConverter;
import io.github.nhwalker.jsonup.convert.JsonConverterSet;
import io.github.nhwalker.jsonup.convert.ToJsonContext;
import io.github.nhwalker.jsonup.elements.JsonBoolean;
import io.github.nhwalker.jsonup.elements.JsonElement;
import io.github.nhwalker.jsonup.util.CollectionUnmodifiables;

public enum BooleanConverterSet implements JsonConverterSet {
  DEFAULT;

  private static final List<JsonAdapter> CONVERTERS = CollectionUnmodifiables.copyNoNulls(//
      new BooleanConverter(boolean.class), //
      new BooleanConverter(Boolean.class));

  @Override
  public Iterable<? extends JsonAdapter> converters() {
    return CONVERTERS;
  }

  private static class BooleanConverter implements FullJsonConverter<Boolean> {
    private final Class<?> type;

    public BooleanConverter(Class<?> type) {
      this.type = type;
    }

    @Override
    public JsonElement toJson(ToJsonContext toJsonContext, Type type, Boolean value) {
      return JsonBoolean.ofValue(value);
    }

    @Override
    public Class<?> type() {
      return type;
    }

    @Override
    public Boolean fromJson(FromJsonContext fromJsonContext, Type type, JsonElement json) {
      return json.asBooleanLenient().booleanValue();
    }
  }

}
