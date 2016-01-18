package com.xiaoluo.statistics.service;

import com.xiaoluo.statistics.constant.IdentityType;
import com.xiaoluo.statistics.dao.UserPropertyDao;
import com.xiaoluo.statistics.vo.UserBindResult;
import com.xl.tool.codec.UUIDKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Caedmon on 2016/1/7.
 */
@Service
public class UserService {
    @Autowired
    private UserPropertyDao userPropertyDao;
    public String getUid(String phone,IdentityType identityType,String value){
        String uid= userPropertyDao.getUidByProperty(identityType,value);
        //如果为空,则是一个新的设备上报
        if(uid==null){
            uid= UUIDKit.base58Uuid();
            userPropertyDao.insert(uid,phone,identityType,value);
        }
        return uid;
    }
    public UserBindResult bindUser(String phone,IdentityType identityType,String value){
        //注册或者登陆时才会触发
        UserBindResult result=new UserBindResult();
        if(phone!=null&&!phone.trim().equals("")){
            //旧的uid
            List<String> oldUids= userPropertyDao.getUidByPhone(phone);
            String newUid= getUid(phone,identityType,value);
            result.setOldUids(oldUids);
            result.setNewUid(newUid);
            if(result.needUpdate()){
                userPropertyDao.updateOldUid(oldUids,newUid);
            }
            userPropertyDao.updatePhone(phone,identityType,value);
            return result;
        }else{
            throw new NullPointerException("Phone can not be null");
        }

    }
}
