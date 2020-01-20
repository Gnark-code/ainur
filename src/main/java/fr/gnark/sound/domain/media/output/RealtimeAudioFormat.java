package fr.gnark.sound.domain.media.output;

import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import static fr.gnark.sound.domain.media.output.WavConstants.*;

/**
 * plays encoded bytes to the sound card.
 * It uses a sliding window to write data each 10 ms
 */
@Slf4j
public class RealtimeAudioFormat {
    private final SourceDataLine line;
    private final SlidingWindow slidingWindow;
    private final AudioFormat audioFormat;
    public RealtimeAudioFormat() throws LineUnavailableException {
        this(AUDIO_FORMAT_STEREO);
    }

    public RealtimeAudioFormat(final AudioFormat audioFormat) throws LineUnavailableException {
        line = AudioSystem.getSourceDataLine(audioFormat);
        slidingWindow = new SlidingWindow((int) audioFormat.getFrameRate(), audioFormat.getFrameSize(),  (int) audioFormat.getFrameRate()/ 100);
        slidingWindow.setFlushOutput(this::processData);
        this.audioFormat = audioFormat;
    }
    private void processData(byte... data) {
        try {
            if (!line.isOpen()) {
                line.open(audioFormat, data.length * 2);
                line.start();
            }

            line.write(data, 0, data.length);
        } catch (Exception e) {
            log.error("error caught", e);
        }
    }

    public void storeDataMono(final double level) {
        int castLevel = (int) (level * GAIN);
        slidingWindow.write(
                (byte) (castLevel),
                (byte) (castLevel >>> 8));

    }

    public void storeDataStereo(final double levelLeft, final double levelRight) {
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

}
