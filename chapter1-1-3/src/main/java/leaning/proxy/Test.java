package leaning.proxy;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/9/20
 * Time                 : 16:01
 * Description          :
 */
public class Test {
    public static void main(String[] args) {
        People tom = (People) DynamicProxy.newInstance(new Tom());
        tom.sayHello("D.C");
    }
}
