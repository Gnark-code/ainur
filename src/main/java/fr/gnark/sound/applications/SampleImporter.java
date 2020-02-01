package fr.gnark.sound.applications;

import fr.gnark.sound.domain.media.WavFile;
import fr.gnark.sound.domain.physics.PhaseVocoder;
import fr.gnark.sound.domain.physics.PitchShift;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;

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


    public double[] stretch(final String resourcePath, final double ratio) throws IOException {
        final double[] buffer = getWavBuffer(resourcePath);
        return new PhaseVocoder(WINDOW_SIZE, ratio).proceed(buffer);
    }

    /**
     * Extracts data from a wav file.
     * Only mono has been tested for now, and it must be considered when using the double array returned.
     */
    private double[] getWavBuffer(final String resourcePath) throws IOException {
        final Resource resource = resourceLoader.getResource(resourcePath);

        final WavFile wavFile = WavFile.openWavFile(resource.getFile());
        final int numberOfSamples = (int) Math.pow(2, 19);
        final double[] buffer = new double[numberOfSamples];
        wavFile.readFrames(buffer, buffer.length);
        wavFile.close();
        return buffer;
    }

    public double[] pitchShift(final String resourcePath, final double ratio) throws IOException {
        final double[] buffer = getWavBuffer(resourcePath);
        return pitchShift.shift(buffer, ratio);
    }
}
