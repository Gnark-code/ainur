package fr.gnark.sound.domain.media;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Chart {
    private final String title;
    private final String xTitle;
    private final String yTitle;
    private final double[] data;
}
