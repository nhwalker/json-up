package io.github.nhwalker.jsonup.elements;

import java.io.IOException;

import io.github.nhwalker.jsonup.exceptions.JsonParseException;

public final class JsonNullParser extends JsonParser<JsonNull> {
  public static final JsonNullParser DEFAULT = new JsonNullParser();

  @Override
  public boolean isNext(JsonParserContext context, PeekingReader reader) throws IOException {
    return reader.peek() == 'n';
  }

  @Override
  public JsonNull parse(JsonParserContext context, PeekingReader reader) throws IOException, JsonParseException {
    reader.readAndAssert("null");
    return JsonNull.VALUE;
  }
}