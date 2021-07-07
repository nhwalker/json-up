package io.github.nhwalker.jsonup.convert.internal;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.github.nhwalker.jsonup.Types;
import io.github.nhwalker.jsonup.adapters.core.CoreSet;
import io.github.nhwalker.jsonup.convert.FromJsonContext;
import io.github.nhwalker.jsonup.convert.FromJsonConverter;
import io.github.nhwalker.jsonup.convert.JsonAdapter;
import io.github.nhwalker.jsonup.convert.JsonConverterFactory;
import io.github.nhwalker.jsonup.convert.JsonConverterSet;
import io.github.nhwalker.jsonup.convert.ToJsonConverter;
import io.github.nhwalker.jsonup.elements.JsonElement;
import io.github.nhwalker.jsonup.exceptions.JsonConversionException;
import io.github.nhwalker.jsonup.exceptions.JsonFormatException;
import io.github.nhwalker.jsonup.util.CollectionUnmodifiables;

public class FromJsonContextImpl implements FromJsonContext {

  public static FromJsonContext sharedRoot() {
    return LazyHolder.instance;
  }

  private final JsonAdapterFinder adapterSearch;
  private final FromJsonContextImpl parent;
  private final List<JsonAdapter> adapters;

  private final boolean nullsAllowed;

  private LastTypeContext lastChildTypeContext;
  private LastToJsonConverter lastConverter;

  public FromJsonContextImpl() {
    this(JsonAdapterFinder.newLocalContext());
  }

  private FromJsonContextImpl(JsonAdapterFinder searcher) {
    this.parent = null;
    this.adapters = CoreSet.DEFAULT.converters();
    this.adapterSearch = searcher;
    this.nullsAllowed = true;
  }

  private FromJsonContextImpl(FromJsonContextImpl parent, List<JsonAdapter> adapters, boolean nullsAllowed) {
    this.parent = parent;
    this.adapters = adapters;
    this.adapterSearch = parent.adapterSearch;
    this.nullsAllowed = nullsAllowed;
  }

  @Override
  public <T> T fromJson(Type type, JsonElement json) {
    FromJsonContextImpl childContext = childContext(type);
    return childContext.directFromJson(type, json);
  }

  @Override
  public <T> T fromJson(Class<? extends T> type, JsonElement json) {
    FromJsonContextImpl childContext = childContext(type);
    return childContext.directFromJson(type, json);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private <T> T directFromJson(Type type, JsonElement json) {
    FromJsonConverter fromJson = findFromJsonConverter(type);
    if (fromJson == null) {
      throw new JsonConversionException("No way to create " + type + " from json");
    } else {
      if (json.isNull()) {
        switch (fromJson.nullSupport()) {
        case DEFAULT_SUPPORT:
          return null;
        case NO_NULL:
          throw new JsonFormatException("Encountered JsonNull when null support was NO_NULL");
        case CONSUMES_NULL:
        default:
          // continue to fromJson call
        }
      }
      return (T) fromJson.fromJson(this, type, json);
    }
  }

  private FromJsonConverter<?> findFromJsonConverter(Type type) {
    LastToJsonConverter cached = lastConverter;
    if (cached != null && cached.type == type) {
      return cached.converter;
    } else {
      FromJsonConverter<?> found;
      if (adapters.isEmpty()) {
        found = parent == null ? null : parent.findFromJsonConverter(type);
      } else {
        found = directFindFromJsonConveter(type);
        if (found == null && parent != null) {
          found = parent.findFromJsonConverter(type);
        } else {
          found = null;
        }
      }
      lastConverter = new LastToJsonConverter(found, type);
      return found;
    }
  }

  private FromJsonConverter<?> directFindFromJsonConveter(Type type) {
    Class<?> rawType = Types.rawType(type);
    return directSearch(type, rawType, adapters, this);
  }

  private static FromJsonConverter<?> directSearch(Type type, Class<?> rawType,
      Iterable<? extends JsonAdapter> adapters, FromJsonContextImpl context) {
    for (JsonAdapter adapter : adapters) {
      if (rawType.isAssignableFrom(adapter.type())) {
        if (adapter instanceof JsonConverterFactory) {
          JsonConverterFactory factory = (JsonConverterFactory) adapter;
          FromJsonConverter<?> fromJson = factory.fromJson(context, type);
          if (fromJson != null) {
            return fromJson;
          }
        } else if (adapter instanceof ToJsonConverter) {
          return (FromJsonConverter<?>) adapter;
        } else if (adapter instanceof JsonConverterSet) {
          JsonConverterSet set = (JsonConverterSet) adapter;
          FromJsonConverter<?> fromJson = directSearch(type, rawType, set.converters(), context);
          if (fromJson != null) {
            return fromJson;
          }
        }
      }
    }
    return null;
  }

  @Override
  public FromJsonContextImpl childContext(Type forType) {
    LastTypeContext cached = lastChildTypeContext;
    if (cached != null && cached.type == forType) {
      return cached.context;
    }
    FromJsonContextImpl child = new FromJsonContextImpl(parent, findAdapters(Types.rawType(forType)),
        Types.mayContainNulls(forType));
    cached = new LastTypeContext(child, forType);
    lastChildTypeContext = cached;
    return child;
  }

  @Override
  public FromJsonContextImpl childContext(boolean nullsAllowed, JsonAdapter... adapters) {
    List<JsonAdapter> newAdapters = CollectionUnmodifiables.copyNoNulls(adapters);
    if (newAdapters.isEmpty() && nullsAllowed == this.nullsAllowed) {
      return this;
    }
    return new FromJsonContextImpl(this, newAdapters, nullsAllowed);
  }

  @Override
  public FromJsonContextImpl childContext(boolean nullsAllowed, Collection<? extends JsonAdapter> adapters) {
    List<JsonAdapter> newAdapters = CollectionUnmodifiables.copyNoNulls(adapters);
    if (newAdapters.isEmpty() && nullsAllowed == this.nullsAllowed) {
      return this;
    }
    return new FromJsonContextImpl(this, newAdapters, nullsAllowed);
  }

  @Override
  public FromJsonContext childContextWithNullsAllowed(boolean nullsAllowed) {
    if (nullsAllowed == this.nullsAllowed) {
      return this;
    }
    return new FromJsonContextImpl(this, Collections.emptyList(), nullsAllowed);
  }

  @Override
  public boolean nullsAllowed() {
    return nullsAllowed;
  }

  protected List<JsonAdapter> findAdapters(Class<?> rawType) {
    return Arrays.asList(adapterSearch.find(rawType));
  }

  private static class LazyHolder {
    private static final FromJsonContext instance = new FromJsonContextImpl(JsonAdapterFinder.sharedGlobalContext());
  }

  private static class LastTypeContext {
    private final FromJsonContextImpl context;
    private final Type type;

    LastTypeContext(FromJsonContextImpl context, Type type) {
      this.context = context;
      this.type = type;
    }
  }

  private static class LastToJsonConverter {
    private final FromJsonConverter<?> converter;
    private final Type type;

    LastToJsonConverter(FromJsonConverter<?> converter, Type type) {
      this.converter = converter;
      this.type = type;
    }
  }

}
