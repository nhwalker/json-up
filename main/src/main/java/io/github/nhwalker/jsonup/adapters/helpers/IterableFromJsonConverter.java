package io.github.nhwalker.jsonup.adapters.helpers;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import io.github.nhwalker.jsonup.Types;
import io.github.nhwalker.jsonup.convert.FromJsonContext;
import io.github.nhwalker.jsonup.convert.FromJsonConverter;
import io.github.nhwalker.jsonup.elements.JsonArray;
import io.github.nhwalker.jsonup.elements.JsonElement;
import io.github.nhwalker.jsonup.util.CollectionUnmodifiables;

public class IterableFromJsonConverter<A extends Collection<E>, R extends Iterable<E>, E>
    implements FromJsonConverter<R> {

  private final Class<? extends R> type;
  private final Supplier<? extends A> builder;
  private final Function<? super A, ? extends R> prepare;
  private final boolean skipNullCheck;

  public IterableFromJsonConverter(Class<? extends R> type, Supplier<? extends A> builder,
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

  A parse(FromJsonContext fromJsonContext, Type type, JsonElement json) {
    Type elementType = Types.iterableType(type);
    A list = builder.get();
    JsonArray array = json.asArrayLenient();
    for (JsonElement item : array.entries()) {
      list.add(fromJsonContext.fromJson(elementType, item));
    }
    return list;
  }

}