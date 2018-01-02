package com.zhangwq.service.imp;

import com.google.common.base.Joiner;
import com.zhangwq.beans.CacheKeyConstants;
import com.zhangwq.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;

@Service
@Slf4j
public class SysCacheService {
//    @Autowired
    private RedisPool redisPool;

    public void svaeCache(String toSavedValue, int timeOutSecond, CacheKeyConstants prefix) {
        this.saveCache(toSavedValue, timeOutSecond, prefix, null);
    }

    public void saveCache(String toSavedValue, int timeOutSecond, CacheKeyConstants prefix, String... keys) {
        if (null == toSavedValue) {
            return;
        }
        ShardedJedis shardedJedis = null;
        try {
         String cacheKey = this.generateCacheKey(prefix,keys);
            shardedJedis = redisPool.getInstance();
            shardedJedis.setex(cacheKey,timeOutSecond,toSavedValue);
        } catch (Exception e) {
          log.error("save cache exception,prefix:{},keys:{}",prefix, JsonMapper.objToString(keys),e);
        }finally {
            redisPool.closeSafe(shardedJedis);
        }
    }

    public String  getFromCache(CacheKeyConstants prefix, String... keys){
        ShardedJedis shardedJedis = null;
        String cacheKey = this.generateCacheKey(prefix,keys);
        try {
            shardedJedis = redisPool.getInstance();
            String value =shardedJedis.get(cacheKey);
            return value;
        } catch (Exception e) {
            log.error("get cache exception,prefix:{},keys:{}",prefix, JsonMapper.objToString(keys),e);
            return null;
        }finally {
            redisPool.closeSafe(shardedJedis);
        }
    }

    private String generateCacheKey(CacheKeyConstants prefix, String... keys) {
        String key = prefix.name();
        if (ArrayUtils.isNotEmpty(keys)) {
            key += "_" + Joiner.on("_").join(keys);
        }
        return key;
    }
}
