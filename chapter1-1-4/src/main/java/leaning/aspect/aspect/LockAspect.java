package leaning.aspect.aspect;


import leaning.aspect.annotation.CatchLock;
import leaning.aspect.annotation.LockAttribute;
import leaning.aspect.annotation.LockParameter;
import leaning.aspect.lock.Lock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    Lock lock;

    @Around("@annotation(catchLock)")
    public Object catchLock(final ProceedingJoinPoint pjp, CatchLock catchLock) throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException {
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
        List<String> lockList = new ArrayList<>();


        for (int i = 0; i < parameters.length; i++ ) {
            Parameter parameter = parameters[i];
            Object obj = objects[i];
            Annotation[] annotations = parameter.getAnnotations();
            // 循环处理注解
            for (Annotation annotation : annotations) {
                // 锁定参数
                if(annotation.annotationType().equals(LockParameter.class)) {
                    lockList.add(obj.toString());
                } else if (annotation.annotationType().equals(LockAttribute.class)){
                    String[] fields = ((LockAttribute)annotation).fileds();
                    Class cls = obj.getClass();
                    for (String field : fields) {
                        Field fd = cls.getDeclaredField(field);
                        fd.setAccessible(Boolean.TRUE);
                        String value = fd.get(obj).toString();
                        lockList.add(value);
                    }
                    System.out.println("LockAttribute");
                }
            }
        }
        lock.lock(lockList, cl.expire());

        System.out.println("========================>>before<<========================");



        try {
            result = pjp.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            System.out.println("========================<<after>>=========================");

            lock.unlock(lockList);
            System.out.println("========================>>after<<=========================");
        }
        return result;
    }

}
