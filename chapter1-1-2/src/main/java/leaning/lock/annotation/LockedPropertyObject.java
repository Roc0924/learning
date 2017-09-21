package leaning.lock.annotation;

import com.sun.org.apache.xml.internal.security.utils.ElementProxy;

import java.lang.annotation.*;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/9/20
 * Time                 : 17:00
 * Description          :
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LockedPropertyObject {
    String field() default "";
}
