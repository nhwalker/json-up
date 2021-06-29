package io.github.nhwalker.jsonup.types;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import io.github.nhwalker.jsonup.exceptions.JsonFormatException;
import io.github.nhwalker.jsonup.format.JsonArrayStyle;
import io.github.nhwalker.jsonup.format.JsonObjectStyle;
import io.github.nhwalker.jsonup.format.JsonStyle;

public abstract class JsonElement {

  public String toString() {
    return toString(JsonStyle.COMPACT);
  }

  public String toString(JsonStyle style) {
    StringBuilder b = new StringBuilder();
    try {
      write(b, style);
    } catch (IOException e) {
      // StringBuilder should not throw an IOException
      throw new UncheckedIOException(e);
    }
    return b.toString();
  }

  public void write(Appendable out) throws IOException {
    write(out, JsonStyle.COMPACT);
  }

  public void write(Appendable out, JsonStyle style) throws IOException {
    write(out, style, 0);
  }

  @Override
  public abstract int hashCode();

  @Override
  public abstract boolean equals(Object obj);

  protected abstract int write(Appendable out, JsonStyle style, int indentLevel) throws IOException;

  public static class JsonNull extends JsonElement {
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

  }

  public static class JsonBoolean extends JsonElement {
    private final boolean value;

    public JsonBoolean(boolean value) {
      this.value = value;
    }

    public boolean booleanValue() {
      return value;
    }

