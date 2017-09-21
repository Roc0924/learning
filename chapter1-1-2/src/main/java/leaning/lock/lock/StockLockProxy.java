package leaning.lock.lock;

import leaning.lock.annotation.CacheLock;
import leaning.lock.annotation.LockedObject;
import leaning.lock.annotation.LockedPropertyObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/9/20
 * Time                 : 17:12
 * Description          :
 */
@Component
public class StockLockProxy implements InvocationHandler {

    @Autowired
    RedisLock redisLock;


    private Object proxied;

//    private StockLockProxy(Object proxied) {
//        this.proxied = proxied;
//    }

    public Object getInstance(Object proxied) {
        this.setProxied(proxied);
        return Proxy.newProxyInstance(proxied.getClass().getClassLoader(), proxied.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        CacheLock cacheLock = method.getAnnotation(CacheLock.class);
        if (null == cacheLock) {
            return method.invoke(proxied, args);
        }

        Annotation[][] annotations = method.getParameterAnnotations();

        if(null == annotations && annotations.length <= 0) {
            return method.invoke(proxied, args);
        }

        Object lockedObject = generateLockObject(annotations, args);

        generateLock(cacheLock.lockedPrefix(), lockedObject.toString());

        Boolean result = redisLock.lock1(cacheLock.timeout(), cacheLock.expireTime());

        if (!result) {
            throw new Exception("get lock fail");
        }

        System.out.println(Thread.currentThread().getName() + " locking");
        try {
            return method.invoke(proxied, args);
        } finally {
            redisLock.unLock1(cacheLock.lockedPrefix() + lockedObject.toString());
            redisLock.setKey(null);
            redisLock.setPrefix(null);
            System.out.println(Thread.currentThread().getName() + " unlocked");
        }

    }

    private void generateLock(String prefix, String lockedObject) {
        redisLock.setPrefix(prefix);
        redisLock.setKey(lockedObject);
    }

    private Object generateLockObject(Annotation[][] annotations, Object[] args) throws Exception {
        if(null == args || args.length == 0){
            throw new Exception("方法参数为空，没有被锁定的对象");
        }

        if(null == annotations || annotations.length == 0){
            throw new Exception("没有被注解的参数");
        }
        int index = -1;
        for (int i = 0; i < annotations.length; i++) {
            for (int j = 0; j < annotations[i].length; j++) {
                if(annotations[i][j] instanceof LockedPropertyObject){
                    try {
                        Field field = args[i].getClass().getDeclaredField(((LockedPropertyObject)annotations[i][j]).field());
                        field.setAccessible(Boolean.TRUE);
                        return field.get(args[i]);
                    } catch (NoSuchFieldException | SecurityException e) {
                        throw new Exception("注解对象中没有该属性" + ((LockedPropertyObject)annotations[i][j]).field());
                    }
                }


                if (annotations[i][j] instanceof LockedObject) {
                    index = i;
                    break;
                }
            }

            if (index == -1) {
                throw new Exception("请指定被锁定参数");
            }
        }
        return args[index];

    }

    public Object getProxied() {
        return proxied;
    }

    public void setProxied(Object proxied) {
        this.proxied = proxied;
    }
}
