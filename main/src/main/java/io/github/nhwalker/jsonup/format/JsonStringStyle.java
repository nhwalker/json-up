package io.github.nhwalker.jsonup.format;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import io.github.nhwalker.jsonup.util.Configure;

public class JsonStringStyle {
  public static final JsonStringStyle DEFAULT = new JsonStringStyle();

  public static final class Args {
    private UnaryOperator<String> customEncode = null;
    private UnaryOperator<String> customDecode = null;

    public Args() {
      // none
    }

    public Args(Args args) {
      this.customEncode = args.customEncode;
      this.customDecode = args.customDecode;
    }

    public Args(JsonStringStyle args) {
      this.customEncode = args.customEncode;
      this.customDecode = args.customDecode;
    }

    public final UnaryOperator<String> customEncode() {
      return customEncode;
    }

    public final void customEncode(UnaryOperator<String> customEncode) {
      this.customEncode = customEncode;
    }

    public final UnaryOperator<String> customDecode() {
      return customDecode;
    }

    public final void customDecode(UnaryOperator<String> customDecode) {
      this.customDecode = customDecode;
    }

  }

  private final UnaryOperator<String> customEncode;
  private final UnaryOperator<String> customDecode;

  public JsonStringStyle() {
    this(new Args());
  }

  public JsonStringStyle(Args args) {
    customEncode = args.customEncode;
    customDecode = args.customDecode;
  }

  public JsonStringStyle(Consumer<Args> args) {
    this(Configure.configure(new Args(), args));
  }

  public JsonStringStyle withChanges(Consumer<Args> args) {
    return new JsonStringStyle(Configure.configure(new Args(this), args));
  }

  public final UnaryOperator<String> customEncode() {
    return customEncode;
  }

  public final UnaryOperator<String> customDecode() {
    return customDecode;
  }

  public final boolean hasCustomEncode() {
    return customEncode != null;
  }

  public final boolean hasCustomDecode() {
    return customDecode != null;
  }
}
