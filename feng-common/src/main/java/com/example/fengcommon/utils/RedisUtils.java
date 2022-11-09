package com.example.fengcommon.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA. User: liuyaowen Date: 2017/1/12 Time: 8:48
 *
 * @Description:redis通用的的帮助类，需要spring 容器支持
 */
@Component
public class RedisUtils {
	protected static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    private static RedisTemplate redisTemplate;

    @Autowired
    public RedisUtils(RedisTemplate redisTemplate){
        RedisUtils.redisTemplate = redisTemplate;
    }

	@SuppressWarnings("unchecked")
	public static <T> T get(String key, Class<T> clazz) {
		try {
			ValueOperations<String, T> ops = getRedisTemplate().opsForValue();
			T value = ops.get(key);
			return value;
		} catch (Exception e) {
			logger.info("get from Cache error! key = {" + key + "}", e);
			logger.warn("catch error!", e);
			return null;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Set getSet(String key) {
		try {
			SetOperations ops = getRedisTemplate().opsForSet();
			return ops.members(key);
		} catch (Exception e) {
			logger.info("get from Cache error! key = {" + key + "}", e);
			logger.warn("catch error!", e);
			return null;
		}
	}

	public static String getString(String key) {
		return get(key, String.class);
	}

	public static Integer getInteger(String key) {
		return get(key, Integer.class);
	}

	public static BigDecimal getDecimal(String key) {
		return get(key, BigDecimal.class);
	}

	@SuppressWarnings("unchecked")
	public static <T> boolean set(String key, T value, int expire) {
		try {
			if (value == null) {
				return Boolean.FALSE;
			}
			ValueOperations<String, T> ops = getRedisTemplate().opsForValue();
			ops.set(key, value);
			return getRedisTemplate().expire(key, expire, TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.info("set to Cache error! key = {" + key + "}", e);
			logger.warn("set to Cache error!", e);
		}
		return Boolean.FALSE;
	}
	public static <T> void setAndexpire(String key, T value, int expire) {
		try {
			ValueOperations<String, T> ops = getRedisTemplate().opsForValue();
		    ops.set(key, value,expire,TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.info("set to Cache error! key = {" + key + "}", e);
			logger.warn("set to Cache error!", e);
		}
	}
/**
 * @title: 根据前缀删除
 * @author: luo heng
 * @date: 2021/12/20 11:14
 * @description: TODO
 * @version:
 * @return:
 */
//	public static Set<Object> scanDel(String pattern) {
//		RedisTemplate redisTemplate = getRedisTemplate();
//		redisTemplate.execute((RedisCallback<Set<Object>>) connection -> {
//			JedisCommands commands = (JedisCommands) connection.getNativeConnection();
//			MultiKeyCommands multiKeyCommands = (MultiKeyCommands) commands;
//			int scanInvokeCount = 0;
//			int totalCount = 0;
//			logger.info("RedisHelper_clearScan_invoke_start scanInvokeCount:{}",scanInvokeCount);
//			ScanParams scanParams = new ScanParams();
//			scanParams.match(pattern + "*");
//			scanParams.count(500);
//			ScanResult<String> scan = multiKeyCommands.scan("0", scanParams);
//			scanInvokeCount++;
//			while (null != scan.getStringCursor()) {
//				List<String> keys = scan.getResult();
//				if (!CollectionUtils.isEmpty(keys)){
//					int count = 0;
//					for (String key : keys) {
//						try {
//							connection.del(key.getBytes());
//							logger.info("RedisHelper_clearScan key:{}",key);
//							count++;
//							totalCount++;
//						} catch (Exception e) {
//							logger.info("RedisHelper_clearScan_fail key:{}",key);
//							e.printStackTrace();
//						}
//					}
//					logger.info("RedisHelper_clearScan_delete count:{}",count);
//				}
//				if (!StringUtils.equals("0", scan.getStringCursor())) {
//					scan = multiKeyCommands.scan(scan.getStringCursor(), scanParams);
//					scanInvokeCount++;
//					logger.info("RedisHelper_clearScan_invoke scanInvokeCount:{}",scanInvokeCount);
//					continue;
//				} else {
//					break;
//				}
//			}
//			logger.info("RedisHelper_clearScan_invoke_end totalCount:{}",totalCount);
//			logger.info("RedisHelper_clearScan_invoke_end scanInvokeCount:{}",scanInvokeCount);
//			return null;
//		});
//		return null;
//	}
	public static <T> void set(String key, T value) {
		try {
			if (value == null) {
				return;
			}
			ValueOperations<String, T> ops = getRedisTemplate().opsForValue();
			ops.set(key, value);
		} catch (Exception e) {
			logger.info("set to Cache error! key = {" + key + "}", e);
			logger.warn("set to Cache error!", e);
		}
	}

	public static boolean setString(String key, String value, int expire) {
		return set(key, value, expire);
	}

	public static boolean setDefExpire(String key, String value) {
		return set(key, value, 5 * 60 * 1000);
	}

	public static <T> boolean setDefExpire(String key, T value) {
		return set(key, value, 5 * 60 * 1000);
	}

	@SuppressWarnings("unchecked")
	public static void del(String key) {
		try {
			getRedisTemplate().delete(key);
		} catch (Exception e) {
			logger.info("delete from Cache error! key = {" + key + "}", e);
			logger.warn("delete from Cache error!", e);
		}
	}

	/**
	 * key是否存在
	 *
	 * @param key
	 */
	@SuppressWarnings("unchecked")
	public static boolean exists(final String key) {
		return getRedisTemplate().hasKey(key);
	}

	/**
	 * 把lis放入redis缓存起来,当天
	 *
	 * @param cacheKey
	 * @param list
	 */
//	public static void putListByRedisCache2ToDay(String cacheKey, List<?> list) {
//		putListByRedisCache(cacheKey, list, DateUtils.getMilliSecondsLeftToday());
//	}

	/**
	 * 把lis放入redis缓存起来,一个小时
	 *
	 * @param cacheKey
	 * @param list
	 */
	public static void putListByRedisCache2ToOneHours(String cacheKey, List<?> list) {
		putListByRedisCache(cacheKey, list, 2 * 60 * 60 * 1000);
	}

	/**
	 * 把lis放入redis缓存起来,30分钟
	 *
	 * @param cacheKey
	 * @param list
	 */
	public static void putListByRedisCache2To30Min(String cacheKey, List<?> list) {
		putListByRedisCache(cacheKey, list, 5 * 60 * 60 * 1000);
	}

	/**
	 * 把map放入redis缓存起来,当天
	 *
	 * @param cacheKey
	 * @param map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Long putHashMap(String cacheKey, Map map, long time) {
		try {
			getRedisTemplate().opsForHash().putAll(cacheKey, map);
			if (time > 0) {
				getRedisTemplate().expire(cacheKey, time, TimeUnit.SECONDS);
			}
			return getRedisTemplate().opsForHash().size(cacheKey);
		} catch (Exception e) {
			logger.error("putHashMap,cacheKey[" + cacheKey + "]", e);
		}
		return 0l;
	}

	/**
	 * 向key对应的map中添加缓存对象
	 *
	 * @param cacheKey
	 *            cache对象key
	 * @param field
	 *            map对应的key
	 * @param value
	 *            值
	 */
	@SuppressWarnings("unchecked")
	public static void addMap(String cacheKey, String field, Object value) {
		getRedisTemplate().opsForHash().put(cacheKey, field, value);
	}

	/**
	 * 把map放入redis缓存起来,当天
	 *
	 * @param cacheKey
	 */
	@SuppressWarnings("rawtypes")
	public static Map getAllHashMap(String cacheKey) {
		@SuppressWarnings("unchecked")
        BoundHashOperations boundHashOperations = getRedisTemplate().boundHashOps(cacheKey);
		return boundHashOperations.entries();
	}

	/**
	 * 把map放入redis缓存起来,当天
	 *
	 * @param cacheKey
	 */
	@SuppressWarnings("unchecked")
	public static Object getMapField(String cacheKey, String field) {
		return getRedisTemplate().boundHashOps(cacheKey).get(field);
	}

	/**
	 * 删除map中的某个对象
	 *
	 * @param key
	 *            map对应的key
	 * @param field
	 *            map中该对象的key
	 * @author lh
	 * @date 2016年8月10日
	 */
	@SuppressWarnings("unchecked")
	public static void delMapField(String key, Object... field) {
		BoundHashOperations<String, String, ?> boundHashOperations = getRedisTemplate().boundHashOps(key);
		boundHashOperations.delete(field);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void putListByRedisCache(String cacheKey, List list, long time) {
		try {
			Set set = getRedisTemplate().opsForSet().members(cacheKey);
			if (!set.isEmpty()) {// 如果当天数据已经存在redis中则不进行更新
				return;
			}
			SetOperations operations = getRedisTemplate().opsForSet();
			int size = 0;
			for (int i = 0; i < list.size(); i++) {
				size += operations.add(cacheKey, list.get(i));
			}
			// 放入缓存的时间 毫秒
			getRedisTemplate().expire(cacheKey, time, TimeUnit.MILLISECONDS);

			if (size == list.size()) {
				logger.info("[" + cacheKey + "]缓存成功,size:" + size);
			} else {
				logger.info("[" + cacheKey + "]缓存失败,size:" + size);
				logger.error("[" + cacheKey + "]缓存失败,size:" + size + ",listsize:" + list.size());
			}

		} catch (Exception e) {
			logger.error("[" + cacheKey + "]缓存失败,List:" + JSONObject.toJSONString(list));
		}

	}

	@SuppressWarnings("unchecked")
	public static boolean hasKey(String pubparadescmap, String key) {
		try {
			return getRedisTemplate().boundHashOps(pubparadescmap).hasKey(key);
		} catch (Exception e) {
			logger.error("hasKey[" + key + "]hasKey,List:", e);
		}

		return false;
	}

	/**
	 * pub 、sub 模式
	 * 
	 * @param channel
	 * @param message
	 * @return
	 */
	public static boolean pubMessage(String channel, Object message) {

		try {
			getRedisTemplate().convertAndSend(channel, message);
		} catch (Exception e) {
			logger.error("pubMessage,channel=" + channel + ",message=" + StringUtils.toJson(message) + " ", e);
			return false;
		}

		return true;
	}

	@SuppressWarnings("rawtypes")
	public static RedisTemplate getRedisTemplate() {
			return redisTemplate;
	}

	public static void putListByRedisWithExpired(String cacheKey, List<?> list) {
		try {
			getRedisTemplate().opsForList().leftPushAll(cacheKey, list);
			getRedisTemplate().expire(cacheKey, 8 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
		}catch (Exception e){
			logger.error("缓存["+cacheKey+"]失败:", e);
		}

	}
	public static void putSetByRedisWithExpired(String cacheKey, List<?> list) {
		try {
			SetOperations setOperations = getRedisTemplate().opsForSet();
			if(exists(cacheKey)){
				//set集已存在，无需再次存入
				logger.info("缓存"+cacheKey+"已存在,无需重复插入！");
				return;
			}
			for(int i = 0; i < list.size(); i++){
				setOperations.add(cacheKey, list.get(i));
			}
			getRedisTemplate().expire(cacheKey, 8 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
		}catch (Exception e){
			logger.error("缓存["+cacheKey+"]失败:", e);
		}

	}
	public static List getList(String cacheKey ) {
		try {
			return getRedisTemplate().opsForList().range(cacheKey, 0, -1);
		}catch (Exception e){
			logger.error("获取缓存["+cacheKey+"]失败:", e);
		}
		return new ArrayList();
	}
}
