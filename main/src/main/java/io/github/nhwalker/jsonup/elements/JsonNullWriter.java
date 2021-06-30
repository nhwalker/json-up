package io.github.nhwalker.jsonup.elements;

import java.io.IOException;

import io.github.nhwalker.jsonup.format.JsonStyle;

public class JsonNullWriter extends JsonWriter<JsonNull> {
  public static final JsonNullWriter DEFAULT = new JsonNullWriter();
  
  @Override
  public int write(JsonWriterContext context, JsonNull input, Appendable out, JsonStyle style, int indentLevel) throws IOException {
    out.append("null");
    return indentLevel;
  }

}
