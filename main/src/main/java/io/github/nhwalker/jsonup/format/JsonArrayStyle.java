package io.github.nhwalker.jsonup.format;

import java.util.Objects;
import java.util.function.Consumer;

import io.github.nhwalker.jsonup.internal.Configure;

public final class JsonArrayStyle extends AbstractJsonBracketStyle {
  public static final JsonArrayStyle COMPACT = new JsonArrayStyle();
  
  public static final JsonArrayStyle SINGLE_LINE = new JsonArrayStyle(x -> {
    x.betweenEmptyBrackets(JsonWhitespace.space());
    
    x.afterOpenBracket(JsonWhitespace.space());
    x.afterEntrySeperator(JsonWhitespace.space());
    x.beforeCloseBracket(JsonWhitespace.space());
  });
  
  public static final JsonArrayStyle MULTI_LINE = new JsonArrayStyle(x -> {
    x.betweenEmptyBrackets(JsonWhitespace.newline());

    x.afterOpenBracket(JsonWhitespace.newLineAndIndent());
    x.afterEntrySeperator(JsonWhitespace.newline());
    x.beforeCloseBracket(JsonWhitespace.newLineAndUnindent());
  });
  
  public static final JsonArrayStyle MULTI_LINE_TALL = new JsonArrayStyle(x -> {
    x.betweenEmptyBrackets(JsonWhitespace.newline());
    
    x.beforeOpenBracket(JsonWhitespace.newline());
    x.afterOpenBracket(JsonWhitespace.newLineAndIndent());
    
    x.afterEntrySeperator(JsonWhitespace.newline());
    
    x.beforeCloseBracket(JsonWhitespace.newLineAndUnindent());
  });

  public static class Args extends AbstractJsonBracketStyle.Args {

    private JsonWhitespace beforeEntrySeperator = JsonWhitespace.none();
    private JsonWhitespace afterEntrySeperator = JsonWhitespace.none();

    public Args() {
      super();
    }

    public Args(Args args) {
      super(args);
      beforeEntrySeperator = args.beforeEntrySeperator;
      afterEntrySeperator = args.afterEntrySeperator;
    }

    public Args(JsonArrayStyle args) {
      super(args);
      beforeEntrySeperator = args.beforeEntrySeperator;
      afterEntrySeperator = args.afterEntrySeperator;
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
  }

  private final JsonWhitespace beforeEntrySeperator;
  private final JsonWhitespace afterEntrySeperator;

  public JsonArrayStyle() {
    this(new Args());
  }

  public JsonArrayStyle(Args args) {
    super(args);
    beforeEntrySeperator = args.beforeEntrySeperator;
    afterEntrySeperator = args.afterEntrySeperator;
  }

  public JsonArrayStyle(Consumer<Args> args) {
    this(Configure.configure(new Args(), args));
  }

  public JsonArrayStyle withChanges(Consumer<Args> args) {
    return new JsonArrayStyle(Configure.configure(new Args(this), args));
  }

  public JsonWhitespace afterEntrySeperator() {
    return afterEntrySeperator;
  }

  public JsonWhitespace beforeEntrySeperator() {
    return beforeEntrySeperator;
  }
}
