package leaning.lock.lock;

import leaning.lock.annotation.RedisLockAnno;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


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
    public Object test(final ProceedingJoinPoint pjp, RedisLockAnno redisLockAnno) throws InterruptedException {
        Object result = null;
        System.out.println("start");
        Map<String, Object> paramsMap = generateParamsMap(pjp);
        if(redisLock.lock(paramsMap.get("sku").toString(), 300000L)){
            System.out.println(Thread.currentThread().getName() + " locked " + paramsMap.get("sku"));
        } else {
            System.out.println(Thread.currentThread().getName() + " timeout");
            return null;
        }
        try {
            result = pjp.proceed();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            redisLock.unLock(paramsMap.get("sku").toString());
            System.out.println(Thread.currentThread().getName() + " unlocked " + paramsMap.get("sku"));
        }

        System.out.println("end");
        return result;
    }

    private Map<String, Object> generateParamsMap(ProceedingJoinPoint pjp) {
        Object[] paramsValue = pjp.getArgs();
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        String[] paramsName = methodSignature.getParameterNames();

        HashMap<String, Object> paramsMap = new HashMap<>();
        for (int i = 0; i < paramsName.length; i++) {
            if (paramsName[i].toLowerCase().contains("password")) {
                continue;
            }
            paramsMap.put(paramsName[i], paramsValue[i]);
        }
        return paramsMap;
    }


}
