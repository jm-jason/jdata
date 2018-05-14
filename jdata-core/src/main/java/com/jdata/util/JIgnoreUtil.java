package com.jdata.util;

import com.jdata.annotation.JIgnore;

import java.io.Serializable;
import java.lang.reflect.Field;

public class JIgnoreUtil implements Serializable{


    private static final long serialVersionUID = 7790320675537397505L;

    /**
     * 检查注解是否存在
     * @param field
     * @return
     */
    public static boolean checkAnnotation(Field field){
        try{
            JIgnore jIgnore =(JIgnore)field.getAnnotation(JIgnore.class);
            if(jIgnore == null){
                return false;
            }
        }catch (Exception e){
            return false;
        }
        return true;

    }

}
