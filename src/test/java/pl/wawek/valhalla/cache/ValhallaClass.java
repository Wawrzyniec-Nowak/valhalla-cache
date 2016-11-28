package pl.wawek.valhalla.cache;

import org.apache.log4j.Logger;
import pl.wawek.valhalla.cache.algorithm.AlgorithmType;

class ValhallaClass {

    private Logger logger = Logger.getLogger(ValhallaClass.class);

    @Valhalla(algorithm = AlgorithmType.LRU)
    long currentTimeMillisAfterTwoSeconds() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            logger.warn("longRunningMethod interrupted");
        }
        return System.currentTimeMillis();
    }

    @Valhalla
    String concatArguments(int number, String word) {
        try {
            Thread.sleep(25);
        } catch (InterruptedException e) {
            logger.warn("longRunningMethod interrupted");
        }
        return word + number;
    }
}
