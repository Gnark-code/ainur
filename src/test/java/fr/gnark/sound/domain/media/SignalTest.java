package fr.gnark.sound.domain.media;

import fr.gnark.sound.adapter.ChordProgressionToEvents;
import fr.gnark.sound.applications.Instruments;
import fr.gnark.sound.domain.media.output.AudioFormatOutput;
import fr.gnark.sound.domain.media.waveforms.EnvelopeADSR;
import fr.gnark.sound.domain.media.waveforms.SineWave;
import fr.gnark.sound.domain.music.*;
import graphql.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class used to debug Signal processing
 */
@Disabled("Useful to debug waveforms")
class SignalTest {
    private Instruments instruments = new Instruments();
    private static int BPM = 500;
    private static final int TICKS_BY_WHOLE_NOTE = 128;
    final double definitionInMs = 60000.0 / (BPM * (TICKS_BY_WHOLE_NOTE / 4.0));
    private final Player player;

    public SignalTest() throws LineUnavailableException {
        player = new Player(new Dispatcher(64, instruments.getProxy()), definitionInMs);
    }

    private static final ChordProgressionToEvents CHORD_PROGRESSION_TO_EVENTS = new ChordProgressionToEvents(TICKS_BY_WHOLE_NOTE);


    @Test
    void computeBuffer() throws InterruptedException {
        Signal signal = new SineWave()
                .initBufferIfNecessary(440);
        List<Double> buffer = signal.getBuffer().get(440.0);
        double[] xData = new double[buffer.size()];
        double[] yData = new double[buffer.size()];
        for (int i = 0; i < buffer.size(); i++) {
            xData[i] = i;
            yData[i] = buffer.get(i);
        }

        // Create Chart
        //XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);
        XYChart chart = new XYChart(1200, 800);
        chart.setTitle("Signal over time");
        chart.setXAxisTitle("amplitude");
        chart.setYAxisTitle("time");
        chart.addSeries("test", xData, yData);
        Assert.assertNotNull(chart);
        // Show it
        new SwingWrapper<>(chart).displayChart();
        Thread.sleep(10000);
    }


    @Test
    void debugBuffer() throws InterruptedException, LineUnavailableException {
        final AudioFormatOutput audioFormatOutput = new AudioFormatOutput();

        Signal signal = new SineWave();

        final long throughput = audioFormatOutput.getThroughputInBytes();
        final int frameSize = audioFormatOutput.getFrameSize();
        final double frameRate = audioFormatOutput.getFrameRate();
        double seconds = 0.1;
        int nbBytes = (int) (seconds * throughput);
        // truncate to the nearest complete frame
        nbBytes = nbBytes - (nbBytes % frameSize);
        List<Double> buffer = new ArrayList<>();
        for (int sample = 0; sample < nbBytes; sample = sample + frameSize) {
            double computed = 0;
            double time = sample / (double) throughput;
            computed += signal.computeFormula(440.0, time);
            buffer.add(computed);
        }
        double[] xData = new double[buffer.size()];
        double[] yData = new double[buffer.size()];
        for (int i = 0; i < buffer.size(); i++) {
            xData[i] = i;
            yData[i] = buffer.get(i);
        }

        // Create Chart
        //XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);
        XYChart chart = new XYChart(1200, 800);
        chart.setTitle("Signal over time");
        chart.setXAxisTitle("amplitude");
        chart.setYAxisTitle("time");
        chart.addSeries("test", xData, yData);
        Assert.assertNotNull(chart);
        // Show it
        new SwingWrapper<>(chart).displayChart();
        Thread.sleep(10000);
    }

    @Test
    void probe() throws InterruptedException, LineUnavailableException {
        Encoder encoder = new Encoder("SAWTOOTH", definitionInMs, new AudioFormatOutput(),
                new SineWave());
        encoder.setEnvelope(EnvelopeADSR.builder()
                .attackInSeconds(0.5)
                .decayInSeconds(0.01)
                .sustainFactorinDbfs(-3.0)
                .releaseInSeconds(0.8)
                .build());
        final RythmicPatterns rythmicPatterns = new RythmicPatterns();
        Subdivision subdivision = Subdivision.builder().type(Subdivision.Type.WHOLE).build();
        Subdivision pause = Subdivision.builder().type(Subdivision.Type.WHOLE).isPause(true).build();

        rythmicPatterns
                .addRythmicPattern(Arrays.asList(subdivision), Degree.I, Alterations.emptyAlterations(),
                        3,
                        PlayStyle.UNISON)
                .addRythmicPattern(Arrays.asList(subdivision), Degree.III, Alterations.emptyAlterations(),
                        3,
                        PlayStyle.UNISON)
                .addRythmicPattern(Arrays.asList(subdivision), Degree.IV, Alterations.emptyAlterations(),
                        3,
                        PlayStyle.UNISON)
                .addRythmicPattern(Arrays.asList(subdivision), Degree.V, Alterations.emptyAlterations(),
                        3,
                        PlayStyle.UNISON)
        ;

        final ChordProgression chordProgression = ChordProgression.builder()
                .mode(Mode.MAJOR)
                .rootNote(Note.builder().octave(2)
                        .baseNote(BaseNote.E_FLAT)
                        .build())
                // .bassPattern(Chord.BassPattern.OCTAVE_BASS)
                .rythmicPatterns(rythmicPatterns)
                .build();
        Assert.assertNotNull(chordProgression);

        //index in the resolution
        CHORD_PROGRESSION_TO_EVENTS.map(chordProgression)
                .forEach(player::postEvents);
        player.play();
        List<Double> buffer = encoder.data;

        int firstWindowMin = 20000;
        int firstWindowMax = 25000;

        int minBound = 60500;
        int maxBound = 67000;
        //int minBound =0;
        //int maxBound = buffer.size();
        int windowSize = maxBound - minBound;
        double[] x1Data = new double[firstWindowMax - firstWindowMin];
        double[] y1Data = new double[firstWindowMax - firstWindowMin];
        double[] xData = new double[windowSize];
        double[] yData = new double[windowSize];
        double[] fullXData = new double[buffer.size()];
        double[] fullYData = new double[buffer.size()];
        int firstWindowIndex = 0;
        int index = 0;
        for (int i = 0; i < buffer.size(); i++) {
            final double value = buffer.get(i) * Math.pow(2, 16);
            fullXData[i] = i;
            fullYData[i] = value;
            if (i >= minBound && i < maxBound) {
                xData[index] = i;
                yData[index] = value;
                index++;
            }
            if (i >= firstWindowMin && i < firstWindowMax) {
                x1Data[firstWindowIndex] = i;
                y1Data[firstWindowIndex] = value;
                firstWindowIndex++;
            }
        }

        // Create Chart
        XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);
        XYChart chart3 = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", x1Data, y1Data);

        XYChart chart2 = QuickChart.getChart("Sample Chart2", "X", "Y", "y(x)", fullXData, fullYData);

        Assert.assertNotNull(chart);
        // Show it
        new SwingWrapper<>(chart2).displayChart();
        new SwingWrapper<>(chart).displayChart();
        new SwingWrapper<>(chart3).displayChart();
        Thread.sleep(100000);
    }
}