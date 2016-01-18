package com.xiaoluo.statistics.service;

import com.xiaoluo.statistics.dao.DictDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Caedmon on 2016/1/14.
 */
@Service
public class DictService {
    @Autowired
    private DictDao dictDao;
    public int update(int type,String key,String description){
        return dictDao.update(type,key,description);
    }
    public int del(int type,String key){
        return dictDao.del(type,key);
    }
    public Map<String,Object> find(int type,String key,String description){
        return dictDao.find(type,key,description);
    }
}
