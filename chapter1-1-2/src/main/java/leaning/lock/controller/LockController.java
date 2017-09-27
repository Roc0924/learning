package leaning.lock.controller;

import leaning.lock.entity.SecondKill;
import leaning.lock.lock.CglibProxy;
import leaning.lock.lock.StockLockProxy;
import leaning.lock.service.RedisLockService;
import leaning.lock.service.RedisLockServiceI;
import leaning.lock.service.TestCglib;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
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


    @Autowired
    CglibProxy cglibProxy;

    @Autowired
    TestCglib testCglib;


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


    @RequestMapping(value = "/kill2", method = RequestMethod.POST)
    public Object secKill2(@RequestBody SecondKill secondKill, @RequestParam(name = "sleep") Long sleep) throws InterruptedException {

        Enhancer enhancer = new Enhancer();

        enhancer.setSuperclass(TestCglib.class);
        enhancer.setCallback(cglibProxy);

//        Class[] arg1 = new Class[1];
//        Object[] arg2 = new Object[1];
//        arg1[0] = redisLockService.getClass();
//        arg2[0] = redisLockService;
        TestCglib rls = (TestCglib)enhancer.create();

        return rls.method1();
    }

}
