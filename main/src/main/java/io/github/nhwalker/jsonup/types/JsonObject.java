package io.github.nhwalker.jsonup.types;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import io.github.nhwalker.jsonup.exceptions.JsonFormatException;
import io.github.nhwalker.jsonup.format.JsonStyle;
import io.github.nhwalker.jsonup.internal.Configure;

public final class JsonObject extends JsonElement {

  public static class Args {

    private final ArrayList<JsonElement> elements;
    private boolean allowDuplicates = false;

    public Args(int capacity) {
      this.elements = new ArrayList<>(capacity);
    }

    public boolean allowDuplicates() {
      return allowDuplicates;
    }

    public void allowDuplicates(boolean allow) {
      allowDuplicates = allow;
    }

    public Args() {
      this.elements = new ArrayList<>();
    }

    public List<JsonElement> entries() {
      return elements;
    }

    public void ensureCapacityFor(int toAdd) {
      this.elements.ensureCapacity(this.elements.size() + toAdd);
    }

    public void add(String name, Number number) {
      add(name, new JsonNumber(number));
    }

    public void add(String name, String string) {
      add(name, new JsonString(string));
    }

    public void add(String name, boolean value) {
      add(name, JsonBoolean.ofValue(value));
    }

    public void addNull(String name) {
      add(name, JsonNull.VALUE);
    }

    public void add(String name, JsonElement element) {
      add(new JsonString(name), element);
    }

    public void add(JsonElement name, JsonElement element) {
      elements.add(new JsonObjectEntry(name, element));
    }

    public void add(JsonObjectEntry entry) {
      elements.add(Objects.requireNonNull(entry));
    }

    public void add(JsonElement element) {
      if (!element.isObjectEntry()) {
        throw new IllegalArgumentException("Can only add Object Entries");
      }
      elements.add(element);
    }
  }

  private final List<JsonElement> entries;
  private transient Map<String, JsonElement> asMap;

  public JsonObject(Consumer<Args> args) {
    this(Configure.configure(new Args(), args));
  }

  public JsonObject(Args args) {
    this.entries = Collections.unmodifiableList(new ArrayList<>(args.elements));
    verifyEntries(args.allowDuplicates());
  }

  public JsonObject(JsonElement... entries) {
    this.entries = Collections.unmodifiableList(Arrays.asList(entries));
    verifyEntries(false);
  }

  public JsonObject(boolean allowDuplicates, JsonElement... entries) {
    this.entries = Collections.unmodifiableList(Arrays.asList(entries));
    verifyEntries(allowDuplicates);
  }

  public JsonObject(Collection<JsonObjectEntry> entries) {
    this(Collections.unmodifiableList(new ArrayList<>(entries)), false);
    verifyEntries(false);
  }

  public JsonObject(boolean allowDuplicates, Collection<JsonObjectEntry> entries) {
    this.entries = Collections.unmodifiableList(new ArrayList<>(entries));
    verifyEntries(allowDuplicates);
  }

  /* package-only */ JsonObject(List<JsonElement> list, boolean unsafeFlag) {
    this.entries = list;
  }

  private static void verifyEntry(int index, JsonElement element) {
    if (element == null) {
      throw new NullPointerException("Entries may not be null index:" + index);
    }
    if (!element.isObjectEntry()) {
      throw new IllegalArgumentException("Entries must be object entries");
    }
  }

  private void verifyEntries(boolean allowDuplicates) {
    if (allowDuplicates) {
      for (int i = 0; i < this.entries.size(); i++) {
        verifyEntry(i, this.entries.get(i));
      }
    } else {
      Set<String> matches = new HashSet<>();
      for (int i = 0; i < this.entries.size(); i++) {
        JsonElement e = this.entries.get(i);
        verifyEntry(i, e);
        if (!matches.add(e.asObjectEntry().name())) {
          throw new JsonFormatException("Duplicate Keys In Object Not Allowed index:" + i);
        }
      }
    }
  }

  public List<JsonElement> entries() {
    return entries;
  }

  public Map<String, JsonElement> asMap() {
    if (asMap == null) {
      Map<String, JsonElement> map = new LinkedHashMap<>();
      for (JsonElement e : entries) {
        JsonObjectEntry entry = e.asObjectEntry();
        map.put(entry.name(), entry.resolvedValue());
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
  public Kind kind() {
    return Kind.OBJECT;
  }

  @Override
  public boolean isObject() {
    return true;
  }

  @Override
  public JsonObject asObject() {
    return this;
  }

  @Override
  public int defaultWrite(JsonWriterContext context, Appendable out, JsonStyle style, int indentLevel)
      throws IOException {
    return JsonObjectWriter.DEFAULT.write(context, this, out, style, indentLevel);
  }
}