package fr.gnark.sound.domain.physics;

import fr.gnark.sound.domain.anticorruption.ChordProgressionToEvents;
import fr.gnark.sound.domain.physics.waveforms.SineWave;
import graphql.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

/**
 * Class used to debug Signal processing
 */
@Disabled("Useful to debug waveforms")
class SignalTest {
    private static int BPM = 500;
    private static final int TICKS_BY_WHOLE_NOTE = 128;
    final double definitionInMs = 60000.0 / (BPM * (TICKS_BY_WHOLE_NOTE / 4.0));


    private static final ChordProgressionToEvents CHORD_PROGRESSION_TO_EVENTS = new ChordProgressionToEvents(TICKS_BY_WHOLE_NOTE);


    @Test
    void computeBuffer() throws InterruptedException {
        Signal signal = new SineWave()
                .initBufferIfNecessary(440);
        double[] buffer = signal.getBuffer().get(440.0);
        double[] xData = new double[buffer.length];
        double[] yData = new double[buffer.length];
        for (int i = 0; i < buffer.length; i++) {
            xData[i] = i;
            yData[i] = buffer[i];
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

/*
    @Test
    void probe() throws InterruptedException, LineUnavailableException {
        WavEncoder wavEncoder = new WavEncoder("SAWTOOTH", definitionInMs,
                new SineWave());
        wavEncoder.setEnvelope(EnvelopeADSR.builder()
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

 */
}