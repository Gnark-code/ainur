package fr.gnark.sound.domain.physics;

import java.util.ArrayList;
import java.util.List;

public class PeakAggregator {
    private final double cents;

    public PeakAggregator(final double cents) {
        this.cents = cents;
    }

    public List<Peak> proceed(final List<Peak> peaks) {
        List<Peak> result = new ArrayList<>();
        //TODO :   assert size
        Peak localMaximum = peaks.get(0);
        for (int i = 1; i < peaks.size(); i++) {
            final Peak peak = peaks.get(i);
            if (localMaximum == null) {
                localMaximum = peak;
            } else {
                final double maxFrequencyDistance = localMaximum.getFrequency() * Math.pow(2, cents / 1200.0) - localMaximum.getFrequency();
                final double delta = peak.getFrequency() - localMaximum.getFrequency();
                final boolean isNearEnough = delta < maxFrequencyDistance;
                if (!isNearEnough) {
                    result.add(localMaximum);
                    localMaximum = null;
                } else if (peak.getMagnitude() > localMaximum.getMagnitude()) {
                    localMaximum = peak;
                }
            }
        }

        return result;
    }
}
