package fr.gnark.sound.domain.media;

import fr.gnark.sound.domain.DomainObject;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * Created by Gnark on 13/04/2019.
 * represents a sound to be played using the sound card
 */
@Getter
@ToString
public class Event extends DomainObject {
    //between 20 Hz and 20 kHz
    private Double frequency;
    // between -100( completely left ) and 100 (completely right) : default 0 = center ;
    private Float panning;
    // in percentage between 0 and 100 : default 100
    private Float amplitude;

    @Builder
    public Event(final Double frequency, final Float panning, final Float amplitude) {
        this.frequency = frequency;
        this.panning = panning;
        this.amplitude = amplitude;
        validateAllFields();
    }


    private void validateAllFields() {
        checkRange("frequency",frequency,0.0,20000.0);
        Objects.requireNonNull(frequency);
        if (frequency > 20000 || frequency < 0) {
            throw new IllegalArgumentException("amplitude must be between 0 and 20000");
        }

        if (panning == null) {
            panning = 0.0f;
        } else if (panning > 100f || panning < -100f) {
            checkRange("panning",(double)panning,-100.0,100.0);

            throw new IllegalArgumentException("amplitude must be between -100.0 and 100.0");
        }

        if (amplitude == null) {
            amplitude = 100f;
        } else if (amplitude > 100f || amplitude < 0f) {
            checkRange("panning",(double)amplitude,-100.0,100.0);

            throw new IllegalArgumentException("amplitude must be between 0.0 and 100.0");
        }
    }
}
