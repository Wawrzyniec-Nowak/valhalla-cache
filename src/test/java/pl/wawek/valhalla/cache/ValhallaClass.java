package pl.wawek.valhalla.cache;

import org.apache.log4j.Logger;

class ValhallaClass {

    private Logger logger = Logger.getLogger(ValhallaClass.class);

    @Valhalla(maxAge = 2000)
    long currentTimeMillisAfterTwoSeconds() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            logger.warn("longRunningMethod interrupted");
        }
        return System.currentTimeMillis();
    }

    @Valhalla(maxAge = 2000)
    String concatArgumentsAfterTwoSeconds(int number, String word) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            logger.warn("longRunningMethod interrupted");
        }
        return word + number;
    }
}
