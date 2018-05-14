package com.jdata.results;

import java.io.Serializable;

public class JWriteResult implements Serializable{
    private static final long serialVersionUID = -8696892783045375740L;
    private boolean execute =false;
    private int rows = 0 ;
    private String msg = null;

    public static JWriteResult result(int rows ){
        if(rows < 1){
            return fail("没有影响任何记录！");
        }
        JWriteResult result = new JWriteResult();
        result.rows  = rows ;
        result.execute = true;
        return result;
    }


    public static JWriteResult fail(String msg){
        JWriteResult result = new JWriteResult();
        result.msg = msg;
        return result;
    }

}
