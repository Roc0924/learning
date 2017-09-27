package leaning.lock.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/9/27
 * Time                 : 11:27
 * Description          :
 */
@Service("testCglib")
public class TestCglib{

    @Transactional
    public Object method1() {
        return "method1";
    }
}
