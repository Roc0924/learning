package leaning.lock.entity;

import leaning.lock.annotation.LockedObject;
import leaning.lock.annotation.LockedPropertyObject;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/9/20
 * Time                 : 17:21
 * Description          :
 */
public class SecondKill {
    private String sku;
    private Integer count;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
