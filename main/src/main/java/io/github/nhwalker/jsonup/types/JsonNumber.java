package io.github.nhwalker.jsonup.types;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import io.github.nhwalker.jsonup.exceptions.JsonFormatException;
import io.github.nhwalker.jsonup.format.JsonStyle;

public class JsonNumber extends JsonElement {
  private final Number value;

  public JsonNumber(Number value) {
    this.value = Objects.requireNonNull(value);
  }

  public Number numberValue() {
    return value;
  }

  public int intValue() {
    return value.intValue();
  }

  public long longValue() {
    return value.longValue();
  }

  public double doubleValue() {
    return value.doubleValue();
  }

  public short shortValue() {
    return value.shortValue();
  }

  public byte byteValue() {
    return value.byteValue();
  }

  public BigInteger bigIntegerValue() {
    if (value instanceof BigInteger) {
      return (BigInteger) value;
    } else {
      return BigInteger.valueOf(value.longValue());
    }
  }

  public BigDecimal bigDecimalValue() {
    if (value instanceof BigDecimal) {
      return (BigDecimal) value;
    } else {
      return BigDecimal.valueOf(value.doubleValue());
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    return prime + value.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof JsonNumber)) {
      return false;
    }
    JsonNumber other = (JsonNumber) obj;
    if (!value.equals(other.value)) {
      return false;
    }
    return true;
  }

  @Override
  protected int write(Appendable out, JsonStyle style, int indentLevel) throws IOException {
    if (value instanceof Integer //
        || value instanceof Long //
        || value instanceof Byte //
        || value instanceof Short//
        || value instanceof AtomicInteger //
        || value instanceof AtomicLong //
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

  private void writeFloat(Appendable out, JsonStyle style, float x) throws IOException {
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

  private void writeDouble(Appendable out, JsonStyle style, double x) throws IOException {
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

  private void writeBigInteger(Appendable out, JsonStyle style, BigInteger x) throws IOException {
    if (style.numberStyle().forceEngineeringString()) {
      out.append(new BigDecimal(x).toEngineeringString());
    } else {
      out.append(x.toString());
    }
  }

  private void writeLong(Appendable out, JsonStyle style, long x) throws IOException {
    if (style.numberStyle().forceEngineeringString()) {
      out.append(BigDecimal.valueOf(x).toEngineeringString());
    } else {
      out.append(Long.toString(x));
    }
  }

  private void writeInt(Appendable out, JsonStyle style, int x) throws IOException {
    if (style.numberStyle().forceEngineeringString()) {
      BigDecimal.valueOf(x).toEngineeringString();
    } else {
      out.append(Integer.toString(x));
    }
  }

  private void writeBigDecimal(Appendable out, JsonStyle style, BigDecimal x) throws IOException {
    if (style.numberStyle().forceEngineeringString()) {
      out.append(x.toEngineeringString());
    } else if (style.numberStyle().forcePlainString()) {
      out.append(x.toPlainString());
    } else {
      out.append(x.toString());
    }
  }

  @Override
  public Kind kind() {
    return Kind.NUMBER;
  }

  @Override
  public JsonNumber asNumber() {
    return this;
  }

  @Override
  public boolean isNumber() {
    return true;
  }
}