package io.github.nhwalker.jsonup.convert.internal;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.github.nhwalker.jsonup.ann.AsJson;
import io.github.nhwalker.jsonup.ann.FromJson;
import io.github.nhwalker.jsonup.ann.ToJson;
import io.github.nhwalker.jsonup.convert.JsonAdapter;
import io.github.nhwalker.jsonup.exceptions.JsonConversionException;

public class JsonAdapterFinder {

  public static JsonAdapterFinder sharedGlobalContext() {
    return LazyHolder.globalRoot;
  }

  public static JsonAdapterFinder newLocalContext() {
    return new JsonAdapterFinder(false);
  }

  private static class LazyHolder {
    private static final JsonAdapterFinder globalRoot = new JsonAdapterFinder(true);
  }

  private final Map<Class<?>, JsonAdapter[]> adapterCache;

  public JsonAdapterFinder(boolean concurrent) {
    adapterCache = concurrent ? new ConcurrentHashMap<>() : new HashMap<>();
  }

  private JsonAdapter[] copy(JsonAdapter[] copy) {
    return Arrays.copyOf(copy, copy.length);
  }

  public JsonAdapter[] find(Class<?> rawType) {
    return copy(unsafeFind(rawType));
  }

  private JsonAdapter[] unsafeFind(Class<?> rawType) {
    JsonAdapter[] result;
    if (adapterCache.containsKey(rawType)) {
      result = adapterCache.get(rawType);
    } else {
      result = compute(rawType);
      adapterCache.put(rawType, result);
    }
    return result;
  }

  private JsonAdapter[] compute(Class<?> type) {
    Set<JsonAdapter> set = computeDeclared(type);

    Class<?> superType = type.getSuperclass();
    if (superType != null) {
      JsonAdapter[] superTypeAdapters = unsafeFind(superType);
      set.addAll(Arrays.asList(superTypeAdapters));
    }
    for (Class<?> superInterface : type.getInterfaces()) {
      JsonAdapter[] superTypeAdapters = unsafeFind(superInterface);
      set.addAll(Arrays.asList(superTypeAdapters));
    }

    return set.toArray(new JsonAdapter[set.size()]);
  }

  public Set<JsonAdapter> computeDeclared(Class<?> type) {
    Annotation[] annotations = type.getDeclaredAnnotations();
    LinkedHashSet<JsonAdapter> adapters = new LinkedHashSet<>(annotations.length);
    for (Annotation ann : annotations) {
      if (ann instanceof AsJson) {
        AsJson jsonAnn = (AsJson) ann;
        adapters.add(resolve(jsonAnn.value(), jsonAnn.enumValue()));
      } else if (ann instanceof FromJson) {
        FromJson jsonAnn = (FromJson) ann;
        adapters.add(resolve(jsonAnn.value(), jsonAnn.enumValue()));
      } else if (ann instanceof ToJson) {
        ToJson jsonAnn = (ToJson) ann;
        adapters.add(resolve(jsonAnn.value(), jsonAnn.enumValue()));
      }
    }
    return adapters;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private JsonAdapter resolve(Class<?> type, String enumValue) {
    try {
      if (type.isEnum()) {
        Object[] enumValues = type.getEnumConstants();
        if (enumValues.length == 1) {
          return (JsonAdapter) enumValues[0];
        } else if (enumValues.length > 1) {
          String name = enumValue.isEmpty() ? "DEFAULT" : enumValue;
          try {
            return (JsonAdapter) Enum.valueOf((Class<Enum>) type, name);
          } catch (NoSuchElementException e) {
            throw new JsonConversionException("Could not find enum value " + name + " in " + type);
          }
        } else {
          throw new JsonConversionException("No values in enum to load");
        }
      } else {
        return (JsonAdapter) type.getDeclaredConstructor().newInstance();
      }
    } catch (Exception e) {
      throw new JsonConversionException("Problem obtaining JsonAdapter " + type, e);
    }
  }

}
