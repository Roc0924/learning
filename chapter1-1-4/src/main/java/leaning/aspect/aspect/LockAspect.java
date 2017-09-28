package leaning.aspect.aspect;


import leaning.aspect.annotation.CatchLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/9/28
 * Time                 : 14:19
 * Description          : 锁切面
 */
@Component
@Aspect
public class LockAspect {

    @Around("@annotation(catchLock)")
    public Object catchLock(final ProceedingJoinPoint pjp, CatchLock catchLock) throws NoSuchMethodException {
        Object result = null;
        System.out.println("========================<<before>>========================");
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        Method m = pjp.getTarget().getClass().getMethod(ms.getName(),ms.getParameterTypes());
        CatchLock cl = m.getAnnotation(CatchLock.class);

        if (null == cl) {
            System.out.println("no annotation");
            return null;
        }

        Annotation[] annotations = m.getAnnotations();

        for (Annotation annotation : annotations) {
            System.out.println(annotation);
        }
        System.out.println("========================>>before<<========================");



        try {
            result = pjp.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        System.out.println("========================<<after>>=========================");
        System.out.println("========================>>after<<=========================");
        return result;
    }

}
