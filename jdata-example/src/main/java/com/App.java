package com;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import sun.jvm.hotspot.debugger.Page;

import javax.sql.DataSource;
import java.util.Map;

@SpringBootApplication
public class App {



    @Autowired
    private Environment env;

    @Bean(name="ds")
    @Primary
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));//用户名
        dataSource.setPassword(env.getProperty("spring.datasource.password"));//密码
        dataSource.setInitialSize(2);
        dataSource.setMaxActive(20);
        dataSource.setMinIdle(0);
        dataSource.setMaxWait(60000);
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestOnBorrow(false);
        dataSource.setTestWhileIdle(true);
        dataSource.setPoolPreparedStatements(false);
        System.out.println(dataSource.hashCode());
        return dataSource;
    }



    @Bean(name = "ds_jdbc")
    public JdbcTemplate jdbcTemplate(){


        DataSource dataSource = dataSource();
        try {
            System.out.println(dataSource.getConnection().isClosed());
        }catch (Exception e) {
            System.out.println("无法判断");
        }
        System.out.println("dsJDBC" +dataSource.hashCode());
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        Map map = jdbcTemplate.queryForMap("select * from a where id = 1");
        System.out.println(map);

        return jdbcTemplate;
    }


    @Bean(name="ds2")
    public DataSource dataSource2() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(env.getProperty("spring.datasource2.url"));
        dataSource.setUsername(env.getProperty("spring.datasource2.username"));//用户名
        dataSource.setPassword(env.getProperty("spring.datasource2.password"));//密码
        dataSource.setInitialSize(2);
        dataSource.setMaxActive(20);
        dataSource.setMinIdle(0);
        dataSource.setMaxWait(60000);
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestOnBorrow(false);
        dataSource.setTestWhileIdle(true);
        dataSource.setPoolPreparedStatements(false);
        return dataSource;
    }


    @Bean(name = "ds2_jdbc")
    public JdbcTemplate jdbcTemplate2(){
        DataSource dataSource = dataSource();
        System.out.println("ds2JDBC" +dataSource.hashCode());
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate;
    }




    public static void main(String[] args) {
        System.out.println("start");
        SpringApplication.run(App.class);
        System.out.println("ok");
    }

}
