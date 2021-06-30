package io.github.nhwalker.jsonup.elements;

import java.io.IOException;

import io.github.nhwalker.jsonup.exceptions.JsonParseException;

public final class JsonStringParser extends JsonParser<JsonString> {
  public static final JsonStringParser DEFAULT = new JsonStringParser();

  @Override
  public boolean isNext(JsonParserContext context, PeekingReader reader) throws IOException {
    return reader.peek() == '"';
  }

  @Override
  public JsonString parse(JsonParserContext context, PeekingReader reader) throws IOException, JsonParseException {
    reader.readAndAssert('"');

    StringBuilder string = new StringBuilder();
    int next = reader.peek();
    while (next != '"') {
      if (next == '\\') {
        reader.read();
        next = reader.peek();
        char escaped;
        switch (next) {
        case '"':
        case '\\':
        case '/':
        case 'b':
        case 'f':
        case 'n':
        case 'r':
        case 't':
          escaped = (char) next;
          break;
        case 'u':
          escaped = readEscapeCharacter(reader, reader.read(4));
          break;
        default:
          throw reader.newException("Unrecognized escape character " + next);
        }
        string.append(escaped);
      } else {
        string.append((char) reader.read());
      }
      next = reader.peek();
    }

    reader.readAndAssert('"');

    return new JsonString(string.toString());
  }

  private char readEscapeCharacter(PeekingReader reader, char[] buffer) throws IOException, JsonParseException {
    char result = 0;
    for (int i = 0; i < 4; i++) {
      char c = buffer[i];
      result <<= 4;
      if (c >= '0' && c <= '9') {
        result += (c - '0');
      } else if (c >= 'a' && c <= 'f') {
        result += (c - 'a' + 10);
      } else if (c >= 'A' && c <= 'F') {
        result += (c - 'A' + 10);
      } else {
        throw reader.newException("Invalid Escape Hex \\u" + String.valueOf(buffer));
      }
    }
    return result;
  }
}