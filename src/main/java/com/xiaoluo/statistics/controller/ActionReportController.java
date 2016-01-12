package com.xiaoluo.statistics.controller;

import com.alibaba.fastjson.JSONObject;
import com.xiaoluo.statistics.service.ActionReportService;
import com.xiaoluo.statistics.service.UserService;
import com.xiaoluo.statistics.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by Caedmon on 2015/12/24.
 */
@Controller
@RequestMapping("/report")
public class ActionReportController extends RestBaseController{
    @Autowired
    private ActionReportService actionReportService;
    @Autowired
    private UserService userService;
    @RequestMapping("/multiSearch")
    public @ResponseBody String multiSearch(SearchParams params) throws Exception{
        if(params.getFrom()==null){
            throw new NullPointerException("From time is null");
        }
        if(params.getTo()==null){
            params.setTo(new Date());
        }
        List<SimpleStatResult> list=actionReportService.multiSearch(params);
        List<Long> pvs=new ArrayList<Long>();
        List<Double> uvs=new ArrayList<Double>();
        List<Double> ips=new ArrayList<Double>();
        List<String> times=new ArrayList<String>();
        List<Long> actives=new ArrayList<Long>();
        for(SimpleStatResult result:list){
            pvs.add(result.getPv());
            uvs.add(result.getUv());
            ips.add(result.getIp());
            actives.add(result.getActiveCount());
            times.add("\""+result.getTo()+"\"");
        }
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("pvs", Arrays.toString(pvs.toArray()));
        jsonObject.put("uvs",Arrays.toString(uvs.toArray()));
        jsonObject.put("ips",Arrays.toString(ips.toArray()));
        jsonObject.put("times",Arrays.toString(times.toArray()));
        jsonObject.put("actives",Arrays.toString(actives.toArray()));
        return jsonObject.toJSONString();
    }
    @RequestMapping("/rebuild")
    public @ResponseBody String rebuild(HttpServletRequest request) throws Exception{
        if(request.getRemoteHost().endsWith("localhost")||request.getRemoteHost().endsWith("127.0.0.1")){
            return actionReportService.rebuild().toString();
        }
        return "Auth fail";

    }
    @RequestMapping("/stat")
    public String statistics(HttpServletRequest request,HttpServletResponse response){
        Cookie cookie=new Cookie("id", UUID.randomUUID().toString());
        response.addCookie(cookie);
        return "report_stat";
    }
    @RequestMapping("/insert")
    public @ResponseBody String insert(FullActionReport report,HttpServletRequest request) throws Exception{
        report.setProperty("pc_cookie");
        report.setTime(System.currentTimeMillis());
        report.setTerminal("pc");
        report.setIp("127.0.0.1");
        String value=null;
        for(Cookie cookie:request.getCookies()){
            if(cookie.getName().equals("id")){
                value=cookie.getValue();
            }
        }
        if(value==null){
            value=UUID.randomUUID().toString();
        }
        report.setValue(value);
        actionReportService.insert(report);
        return null;
    }
}
