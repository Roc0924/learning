package leaning.aspect.annotation;

import java.lang.annotation.*;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/9/27
 * Time                 : 14:48
 * Description          : 捕获要加锁的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CatchLock {
    String prefix() default "";     // 锁前缀
    long expire() default 2000L;      // 失效时间
}
