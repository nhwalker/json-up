package io.github.nhwalker.jsonup.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;

public class CollectionUnmodifiables {

  public static <E> List<E> list(List<E> value, boolean allowNulls) {
    if (allowNulls) {
      return listWithNulls(value);
    } else {
      return listNoNulls(value);
    }
  }

  public static <E> List<E> listWithNulls(List<E> value) {
    return Collections.unmodifiableList(value);
  }

  public static <E> List<E> listNoNulls(List<? extends E> value) {
    return CollectionUnmodifiablesJreSpecific.list(value);
  }

  public static <E> List<E> copyNoNulls(E... value) {// TODO
    return CollectionUnmodifiablesJreSpecific.list(Arrays.asList(value));
  }

  public static <E> List<E> copyNoNulls(Collection<? extends E> value) {// TODO
    return CollectionUnmodifiablesJreSpecific.copyToList(value);
  }

  public static <E> Set<E> set(Set<E> value, boolean allowNulls) {
    if (allowNulls) {
      return setWithNulls(value);
    } else {
      return setNoNulls(value);
    }
  }

  public static <E> Set<E> setWithNulls(Set<E> value) {
    return Collections.unmodifiableSet(value);
  }

  public static <E> Set<E> setNoNulls(Set<E> value) {
    return CollectionUnmodifiablesJreSpecific.set(value);
  }

  public static <E> NavigableSet<E> navigableSet(NavigableSet<E> value, boolean allowNulls) {
    if (allowNulls) {
      return navigableSetWithNulls(value);
    } else {
      return navigableSetNoNulls(value);
    }
  }

  public static <E> NavigableSet<E> navigableSetWithNulls(NavigableSet<E> value) {
    return Collections.unmodifiableNavigableSet(value);
  }

  public static <E> NavigableSet<E> navigableSetNoNulls(NavigableSet<E> value) {
    return CollectionUnmodifiablesJreSpecific.navigableSet(value);
  }

  public static <K, V> Map<K, V> map(Map<K, V> value, boolean allowNulls) {
    if (allowNulls) {
      return mapWithNulls(value);
    } else {
      return mapNoNulls(value);
    }
  }

  public static <K, V> Map<K, V> mapWithNulls(Map<K, V> value) {
    return Collections.unmodifiableMap(value);
  }

  public static <K, V> Map<K, V> mapNoNulls(Map<K, V> value) {
    return CollectionUnmodifiablesJreSpecific.map(value);
  }

  public static <K, V> NavigableMap<K, V> navigableMap(NavigableMap<K, V> value, boolean allowNulls) {
    if (allowNulls) {
      return navigableMapWithNulls(value);
    } else {
      return navigableMapNoNulls(value);
    }
  }

  public static <K, V> NavigableMap<K, V> navigableMapWithNulls(NavigableMap<K, V> value) {
    return Collections.unmodifiableNavigableMap(value);
  }

  public static <K, V> NavigableMap<K, V> navigableMapNoNulls(NavigableMap<K, V> value) {
    return CollectionUnmodifiablesJreSpecific.navigableMap(value);
  }

  public static <C extends Iterable<?>> C verifyNoNulls(C collection) {
    for (Object obj : collection) {
      Objects.requireNonNull(obj);
    }
    return collection;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static <M extends Map> M verifyNoNulls(M map) {
    Set<Map.Entry> set = map.entrySet();
    for (Map.Entry<Object, Object> obj : set) {
      Objects.requireNonNull(obj.getKey());
      Objects.requireNonNull(obj.getValue());
    }
    return map;
  }

  static class CollectionUnmodifiablesJreSpecific {

    public static <E> List<E> copyToList(Collection<? extends E> value) {
      ArrayList<E> list = new ArrayList<>(value);
      return Collections.unmodifiableList(verifyNoNulls(list));
    }

    public static <E> List<E> list(List<? extends E> value) {
      return Collections.unmodifiableList(verifyNoNulls(value));
    }

    public static <E> Set<E> set(Set<E> value) {
      return Collections.unmodifiableSet(verifyNoNulls(value));
    }

    public static <E> NavigableSet<E> navigableSet(NavigableSet<E> value) {
      return Collections.unmodifiableNavigableSet(verifyNoNulls(value));
    }

    public static <K, V> Map<K, V> map(Map<K, V> value) {
      return Collections.unmodifiableMap(verifyNoNulls(value));
    }

    public static <K, V> NavigableMap<K, V> navigableMap(NavigableMap<K, V> list) {
      return Collections.unmodifiableNavigableMap(verifyNoNulls(list));
    }
  }
}
