package io.github.nhwalker.jsonup.adapters.core;

import java.util.List;

import io.github.nhwalker.jsonup.convert.JsonAdapter;
import io.github.nhwalker.jsonup.convert.JsonConverterSet;
import io.github.nhwalker.jsonup.util.CollectionUnmodifiables;

public enum CoreSet implements JsonConverterSet {
  DEFAULT;

  private static final List<JsonAdapter> CONVERTERS = CollectionUnmodifiables.copyNoNulls(//
      ArrayConverterSet.DEFAULT, //
      BooleanConverterSet.DEFAULT, //
      EnumConverterFactory.DEFAULT, //
      IterableConverterFactory.DEFAULT, //
      MapConverterFactory.DEFAULT, //
      NumberConverterSet.DEFAULT, //
      StringConverterSet.DEFAULT//
  );

  @Override
  public List<JsonAdapter> converters() {
    return CONVERTERS;
  }

}
