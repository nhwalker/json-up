package io.github.nhwalker.jsonup.elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

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
  
  @Override
  public int defaultWrite(JsonWriterContext context, Appendable out, JsonStyle style, int indentLevel)
      throws IOException {
    return JsonNumberWriter.DEFAULT.write(context, this, out, style, indentLevel);
  }
}