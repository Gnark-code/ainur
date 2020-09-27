package fr.gnark.sound.applications;

import fr.gnark.sound.domain.media.WavFile;
import fr.gnark.sound.domain.physics.PhaseVocoder;
import fr.gnark.sound.domain.physics.PitchShift;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SampleImporter {
    private static final int WINDOW_SIZE = 8192;
    private ResourceLoader resourceLoader;
    private PitchShift pitchShift;

    public SampleImporter(final ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.pitchShift = new PitchShift(WINDOW_SIZE);
    }


    public double[] stretch(final String resourcePath, final double ratio) {
        final double[] buffer = getWavBuffer(resourcePath);
        return new PhaseVocoder(WINDOW_SIZE, ratio).proceed(buffer);
    }

    /**
     * Extracts data from a wav file.
     * Only mono has been tested for now, and it must be considered when using the double array returned.
     */
    public double[] getWavBuffer(final String resourcePath) {
        final Resource resource = resourceLoader.getResource(resourcePath);

        final double[] buffer = new double[(int) Math.pow(2, 25)];
        boolean isStereo = false;
        try {
            final WavFile wavFile = WavFile.openWavFile(resource.getFile());
            isStereo = wavFile.getNumChannels() == 2;
            wavFile.readFrames(buffer, buffer.length);
            wavFile.close();
        } catch (final Exception e) {
            log.error("error caught while reading wav file", e);
        }
        if (isStereo) {
            //merge interlaced samples
            final double[] mergedBuffer = new double[buffer.length / 2];
            int j = 0;
            for (int i = 0; i < buffer.length - 2; i = i + 2) {
                mergedBuffer[++j] = ((buffer[i] + buffer[i + 1]) / 2.0);
            }
            return mergedBuffer;
        }
        return buffer;
    }

    public double[] pitchShift(final String resourcePath, final double ratio) {
        final double[] buffer = getWavBuffer(resourcePath);
        return pitchShift.shift(buffer, ratio);
    }

    public double[] pitchShift(final double[] data, final double ratio) {
        return pitchShift.shift(data, ratio);
    }
}
