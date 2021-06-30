package io.github.nhwalker.jsonup.elements;

import java.io.IOException;

import io.github.nhwalker.jsonup.format.JsonArrayStyle;
import io.github.nhwalker.jsonup.format.JsonStyle;

public class JsonArrayWriter extends JsonWriter<JsonArray> {
  public static final JsonArrayWriter DEFAULT = new JsonArrayWriter();
  
  @Override
  public int write(JsonWriterContext context, JsonArray input, Appendable out, JsonStyle style, int indentLevel) throws IOException {
    JsonWriter<JsonElement> valueWriter = context.elementWriter();
    JsonArrayStyle arrayStyle = style.arrayStyle();
    int indent = arrayStyle.beforeOpenBracket().write(style, indentLevel, out);
    out.append('[');
    if (input.entries().isEmpty()) {
      indent = arrayStyle.betweenEmptyBrackets().write(style, indent, out);
    } else {
      indent = arrayStyle.afterOpenBracket().write(style, indent, out);
      indent = valueWriter.write(context, input.entries().get(0),out, style, indent);
      for (int i = 1; i < input.entries().size(); i++) {
        indent = arrayStyle.beforeEntrySeperator().write(style, indent, out);
        out.append(',');
        indent = arrayStyle.afterEntrySeperator().write(style, indent, out);
        indent = valueWriter.write(context, input.entries().get(i),out, style, indent);
      }
      indent = arrayStyle.beforeCloseBracket().write(style, indent, out);
    }

    out.append(']');
    indent = arrayStyle.afterCloseBracket().write(style, indent, out);
    return indent;
  }

}
