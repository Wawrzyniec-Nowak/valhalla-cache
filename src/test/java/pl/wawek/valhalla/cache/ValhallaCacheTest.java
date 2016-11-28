package pl.wawek.valhalla.cache;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.stream.IntStream;

public class ValhallaCacheTest {

    private ValhallaClass testClass = new ValhallaClass();

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
        String firstConcatArguments = testClass.concatArguments(1, "hello");
        long end = System.currentTimeMillis();
        long firstConcatenationTime = end - start;

        start = System.currentTimeMillis();
        String newConcatArguments = testClass.concatArguments(1, "world");
        end = System.currentTimeMillis();
        long newConcatenationTime = end - start;

        start = System.currentTimeMillis();
        String cachedConcatArguments = testClass.concatArguments(1, "hello");
        end = System.currentTimeMillis();
        long cachedConcatenationTime = end - start;

        Assert.assertTrue(firstConcatenationTime >= 25);
        Assert.assertTrue(newConcatenationTime >= 25);
        Assert.assertTrue(cachedConcatenationTime < 25);

        Assert.assertEquals(firstConcatArguments, cachedConcatArguments);
        Assert.assertNotEquals(firstConcatArguments, newConcatArguments);
        Assert.assertNotEquals(cachedConcatArguments, newConcatArguments);
    }

    @Test
    @Ignore("This should be tested in a better way instead of waiting so long.")
    public void cacheShouldBeClearedAfterTooManyCacheEntries() {
        final int cacheCapacity = 1000;
        IntStream.range(0, cacheCapacity + 3)
                .forEach(i -> testClass.concatArguments(i, "test"));

        long start = System.currentTimeMillis();
        testClass.concatArguments(cacheCapacity + 2, "test");
        long end = System.currentTimeMillis();

        Assert.assertTrue(end - start < 25);

        start = System.currentTimeMillis();
        testClass.concatArguments(0, "test");
        end = System.currentTimeMillis();
        Assert.assertTrue(end - start >= 25);
    }
}
