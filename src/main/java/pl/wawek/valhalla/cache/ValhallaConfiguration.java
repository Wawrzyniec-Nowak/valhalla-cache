package pl.wawek.valhalla.cache;

import pl.wawek.valhalla.cache.evict.Algorithm;

/**
 * Stores whole configuration for valhalla cache
 */
public class ValhallaConfiguration {

    private long capacity;
    private String name;
    private Algorithm algorithm;

    private ValhallaConfiguration(Config config) {
        capacity = config.capacity;
        name = config.name;
        algorithm = config.algorithm;
        ValhallaCache.initialize(algorithm, capacity);
    }


    public static final class Config {
        private long capacity;
        private String name;
        private Algorithm algorithm;

        public Config() {
        }

        public Config capacity(long val) {
            capacity = val;
            return this;
        }

        public Config name(String val) {
            name = val;
            return this;
        }

        public Config algorithm(Algorithm val) {
            algorithm = val;
            return this;
        }

        public ValhallaConfiguration configure() {
            return new ValhallaConfiguration(this);
        }
    }
}
