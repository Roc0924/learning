package leaning.aspect.aspect;


import leaning.aspect.annotation.CatchLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

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
    public Object catchLock(final ProceedingJoinPoint pjp, CatchLock catchLock) {
        Object result = null;
        System.out.println("========================before========================");




        try {
            result = pjp.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        System.out.println("========================after=========================");
        return result;
    }

}
