package com.toy.webfluxcache.config.cache

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.core.ResolvableType
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.lang.reflect.ParameterizedType
import java.util.*
import java.util.stream.Collectors

@Aspect
@Component
class ReactorCacheAspect(
  private val reactorCacheManager: ReactorCacheManager
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Pointcut("@annotation(com.toy.webfluxcache.config.cache.ReactorCache)")
  fun cachePointcut() {
  }

  @Around("cachePointcut()")
  fun around(joinPoint: ProceedingJoinPoint): Any {
    val methodSignature = joinPoint.signature as MethodSignature
    val method = methodSignature.method

    val parameterizedType = method.genericReturnType as ParameterizedType
    val rawType = parameterizedType.rawType

    if (!(rawType.equals(Mono::class.java)))
      throw RuntimeException("method[${method.name}] type should be Mono or Flux type")

    val reactorCache = method.getAnnotation(ReactorCache::class.java)
    val cacheName = reactorCache.name
    val args = joinPoint.args

    val retriever = {
      try{
        joinPoint.proceed(args) as Mono<*>
      } catch (th: Throwable) {
        throw RuntimeException(th)
      }
    }

    val returnTypeInsideMono = parameterizedType.actualTypeArguments[0]
    val returnClass = ResolvableType.forType(returnTypeInsideMono).resolve() as Class<*>
    log.debug("start reactorCaching...")
    return reactorCacheManager
      .findCachedMono(cacheName, generateKey(*args)!!, retriever, returnClass)
      .doOnError { e -> log.error("Failed to processing mono cache. method: " + method.name, e) }
      .subscribeOn(Schedulers.boundedElastic())
  }

  private fun generateKey(vararg objects: Any): String? {
    return Arrays.stream(objects)
      .map { obj: Any? -> obj?.toString() ?: "" }
      .collect(Collectors.joining(":"))
  }
}