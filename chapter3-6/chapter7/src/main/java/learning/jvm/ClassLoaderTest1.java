package learning.jvm;

import java.io.IOException;
import java.io.InputStream;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2018/1/24
 * Time                 : 10:55
 * Description          :
 */
public class ClassLoaderTest1 {
    public static void main(String[] args) throws Exception{
        ClassLoader myLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                InputStream is = getClass().getResourceAsStream(fileName);

                if (null == is) {
                    return super.loadClass(name);
                }

                try {
                    byte[] b = new byte[is.available()];
                    is.read(b);
                    return defineClass(name, b, 0, b.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }




                return super.loadClass(name);
            }
        };


        Object obj = myLoader.loadClass("learning.jvm.ClassLoaderTest1").newInstance();

        System.out.println(obj.getClass());
        System.out.println(obj instanceof learning.jvm.ClassLoaderTest1);



        ClassLoaderTest1 obj1 = new ClassLoaderTest1();
        System.out.println(obj.getClass().getClassLoader());
        System.out.println(obj1.getClass().getClassLoader());

    }
}
