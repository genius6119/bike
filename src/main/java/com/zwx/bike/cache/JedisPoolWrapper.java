package com.zwx.bike.cache;

import com.zwx.bike.common.constants.Parameters;
import com.zwx.bike.common.exception.BikeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;

/**
 * Create By Zhang on 2018/3/11
 */
@Component
@Slf4j
public class JedisPoolWrapper {
    private JedisPool jedisPool=null;

    @Autowired
    private Parameters parameters;

    @PostConstruct
    public void init() throws BikeException{
        try {
            JedisPoolConfig config=new JedisPoolConfig();
            config.setMaxWaitMillis(parameters.getRedisMaxWaitMillis());
            config.setMaxIdle(parameters.getRedisMaxIdle());
            config.setMaxTotal(parameters.getRedisMaxTotal());

            jedisPool=new JedisPool(config,parameters.getRedisHost(),parameters.getRedisPort(),2000);
        } catch (Exception e) {
            log.error("Fail to initialize redis",e);
            throw new BikeException("初始化redis失败");
        }
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }
}
