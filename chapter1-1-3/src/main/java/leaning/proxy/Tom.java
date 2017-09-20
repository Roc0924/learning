package leaning.proxy;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/9/20
 * Time                 : 15:48
 * Description          :
 */
public class Tom implements People{
    @Override
    public void sayHello(String location) {
        System.out.println("Tom say Hello");
    }
}
