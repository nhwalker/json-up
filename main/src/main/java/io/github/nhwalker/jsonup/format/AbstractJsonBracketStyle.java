package io.github.nhwalker.jsonup.format;

import java.util.Objects;

public class AbstractJsonBracketStyle {

  public static class Args {
    private JsonWhitespace betweenEmptyBrackets = JsonWhitespace.none();
    private JsonWhitespace beforeOpenBracket = JsonWhitespace.none();
    private JsonWhitespace afterOpenBracket = JsonWhitespace.none();
    private JsonWhitespace beforeCloseBracket = JsonWhitespace.none();
    private JsonWhitespace afterCloseBracket = JsonWhitespace.none();
    private boolean skipIfEmpty = false;

    public Args() {
      // nothing
    }
    public Args(Args args) {
      betweenEmptyBrackets = args.betweenEmptyBrackets;
      beforeOpenBracket = args.beforeOpenBracket;
      afterOpenBracket = args.afterOpenBracket;
      beforeCloseBracket = args.beforeCloseBracket;
      afterCloseBracket = args.afterCloseBracket;
      skipIfEmpty = args.skipIfEmpty;
    }
    
    public Args(AbstractJsonBracketStyle args) {
      betweenEmptyBrackets = args.betweenEmptyBrackets;
      beforeOpenBracket = args.beforeOpenBracket;
      afterOpenBracket = args.afterOpenBracket;
      beforeCloseBracket = args.beforeCloseBracket;
      afterCloseBracket = args.afterCloseBracket;
      skipIfEmpty = args.skipIfEmpty;
    }

    public final JsonWhitespace betweenEmptyBrackets() {
      return betweenEmptyBrackets;
    }

    public final void betweenEmptyBrackets(JsonWhitespace betweenEmptyBrackets) {
      this.betweenEmptyBrackets = Objects.requireNonNull(betweenEmptyBrackets);
    }

    public final JsonWhitespace beforeOpenBracket() {
      return beforeOpenBracket;
    }

    public final void beforeOpenBracket(JsonWhitespace beforeOpenBracket) {
      this.beforeOpenBracket = Objects.requireNonNull(beforeOpenBracket);
    }

    public final JsonWhitespace afterOpenBracket() {
      return afterOpenBracket;
    }

    public final void afterOpenBracket(JsonWhitespace afterOpenBracket) {
      this.afterOpenBracket = Objects.requireNonNull(afterOpenBracket);
    }

    public final JsonWhitespace beforeCloseBracket() {
      return beforeCloseBracket;
    }

    public final void beforeCloseBracket(JsonWhitespace beforeCloseBracket) {
      this.beforeCloseBracket = Objects.requireNonNull(beforeCloseBracket);
    }

    public final JsonWhitespace afterCloseBracket() {
      return afterCloseBracket;
    }

    public final void afterCloseBracket(JsonWhitespace afterCloseBracket) {
      this.afterCloseBracket = Objects.requireNonNull(afterCloseBracket);
    }

    public final boolean skipIfEmpty() {
      return skipIfEmpty;
    }

    public final void skipIfEmpty(boolean skipIfEmpty) {
      this.skipIfEmpty = skipIfEmpty;
    }

  }

  private final JsonWhitespace betweenEmptyBrackets;
  private final JsonWhitespace beforeOpenBracket;
  private final JsonWhitespace afterOpenBracket;
  private final JsonWhitespace beforeCloseBracket;
  private final JsonWhitespace afterCloseBracket;
  private final boolean skipIfEmpty;

  public AbstractJsonBracketStyle(Args args) {
    betweenEmptyBrackets = args.betweenEmptyBrackets;
    beforeOpenBracket = args.beforeOpenBracket;
    afterOpenBracket = args.afterOpenBracket;
    beforeCloseBracket = args.beforeCloseBracket;
    afterCloseBracket = args.afterCloseBracket;
    skipIfEmpty = args.skipIfEmpty;
  }

  public final JsonWhitespace betweenEmptyBrackets() {
    return betweenEmptyBrackets;
  }

  public final JsonWhitespace beforeOpenBracket() {
    return beforeOpenBracket;
  }

  public final JsonWhitespace afterOpenBracket() {
    return afterOpenBracket;
  }

  public final JsonWhitespace beforeCloseBracket() {
    return beforeCloseBracket;
  }

  public final JsonWhitespace afterCloseBracket() {
    return afterCloseBracket;
  }

  public final boolean skipIfEmpty() {
    return skipIfEmpty;
  }

}
