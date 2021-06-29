package io.github.nhwalker.jsonup.format;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;

public class RepeatingStringFactory {

  public static RepeatingStringFactory empty() {
    return EMPTY;
  }

  public static RepeatingStringFactory spaceFactory() {
    return LazySpaces.INSTANCES[1];
  }

  public static RepeatingStringFactory spaceFactory(int size) {
    if (size <= 0) {
      return EMPTY;
    } else if (size < LazySpaces.INSTANCES.length) {
      return LazySpaces.INSTANCES[size];
    }

    final int cacheSize = DEFAULT_CACHE_SIZE * size;
    char[] bufferArray = new char[cacheSize];
    Arrays.fill(bufferArray, ' ');
    String bufferString = String.valueOf(bufferArray);
    return new RepeatingStringFactory(bufferString, size);
  }

  public static RepeatingStringFactory tabFactory() {
    return LazyTabs.instance;
  }

  public static RepeatingStringFactory of(String toRepeat) {
    switch (toRepeat) {
    case "": // 0
      return EMPTY;
    case " ": // space = 1
      return LazySpaces.INSTANCES[1];
    case "  ": // space = 2
      return LazySpaces.INSTANCES[2];
    case "   ": // space = 3
      return LazySpaces.INSTANCES[3];
    case "    ": // space = 4
      return LazySpaces.INSTANCES[4];
    case "     ": // space = 5
      return LazySpaces.INSTANCES[5];
    case "\t":
      return LazyTabs.instance;
    default:
      return create(toRepeat, DEFAULT_CACHE_SIZE);
    }
  }

  private static final int DEFAULT_CACHE_SIZE = 128;

  private static final RepeatingStringFactory EMPTY = new RepeatingStringFactory();

  private static class LazySpaces {
    private static final RepeatingStringFactory[] INSTANCES = new RepeatingStringFactory[6];
    static {
      final int size = DEFAULT_CACHE_SIZE * 5;
      char[] bufferArray = new char[size];
      Arrays.fill(bufferArray, ' ');
      String bufferString = String.valueOf(bufferArray);
      INSTANCES[0] = EMPTY;
      INSTANCES[1] = new RepeatingStringFactory(bufferString, 1);
      INSTANCES[2] = new RepeatingStringFactory(sizedRight(bufferString, 2), 2);
      INSTANCES[3] = new RepeatingStringFactory(sizedRight(bufferString, 3), 3);
      INSTANCES[4] = new RepeatingStringFactory(sizedRight(bufferString, 4), 4);
      INSTANCES[5] = new RepeatingStringFactory(sizedRight(bufferString, 5), 5);
    }

    private static final String sizedRight(String str, int size) {
      return str.substring(0, (str.length() / size) * size);
    }
  }

  private static class LazyTabs {
    private static final RepeatingStringFactory instance;
    static {
      final int size = 128;
      char[] bufferArray = new char[size];
      Arrays.fill(bufferArray, '\t');
      String bufferString = String.valueOf(bufferArray);
      instance = new RepeatingStringFactory(bufferString, size);
    }
  }

  private final String cached;
  private final int stride;
  private final int cacheSize;

  private static RepeatingStringFactory create(String toRepeat, int repeatsToCache) {
    StringBuilder builder = new StringBuilder(toRepeat.length() * repeatsToCache);
    for (int i = 0; i < repeatsToCache; i++) {
      builder.append(toRepeat);
    }
    return new RepeatingStringFactory(builder.toString(), toRepeat.length());
  }

  private RepeatingStringFactory() {
    this.cached = "";
    this.stride = 0;
    this.cacheSize = 0;
  }

  private RepeatingStringFactory(String cache, int stride) {
    this.cached = cache;
    this.stride = stride;
    this.cacheSize = cache.length() / stride;
  }

  public String create(int size) {
    if (size <= 0 || this.cacheSize == 0) {
      return "";
    } else if (size <= cacheSize) {
      return cached.substring(0, size * stride);
    }
    try {
      StringBuilder builder = new StringBuilder(size * stride);
      append(builder, size);
      return builder.toString();
    } catch (IOException e) {
      // should not be possible from a StringBuilder
      throw new UncheckedIOException(e);
    }
  }

  public void append(Appendable out, int size) throws IOException {
    if (size <= 0 || this.cacheSize == 0) {
      return;
    } else if (size <= cacheSize) {
      out.append(cached.substring(0, size * stride));
    } else {
      int remaining = size;
      for (; remaining >= cacheSize; remaining -= cacheSize) {
        out.append(cached);
      }
      if (remaining > 0) {
        out.append(cached.substring(0, remaining * stride));
      }
    }
  }

}
