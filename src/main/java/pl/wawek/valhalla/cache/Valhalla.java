package pl.wawek.valhalla.cache;

import pl.wawek.valhalla.cache.algorithm.AlgorithmType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Valhalla {

    AlgorithmType algorithm() default AlgorithmType.LRU;
}
