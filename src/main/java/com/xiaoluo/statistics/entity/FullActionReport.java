package com.xiaoluo.statistics.entity;

import com.xiaoluo.statistics.entity.ActionReport;

/**
 * Created by Caedmon on 2016/1/7.
 */
public class FullActionReport extends ActionReport {
    private String identity_type;
    private String identity_value;

    public String getIdentity_type() {
        return identity_type;
    }

    public void setIdentity_type(String identity_type) {
        this.identity_type = identity_type;
    }

    public String getIdentity_value() {
        return identity_value;
    }

    public void setIdentity_value(String identity_value) {
        this.identity_value = identity_value;
    }
}
