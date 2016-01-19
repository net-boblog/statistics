package com.xiaoluo.statistics.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaoluo.statistics.dao.DictDao;
import com.xiaoluo.statistics.entity.Dict;
import com.xiaoluo.statistics.search.SearchParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Caedmon on 2016/1/14.
 */
@Service
public class DictService {
    @Autowired
    private DictDao dictDao;

    public int update(Dict dict){
        return dictDao.update(dict);
    }
    public int del(int id){
        return dictDao.del(id);
    }
    public List<Dict> find(String ids,int type,String description){
        return dictDao.find(ids,type,description);
    }
}
