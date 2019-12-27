package fr.gnark.sound.domain;

public class DomainObject {

    protected void checkRange(final String fieldName, final Double value, final Double min, final Double max) {
        checkNotNull(fieldName, value);
        if (value < min || value > max) {
            throw new DomainException(fieldName + " must be between " + min + " and " + max);
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
}
