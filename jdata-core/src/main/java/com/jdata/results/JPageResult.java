package com.jdata.results;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Jpage对象，存储mysql的分页信息 ，包含count 信息
 */
public class JPageResult implements Serializable {
    private static final long serialVersionUID = 5366631948430973478L;

    private long pageNo ;
    private long pageSize ;
    private long total ;
    private long totalPage ;

    private List<Object> data = null ;


    public JPageResult(long pageNo,long pageSize,long total , List<Object> data){
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = total;
        if(data == null){
            data = new ArrayList<Object>();
        }
        this.data = data;
        this.totalPage = total / pageSize + (total % pageSize > 0 ? 1 : 0);
    }

    public long getPageNo() {
        return pageNo;
    }

    public long getPageSize() {
        return pageSize;
    }

    public long getTotal() {
        return total;
    }

    public List<Object> getData() {
        return data;
    }

    public long getTotalPage() {
        return totalPage;
    }
}
