package io.github.nhwalker.jsonup.adapters.core;

import java.lang.reflect.Type;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import io.github.nhwalker.jsonup.Types;
import io.github.nhwalker.jsonup.convert.FromJsonContext;
import io.github.nhwalker.jsonup.convert.FromJsonConverter;
import io.github.nhwalker.jsonup.convert.JsonAdapter;
import io.github.nhwalker.jsonup.convert.FullJsonConverter;
import io.github.nhwalker.jsonup.convert.JsonConverterFactory;
import io.github.nhwalker.jsonup.convert.JsonConverterSet;
import io.github.nhwalker.jsonup.convert.ToJsonContext;
import io.github.nhwalker.jsonup.convert.ToJsonConverter;
import io.github.nhwalker.jsonup.elements.JsonArray;
import io.github.nhwalker.jsonup.elements.JsonElement;
import io.github.nhwalker.jsonup.elements.JsonString;
import io.github.nhwalker.jsonup.util.CollectionUnmodifiables;

public enum ArrayConverterSet implements JsonConverterSet {
  DEFAULT;

  private static final List<JsonAdapter> CONVERTERS = CollectionUnmodifiables.copyNoNulls(//
      ByteArrayConverter.DEFAULT, //
      ShortArrayConverter.DEFAULT, //
      IntArrayConverter.DEFAULT, //
      LongArrayConverter.DEFAULT, //
      FloatArrayConverter.DEFAULT, //
      DoubleArrayConverter.DEFAULT, //
      BooleanArrayConverter.DEFAULT, //
      CharArrayConverter.DEFAULT, //
      ObjectArrayConverterFactory.DEFAULT);

  @Override
  public Iterable<? extends JsonAdapter> converters() {
    return CONVERTERS;
  }

