package com.xiaoluo.statistics.dao;

import com.xiaoluo.domain.DataPage;
import com.xiaoluo.statistics.dao.filter.SearchTemplateFilter;
import com.xiaoluo.statistics.entity.SearchTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Caedmon on 2016/1/15.
 */
@Component
public class SearchTemplateDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private RowMapper<SearchTemplate> rowMapper=new RowMapper<SearchTemplate>() {
        @Override
        public SearchTemplate mapRow(ResultSet resultSet, int i) throws SQLException {
            SearchTemplate template=new SearchTemplate();
            template.setId(resultSet.getInt("id"));
            template.setType(resultSet.getInt("type"));
            template.setName(resultSet.getString("name"));
            template.setParams(resultSet.getString("params"));
            template.setInterval(resultSet.getInt("interval"));
            template.setUnit(resultSet.getInt("unit"));
            return template;
        }
    };
    public SearchTemplate update(SearchTemplate template){
        String sql="REPLACE INSET INTO t_search_template (id,type,name,params,interval,unit) VALUES(?,?,?,?,?,?)";
//        return jdbcTemplate.update(sql,new Object[]{
//                template.getId(),template.getType(),template.getName(),template.getParams(),
//                template.getInterval(),template.getUnit()
//        });
        return null;
    }
    public DataPage<SearchTemplate> find(SearchTemplateFilter filter){
        StringBuilder countsql=new StringBuilder("SELECT count(*) FROM t_search_template WHERE 1=1");
        StringBuilder sql=new StringBuilder("SELECT * FROM t_search_template WHERE 1=1 ");
        StringBuilder conditon=new StringBuilder();
        if(!StringUtils.isEmpty(filter.getName())){
            conditon.append(" and name like '%").append(filter.getName()).append("'");
        }
        if(filter.getType()!=0){
            conditon.append(" and type =").append(filter.getType());
        }
        if(filter.getOrderType()!=null){
            conditon.append(" ").append(filter.getOrderType()).append(" ");
        }
        if(filter.getPageSize()>0&&filter.getPageIndex()>=1){
            conditon.append(" limit ").append((filter.getPageIndex()-1)*filter.getPageSize()).append(",")
                    .append(filter.getPageSize());
        }
        long total=jdbcTemplate.queryForLong(countsql.append(conditon).toString());
        DataPage<SearchTemplate> page=new DataPage(jdbcTemplate.query(sql.toString(),rowMapper),total,filter.getPageIndex(),filter.getPageSize());
        return page;
    }
    public SearchTemplate find(int id){
        String sql="SELECT * FROM t_search_template where id="+id;
        return jdbcTemplate.queryForObject(sql,rowMapper);
    }
}
