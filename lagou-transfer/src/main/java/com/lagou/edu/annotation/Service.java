package com.lagou.edu.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解@Service
 * @author lane
 * @date 2021年03月28日 下午6:37
 */
@Target(ElementType.TYPE) //用于描述类、接口(包括注解类型) 或enum声明
@Retention(RetentionPolicy.RUNTIME) //运行时有效
public @interface Service {
    String value() default "";
}

