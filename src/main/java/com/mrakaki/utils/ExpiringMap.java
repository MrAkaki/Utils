package com.mrakaki.utils;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExpiringMap<K extends Serializable, V> {
    private final ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void put(K key, V value, long duration, TimeUnit unit) {
        map.put(key, value);
        scheduler.schedule(() -> map.remove(key), duration, unit);
    }

    public void remove(K key) {
        map.remove(key);
    }

    public V get(K key) {
        return map.get(key);
    }
}
