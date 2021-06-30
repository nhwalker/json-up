package io.github.nhwalker.jsonup.types;

import java.io.IOException;
import java.util.Objects;

import io.github.nhwalker.jsonup.format.JsonStyle;

public final class JsonObjectEntry extends JsonElement {

  private final JsonElement key;
  private final JsonElement value;


  public JsonObjectEntry(JsonElement key, JsonElement value) {
    this.key = key;
    this.value = value;
    verify();
  }

  @Override
  public Kind kind() {
    return Kind.OBJECT_ENTRY;
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
  public int defaultWrite(JsonWriterContext context, Appendable out, JsonStyle style, int indentLevel)
      throws IOException {
    return JsonObjectEntryWriter.DEFAULT.write(context, this, out, style, indentLevel);
  }

}