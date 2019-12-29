package fr.gnark.sound.domain.media.waveforms;


import fr.gnark.sound.domain.DomainObject;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class EnvelopeADSR extends DomainObject {
    private static final double MIN_GAIN = -50.0;
    private static final double MIN_ATTACK = 0.001;
    private static final double MAX_ATTACK = 8.0;
    private static final double MIN_DECAY = MIN_ATTACK;
    private static final double MAX_DECAY = MAX_ATTACK;
    private static final double MIN_RELEASE = 0.01;
    private static final double MAX_RELEASE = 16.0;
    private double attackInSeconds;
    private double decayInSeconds;
    private double sustainFactorInDbfs;
    private double sustainFactorInPercentage;
    private double maxFactorForFaders;
    @Getter
    private double releaseInSeconds;


    @Builder
    private EnvelopeADSR(final Double attackInSeconds, final Double decayInSeconds, final Double sustainFactorinDbfs, final Double releaseInSeconds) {
        setAttackInSeconds(attackInSeconds);
        setDecayInSeconds(decayInSeconds);
        setSustainFactorInDbfs(sustainFactorinDbfs);
        setReleaseInSeconds(releaseInSeconds);
        //arbitrary factor for faders to be useable from ms values to seconds values
        maxFactorForFaders = Math.exp(10);
    }

    public EnvelopeADSR copy() {
        return new EnvelopeADSR(attackInSeconds, decayInSeconds, sustainFactorInDbfs, releaseInSeconds);
    }

    private void setAttackInSeconds(final Double attackInSeconds) {
        checkPositive("attackInSeconds", attackInSeconds);
        this.attackInSeconds = attackInSeconds;
    }


    private void setDecayInSeconds(final Double decayInSeconds) {
        checkPositive("decayInSeconds", decayInSeconds);
        this.decayInSeconds = decayInSeconds;
    }

    private void setSustainFactorInDbfs(final Double sustainFactorInDbfs) {
        checkRange("sustainFactor", sustainFactorInDbfs, MIN_GAIN, 0.0);
        this.sustainFactorInDbfs = sustainFactorInDbfs;
        this.sustainFactorInPercentage = Math.pow(10, this.sustainFactorInDbfs / 20.0);
        ;
    }

    private void setReleaseInSeconds(final Double releaseInSeconds) {
        checkPositive("releaseInSeconds", releaseInSeconds);
        this.releaseInSeconds = releaseInSeconds;
    }

    public double computeAmplitude(final double time) {
        final double gainInDbfs = (MIN_GAIN + (-MIN_GAIN * computeLinearAmplitude(time)));
        return Math.pow(10, gainInDbfs / 20.0);
    }

    private double computeLinearAmplitude(final double time) {
        if (time <= attackInSeconds) {
            return time / attackInSeconds;
        } else if (time <= (attackInSeconds + decayInSeconds)) {
            return (1.0 - (time - attackInSeconds) / (decayInSeconds)) * (1.0 - sustainFactorInPercentage) + sustainFactorInPercentage;
        } else {
            return sustainFactorInPercentage;
        }
    }

    public double computeRelease(final double time) {
        if (time < 0) {
            return MIN_GAIN + (-MIN_GAIN * sustainFactorInPercentage);
        }
        final double gainInDbfs = MIN_GAIN + (-MIN_GAIN * (sustainFactorInPercentage - ((time / releaseInSeconds) * sustainFactorInPercentage)));
        return Math.pow(10, gainInDbfs / 20);
    }

    public boolean mustComputeRelease(final double time) {
        return time <= releaseInSeconds;
    }

    public void modifyAttack(final double valueInPercent) {
        setAttackInSeconds(faderFunction(valueInPercent, MIN_ATTACK, MAX_ATTACK));
        log.trace("setting attack to :" + attackInSeconds + "s");
    }


    public void modifyDecay(final double valueInPercent) {
        setDecayInSeconds(faderFunction(valueInPercent, MIN_DECAY, MAX_DECAY));
        log.trace("setting decay to :" + decayInSeconds + "s");
    }

    public void modifySustain(final double valueInPercent) {
        setSustainFactorInDbfs(MIN_GAIN - (MIN_GAIN * valueInPercent) / 100);
        log.trace("setting sustain to :" + sustainFactorInDbfs + "dB");
    }

    public void modifyRelease(final double valueInPercent) {
        setReleaseInSeconds(faderFunction(valueInPercent, MIN_RELEASE, MAX_RELEASE));
        log.trace("setting release to :" + releaseInSeconds + "s");
    }

    private double faderFunction(final double valueInPercent, final double min, final double max) {
        if (valueInPercent == 100.0) {
            return max;
        }
        if (valueInPercent == 0.0) {
            return min;
        }
        return (Math.exp(valueInPercent / 10) * max) / maxFactorForFaders + min;
    }
}
