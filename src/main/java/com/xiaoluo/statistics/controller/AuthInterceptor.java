package com.xiaoluo.statistics.controller;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Dlen on 2015/8/19.
 */
public class AuthInterceptor implements HandlerInterceptor {

    private static final String[] IGNORE_URI = {"/login.jsp", "/login","/dologin","/resources","/report/insert"};
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        boolean flag = false;
        String url = request.getRequestURL().toString();

        for (String s : IGNORE_URI) {
            if (url.contains(s)) {
                flag = true;
                break;
            }
        }
        String s="{\"channel\":\"push_sms\",\"identity_type\":\"pc_cookie\",\"identity_value\":\"bbdf82a7-331d-3f4d-988d-51e79178cefc\",\"prefix_page\":\"http:\\/\\/www.xiaoluo.com\\/company\\/list\",\"current_page\":\"http:\\/\\/www.xiaoluo.com\\/company\\/detail\\/1440644480194X3M71GI50qDH?src=pcadlist\",\"terminal\":\"pc\",\"event\":\"detail_pv\",\"extra\":\"[object Object]\",\"ip\":\"119.122.246.154\",\"version\":\"\"}";
        if (!flag) {
            Object obj = request.getSession().getAttribute("SESSION_USER");
            if(obj != null ){
                flag = true;
            }else{
                if(null==request.getSession(false)){
                    if(true==request.getSession(true).isNew()){
                    }
                    else{
                        System.out.println("session已经过期");
                    }
                }
                response.sendRedirect(request.getContextPath()+"/auth/login");
                flag = false;
            }

        }
        return flag;


    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    boolean isAjax(HttpServletRequest request){
        return  (request.getHeader("X-Requested-With") != null  && "XMLHttpRequest".equals( request.getHeader("X-Requested-With").toString())   ) ;
    }
}
