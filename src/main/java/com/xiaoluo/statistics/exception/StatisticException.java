package com.xiaoluo.statistics.exception;

/**
 * Created by Administrator on 2016/1/15.
 */
public class StatisticException extends RuntimeException{
    private Object[] params;
    private String error;
    public StatisticException(String error) {
        this.error=error;
    }
    public StatisticException(String error,Object[] params) {
        this(error);
        this.params=params;
        if(params!=null){
            for(int i=0;i<params.length;i++){
                error=error.replaceFirst("#"+(i+1),String.valueOf(params[i]));
            }
        }
    }

    public String getError() {
        return error;
    }

    @Override
    public  Throwable fillInStackTrace() {
        return this;
    }
}
