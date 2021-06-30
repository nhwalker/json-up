package io.github.nhwalker.jsonup.elements;

import java.io.IOException;
import java.util.List;

import io.github.nhwalker.jsonup.exceptions.JsonParseException;

public final class JsonObjectParser extends JsonParser<JsonObject> {
  public static final JsonObjectParser DEFAULT = new JsonObjectParser();

  @Override
  public boolean isNext(JsonParserContext context, PeekingReader reader) throws IOException {
    return reader.peek() == '{';
  }

  @Override
  public JsonObject parse(JsonParserContext context, PeekingReader reader) throws IOException, JsonParseException {
    List<JsonElement> items = ParserUtils.iterableParse(context, reader, '{', '}', context.objectEntryParser());
    return new JsonObject(items, false);
  }
}