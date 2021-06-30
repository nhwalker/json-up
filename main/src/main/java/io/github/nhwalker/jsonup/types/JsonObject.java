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
import java.util.Set;
import java.util.function.Consumer;

import io.github.nhwalker.jsonup.exceptions.JsonFormatException;
import io.github.nhwalker.jsonup.format.JsonObjectStyle;
import io.github.nhwalker.jsonup.format.JsonStyle;
import io.github.nhwalker.jsonup.internal.Configure;

public final class JsonObject extends JsonElement {

  public static class Args {

    private final ArrayList<JsonObjectEntry> elements;
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

    public List<JsonObjectEntry> entries() {
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

  }

  private final List<JsonObjectEntry> entries;
  private transient Map<String, JsonElement> asMap;

  public JsonObject(Consumer<Args> args) {
    this(Configure.configure(new Args(), args));
  }

  public JsonObject(Args args) {
    this(Collections.unmodifiableList(new ArrayList<>(args.elements)), false);
    verifyEntries(args.allowDuplicates());
  }

  public JsonObject(JsonObjectEntry... entries) {
    this(false, entries);
  }

  public JsonObject(boolean allowDuplicates, JsonObjectEntry... entries) {
    this(Collections.unmodifiableList(Arrays.asList(entries)), false);
    verifyEntries(allowDuplicates);
  }

  public JsonObject(Collection<JsonObjectEntry> entries) {
    this(false, entries);
  }

  public JsonObject(boolean allowDuplicates, Collection<JsonObjectEntry> entries) {
    this(Collections.unmodifiableList(new ArrayList<>(entries)), false);
    verifyEntries(allowDuplicates);
  }

  /* package-only */ JsonObject(List<JsonObjectEntry> list, boolean unsafeFlag) {
    this.entries = list;
  }

  private void verifyEntries(boolean allowDuplicates) {
    if (allowDuplicates) {
      for (int i = 0; i < this.entries.size(); i++) {
        if (this.entries().get(i) == null) {
          throw new NullPointerException("Entries may not be null index:" + i);
        }
      }
    } else {
      Set<String> matches = new HashSet<>();
      for (int i = 0; i < this.entries.size(); i++) {
        JsonObjectEntry e = this.entries.get(i);
        if (e == null) {
          throw new NullPointerException("Entries may not be null index:" + i);
        } else if (!matches.add(e.name())) {
          throw new JsonFormatException("Duplicate Keys In Object Not Allowed index:" + i);
        }
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
        map.put(e.name(), e.resolvedValue());
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
}