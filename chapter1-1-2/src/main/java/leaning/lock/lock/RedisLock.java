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

    private static final String LOCK = "lock";

    private String prefix;
    private String key;

    Boolean lock = Boolean.FALSE;

    public RedisLock() {

    }

    public RedisLock(String prefix, String key) {
        this.prefix = prefix;
        this.key = key;
    }

//    RedisService redisService = new RedisService();

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

    public Boolean lock1(Long timeout, Long expire) throws InterruptedException {
        Long start = System.currentTimeMillis();

        try{
            while(System.currentTimeMillis() - start < timeout) {
                String redisKey =  this.getPrefix() + key;
                if(redisService.setNE(redisKey, LOCK)) {
                    redisService.setExpire(this.getPrefix() + key, expire);
                    this.lock = Boolean.TRUE;
                    return this.lock;
                } else {
                    System.out.println("waiting lock");
                    Thread.sleep(30);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean unLock(String sku) throws InterruptedException {
        return redisService.del(sku);
    }

    public Boolean unLock1(String key) throws InterruptedException {

        try {
            if (this.lock) {
                return redisService.del(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }


    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
