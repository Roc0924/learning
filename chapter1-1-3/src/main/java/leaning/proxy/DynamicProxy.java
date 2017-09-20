package leaning.proxy;

import leaning.proxy.annotation.Country;
import leaning.proxy.annotation.Location;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/9/20
 * Time                 : 15:51
 * Description          :
 */
public class DynamicProxy implements InvocationHandler {

    private Object obj;

    private DynamicProxy(Object obj) {
        this.obj = obj;
    }

    public static Object newInstance(Object obj) {
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new DynamicProxy(obj));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Object result = null;

        try {
            System.out.println("before method " + method.getName());
            Country country = method.getAnnotation(Country.class);
            if (null == country) {
                result = method.invoke(obj, args);
            }

            System.out.println(country.country());


            Annotation[][] annotations = method.getParameterAnnotations();
            System.out.println(from(annotations, args));
            result = method.invoke(obj, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("after method " + method.getName());
        }



        return result;
    }


    Object from(Annotation[][] annotations, Object[] args) throws Exception {
        if(null == args || args.length == 0){
            throw new Exception("方法参数为空，没有被锁定的对象");
        }

        if(null == annotations || annotations.length == 0){
            throw new Exception("没有被注解的参数");
        }

        int index = 0;

        for (int i = 0; i < annotations.length; i++) {
            for (int j = 0; j < annotations[i].length; j++) {
                if (annotations[i][j] instanceof Location) {
                    index = i;
                    return args[i];

                }
            }
        }


        return null;

    }
}
