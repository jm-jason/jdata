package com.jdata.annotation;


import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JEntity {

    /**
     * 多数据源时 使用， 指定 datasource
     * @return
     */
    String dataSource() default "";

    /**
     * 表 名 - 默认为 类名！
     * @return
     */
    String name()  ;

    /**
     * 索引 ，默认 无
     * @return
     */
    String index() default "" ;

    /**
     * 唯一值索引
     * @return
     */
    String uniqueIndex() default "";

    JSplit spilt() default JSplit.NONE;




}
