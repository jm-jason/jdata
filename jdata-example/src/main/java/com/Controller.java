package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("test")
@RestController
public class Controller {




    @Autowired
    ADao aDao;
    @Autowired
    ApplicationContext applicationContext ;



    @ResponseBody
    @RequestMapping("/insert")
    public Map updates(String id){
        AEntity a = new AEntity();
        a.setId(id);
        a.setName("hello test");
        try{
            aDao.insert( a) ;
        }catch (Exception e){
            e.printStackTrace();
        }

        return new HashMap();

    }

    @ResponseBody
    @RequestMapping("/get")
    public AEntity dos(String id){
        System.out.println("get _" + id);
        AEntity a = aDao.get(id);
        return a;
    }


}
