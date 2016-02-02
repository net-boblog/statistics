package com.xiaoluo.statistics.dao;

import com.xiaoluo.statistics.constant.DictType;
import com.xiaoluo.statistics.entity.Dict;
import com.xiaoluo.statistics.exception.StatisticException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
            dict.setId(resultSet.getString("id"));
            dict.setType(resultSet.getInt("type"));
            dict.setDescription(resultSet.getString("description"));
            return dict;
        }
    };
    public int insert(final Dict dict){
        Dict one=findOne(dict.getId());
        if(one!=null){
            throw new StatisticException("字典已存在");
        }
        String sql="INSERT INTO t_dict (id,type,description) VALUES (?,?,?)";
        return jdbcTemplate.update(sql,new Object[]{dict.getId(),dict.getType(),dict.getDescription()});
    }
    public int update(final String id,String description){
        String sql="UPDATE t_dict SET description=? WHERE id=?";
            return jdbcTemplate.update(sql,new Object[]{description,id});

    }
    public int del(String id){
        String sql="DELETE FROM t_dict where id=?";
        return jdbcTemplate.update(sql,new Object[]{id});
    }
    public List<Dict> find(String ids,int type, String description){
        StringBuilder sql=new StringBuilder("SELECT * FROM t_dict where 1=1");
        if(!StringUtils.isEmpty(ids)){
            sql.append(" and id in(");
            String[] idsArray=ids.split(",");
            for(int i=0;i<idsArray.length;i++){
                String id=idsArray[i];
                sql.append("'").append(id).append("'");
                if(i<idsArray.length-1){
                    sql.append(",");
                }
            }
            sql.append(")");
        }
        if(type!=0){
            sql.append(" and type =").append(type);
        }
        if(!StringUtils.isEmpty(description)){
            sql.append(" and description like '%").append(description).append("%'");
        }

        return jdbcTemplate.query(sql.toString(),rowMapper);
    }
    public Dict findOne(String id){
        String sql="SELECT * FROM t_dict where id='"+id+"'";
        List<Dict> dicts=jdbcTemplate.query(sql,rowMapper);
        if(dicts.size()>1){
            throw new StatisticException("字典ID重复 id="+id);
        }
        if(dicts.size()==1){
            return dicts.get(0);
        }else{

            return null;
        }
    }
}
