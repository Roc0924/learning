package leaning.lock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/9/19
 * Time                 : 18:19
 * Description          :
 */
@Service("redisService")
public class RedisService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public Boolean setNE(final String key, final String value) {
        return stringRedisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.setNX(key.getBytes(), value.getBytes());
            }
        });
    }


    public Boolean setExpire(final String key, final Long timeout) {
        return stringRedisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.expire(key.getBytes(), timeout);
            }
        });
    }


    public Boolean del(final String key) {
        return stringRedisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.del(key.getBytes()) > 0;
            }
        });
    }

    public Object get(final String key) {
        return stringRedisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.get(key.getBytes());
            }
        });
    }


    public Boolean set(final String key, final String value) {
        return stringRedisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.set(key.getBytes(), value.getBytes());
                return Boolean.TRUE;
            }
        });
    }

    public Long incrByLength(final String key, final Long length) {
        return stringRedisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.incrBy(key.getBytes(), length);
            }
        });
    }


    public Long incrByOne(final String key) {
        return this.incrByLength(key, 1L);
    }




}
