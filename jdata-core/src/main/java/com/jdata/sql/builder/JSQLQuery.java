package com.jdata.sql.builder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.List;

/**
 * SQL查询输出对象！ 作为dao层的参数！
 */
public class JSQLQuery implements Serializable{
    private static final long serialVersionUID = -3630454291946814272L;

    private static Logger logger = LogManager.getLogger(JSQLQuery.class);
    private final String sql ;
    private final List<Object> values;

    public JSQLQuery(final String sql, final List<Object> values) {
        this.sql = sql;
        this.values = values;
        logger.debug("参数总数" +(sql.length() - sql.replaceAll("\\?","").length()) + " 值总数"  + values.size() );
        logger.debug("sql : " + sql);
        for(Object o : values){
            logger.debug( "\t\t ->"+o );
        }

    }



    public String sql(){
        return this.sql;
    }

    public List<Object> values(){
        return this.values;
    }

}
