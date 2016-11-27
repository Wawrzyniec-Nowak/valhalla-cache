package pl.wawek.valhalla.cache.quartz;

import org.apache.log4j.Logger;
import org.quartz.*;
import pl.wawek.valhalla.cache.ValhallaCache;

/**
 * Implements quartz Job interface. Responsible for clearing cache.
 */
public class OdinAngerJob implements Job {

    private static final Logger log = Logger.getLogger(OdinAngerJob.class);

    /**
     * Clears the cache from the expired objects.
     * @param jobExecutionContext passed context which holds scheduler context and it
     *                            passes the cache instance which needs to be cleared
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            SchedulerContext schedulerContext = jobExecutionContext.getScheduler().getContext();
            ValhallaCache cache = (ValhallaCache) schedulerContext.get("cache");
            cache.clearExpiredObjects();
        } catch (SchedulerException e) {
            log.warn("Exception during obtaining scheduler context", e);
        }
    }
}
