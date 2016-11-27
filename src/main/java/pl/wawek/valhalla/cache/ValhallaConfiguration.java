package pl.wawek.valhalla.cache;

import org.aspectj.lang.Aspects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableLoadTimeWeaving;

@Configuration
@EnableLoadTimeWeaving(aspectjWeaving = EnableLoadTimeWeaving.AspectJWeaving.ENABLED)
@ComponentScan(basePackages = "pl.wawek.valhalla.cache")
class ValhallaConfiguration {

    @Bean
    public CacheAspect cacheAspect() {
        return Aspects.aspectOf(CacheAspect.class);
    }

}
