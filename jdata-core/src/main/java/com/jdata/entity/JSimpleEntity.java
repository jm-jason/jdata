package com.jdata.entity;

import com.jdata.annotation.JColumn;

import java.io.Serializable;

public class JSimpleEntity implements Serializable{
    private static final long serialVersionUID = 8617483897232702082L;
    @JColumn(pk = true,length = 32)
    String id ;

    @JColumn(length = 4)
    String isDel = "NO" ;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }
}
