package io.github.nhwalker.jsonup.types;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class JsonWriterContext {
  public static final JsonWriterContext DEFAULT = new JsonWriterContext(new Args());

  public static class Args {
    private JsonWriter<JsonElement> elementWriter = JsonElementWriter.DEFAULT;
    private JsonWriter<JsonNull> nullWriter = JsonNullWriter.DEFAULT;
    private JsonWriter<JsonBoolean> booleanWriter = JsonBooleanWriter.DEFAULT;
    private JsonWriter<JsonNumber> numberWriter = JsonNumberWriter.DEFAULT;
    private JsonWriter<JsonString> stringWriter = JsonStringWriter.DEFAULT;
    private JsonWriter<JsonArray> arrayWriter = JsonArrayWriter.DEFAULT;
    private JsonWriter<JsonObject> objectWriter = JsonObjectWriter.DEFAULT;
    private JsonWriter<JsonObjectEntry> objectEntryWriter = JsonObjectEntryWriter.DEFAULT;

    public Args() {
      // nothing
    }

    public Args(JsonWriterContext args) {
      this.elementWriter = args.elementWriter;
      this.nullWriter = args.nullWriter;
      this.booleanWriter = args.booleanWriter;
      this.numberWriter = args.numberWriter;
      this.stringWriter = args.stringWriter;
      this.arrayWriter = args.arrayWriter;
      this.objectWriter = args.objectWriter;
      this.objectEntryWriter = args.objectEntryWriter;
    }

    public Args(JsonWriterContext.Args args) {
      this.elementWriter = args.elementWriter;
      this.nullWriter = args.nullWriter;
      this.booleanWriter = args.booleanWriter;
      this.numberWriter = args.numberWriter;
      this.stringWriter = args.stringWriter;
      this.arrayWriter = args.arrayWriter;
      this.objectWriter = args.objectWriter;
      this.objectEntryWriter = args.objectEntryWriter;
    }

    public void elementWriter(JsonWriter<JsonElement> elementWriter) {
      this.elementWriter = Objects.requireNonNull(elementWriter);
    }

    public JsonWriter<JsonElement> elementWriter() {
      return this.elementWriter;
    }

    public void nullWriter(JsonWriter<JsonNull> nullWriter) {
      this.nullWriter = Objects.requireNonNull(nullWriter);
    }

    public JsonWriter<JsonNull> nullWriter() {
      return this.nullWriter;
    }

    public void booleanWriter(JsonWriter<JsonBoolean> booleanWriter) {
      this.booleanWriter = Objects.requireNonNull(booleanWriter);
    }

    public JsonWriter<JsonBoolean> booleanWriter() {
      return this.booleanWriter;
    }

    public void numberWriter(JsonWriter<JsonNumber> numberWriter) {
      this.numberWriter = Objects.requireNonNull(numberWriter);
    }

    public JsonWriter<JsonNumber> numberWriter() {
      return this.numberWriter;
    }

    public void stringWriter(JsonWriter<JsonString> stringWriter) {
      this.stringWriter = Objects.requireNonNull(stringWriter);
    }

    public JsonWriter<JsonString> stringWriter() {
      return this.stringWriter;
    }

    public void arrayWriter(JsonWriter<JsonArray> arrayWriter) {
      this.arrayWriter = Objects.requireNonNull(arrayWriter);
    }

    public JsonWriter<JsonArray> arrayWriter() {
      return this.arrayWriter;
    }

    public void objectWriter(JsonWriter<JsonObject> objectWriter) {
      this.objectWriter = Objects.requireNonNull(objectWriter);
    }

    public JsonWriter<JsonObject> objectWriter() {
      return this.objectWriter;
    }

    public void objectEntryWriter(JsonWriter<JsonObjectEntry> objectEntryWriter) {
      this.objectEntryWriter = Objects.requireNonNull(objectEntryWriter);
    }

    public JsonWriter<JsonObjectEntry> objectEntryWriter() {
      return this.objectEntryWriter;
    }
  }

  private final JsonWriter<JsonElement> elementWriter;
  private final JsonWriter<JsonNull> nullWriter;
  private final JsonWriter<JsonBoolean> booleanWriter;
  private final JsonWriter<JsonNumber> numberWriter;
  private final JsonWriter<JsonString> stringWriter;
  private final JsonWriter<JsonArray> arrayWriter;
  private final JsonWriter<JsonObject> objectWriter;
  private final JsonWriter<JsonObjectEntry> objectEntryWriter;

  private final transient List<JsonWriter<?>> valueWriters;

  public JsonWriterContext(JsonWriterContext.Args args) {
    this.elementWriter = args.elementWriter;
    this.nullWriter = args.nullWriter;
    this.booleanWriter = args.booleanWriter;
    this.numberWriter = args.numberWriter;
    this.stringWriter = args.stringWriter;
    this.arrayWriter = args.arrayWriter;
    this.objectWriter = args.objectWriter;
    this.objectEntryWriter = args.objectEntryWriter;
    valueWriters = Collections.unmodifiableList(Arrays.asList(//
        this.nullWriter, //
        this.booleanWriter, //
        this.numberWriter, //
        this.stringWriter, //
        this.arrayWriter, //
        this.objectWriter//
    ));
  }

  public List<JsonWriter<?>> valueWriters() {
    return valueWriters;
  }

  public JsonWriter<JsonElement> elementWriter() {
    return this.elementWriter;
  }

  public JsonWriter<JsonNull> nullWriter() {
    return this.nullWriter;
  }

  public JsonWriter<JsonBoolean> booleanWriter() {
    return this.booleanWriter;
  }

  public JsonWriter<JsonNumber> numberWriter() {
    return this.numberWriter;
  }

  public JsonWriter<JsonString> stringWriter() {
    return this.stringWriter;
  }

  public JsonWriter<JsonArray> arrayWriter() {
    return this.arrayWriter;
  }

  public JsonWriter<JsonObject> objectWriter() {
    return this.objectWriter;
  }

  public JsonWriter<JsonObjectEntry> objectEntryWriter() {
    return this.objectEntryWriter;
  }
  
}