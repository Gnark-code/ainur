package fr.gnark.sound.domain.media;

import org.sheinbergon.aac.encoder.AACAudioEncoder;
import org.sheinbergon.aac.encoder.WAVAudioInput;
import org.sheinbergon.aac.encoder.util.AACEncodingProfile;
import org.sheinbergon.aac.encoder.util.WAVAudioFormat;

import java.nio.ByteOrder;

public class AACOutput implements Output {

    private final AudioFormatOutput internalWavOutput;

    public AACOutput(final AudioFormatOutput output) {
        this.internalWavOutput = output;
    }

    @Override
    public void flush() {
        this.internalWavOutput.flush();
    }

    @Override
    public void storeData(final double levelLeft, final double levelRight) {
        this.internalWavOutput.storeData(levelLeft, levelRight);
    }

    @Override
    public long getThroughputInBytes() {
        return this.internalWavOutput.getThroughputInBytes();
    }

    @Override
    public int getFrameSize() {
        return this.internalWavOutput.getFrameSize();
    }


    //transform the wav buffer to AAC buffer
    public byte[] getBuffer() {
        byte[] internal = this.internalWavOutput.getBuffer();
        WAVAudioInput wavAudioInput = WAVAudioInput.builder()
                .data(internal)
                .sampleSize(16)
                .endianness(ByteOrder.LITTLE_ENDIAN)
                .audioFormat(WAVAudioFormat.PCM)
                .length(internal.length).build();
        AACAudioEncoder aacAudioEncoder = AACAudioEncoder.builder().channels(2).sampleRate(44100)
                .profile(AACEncodingProfile.AAC_LC)
                .build();
        aacAudioEncoder.encode(wavAudioInput);

        return aacAudioEncoder.conclude()
                .data();
    }
}
