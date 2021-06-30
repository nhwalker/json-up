package io.github.nhwalker.jsonup.elements;

import java.io.IOException;
import java.util.List;

import io.github.nhwalker.jsonup.exceptions.JsonParseException;

public final class JsonArrayParser extends JsonParser<JsonArray> {
  public static final JsonArrayParser DEFAULT = new JsonArrayParser();

  @Override
  public boolean isNext(JsonParserContext context, PeekingReader reader) throws IOException {
    return reader.peek() == '[';
  }

  @Override
  public JsonArray parse(JsonParserContext context, PeekingReader reader) throws IOException, JsonParseException {
    List<JsonElement> items = ParserUtils.iterableParse(context, reader, '{', '}', context.elementParser());
    return new JsonArray(items, false);
  }
}