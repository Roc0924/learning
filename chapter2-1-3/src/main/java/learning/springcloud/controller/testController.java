package learning.springcloud.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/10/27
 * Time                 : 9:40
 * Description          :
 */
@RestController
public class testController {

    @Value("${environment}")
    private String environment;


    @RequestMapping(value = "/test/environment", method = RequestMethod.GET)
    public Object doTest() {
        return environment;
    }
}
