package com.example.fengcommon.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName NoRepeatSubmit
 * @Description 这里描述
 * @Author admin
 * @Date 2021/3/2 16:16
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoRepeatSubmit {

    /**
     * 设置请求锁定时间
     *
     * @return
     */
    int lockTime() default 10;
    /**
     * key name  全小写  不写默认用方法名+方法参数做key
     */
    String lockKey() default "";

}