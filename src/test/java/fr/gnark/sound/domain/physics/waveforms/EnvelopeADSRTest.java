package fr.gnark.sound.domain.physics.waveforms;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

class EnvelopeADSRTest {
    DecimalFormat df = new DecimalFormat("#.#", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
    final EnvelopeADSR envelopeADSR = EnvelopeADSR.builder()
            .attackInSeconds(5.0)
            .decayInSeconds(2.0)
            .sustainFactorinDbfs(-3.0)
            .releaseInSeconds(8.0)
            .build();

    @Test
    public void test() {
        Assertions.assertEquals(Math.pow(10, (-50.0 / 20.0)), envelopeADSR.computeAmplitude(0));
        Assertions.assertEquals(Math.pow(10, (-40.0 / 20.0)), envelopeADSR.computeAmplitude(1.0));
        Assertions.assertEquals(Math.pow(10, (-30.0 / 20.0)), envelopeADSR.computeAmplitude(2.0));
        Assertions.assertEquals(Math.pow(10, (-20.0 / 20.0)), envelopeADSR.computeAmplitude(3.0));
        Assertions.assertEquals(Math.pow(10, (-10.0 / 20.0)), envelopeADSR.computeAmplitude(4.0));
        Assertions.assertEquals(1.0, envelopeADSR.computeAmplitude(5.0));
        Assertions.assertEquals(0.8, envelopeADSR.computeAmplitude(8.0));
    }

    @Test
    public void computeRelease() {
        Assertions.assertEquals("0.8", computeRelease(0.0));
        Assertions.assertEquals("0.7", computeRelease(1.0));
        Assertions.assertEquals("0.6", computeRelease(2.0));
        Assertions.assertEquals("0.5", computeRelease(3.0));
        Assertions.assertEquals("0.4", computeRelease(4.0));
        Assertions.assertEquals("0.3", computeRelease(5.0));
        Assertions.assertEquals("0.2", computeRelease(6.0));
        Assertions.assertEquals("0.1", computeRelease(7.0));
        Assertions.assertEquals("0", computeRelease(8.0));
    }

    //truncate the value to make it comparable
    private String computeRelease(final double time) {
        return df.format(envelopeADSR.computeRelease(time));
    }
}