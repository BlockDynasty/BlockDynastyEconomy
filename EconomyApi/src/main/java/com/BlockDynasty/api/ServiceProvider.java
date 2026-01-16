package com.BlockDynasty.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceProvider {
    private static final Map<Class<?>, Object> SERVICES = new ConcurrentHashMap<>();

    public static <T> void register(Class<T> clazz, T service) {
        SERVICES.put(clazz, service);
    }

    public static <T> T get(Class<T> clazz) {
        return clazz.cast(SERVICES.get(clazz));
    }

    public static <T> void unregister(Class<T> clazz) {
        SERVICES.remove(clazz);
    }
}