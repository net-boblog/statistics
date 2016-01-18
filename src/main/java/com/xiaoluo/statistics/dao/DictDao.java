package com.xiaoluo.statistics.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Created by Caedmon on 2016/1/14.
 */
@Component
public class DictDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public int update(int type,String key,String description){
        String sql=" REPLACE INSERT INTO t_event (type,key,value) VALUES(?,?,?)";
        return jdbcTemplate.update(sql,new Object[]{type,key, description});
    }
    public int del(int type,String key){
        String sql="DELETE FROM t_event where type=? and key=?";
        return jdbcTemplate.update(sql,new Object[]{type,key});
    }
    public Map<String,Object> find(int type,String key,String description){
        StringBuilder sql=new StringBuilder("SELECT * FROM t_event where type= ").append(type);
        if(!StringUtils.isEmpty(key)){
            sql.append(" and key ='").append(key).append("'");
        }
        if(!StringUtils.isEmpty(description)){
            sql.append(" and description ='").append(description).append("'");
        }
        return jdbcTemplate.queryForMap(sql.toString());
    }
}
