package io.github.nhwalker.jsonup.elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import io.github.nhwalker.jsonup.exceptions.JsonFormatException;
import io.github.nhwalker.jsonup.format.JsonStyle;

public class JsonNumberWriter extends JsonWriter<JsonNumber> {
  public static final JsonNumberWriter DEFAULT = new JsonNumberWriter();
  
  @Override
  public int write(JsonWriterContext context, JsonNumber input, Appendable out, JsonStyle style, int indentLevel) throws IOException {
    Number value = input.numberValue();
    if (value instanceof Integer //
        || value instanceof Long //
        || value instanceof Byte //
        || value instanceof Short//
        || value instanceof AtomicInteger //
        || value instanceof BigInteger) {
      writeInt(out, style, value.intValue());
    } else if (value instanceof Long || value instanceof AtomicLong) {
      writeLong(out, style, value.longValue());
    } else if (value instanceof BigInteger) {
      writeBigInteger(out, style, (BigInteger) value);
    } else if (value instanceof Float) {
      writeFloat(out, style, value.floatValue());
    } else if (value instanceof Double) {
      writeDouble(out, style, value.doubleValue());
    } else if (value instanceof BigDecimal) {
      writeBigDecimal(out, style, (BigDecimal) value);
    } else {
      long xLong = value.longValue();
      double xDouble = value.doubleValue();
      if (xLong == xDouble) {
        writeLong(out, style, xLong);
      } else {
        writeDouble(out, style, xDouble);
      }
    }
    return indentLevel;
  }

  private static void writeFloat(Appendable out, JsonStyle style, float x) throws IOException {
    if (!Float.isFinite(x)) {
      if (!style.numberStyle().allowNonFiniteStrings()) {
        throw new JsonFormatException("Cannot encode non-finite float: " + x);
      }
      out.append('"');
      out.append(Float.toString(x));
      out.append('"');
    } else if (style.numberStyle().forceEngineeringString()) {
      out.append(BigDecimal.valueOf(x).toEngineeringString());
    } else if (style.numberStyle().forcePlainString()) {
      out.append(BigDecimal.valueOf(x).toPlainString());
    } else {
      out.append(Float.toString(x));
    }
  }

  private static void writeDouble(Appendable out, JsonStyle style, double x) throws IOException {
    if (!Double.isFinite(x)) {
      if (!style.numberStyle().allowNonFiniteStrings()) {
        throw new JsonFormatException("Cannot encode non-finite double: " + x);
      }
      out.append('"');
      out.append(Double.toString(x));
      out.append('"');
    } else if (style.numberStyle().forceEngineeringString()) {
      out.append(BigDecimal.valueOf(x).toEngineeringString());
    } else if (style.numberStyle().forcePlainString()) {
      out.append(BigDecimal.valueOf(x).toPlainString());
    } else {
      out.append(Double.toString(x));
    }
  }

  private static void writeBigInteger(Appendable out, JsonStyle style, BigInteger x) throws IOException {
    if (style.numberStyle().forceEngineeringString()) {
      out.append(new BigDecimal(x).toEngineeringString());
    } else {
      out.append(x.toString());
    }
  }

  private static void writeLong(Appendable out, JsonStyle style, long x) throws IOException {
    if (style.numberStyle().forceEngineeringString()) {
      out.append(BigDecimal.valueOf(x).toEngineeringString());
    } else {
      out.append(Long.toString(x));
    }
  }

  private static void writeInt(Appendable out, JsonStyle style, int x) throws IOException {
    if (style.numberStyle().forceEngineeringString()) {
      BigDecimal.valueOf(x).toEngineeringString();
    } else {
      out.append(Integer.toString(x));
    }
  }

  private static void writeBigDecimal(Appendable out, JsonStyle style, BigDecimal x) throws IOException {
    if (style.numberStyle().forceEngineeringString()) {
      out.append(x.toEngineeringString());
    } else if (style.numberStyle().forcePlainString()) {
      out.append(x.toPlainString());
    } else {
      out.append(x.toString());
    }
  }

}
