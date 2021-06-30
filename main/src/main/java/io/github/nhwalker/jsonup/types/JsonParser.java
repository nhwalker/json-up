package io.github.nhwalker.jsonup.types;

import java.io.IOException;

import io.github.nhwalker.jsonup.exceptions.JsonParseException;

public abstract class JsonParser<T extends JsonElement> {

  public static JsonElement read(PeekingReader reader) throws IOException, JsonParseException {
    return JsonParserContext.DEFAULT.elementParser().parse(JsonParserContext.DEFAULT, reader);
  }

  public static JsonElement read(JsonParserContext context, PeekingReader reader)
      throws IOException, JsonParseException {
    return context.elementParser().parse(context, reader);
  }

  public abstract boolean isNext(JsonParserContext context, PeekingReader reader) throws IOException;

  public abstract T parse(JsonParserContext context, PeekingReader reader) throws IOException, JsonParseException;

}
