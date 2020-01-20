package fr.gnark.sound.applications;

import fr.gnark.sound.domain.media.output.RealtimeAudioFormat;
import fr.gnark.sound.domain.media.output.WavConstants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SampleImporterTest {

    @Autowired
    private SampleImporter sampleImporter;
    @Autowired
    private Oscilloscope oscilloscope;
    private final  RealtimeAudioFormat realtimeAudioFormat;

    SampleImporterTest() throws LineUnavailableException {
        realtimeAudioFormat = new RealtimeAudioFormat(WavConstants.AUDIO_FORMAT_MONO);
    }

    @BeforeAll
    public static void init(){
        System.setProperty("java.awt.headless","false");
    }
    @Test
    public void importSample() throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        double[] data =  sampleImporter.importSample("classpath:samples/guitar_e_44100_mono.wav");
        getWindows(data);
    }

    @Test
    public void pitchShift() throws IOException, UnsupportedAudioFileException, InterruptedException {

        double[] data = sampleImporter.pitchShift("classpath:samples/guitar_e_44100_mono.wav",3.0);
        getWindows(data);
       /*  for (final double v : data) {
            realtimeAudioFormat.storeDataMono(v);
        }  */
    }

    private void getWindows( final  double[] data ) throws InterruptedException {
        oscilloscope.addChart("new signal", "time", "amplitude", data);
        for(int i =0 ; i<10; i++) {
            double[] window = new double[1024];
            System.arraycopy(data,i*window.length, window,0,window.length);
            oscilloscope.addChart("new signal"+i, "time", "amplitude", window);
        }
        oscilloscope.displayAll();
        Thread.sleep(500000);
    }
}