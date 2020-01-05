package fr.gnark.sound.domain.media;

import fr.gnark.sound.domain.physics.Signal;
import fr.gnark.sound.domain.physics.waveforms.EnvelopeADSR;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import static fr.gnark.sound.domain.media.output.WavConstants.*;

@EqualsAndHashCode
@Slf4j
public class WavEncoder {
    //duration of a clock tick in milliseconds
    private final double definitionInMs;
    //50  MB of buffer available
    public static final int MAX_BUFFER_SIZE = 50000000;
    private final String name;
    private final Signal signal;
    private EnvelopeADSR envelope;
    private byte[] _buffer;
    private int bufferIndex = 0;

    public WavEncoder(final String name, final double definitionInMs, final Signal signal) {
        this.definitionInMs = definitionInMs;
        this.name = name;
        this.signal = signal;
        this.envelope = EnvelopeADSR.builder()
                .attackInSeconds(0.2)
                .decayInSeconds(0.01)
                .sustainFactorinDbfs(-3.0)
                .releaseInSeconds(0.05)
                .build();
        newBuffer();
    }

    public void handleEvents(final Events events) {
        final long throughput = (long) (FRAME_SIZE * FRAME_RATE);
        final int frameSize = FRAME_SIZE;
        final double delta = 1 / FRAME_RATE;
        double seconds = (events.getDurationInClockTicks() * definitionInMs) / 1000;
        double timeBeforeRelease = seconds - envelope.getReleaseInSeconds();
        int nbBytes = (int) (seconds * throughput);
        // truncate to the nearest complete frame
        nbBytes = nbBytes - (nbBytes % frameSize);
        double time = 0;
        for (int sample = 0; sample < nbBytes; sample = sample + frameSize) {
            mapEvents(events, delta, timeBeforeRelease, time);
        }

    }

    private double mapEvents(final Events events, final double delta, final double timeBeforeRelease, double time) {
        double computed = 0;
        double amplitudeR = 0;
        double amplitudeL = 0;
        time += delta;
        if (!events.isPause()) {
            final Iterator<Event> iterator = events.getEvents();
            while (iterator.hasNext()) {
                final Event event = iterator.next();
                double amplitude = event.getAmplitude();
                if (time <= timeBeforeRelease)
                    amplitude *= envelope.computeAmplitude(time);
                else {
                    amplitude *= envelope.computeRelease(time - timeBeforeRelease);
                }
                amplitudeR += amplitude / 100;
                amplitudeL += amplitude / 100;
                //apply panning if necessary
                if (event.getPanning().compareTo(0.0f) != 0) {
                    amplitudeR += amplitudeR * (event.getPanning() / 100);
                    amplitudeL -= amplitudeL * (event.getPanning() / 100);
                }
                computed += signal.computeFormula(event.getFrequency(), time);
                storeData(amplitudeL * computed, amplitudeR * computed);
            }
        } else {
            storeData(0.0, 0.0);
        }
        return time;
    }

    private void storeData(final double levelLeft, final double levelRight) {
        int castLevelLeft = (int) (levelLeft * GAIN);
        int castLevelRight = (int) (levelRight * GAIN);

        _buffer[bufferIndex] = (byte) (castLevelLeft);
        _buffer[bufferIndex + 1] = (byte) (castLevelLeft >>> 8);
        _buffer[bufferIndex + 2] = (byte) (castLevelRight);
        _buffer[bufferIndex + 3] = (byte) (castLevelRight >>> 8);
        bufferIndex = bufferIndex + 4;
    }

    public byte[] toWavBuffer() throws IOException {
        final byte[] data = getBuffer();
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        AudioInputStream audioInputStream = new AudioInputStream(bais, AUDIO_FORMAT_STEREO, data.length);
        AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, baos);
        audioInputStream.close();
        newBuffer();
        return baos.toByteArray();
    }

    private void newBuffer() {
        //50  MB of buffer available
        _buffer = new byte[MAX_BUFFER_SIZE];
        bufferIndex = 0;
    }


    public byte[] getBuffer() {
        byte[] minData = new byte[bufferIndex];
        System.arraycopy(_buffer, 0, minData, 0, bufferIndex);
        return minData;
    }
}
