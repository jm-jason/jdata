package com.jdata.util;

import com.jdata.annotation.JColumn;
import com.jdata.exception.JDataException;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 不同类之间的同参数值复制工具
 * Created by JasonMao on 2017/11/15.
 */
public class JClassUtil implements Serializable {


    private static final long serialVersionUID = -8734491653439235860L;

    /**
     * 获得指定 字段的值
     * @param bean
     * @param fieldName
     * @return
     */
    private static Object getProperty(Object bean, String fieldName) {
        Object obj = getProperty(bean.getClass(),bean,fieldName);
        return obj;
    }



    /**
     * 获取指定字段的值
     * @param _class
     * @param bean
     * @param fieldName
     * @return
     */
    private static Object getProperty(Class _class,Object bean,String fieldName){
        try {
            if(_class ==null){
                _class = bean.getClass();
            }
            return getFiled(_class, fieldName).get(bean);
        }catch (Exception e){
            return null;
        }
        /*
        Object obj = null;
        Field[] fields = _class.getDeclaredFields();
        Field.setAccessible(fields, true);
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (fieldName.equals(field.getName())) {
                try {
                    obj = field.get(bean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        if(obj == null && _class.getGenericSuperclass()!=null){
            obj = getProperty(_class.getSuperclass(), bean, fieldName);
        }
        return obj;
        */
    }

