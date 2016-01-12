package com.xiaoluo.statistics.vo;

import java.util.List;

/**
 * Created by Caedmon on 2016/1/7.
 */
public class UserBindResult {
    private List<String> oldUids;
    private String newUid;

    public List<String> getOldUids() {
        return oldUids;
    }

    public void setOldUids(List<String> oldUids) {
        this.oldUids = oldUids;
    }

    public String getNewUid() {
        return newUid;
    }

    public void setNewUid(String newUid) {
        this.newUid = newUid;
    }
    public boolean needUpdate(){
        if(oldUids==null||oldUids.isEmpty()){
            return false;
        }
        if(oldUids.size()>1){
            return true;
        }else{
            return !oldUids.get(0).equals(newUid);
        }
    }
}
