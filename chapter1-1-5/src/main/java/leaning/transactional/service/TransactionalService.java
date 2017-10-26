package leaning.transactional.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/9/29
 * Time                 : 15:05
 * Description          : transactional service 类
 */
@Service("transactionalService")
public class TransactionalService {

    @Transactional
    public Object testTransaction1(String param1) {

        return param1;
    }
}
