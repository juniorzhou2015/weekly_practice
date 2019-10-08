package weekly.practice.d191008;

import java.io.Serializable;

public class Singleton implements Serializable {

    private Singleton() {
        throw new RuntimeException("Can't new Singleton from reflection");
    }

    private static class Holder {
        private static final transient Singleton SINGLETON = new Singleton();
    }

    public static Singleton getInstance() {
        return Holder.SINGLETON;
    }

//    protected Object readResolve() {
//        return getInstance();
//    }

}
