package leaning.lock.service;

import leaning.lock.annotation.CacheLock;
import leaning.lock.annotation.LockedObject;
import leaning.lock.annotation.LockedPropertyObject;
import leaning.lock.constant.RedisConstant;
import leaning.lock.entity.SecondKill;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/9/20
 * Time                 : 17:19
 * Description          :
 */
public interface RedisLockServiceI {
    @CacheLock(lockedPrefix = "oversea:test:lock:")
    public Object test(@LockedObject String sku, Long sleep) throws InterruptedException;


    @CacheLock(lockedPrefix = "oversea:test:lock:")
    public Object test1(@LockedPropertyObject(field = "sku") SecondKill secondKill, Long sleep) throws InterruptedException;
}
