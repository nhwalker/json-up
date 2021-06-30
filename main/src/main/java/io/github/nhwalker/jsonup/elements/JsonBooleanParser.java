package io.github.nhwalker.jsonup.elements;

import java.io.IOException;

import io.github.nhwalker.jsonup.exceptions.JsonParseException;

public final class JsonBooleanParser extends JsonParser<JsonBoolean> {
  public static final JsonBooleanParser DEFAULT = new JsonBooleanParser();

  @Override
  public boolean isNext(JsonParserContext context, PeekingReader reader) throws IOException {
    int next = reader.peek();
    return next == 't' || next == 'f';
  }

  @Override
  public JsonBoolean parse(JsonParserContext context, PeekingReader reader) throws IOException, JsonParseException {
    int next = reader.peek();
    if (next == 't') {
      reader.readAndAssert("true");
      return JsonBoolean.TRUE;
    }
    reader.readAndAssert("false");
    return JsonBoolean.FALSE;
  }
}