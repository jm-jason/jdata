package com.jdata.util;

import java.io.Serializable;

/**
 * 分页工具
 */
public class JPageUtil implements Serializable {

    private static final long serialVersionUID = -4579214538746919961L;
    private long pageNo = 1 ;
    private long pageSize =20 ;

    String sortParam = " order by id desc";

    boolean isAsc ;


    public JPageUtil(){
    }


    public static JPageUtil instance(int pageSize){
        JPageUtil jPageUtil =  new JPageUtil();
        jPageUtil.pageSize = pageSize;
        return jPageUtil;
    }

    public long nowPage(){
        return this.pageNo;
    }

    public long pageSize(){
        return this.pageSize;
    }

    public String sortInfo(){
        return this.sortParam;
    }


    public JPageUtil pageNo(int pageNo){
        if(pageNo <1 ){
            pageNo =1;
        }
        this.pageNo = pageNo;
        return this;
    }

    public JPageUtil nextPage(){
        this.pageNo ++;
        return this;
    }

    public JPageUtil prePage(){
        this.pageNo -- ;
        if(pageNo < 1) {
            this.pageNo =1 ;
        }
        return this;
    }


    /**
     * 根据key 顺序
     * @param key
     */
    public JPageUtil sortAsc(String key){
        this.sortParam =  "order by  "+key + " asc";
        return this;
    }

    /**
     * 根据key 倒叙
     * @param key
     */
    public JPageUtil sortDesc(String key){
        this.sortParam =  "order by  "+key + " desc";
        return this;
    }


    public long start(){
        return (pageNo -1) * pageSize;
    }

    public long limit(){
        return pageSize;
    }


    public String endSQL(){
        return  sortParam + " limit " + start() +" ," + limit() ;
    }
}
