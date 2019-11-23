package fr.gnark.sound.port.graphql.dto;

import lombok.Data;

import java.util.List;

@Data
public class MusicEnumsDto {
    private List<String> degrees;
    private List<String> modes;
    private List<String> bassPatterns;
    private List<String> subdivisions;
    private List<String> notes;
}
