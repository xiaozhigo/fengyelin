package com.example.controller;

import com.example.User;
import com.example.biz.UserBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserBiz userBiz;

    @RequestMapping("/getUserInfo/{id}")
    @ResponseBody
    public User getUserInfo(@PathVariable("id") Integer id){
        User userInfo = userBiz.getUserInfo(id);
        return userInfo;
    }
}
