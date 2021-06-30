package io.github.nhwalker.jsonup.types;

import java.io.IOException;
import java.io.UncheckedIOException;

import io.github.nhwalker.jsonup.format.JsonStyle;

public abstract class JsonWriter<T extends JsonElement> {

  public final String toString(T input) {
    return toString(input, JsonStyle.SINGLE_LINE_LENIENT);
  }

  public final String toString(T input, JsonStyle style) {
    return toString(input, style, JsonWriterContext.DEFAULT);
  }

  public final String toString(T input, JsonStyle style, JsonWriterContext context) {
    try {
      StringBuilder b = new StringBuilder();
      write(context, input, b, style);
      return b.toString();
    } catch (IOException e) {
      throw new UncheckedIOException(e);// Should not happen with a StringBuilder
    }
  }

  public final void write(T input, Appendable out) throws IOException {
    write(JsonWriterContext.DEFAULT, input, out, JsonStyle.SINGLE_LINE_LENIENT);
  }

  public final void write(JsonWriterContext context, T input, Appendable out) throws IOException {
    write(context, input, out, JsonStyle.SINGLE_LINE_LENIENT);
  }

  public final void write(T input, Appendable out, JsonStyle style) throws IOException {
    write(JsonWriterContext.DEFAULT, input, out, style);
  }

  public final void write(JsonWriterContext context, T input, Appendable out, JsonStyle style) throws IOException {
    write(context, input, out, style, 0);
  }

  public abstract int write(JsonWriterContext context, T input, Appendable out, JsonStyle style, int indentLevel)
      throws IOException;
}
