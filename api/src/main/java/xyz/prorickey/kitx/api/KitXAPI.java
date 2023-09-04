package xyz.prorickey.kitx.api;

public class KitXAPI {

    public static KitManager instance;
    public static void setInstance(KitManager inst) { instance = inst; }
    public static KitManager getInstance() { return instance; }

}
