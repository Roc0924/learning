package leaning.aspect.annotation;

import java.lang.annotation.*;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/9/27
 * Time                 : 14:56
 * Description          : 锁定对象属性
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LockAttribute {
    String[] fileds() default {};       // 锁定的属性
}
