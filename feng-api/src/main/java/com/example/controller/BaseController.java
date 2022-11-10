package com.example.controller;

import com.example.fengcommon.map.FunctionMap;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: hehai
 * @since: 2022-09-26  13:27
 * @description: 对外统一接受请求接口
 */
@Slf4j
@RestController
@RequestMapping("/base/")
@Api(tags = "base")
public class BaseController {


    /**
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/doRequest")
    public void doRequest(HttpServletRequest request, HttpServletResponse response,
                          @RequestHeader(value = "ProcessCode",required = true) String ProcessCode) throws ServletException, IOException {
        log.info("******************************************************************");
        log.info("请求功能代码ProcessCode："+ProcessCode);
        request.getRequestDispatcher(FunctionMap.functionMap.get(ProcessCode)).forward(request,response);
        log.info("=============接口调用结束==========");
    }
}
