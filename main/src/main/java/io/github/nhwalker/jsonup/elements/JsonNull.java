package io.github.nhwalker.jsonup.elements;

import java.io.IOException;

import io.github.nhwalker.jsonup.format.JsonStyle;

public class JsonNull extends JsonElement {
  public static final JsonNull VALUE = new JsonNull();

  private JsonNull() {
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
  
  @Override
  public int defaultWrite(JsonWriterContext context, Appendable out, JsonStyle style, int indentLevel)
      throws IOException {
    return JsonNullWriter.DEFAULT.write(context, this, out, style, indentLevel);
  }
}