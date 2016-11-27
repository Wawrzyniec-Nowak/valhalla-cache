package pl.wawek.valhalla.cache.quartz;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OdinAngerConfiguration {

    @Bean
    public OdinAngerLoader odinAngerLoader() {
        return OdinAngerLoader.getInstance();
    }
}
