package com.example.fengcommon.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lettuce客户端 redis配置
 */
@Configuration
@EnableCaching
public class LettuceRedisConfig {

    private Logger log = LoggerFactory.getLogger(LettuceRedisConfig.class);


    @Autowired
    RedisProperties redisProperties;

    //读取pool配置
    @Bean
    public GenericObjectPoolConfig poolConfig() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMinIdle(redisProperties.getLettuce().getPool().getMinIdle());
        config.setMaxIdle(redisProperties.getLettuce().getPool().getMaxIdle());
        config.setMaxTotal(redisProperties.getLettuce().getPool().getMaxActive());
        config.setMaxWaitMillis(redisProperties.getLettuce().getPool().getMaxWait().toMillis());
        return config;
    }
    /**
    * @Description:  将哨兵信息放到配置中
    */
    @Bean
    public RedisSentinelConfiguration configuration() {
        RedisSentinelConfiguration redisConfig = new RedisSentinelConfiguration();
        redisConfig.setPassword(redisProperties.getPassword());
        redisConfig.setMaster(redisProperties.getSentinel().getMaster());
//        redisConfig.setPassword(RedisPassword.of(redisConfigThree.getPassword()));
        if(redisProperties.getSentinel().getNodes()!=null) {
            List<RedisNode> sentinelNode=new ArrayList<RedisNode>();
            for(String sen : redisProperties.getSentinel().getNodes()) {
                String[] arr = sen.split(":");
                sentinelNode.add(new RedisNode(arr[0],Integer.parseInt(arr[1])));
            }
            redisConfig.setSentinels(sentinelNode);
        }
        return redisConfig;
    }

    @Bean("lettuceConnectionFactory")
    public LettuceConnectionFactory lettuceConnectionFactory(@Qualifier("poolConfig") GenericObjectPoolConfig config,
                                                             @Qualifier("configuration") RedisSentinelConfiguration redisConfig) {//注意传入的对象名和类型RedisSentinelConfiguration
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(config).build();
        return new LettuceConnectionFactory(redisConfig, clientConfiguration);
    }

    @Bean("stringObjectRedisTemplate")
    public RedisTemplate<String, Object> stringObjectRedisTemplate(@Qualifier("lettuceConnectionFactory") LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

        //设置序列化器
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean("stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(@Qualifier("lettuceConnectionFactory") LettuceConnectionFactory connectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate(connectionFactory);
        template.setEnableTransactionSupport (true);
        ObjectMapper mapper;
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        template.setValueSerializer (new StringRedisSerializer());
        template.setKeySerializer (new StringRedisSerializer());
        template.setHashKeySerializer (new StringRedisSerializer());
        template.setHashValueSerializer (new StringRedisSerializer());
        template.afterPropertiesSet ();
        return template;
    }


    @Bean("redisTemplate")
    public RedisTemplate<String, Serializable> redisTemplate(@Qualifier("lettuceConnectionFactory") LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer (new StringRedisSerializer());
        redisTemplate.setValueSerializer (new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory (connectionFactory);
        return redisTemplate;
    }


//    @Bean("cacheManager")
//    public CacheManager cacheManager(@Qualifier("lettuceConnectionFactory")LettuceConnectionFactory connectionFactory) {
//        RedisCacheConfiguration redisCacheConfiguration= RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(null);
//        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory))
//                .cacheDefaults(redisCacheConfiguration).build();
//    }
    /**
     * 最新版，设置redis缓存过期时间
     */

    @Bean
    public RedisCacheManager cacheManager(@Qualifier("lettuceConnectionFactory") LettuceConnectionFactory connectionFactory) {
        return new RedisCacheManager(
                RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory),
                this.getRedisCacheConfigurationWithTtl(60), // 默认策略，未配置的 key 会使用这个
                this.getRedisCacheConfigurationMap() // 指定 key 策略
        );
    }

    private Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap() {
        //自定义设置缓存时间
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        redisCacheConfigurationMap.put("redisCache", this.getRedisCacheConfigurationWithTtl(30 * 60));
        redisCacheConfigurationMap.put("customCache", this.getRedisCacheConfigurationWithTtl(60));
        return redisCacheConfigurationMap;
    }

    private RedisCacheConfiguration getRedisCacheConfigurationWithTtl(Integer seconds) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration.serializeValuesWith(
                RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(jackson2JsonRedisSerializer)
        ).entryTtl(Duration.ofSeconds(seconds));

        return redisCacheConfiguration;
    }


    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                log.info("key:" + sb.toString());
                return sb.toString();
            }
        };
    }
}