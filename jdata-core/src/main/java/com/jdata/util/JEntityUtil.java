package com.jdata.util;

import com.jdata.annotation.JEntity;
import com.jdata.annotation.JSplit;
import com.jdata.exception.JDataException;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Jentity注解的支持类
 */
public class JEntityUtil implements Serializable{

    private static final long serialVersionUID = 169667766117128806L;

    /**
     * 检查注解是否存在
     * @param cls
     * @return
     */
    public static boolean checkAnnotation(Class cls){
        try{
            JEntity jEntity =(JEntity)cls.getAnnotation(JEntity.class);
            if(jEntity == null){
                return false;
            }
        }catch (Exception e){
            return false;
        }
        return true;

    }

    /**
     * 得到JEntity
     * @param cls
     * @return
     */
    public static JEntity getJEntiy(Class cls){
        boolean b =checkAnnotation(cls);
        if(b) {
            JEntity jEntity = (JEntity) cls.getAnnotation(JEntity.class);
            return jEntity;
        }
        return null;
    }

    public static boolean isSplitTable(Class cls){
        JEntity jEntity = getJEntiy(cls);
        return (jEntity.spilt() != JSplit.NONE) ;
    }

    /**
     * 获取相应的表名
     * @param cls
     * @param splitDate
     * @return
     */
    public static String getTableName(Class cls, Date splitDate) throws JDataException{
        JEntity jEntity = getJEntiy(cls);
        SimpleDateFormat sdf =null;
        String packageName = cls.getPackage().getName();
        String tname = null;
        if(jEntity.name().trim().equals("")){
            tname= cls.getName().replaceAll(packageName + "." ,"");
        }else{
            tname = jEntity.name();
        }
        if(jEntity.spilt() != JSplit.NONE){
            if(splitDate == null){
                throw new JDataException( "无法明确得到到正确的分表对象，"+tname +" 是一个分表的对象，请传入splitDate作为判定依据！");
            }
            switch (jEntity.spilt() ){
                case DAY:
                    tname = tname +"_" + new SimpleDateFormat("yyyy_MM_dd").format(splitDate);
                    break;
                case MONTH:
                    tname = tname +"_" + new SimpleDateFormat("yyyy_MM").format(splitDate);
                    break;
                case YEAR:
                    tname = tname +"_" + new SimpleDateFormat("yyyy").format(splitDate);
                    break;
            }
        }
        return tname;
    }

}
