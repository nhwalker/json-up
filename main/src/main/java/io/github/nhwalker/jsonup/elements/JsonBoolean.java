package io.github.nhwalker.jsonup.elements;

import java.io.IOException;

import io.github.nhwalker.jsonup.format.JsonStyle;

public class JsonBoolean extends JsonElement {
  public static final JsonBoolean TRUE = new JsonBoolean(true);
  public static final JsonBoolean FALSE = new JsonBoolean(false);

  public static JsonBoolean ofValue(boolean value) {
    return value ? TRUE : FALSE;
  }

  private final boolean value;

  JsonBoolean(boolean value) {
    this.value = value;
  }

  public boolean booleanValue() {
    return value;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    return prime + Boolean.hashCode(value);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof JsonBoolean)) {
      return false;
    }
    JsonBoolean other = (JsonBoolean) obj;
    if (value != other.value) {
      return false;
    }
    return true;
  }

  @Override
  public Kind kind() {
    return Kind.BOOLEAN;
  }

  @Override
  public boolean isBoolean() {
    return true;
  }

  @Override
  public JsonBoolean asBoolean() {
    return this;
  }

  @Override
  public int defaultWrite(JsonWriterContext context, Appendable out, JsonStyle style, int indentLevel)
      throws IOException {
    return JsonBooleanWriter.DEFAULT.write(context, this, out, style, indentLevel);
  }
}