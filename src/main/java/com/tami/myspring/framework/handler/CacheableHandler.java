package com.tami.myspring.framework.handler;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.tami.myspring.framework.annotation.Cacheable;

public class CacheableHandler extends AbstractProxyHandler {

    private final Map<List<Object>, Object> cacheContainer = new ConcurrentHashMap<>();

    public CacheableHandler(final Object objectToHandle) {
        super(objectToHandle, Cacheable.class);
    }

    public List<Object> createKeyCache(final Method method, Object[] args) {
        return List.of(method, Arrays.asList(args));
    }

    public Object takeResultOrCalculate(final Method method, Object[] args, final Supplier<Object> resultSupplier) {
        final List<Object> keyCache = createKeyCache(method, args);
        return cacheContainer.computeIfAbsent(keyCache, key -> resultSupplier.get());
    }
}
