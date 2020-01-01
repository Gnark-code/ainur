package fr.gnark.sound.domain.media.output;

import javax.sound.sampled.AudioFormat;

public class WavConstants {
    public static final double SAMPLE_RATE = 44100;
    public static final int SAMPLE_SIZE_IN_BITS = 16;
    public static final int CHANNELS = 2;
    public static final double GAIN = (Math.pow(2, SAMPLE_SIZE_IN_BITS) / 2) - 1;
    public static final int FRAME_SIZE = ((SAMPLE_SIZE_IN_BITS + 7) / 8) * CHANNELS;
    public static final double FRAME_RATE = SAMPLE_RATE;
    public static final boolean BIG_ENDIAN = false;
    public static final boolean SIGNED = true;
    public static final AudioFormat AUDIO_FORMAT = new AudioFormat((float) SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS, SIGNED,
            BIG_ENDIAN);
}
