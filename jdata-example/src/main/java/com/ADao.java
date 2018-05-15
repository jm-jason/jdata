package com;

import com.jdata.dao.SimpleDao;
import com.jdata.results.JWriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class ADao   {



    JdbcTemplate jdbcTemplate ;
    SimpleDao simpleDao ;



    @Autowired
    @Qualifier("ds_jdbc")
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;


        System.out.println("jdbc--" + jdbcTemplate.hashCode());
        simpleDao = new SimpleDao(jdbcTemplate);

        simpleDao.createTable(AEntity.class);
    }

    public JWriteResult insert(AEntity a){

        System.out.println( "insert >>>"+jdbcTemplate.hashCode());
        return simpleDao.insert(a);
    }



    public AEntity get(String id){
        System.out.println( " get >>>"+jdbcTemplate.hashCode());

        return (AEntity) simpleDao.findOneById(AEntity.class,id);
    }
}
