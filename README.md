# jdata项目介绍

jdata-core 模块
  一个 基于SpringJdbc封装的工具组件包

jdata-example
  参考示例
# 简介
## 版本：0.1.1.beta

## 1. 注解介绍

### JEntity
    表映射注解
    dataSource
            默认值：无
            作用： 多数据源时候，指定特定的数据源

    name
           默认值：无
           作用：  指定mysql的数据库表名
    spilt
            默认值：JSplit.NONE
            作用： 分表参数，参考JSplit 注解。

### JIgnore
    分表支持注解，目前仅支持按照时间分表




  
# 快速开始 quick start
## 1.引入项目 Maven


POM引入 ：

```xml
    <dependency>
        <groupId>com.jdata</groupId>
        <artifactId>jdata-core</artifactId>
        <version>0.1.0-beta</version>
        <exclusions>
            <exclusion>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-parent</artifactId>
            </exclusion>
            <exclusion>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
```

## 2.配置数据源/多数据源


