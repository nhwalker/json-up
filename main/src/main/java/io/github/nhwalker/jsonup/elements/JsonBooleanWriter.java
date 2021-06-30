package io.github.nhwalker.jsonup.elements;

import java.io.IOException;

import io.github.nhwalker.jsonup.format.JsonStyle;

public class JsonBooleanWriter extends JsonWriter<JsonBoolean> {
  public static final JsonBooleanWriter DEFAULT = new JsonBooleanWriter();
  
  @Override
  public int write(JsonWriterContext context, JsonBoolean input, Appendable out, JsonStyle style, int indentLevel) throws IOException {
    out.append(Boolean.toString(input.booleanValue()));
    return indentLevel;
  }

}
