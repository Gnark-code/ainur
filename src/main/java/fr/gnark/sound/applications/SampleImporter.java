package fr.gnark.sound.applications;

import fr.gnark.sound.domain.media.WavFile;
import fr.gnark.sound.domain.media.output.WavConstants;
import fr.gnark.sound.domain.physics.PitchShift;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static fr.gnark.sound.domain.media.output.WavConstants.GAIN;

@Component
@Slf4j
public class SampleImporter {
    private ResourceLoader resourceLoader;
    private PitchShift pitchShift;

    public SampleImporter(final ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.pitchShift = new PitchShift(WavConstants.SAMPLE_RATE,1024);
    }

    public double[] importSample(final String resourcePath) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        Resource resource = resourceLoader.getResource(resourcePath);

        // Open the wav file specified as the first argument
        WavFile wavFile = WavFile.openWavFile(resource.getFile());
        final AudioFileFormat audioFileFormat = AudioSystem.getAudioFileFormat(resource.getFile());

        // Display information about the wav file
        wavFile.display();

        // Get the number of audio channels in the wav file
        int numChannels = wavFile.getNumChannels();

        // Create a buffer of 100 frames
        final int numberOfSamples = (int) Math.pow(2, 19);
        double[] buffer = new double[numberOfSamples];


        wavFile.readFrames(buffer, buffer.length);
        wavFile.close();
        double[] buffer2 = pitchShift.shift(buffer,1.00);

        byte[] data = new byte[buffer2.length * 2];

        int index = 0;
        for (final double value : buffer2) {
            int castLevelLeft = (int) (value * GAIN);
            data[index] = (byte) (castLevelLeft);
            data[index + 1] = (byte) (castLevelLeft >>> 8);
            index = index + 2;
        }



        final AudioFormat audioFormat = audioFileFormat.getFormat();
        final SourceDataLine line = AudioSystem.getSourceDataLine(audioFormat);
        line.open(audioFormat);
        line.write(data, 0, data.length);
        line.drain();
        line.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        AudioInputStream audioInputStream = new AudioInputStream(bais, audioFormat, buffer.length);
        AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, File.createTempFile("test", ".wav"));
        audioInputStream.close();


        return buffer;

    }

    public double[] pitchShift(final String resourcePath,final double ratio) throws IOException, UnsupportedAudioFileException {
        Resource resource = resourceLoader.getResource(resourcePath);

        // Open the wav file specified as the first argument
        WavFile wavFile = WavFile.openWavFile(resource.getFile());
        final AudioFileFormat audioFileFormat = AudioSystem.getAudioFileFormat(resource.getFile());

        // Display information about the wav file
        wavFile.display();

        // Get the number of audio channels in the wav file
        int numChannels = wavFile.getNumChannels();

        // Create a buffer of 100 frames
        final int numberOfSamples = (int) Math.pow(2, 19);
        double[] buffer = new double[numberOfSamples];
        wavFile.readFrames(buffer, buffer.length);
        wavFile.close();

        return pitchShift.shift(buffer,ratio);
    }
}
