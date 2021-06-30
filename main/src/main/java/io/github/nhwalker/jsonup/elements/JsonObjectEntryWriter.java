package io.github.nhwalker.jsonup.elements;

import java.io.IOException;

import io.github.nhwalker.jsonup.format.JsonObjectStyle;
import io.github.nhwalker.jsonup.format.JsonStyle;

public class JsonObjectEntryWriter extends JsonWriter<JsonObjectEntry> {
  public static final JsonObjectEntryWriter DEFAULT = new JsonObjectEntryWriter();

  @Override
  public int write(JsonWriterContext context, JsonObjectEntry input, Appendable out, JsonStyle style, int indentLevel)
      throws IOException {
    JsonWriter<JsonElement> valueWriter = context.elementWriter();

    JsonObjectStyle objStyle = style.objectStyle();
    int indent = indentLevel;

    indent = valueWriter.write(context, input.keyElement(), out, style, indent);

    indent = objStyle.beforeKeyValueSeperator().write(style, indent, out);
    out.append(':');
    indent = objStyle.afterKeyValueSeperator().write(style, indent, out);

    indent = valueWriter.write(context, input.valueElement(), out, style, indent);
    return indent;
  }

}
