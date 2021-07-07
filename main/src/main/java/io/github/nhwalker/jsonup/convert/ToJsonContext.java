package io.github.nhwalker.jsonup.convert;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;

import io.github.nhwalker.jsonup.elements.JsonElement;

public interface ToJsonContext {

  /**
   * Same as calling {@link #toJson(Type, Object) toJson(object.getClass(),
   * object)}
   * <p>
   * If {@code object} is {@code null}, a null pointer exception is thrown. If you
   * need to be able to handle null elements, use {@link #toJson(Type, Object)} so
   * that the appropriate converter can be used to convert the null value.
   * 
   * @param object the object to convert
   * @return the json representation of the object
   */
  default JsonElement toJson(Object object) {
    return toJson(object.getClass(), object);
  }

  /**
   * Convert an object to the desired json representation
   * 
   * @param type   the type of {@code object}
   * @param object the value to convert
   * @return json representation of the object
   */
  JsonElement toJson(Type type, Object object);

  /**
   * Create a sub-context for serializing objects of type {@code forType}
   * 
   * @param forType the type to serialize
   * @return a sub-context for serializing object of type {@code forType}
   */
  ToJsonContext childContext(Type forType);

  /**
   * Create a sub-context that will check the provided collection of
   * {@link JsonAdapter}s before delegating to the parent context
   * 
   * @param adapters the adapters to search before the parent context
   * @return a sub-context that uses the provided adapters
   */
  ToJsonContext childContext(Collection<? extends JsonAdapter> adapters);

  /**
   * Same as calling {@link #childContext(Collection) childContext(Arrays.asList(adapters))}
   */
  default ToJsonContext childContext(JsonAdapter... adapters) {
    return childContext(Arrays.asList(adapters));
  }
}
