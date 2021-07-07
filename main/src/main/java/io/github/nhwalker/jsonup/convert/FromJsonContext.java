package io.github.nhwalker.jsonup.convert;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;

import io.github.nhwalker.jsonup.elements.JsonElement;

public interface FromJsonContext {

  boolean nullsAllowed();

  <T> T fromJson(Type type, JsonElement element);

  <T> T fromJson(Class<? extends T> type, JsonElement element);

  FromJsonContext childContext(Type forType);

  default FromJsonContext childContext(Collection<? extends JsonAdapter> adapters) {
    return childContext(nullsAllowed(), adapters);
  }

  FromJsonContext childContext(boolean nullsAllowed, Collection<? extends JsonAdapter> adapters);

  default FromJsonContext childContext(JsonAdapter... adapters) {
    return childContext(nullsAllowed(), adapters);
  }

  default FromJsonContext childContext(boolean nullsAllowed, JsonAdapter... adapters) {
    return childContext(nullsAllowed, Arrays.asList(adapters));
  }
  FromJsonContext childContextWithNullsAllowed(boolean nullsAllowed);
}
