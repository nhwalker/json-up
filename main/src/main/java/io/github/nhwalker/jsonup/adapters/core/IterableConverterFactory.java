package io.github.nhwalker.jsonup.adapters.core;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Function;

import io.github.nhwalker.jsonup.Types;
import io.github.nhwalker.jsonup.adapters.helpers.IterableFromJsonConverter;
import io.github.nhwalker.jsonup.convert.FromJsonContext;
import io.github.nhwalker.jsonup.convert.FromJsonConverter;
import io.github.nhwalker.jsonup.convert.JsonConverterFactory;
import io.github.nhwalker.jsonup.convert.ToJsonContext;
import io.github.nhwalker.jsonup.convert.ToJsonConverter;
import io.github.nhwalker.jsonup.elements.JsonArray;
import io.github.nhwalker.jsonup.elements.JsonElement;
import io.github.nhwalker.jsonup.util.CollectionUnmodifiables;

public enum IterableConverterFactory implements JsonConverterFactory {
  DEFAULT;

  @Override
  public Class<?> type() {
    return Iterable.class;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> ToJsonConverter<T> toJson(ToJsonContext toJsonContext, Type type) {
    return (ToJsonConverter<T>) IterableToJson.DEFAULT;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public <T> FromJsonConverter<T> fromJson(FromJsonContext toJsonContext, Type type) {
    Class<?> raw = Types.rawType(type);
    if (List.class.isAssignableFrom(raw)) {
      if (raw.isAssignableFrom(List.class)) {
        return (FromJsonConverter<T>) newForList(toJsonContext.nullsAllowed());
      } else if (raw.isAssignableFrom(ArrayList.class)) {
        return (FromJsonConverter<T>) newForArrayList(toJsonContext.nullsAllowed());
      } else if (raw.isAssignableFrom(LinkedList.class)) {
        return (FromJsonConverter<T>) newForLinkedList(toJsonContext.nullsAllowed());
      } else if (raw.isAssignableFrom(CopyOnWriteArrayList.class)) {
        return (FromJsonConverter<T>) newForCopyOnWriteArrayList(toJsonContext.nullsAllowed());
      }
    } else if (Set.class.isAssignableFrom(raw)) {
      if (raw.isAssignableFrom(Set.class)) {
        return (FromJsonConverter<T>) newForSet((Class) Types.rawType(Types.iterableType(type)),
            toJsonContext.nullsAllowed());
      } else if (raw.isAssignableFrom(NavigableSet.class)) {
        return (FromJsonConverter<T>) newForNavigableSet(toJsonContext.nullsAllowed());
      } else if (raw.isAssignableFrom(HashSet.class)) {
        return (FromJsonConverter<T>) newForHashSet(toJsonContext.nullsAllowed());
      } else if (raw.isAssignableFrom(LinkedHashSet.class)) {
        return (FromJsonConverter<T>) newForLinkedHashSet(toJsonContext.nullsAllowed());
      } else if (raw.isAssignableFrom(TreeSet.class)) {
        return (FromJsonConverter<T>) newForTreeSet(toJsonContext.nullsAllowed());
      } else if (raw.isAssignableFrom(CopyOnWriteArraySet.class)) {
        return (FromJsonConverter<T>) newForCopyOnWriteArraySet(toJsonContext.nullsAllowed());
      } else if (raw.isAssignableFrom(EnumSet.class)) {
        return (FromJsonConverter<T>) newForEnumSet((Class) Types.rawType(Types.iterableType(type)));
      }
    } else if (Iterable.class.isAssignableFrom(raw)) {
      if (raw.isAssignableFrom(List.class)) {
        return (FromJsonConverter<T>) newForList(toJsonContext.nullsAllowed());
      }
    }
    return null;
  }

  private static <E> IterableFromJsonConverter<ArrayList<E>, List<E>, E> newForList(boolean allowNulls) {
    Function<ArrayList<E>, List<E>> prepare = allowNulls ? x -> {
      x.trimToSize();
      return CollectionUnmodifiables.listWithNulls(x);
    } : CollectionUnmodifiables::listNoNulls;
    return new IterableFromJsonConverter<>(List.class, ArrayList::new, prepare,
        true/* Checked by prepare function instead */);
  }

  private static <E> IterableFromJsonConverter<?, Set<E>, E> newForSet(Class<E> elementType, boolean allowNulls) {
    Function<LinkedHashSet<E>, Set<E>> prepare = allowNulls ? CollectionUnmodifiables::setWithNulls
        : CollectionUnmodifiables::setNoNulls;
    return new IterableFromJsonConverter<>(Set.class, LinkedHashSet::new, prepare, true);
  }

  private static <E> IterableFromJsonConverter<TreeSet<E>, NavigableSet<E>, E> newForNavigableSet(boolean allowNulls) {
    Function<TreeSet<E>, NavigableSet<E>> prepare = allowNulls ? CollectionUnmodifiables::navigableSetWithNulls
        : CollectionUnmodifiables::navigableSetNoNulls;
    return new IterableFromJsonConverter<>(NavigableSet.class, TreeSet::new, prepare, true);
  }

  private static <E> IterableFromJsonConverter<ArrayList<E>, ArrayList<E>, E> newForArrayList(boolean allowNulls) {
    return new IterableFromJsonConverter<>(ArrayList.class, ArrayList::new, CollectionUnmodifiables::verifyNoNulls,
        allowNulls);
  }

  private static <E> IterableFromJsonConverter<LinkedList<E>, LinkedList<E>, E> newForLinkedList(boolean allowNulls) {
    return new IterableFromJsonConverter<>(LinkedList.class, LinkedList::new, Function.identity(), allowNulls);
  }

  private static <E> IterableFromJsonConverter<ArrayList<E>, CopyOnWriteArrayList<E>, E> newForCopyOnWriteArrayList(
      boolean allowNulls) {
    return new IterableFromJsonConverter<>(CopyOnWriteArrayList.class, ArrayList::new, CopyOnWriteArrayList::new,
        allowNulls);
  }

  private static <E> IterableFromJsonConverter<HashSet<E>, HashSet<E>, E> newForHashSet(boolean allowNulls) {
    return new IterableFromJsonConverter<>(HashSet.class, HashSet::new, Function.identity(), allowNulls);
  }

  private static <E> IterableFromJsonConverter<LinkedHashSet<E>, LinkedHashSet<E>, E> newForLinkedHashSet(
      boolean allowNulls) {
    return new IterableFromJsonConverter<>(LinkedHashSet.class, LinkedHashSet::new, Function.identity(), allowNulls);
  }

  private static <E> IterableFromJsonConverter<TreeSet<E>, TreeSet<E>, E> newForTreeSet(boolean allowNulls) {
    return new IterableFromJsonConverter<>(TreeSet.class, TreeSet::new, Function.identity(), allowNulls);
  }

  private static <E> IterableFromJsonConverter<ArrayList<E>, CopyOnWriteArraySet<E>, E> newForCopyOnWriteArraySet(
      boolean allowNulls) {
    return new IterableFromJsonConverter<>(CopyOnWriteArraySet.class, ArrayList::new, CopyOnWriteArraySet::new,
        allowNulls);
  }

  private static <E extends Enum<E>> IterableFromJsonConverter<EnumSet<E>, EnumSet<E>, E> newForEnumSet(
      Class<E> enumType) {
    return new IterableFromJsonConverter<>(EnumSet.class, () -> EnumSet.noneOf(enumType), Function.identity(), true);
  }

  @SuppressWarnings("rawtypes")
  public static enum IterableToJson implements ToJsonConverter<Iterable> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return Iterable.class;
    }

    @Override
    public JsonElement toJson(ToJsonContext toJsonContext, Type type, Iterable value) {
      Type elementType = Types.iterableType(type);
      return new JsonArray(json -> {
        for (Object item : value) {
          json.add(toJsonContext.toJson(elementType, item));
        }
      });
    }
  }

}
