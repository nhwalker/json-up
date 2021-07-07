package io.github.nhwalker.jsonup.adapters.helpers;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Supplier;

import io.github.nhwalker.jsonup.Types;
import io.github.nhwalker.jsonup.convert.FromJsonContext;
import io.github.nhwalker.jsonup.convert.FromJsonConverter;
import io.github.nhwalker.jsonup.elements.JsonElement;
import io.github.nhwalker.jsonup.elements.JsonObject;
import io.github.nhwalker.jsonup.elements.JsonObjectEntry;
import io.github.nhwalker.jsonup.util.CollectionUnmodifiables;

public class MapFromJsonConverter<A extends Map<K, V>, R extends Map<K, V>, K, V>
    implements FromJsonConverter<R> {

  private final Class<? extends R> type;
  private final Function<? super A, ? extends R> prepare;
  private final Supplier<? extends A> builder;
  private final boolean skipNullCheck;

  public MapFromJsonConverter(Class<? extends R> type, Supplier<? extends A> builder,
      Function<? super A, ? extends R> prepare, boolean skipNullCheck) {
    this.type = type;
    this.prepare = prepare;
    this.builder = builder;
    this.skipNullCheck = skipNullCheck;
  }

  @Override
  public Class<? extends R> type() {
    return type;
  }

  @Override
  public R fromJson(FromJsonContext fromJsonContext, Type type, JsonElement json) {
    R result = prepare.apply(parse(fromJsonContext, type, json));
    if (!skipNullCheck) {
      CollectionUnmodifiables.verifyNoNulls(result);
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  A parse(FromJsonContext fromJsonContext, Type type, JsonElement json) {
    Entry<Type, Type> mapParams = Types.mapTypes(type);
    Type keyType = mapParams.getKey();
    Type valueType = mapParams.getValue();

    A map = builder.get();
    JsonObject jsonObject = json.asObject();
    for (JsonElement rawElement : jsonObject.entries()) {
      JsonObjectEntry element = rawElement.asObjectEntry();
      JsonElement jsonKey = element.keyElement();
      JsonElement jsonValue = element.valueElement();
      Object key = fromJsonContext.fromJson(keyType, jsonKey);
      Object value = fromJsonContext.fromJson(valueType, jsonValue);
      map.put((K) key, (V) value);
    }
    return map;
  }

}