package com.xiaoluo.statistics.service;

import com.xiaoluo.statistics.dao.DictDao;
import com.xiaoluo.statistics.entity.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Caedmon on 2016/1/14.
 */
@Service
public class DictService {
    @Autowired
    private DictDao dictDao;
    private Map<Integer,Dict> dictCache=new HashMap<Integer, Dict>();
    @PostConstruct
    public void init(){
        List<Dict> allDict=find(null,0,null);
        for(Dict dict:allDict){
            dictCache.put(dict.getId(),dict);
        }
    }
    public int update(Dict dict){
        int i=dictDao.update(dict);
        if(i==1){
            dictCache.put(dict.getId(),dict);
        }
        return i;
    }
    public int del(int id){
        int i=dictDao.del(id);
        if(i==1){
            dictCache.remove(id);
        }
        return i;
    }
    public List<Dict> find(String ids,int type,String description){
        return dictDao.find(ids,type,description);
    }
    public Dict get(int id){
        Dict dict=dictCache.get(id);
        if(dict==null){
            dict=dictDao.findOne(id);
        }
        return dict;
    }
}
