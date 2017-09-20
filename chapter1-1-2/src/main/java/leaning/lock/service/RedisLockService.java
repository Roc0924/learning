package leaning.lock.service;

import leaning.lock.annotation.RedisLockAnno;
import leaning.lock.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/9/19
 * Time                 : 17:34
 * Description          :
 */
@Service("redisLockService")
public class RedisLockService {
    @Autowired
    RedisService redisService;

    @RedisLockAnno
    public Object test(String sku, Long sleep) throws InterruptedException {
        Map<String, Object> response = new HashMap<>();

        String key = RedisConstant.LOCK_REDIS_KEY_PROFIX + "sku:" + sku;
        redisService.incrByLength(key, -1L);



        response.put(sku, Integer.parseInt(new String((byte[])redisService.get(key))));
        Thread.sleep(sleep);

        redisService.set(key, response.get(sku).toString());
        System.out.println(Thread.currentThread().getName() + "set: " + response.get(sku) );



        return response;

    }
}
