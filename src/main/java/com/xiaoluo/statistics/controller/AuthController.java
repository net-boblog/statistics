package com.xiaoluo.statistics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Caedmon on 2016/1/20.
 */
@Controller
@RequestMapping("/auth")
public class AuthController extends RestBaseController{
    @Autowired
    private String authName;
    @Autowired
    private String authPassword;
    @RequestMapping("/dologin")
    public String doLogin(String name, String password, HttpServletRequest request){

        if(authName.equals(name)&&password.equals(authPassword)){
            request.getSession().setAttribute("SESSION_USER",name);
            return "redirect:/template/list";

        }else{
            return "forward:/auth/login";
        }
    }
    @RequestMapping("/login")
    public String login(){
        return "login";
    }
}
