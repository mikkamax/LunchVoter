package com.mike.lunchvoter.caching;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@ConditionalOnProperty(name = "cache.scheduled-eviction.enabled", havingValue = "true")
public class CacheEvictionScheduler {

    private final CacheEvictionService cacheEvictionService;

    @Autowired
    public CacheEvictionScheduler(CacheEvictionService cacheEvictionService) {
        this.cacheEvictionService = cacheEvictionService;
    }

    @Scheduled(cron = "${cache.scheduled-eviction.cron}")
    public void cacheEvictionScheduler() {
        log.info("Executing scheduled cache eviction");
        cacheEvictionService.evictCaches();
    }

}
