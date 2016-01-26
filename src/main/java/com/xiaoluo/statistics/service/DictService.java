package com.xiaoluo.statistics.service;

import com.google.common.collect.Lists;
import com.xiaoluo.statistics.dao.DictDao;
import com.xiaoluo.statistics.entity.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by Caedmon on 2016/1/14.
 */
@Service
public class DictService {
    @Autowired
    private DictDao dictDao;
    private Map<String,Dict> dictCache =new HashMap<String, Dict>();
    @PostConstruct
    public void init(){
        List<Dict> allDict=find(null,0,null);
        for(Dict dict:allDict){
            dictCache.put(dict.getId(),dict);
        }
    }
    public int insert(Dict dict){
        if(StringUtils.isEmpty(dict.getDescription())){
            dict.setDescription(dict.getId());
        }
        int i=dictDao.insert(dict);
        if(i==1){
            dictCache.put(dict.getId(),dict);
        }
        return i;
    }
    public int update(String id,String description){
        int i= dictDao.update(id,description);
        if(i==1){
            Dict dict=get(id);
            dictCache.put(dict.getId(),dict);
        }
        return i;
    }
    public int del(int id){
        int i= dictDao.del(id);
        if(i==1){
            dictCache.remove(id);
        }
        return i;
    }
    public List<Dict> find(String ids,int type,String description){
        return dictDao.find(ids,type,description);
    }
    public Collection<Dict> findAll(){
        return dictCache.values();
    }
    public Dict get(String id){
        Dict dict= dictCache.get(id);
        if(dict==null){
            dict= dictDao.findOne(id);
        }
        return dict;
    }
}
