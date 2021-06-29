package io.github.nhwalker.jsonup.format;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public abstract class JsonWhitespace {

  public static JsonWhitespace none() {
    return None.INSTANCE;
  }

  public static JsonWhitespace newline() {
    return NewLine.INSTANCE;
  }

  public static JsonWhitespace newLineAndIndent() {
    return NewLineWithIndent.INDENT;
  }

  public static JsonWhitespace newLineAndUnindent() {
    return NewLineWithIndent.UNINDENT;
  }

  public static JsonWhitespace space() {
    return Literal.SPACE;
  }

  public static JsonWhitespace tab() {
    return Literal.TAB;
  }

  public static JsonWhitespace spaces(int size) {
    if (size <= 0) {
      return none();
    } else if (size == 1) {
      return space();
    }
    return new Literal(RepeatingStringFactory.spaceFactory().create(size));
  }

  public static JsonWhitespace tabs(int size) {
    if (size <= 0) {
      return none();
    } else if (size == 1) {
      return tab();
    }
    return new Literal(RepeatingStringFactory.tabFactory().create(size));
  }

  public static JsonWhitespace literal(String whitespace) {
    return new Literal(whitespace);
  }

  public static JsonWhitespace newLineAndClearIndent() {
    return NewLineWithFixedIndent.ZERO;
  }

  public static JsonWhitespace newLineWithFixedIndent(int size) {
    if (size <= 0) {
      return newLineAndClearIndent();
    }
    return new NewLineWithFixedIndent(size);
  }

  public abstract int write(JsonStyle style, int indentLevel, Appendable out) throws IOException;

  public JsonWhitespace append(JsonWhitespace next) {
    if (next instanceof None) {
      return this;
    }
    return new Concatted(this, next);
  }

  private static class None extends JsonWhitespace {
    static final None INSTANCE = new None();

    @Override
    public int write(JsonStyle style, int indentLevel, Appendable out) throws IOException {
      return indentLevel;
    }

    @Override
    public JsonWhitespace append(JsonWhitespace next) {
      return next;
    }
  }

  private static class NewLine extends JsonWhitespace {
    static final NewLine INSTANCE = new NewLine();

    @Override
    public int write(JsonStyle style, int indentLevel, Appendable out) throws IOException {
      out.append(style.newlineString());
      return indentLevel;
    }
  }

  private static class NewLineWithIndent extends JsonWhitespace {
    static final NewLineWithIndent INDENT = new NewLineWithIndent(1);
    static final NewLineWithIndent UNINDENT = new NewLineWithIndent(-1);

    private final int indentSize;

    public NewLineWithIndent(int indent) {
      this.indentSize = indent;
    }

    @Override
    public int write(JsonStyle style, int indentLevel, Appendable out) throws IOException {
      int newIndentLevel = indentLevel + indentSize;
      out.append(style.newlineString());
      style.indentStringFactory().append(out, newIndentLevel);
      return newIndentLevel;
    }
  }

  private static class Literal extends JsonWhitespace {
    static final Literal SPACE = new Literal(" ");
    static final Literal TAB = new Literal("\t");

    private final String literal;

    public Literal(String literal) {
      this.literal = Objects.requireNonNull(literal);
    }

    @Override
    public int write(JsonStyle style, int indentLevel, Appendable out) throws IOException {
      out.append(literal);
      return indentLevel;
    }
  }

  private static class NewLineWithFixedIndent extends JsonWhitespace {
    static final NewLineWithFixedIndent ZERO = new NewLineWithFixedIndent(0);

    private final int indentSize;

    public NewLineWithFixedIndent(int indent) {
      this.indentSize = Math.max(0, indent);
    }

    @Override
    public int write(JsonStyle style, int indentLevel, Appendable out) throws IOException {
      out.append(style.newlineString());
      style.indentStringFactory().append(out, indentSize);
      return indentSize;
    }

  }

  private static class Concatted extends JsonWhitespace {
    private JsonWhitespace[] whitespace;

    private Concatted(JsonWhitespace a, JsonWhitespace b) {
      this.whitespace = new JsonWhitespace[2];
      this.whitespace[0] = a;
      this.whitespace[1] = b;
    }

    private Concatted(JsonWhitespace[] whitespace, JsonWhitespace next) {
      this.whitespace = Arrays.copyOf(whitespace, whitespace.length + 1);
      this.whitespace[this.whitespace.length - 1] = next;
    }

    @Override
    public int write(JsonStyle style, int indentLevel, Appendable out) throws IOException {
      int newIndentLevel = indentLevel;
      for (JsonWhitespace x : this.whitespace) {
        newIndentLevel = x.write(style, newIndentLevel, out);
      }
      return newIndentLevel;
    }

    @Override
    public JsonWhitespace append(JsonWhitespace next) {
      if (next instanceof None) {
        return this;
      }
      return new Concatted(this.whitespace, next);
    }
  }
}
