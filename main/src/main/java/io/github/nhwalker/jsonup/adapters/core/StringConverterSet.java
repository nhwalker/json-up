package io.github.nhwalker.jsonup.adapters.core;

import java.lang.reflect.Type;
import java.util.List;

import io.github.nhwalker.jsonup.convert.FromJsonContext;
import io.github.nhwalker.jsonup.convert.FromJsonConverter;
import io.github.nhwalker.jsonup.convert.JsonAdapter;
import io.github.nhwalker.jsonup.convert.JsonConverterSet;
import io.github.nhwalker.jsonup.convert.ToJsonContext;
import io.github.nhwalker.jsonup.convert.ToJsonConverter;
import io.github.nhwalker.jsonup.elements.JsonElement;
import io.github.nhwalker.jsonup.elements.JsonString;
import io.github.nhwalker.jsonup.util.CollectionUnmodifiables;

public enum StringConverterSet implements JsonConverterSet {
  DEFAULT;

  private static final List<JsonAdapter> CONVERTERS = CollectionUnmodifiables.copyNoNulls(//
      new CharSequenceToJson(), //
      new StringFromJson());

  @Override
  public Iterable<? extends JsonAdapter> converters() {
    return CONVERTERS;
  }

  private static class CharSequenceToJson implements ToJsonConverter<CharSequence> {

    @Override
    public Class<?> type() {
      return CharSequence.class;
    }

    @Override
    public JsonElement toJson(ToJsonContext toJsonContext, Type type, CharSequence value) {
      return new JsonString(value.toString());
    }
  }

  private static class StringFromJson implements FromJsonConverter<String> {
    @Override
    public String fromJson(FromJsonContext fromJsonContext, Type type, JsonElement json) {
      return json.asStringLenient().stringValue();
    }

    @Override
    public Class<?> type() {
      return String.class;
    }
  }

}
