package com.jdata.annotation;


import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JColumn {

    /**
     * 数据字段长度
     * @return
     */
    int length() default 64;

    /**
     * 是否建立唯一值索引
     * @return
     */
    boolean unique() default false;


    /**
     * 是否建立索引
     */
    boolean index() default false;

    /**
     * 是否允许空
     * @return
     */
    boolean nullable() default true;


    /**
     * 要求 不要创建字段 ！
     * @return
     */
    //boolean notCreate() default false;


    /**
     * 是否为主键
     * @return
     */
    boolean pk() default false;






}
