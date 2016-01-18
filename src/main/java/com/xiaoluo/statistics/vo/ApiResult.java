package com.xiaoluo.statistics.vo;

import com.alibaba.fastjson.JSON;

/**
 * Created by Administrator on 2015/11/6.
 */
public class ApiResult {
    private int code;
    private Object data;
    private String msg;
    public static final ApiResult EMPTY_RESULT=new ApiResult();
    public ApiResult(int code, Object data, String msg){
        this.code=code;
        this.data=data;
        this.msg=msg;
    }

    public ApiResult(Object data) {
        this(0,data,"success");
    }
    public ApiResult(){
        this(0,"","success");
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String toString(){
        return JSON.toJSONString(this);
    }
}
