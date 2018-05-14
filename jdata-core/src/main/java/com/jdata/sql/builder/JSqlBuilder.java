package com.jdata.sql.builder;

import com.jdata.annotation.JEntity;
import com.jdata.entity.JSimpleEntity;
import com.jdata.exception.JSqlException;
import com.jdata.util.JClassUtil;
import com.jdata.util.JEntityUtil;

import java.io.Serializable;
import java.util.*;

public class JSqlBuilder<T extends JSimpleEntity> implements Serializable{
    private static final long serialVersionUID = 4825239764031389168L;
    private List<Map> updateList = new ArrayList<Map>();
    private Class cls = null;
    /**
     * 表名
     */
    private String tableName ;
    /**
     * 查询字段 查询用！
     */
    private String[] selectClms;
    private List<Object> values;

    private StringBuffer whereQuery = new StringBuffer( " where 1 = 1 " );

    public JSqlBuilder(Class<T> cls,Date splitDate) throws JSqlException {
        if(cls == null){
            throw new JSqlException("未指定实体对象的Class类型");
        }
        JEntity jEntity =(JEntity)cls.getAnnotation(JEntity.class);
        this.tableName = JEntityUtil.getTableName(cls,splitDate);
        this.cls = cls;
        this.values = new ArrayList<Object>();
    }

    public JSqlPqrt buildQueryPart(){
        return new JSqlPqrt( this.cls);
    }


    /**
     * 追加 and 语句片段  追加到where 后
     * @param query
     */
    public void and(final JSqlPqrt query){
        whereQuery.append(" and ").append(query.toQuery()) ;
        values.addAll(query.values());
    }


    /**
     * 追加 or 语句 片段 追加到where 后
     * @param query
     */
    public void or(final JSqlPqrt query){
        whereQuery.append( " or ").append(query.toQuery());
        values.addAll(query.values());
    }


    /**
     *  增加update 字段
     * @param key
     * @param v
     */
    public void updateSetOne(String key ,Object v) throws JSqlException{
        Map<String,Object> map = new HashMap<String,Object>(1);
        if(JClassUtil.existsFieldName(cls,key)){
            map.put("k",key);
            map.put("v",v);
            updateList.add(map);
            map = null;
        }else{
            throw new JSqlException( this.cls.getName() + " update 的参数" + key + "不存在");
        }
    }


    public void updateSetAll(T t){
        Map<String,Object> all =JClassUtil.getAllNameAndValueMap( t) ;
        updateList.clear();
        for(String key : all.keySet()){
            if(key.equals("id")) continue;
            Map<String,Object> map = new HashMap<String,Object>(1);
            map.put("k",key);
            map.put("v",all.get(key));
            updateList.add(map);
        }
    }


    private String wherequery(){
        return  whereQuery.toString();
       // return new JSQLQuery(sql,values);
    }

    public JSQLQuery selectCountSQL(){
        StringBuffer sql = new StringBuffer("select count(*) from ")
                .append(tableName)
                .append(" ")
                .append(wherequery());
        return new JSQLQuery(sql.toString(),values);

    }

    /**
     * 得到 查询 语句 ，不用考虑分页，分页由dao处理
     * @return
     */
    public JSQLQuery selectSQL(){
        StringBuffer sql = new StringBuffer("select * from " ).append( tableName).append( wherequery());
        JSQLQuery jsqlQuery =  new JSQLQuery(sql.toString(),values);
        sql = null;
        return jsqlQuery;
    }



    /**
     *
     * @return
     */
    public JSQLQuery updateSQL(){
        StringBuffer sql = new StringBuffer("update ").append( tableName).append(" ").append(" set ");
        for(int i= 0 ; i < updateList.size(); i ++ ){
            if(i >0){
                sql.append(", ");
            }
            Map map = updateList.get(i);
            sql.append( map.get("k")).append(" = ? ");
        }
        sql.append(wherequery());
        List<Object> list = new ArrayList<Object>();
        for(Map o : updateList){
            list.add(o.get("v"));
        }
        list.addAll(values);

        return  new JSQLQuery(sql.toString(),list);
    }