    /**
     * 获得 field
     * @param _class
     * @param fieldName
     * @return
     */
    public static Field getFiled(Class _class ,String fieldName){
        Field _field = null ;
        Field[] fields = _class.getDeclaredFields();
        Field.setAccessible(fields, true);
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (fieldName.equals(field.getName())) {
                return field;
            }
        }
        if(_field == null && _class.getGenericSuperclass()!=null){
            _field = getFiled(_class.getSuperclass(), fieldName);
        }
        return _field;
    }

    /**
     * 通过map 转换为指定对象
     * @param map
     * @param newObject
     * @return
     */
    public static Object fromMap(Map<String,Object> map ,Object newObject){
        for (Map.Entry entry : map.entrySet()){
            String key = (String) entry.getKey();
            Field field = getFiled(newObject.getClass(),  key);
            setValue(field, newObject, key, entry.getValue());
        }
        return newObject ;
    }


    /**
     * 检查字段 value字段是否 type一致
     * @param cls
     * @param filedName
     * @param value
     * @return
     */
    public static boolean checkType(Class cls, String filedName , Object value) {
        try{
            return getFiled(cls,filedName).getType().getTypeName().equals( value.getClass().getTypeName());
        }catch (Exception e){

        }
        return false;
    }


    /**
     * 获取 对象 内 全部 参数的 名称和值 ,通过Map 返回
     * @return
     */
    public static Map getAllNameAndValueMap(Object o){
        Map<String,Object> map = new HashMap<String, Object>();
        Class _class = o.getClass();
        Field[] fields = _class.getDeclaredFields();
        Field.setAccessible(fields, true);
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if("serialVersionUID".equals(field.getName())) continue;
            if(JIgnoreUtil.checkAnnotation(field)) continue;
            String name =  field.getName();
            Object value = getProperty(o,name);
            map.put(name,value);
        }
        //父类
        Class clazz = _class;
        while (!clazz.getSuperclass().toString().contains("java.lang.Object")){
            clazz = clazz.getSuperclass();
            Field[] fieldsSuper = clazz.getDeclaredFields();
            Field.setAccessible(fieldsSuper, true);
            for (int i = 0; i < fieldsSuper.length; i++) {
                Field field = fieldsSuper[i];
                if("serialVersionUID".equals(field.getName())) continue;
                if(JIgnoreUtil.checkAnnotation(field)) continue;
                String name =  field.getName();
                Object value = getProperty(o,name);
                map.put(name,value);

            }

        }

        return map;

    }

    /**
     * 拷贝到另外一个对象 ！ 字段相同的 会被 拷贝
     */
    public static Object copyTo(Object bean, Object newObject){
        Class _class =newObject.getClass();
        Field[] fields = _class.getDeclaredFields();
        Field.setAccessible(fields, true);
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            setValue(field,newObject,field.getName(),getProperty(bean,field.getName()));
        }
        //System.out.println("to fu lei ");
        //父类
        Class clazz = _class;
        while (!clazz.getSuperclass().toString().contains("java.lang.Object")){
            clazz = clazz.getSuperclass();
            Field[] fieldsSuper = clazz.getDeclaredFields();
            Field.setAccessible(fieldsSuper, true);
            for (int i = 0; i < fieldsSuper.length; i++) {
                Field field = fieldsSuper[i];
                setValue(field,newObject,field.getName(),getProperty(bean,field.getName()));
            }
        }

        return newObject;
    }



    private static void setValue(Field field, Object bean , String name ,Object v){
        try{
            if(field.getName().equals("serialVersionUID")) return;
            field.set(bean, v);
        }catch (Exception e){

        }
    }


    /**
     * 检查是否存在 该字段,会过滤 serialVersionUID
     * @param cls
     * @param name
     * @return
     */
    public static boolean existsFieldName(Class cls , String name){
        Field field =  getFiled(cls,name) ;
        if(field == null) return false;
        if(field.getName().equals("serialVersionUID")){
            return false;
        }
        return true;
    }


    /**
     * 通过对象创建一个新的对象！
     * @param cls
     * @return
     */
    public static Object instance(Class cls) throws IllegalAccessException, InstantiationException {

        return cls.newInstance();

    }


    /**
     * 将Class结构通过map 输出
     * @param _class
     * @return
     */
    public static Map<String,String> getStructsMap(Class _class){
        Map<String,String> map = new HashMap();
        Field[] fields = _class.getDeclaredFields();
        Field.setAccessible(fields, true);
        for (int i = 0; i < fields.length; i++) {

            Field field = fields[i];
            if("serialVersionUID".equals(field.getName())) continue;

            map.put(field.getName(),field.getType().getName());

        }
        //父类
        Class clazz = _class;
        while (!clazz.getSuperclass().toString().contains("java.lang.Object")){
            clazz = clazz.getSuperclass();
            Field[] fieldsSuper = clazz.getDeclaredFields();
            Field.setAccessible(fieldsSuper, true);
            for (int i = 0; i < fieldsSuper.length; i++) {
                Field field = fieldsSuper[i];
                if("serialVersionUID".equals(field.getName())) continue;
                map.put(field.getName(),field.getType().getName());
            }
        }
        return map;
    }


    private static String typeToDbtype(String typeName ,int length) throws JDataException{
        if(typeName.equals("java.lang.String")) return "VARCHAR("+length+")";
        if(typeName.equals("int") ) return "INT(11)";
        if(typeName.equals("long")) return "BIGINT(20)";
        if(typeName.equals("float")) return "FLOAT";
        if(typeName.equals("double")) return "DOUBLE";
        if(typeName.equals("java.lang.Integer")) return "INT(11)";
        if(typeName.equals("java.lang.Long")) return "BIGINT(20)";
        if(typeName.equals("java.lang.Float")) return "FLOAT";
        if(typeName.equals("java.lang.Double")) return "DOUBLE";
        if(typeName.contains("Date")) return "DATETIME";

        //f( field instanceof )

        throw new JDataException(typeName +  "  未定义或无法找到对应的数据库映射类型！");
    }


    public static Map<String,Map<String,Object>> getStructsMapForDbTable(Class _class){
        Map<String,Map<String,Object>> map = new HashMap();
        Field[] fields = _class.getDeclaredFields();
        Field.setAccessible(fields, true);
        for (int i = 0; i < fields.length; i++) {
            Map jc = new HashMap();

            Field field = fields[i];
            if("serialVersionUID".equals(field.getName())) continue;
            if(JIgnoreUtil.checkAnnotation(field)) continue;

            JColumn jColumn = JColumnUtil.getAnnotation(field);
            if(jColumn != null){
                jc.put("type",typeToDbtype(field.getType().getTypeName(),jColumn.length()));
                jc.put("pk",jColumn.pk());
                jc.put("unique",jColumn.unique());
                jc.put("nullable", (jColumn.index()||jColumn.unique()||jColumn.pk())?false:jColumn.nullable());
                jc.put("index" , jColumn.index());
                map.put(field.getName() , jc);
            }else{
                jc.put("type",typeToDbtype(field.getType().getTypeName(), 255));
                jc.put("pk",false);
                jc.put("unique",false);
                jc.put("nullable",true);
                jc.put("index" , false);
                map.put(field.getName() , jc);
                jColumn = null;
                jc = null;

            }
        }
        //父类
        Class clazz = _class;
        while (!clazz.getSuperclass().toString().contains("java.lang.Object")){
            clazz = clazz.getSuperclass();
            Field[] fieldsSuper = clazz.getDeclaredFields();
            Field.setAccessible(fieldsSuper, true);
            for (int i = 0; i < fieldsSuper.length; i++) {
                Map jcx = new HashMap();
                Field field = fieldsSuper[i];
                if("serialVersionUID".equals(field.getName())) continue;
                if(JIgnoreUtil.checkAnnotation(field)) continue;
                JColumn jColumn = JColumnUtil.getAnnotation(field);
                if(jColumn != null){
                    jcx.put("type",typeToDbtype(field.getType().getTypeName(),jColumn.length()));
                    jcx.put("pk",jColumn.pk());
                    jcx.put("unique",jColumn.unique());
                    jcx.put("nullable",(jColumn.index()||jColumn.unique()||jColumn.pk())?false:jColumn.nullable());
                    jcx.put("index" , jColumn.index());
                    map.put(field.getName() , jcx);
                }else{
                    jcx.put("type",typeToDbtype(field.getType().getTypeName(), 255));
                    jcx.put("pk",false);
                    jcx.put("unique",false);
                    jcx.put("nullable",true);
                    jcx.put("index" , false);
                    map.put(field.getName() , jcx);
                    jcx = null ;
                    jColumn = null;
                }
            }
        }
        return map;
    }


}
