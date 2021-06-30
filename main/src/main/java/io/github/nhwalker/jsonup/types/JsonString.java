package io.github.nhwalker.jsonup.types;

import java.io.IOException;
import java.util.Objects;

import io.github.nhwalker.jsonup.format.JsonStyle;

public class JsonString extends JsonElement {

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
  protected int write(Appendable out, JsonStyle style, int indentLevel) throws IOException {
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
}