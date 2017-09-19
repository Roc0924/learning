package leaning.lock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/9/19
 * Time                 : 16:49
 * Description          :
 */
@RestController
@RequestMapping("/test")
public class TestController {



    @Autowired
    StringRedisTemplate stringRedisTemplate;

//    @Resource(name = "stringRedisTemplate")
//    ValueOperations<String, String> valueOperations;

    @RequestMapping("/test1")
    public Object test1(@RequestParam(name = "key") String key, @RequestParam(name = "value") String value) {
        ValueOperations valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key, value);

        return valueOperations.get(key);
    }
}
