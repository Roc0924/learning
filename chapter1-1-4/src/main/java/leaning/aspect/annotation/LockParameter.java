package leaning.aspect.annotation;

import java.lang.annotation.*;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/9/27
 * Time                 : 14:55
 * Description          : 锁定参数
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LockParameter {
    String test() default "";
}
