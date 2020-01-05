package fr.gnark.sound.domain.media.output;

import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import static fr.gnark.sound.domain.media.output.WavConstants.*;

@Slf4j
public class RealtimeAudioFormat {
    private final SourceDataLine line;
    private final SlidingWindow slidingWindow;

    public RealtimeAudioFormat() throws LineUnavailableException {
        line = AudioSystem.getSourceDataLine(AUDIO_FORMAT_STEREO);
        slidingWindow = new SlidingWindow((int) SAMPLE_RATE, FRAME_SIZE, (int) SAMPLE_RATE / 100);
        slidingWindow.setFlushOutput(this::processData);
    }


    private void processData(byte... data) {
        try {
            if (!line.isOpen()) {
                line.open(AUDIO_FORMAT_STEREO, data.length * 2);
                line.start();
            }

            line.write(data, 0, data.length);
        } catch (Exception e) {
            log.error("error caught", e);
        }
    }

    public void storeData(final double levelLeft, final double levelRight) {
        int castLevelLeft = (int) (levelLeft * GAIN);
        int castLevelRight = (int) (levelRight * GAIN);
        slidingWindow.write((byte) (castLevelLeft),
                (byte) (castLevelLeft >>> 8),
                (byte) (castLevelRight),
                (byte) (castLevelRight >>> 8));

    }

    public void cleanup() {
        if (this.line.isOpen()) {
            this.line.drain();
        }
        this.line.close();
    }

    public double getFrameRate() {
        return FRAME_RATE;
    }

    public void flush() {
    }


}
