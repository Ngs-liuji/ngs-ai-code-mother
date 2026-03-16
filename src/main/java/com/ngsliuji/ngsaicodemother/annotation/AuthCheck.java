package com.ngsliuji.ngsaicodemother.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//方法注解
@Retention(RetentionPolicy.RUNTIME)//运行时注解
public @interface AuthCheck {

    /**
     * 必须有某个权限
     *
     * @return
     */
    String mustRole() default "";

}
