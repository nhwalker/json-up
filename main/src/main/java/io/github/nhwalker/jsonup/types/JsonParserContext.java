package io.github.nhwalker.jsonup.types;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class JsonParserContext {
  public static final JsonParserContext DEFAULT = new JsonParserContext(new Args());

  public static class Args {
    private JsonParser<JsonElement> elementParser = JsonElementParser.DEFAULT;
    private JsonParser<JsonNull> nullParser = JsonNullParser.DEFAULT;
    private JsonParser<JsonBoolean> booleanParser = JsonBooleanParser.DEFAULT;
    private JsonParser<JsonNumber> numberParser = JsonNumberParser.DEFAULT;
    private JsonParser<JsonString> stringParser = JsonStringParser.DEFAULT;
    private JsonParser<JsonArray> arrayParser = JsonArrayParser.DEFAULT;
    private JsonParser<JsonObject> objectParser = JsonObjectParser.DEFAULT;
    private JsonParser<JsonObjectEntry> objectEntryParser = JsonObjectEntryParser.DEFAULT;

    public Args() {
      // nothing
    }

    public Args(JsonParserContext args) {
      this.elementParser = args.elementParser;
      this.nullParser = args.nullParser;
      this.booleanParser = args.booleanParser;
      this.numberParser = args.numberParser;
      this.stringParser = args.stringParser;
      this.arrayParser = args.arrayParser;
      this.objectParser = args.objectParser;
      this.objectEntryParser = args.objectEntryParser;
    }

    public Args(JsonParserContext.Args args) {
      this.elementParser = args.elementParser;
      this.nullParser = args.nullParser;
      this.booleanParser = args.booleanParser;
      this.numberParser = args.numberParser;
      this.stringParser = args.stringParser;
      this.arrayParser = args.arrayParser;
      this.objectParser = args.objectParser;
      this.objectEntryParser = args.objectEntryParser;
    }

    public void elementParser(JsonParser<JsonElement> elementParser) {
      this.elementParser = Objects.requireNonNull(elementParser);
    }

    public JsonParser<JsonElement> elementParser() {
      return this.elementParser;
    }

    public void nullParser(JsonParser<JsonNull> nullParser) {
      this.nullParser = Objects.requireNonNull(nullParser);
    }

    public JsonParser<JsonNull> nullParser() {
      return this.nullParser;
    }

    public void booleanParser(JsonParser<JsonBoolean> booleanParser) {
      this.booleanParser = Objects.requireNonNull(booleanParser);
    }

    public JsonParser<JsonBoolean> booleanParser() {
      return this.booleanParser;
    }

    public void numberParser(JsonParser<JsonNumber> numberParser) {
      this.numberParser = Objects.requireNonNull(numberParser);
    }

    public JsonParser<JsonNumber> numberParser() {
      return this.numberParser;
    }

    public void stringParser(JsonParser<JsonString> stringParser) {
      this.stringParser = Objects.requireNonNull(stringParser);
    }

    public JsonParser<JsonString> stringParser() {
      return this.stringParser;
    }

    public void arrayParser(JsonParser<JsonArray> arrayParser) {
      this.arrayParser = Objects.requireNonNull(arrayParser);
    }

    public JsonParser<JsonArray> arrayParser() {
      return this.arrayParser;
    }

    public void objectParser(JsonParser<JsonObject> objectParser) {
      this.objectParser = Objects.requireNonNull(objectParser);
    }

    public JsonParser<JsonObject> objectParser() {
      return this.objectParser;
    }

    public void objectEntryParser(JsonParser<JsonObjectEntry> objectEntryParser) {
      this.objectEntryParser = Objects.requireNonNull(objectEntryParser);
    }

    public JsonParser<JsonObjectEntry> objectEntryParser() {
      return this.objectEntryParser;
    }
  }

  private final JsonParser<JsonElement> elementParser;
  private final JsonParser<JsonNull> nullParser;
  private final JsonParser<JsonBoolean> booleanParser;
  private final JsonParser<JsonNumber> numberParser;
  private final JsonParser<JsonString> stringParser;
  private final JsonParser<JsonArray> arrayParser;
  private final JsonParser<JsonObject> objectParser;
  private final JsonParser<JsonObjectEntry> objectEntryParser;

  private final transient List<JsonParser<?>> valueParsers;

  public JsonParserContext(JsonParserContext.Args args) {
    this.elementParser = args.elementParser;
    this.nullParser = args.nullParser;
    this.booleanParser = args.booleanParser;
    this.numberParser = args.numberParser;
    this.stringParser = args.stringParser;
    this.arrayParser = args.arrayParser;
    this.objectParser = args.objectParser;
    this.objectEntryParser = args.objectEntryParser;
    valueParsers = Collections.unmodifiableList(Arrays.asList(//
        this.nullParser, //
        this.booleanParser, //
        this.numberParser, //
        this.stringParser, //
        this.arrayParser, //
        this.objectParser//
    ));
  }

  public List<JsonParser<?>> valueParsers() {
    return valueParsers;
  }

  public JsonParser<JsonElement> elementParser() {
    return this.elementParser;
  }

  public JsonParser<JsonNull> nullParser() {
    return this.nullParser;
  }

  public JsonParser<JsonBoolean> booleanParser() {
    return this.booleanParser;
  }

  public JsonParser<JsonNumber> numberParser() {
    return this.numberParser;
  }

  public JsonParser<JsonString> stringParser() {
    return this.stringParser;
  }

  public JsonParser<JsonArray> arrayParser() {
    return this.arrayParser;
  }

  public JsonParser<JsonObject> objectParser() {
    return this.objectParser;
  }

  public JsonParser<JsonObjectEntry> objectEntryParser() {
    return this.objectEntryParser;
  }
}