package com.jalgoarena.web

import org.springframework.cache.CacheManager
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.inject.Inject

@RestController
class CacheController(@Inject private val cacheManager: CacheManager) {

    @GetMapping("/cache/reset")
    fun resetCache() {
        cacheManager.cacheNames.forEach{
            cacheManager.getCache(it).clear()
        }
    }
}
