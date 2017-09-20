package leaning.proxy;

import leaning.proxy.annotation.Country;
import leaning.proxy.annotation.Location;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/9/20
 * Time                 : 15:47
 * Description          :
 */
public interface People {
    @Country(country = "American")
    void sayHello(@Location String location);
}
