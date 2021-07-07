package io.github.nhwalker.jsonup.ann;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE )
@Repeatable(ToJson.Container.class)
public @interface ToJson {

  Class<?> value();

  String enumValue() default "";

  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE )
  public @interface Container {
    ToJson[] value();
  }
}