    @Override
    protected int write(Appendable out, JsonStyle style, int indentLevel) throws IOException {
      out.append(Boolean.toString(value));
      return indentLevel;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      return prime + Boolean.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof JsonBoolean)) {
        return false;
      }
      JsonBoolean other = (JsonBoolean) obj;
      if (value != other.value) {
        return false;
      }
      return true;
    }
  }

  public static class JsonNumber extends JsonElement {
    private final Number value;

    public JsonNumber(Number value) {
      this.value = Objects.requireNonNull(value);
    }

    public int intValue() {
      return value.intValue();
    }

    public long longValue() {
      return value.longValue();
    }

    public double doubleValue() {
      return value.doubleValue();
    }

    public short shortValue() {
      return value.shortValue();
    }

    public byte byteValue() {
      return value.byteValue();
    }

    public BigInteger bigIntegerValue() {
      if (value instanceof BigInteger) {
        return (BigInteger) value;
      } else {
        return BigInteger.valueOf(value.longValue());
      }
    }

    public BigDecimal bigDecimalValue() {
      if (value instanceof BigDecimal) {
        return (BigDecimal) value;
      } else {
        return BigDecimal.valueOf(value.doubleValue());
      }
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
      if (!(obj instanceof JsonNumber)) {
        return false;
      }
      JsonNumber other = (JsonNumber) obj;
      if (!value.equals(other.value)) {
        return false;
      }
      return true;
    }

    @Override
    protected int write(Appendable out, JsonStyle style, int indentLevel) throws IOException {
      if (value instanceof Integer //
          || value instanceof Long //
          || value instanceof Byte //
          || value instanceof Short//
          || value instanceof AtomicInteger //
          || value instanceof AtomicLong //
          || value instanceof BigInteger) {
        writeInt(out, style, value.intValue());
      } else if (value instanceof Long || value instanceof AtomicLong) {
        writeLong(out, style, value.longValue());
      } else if (value instanceof BigInteger) {
        writeBigInteger(out, style, (BigInteger) value);
      } else if (value instanceof Float) {
        writeFloat(out, style, value.floatValue());
      } else if (value instanceof Double) {
        writeDouble(out, style, value.doubleValue());
      } else if (value instanceof BigDecimal) {
        writeBigDecimal(out, style, (BigDecimal) value);
      } else {
        long xLong = value.longValue();
        double xDouble = value.doubleValue();
        if (xLong == xDouble) {
          writeLong(out, style, xLong);
        } else {
          writeDouble(out, style, xDouble);
        }
      }
      return indentLevel;
    }

    private void writeFloat(Appendable out, JsonStyle style, float x) throws IOException {
      if (!Float.isFinite(x)) {
        if (!style.numberStyle().allowNonFiniteStrings()) {
          throw new JsonFormatException("Cannot encode non-finite float: " + x);
        }
        out.append('"');
        out.append(Float.toString(x));
        out.append('"');
      } else if (style.numberStyle().forceEngineeringString()) {
        out.append(BigDecimal.valueOf(x).toEngineeringString());
      } else if (style.numberStyle().forcePlainString()) {
        out.append(BigDecimal.valueOf(x).toPlainString());
      } else {
        out.append(Float.toString(x));
      }
    }

    private void writeDouble(Appendable out, JsonStyle style, double x) throws IOException {
      if (!Double.isFinite(x)) {
        if (!style.numberStyle().allowNonFiniteStrings()) {
          throw new JsonFormatException("Cannot encode non-finite double: " + x);
        }
        out.append('"');
        out.append(Double.toString(x));
        out.append('"');
      } else if (style.numberStyle().forceEngineeringString()) {
        out.append(BigDecimal.valueOf(x).toEngineeringString());
      } else if (style.numberStyle().forcePlainString()) {
        out.append(BigDecimal.valueOf(x).toPlainString());
      } else {
        out.append(Double.toString(x));
      }
    }

    private void writeBigInteger(Appendable out, JsonStyle style, BigInteger x) throws IOException {
      if (style.numberStyle().forceEngineeringString()) {
        out.append(new BigDecimal(x).toEngineeringString());
      } else {
        out.append(x.toString());
      }
    }

    private void writeLong(Appendable out, JsonStyle style, long x) throws IOException {
      if (style.numberStyle().forceEngineeringString()) {
        out.append(BigDecimal.valueOf(x).toEngineeringString());
      } else {
        out.append(Long.toString(x));
      }
    }

    private void writeInt(Appendable out, JsonStyle style, int x) throws IOException {
      if (style.numberStyle().forceEngineeringString()) {
        BigDecimal.valueOf(x).toEngineeringString();
      } else {
        out.append(Integer.toString(x));
      }
    }

    private void writeBigDecimal(Appendable out, JsonStyle style, BigDecimal x) throws IOException {
      if (style.numberStyle().forceEngineeringString()) {
        out.append(x.toEngineeringString());
      } else if (style.numberStyle().forcePlainString()) {
        out.append(x.toPlainString());
      } else {
        out.append(x.toString());
      }
    }
  }

  public static class JsonString extends JsonElement {

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

  }

  public static class JsonArray extends JsonElement {

    private final List<JsonElement> entries;

    public JsonArray(JsonElement... entries) {
      this(Collections.unmodifiableList(Arrays.asList(entries)), false);
      verifyEntries();
    }

    public JsonArray(Collection<? extends JsonElement> entries) {
      this(Collections.unmodifiableList(new ArrayList<>(entries)), false);
      verifyEntries();
    }

    /* package-only */ JsonArray(List<JsonElement> list, boolean unsafeFlag) {
      this.entries = list;
    }

    private void verifyEntries() {
      for (int i = 0; i < this.entries.size(); i++) {
        if (this.entries().get(i) == null) {
          throw new NullPointerException("Entries may not be null index:" + i);
        }
      }
    }

    public List<JsonElement> entries() {
      return entries;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      return prime + entries.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof JsonArray)) {
        return false;
      }
      JsonArray other = (JsonArray) obj;
      if (!entries.equals(other.entries)) {
        return false;
      }
      return true;
    }

    @Override
    protected int write(Appendable out, JsonStyle style, int indentLevel) throws IOException {
      JsonArrayStyle arrayStyle = style.arrayStyle();
      int indent = arrayStyle.beforeOpenBracket().write(style, indentLevel, out);
      out.append('[');
      if (entries.isEmpty()) {
        indent = arrayStyle.betweenEmptyBrackets().write(style, indent, out);
      } else {
        indent = arrayStyle.afterOpenBracket().write(style, indent, out);
        indent = entries.get(0).write(out, style, indent);
        for (int i = 1; i < entries.size(); i++) {
          indent = arrayStyle.beforeEntrySeperator().write(style, indent, out);
          out.append(',');
          indent = arrayStyle.afterEntrySeperator().write(style, indent, out);
          indent = entries.get(i).write(out, style, indent);
        }
        indent = arrayStyle.beforeCloseBracket().write(style, indent, out);
      }

      out.append(']');
      indent = arrayStyle.afterCloseBracket().write(style, indent, out);
      return indent;
    }

  }

  public static final class JsonObject extends JsonElement {
    private final List<JsonObjectEntry> entries;
    private transient Map<String, JsonElement> asMap;

    public JsonObject(JsonObjectEntry... entries) {
      this(Collections.unmodifiableList(Arrays.asList(entries)), false);
      verifyEntries();
    }

    public JsonObject(Collection<JsonObjectEntry> entries) {
      this(Collections.unmodifiableList(new ArrayList<>(entries)), false);
      verifyEntries();
    }

    /* package-only */ JsonObject(List<JsonObjectEntry> list, boolean unsafeFlag) {
      this.entries = list;
    }

    private void verifyEntries() {
      for (int i = 0; i < this.entries.size(); i++) {
        if (this.entries().get(i) == null) {
          throw new NullPointerException("Entries may not be null index:" + i);
        }
      }
    }

    public List<JsonObjectEntry> entries() {
      return entries;
    }

    public Map<String, JsonElement> asMap() {
      if (asMap == null) {
        Map<String, JsonElement> map = new LinkedHashMap<>();
        for (JsonObjectEntry e : entries) {
          map.put(e.key().stringValue(), e.value());
        }
        asMap = Collections.unmodifiableMap(map);
      }
      return asMap;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      return prime + entries.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof JsonObject)) {
        return false;
      }
      JsonObject other = (JsonObject) obj;
      if (!entries.equals(other.entries)) {
        return false;
      }
      return true;
    }

    @Override
    protected int write(Appendable out, JsonStyle style, int indentLevel) throws IOException {
      JsonObjectStyle objStyle = style.objectStyle();
      int indent = objStyle.beforeOpenBracket().write(style, indentLevel, out);
      out.append('{');
      if (entries.isEmpty()) {
        indent = objStyle.betweenEmptyBrackets().write(style, indent, out);
      } else {
        indent = objStyle.afterOpenBracket().write(style, indent, out);
        indent = entries.get(0).write(out, style, indent);
        for (int i = 1; i < entries.size(); i++) {
          indent = objStyle.beforeEntrySeperator().write(style, indent, out);
          out.append(',');
          indent = objStyle.afterEntrySeperator().write(style, indent, out);
          indent = entries.get(i).write(out, style, indent);
        }
        indent = objStyle.beforeCloseBracket().write(style, indent, out);
      }

      out.append('}');
      indent = objStyle.afterCloseBracket().write(style, indent, out);
      return indent;
    }

  }

  public static final class JsonObjectEntry extends JsonElement {
    private final JsonString key;
    private final JsonElement value;

    public JsonObjectEntry(JsonString key, JsonElement value) {
      this.key = Objects.requireNonNull(key);
      this.value = Objects.requireNonNull(value);
    }

    public JsonString key() {
      return key;
    }

    public JsonElement value() {
      return value;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + key.hashCode();
      result = prime * result + value.hashCode();
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof JsonObjectEntry)) {
        return false;
      }
      JsonObjectEntry other = (JsonObjectEntry) obj;
      if (!key.equals(other.key)) {
        return false;
      }
      if (!value.equals(other.value)) {
        return false;
      }
      return true;
    }

    @Override
    protected int write(Appendable out, JsonStyle style, int indentLevel) throws IOException {
      JsonObjectStyle objStyle = style.objectStyle();
      int indent = indentLevel;

      indent = key.write(out, style, indent);

      indent = objStyle.beforeKeyValueSeperator().write(style, indent, out);
      out.append(':');
      indent = objStyle.afterKeyValueSeperator().write(style, indent, out);

      indent = value.write(out, style, indent);
      return indent;
    }

  }
}
