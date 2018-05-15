package com.jdata.dao;

import com.jdata.entity.JSimpleEntity;
import com.jdata.exception.JDataException;
import com.jdata.results.JPageResult;
import com.jdata.results.JWriteResult;
import com.jdata.sql.builder.JSQLQuery;
import com.jdata.sql.builder.JSqlBuilder;
import com.jdata.sql.builder.JSqlPqrt;
import com.jdata.util.JClassUtil;
import com.jdata.util.JEntityUtil;
import com.jdata.util.JPageUtil;
import com.jdata.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 简单基础Dao
 */
public  class SimpleDao<T extends JSimpleEntity>  {

    private JdbcTemplate jdbcTemplate;

    public SimpleDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 返回一个Map
     * @param cls
     * @param id
     * @return
     */
    public Map findMapById(Class<T> cls ,String id){
        String table = JEntityUtil.getTableName(cls,null);
        StringBuffer sql = new StringBuffer("select * from " ).append(table).append(" where id = ?");
        Map map = jdbcTemplate.queryForMap(sql.toString(),id);
        sql = null;
        return map;
    }


    public Object findOneById(Class<T> cls, String id){
        String table = JEntityUtil.getTableName(cls,null);
        StringBuffer sql = new StringBuffer("select * from " ).append(table).append(" where id = ? ");
        Map map = jdbcTemplate.queryForMap(sql.toString(),id);
        sql = null;
        Object o = null;
        try {
            o = JClassUtil.instance(cls);
            o = JClassUtil.fromMap(map,o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return o;
    }


    /**
     * 获得 全部数据
     *  不建议使用，会返回所有的数据，全表的！！！！
     * @param cls
     * @return
     */
    @Deprecated
    public List<Object> findAll(Class<T> cls ) {
        StringBuffer stringBuffer = new StringBuffer("select * from  ").append(JEntityUtil.getTableName(cls,null));
        List<Map<String,Object>> list = jdbcTemplate.queryForList(stringBuffer.toString()) ;
        stringBuffer = null;
        if(list == null){
            return new ArrayList<Object>();
        }
        List<Object> rlist = new ArrayList<Object>();
        for(Map<String,Object> map : list){
            try{
                T t = (T) ObjectUtil.mapToObject(JClassUtil.instance(cls),map);
                rlist.add(t);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return rlist;
    }


    /**
     * 获得总条目数
     * @param cls
     * @return
     */
    public Long count(Class<T> cls){
        StringBuffer sql = new StringBuffer("select count(*) from ").append(JEntityUtil.getTableName(cls,null));
        long c = jdbcTemplate.queryForObject(sql.toString(),Long.class);
        sql = null;
        return c ;
    }


    /**
     * 自定义sql 分页查询
     * @param cls
     * @param jPageUtil
     * @param sql
     * @param values
     * @return
     */
    public List<Object> findBySql(Class<T> cls ,JPageUtil jPageUtil,String sql ,List<Object> values){
        StringBuffer stringBuffer = new StringBuffer(sql).append(" limit ?,?");
        List<Map<String,Object>> list = jdbcTemplate.queryForList( stringBuffer.toString() , values.toArray() );
        if(list == null){
            return new ArrayList<Object>();
        }
        List<Object> rlist = new ArrayList<Object>();
        for(Map<String,Object> map : list){
            try{
                T t = (T) ObjectUtil.mapToObject(JClassUtil.instance(cls),map);
                rlist.add(t);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return rlist;
    }


    /**
     * 自定义统计总数
     * @param cls
     * @param sql
     * @param values
     * @return
     */
    public Long countBySql(Class<T> cls , String sql ,List<Object> values){
        return jdbcTemplate.queryForObject(sql,values.toArray(),Long.class);
    }


    /**
     * 根据id 判断是否存在
     * @param cls
     * @param id
     * @return
     */
    public boolean exists(Class<T> cls ,String id){
        try{
            String tbname = JEntityUtil.getTableName(cls,null);
            StringBuffer stringBuffer = new StringBuffer("select count(id) from ").append(tbname).append(" where id = ? ");
            boolean b = jdbcTemplate.queryForObject(stringBuffer.toString() ,new Object[]{id} ,Long.class ) > 0;
            stringBuffer = null;
            return b ;
        }catch (JDataException e){
            e.printStackTrace();
        }
        return false;
    }


    public JPageResult page(Class<T> cls,JPageUtil jPageUtil ,JSqlBuilder builder){

        JSQLQuery jsqlQuery = builder.selectSQL();
        JSQLQuery countQuery = builder.selectCountSQL();
        List<Object> list = (List<Object>) this.findBySql(cls,jPageUtil,jsqlQuery.sql(),jsqlQuery.values());
        long count = this.countBySql(cls,countQuery.sql(),countQuery.values());
        JPageResult result =  new JPageResult(jPageUtil.nowPage(),jPageUtil.pageSize(),count,list);
        list = null;
        return result;
    }


    /**
     * 更新 对象
     * @param t
     * @return
     */
    public JWriteResult update(T t ){
        JSqlBuilder jSqlBuilder = new JSqlBuilder(t.getClass(),null);
        jSqlBuilder.updateSetAll(t);
        JSqlPqrt sqlPqrt = jSqlBuilder.buildQueryPart();
        sqlPqrt.equal("id",t.getId());
        jSqlBuilder.and(sqlPqrt);
        JSQLQuery sqlQuery =jSqlBuilder.updateSQL();
        int i = jdbcTemplate.update(sqlQuery.sql(),sqlQuery.values().toArray());
        return JWriteResult.result(i);
    }


    public JWriteResult insert(T t){
        JSqlBuilder sqlBuilder = new JSqlBuilder(t.getClass(),null);
        JSQLQuery jsqlQuery = sqlBuilder.insetSQL(t ,null);
        int i = jdbcTemplate.update(jsqlQuery.sql(),jsqlQuery.values().toArray());
        return JWriteResult.result(i);
    }


    /**
     * 批量插入
     * @param list
     * @return
     */
    public JWriteResult batchInsert(List<T> list ){
        JSqlBuilder sqlBuilder = new JSqlBuilder(list.get(0).getClass(),null);
        JSQLQuery sqlQuery = sqlBuilder.batchInsertSQL(list,null);
        int i = jdbcTemplate.update(sqlQuery.sql(),sqlQuery.values().toArray());
        return JWriteResult.result(i);
    }


    /**
     * 自动立表
     * @param cls
     */
    public void createTable(Class<T> cls ){
        JSqlBuilder jSqlBuilder = new JSqlBuilder(cls,null);
        String sql = jSqlBuilder.createTableSQL().sql();
        this.jdbcTemplate.update(sql);
    }


    /**
     * 逻辑删除 ！
     * @return
     */
    public JWriteResult delete(T t){
        return this.delete((Class<T>) t.getClass(),t.getId());
    }

    /**
     * 物理删除 ，谨慎使用！
     * @param t
     * @return
     */
    @Deprecated
    public JWriteResult physicalDelete(T t ){
       return this.physicalDelete((Class<T>) t.getClass(),t.getId());
    }

    /**
     * 逻辑删除
     * @param cls
     * @param id
     * @return
     */
    public JWriteResult delete(Class<T> cls ,String id){
        String table = JEntityUtil.getTableName(cls,null);
        StringBuffer sql = new StringBuffer("update  ").append( table).append(" t set t.isDel = ?  where t.id = ?");
        int i = jdbcTemplate.update(sql.toString(),"YES",id);
        sql = null;
        return JWriteResult.result(i);
    }


    /**
     * 物理删除
     * @param cls
     * @param id
     * @return
     */
    @Deprecated
    public JWriteResult physicalDelete(Class<T> cls ,String id){
        String table = JEntityUtil.getTableName(cls,null);
        StringBuffer sql = new StringBuffer("DELETE  FROM ").append( table).append(" where id = ?");
        int i = jdbcTemplate.update(sql.toString(),id);
        sql = null ;
        return JWriteResult.result(i);
    }



}
