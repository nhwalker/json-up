package io.github.nhwalker.jsonup.adapters.core;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.function.Function;

import io.github.nhwalker.jsonup.convert.FromJsonContext;
import io.github.nhwalker.jsonup.convert.FromJsonConverter;
import io.github.nhwalker.jsonup.convert.JsonAdapter;
import io.github.nhwalker.jsonup.convert.JsonConverterSet;
import io.github.nhwalker.jsonup.convert.ToJsonContext;
import io.github.nhwalker.jsonup.convert.ToJsonConverter;
import io.github.nhwalker.jsonup.elements.JsonElement;
import io.github.nhwalker.jsonup.elements.JsonNumber;
import io.github.nhwalker.jsonup.util.CollectionUnmodifiables;

public enum NumberConverterSet implements JsonConverterSet {
  DEFAULT;

  private static final List<JsonAdapter> CONVERTERS =  CollectionUnmodifiables.copyNoNulls(//
      NumberToJson.DEFAULT, //
      
      // BYTE
      new NumberFromJson<Byte>(byte.class, JsonNumber::byteValue), //
      new NumberFromJson<Byte>(Byte.class, JsonNumber::byteValue), //

      // SHORT
      new NumberFromJson<Short>(short.class, JsonNumber::shortValue), //
      new NumberFromJson<Short>(Short.class, JsonNumber::shortValue), //

      // Integer
      new NumberFromJson<Integer>(int.class, JsonNumber::intValue), //
      new NumberFromJson<Integer>(Integer.class, JsonNumber::intValue), //

      // Long
      new NumberFromJson<Long>(long.class, JsonNumber::longValue), //
      new NumberFromJson<Long>(Long.class, JsonNumber::longValue), //

      // Float
      new NumberFromJson<Float>(float.class, JsonNumber::floatValue), //
      new NumberFromJson<Float>(Float.class, JsonNumber::floatValue), //

      // Double
      new NumberFromJson<Double>(double.class, JsonNumber::doubleValue), //
      new NumberFromJson<Double>(Double.class, JsonNumber::doubleValue), //

      // BigInteger
      new NumberFromJson<BigInteger>(BigInteger.class, JsonNumber::bigIntegerValue), //

      // BigDecimal
      new NumberFromJson<BigDecimal>(BigDecimal.class, JsonNumber::bigDecimalValue));

  @Override
  public Iterable<? extends JsonAdapter> converters() {
    return CONVERTERS;
  }

  public enum NumberToJson implements ToJsonConverter<Number> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return Number.class;
    }

    @Override
    public JsonElement toJson(ToJsonContext toJsonContext, Type type, Number value) {
      return new JsonNumber(value);
    }
  }

  public static class NumberFromJson<T extends Number> implements FromJsonConverter<T> {
    private final Class<?> type;
    private final Function<JsonNumber, T> convert;

    public NumberFromJson(Class<?> type, Function<JsonNumber, T> convert) {
      this.type = type;
      this.convert = convert;
    }

    @Override
    public Class<?> type() {
      return this.type;
    }

    @Override
    public T fromJson(FromJsonContext fromJsonContext, Type type, JsonElement json) {
      JsonNumber jsonNumber = json.asNumberLenient(true);
      return convert.apply(jsonNumber);
    }

  }

}
