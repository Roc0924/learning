package leaning.lock.service;

import leaning.lock.annotation.RedisLockAnno;
import org.springframework.stereotype.Service;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/9/19
 * Time                 : 17:34
 * Description          :
 */
@Service("redisLockService")
public class RedisLockService {

    @RedisLockAnno
    public Object test(String sku) {

        return sku;
    }
}
