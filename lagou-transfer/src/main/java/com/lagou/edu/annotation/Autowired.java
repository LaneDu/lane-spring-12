package com.lagou.edu.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解@Autowired
 * @author lane
 * @date 2021年03月28日 下午6:37
 */
@Target(ElementType.FIELD) //作用于字段
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
    boolean required() default true;
}

