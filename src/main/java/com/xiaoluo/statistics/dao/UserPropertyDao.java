package com.xiaoluo.statistics.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Caedmon on 2016/1/7.
 */
@Component
public class UserPropertyDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public String getUidByProperty(String property, String value){
        StringBuilder sql=new StringBuilder("SELECT uid from t_user_property ");
        sql.append(buildSqlCondition(property,value));
        List<String> uids=jdbcTemplate.queryForList(sql.toString(), String.class);
        if(uids==null||uids.isEmpty()){
            return null;
        }
        return uids.get(uids.size() - 1);
    }
    private String buildSqlCondition(String property,String value){
        StringBuilder sql=new StringBuilder(" where 1=1 ");
        if(property.equals("app_cookie")){
            sql.append(" and app_cookie='");
        }
        if(property.equals("h5_cookie")){
            sql.append(" and h5_cookie ='");
        }
        if(property.equals("pc_cookie")){
            sql.append(" and pc_cookie ='");
        }
        if(property.equals("wx_open_id")){
            sql.append(" and wx_open_id ='");
        }
        if(property.equals("app_imei")){
            sql.append(" and app_imei='");
        }
        sql.append(value).append("'");
        return sql.toString();
    }
    public int updateBindUid(String uid,String property,String value){
        String condition=buildSqlCondition(property,value);
        String sql="UPDATE t_user_property set uid='"+uid+"'"+condition;
        return jdbcTemplate.update(sql);
    }
    public int updateOldUid(String oldUid,String newUid){
        String sql="UPDATE t_user_property set uid=? where uid = ?";
        return jdbcTemplate.update(sql, new Object[]{oldUid, newUid});
    }
    public int updatePhone(String phone,String property,String value){
        String sql="UPDATE t_user_property set phone='"+phone+"' where "+property+"='"+ value +"'";
        return jdbcTemplate.update(sql);
    }
    public int updateOldUid(List<String> oldUids,String newUid){
        StringBuilder uidsBuilder=new StringBuilder();
        for(int i=0;i<oldUids.size();i++){
            uidsBuilder.append("'").append(oldUids.get(i)).append("'");
            if(i<oldUids.size()-1){
               uidsBuilder.append(",");
            }
        }
        String sql="UPDATE t_user_property set uid='"+newUid+"' where uid IN ("+uidsBuilder.toString()+")";
        return jdbcTemplate.update(sql);
    }
    public List<String> getUidByPhone(String phone){
        String sql="SELECT DISTINCT(uid) FROM t_user_property where phone='"+phone+"'";
        return jdbcTemplate.queryForList(sql,String.class);
    }
    public void insert(String uid,String phone,String property,String value){
        StringBuilder sql=new StringBuilder("INSERT INTO t_user_property (uid,phone,"+property+") VALUES (?,?,?)");

        jdbcTemplate.update(sql.toString(),new Object[]{uid,phone,value});
    }
    private static boolean isEmpty(String s){
        return s==null||s.trim().equals("");
    }
}
