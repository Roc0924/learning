package leaning.lock.lock;

import leaning.lock.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
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
        Long start = System.currentTimeMillis();
        while(System.currentTimeMillis() - start < timeout) {
            if(redisService.setNE(sku, "test")) {
                redisService.setExpire(sku, timeout);
                return true;
            } else {
                System.out.println(Thread.currentThread().getName() + " sleep...");
                Thread.sleep(30);
            }
        }
        return false;
    }


    public boolean unLock(String sku) throws InterruptedException {
        return redisService.del(sku);
    }
}
