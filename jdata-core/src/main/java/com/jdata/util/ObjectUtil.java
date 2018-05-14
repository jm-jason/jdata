package com.jdata.util;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Object 检查工具类
 * Created by JasonMao on 2017/12/23.
 */
public class ObjectUtil implements Serializable {
    private static final long serialVersionUID = -6613663109553276024L;

    public static boolean isNull(Object o){
        return null == o;
    }
    public static boolean isEmpty(Object o){
        if(isNull(o)) return true;
        if(isString(o)){
            return ((String)o).trim().equals("");
        }
        return false;
    }

    public static boolean isChar(Object o){
        return o instanceof Character;
    }

    public static boolean isString(Object o){
       return o instanceof String;
    }

    public static boolean isInteger(Object o){
        return (o instanceof Integer);
    }

    public static boolean isLong(Object o){
        return o instanceof Long;
    }

    public static boolean isDouble(Object o){
        return o instanceof Double;
    }

    public static boolean isFloat(Object o){
        return o instanceof Float;
    }

    public static boolean isMap(Object o){
        return o instanceof Map;
    }


    /**
     * 检查是否是Date类型， sqldate 也会返回true
     * @param o
     * @return
     */
    public static boolean isDate(Object o){
        return o instanceof Date;
    }

    public static boolean isSqlDate(Object o){
        return o instanceof java.sql.Date;
    }


    /**
     * Map 转换为 实体对象！
     * @param o
     * @param map
     * @return
     */
    public static Object mapToObject(Object o ,Map<String,Object> map){
        return JClassUtil.fromMap(map,o);
    }


}
