package pl.wawek.valhalla.cache.quartz;

import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import pl.wawek.valhalla.cache.ValhallaCache;

/**
 * Initialized job responsible for clearing expired objects from cache.
 * Creates a trigger definition and fires the job.
 */
class OdinAngerLoader {

    private static final Logger logger = Logger.getLogger(OdinAngerLoader.class);

    private OdinAngerLoader() {
        JobDetail job = job("cacheClearerJob");
        Trigger trigger = trigger("cacheClearerTrigger");

        try {
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.getContext().put("cache", cache);
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            logger.warn("Exception during scheduling the job", e);
        }
    }

    /**
     * Helper class responsible for lazy loading the singleton instance of the scheduler.
     */
    private static class LazyLoader {
        private static final OdinAngerLoader INSTANCE = new OdinAngerLoader();
    }

    /**
     * Initializes the scheduler on demand.
     *
     * @return the cache instance
     */
    public static OdinAngerLoader getInstance() {
        return OdinAngerLoader.LazyLoader.INSTANCE;
    }

    /**
     * For this moment job clearing cache is executed every five seconds.
     */
    private static final int INTERVAL_IN_SECONDS = 5;

    private ValhallaCache cache = ValhallaCache.getInstance();

    private JobDetail job(final String jobIdentity) {
        return JobBuilder.newJob(OdinAngerJob.class)
                .withIdentity(jobIdentity)
                .build();
    }

    private Trigger trigger(final String triggerIdentity) {
        return TriggerBuilder
                .newTrigger()
                .withIdentity(triggerIdentity)
                .withSchedule(
                        SimpleScheduleBuilder
                                .simpleSchedule()
                                .withIntervalInSeconds(INTERVAL_IN_SECONDS)
                                .repeatForever())
                .build();
    }
}
