package io.github.nhwalker.jsonup.types;

import java.io.IOException;
import java.util.Objects;

import io.github.nhwalker.jsonup.format.JsonStyle;

public class JsonString extends JsonElement {

  private final String value;

  public JsonString(String str) {
    value = Objects.requireNonNull(str);
  }

  public String stringValue() {
    return value;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    return prime + value.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof JsonString)) {
      return false;
    }
    JsonString other = (JsonString) obj;
    if (!value.equals(other.value)) {
      return false;
    }
    return true;
  }

  @Override
  public Kind kind() {
    return Kind.STRING;
  }

  @Override
  public boolean isString() {
    return true;
  }

  @Override
  public JsonString asString() {
    return this;
  }
  @Override
  public int defaultWrite(JsonWriterContext context, Appendable out, JsonStyle style, int indentLevel)
      throws IOException {
    return JsonStringWriter.DEFAULT.write(context, this, out, style, indentLevel);
  }
}