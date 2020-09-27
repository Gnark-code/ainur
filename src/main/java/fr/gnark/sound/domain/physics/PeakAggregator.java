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
        if (peaks.size() > 0) {
            //TODO :   assert size
            result.add(peaks.get(0));
            for (int i = 1; i < peaks.size(); i++) {
                int lastInsertedIndex = result.size() - 1;
                final Peak localMaximum = result.get(lastInsertedIndex);
                final Peak peak = peaks.get(i);
                final double maxFrequencyDistance = localMaximum.getFrequency() * Math.pow(2, cents / 1200.0) - localMaximum.getFrequency();
                final double delta = peak.getFrequency() - localMaximum.getFrequency();
                final boolean isNewPeak = delta > maxFrequencyDistance;
                final boolean localMaximumMustBeReplaced = !isNewPeak && peak.getMagnitude() > localMaximum.getMagnitude();

                if (isNewPeak) {
                    result.add(peak);
                } else if (localMaximumMustBeReplaced) {
                    result.set(lastInsertedIndex, peak);
                }

            }
        }
        return result;
    }
}
