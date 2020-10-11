package com.dhht.sld.base.inject;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 */
@Retention(RUNTIME)  //运行时 注解
@Target(TYPE) // 类 接口 注解
public @interface ViewInject {
    int mainLayoutId()  default  -1;
}
