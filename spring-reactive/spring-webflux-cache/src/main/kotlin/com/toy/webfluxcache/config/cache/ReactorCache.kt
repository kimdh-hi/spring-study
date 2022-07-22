package com.toy.webfluxcache.config.cache

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ReactorCache(val name: String)

