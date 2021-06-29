package io.github.nhwalker.jsonup.internal;

import java.util.function.Consumer;

public final class Configure {

  public static <X> X configure(X x, Consumer<X> config) {
    config.accept(x);
    return x;
  }
}
