package io.github.nhwalker.jsonup.adapters.common;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.github.nhwalker.jsonup.adapters.helpers.StringFormatConverter;
import io.github.nhwalker.jsonup.convert.JsonAdapter;
import io.github.nhwalker.jsonup.convert.JsonConverterSet;

public enum JavaTimeConverterSet implements JsonConverterSet {
  DEFAULT;

  private static final List<JsonAdapter> CONVERTERS = Collections.unmodifiableList(Arrays.asList(//
      DurationConverter.DEFAULT, //
      InstantConverter.DEFAULT, //
      PeriodConverter.DEFAULT, //
      LocalDateTimeConverter.DEFAULT, //
      LocalDateConverter.DEFAULT, //
      LocalTimeConverter.DEFAULT, //
      ZonedDateTimeConverter.DEFAULT, //
      OffsetDateTimeConverter.DEFAULT, //
      MonthDayConverter.DEFAULT, //
      YearConverter.DEFAULT, //
      YearMonthConverter.DEFAULT, //
      ZoneIdConverter.DEFAULT, //
      ZoneOffsetConverter.DEFAULT));

  @Override
  public Iterable<? extends JsonAdapter> converters() {
    return CONVERTERS;
  }

  public static enum DurationConverter implements StringFormatConverter<Duration> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return Duration.class;
    }

    @Override
    public Duration fromString(String value) {
      return Duration.parse(value);
    }
  }

  public static enum InstantConverter implements StringFormatConverter<Instant> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return Instant.class;
    }

    @Override
    public Instant fromString(String value) {
      return Instant.parse(value);
    }
  }

  public static enum PeriodConverter implements StringFormatConverter<Period> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return Period.class;
    }

    @Override
    public Period fromString(String value) {
      return Period.parse(value);
    }
  }

  public static enum LocalDateTimeConverter implements StringFormatConverter<LocalDateTime> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return LocalDateTime.class;
    }

    @Override
    public LocalDateTime fromString(String value) {
      return LocalDateTime.parse(value);
    }
  }

  public static enum LocalDateConverter implements StringFormatConverter<LocalDate> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return LocalDate.class;
    }

    @Override
    public LocalDate fromString(String value) {
      return LocalDate.parse(value);
    }
  }

  public static enum LocalTimeConverter implements StringFormatConverter<LocalTime> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return LocalTime.class;
    }

    @Override
    public LocalTime fromString(String value) {
      return LocalTime.parse(value);
    }
  }

  public static enum ZonedDateTimeConverter implements StringFormatConverter<ZonedDateTime> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return ZonedDateTime.class;
    }

    @Override
    public ZonedDateTime fromString(String value) {
      return ZonedDateTime.parse(value);
    }
  }

  public static enum OffsetDateTimeConverter implements StringFormatConverter<OffsetDateTime> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return OffsetDateTime.class;
    }

    @Override
    public OffsetDateTime fromString(String value) {
      return OffsetDateTime.parse(value);
    }
  }

  public static enum MonthDayConverter implements StringFormatConverter<MonthDay> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return MonthDay.class;
    }

    @Override
    public MonthDay fromString(String value) {
      return MonthDay.parse(value);
    }
  }

  public static enum YearConverter implements StringFormatConverter<Year> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return Year.class;
    }

    @Override
    public Year fromString(String value) {
      return Year.parse(value);
    }
  }

  public static enum YearMonthConverter implements StringFormatConverter<YearMonth> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return YearMonth.class;
    }

    @Override
    public YearMonth fromString(String value) {
      return YearMonth.parse(value);
    }
  }

  public static enum ZoneIdConverter implements StringFormatConverter<ZoneId> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return ZoneId.class;
    }

    @Override
    public ZoneId fromString(String value) {
      return ZoneId.of(value);
    }

    @Override
    public String toString(ZoneId value) {
      return value.getId();
    }
  }

  public static enum ZoneOffsetConverter implements StringFormatConverter<ZoneOffset> {
    DEFAULT;

    @Override
    public Class<?> type() {
      return ZoneOffset.class;
    }

    @Override
    public ZoneOffset fromString(String value) {
      return ZoneOffset.of(value);
    }

    @Override
    public String toString(ZoneOffset value) {
      return value.getId();
    }
  }
}
