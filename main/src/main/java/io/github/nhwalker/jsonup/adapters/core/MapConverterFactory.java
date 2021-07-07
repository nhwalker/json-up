package io.github.nhwalker.jsonup.adapters.core;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Function;

import io.github.nhwalker.jsonup.Types;
import io.github.nhwalker.jsonup.adapters.helpers.MapFromJsonConverter;
import io.github.nhwalker.jsonup.convert.FromJsonContext;
import io.github.nhwalker.jsonup.convert.FromJsonConverter;
import io.github.nhwalker.jsonup.convert.FullJsonConverter;
import io.github.nhwalker.jsonup.convert.JsonConverterFactory;
import io.github.nhwalker.jsonup.convert.ToJsonContext;
import io.github.nhwalker.jsonup.convert.ToJsonConverter;
import io.github.nhwalker.jsonup.elements.JsonElement;
import io.github.nhwalker.jsonup.elements.JsonObject;
import io.github.nhwalker.jsonup.elements.JsonObjectEntry;
import io.github.nhwalker.jsonup.util.CollectionUnmodifiables;

public enum MapConverterFactory implements JsonConverterFactory {
  DEFAULT;

  @Override
  public Class<?> type() {
    return Map.class;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> ToJsonConverter<T> toJson(ToJsonContext toJsonContext, Type type) {
    Class<?> raw = Types.rawType(type);
    if (Properties.class.isAssignableFrom(raw)) {
      return (ToJsonConverter<T>) PropertiesConverter.DEFAULT;
    }
    return (ToJsonConverter<T>) MapToJson.DEFAULT;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public <T> FromJsonConverter<T> fromJson(FromJsonContext toJsonContext, Type type) {
    Class<?> raw = Types.rawType(type);
    if (Map.class.isAssignableFrom(raw)) {
      if (raw.isAssignableFrom(Map.class)) {
        return (FromJsonConverter<T>) newMap(toJsonContext.nullsAllowed());
      } else if (raw.isAssignableFrom(NavigableMap.class)) {
        return (FromJsonConverter<T>) newNavigableMap(toJsonContext.nullsAllowed());
      } else if (raw.isAssignableFrom(HashMap.class)) {
        return (FromJsonConverter<T>) newHashMap(toJsonContext.nullsAllowed());
      } else if (raw.isAssignableFrom(LinkedHashMap.class)) {
        return (FromJsonConverter<T>) newLinkedHashMap(toJsonContext.nullsAllowed());
      } else if (raw.isAssignableFrom(TreeMap.class)) {
        return (FromJsonConverter<T>) newTreeMap(toJsonContext.nullsAllowed());
      } else if (raw.isAssignableFrom(ConcurrentHashMap.class)) {
        return (FromJsonConverter<T>) newConcurrentHashMap();
      } else if (raw.isAssignableFrom(ConcurrentSkipListMap.class)) {
        return (FromJsonConverter<T>) newConcurrentSkipListMap();
      } else if (raw.isAssignableFrom(EnumMap.class)) {
        return (FromJsonConverter<T>) newEnumMap((Class) Types.rawType(Types.iterableType(type)));
      } else if (raw.isAssignableFrom(Properties.class)) {
        return (FromJsonConverter<T>) PropertiesConverter.DEFAULT;
      }
    }
    return null;
  }

  private static <K, V> MapFromJsonConverter<LinkedHashMap<K, V>, Map<K, V>, K, V> newMap(boolean allowNulls) {
    Function<LinkedHashMap<K, V>, Map<K, V>> prepare = allowNulls ? CollectionUnmodifiables::mapWithNulls
        : CollectionUnmodifiables::mapNoNulls;
    return new MapFromJsonConverter<>(Map.class, LinkedHashMap::new, prepare, true);
  }

  private static <K, V> MapFromJsonConverter<TreeMap<K, V>, NavigableMap<K, V>, K, V> newNavigableMap(
      boolean allowNulls) {
    Function<TreeMap<K, V>, NavigableMap<K, V>> prepare = allowNulls ? CollectionUnmodifiables::navigableMapWithNulls
        : CollectionUnmodifiables::navigableMapNoNulls;
    return new MapFromJsonConverter<>(NavigableMap.class, TreeMap::new, prepare, true);
  }

  private static <K, V> MapFromJsonConverter<LinkedHashMap<K, V>, LinkedHashMap<K, V>, K, V> newLinkedHashMap(
      boolean allowNulls) {
    return new MapFromJsonConverter<>(LinkedHashMap.class, LinkedHashMap::new, Function.identity(), allowNulls);
  }

  private static <K, V> MapFromJsonConverter<HashMap<K, V>, HashMap<K, V>, K, V> newHashMap(boolean allowNulls) {
    return new MapFromJsonConverter<>(HashMap.class, HashMap::new, Function.identity(), allowNulls);
  }

  private static <K, V> MapFromJsonConverter<TreeMap<K, V>, TreeMap<K, V>, K, V> newTreeMap(boolean allowNulls) {
    return new MapFromJsonConverter<>(TreeMap.class, TreeMap::new, Function.identity(), allowNulls);
  }

  private static <K, V> MapFromJsonConverter<LinkedHashMap<K, V>, ConcurrentHashMap<K, V>, K, V> newConcurrentHashMap() {
    return new MapFromJsonConverter<>(ConcurrentHashMap.class, LinkedHashMap::new, ConcurrentHashMap::new, true);
  }

  private static <K, V> MapFromJsonConverter<ConcurrentSkipListMap<K, V>, ConcurrentSkipListMap<K, V>, K, V> newConcurrentSkipListMap() {
    return new MapFromJsonConverter<>(ConcurrentSkipListMap.class, ConcurrentSkipListMap::new, Function.identity(), true);
  }

  private static <K extends Enum<K>, V> MapFromJsonConverter<EnumMap<K, V>, EnumMap<K, V>, K, V> newEnumMap(
      Class<K> enumType) {
    return new MapFromJsonConverter<>(EnumMap.class, () -> new EnumMap<>(enumType), Function.identity(), true);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private enum MapToJson implements ToJsonConverter<Map> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return Map.class;
    }

    @Override
    public JsonElement toJson(ToJsonContext toJsonContext, Type type, Map value) {
      Entry<Type, Type> mapParams = Types.mapTypes(type);
      Type keyType = mapParams.getKey();
      Type valueType = mapParams.getValue();

      return new JsonObject(json -> {
        Set<Map.Entry<Object, Object>> entries = value.entrySet();
        for (Map.Entry<Object, Object> entry : entries) {
          JsonElement jsonKey = toJsonContext.toJson(keyType, entry.getKey());
          JsonElement jsonValue = toJsonContext.toJson(valueType, entry.getValue());
          json.add(jsonKey, jsonValue);
        }
      });
    }
  }

  static enum PropertiesConverter implements FullJsonConverter<Properties> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return Properties.class;
    }

    @Override
    public JsonElement toJson(ToJsonContext toJsonContext, Type type, Properties value) {
      return new JsonObject(json -> {
        Set<Map.Entry<Object, Object>> entries = value.entrySet();
        for (Map.Entry<Object, Object> entry : entries) {
          JsonElement jsonKey = toJsonContext.toJson(String.class, entry.getKey());
          JsonElement jsonValue = toJsonContext.toJson(String.class, entry.getValue());
          json.add(jsonKey, jsonValue);
        }
      });
    }

    @Override
    public Properties fromJson(FromJsonContext fromJsonContext, Type type, JsonElement json) {
      Properties props = new Properties();
      JsonObject jsonObject = json.asObject();
      for (JsonElement rawElement : jsonObject.entries()) {
        JsonObjectEntry element = rawElement.asObjectEntry();
        JsonElement jsonKey = element.keyElement();
        JsonElement jsonValue = element.valueElement();
        props.setProperty(jsonKey.asStringLenient().stringValue(), jsonValue.asStringLenient().stringValue());
      }
      return props;
    }

  }
}
