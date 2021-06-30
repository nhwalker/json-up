package io.github.nhwalker.jsonup.types;

import java.io.IOException;
import java.util.Objects;
import java.util.function.UnaryOperator;

import io.github.nhwalker.jsonup.format.JsonObjectStyle;
import io.github.nhwalker.jsonup.format.JsonStyle;

public final class JsonObjectEntry extends VirtualJsonElement {

  private final UnaryOperator<JsonStyle> styleSettings;
  private final JsonElement key;
  private final JsonElement value;

  public JsonObjectEntry(JsonElement key, JsonElement value) {
    this.key = key;
    this.value = value;
    this.styleSettings = null;
    verify();
  }

  public JsonObjectEntry(JsonElement key, JsonElement value, JsonStyle styleSettings) {
    this.key = key;
    this.value = value;
    this.styleSettings = ignore -> styleSettings;
    verify();
  }

  public JsonObjectEntry(JsonElement key, JsonElement value, UnaryOperator<JsonStyle> styleSettings) {
    this.key = key;
    this.value = value;
    this.styleSettings = styleSettings;
    verify();
  }

  private void verify() {
    Objects.requireNonNull(key);
    Objects.requireNonNull(value);
    this.key.asString();
  }

  public JsonElement keyElement() {
    return key;
  }

  public JsonElement valueElement() {
    return value;
  }

  public String name() {
    return key.asString().stringValue();
  }

  public JsonElement resolvedValue() {
    return asResolved();
  }

  @Override
  public JsonElement asResolved() {
    return value.asResolved();
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
    JsonStyle localStyle = styleSettings == null ? style : styleSettings.apply(style);

    JsonObjectStyle objStyle = localStyle.objectStyle();
    int indent = indentLevel;

    indent = key.write(out, localStyle, indent);

    indent = objStyle.beforeKeyValueSeperator().write(localStyle, indent, out);
    out.append(':');
    indent = objStyle.afterKeyValueSeperator().write(localStyle, indent, out);

    indent = value.write(out, localStyle, indent);
    return indent;
  }

}