package io.github.nhwalker.jsonup.types;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.nhwalker.jsonup.exceptions.JsonParseException;

public class ParserUtils {
  public static <T extends JsonElement> List<T> iterableParse(JsonParserContext context, PeekingReader reader,
      char openBracket, char closeBracket, JsonParser<? extends T> itemParser) throws IOException, JsonParseException {

    ArrayList<T> values = new ArrayList<>();

    // Consume array open
    reader.readAndAssert(openBracket);

    // Check if any values
    int next = reader.skipWhitespaceAndPeek();
    boolean cont = next != closeBracket;

    while (cont) {
      // Parse value
      values.add(itemParser.parse(context, reader));

      // See what's next
      next = reader.skipWhitespaceAndPeek();
      if (next == ',') {
        reader.read();
      } else if (next == closeBracket || next == PeekingReader.EOF) {
        cont = false;
      }
    }

    // Consume array close
    reader.readAndAssert(closeBracket);

    values.trimToSize();
    return Collections.unmodifiableList(values);

  }
}
