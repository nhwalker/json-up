package io.github.nhwalker.jsonup.format;

import java.util.function.Consumer;

import io.github.nhwalker.jsonup.util.Configure;
import io.github.nhwalker.jsonup.util.RepeatingStringFactory;

public final class JsonStyle {

  public static final JsonStyle COMPACT = new JsonStyle();
  public static final JsonStyle COMPACT_LENIENT = COMPACT.withChanges(JsonStyle::makeLenient);

  public static final JsonStyle SINGLE_LINE = new JsonStyle(x -> {
    x.arrayStyle(JsonArrayStyle.SINGLE_LINE);
    x.objectStyle(JsonObjectStyle.SINGLE_LINE);
  });
  public static final JsonStyle SINGLE_LINE_LENIENT = SINGLE_LINE.withChanges(JsonStyle::makeLenient);

  public static final JsonStyle MULTI_LINE = new JsonStyle(x -> {
    x.arrayStyle(JsonArrayStyle.MULTI_LINE);
    x.objectStyle(JsonObjectStyle.MULTI_LINE);
  });
  public static final JsonStyle MULTI_LINE_LENIENT = MULTI_LINE.withChanges(JsonStyle::makeLenient);

  public static final JsonStyle MULTI_LINE_TALL = new JsonStyle(x -> {
    x.arrayStyle(JsonArrayStyle.MULTI_LINE_TALL);
    x.objectStyle(JsonObjectStyle.MULTI_LINE_TALL);
  });
  public static final JsonStyle MULTI_LINE_TALL_LENIENT = MULTI_LINE_TALL.withChanges(JsonStyle::makeLenient);

  public static void makeLenient(Args args) {
    args.numberStyle(args.numberStyle().withChanges(numArgs -> {
      numArgs.allowNonFiniteStrings(true);
    }));
  }

  public static class Args {
    private JsonNumberStyle numberStyle = JsonNumberStyle.DEFAULT;
    private JsonArrayStyle arrayStyle = JsonArrayStyle.COMPACT;
    private JsonObjectStyle objectStyle = JsonObjectStyle.COMPACT;
    private JsonStringStyle stringStyle = JsonStringStyle.DEFAULT;
    private JsonWhitespace spaceBeforeItem = JsonWhitespace.none();
    private JsonWhitespace spaceAfterItem = JsonWhitespace.none();
    private String indentString = "  ";
    private String newlineString = "\n";

    public Args() {
      // nothing
    }

    public Args(Args args) {
      this.numberStyle = args.numberStyle;
      this.arrayStyle = args.arrayStyle;
      this.objectStyle = args.objectStyle;
      this.stringStyle = args.stringStyle;
      this.spaceBeforeItem = args.spaceBeforeItem;
      this.spaceAfterItem = args.spaceAfterItem;
      this.indentString = args.indentString;
      this.newlineString = args.newlineString;
    }

    public Args(JsonStyle args) {
      this.numberStyle = args.numberStyle;
      this.arrayStyle = args.arrayStyle;
      this.objectStyle = args.objectStyle;
      this.stringStyle = args.stringStyle;
      this.spaceBeforeItem = args.spaceBeforeItem;
      this.spaceAfterItem = args.spaceAfterItem;
      this.indentString = args.indentString;
      this.newlineString = args.newlineString;
    }

    public final JsonNumberStyle numberStyle() {
      return numberStyle;
    }

    public final JsonArrayStyle arrayStyle() {
      return arrayStyle;
    }

    public final JsonObjectStyle objectStyle() {
      return objectStyle;
    }

    public final JsonStringStyle stringStyle() {
      return stringStyle;
    }

    public final JsonWhitespace spaceBeforeItem() {
      return spaceBeforeItem;
    }

    public final JsonWhitespace spaceAfterItem() {
      return spaceAfterItem;
    }

    public final String indentString() {
      return indentString;
    }

    public final String newlineString() {
      return newlineString;
    }

    public final void numberStyle(JsonNumberStyle numberStyle) {
      this.numberStyle = numberStyle;
    }

    public final void arrayStyle(JsonArrayStyle arrayStyle) {
      this.arrayStyle = arrayStyle;
    }

    public final void objectStyle(JsonObjectStyle objectStyle) {
      this.objectStyle = objectStyle;
    }

    public final void stringStyle(JsonStringStyle stringStyle) {
      this.stringStyle = stringStyle;
    }

    public final void spaceBeforeItem(JsonWhitespace spaceBeforeItem) {
      this.spaceBeforeItem = spaceBeforeItem;
    }

    public final void spaceAfterItem(JsonWhitespace spaceAfterItem) {
      this.spaceAfterItem = spaceAfterItem;
    }

    public final void indentString(String indentString) {
      this.indentString = indentString;
    }

    public final void newlineString(String newlineString) {
      this.newlineString = newlineString;
    }
  }

  private final JsonNumberStyle numberStyle;
  private final JsonArrayStyle arrayStyle;
  private final JsonObjectStyle objectStyle;
  private final JsonStringStyle stringStyle;
  private final JsonWhitespace spaceBeforeItem;
  private final JsonWhitespace spaceAfterItem;
  private final String indentString;
  private final String newlineString;

  public JsonStyle() {
    this(new Args());
  }

  public JsonStyle(Args args) {
    this.numberStyle = args.numberStyle;
    this.arrayStyle = args.arrayStyle;
    this.objectStyle = args.objectStyle;
    this.stringStyle = args.stringStyle;
    this.spaceBeforeItem = args.spaceBeforeItem;
    this.spaceAfterItem = args.spaceAfterItem;
    this.indentString = args.indentString;
    this.newlineString = args.newlineString;
  }

  public JsonStyle(Consumer<Args> args) {
    this(Configure.configure(new Args(), args));
  }

  public JsonStyle withChanges(Consumer<Args> args) {
    return new JsonStyle(Configure.configure(new Args(this), args));
  }

  public final JsonNumberStyle numberStyle() {
    return numberStyle;
  }

  public final JsonArrayStyle arrayStyle() {
    return arrayStyle;
  }

  public final JsonObjectStyle objectStyle() {
    return objectStyle;
  }

  public final JsonStringStyle stringStyle() {
    return stringStyle;
  }

  public final JsonWhitespace spaceBeforeItem() {
    return spaceBeforeItem;
  }

  public final JsonWhitespace spaceAfterItem() {
    return spaceAfterItem;
  }

  public final String indentString() {
    return indentString;
  }

  private RepeatingStringFactory indentFactory = null;

  public RepeatingStringFactory indentStringFactory() {
    return indentFactory == null ? (indentFactory = RepeatingStringFactory.of(indentString)) : indentFactory;
  }

  public final String newlineString() {
    return newlineString;
  }

}
