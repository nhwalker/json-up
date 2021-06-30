package io.github.nhwalker.jsonup.format;

import java.util.Objects;

import io.github.nhwalker.jsonup.elements.JsonElement;

public class JsonStyleWrapper {

  private final JsonElement element;
  private final JsonStyle style;

  public JsonStyleWrapper(JsonElement element, JsonStyle style) {
    this.element = Objects.requireNonNull(element);
    this.style = Objects.requireNonNull(style);
  }

  public JsonElement element() {
    return element;
  }

  public JsonStyle style() {
    return style;
  }
}
