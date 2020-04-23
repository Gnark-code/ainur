package fr.gnark.sound.applications;

import fr.gnark.sound.domain.media.output.RealtimeAudioFormat;
import fr.gnark.sound.domain.media.output.WavConstants;
import fr.gnark.sound.domain.music.BaseNote;
import fr.gnark.sound.domain.music.Note;
import fr.gnark.sound.domain.physics.Peak;
import fr.gnark.sound.domain.physics.PeakAggregator;
import fr.gnark.sound.domain.physics.PeakFinder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.gnark.sound.domain.media.output.WavConstants.SAMPLE_RATE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Disabled("Needs a computer with sound card to run")
class SampleImporterTest {

    @Autowired
    private SampleImporter sampleImporter;
    @Autowired
    private Oscilloscope oscilloscope;
    private final RealtimeAudioFormat realtimeAudioFormat;

    SampleImporterTest() throws LineUnavailableException {
        realtimeAudioFormat = new RealtimeAudioFormat(WavConstants.AUDIO_FORMAT_MONO);
    }

    @BeforeAll
    public static void init() {
        System.setProperty("java.awt.headless", "false");
    }



    @Test
    public void maple() throws IOException {
        double[] data = sampleImporter.stretch("classpath:samples/maple.wav", 1.0);
        Assertions.assertNotNull(data);
        for (final double v : data) {
            realtimeAudioFormat.storeDataMono(v);
        }
    }

    @Test
    public void pitchShift() throws Exception {
        double[] data = sampleImporter.pitchShift("classpath:samples/guitar_e_44100_mono.wav", 1.0);
        Assertions.assertNotNull(data);
        //  getWindows(data);
        for (final double v : data) {
            realtimeAudioFormat.storeDataMono(v);
        }
    }

    @Test
    public void peakFinder() throws Exception {
        final double cents = 1.0;
        double[] data = sampleImporter.getWavBuffer("classpath:samples/maple.wav");
        final PeakFinder peakFinder = new PeakFinder(SAMPLE_RATE, 0.05f);
        final PeakAggregator peakAggregator = new PeakAggregator(cents);
        final List<Peak> peaks = peakAggregator.proceed(peakAggregator.proceed(peakFinder.proceed(data)));

        final List<Note> aggregates = peaks.stream()
                .map(Peak::getFrequency)
                .map(freq -> Note.getFromFrequency(freq, cents))
                .filter(Optional::isPresent)
                .map(Optional::get)

                .collect(Collectors.toList());
        final Set<BaseNote> baseNotes = aggregates.stream().map(Note::getBaseNote).collect(Collectors.toSet());
        Assertions.assertNotNull(peaks);
    }

    private void getWindows(final double[] data, final int windowSize) throws InterruptedException {
        oscilloscope.addChart("new signal", "time", "amplitude", data);
        for (int i = 0; i < 2; i++) {
            double[] window = new double[windowSize];
            System.arraycopy(data, i * window.length, window, 0, window.length);
            oscilloscope.addChart("new signal" + i, "time", "amplitude", window);
        }
        oscilloscope.displayAll();
        Thread.sleep(500000);
    }

    private void getWindows(final double[] data) throws InterruptedException {
        this.getWindows(data, 1024);
    }
}