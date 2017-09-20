package leaning.lock.controller;

import leaning.lock.service.RedisLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisLockService redisLockService;


    @RequestMapping(value = "/kill", method = RequestMethod.GET)
    public Object secKill(@RequestParam(name = "sku") String sku, @RequestParam(name = "sleep") Long sleep) throws InterruptedException {



        return redisLockService.test(sku, sleep);
    }


}
