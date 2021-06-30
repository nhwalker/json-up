package io.github.nhwalker.jsonup.types;

import java.io.IOException;

import io.github.nhwalker.jsonup.format.JsonStyle;

public class JsonStringWriter extends JsonWriter<JsonString> {
  public static final JsonStringWriter DEFAULT = new JsonStringWriter();
  
  /*
   * From RFC 7159, "All Unicode characters may be placed within the quotation
   * marks except for the characters that must be escaped: quotation mark, reverse
   * solidus, and the control characters (U+0000 through U+001F)."
   */
  private static final String[] REPLACEMENT_CHARS;
  static {
    REPLACEMENT_CHARS = new String[128];
    for (int i = 0; i <= 0x1f; i++) {
      REPLACEMENT_CHARS[i] = String.format("\\u%04x", (int) i);
    }
    REPLACEMENT_CHARS['"'] = "\\\"";
    REPLACEMENT_CHARS['\\'] = "\\\\";
    REPLACEMENT_CHARS['\t'] = "\\t";
    REPLACEMENT_CHARS['\b'] = "\\b";
    REPLACEMENT_CHARS['\n'] = "\\n";
    REPLACEMENT_CHARS['\r'] = "\\r";
    REPLACEMENT_CHARS['\f'] = "\\f";
  }

  // TODO Replace those 2 random javascript unallowed whitespace chars
  @Override
  public int write(JsonWriterContext context, JsonString input, Appendable out, JsonStyle style, int indentLevel) throws IOException {
    String value = input.stringValue();

    String[] replacements = REPLACEMENT_CHARS;
    out.append('\"');
    int last = 0;
    int length = value.length();
    for (int i = 0; i < length; i++) {
      char c = value.charAt(i);
      String replacement;
      if (c >= replacements.length || (replacement = replacements[c]) == null) {
        continue;
      }
      if (last < i) {
        out.append(value.substring(last, i));
      }
      out.append(replacement);
      last = i + 1;
    }
    if (last < length) {
      out.append(value.substring(last, length));
    }
    out.append('\"');

    return indentLevel;
  }

}
