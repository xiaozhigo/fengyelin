package com.example.controller;

import com.example.dto.User;
import com.example.biz.UserBiz;
import com.example.fengcommon.annotation.NoRepeatSubmit;
import com.example.fengcommon.config.kafka.KafkaProducer;
import com.example.fengcommon.config.kafka.KafkaTopic;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
@Api(tags = "用户接口")
public class UserController {

    @Autowired
    KafkaProducer kafkaProducer;

    @Autowired
    private UserBiz userBiz;

    @ApiOperation(value = "查询客用户信息")
    @RequestMapping("/getUserInfo/{id}")
    @ResponseBody
    public User getUserInfo(@PathVariable("id") Integer id){
        User userInfo = userBiz.getUserInfo(id);
        return userInfo;
    }

    @ApiOperation(value = "查询Redis客用户信息")
    @RequestMapping("/getUserInfoRedis/{custId}")
    @ResponseBody
    public String getUserInfoRedis(@PathVariable("custId") String custId){
        String userInfoRedis = userBiz.getUserInfoRedis(custId);
        return userInfoRedis;
    }

    @NoRepeatSubmit(lockKey="custId",lockTime = 60)
    @ApiOperation(value = "查询Redis客用户信息")
    @RequestMapping("/getUserInfoRepeatSubmit/{custId}")
    @ResponseBody
    public String getUserInfoRepeatSubmit(@PathVariable("custId") String custId){
        String userInfoRedis = userBiz.getUserInfoRedis(custId);
        return userInfoRedis;
    }

    @ApiOperation(value = "处理发送kafka")
    @RequestMapping("/getUserInfoKafka/{custId}")
    @ResponseBody
    public void getUserInfoKafka(@PathVariable("custId") String custId){
        kafkaProducer.send(KafkaTopic.FENG_TOPIC,custId);
    }
}
