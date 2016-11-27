package pl.wawek.valhalla.cache;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ValhallaConfiguration.class)
public class ValhallaCacheTest {

    private ValhallaClass testClass = new ValhallaClass();

    private ValhallaCache valhallaCache = ValhallaCache.getInstance();

    @Test
    public void shouldReturnTheSameSystemMillis() {
        long currentMillis = testClass.currentTimeMillisAfterTwoSeconds();
        long nextMillis = testClass.currentTimeMillisAfterTwoSeconds();

        Assert.assertEquals(currentMillis, nextMillis);
    }

    @Test
    public void shouldCallTheSecondMethodImmediately() {
        testClass.currentTimeMillisAfterTwoSeconds();
        long start = System.currentTimeMillis();
        testClass.currentTimeMillisAfterTwoSeconds();
        long end = System.currentTimeMillis();

        Assert.assertTrue(end - start < 2000);
    }

    @Test
    public void shouldCacheFirstCallAndUseItDuringTheSameCall() {
        long start = System.currentTimeMillis();
        String firstConcatArguments = testClass.concatArgumentsAfterTwoSeconds(1, "hello");
        long end = System.currentTimeMillis();
        long firstConcatenationTime = end - start;

        start = System.currentTimeMillis();
        String newConcatArguments = testClass.concatArgumentsAfterTwoSeconds(1, "world");
        end = System.currentTimeMillis();
        long newConcatenationTime = end - start;

        start = System.currentTimeMillis();
        String cachedConcatArguments = testClass.concatArgumentsAfterTwoSeconds(1, "hello");
        end = System.currentTimeMillis();
        long cachedConcatenationTime = end - start;

        Assert.assertTrue(firstConcatenationTime >= 2000);
        Assert.assertTrue(newConcatenationTime >= 2000);
        Assert.assertTrue(cachedConcatenationTime < 2000);

        Assert.assertEquals(firstConcatArguments, cachedConcatArguments);
        Assert.assertNotEquals(firstConcatArguments, newConcatArguments);
        Assert.assertNotEquals(cachedConcatArguments, newConcatArguments);
    }

    @Test
    public void cacheShouldStoreOneElementForManyCalls() throws NoSuchFieldException, IllegalAccessException {
        IntStream.range(0, 10)
                .parallel()
                .forEach(i -> testClass.currentTimeMillisAfterTwoSeconds());

        Field cache = valhallaCache.getClass().getDeclaredField("cache");
        cache.setAccessible(true);
        ConcurrentHashMap<String, CachedObject> concurrentMap
                = (ConcurrentHashMap<String, CachedObject>) cache.get(valhallaCache);

        Assert.assertEquals(1, concurrentMap.size());
    }

    @Test
    public void jobShouldClearCache() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        testClass.currentTimeMillisAfterTwoSeconds();
        testClass.concatArgumentsAfterTwoSeconds(1, "Hello");

        Field cache = valhallaCache.getClass().getDeclaredField("cache");
        cache.setAccessible(true);
        ConcurrentHashMap<String, CachedObject> concurrentMap
                = (ConcurrentHashMap<String, CachedObject>) cache.get(valhallaCache);

        Assert.assertEquals(2, concurrentMap.size());
        Thread.sleep(10000);
        Assert.assertEquals(0, concurrentMap.size());
    }
}
