package com.mike.lunchvoter.caching;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnBean(CacheEvictionScheduler.class)
public class CacheEvictionService {

    @CacheEvict(cacheNames = {"restaurantsToday", "menusToday"}, allEntries = true)
    public void evictCaches() {
        log.info("RestaurantsToday and MenusToday caches have been evicted");
    }

}
