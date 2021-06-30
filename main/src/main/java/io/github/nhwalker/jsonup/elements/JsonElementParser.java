package io.github.nhwalker.jsonup.elements;

import java.io.IOException;

import io.github.nhwalker.jsonup.exceptions.JsonParseException;

public class JsonElementParser extends JsonParser<JsonElement> {
  public static final JsonElementParser DEFAULT = new JsonElementParser();

  @Override
  public boolean isNext(JsonParserContext context, PeekingReader reader) throws IOException {
    return true;
  }

  @Override
  public JsonElement parse(JsonParserContext context, PeekingReader reader) throws JsonParseException, IOException {
    try {
      reader.skipWhitespace();
      for (JsonParser<?> parser : context.valueParsers()) {
        if (parser.isNext(context, reader)) {
          return parser.parse(context, reader);
        }
      }
      throw reader.newException("Unable to parse element");
    } catch (JsonParseException e) {
      throw e;
    } catch (IOException e) {
      throw e;
    } catch (Exception e) {
      throw reader.newException(e);
    }
  }

}