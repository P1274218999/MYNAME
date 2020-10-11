package com.dhht.sld.base.inject;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 判断是否登录才可
 */
@Retention(RUNTIME)  //运行时 注解
@Target(TYPE) // 类 接口 注解
public @interface IsLoginInject {}
