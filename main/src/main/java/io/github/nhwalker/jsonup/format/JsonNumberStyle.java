package io.github.nhwalker.jsonup.format;

import java.util.function.Consumer;

import io.github.nhwalker.jsonup.util.Configure;

public final class JsonNumberStyle {

  public static final JsonNumberStyle DEFAULT = new JsonNumberStyle();

  public static final class Args {
    private boolean allowNonFiniteStrings = false;
    private boolean forceEngineeringString = false;
    private boolean forcePlainString = false;

    public Args() {
      // none
    }

    public Args(Args args) {
      allowNonFiniteStrings = args.allowNonFiniteStrings;
      forceEngineeringString = args.forceEngineeringString;
      forcePlainString = args.forcePlainString;
    }

    public Args(JsonNumberStyle args) {
      allowNonFiniteStrings = args.allowNonFiniteStrings;
      forceEngineeringString = args.forceEngineeringString;
      forcePlainString = args.forcePlainString;
    }

    public final boolean allowNonFiniteStrings() {
      return allowNonFiniteStrings;
    }

    public final void allowNonFiniteStrings(boolean allowNonFiniteStrings) {
      this.allowNonFiniteStrings = allowNonFiniteStrings;
    }

    public final boolean forceEngineeringString() {
      return forceEngineeringString;
    }

    public final void forceEngineeringString(boolean forceEngineeringString) {
      this.forceEngineeringString = forceEngineeringString;
    }

    public final boolean forcePlainString() {
      return forcePlainString;
    }

    public final void forcePlainString(boolean forcePlainString) {
      this.forcePlainString = forcePlainString;
    }
  }

  private final boolean allowNonFiniteStrings;
  private final boolean forceEngineeringString;
  private final boolean forcePlainString;

  public JsonNumberStyle() {
    this(new Args());
  }

  public JsonNumberStyle(Args args) {
    allowNonFiniteStrings = args.allowNonFiniteStrings;
    forceEngineeringString = args.forceEngineeringString;
    forcePlainString = args.forcePlainString;
  }

  public JsonNumberStyle(Consumer<Args> args) {
    this(Configure.configure(new Args(), args));
  }

  public JsonNumberStyle withChanges(Consumer<Args> args) {
    return new JsonNumberStyle(Configure.configure(new Args(this), args));
  }

  public final boolean allowNonFiniteStrings() {
    return allowNonFiniteStrings;
  }

  public final boolean forceEngineeringString() {
    return forceEngineeringString;
  }

  public final boolean forcePlainString() {
    return forcePlainString;
  }

}
