package com.example.biz;

import com.example.User;
import com.example.interfaces.UserServer;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
public class UserBiz {

    @DubboReference(version = "5.2.0")
    private UserServer userServer;

    public User getUserInfo(Integer id) {
        return userServer.getUserInfo(id);
    }
}
