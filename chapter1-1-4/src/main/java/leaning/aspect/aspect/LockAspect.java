package leaning.aspect.aspect;


import leaning.aspect.annotation.CatchLock;
import leaning.aspect.annotation.LockAttribute;
import leaning.aspect.annotation.LockParameter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

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

        // 判断是否开启锁
        if (null == cl) {
            System.out.println("no annotation");
            return null;
        }

        // 获得方法上的注解
        Parameter[] parameters = m.getParameters();
        Object[] objects = pjp.getArgs();
        for (int i = 0; i < parameters.length; i++ ) {
            Parameter parameter = parameters[i];
            Object obj = objects[i];
            Annotation[] annotations = parameter.getAnnotations();
            // 循环处理注解
            for (Annotation annotation : annotations) {
                // 锁定参数
                if(annotation.annotationType().equals(LockParameter.class)) {
                    System.out.println(obj);
                    System.out.println("LockParameter");
                } else if (annotation.annotationType().equals(LockAttribute.class)){
                    System.out.println("LockAttribute");
                }
            }
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
