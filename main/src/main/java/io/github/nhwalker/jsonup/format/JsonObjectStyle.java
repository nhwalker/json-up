package io.github.nhwalker.jsonup.format;

import java.util.Objects;
import java.util.function.Consumer;

import io.github.nhwalker.jsonup.util.Configure;

public final class JsonObjectStyle extends AbstractJsonBracketStyle {

  public static final JsonObjectStyle COMPACT = new JsonObjectStyle();

  public static final JsonObjectStyle SINGLE_LINE = new JsonObjectStyle(x -> {
    x.betweenEmptyBrackets(JsonWhitespace.space());

    x.afterOpenBracket(JsonWhitespace.space());
    
    x.afterKeyValueSeperator(JsonWhitespace.space());
    x.afterEntrySeperator(JsonWhitespace.space());
    
    x.beforeCloseBracket(JsonWhitespace.space());
  });

  public static final JsonObjectStyle MULTI_LINE = new JsonObjectStyle(x -> {
    x.betweenEmptyBrackets(JsonWhitespace.newline());

    x.afterOpenBracket(JsonWhitespace.newLineAndIndent());
    
    x.afterKeyValueSeperator(JsonWhitespace.space());
    x.afterEntrySeperator(JsonWhitespace.newline());
    
    x.beforeCloseBracket(JsonWhitespace.newLineAndUnindent());
  });

  public static final JsonObjectStyle MULTI_LINE_TALL = new JsonObjectStyle(x -> {
    x.betweenEmptyBrackets(JsonWhitespace.newline());

    x.beforeOpenBracket(JsonWhitespace.newline());
    x.afterOpenBracket(JsonWhitespace.newLineAndIndent());
    
    x.afterKeyValueSeperator(JsonWhitespace.space());
    x.afterEntrySeperator(JsonWhitespace.newline());

    x.beforeCloseBracket(JsonWhitespace.newLineAndUnindent());
  });

  public static class Args extends AbstractJsonBracketStyle.Args {

    private JsonWhitespace beforeEntrySeperator = JsonWhitespace.none();
    private JsonWhitespace afterEntrySeperator = JsonWhitespace.none();

    private JsonWhitespace beforeKeyValueSeperator = JsonWhitespace.none();
    private JsonWhitespace afterKeyValueSeperator = JsonWhitespace.none();

    public Args() {
      super();
    }

    public Args(Args args) {
      super(args);
      beforeEntrySeperator = args.beforeEntrySeperator;
      afterEntrySeperator = args.afterEntrySeperator;

      beforeKeyValueSeperator = args.beforeKeyValueSeperator;
      afterKeyValueSeperator = args.afterKeyValueSeperator;
    }

    public Args(JsonObjectStyle args) {
      super(args);
      beforeEntrySeperator = args.beforeEntrySeperator;
      afterEntrySeperator = args.afterEntrySeperator;

      beforeKeyValueSeperator = args.beforeKeyValueSeperator;
      afterKeyValueSeperator = args.afterKeyValueSeperator;
    }

    public JsonWhitespace afterEntrySeperator() {
      return afterEntrySeperator;
    }

    public void afterEntrySeperator(JsonWhitespace afterEntrySeperator) {
      this.afterEntrySeperator = Objects.requireNonNull(afterEntrySeperator);
    }

    public JsonWhitespace beforeEntrySeperator() {
      return beforeEntrySeperator;
    }

    public void beforeEntrySeperator(JsonWhitespace beforeEntrySeperator) {
      this.beforeEntrySeperator = Objects.requireNonNull(beforeEntrySeperator);
    }

    public JsonWhitespace beforeKeyValueSeperator() {
      return beforeKeyValueSeperator;
    }

    public void beforeKeyValueSeperator(JsonWhitespace beforeKeyValueSeperator) {
      this.beforeKeyValueSeperator = Objects.requireNonNull(beforeKeyValueSeperator);
    }

    public JsonWhitespace afterKeyValueSeperator() {
      return afterKeyValueSeperator;
    }

    public void afterKeyValueSeperator(JsonWhitespace afterKeyValueSeperator) {
      this.afterKeyValueSeperator = Objects.requireNonNull(afterKeyValueSeperator);
    }
  }

  private final JsonWhitespace beforeEntrySeperator;
  private final JsonWhitespace afterEntrySeperator;
  private final JsonWhitespace beforeKeyValueSeperator;
  private final JsonWhitespace afterKeyValueSeperator;

  public JsonObjectStyle() {
    this(new Args());
  }

  public JsonObjectStyle(Args args) {
    super(args);
    beforeEntrySeperator = args.beforeEntrySeperator;
    afterEntrySeperator = args.afterEntrySeperator;
    beforeKeyValueSeperator = args.beforeKeyValueSeperator;
    afterKeyValueSeperator = args.afterKeyValueSeperator;
  }

  public JsonObjectStyle(Consumer<Args> args) {
    this(Configure.configure(new Args(), args));
  }

  public JsonObjectStyle withChanges(Consumer<Args> args) {
    return new JsonObjectStyle(Configure.configure(new Args(this), args));
  }

  public JsonWhitespace afterEntrySeperator() {
    return afterEntrySeperator;
  }

  public JsonWhitespace beforeEntrySeperator() {
    return beforeEntrySeperator;
  }

  public JsonWhitespace beforeKeyValueSeperator() {
    return beforeKeyValueSeperator;
  }

  public JsonWhitespace afterKeyValueSeperator() {
    return afterKeyValueSeperator;
  }
}
