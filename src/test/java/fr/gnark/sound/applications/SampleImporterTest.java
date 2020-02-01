package fr.gnark.sound.applications;

import fr.gnark.sound.domain.media.output.RealtimeAudioFormat;
import fr.gnark.sound.domain.media.output.WavConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Disabled("Needs a computer with sound card to run")
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
    public void maple() throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        double[] data = sampleImporter.stretch("classpath:samples/maple.wav", 1.0);
        Assertions.assertNotNull(data);
        for (final double v : data) {
            realtimeAudioFormat.storeDataMono(v);
        }
    }

    @Test
    public void pitchShift() throws Exception {
        double[] data = sampleImporter.pitchShift("classpath:samples/guitar_e_44100_mono.wav", 1.00);
        Assertions.assertNotNull(data);
        //  getWindows(data);
        for (final double v : data) {
            realtimeAudioFormat.storeDataMono(v);
        }
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