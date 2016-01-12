package com.xiaoluo.statistics.controller;

import com.xiaoluo.constant.AllRetCode;
import com.xiaoluo.exception.ServiceException;
import com.xiaoluo.statistics.vo.ApiResult;
import com.xl.rpc.exception.RemoteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        binder.registerCustomEditor(Date.class, editor);
    }
    @ExceptionHandler
    public @ResponseBody
    String handleException(HttpServletRequest request,Throwable ex){
        ApiResult result=new ApiResult();

        if(ex instanceof ExecutionException &&ex.getCause() instanceof RemoteException &&ex.getCause().getCause() instanceof ServiceException){
            ServiceException se=(ServiceException)(ex.getCause().getCause());
            result.setCode(se.getErrorCode());
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
