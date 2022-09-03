package cn.nyaaar.partridgemngservice.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description: Caffeine缓存，本地缓存
 * @author: yuegenhua
 * @date: 2021/7/23
 */
@Configuration
@EnableCaching
public class CacheConfig {

    private static final int DEFAULT_EXPIRE_TIME = 7200;
    private static final int DEFAULT_MAX_SIZE = 50000;

    public static final String pToken="PTOKEN";
    @Bean
    public CacheManager caffeineCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<CaffeineCache> caffeineCaches = new ArrayList<>();
        for (CacheType cacheType : CacheType.values()) {
            caffeineCaches.add(new CaffeineCache(cacheType.name(),
                    Caffeine.newBuilder()
                            .recordStats()
                            .expireAfterWrite(cacheType.getExpires(), TimeUnit.SECONDS)
                            .maximumSize(cacheType.getMaxSize())
                            .build()));
        }
        cacheManager.setCaches(caffeineCaches);
        return cacheManager;
    }

    @Getter
    public enum CacheType {

        // page token
        PTOKEN(DEFAULT_EXPIRE_TIME, DEFAULT_MAX_SIZE),
        ;

        private final int expires;

        private final int maxSize;

        CacheType(int expires, int maxSize) {
            this.expires = expires;
            this.maxSize = maxSize;
        }

    }


}
