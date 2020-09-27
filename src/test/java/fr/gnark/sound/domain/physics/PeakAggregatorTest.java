package fr.gnark.sound.domain.physics;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class PeakAggregatorTest {

    @Test
    public void testMultiplePeaks() {
        final PeakAggregator peakAggregator = new PeakAggregator(25.0);
        final Peak firstExpected = new Peak(50.005, 1000);
        final Peak secondExpected = new Peak(500.005, 900);
        final Peak thirdExpected = new Peak(5000.005, 900);

        final List<Peak> peaks = Arrays.asList(
                new Peak(50, 120),
                new Peak(50.001, 100),
                firstExpected,
                new Peak(50.006, 110),
                new Peak(50.007, 130),
                secondExpected,
                new Peak(500.006, 899),
                thirdExpected
        );

        final List<Peak> aggregated = peakAggregator.proceed(peaks);
        Assertions.assertEquals(3, aggregated.size());
        Assertions.assertEquals(firstExpected, aggregated.get(0));
        Assertions.assertEquals(secondExpected, aggregated.get(1));
        Assertions.assertEquals(thirdExpected, aggregated.get(2));
    }

    @Test
    public void testMultipleEndingPeaks() {
        final PeakAggregator peakAggregator = new PeakAggregator(25.0);
        final Peak firstExpected = new Peak(50.005, 1000);
        final Peak secondExpected = new Peak(500.005, 900);
        final Peak thirdExpected = new Peak(5000.005, 9000);

        final List<Peak> peaks = Arrays.asList(
                new Peak(50, 120),
                new Peak(50.001, 100),
                firstExpected,
                new Peak(50.006, 110),
                new Peak(50.007, 130),
                secondExpected,
                new Peak(500.006, 899),
                thirdExpected,
                new Peak(5000.007, 200)
        );

        final List<Peak> aggregated = peakAggregator.proceed(peaks);
        Assertions.assertEquals(3, aggregated.size());
        Assertions.assertEquals(firstExpected, aggregated.get(0));
        Assertions.assertEquals(secondExpected, aggregated.get(1));
        Assertions.assertEquals(thirdExpected, aggregated.get(2));
    }

    @Test
    public void testMinPeaks() {
        final PeakAggregator peakAggregator = new PeakAggregator(25.0);
        final Peak firstExpected = new Peak(50.005, 1000);
        final Peak secondExpected = new Peak(500.005, 900);
        final Peak thirdExpected = new Peak(5000.005, 900);

        final List<Peak> peaks = Arrays.asList(
                firstExpected,
                secondExpected,
                thirdExpected
        );

        final List<Peak> aggregated = peakAggregator.proceed(peaks);
        Assertions.assertEquals(3, aggregated.size());
        Assertions.assertEquals(firstExpected, aggregated.get(0));
        Assertions.assertEquals(secondExpected, aggregated.get(1));
        Assertions.assertEquals(thirdExpected, aggregated.get(2));
    }
}