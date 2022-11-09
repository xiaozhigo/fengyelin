package com.example.fengcommon.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.time.Duration;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class RedisLockUtil {

    /**
     * redis分布式锁-加锁
     * @param redisTemplate redis 加锁解锁要保证同一个
     * @param key 分布式锁key
     * @param value 分布式锁value 一般为随机数
     * @param timeout 分布式锁过期时间 秒
     * @param number 重试次数
     * @param interval 重试间隔 毫秒
     * @return
     */
    public static boolean lock(RedisTemplate redisTemplate, String key, String value, int timeout, int number, int interval) {
        //加锁
        for (int i = 0; i < number; i++) {
            //尝试获取锁,成功则返回不成功则重试
            if (redisTemplate.opsForValue().setIfAbsent(key, value, Duration.ofSeconds(timeout))) {
                return true;
            }
            //暂停
            try {
                TimeUnit.MILLISECONDS.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //最终获取不到锁返回失败
        return false;
    }

    public static boolean lock(RedisTemplate redisTemplate, String key, String value, int lockSeconds) {
        return lock(redisTemplate, key, value, lockSeconds, 2, 1000);
    }

    /**
     * 解锁脚本,防止线程将其他线程的锁释放
     */
    private static String UN_LOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    /**
     * redis分布式锁-解锁
     * @param redisTemplate redis 加锁解锁要保证同一个
     * @param key 分布式锁key
     * @param value 分布式锁value 一般为随机数
     * @return
     */
    public static void unLock(RedisTemplate redisTemplate, String key, String value) {
        //解锁
        redisTemplate.execute(new DefaultRedisScript<>(UN_LOCK_SCRIPT, Long.class), Collections.singletonList(key), value);
    }

    /**
     * 获取调用者的类名和方法名
     * @return
     */
    public static String getMethodPath() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];
        //获取当前类名
        String className = stackTraceElement.getClassName();
        //获取当前方法名
        String methodName = stackTraceElement.getMethodName();
        return className + "#" + methodName;
    }

    /**
     * 获取随机数
     * @return
     */
    public static String getRandom() {
        return Objects.toString(ThreadLocalRandom.current().nextInt(0, 100000));
    }
}
