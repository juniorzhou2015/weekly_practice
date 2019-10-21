package weekly.practice.d191008;

import java.io.Serializable;
import java.lang.reflect.Constructor;

public class Singleton implements Serializable {
    /**
     * 恶汉式：内存损失
     * 饱汉式：性能损失
     * 静态内部类：内存、性能最优，通过构造方法重载防反射安全
     * 枚举：多线程、反射、序列化最优，但有序列化风险
     */
    private Singleton() {
        throw new RuntimeException("Can't new Singleton from reflection");
    }

    private Singleton(Object a) {

    }

    private static class Holder {
        private static final Singleton SINGLETON = new Singleton(1);
//        private static final transient Singleton SINGLETON = new Singleton();
    }

    public static Singleton getInstance() {
        return Holder.SINGLETON;
    }

    protected Object readResolve() {
        return getInstance();
    }

    public static void main(String[] args) {
        Singleton s1, s2;
        s1 = Singleton.getInstance();
        s2 = Singleton.getInstance();
        System.out.println(s1 == s2);

        Singleton singleton1 = Singleton.getInstance();
        Singleton singleton2 = null;
        try {
            Class<Singleton> clazz = Singleton.class;
            Constructor<Singleton> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            singleton2 = constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(singleton1.hashCode());
        System.out.println(singleton2.hashCode());
        System.out.println(singleton1 == singleton2);
    }

}
