package io.github.nhwalker.jsonup.types;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.util.Collections;

import io.github.nhwalker.jsonup.format.JsonStyle;

public abstract class JsonElement {

  public static enum Kind {
    NULL, BOOLEAN, NUMBER, STRING, ARRAY, OBJECT, OBJECT_ENTRY,
  }

  JsonElement() {
  }

  public abstract Kind kind();

  public boolean isVirtual() {
    return false;
  }

  public boolean isNull() {
    return false;
  }

  public JsonNull asNull() {
    throw typeException(JsonNull.class);
  }

  public boolean isBoolean() {
    return false;
  }

  public JsonBoolean asBoolean() {
    throw typeException(JsonBoolean.class);
  }

  public boolean isNumber() {
    return false;
  }

  public JsonNumber asNumber() {
    throw typeException(JsonNumber.class);
  }

  public boolean isString() {
    return false;
  }

  public JsonString asString() {
    throw typeException(JsonString.class);
  }

  public boolean isArray() {
    return false;
  }

  public JsonArray asArray() {
    throw typeException(JsonArray.class);
  }

  public boolean isObject() {
    return false;
  }

  public JsonObject asObject() {
    throw typeException(JsonObject.class);
  }

  public boolean isObjectEntry() {
    return false;
  }

  public JsonObjectEntry asObjectEntry() {
    throw typeException(JsonObjectEntry.class);
  }

  public final JsonNumber asNumberLenient(boolean allowNonFinite) {
    if (isNumber()) {
      return asNumber();
    } else if (isString()) {
      String str = asString().stringValue();
      if (allowNonFinite) {
        switch (str) {
        case "NaN":
          return new JsonNumber(Double.NaN);
        case "Infinity":
          return new JsonNumber(Double.POSITIVE_INFINITY);
        case "-Infinity":
          return new JsonNumber(Double.NEGATIVE_INFINITY);
        }
      }
      return new JsonNumber(new BigDecimal(str));
    } else if (isArray()) {
      JsonArray array = asArray();
      if (array.entries().size() == 1) {
        return array.entries().get(0).asNumberLenient(allowNonFinite);
      }
    } else if (isObjectEntry()) {
      return asObjectEntry().valueElement().asNumberLenient(allowNonFinite);
    }

    throw typeException(JsonNumber.class);
  }

  public final JsonString asStringLenient() {
    if (isString()) {
      return asString();
    } else if (isNull()) {
      return new JsonString("null");
    } else if (isBoolean()) {
      return new JsonString(String.valueOf(asBoolean().booleanValue()));
    } else if (isNumber()) {
      return new JsonString(String.valueOf(asNumber().numberValue()));
    } else if (isArray()) {
      JsonArray array = asArray();
      if (array.entries().size() == 1) {
        return array.entries().get(0).asStringLenient();
      }
    }
    throw typeException(JsonString.class);
  }

  public final JsonArray asArrayLenient() {
    if (isArray()) {
      return asArray();
    }
    if (!isObjectEntry()) {
      return new JsonArray(Collections.singletonList(this), false);
    }
    throw typeException(JsonArray.class);
  }

  public final JsonBoolean asBooleanLenient() {
    if (isBoolean()) {
      return asBoolean();
    } else if (isString()) {
      return new JsonBoolean(Boolean.valueOf(asString().stringValue()));
    } else if (isArray()) {
      JsonArray array = asArray();
      if (array.entries().size() == 1) {
        return array.entries().get(0).asBooleanLenient();
      }
    }
    return JsonBoolean.FALSE;
  }

  public JsonElement asResolved() {
    return this;
  }

  public final JsonElement asKind(Kind kind) {
    switch (kind()) {
    case ARRAY:
      return asArray();
    case BOOLEAN:
      return asBoolean();
    case NULL:
      return asNull();
    case NUMBER:
      return asNumber();
    case OBJECT:
      return asObject();
    case STRING:
      return asString();
    case OBJECT_ENTRY:
      return asObjectEntry();
    default:
      // should not happen
      throw new IllegalArgumentException("Unknown kind " + kind);
    }
  }

  public final String toString() {
    return toString(JsonStyle.SINGLE_LINE_LENIENT);
  }

  public final String toString(JsonStyle style) {
    StringBuilder b = new StringBuilder();
    try {
      defaultWrite(JsonWriterContext.DEFAULT, b, style, 0);
    } catch (IOException e) {
      // StringBuilder should not throw an IOException
      throw new UncheckedIOException(e);
    }
    return b.toString();
  }

  @Override
  public abstract int hashCode();

  @Override
  public abstract boolean equals(Object obj);

  public abstract int defaultWrite(JsonWriterContext context, Appendable out, JsonStyle style, int indentLevel)
      throws IOException;

  private IllegalStateException typeException(Class<?> type) {
    return new IllegalStateException(getClass() + "is not a " + type);
  }
}
