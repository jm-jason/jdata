package com.jdata.sql.builder;


import com.jdata.exception.JSqlException;
import com.jdata.util.JClassUtil;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * sql 语句 封装块
 */
public class JSqlPqrt implements Serializable{

    private static final long serialVersionUID = -4737173326413693263L;
    private    Class cls = null;
    /**
     * 参数
     */
    private List<String> queryParams;
    /**
     * 值
     */
    private List<Object> values ;

    private List<String>  special;

    protected JSqlPqrt(Class cls) {
        queryParams = new ArrayList<String>();
        values = new ArrayList<Object>() ;
        special = new ArrayList<String>();
        this.cls = cls;
    }

    public JSqlPqrt equal(String param , Object v) throws JSqlException {
        this.checkExists(param);
        queryParams.add( param + " =  ? ");
        values.add( v) ;
        return this;
    }


    public JSqlPqrt biggerThan(String param , Object v) throws JSqlException{
        this.checkExists(param);
        queryParams.add(param + " > ? " );
        values.add( v) ;
        return this;

    }

    public JSqlPqrt lessThan(String param , Object v) throws JSqlException{
        this.checkExists(param);

        queryParams.add(param + " < ?");
        values.add( v) ;
        return this;
    }


    public JSqlPqrt like(String param , Object v){
        queryParams.add( param + " like ? ");
        values.add( v) ;
        return this;
    }

    public JSqlPqrt in(String param , Object... v)throws JSqlException{
        this.checkExists(param);

        StringBuffer sqlBuf = new StringBuffer(param);
        sqlBuf.append(" in (");
        for(int i = 0 ; i < v.length ; i++){
            if(i > 0){
                sqlBuf.append(", ");
                values.add(v[i]);
            }
            sqlBuf.append(" ?") ;
            values.add(v[i]);
        }
        sqlBuf.append(") ");
        queryParams.add(sqlBuf.toString());
        return this;
    }

    public JSqlPqrt notIn(String param , Object... v) throws JSqlException{
        this.checkExists(param);

        StringBuffer sqlBuf = new StringBuffer(param);
        sqlBuf.append(" not in (");
        for(int i = 0 ; i < v.length ; i++){
            if(i > 0){
                sqlBuf.append(", ");
                values.add(v[i]);
            }
            sqlBuf.append(" ?") ;
            values.add(v[i]);
        }
        sqlBuf.append(") ");
        queryParams.add(sqlBuf.toString());

        return this;
    }



    public JSqlPqrt isNull(String param ) throws JSqlException{
        this.checkExists(param);

        special.add(param + " is null ");
        return this;
    }


    public JSqlPqrt beforeNow(String param) throws JSqlException{
        this.checkExists(param);

        if(
                JClassUtil.checkType(this.cls,param,new Timestamp(System.currentTimeMillis()))
                        ||
                        JClassUtil.checkType(this.cls,param, new Date())
                ){
            special.add( param += " < now() " );
            return this;
        }
        throw new JSqlException( "实体对象的变量 ：" + param + "    类型必须为 java.util.Date 或者java.sql.Timestamp 类型");


    }

    public JSqlPqrt afterNow(String param) throws JSqlException{
        this.checkExists(param);
        if(
                JClassUtil.checkType(this.cls,param,new Timestamp(System.currentTimeMillis()))
                        ||
                JClassUtil.checkType(this.cls,param, new Date())
        ){
            special.add( param += " > now() " );
            return this;
        }
        throw new JSqlException( "实体对象的变量 ：" + param + "    类型必须为 java.util.Date 或者java.sql.Timestamp 类型");

    }



    public String toQuery(){
        StringBuffer sqlBuf = new StringBuffer(" ( ");
        for(String query : queryParams){
            sqlBuf.append( query).append(" and ") ;
        }

        for(String query : special){
            sqlBuf.append(query).append(" and ");
        }
        sqlBuf.append(" 1 = 1  ) ") ;

        return sqlBuf.toString() ;
    }

    public final List<Object> values(){
        return this.values;
    }


    /**
     * 检查实体对象 变量名是否存在
     * @param param
     * @return
     */
    private boolean checkExists(String param) throws JSqlException{
        if( JClassUtil.getFiled(cls,param) == null){
            throw new JSqlException("实体对象不存在名为 " + param +" 的变量。");
        }
        return true;
    }


}
