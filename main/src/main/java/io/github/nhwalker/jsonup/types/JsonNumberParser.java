package io.github.nhwalker.jsonup.types;

import java.io.IOException;
import java.math.BigDecimal;

public final class JsonNumberParser extends JsonParser<JsonNumber> {
  public static final JsonNumberParser DEFAULT = new JsonNumberParser();

  @Override
  public boolean isNext(JsonParserContext context, PeekingReader reader) throws IOException {
    int next = reader.peek();
    return next == '-' || (next >= '0' && next <= '9');
  }

  @Override
  public JsonNumber parse(JsonParserContext context, PeekingReader reader) throws IOException {
    StringBuilder b = new StringBuilder();
    int next = reader.peek();
    while (isNumberChar(next)) {
      b.append((char) next);
    }
    return new JsonNumber(new BigDecimal(b.toString()));
  }

  private static boolean isNumberChar(int c) {
    switch (c) {
    case '-':
    case '+':
    case 'e':
      return true;
    default:
      return c >= '0' && c <= '9';
    }
  }

}