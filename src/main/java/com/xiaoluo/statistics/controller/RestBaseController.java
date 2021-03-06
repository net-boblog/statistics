package com.xiaoluo.statistics.controller;

import com.xiaoluo.constant.AllRetCode;
import com.xiaoluo.exception.ServiceException;
import com.xiaoluo.statistics.exception.StatisticException;
import com.xiaoluo.statistics.vo.ApiResult;
import com.xl.rpc.exception.RemoteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomMapEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 2015/12/24.
 */
public class RestBaseController {
    private static final Logger log= LoggerFactory.getLogger(RestBaseController.class);
    @InitBinder
    public void init(HttpServletRequest request,ServletRequestDataBinder binder) throws UnsupportedEncodingException {

        request.setCharacterEncoding("UTF-8");
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor=new CustomDateEditor(format,true);
        CustomMapEditor mapEditor=new CustomMapEditor(HashMap.class);
        binder.registerCustomEditor(Date.class, editor);
        binder.registerCustomEditor(Map.class,mapEditor);
    }
    @ExceptionHandler
    public @ResponseBody
    String handleException(HttpServletRequest request,Throwable ex){
        ApiResult result=new ApiResult();

        if(ex instanceof StatisticException){
            StatisticException se=(StatisticException)ex;
            result.setCode(AllRetCode.CODE_SYSTEM_ERROR);
            log.error("Statistics error {} :{}",request.getRequestURL(),se.getError());
            result.setMsg(se.getError());
        }else{
            ex.printStackTrace();
            log.error("Api system error {}",request.getRequestURL(),ex);
            result.setCode(AllRetCode.CODE_SYSTEM_ERROR);
            result.setMsg("请求处理异常:"+ex.getMessage());
        }
        return result.toString();
    }
}
