package io.github.nhwalker.jsonup.elements;

import java.io.IOException;
import java.util.List;

import io.github.nhwalker.jsonup.format.JsonObjectStyle;
import io.github.nhwalker.jsonup.format.JsonStyle;

public class JsonObjectWriter extends JsonWriter<JsonObject> {
  public static final JsonObjectWriter DEFAULT = new JsonObjectWriter();

  @Override
  public int write(JsonWriterContext context, JsonObject input, Appendable out, JsonStyle style, int indentLevel)
      throws IOException {
    JsonWriter<JsonObjectEntry> entryWriter = context.objectEntryWriter();

    List<JsonElement> entries = input.entries();

    JsonObjectStyle objStyle = style.objectStyle();
    int indent = objStyle.beforeOpenBracket().write(style, indentLevel, out);
    out.append('{');
    if (entries.isEmpty()) {
      indent = objStyle.betweenEmptyBrackets().write(style, indent, out);
    } else {
      indent = objStyle.afterOpenBracket().write(style, indent, out);
      indent = writeEntry(context, entryWriter, entries.get(0), out, style, indent);
      for (int i = 1; i < entries.size(); i++) {
        indent = objStyle.beforeEntrySeperator().write(style, indent, out);
        out.append(',');
        indent = objStyle.afterEntrySeperator().write(style, indent, out);
        indent = writeEntry(context, entryWriter, entries.get(i), out, style, indent);
      }
      indent = objStyle.beforeCloseBracket().write(style, indent, out);
    }

    out.append('}');
    indent = objStyle.afterCloseBracket().write(style, indent, out);
    return indent;
  }

  private static int writeEntry(JsonWriterContext context, JsonWriter<JsonObjectEntry> entryWriter, JsonElement input,
      Appendable out, JsonStyle style, int indentLevel) throws IOException {
    if (input.isVirtual()) {
      return input.defaultWrite(context, out, style, indentLevel);
    } else {
      return entryWriter.write(context, input.asObjectEntry(), out, style, indentLevel);
    }
  }
}
