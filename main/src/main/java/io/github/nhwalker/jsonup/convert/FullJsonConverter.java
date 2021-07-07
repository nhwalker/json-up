package io.github.nhwalker.jsonup.convert;

/**
 * Both a {@link ToJsonConverter} and a {@link FromJsonConverter}
 * 
 * @param <T> the type to convert
 */
public interface FullJsonConverter<T> extends ToJsonConverter<T>, FromJsonConverter<T> {

}
