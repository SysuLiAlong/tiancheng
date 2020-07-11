package com.tiancheng.ms.common.aop;

import com.tiancheng.ms.common.enums.DeleteTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface DeleteType {
    DeleteTypeEnum value();
    String id() default "";
}
