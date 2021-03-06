package leaning.proxy.annotation;

import java.lang.annotation.*;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/9/20
 * Time                 : 16:08
 * Description          :
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Country {
    String country();
}
