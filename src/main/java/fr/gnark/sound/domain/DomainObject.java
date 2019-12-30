package fr.gnark.sound.domain;

public class DomainObject {

    protected void checkRange(final String fieldName, final Double value, final Double min, final Double max) {
        checkNotNull(fieldName, value);
        if (value < min || value > max) {
            throw new DomainException(fieldName + " must be between " + min + " and " + max + " value read :" + value);
        }
    }

    protected void checkPositive(final String fieldName, final Double value) {
        checkNotNull(fieldName, value);
        if (value < 0) {
            throw new DomainException(fieldName + " must be positive");
        }
    }

    protected void checkNotNull(final String fieldName, final Object value) {
        if (value == null) {
            throw new DomainException(fieldName + " must not be null");
        }
    }

    protected double getPercentage(final double valueInPercent, final double min, final double max) {
        checkRange("valueInPercent", valueInPercent, 0.0, 100.0);
        if (valueInPercent == 100) {
            return max;
        } else if (valueInPercent == 0) {
            return min;
        }
        return (valueInPercent * max) / 100;
    }
}
