package com.mrakaki.utils;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimiter {
    private final int maxRequests;
    private final long timeWindowMillis;
    private final AtomicInteger requestCount;
    private long lastResetTime;

    private final Object lock = new Object();

    public RateLimiter(int maxRequests, long timeWindow, TimeUnit timeUnit) {
        this.maxRequests = maxRequests;
        this.timeWindowMillis = timeUnit.toMillis(timeWindow);
        this.requestCount = new AtomicInteger(0);
        this.lastResetTime = System.currentTimeMillis();
    }

    public synchronized boolean allowRequest() {
        long currentTime = System.currentTimeMillis();

        // Reset the counter if the time window has passed
        if (currentTime - lastResetTime > timeWindowMillis) {
            requestCount.set(0);
            lastResetTime = currentTime;
        }

        // Check if the request is within the limit
        if (requestCount.get() < maxRequests) {
            requestCount.incrementAndGet();
            return true;
        }

        return false; // Rate limit exceeded
    }

    public void waitForAvailability() {
        while (!allowRequest()) {
            synchronized (this) {
                long waitTime = timeWindowMillis - (System.currentTimeMillis() - lastResetTime);
                if (waitTime > 0) {
                    try {
                        this.wait(waitTime);
                    } catch (InterruptedException e) {
                        System.out.println("Thread interrupted while waiting for rate limit availability.");
                    }
                }
            }
        }
    }
}