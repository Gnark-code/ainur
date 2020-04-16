package fr.gnark.sound.domain.physics.waveforms;

import fr.gnark.sound.domain.physics.Signal;

import static fr.gnark.sound.domain.media.output.WavConstants.SAMPLE_RATE;

public class Guitar extends Signal {

    public void addSampleData(final double[] sampleData, final double fundamentalOfSample) {
        _buffer.put(fundamentalOfSample, sampleData);
    }

    @Override
    protected double getResultFromBuffer(final double fundamentalFrequency, final double time, final double[] bufferedValues) {
        final double result;//get from buffer
        final int index = (int) (time * SAMPLE_RATE);
        if (index <= bufferedValues.length - 1) {
            result = bufferedValues[index];
        } else {
            //out of sampled data
            result = 0;
        }
        return result;
    }

    @Override
    protected double innerComputeFormula(final double fundamentalFrequency, final double time) {
        //everything must be calculated within the buffer
        return 0;
    }
}
