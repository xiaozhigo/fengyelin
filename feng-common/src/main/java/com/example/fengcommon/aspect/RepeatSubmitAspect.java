package com.example.fengcommon.aspect;

import com.alibaba.fastjson.JSONObject;
import com.example.fengcommon.annotation.NoRepeatSubmit;
import com.example.fengcommon.enums.Status;
import com.example.fengcommon.utils.RedisLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 防止接口重复提交
 */
@Aspect
@Component
@Slf4j
@Order(2)
public class RepeatSubmitAspect {

    @Autowired
    private RedisTemplate redisTemplate;

    @Pointcut("@annotation(noRepeatSubmit)")
    public void pointCut(NoRepeatSubmit noRepeatSubmit) {
    }

    @Around("pointCut(noRepeatSubmit)")
    public Object around(ProceedingJoinPoint pjp, NoRepeatSubmit noRepeatSubmit) throws Throwable {

        int lockSeconds = noRepeatSubmit.lockTime();
        String keyName = noRepeatSubmit.lockKey();
//        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
//        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
//        HttpServletRequest request = sra.getRequest();
//        Assert.notNull(request, "request can not null");
        // 此处可以用token或者JSessionId
        // String token = request.getHeader("JSessionId");
//        String token = request.getSession().getId();
//        String path = request.getServletPath();
        String methodName = pjp.getSignature().getDeclaringTypeName().concat(".").concat(pjp.getSignature().getName());
        String key = getKey(methodName, pjp.getArgs(),keyName);
        String clientId = getClientId();
        boolean lock = RedisLockUtil.lock(redisTemplate, key, clientId, lockSeconds);
        log.info("tryLock key = [{}], clientId = [{}]", key, clientId);

        if (lock) {
            log.info("tryLock success, key = [{}], clientId = [{}]", key, clientId);
            // 获取锁成功
            Object result;
            try {
                // 执行方法
                result = pjp.proceed();
            } finally {
                // 解锁
                RedisLockUtil.unLock(redisTemplate, key, clientId);
                //redisLock.unlock(key, clientId);
                log.info("releaseLock success, key = [{}], clientId = [{}]", key, clientId);
            }

            return result;

        } else {
            // 获取锁失败，认为是重复提交的请求
            log.info("tryLock fail, key = [{}]", key);
            throw new Exception(Status.REPEAT_SUBMIT.getMessage());
        }

    }




    private String getKey(String methodName, Object[] args,String keyName) {
        StringBuilder sb = new StringBuilder();
        if(!Objects.isNull(args)&&args.length>0){
            for (Object arg : args) {
                if (arg instanceof MultipartFile ||arg instanceof MultipartFile[] || arg instanceof BindingResult || arg instanceof HttpServletRequest) {
                    continue;
                }else{
                    sb.append(JSONObject.toJSONString(arg)).append(",");
                }
            }
        }
        JSONObject jsonArgs;
        try {
             jsonArgs = (JSONObject) JSONObject.parse(sb.toString());
        }catch (Exception e){
            jsonArgs=null;
        }
        String requestArgs = StringEscapeUtils.unescapeJavaScript(JSONObject.toJSONString(sb.substring(0, sb.length() - 1)));
        String key = Objects.isNull(jsonArgs) ? requestArgs
                : Objects.isNull(jsonArgs.get(keyName))?requestArgs:(String)jsonArgs.get(keyName);
        return DigestUtils.md5Hex(methodName + key);
    }

    private String getClientId() {
        return RedisLockUtil.getRandom();
    }


}