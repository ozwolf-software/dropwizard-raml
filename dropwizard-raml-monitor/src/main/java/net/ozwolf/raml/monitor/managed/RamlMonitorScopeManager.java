package net.ozwolf.raml.monitor.managed;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.ozwolf.raml.validator.ApiRequest;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RamlMonitorScopeManager {
    private final Cache<String, RamlMonitorScope> cache;

    private final static RamlMonitorScopeManager INSTANT = new RamlMonitorScopeManager();

    private RamlMonitorScopeManager() {
        this.cache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).build();
    }

    public String addRequest(ApiRequest request) {
        String id = UUID.randomUUID().toString();
        this.cache.put(id, new RamlMonitorScope(request));
        return id;
    }

    public RamlMonitorScope borrow(String scopeId) {
        return this.cache.getIfPresent(scopeId);
    }

    public synchronized RamlMonitorScope take(String scopeId) {
        RamlMonitorScope scope = cache.getIfPresent(scopeId);
        cache.invalidate(scopeId);
        return scope;
    }

    public static RamlMonitorScopeManager instant() {
        return INSTANT;
    }
}
