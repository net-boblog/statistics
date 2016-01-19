package com.xiaoluo.statistics.dao;

import com.xiaoluo.statistics.entity.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Caedmon on 2016/1/14.
 */
@Component
public class DictDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private RowMapper<Dict> rowMapper=new RowMapper() {
        @Override
        public Dict mapRow(ResultSet resultSet, int i) throws SQLException {
            Dict dict=new Dict();
            dict.setType(resultSet.getInt("type"));
            dict.setKey(resultSet.getString("key"));
            dict.setDescription(resultSet.getString("description"));
            return dict;
        }
    };
    public int update(Dict dict){
        String sql=" REPLACE INSERT INTO t_dict (type,key,value) VALUES(?,?,?)";
        return jdbcTemplate.update(sql,new Object[]{dict.getType(),dict.getKey(), dict.getDescription()});
    }
    public int del(int type,String key){
        String sql="DELETE FROM t_dict where type=? and key=?";
        return jdbcTemplate.update(sql,new Object[]{type,key});
    }
    public List<Dict> find(int type, String key, String description){
        StringBuilder sql=new StringBuilder("SELECT * FROM t_dict where type= ").append(type);
        if(!StringUtils.isEmpty(key)){
            sql.append(" and key like '%").append(key).append("%'");
        }
        if(!StringUtils.isEmpty(description)){
            sql.append(" and description like '%").append(description).append("%'");
        }

        return jdbcTemplate.query(sql.toString(),rowMapper);
    }
}
