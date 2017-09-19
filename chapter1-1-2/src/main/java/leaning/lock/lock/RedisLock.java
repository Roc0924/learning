package leaning.lock.lock;

import leaning.lock.constant.RedisConstant;
import leaning.lock.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/9/19
 * Time                 : 17:58
 * Description          :
 */
@Component
public class RedisLock {

    @Autowired
    RedisService redisService;

    public boolean lock(String sku, Long timeout) throws InterruptedException {
        Long nanoStart = System.nanoTime();
        while(System.nanoTime() - nanoStart < timeout)

        if(redisService.setNE(sku, "test")) {
            redisService.setExpire(sku, 3000L);
        } else {
            Thread.sleep(30);
        }


        return false;
    }


    public boolean unLock(String sku) throws InterruptedException {
        return redisService.del(sku);
    }
}
