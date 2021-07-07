package io.github.nhwalker.jsonup.convert.internal;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;

import io.github.nhwalker.jsonup.Types;
import io.github.nhwalker.jsonup.convert.JsonAdapter;
import io.github.nhwalker.jsonup.convert.JsonConverterFactory;
import io.github.nhwalker.jsonup.convert.ToJsonContext;
import io.github.nhwalker.jsonup.convert.ToJsonConverter;
import io.github.nhwalker.jsonup.elements.JsonElement;
import io.github.nhwalker.jsonup.elements.JsonNull;
import io.github.nhwalker.jsonup.exceptions.JsonConversionException;

public class ToJsonContextImpl implements ToJsonContext {

  public static ToJsonContext sharedRoot() {
    return LazyHolder.instance;
  }

  private final JsonAdapterFinder adapterSearch;
  private final ToJsonContextImpl parent;
  private final JsonAdapter[] adapters;

  private LastTypeContext lastChildTypeContext;
  private LastToJsonConverter lastConverter;

  public ToJsonContextImpl() {
    this(JsonAdapterFinder.newLocalContext());
  }

  private ToJsonContextImpl(JsonAdapterFinder searcher) {
    this.parent = null;
    this.adapters = new JsonAdapter[0];
    this.adapterSearch = searcher;
  }

  private ToJsonContextImpl(ToJsonContextImpl parent, JsonAdapter[] adapters) {
    this.parent = parent;
    this.adapters = adapters;
    this.adapterSearch = parent.adapterSearch;
  }

  @Override
  public JsonElement toJson(Object object) {
    return toJson(object.getClass(), object);
  }

  @Override
  public JsonElement toJson(Type type, Object object) {
    ToJsonContextImpl childContext = childContext(type);
    return childContext.directToJson(type, object);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private JsonElement directToJson(Type type, Object object) {
    ToJsonConverter toJson = findToJsonConverter(type);
    if (toJson == null) {
      throw new JsonConversionException("No way to convert " + type + " to json");
    } else {
      if (object == null) {
        switch (toJson.nullSupport()) {
        case DEFAULT_SUPPORT:
          return JsonNull.VALUE;
        case NO_NULL:
          throw new JsonConversionException("Encountered null value when converter declared NO_NULL",
              new NullPointerException());
        case CONSUMES_NULL:
        default:
          // do nothing - continue to toJson call
        }
      }
      return toJson.toJson(this, type, object);
    }
  }

  private ToJsonConverter<?> findToJsonConverter(Type type) {
    LastToJsonConverter cached = lastConverter;
    if (cached != null && cached.type == type) {
      return cached.converter;
    } else {
      ToJsonConverter<?> found;
      if (adapters.length == 0) {
        found = parent == null ? null : parent.findToJsonConverter(type);
      } else {
        found = directFindToJsonConveter(type);
        if (found == null && parent != null) {
          found = parent.findToJsonConverter(type);
        } else {
          found = null;
        }
      }
      lastConverter = new LastToJsonConverter(found, type);
      return found;
    }
  }

  private ToJsonConverter<?> directFindToJsonConveter(Type type) {
    Class<?> rawType = Types.rawType(type);
    for (JsonAdapter adapter : adapters) {
      if (adapter.type().isAssignableFrom(rawType)) {
        if (adapter instanceof JsonConverterFactory) {
          JsonConverterFactory factory = (JsonConverterFactory) adapter;
          ToJsonConverter<?> toJson = factory.toJson(this, type);
          if (toJson != null) {
            return toJson;
          }
        } else if (adapter instanceof ToJsonConverter) {
          return (ToJsonConverter<?>) adapter;
        }
      }
    }
    return null;
  }

  @Override
  public ToJsonContextImpl childContext(Type forType) {
    LastTypeContext cached = lastChildTypeContext;
    if (cached != null && cached.type == forType) {
      return cached.context;
    }
    ToJsonContextImpl child = new ToJsonContextImpl(parent, findAdapters(Types.rawType(forType)));
    cached = new LastTypeContext(child, forType);
    lastChildTypeContext = cached;
    return child;
  }

  @Override
  public ToJsonContextImpl childContext(JsonAdapter... adapters) {
    if (adapters.length == 0) {
      return this;
    }
    return new ToJsonContextImpl(this, Arrays.copyOf(adapters, adapters.length));
  }

  @Override
  public ToJsonContextImpl childContext(Collection<? extends JsonAdapter> adapters) {
    JsonAdapter[] newAdapaters = adapters.toArray(new JsonAdapter[adapters.size()]);
    if (newAdapaters.length == 0) {
      return this;
    }
    return new ToJsonContextImpl(this, newAdapaters);
  }

  protected JsonAdapter[] findAdapters(Class<?> rawType) {
    return adapterSearch.find(rawType);
  }

  private static class LazyHolder {
    private static final ToJsonContext instance = new ToJsonContextImpl(JsonAdapterFinder.sharedGlobalContext());
  }

  private static class LastTypeContext {
    private final ToJsonContextImpl context;
    private final Type type;

    LastTypeContext(ToJsonContextImpl context, Type type) {
      this.context = context;
      this.type = type;
    }
  }

  private static class LastToJsonConverter {
    private final ToJsonConverter<?> converter;
    private final Type type;

    LastToJsonConverter(ToJsonConverter<?> converter, Type type) {
      this.converter = converter;
      this.type = type;
    }
  }

}
