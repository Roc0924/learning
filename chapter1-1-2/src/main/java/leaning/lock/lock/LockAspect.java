package leaning.lock.lock;

import leaning.lock.annotation.RedisLockAnno;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/9/19
 * Time                 : 17:04
 * Description          :
 */
@Component
@Aspect
public class LockAspect {

    @Autowired
    RedisLock redisLock;

    @Around("@annotation(redisLockAnno)")
    public Object test(final ProceedingJoinPoint pjp, RedisLockAnno redisLockAnno) {
        Object result = null;
        Object[] params = pjp.getArgs();
        System.out.println("start");
//        redisLock.lock()
        try {
            result = pjp.proceed();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            long end = System.currentTimeMillis();
        }

        System.out.println("end");
        return result;
    }
}
