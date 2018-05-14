package com.jdata.annotation;

import java.lang.annotation.*;

/**
 * Jdata建表时会忽略字段
 * 优先级大于 JColumn
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JIgnore {

}
