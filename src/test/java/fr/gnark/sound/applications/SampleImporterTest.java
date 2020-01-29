package fr.gnark.sound.applications;

import fr.gnark.sound.domain.media.output.RealtimeAudioFormat;
import fr.gnark.sound.domain.media.output.WavConstants;
import fr.gnark.sound.domain.physics.PhaseVocoder;
import fr.gnark.sound.domain.physics.waveforms.SineWave;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

import static fr.gnark.sound.domain.media.output.WavConstants.SAMPLE_RATE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
    public void importSample() throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        double[] data = sampleImporter.importSample("classpath:samples/guitar_e_44100_mono.wav");
        getWindows(data);
    }

    @Test
    public void pitchShift() throws IOException, UnsupportedAudioFileException, InterruptedException {
        double[] data = sampleImporter.pitchShift("classpath:samples/guitar_e_44100_mono.wav", 2.0);
        //   getWindows(data);
        for (final double v : data) {
            realtimeAudioFormat.storeDataMono(v);
        }
    }

    @Test
    public void pitchShift2() throws IOException, UnsupportedAudioFileException, InterruptedException {
        SineWave sineWave = new SineWave();
        double[] input = new double[(int) SAMPLE_RATE];
        for (int i = 0; i < input.length; i++) {
            input[i] = sineWave.computeFormula(100, i / (100 * SAMPLE_RATE));
        }

        // PitchShift pitchShift = new PitchShift(SAMPLE_RATE,1024);
        PhaseVocoder phaseVocoder = new PhaseVocoder(SAMPLE_RATE, 2048, 4.0);
        getWindows(phaseVocoder.proceed(input));
    }

    private void getWindows(final double[] data, final int windowSize) throws InterruptedException {
        oscilloscope.addChart("new signal", "time", "amplitude", data);
        for (int i = 0; i < 1; i++) {
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