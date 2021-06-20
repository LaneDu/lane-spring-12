package com.lagou.edu.annotation;

import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2SelectQueryBlock;

import java.lang.annotation.*;

/**
 * 自定义注解@Transactional
 * @author lane
 * @date 2021年03月28日 下午6:37
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional {
    String value() default "TransactionManager";

}

