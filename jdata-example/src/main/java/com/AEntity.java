package com;

import com.jdata.annotation.JColumn;
import com.jdata.annotation.JEntity;
import com.jdata.entity.JSimpleEntity;

import java.util.Date;

@JEntity(name = "a_table",dataSource = "ds1")
public class AEntity extends JSimpleEntity {

    @JColumn(length = 32,unique = true)
    String name ;

    Date initDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getInitDate() {
        return initDate;
    }

    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }
}
