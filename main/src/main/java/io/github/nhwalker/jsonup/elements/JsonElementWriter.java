package io.github.nhwalker.jsonup.elements;

import java.io.IOException;

import io.github.nhwalker.jsonup.format.JsonStyle;

public class JsonElementWriter extends JsonWriter<JsonElement> {
  public static final JsonElementWriter DEFAULT = new JsonElementWriter();

  @Override
  public int write(JsonWriterContext context, JsonElement input, Appendable out, JsonStyle style, int indentLevel)
      throws IOException {
    if (input.isVirtual()) {
      return input.defaultWrite(context, out, style, indentLevel);
    } else {
      switch (input.kind()) {
      case ARRAY:
        return context.arrayWriter().write(context, input.asArray(), out, style, indentLevel);
      case BOOLEAN:
        return context.booleanWriter().write(context, input.asBoolean(), out, style, indentLevel);
      case NULL:
        return context.nullWriter().write(context, input.asNull(), out, style, indentLevel);
      case NUMBER:
        return context.numberWriter().write(context, input.asNumber(), out, style, indentLevel);
      case OBJECT:
        return context.objectWriter().write(context, input.asObject(), out, style, indentLevel);
      case STRING:
        return context.stringWriter().write(context, input.asString(), out, style, indentLevel);
      case OBJECT_ENTRY:
        return context.objectEntryWriter().write(context, input.asObjectEntry(), out, style, indentLevel);
      default:
        throw new IllegalArgumentException("Unknown type " + input.kind());
      }
    }

  }

}
