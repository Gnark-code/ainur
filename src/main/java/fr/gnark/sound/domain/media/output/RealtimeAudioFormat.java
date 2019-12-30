package fr.gnark.sound.domain.media.output;

import fr.gnark.sound.domain.media.Output;
import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

@Slf4j
public class RealtimeAudioFormat implements Output {
    public static final double SAMPLE_RATE = 44100;
    protected static final int SAMPLE_SIZE_IN_BITS = 16;
    protected static final int CHANNELS = 2;
    protected static final double GAIN = (Math.pow(2, SAMPLE_SIZE_IN_BITS) / 2) - 1;
    protected static final int FRAME_SIZE = ((SAMPLE_SIZE_IN_BITS + 7) / 8) * CHANNELS;
    protected static final double FRAME_RATE = SAMPLE_RATE;
    protected static final boolean BIG_ENDIAN = false;
    protected static final boolean SIGNED = true;
    private final AudioFormat format;
    private final SourceDataLine line;
    private final SlidingWindow slidingWindow;

    public RealtimeAudioFormat() throws LineUnavailableException {
        this.format = new AudioFormat((float) SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS, SIGNED,
                BIG_ENDIAN);
        line = AudioSystem.getSourceDataLine(format);
        slidingWindow = new SlidingWindow((int) SAMPLE_RATE, FRAME_SIZE, (int) SAMPLE_RATE / 100);
        slidingWindow.setFlushOutput(this::processData);
    }


    private void processData(byte... data) {
        try {
            if (!line.isOpen()) {
                line.open(format, data.length * 2);
                line.start();
            }
            line.write(data, 0, data.length);
        } catch (Exception e) {
            log.error("error caught", e);
        }
    }

    @Override
    public void storeData(final double levelLeft, final double levelRight) {
        int castLevelLeft = (int) (levelLeft * GAIN);
        int castLevelRight = (int) (levelRight * GAIN);
        slidingWindow.write((byte) (castLevelLeft),
                (byte) (castLevelLeft >>> 8),
                (byte) (castLevelRight),
                (byte) (castLevelRight >>> 8));

    }

    @Override
    public long getThroughputInBytes() {
        return (long) (FRAME_SIZE * FRAME_RATE);
    }

    @Override
    public int getFrameSize() {
        return FRAME_SIZE;
    }

    @Override
    public byte[] getBuffer() {
        return slidingWindow.window;
    }

    @Override
    public void cleanup() {
        if (this.line.isOpen()) {
            this.line.drain();
        }
        this.line.close();
    }

    @Override
    public double getFrameRate() {
        return FRAME_RATE;
    }

    public void flush() {
    }


}
