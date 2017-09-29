package leaning.aspect.service;

import leaning.aspect.annotation.CatchLock;
import leaning.aspect.annotation.LockAttribute;
import leaning.aspect.annotation.LockParameter;
import leaning.aspect.entity.TestAspectEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/9/27
 * Time                 : 15:08
 * Description          :
 */
@Service("testService")
public class TestService {

    @CatchLock(prefix = "redis:lock:")
    public Map<String, Object> testLockParameter(@LockParameter String param1, @LockParameter String param2) {
        Map<String, Object> result = new HashMap<>();
        result.put("param1", param1);
        result.put("param2", param2);
        System.out.println("testService.testLockParameter " + param1 + " " + param2);
        return result;
    }

    @CatchLock(prefix = "redis:lock:")
    public Object testLockAttributes( @LockAttribute(fileds = {"testAttribute1", "testAttribute2"}) TestAspectEntity body) {
        System.out.println("testService.testLockAttributes " + body);
        return body;
    }
}
