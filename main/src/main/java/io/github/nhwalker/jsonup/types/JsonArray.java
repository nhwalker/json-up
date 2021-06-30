package io.github.nhwalker.jsonup.types;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import io.github.nhwalker.jsonup.format.JsonStyle;
import io.github.nhwalker.jsonup.internal.Configure;

public class JsonArray extends JsonElement {

  public static class Args {
    private ArrayList<JsonElement> elements;

    public Args(int capacity) {
      this.elements = new ArrayList<>(capacity);
    }

    public Args() {
      this.elements = new ArrayList<>();
    }

    public List<JsonElement> elements() {
      return elements;
    }

    public void ensureCapacityFor(int toAdd) {
      this.elements.ensureCapacity(this.elements.size() + toAdd);
    }

    public void add(Number number) {
      elements.add(new JsonNumber(number));
    }

    public void add(String string) {
      elements.add(new JsonString(string));
    }

    public void add(boolean value) {
      elements.add(JsonBoolean.ofValue(value));
    }

    public void addNull() {
      elements.add(JsonNull.VALUE);
    }

    public void add(JsonElement element) {
      verifyEntry(element, elements.size());
      this.elements.add(element);
    }

    public void addAll(JsonElement... elements) {
      addAll(Arrays.asList(elements));
    }

    public void addAll(Collection<? extends JsonElement> items) {
      int verifyIndex = this.elements.size();
      this.elements.addAll(items);
      if (this.elements.size() != verifyIndex) {
        verifyEntries(this.elements.subList(verifyIndex, this.elements.size()));
      }
    }

  }

  private final List<JsonElement> entries;
  private transient List<JsonElement> resolved;

  public JsonArray(JsonElement... entries) {
    this.entries = Collections.unmodifiableList(Arrays.asList(entries));
    verifyEntries(this.entries);
  }

  public JsonArray(Collection<? extends JsonElement> entries) {
    this.entries = Collections.unmodifiableList(new ArrayList<>(entries));
    verifyEntries(this.entries);
  }

  public JsonArray(Args args) {
    this.entries = Collections.unmodifiableList(new ArrayList<>(args.elements));
    // already verified
  }

  public JsonArray(Consumer<Args> args) {
    this(Configure.configure(new Args(), args));
  }

  /* package-only */ JsonArray(List<JsonElement> list, boolean unsafeFlag) {
    this.entries = list;
  }

  private static void verifyEntries(List<JsonElement> list) {
    for (int i = 0; i < list.size(); i++) {
      verifyEntry(list.get(i), i);
    }
  }

  private static void verifyEntry(JsonElement element, int index) {
    if (element == null) {
      throw new NullPointerException("Null value. index:" + index);
    } else if (element.isObjectEntry()) {
      throw new IllegalArgumentException("Cannot add Object Entries to an array. index:" + index);
    }
  }

  public List<JsonElement> resolvedEntries() {
    if (this.resolved == null) {
      List<JsonElement> list = null;
      boolean changed = false;
      for (int i = 0; i < entries.size(); i++) {
        JsonElement orig = entries.get(i);
        JsonElement resolved = orig.asResolved();
        if (changed) {
          list.add(resolved);
        } else {
          changed = resolved != orig;
          if (changed) {
            list = new ArrayList<>(entries.size());
            list.addAll(entries.subList(0, i));
            list.add(resolved);
          }
        }
      }
      if (list == null) {
        this.resolved = Collections.unmodifiableList(list);
      } else {
        this.resolved = this.entries;
      }
    }
    return this.resolved;
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
  public Kind kind() {
    return Kind.ARRAY;
  }

  @Override
  public JsonArray asArray() {
    return this;
  }

  @Override
  public boolean isArray() {
    return true;
  }

  @Override
  public int defaultWrite(JsonWriterContext context, Appendable out, JsonStyle style, int indentLevel)
      throws IOException {
    return JsonArrayWriter.DEFAULT.write(context, this, out, style, indentLevel);
  }
}