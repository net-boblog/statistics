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
    public int del(int type,String key){
        return dictDao.del(type,key);
    }
    public List<Dict> find(int type, String key, String description){
        return dictDao.find(type,key,description);
    }

    public static void main(String[] args) {
        String text="{\"currentPages\":\"login\",\"prefixPages\":[\"page01\",\"page02\",\"page03\"],\"events\":[\"login\"],\"unit\":\"3\",\"terminals\":[\"android\",\"ios\"],\"interval\":\"1\",\"keyWords\":\"\",\"channels\":[\"baidu\",\"qq\"],\"termsCountField\":\"uid\",\"minTermsCount\":\"1\"}";
        JSONObject jsonObject=JSON.parseObject(text);
        jsonObject.put("unit",jsonObject.getIntValue("unit"));
        jsonObject.put("interval",jsonObject.getIntValue("interval"));
        jsonObject.put("minTermsCount",jsonObject.getIntValue("minTermsCount"));
        System.out.println(jsonObject.toJSONString());
        SearchParams searchParams=JSON.parseObject(jsonObject.toJSONString(),SearchParams.class);
        System.out.println(searchParams);
    }
}