    /**
     * inser 语句
     * @param t
     * @param splitDate
     * @return
     */
    public  JSQLQuery insetSQL(T t, Date splitDate){
        String tableName =JEntityUtil.getTableName(t.getClass(),splitDate);
        Map<String,Object> all =JClassUtil.getAllNameAndValueMap( t) ;
        List<Object> values = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer("insert into ");

        stringBuffer.append(tableName).append("(");
        int index = 0 ;
        for(String key : all.keySet()){
            if(index > 0 ){
                stringBuffer.append(",");
            }
            stringBuffer.append(key).append(" ");
            index ++ ;
        }
        stringBuffer.append(") values(");
        index = 0 ;
        for(String key : all.keySet()){
            if(index > 0 ) {
                stringBuffer.append(",");
            }
            stringBuffer.append(" ? ");
            values.add( all.get(key));
            index ++ ;
        }
        stringBuffer.append(")");
        return new JSQLQuery(stringBuffer.toString(),values);
    }


    /**
     * 批量插入sql
     * @param list
     * @param splitDate
     * @return
     */
    public JSQLQuery batchInsertSQL(List<T> list ,Date splitDate){
        String tableName =JEntityUtil.getTableName(list.get(0).getClass(),splitDate);
        Map<String,Object> one =JClassUtil.getAllNameAndValueMap(list.get(0)) ;
        List<Object> values = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer("insert into ");

        stringBuffer.append(tableName).append("(");
        int index = 0 ;
        for(String key : one.keySet()){
            if(index > 0 ) stringBuffer.append(",");
            stringBuffer.append(key).append(" ");
            index ++ ;
        }
        stringBuffer.append(" ) values ");
        int outIndex = 0 ;
        for(T t : list){
            one = JClassUtil.getAllNameAndValueMap( t);
            index = 0 ;
            if(outIndex > 0){
                stringBuffer.append(" , ");
            }
            stringBuffer.append(" (");
            for(String key : one.keySet()){
                if(index > 0 ) stringBuffer.append(",");
                stringBuffer.append(" ? ");
                values.add( one.get(key));
                index ++ ;
            }
            stringBuffer.append(")");
            outIndex ++ ;

        }

        return new JSQLQuery(stringBuffer.toString(),values);
    }



    public JSQLQuery createTableSQL(){
        String pk = null;
        List<String> indexList=new ArrayList<>();
        JClassUtil.getStructsMap(cls);
        Map<String,Map<String,Object>> map =JClassUtil.getStructsMapForDbTable(cls);
        String tableName = JEntityUtil.getTableName(cls,null);
        StringBuffer sb = new StringBuffer("CREATE TABLE IF NOT EXISTS " );
        sb.append(tableName).append("( ");
        int index = 0;
        for(String key : map.keySet()){
            if(index>0)
                sb.append(" , ");
            Map<String,Object> clmMap = map.get(key);
            sb.append(key).append("  ");
            sb.append(clmMap.get("type").toString()).append(" ");
            if((boolean)clmMap.get("nullable")){
                sb.append(" ");
            }else{
                sb.append( " NOT NULL ");
            }
            if((boolean)clmMap.get("pk")){
                pk = " , PRIMARY KEY ("+key+")" ;
            }
            if((boolean)clmMap.get("unique")  || (boolean)clmMap.get("index")  ){
                if((boolean)clmMap.get("unique")) {
                    indexList.add(" , UNIQUE KEY un" + key + System.nanoTime() + " (" + key + ")");
                }else {
                    indexList.add(" ,KEY index_" + key + System.nanoTime() + " (" + key + ")");
                }
            }
            index ++ ;
        }
        sb.append("");
        if(pk != null){
            sb.append( pk );
        }
        for(String is : indexList ){
            sb.append(is);
        }
        sb.append(") ").append("ENGINE=InnoDB DEFAULT CHARSET=utf8 ");


        return new JSQLQuery(sb.toString(),new ArrayList<>());
    }

}