  public static enum ByteArrayConverter implements FullJsonConverter<byte[]> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return byte[].class;
    }

    @Override
    public JsonElement toJson(ToJsonContext toJsonContext, Type type, byte[] value) {
      return new JsonString(Base64.getEncoder().encodeToString(value));
    }

    @Override
    public byte[] fromJson(FromJsonContext fromJsonContext, Type type, JsonElement json) {
      if (json.isArray()) {
        JsonArray jsonArray = json.asArray();
        byte[] array = new byte[jsonArray.entries().size()];
        List<JsonElement> list = jsonArray.entries();
        for (int i = 0; i < array.length; i++) {
          array[i] = list.get(i).asNumber().byteValue();
        }
        return array;
      } else if (json.isString()) {
        return Base64.getDecoder().decode(json.asString().stringValue());
      }
      return new byte[] { json.asNumberLenient(false).byteValue() };
    }

  }

  public static enum ShortArrayConverter implements FullJsonConverter<short[]> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return short[].class;
    }

    @Override
    public JsonElement toJson(ToJsonContext toJsonContext, Type type, short[] value) {
      return new JsonArray(json -> {
        for (short i : value) {
          json.add(i);
        }
      });
    }

    @Override
    public short[] fromJson(FromJsonContext fromJsonContext, Type type, JsonElement json) {
      JsonArray jsonArray = json.asArrayLenient();
      short[] array = new short[jsonArray.entries().size()];
      List<JsonElement> list = jsonArray.entries();
      for (int i = 0; i < array.length; i++) {
        array[i] = list.get(i).asNumber().shortValue();
      }
      return array;
    }
  }

  public static enum IntArrayConverter implements FullJsonConverter<int[]> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return int[].class;
    }

    @Override
    public JsonElement toJson(ToJsonContext toJsonContext, Type type, int[] value) {
      return new JsonArray(json -> {
        for (int i : value) {
          json.add(i);
        }
      });
    }

    @Override
    public int[] fromJson(FromJsonContext fromJsonContext, Type type, JsonElement json) {
      JsonArray jsonArray = json.asArrayLenient();
      int[] array = new int[jsonArray.entries().size()];
      List<JsonElement> list = jsonArray.entries();
      for (int i = 0; i < array.length; i++) {
        array[i] = list.get(i).asNumber().intValue();
      }
      return array;
    }

  }

  public static enum LongArrayConverter implements FullJsonConverter<long[]> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return long[].class;
    }

    @Override
    public JsonElement toJson(ToJsonContext toJsonContext, Type type, long[] value) {
      return new JsonArray(json -> {
        for (long i : value) {
          json.add(i);
        }
      });
    }

    @Override
    public long[] fromJson(FromJsonContext fromJsonContext, Type type, JsonElement json) {
      JsonArray jsonArray = json.asArrayLenient();
      long[] array = new long[jsonArray.entries().size()];
      List<JsonElement> list = jsonArray.entries();
      for (int i = 0; i < array.length; i++) {
        array[i] = list.get(i).asNumber().longValue();
      }
      return array;
    }
  }

  public static enum FloatArrayConverter implements FullJsonConverter<float[]> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return float[].class;
    }

    @Override
    public JsonElement toJson(ToJsonContext toJsonContext, Type type, float[] value) {
      return new JsonArray(json -> {
        for (float i : value) {
          json.add(i);
        }
      });
    }

    @Override
    public float[] fromJson(FromJsonContext fromJsonContext, Type type, JsonElement json) {
      JsonArray jsonArray = json.asArrayLenient();
      float[] array = new float[jsonArray.entries().size()];
      List<JsonElement> list = jsonArray.entries();
      for (int i = 0; i < array.length; i++) {
        array[i] = list.get(i).asNumber().floatValue();
      }
      return array;
    }
  }

  public static enum DoubleArrayConverter implements FullJsonConverter<double[]> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return double[].class;
    }

    @Override
    public JsonElement toJson(ToJsonContext toJsonContext, Type type, double[] value) {
      return new JsonArray(json -> {
        for (double i : value) {
          json.add(i);
        }
      });
    }

    @Override
    public double[] fromJson(FromJsonContext fromJsonContext, Type type, JsonElement json) {
      JsonArray jsonArray = json.asArrayLenient();
      double[] array = new double[jsonArray.entries().size()];
      List<JsonElement> list = jsonArray.entries();
      for (int i = 0; i < array.length; i++) {
        array[i] = list.get(i).asNumber().doubleValue();
      }
      return array;
    }
  }

  public static enum BooleanArrayConverter implements FullJsonConverter<boolean[]> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return boolean[].class;
    }

    @Override
    public JsonElement toJson(ToJsonContext toJsonContext, Type type, boolean[] value) {
      return new JsonArray(json -> {
        for (boolean v : value) {
          json.add(v);
        }
      });
    }

    @Override
    public boolean[] fromJson(FromJsonContext fromJsonContext, Type type, JsonElement json) {
      JsonArray jsonArray = json.asArrayLenient();
      boolean[] array = new boolean[jsonArray.entries().size()];
      List<JsonElement> list = jsonArray.entries();
      for (int i = 0; i < array.length; i++) {
        array[i] = list.get(i).asBooleanLenient().booleanValue();
      }
      return array;
    }
  }

  public static enum CharArrayConverter implements FullJsonConverter<char[]> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return char[].class;
    }

    @Override
    public JsonElement toJson(ToJsonContext toJsonContext, Type type, char[] value) {
      return new JsonString(new String(value));
    }

    @Override
    public char[] fromJson(FromJsonContext fromJsonContext, Type type, JsonElement json) {
      if (json.isArray()) {
        JsonArray jsonArray = json.asArray();
        char[] array = new char[jsonArray.entries().size()];
        List<JsonElement> list = jsonArray.entries();
        for (int i = 0; i < array.length; i++) {
          array[i] = list.get(i).asBooleanLenient().asStringLenient().stringValue().charAt(0);
        }
        return array;
      }
      String str = json.asStringLenient().stringValue();
      return str.toCharArray();
    }
  }

  @SuppressWarnings("unchecked")
  private static enum ObjectArrayConverterFactory implements JsonConverterFactory {
    DEFAULT;

    @Override
    public Class<?> type() {
      return Object[].class;
    }

    @Override
    public <T> ToJsonConverter<T> toJson(ToJsonContext toJsonContext, Type type) {
      return (ToJsonConverter<T>) ObjectArrayToJsonConverter.DEFAULT;
    }

    @Override
    public <T> FromJsonConverter<T> fromJson(FromJsonContext toJsonContext, Type type) {
      Class<?> raw = Types.rawType(type);
      if (raw.isArray() && !Types.isPrimitiveArray(raw)) {
        return (FromJsonConverter<T>) new ObjectArrayFromJson(raw);
      }
      return null;
    }

    public static enum ObjectArrayToJsonConverter implements ToJsonConverter<Object[]> {
      DEFAULT;

      @Override
      public Class<?> type() {
        return Object[].class;
      }

      @Override
      public JsonElement toJson(ToJsonContext toJsonContext, Type type, Object[] value) {
        Type itemType = Types.arrayType(type);
        return new JsonArray(json -> {
          for (Object v : value) {
            json.add(toJsonContext.toJson(itemType, v));
          }
        });
      }

    }

    private static class ObjectArrayFromJson implements FromJsonConverter<Object[]> {
      private final Class<?> type;

      ObjectArrayFromJson(Class<?> type) {
        this.type = type;
      }

      @Override
      public Class<?> type() {
        return type;
      }

      @Override
      public Object[] fromJson(FromJsonContext fromJsonContext, Type type, JsonElement json) {
        Type itemType = Types.arrayType(type);
        JsonArray jsonArray = json.asArrayLenient();
        Object[] array = new Object[jsonArray.entries().size()];
        List<JsonElement> list = jsonArray.entries();
        if(fromJsonContext.nullsAllowed()) {
          for (int i = 0; i < array.length; i++) {
            array[i] = fromJsonContext.fromJson(itemType, list.get(i));
          }  
        }else {
          for (int i = 0; i < array.length; i++) {
            array[i] = Objects.requireNonNull(fromJsonContext.fromJson(itemType, list.get(i)));
          }
        }
        
        return array;
      }

    }
  }

}
