package io.github.nhwalker.jsonup.elements;

import java.io.IOException;

import io.github.nhwalker.jsonup.exceptions.JsonParseException;

public final class JsonObjectEntryParser extends JsonParser<JsonObjectEntry> {
  public static final JsonObjectEntryParser DEFAULT = new JsonObjectEntryParser();

  @Override
  public boolean isNext(JsonParserContext context, PeekingReader reader) throws IOException {
    return false; // If you have to ask, you aren't reading an object entry
  }

  @Override
  public JsonObjectEntry parse(JsonParserContext context, PeekingReader reader) throws IOException, JsonParseException {
    JsonParser<JsonString> keyParser = context.stringParser();
    JsonParser<JsonElement> valueParser = context.elementParser();

    reader.skipWhitespace();
    JsonString key = keyParser.parse(context, reader);
    reader.skipWhitespace();
    reader.readAndAssert(':');
    reader.skipWhitespace();
    JsonElement value = valueParser.parse(context, reader);

    return new JsonObjectEntry(key, value);
  }
}