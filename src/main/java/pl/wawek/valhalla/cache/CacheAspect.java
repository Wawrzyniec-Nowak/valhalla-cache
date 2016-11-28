package pl.wawek.valhalla.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Aspect which holds logic behind the cache.
 */
@Aspect
public class CacheAspect {

    private ValhallaCache valhallaCache = ValhallaCache.getInstance();

    /**
     * Always before execution of any method annotated with @see Valhalla annotation
     * checks if the value is already in cache and return it. Otherwise call the method
     * and put the returned value to the cache.
     *
     * @param joinPoint the point of the execution
     * @return the cached object or the actual method call
     * @throws Throwable
     */
    @Around("execution(@pl.wawek.valhalla.cache.Valhalla ** *(..))")
    public Object checkCache(ProceedingJoinPoint joinPoint) throws Throwable {
        String cacheKey = buildCacheKey(joinPoint.getSignature(), joinPoint.getArgs());
        if (valhallaCache.contains(cacheKey)) {
            return valhallaCache.get(cacheKey);
        }
        Object objectToCache = joinPoint.proceed();

        valhallaCache.put(cacheKey, objectToCache);
        return objectToCache;
    }

    private String buildCacheKey(Signature signature, Object[] args) {
        String key = signature.toString();
        for (Object arg : args) {
            key += arg;
        }
        return key;
    }
}
