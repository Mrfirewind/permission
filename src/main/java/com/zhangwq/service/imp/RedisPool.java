package com.zhangwq.service.imp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Component
@Slf4j
public class RedisPool {

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    public ShardedJedis getInstance() {
        return shardedJedisPool.getResource();
    }

    public void closeSafe(ShardedJedis shardedJedis) {
        try {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        } catch (Exception e) {
            log.error("close redis resource exception ", e);
        }
    }
}
