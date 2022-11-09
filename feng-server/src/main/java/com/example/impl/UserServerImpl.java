package com.example.impl;

import com.example.dto.User;
import com.example.interfaces.UserServer;
import com.example.mapper.UserMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@DubboService(interfaceClass = UserServer.class,timeout = 10000,version = "5.2.0")
public class UserServerImpl implements UserServer {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserInfo(Integer id) {
        return userMapper.getUserById(id);
    }
}
