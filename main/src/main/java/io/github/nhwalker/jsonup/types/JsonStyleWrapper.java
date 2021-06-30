package io.github.nhwalker.jsonup.types;

import java.io.IOException;
import java.util.Objects;
import java.util.function.UnaryOperator;

import io.github.nhwalker.jsonup.format.JsonStyle;

public class JsonStyleWrapper extends VirtualJsonElement {
  private final JsonElement element;
  private final UnaryOperator<JsonStyle> style;

  public JsonStyleWrapper(JsonElement element, JsonStyle style) {
    this.element = Objects.requireNonNull(element);
    Objects.requireNonNull(style);
    this.style = ignore -> style;
  }

  public JsonStyleWrapper(JsonElement element, UnaryOperator<JsonStyle> style) {
    this.element = Objects.requireNonNull(element);
    this.style = Objects.requireNonNull(style);
  }

  @Override
  public JsonElement asResolved() {
    return element.asResolved();
  }

  public UnaryOperator<JsonStyle> getStyle() {
    return style;
  }

  @Override
  protected int write(Appendable out, JsonStyle style, int indentLevel) throws IOException {
    return element.write(out, this.style.apply(style), indentLevel);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + element.hashCode();
    result = prime * result + style.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof JsonStyleWrapper)) {
      return false;
    }
    JsonStyleWrapper other = (JsonStyleWrapper) obj;
    if (!element.equals(other.element)) {
      return false;
    }
    if (!style.equals(other.style)) {
      return false;
    }
    return true;
  }
}