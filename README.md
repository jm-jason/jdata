# jdata项目介绍

jdata-core 模块
  一个 基于SpringJdbc封装的工具组件包

jdata-example
  参考示例
# 简介
## 版本：0.1.1.beta
## 1. 枚举介绍
### JIgnore
    分表支持注解，目前仅支持按照时间分表
    NONE    ： 不分表
    DAY     ： 按天分表
    MONTH   ： 按月分表
    YEAR    ： 按年分表

## 2. 注解介绍

### JEntity
    表映射注解
    dataSource string
            默认值：无
            作用： 多数据源时候，指定特定的数据源

    name        string
           默认值：无
           作用：  指定mysql的数据库表名
    spilt       JIgnore枚举
            默认值：JSplit.NONE
            作用： 分表参数，参考JSplit 枚举。


### JColumn
    字段映射注解
    pk      boolean
        默认值： false
        作用：   建立主键）
    length  int
        默认值：   64
        作用：     映射数据库字段长度

    index   boolean
        默认值： false
        作用：   建立索引


    nullable  boolean
        默认值：false
        作用： 是否允许空

    unique    boolean
        默认值：false
        作用： 建立唯一值索引





  
# 快速开始 quick start
## 1.引入项目 Maven


POM引入 ：

```xml
    <dependency>
        <groupId>com.jdata</groupId>
        <artifactId>jdata-core</artifactId>
        <version>0.1.1-beta</version>
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


