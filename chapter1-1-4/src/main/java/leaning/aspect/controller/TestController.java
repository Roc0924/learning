package leaning.aspect.controller;

import leaning.aspect.annotation.LockParameter;
import leaning.aspect.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/9/28
 * Time                 : 11:32
 * Description          : 测试controller类
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    TestService testService;

    @RequestMapping(value = "/testLockParameter", method = RequestMethod.GET)
    public Object testLockParameter(@LockParameter String param1, String param2) {
        return testService.testLockParameter(param1, param2);
    }
}
