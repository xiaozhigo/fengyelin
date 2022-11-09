package com.example.biz;

import com.example.dto.User;
import com.example.fengcommon.utils.RedisUtils;
import com.example.interfaces.UserServer;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class UserBiz {

    @Lazy
    @DubboReference(version = "5.2.0")
    private UserServer userServer;

    public User getUserInfo(Integer id) {
        return userServer.getUserInfo(id);
    }

    public String getUserInfoRedis(String custId) {
        return RedisUtils.getString("cust_custinfo"+custId);
    }
}
