# jdata项目介绍

jdata-core 模块
  一个 基于SpringJdbc封装的工具组件包

jdata-example
  参考示例
  
  
# 快速开始 quick start
## 引入项目 Maven


POM引入 ：
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


