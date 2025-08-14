package com.mrakaki.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExpiringMap {
    private final ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void put(String key, String value, long duration, TimeUnit unit) {
        map.put(key, value);
        scheduler.schedule(() -> map.remove(key), duration, unit);
    }

    public void remove(String key) {
        map.remove(key);
    }

    public String get(String key) {
        return map.get(key);
    }
}
