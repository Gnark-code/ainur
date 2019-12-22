package fr.gnark.sound.domain.media;

import fr.gnark.sound.domain.media.waveforms.SquareWave;
import graphql.Assert;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.util.List;

/**
 * Class used to debug Signal processing
 */
//@Disabled("Useful to debug waveforms")
class SignalTest {

    @Test
    void computeBuffer() throws InterruptedException {
        Signal signal = new SquareWave()
                .addHarmonics(200)
                .initBufferIfNecessary(440);
        List<Double> buffer = signal.getBuffer().get(440.0);
        double[] xData = new double[buffer.size()];
        double[] yData = new double[buffer.size()];
        for (int i = 0; i < buffer.size(); i++) {
            xData[i] = i;
            yData[i] = buffer.get(i);
        }

        // Create Chart
        XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);
        Assert.assertNotNull(chart);
        // Show it
        new SwingWrapper<>(chart).displayChart();
        Thread.sleep(10000);
    }

}