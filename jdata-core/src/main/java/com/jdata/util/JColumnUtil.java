package com.jdata.util;

import com.jdata.annotation.JColumn;

import java.io.Serializable;
import java.lang.reflect.Field;

public class JColumnUtil implements Serializable{

    private static final long serialVersionUID = -755210031613600588L;

    /**
     * 检查注解是否存在
     * @param field
     * @return
     */
    public static boolean checkAnnotation(Field field){
        try{
            JColumn jColumn =(JColumn)field.getAnnotation(JColumn.class);
            if(jColumn == null){
                return false;
            }
        }catch (Exception e){
            return false;
        }
        return true;

    }


    public static JColumn getAnnotation(Field field){
        try{
            return (JColumn)field.getAnnotation(JColumn.class);
        }catch (Exception e){
            return null;
        }
    }


}
