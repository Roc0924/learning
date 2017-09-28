package leaning.aspect.lock;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/9/28
 * Time                 : 17:23
 * Description          : 锁
 */
@Component
public class Lock {

    private String profix;

    private String key;

    public Boolean lock(List<String> lockFields, Long expire) {
        for (String lockFiled : lockFields) {
            System.out.println("lock " + lockFiled);
        }
        return Boolean.TRUE;
    }

    public Boolean unlock(List<String> lockFields) {
        for (String lockField : lockFields) {
            System.out.println("unlock " + lockField);
        }
        return Boolean.TRUE;
    }

    public String getProfix() {
        return profix;
    }

    public void setProfix(String profix) {
        this.profix = profix;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
