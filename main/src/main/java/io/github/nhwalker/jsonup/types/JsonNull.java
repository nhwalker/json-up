package io.github.nhwalker.jsonup.types;

import java.io.IOException;

import io.github.nhwalker.jsonup.format.JsonStyle;

public class JsonNull extends JsonElement {
  public static final JsonNull VALUE = new JsonNull();

  private JsonNull() {
  }

  @Override
  protected int write(Appendable out, JsonStyle style, int indentLevel) throws IOException {
    out.append("null");
    return indentLevel;
  }

  public Object nullValue() {
    return null;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof JsonNull)) {
      return false;
    }
    return true;
  }

  @Override
  public Kind kind() {
    return Kind.NULL;
  }

  @Override
  public boolean isNull() {
    return true;
  }

  @Override
  public JsonNull asNull() {
    return this;
  }
}