package com.dhht.sld.base.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解传参
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoArg {
    // Mark param's name or service name.
//    String name() default "";

    // If required, app will be crash when value is null.
    // Primitive type wont be check!
//    boolean required() default false;

    // Description of the field
//    String desc() default "";
}
