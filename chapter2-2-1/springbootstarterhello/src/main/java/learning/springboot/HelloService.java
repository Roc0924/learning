package learning.springboot;

import org.springframework.stereotype.Component;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/12/15
 * Time                 : 13:31
 * Description          :
 */
public class HelloService {
    private String msg;

    public String sayHello() {
        return "Hello " + msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
