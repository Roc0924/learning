package leaning.lock.controller;

import leaning.lock.entity.SecondKill;
import leaning.lock.lock.StockLockProxy;
import leaning.lock.service.RedisLockService;
import leaning.lock.service.RedisLockServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/9/19
 * Time                 : 16:17
 * Description          :
 */
@RestController
@RequestMapping("/lock")
public class LockController {

    @Autowired
    RedisLockService redisLockService;

    @Autowired
    StockLockProxy stockLockProxy;


    @RequestMapping(value = "/kill", method = RequestMethod.GET)
    public Object secKill(@RequestParam(name = "sku") String sku, @RequestParam(name = "sleep") Long sleep) throws InterruptedException {


        RedisLockServiceI redisLock = (RedisLockServiceI) stockLockProxy.getInstance(redisLockService);
        return redisLock.test(sku, sleep);
    }


    @RequestMapping(value = "/kill1", method = RequestMethod.POST)
    public Object secKill1(@RequestBody SecondKill secondKill, @RequestParam(name = "sleep") Long sleep) throws InterruptedException {


        RedisLockServiceI redisLock = (RedisLockServiceI) stockLockProxy.getInstance(redisLockService);
        return redisLock.test1(secondKill, sleep);
    }


}
