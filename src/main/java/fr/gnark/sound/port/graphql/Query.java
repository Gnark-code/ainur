package fr.gnark.sound.port.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import fr.gnark.sound.domain.music.Chord;
import fr.gnark.sound.domain.music.Degree;
import fr.gnark.sound.domain.music.Mode;
import fr.gnark.sound.domain.music.Subdivision;
import fr.gnark.sound.port.graphql.dto.MusicEnumsDto;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Component
public class Query implements GraphQLQueryResolver {
    public MusicEnumsDto getMusicEnums() {
        final MusicEnumsDto dto = new MusicEnumsDto();
        dto.setModes(stream(Mode.values()).map(Enum::name).collect(Collectors.toList()));
        dto.setDegrees(stream(Degree.values()).map(Enum::name).collect(Collectors.toList()));
        dto.setBassPatterns(stream(Chord.BassPattern.values()).map(Enum::name).collect(Collectors.toList()));
        dto.setNotes(Arrays.asList("C","D","E","F","G","A","B"));
        dto.setSubdivisions(stream(Subdivision.Type.values()).map(Enum::name).collect(Collectors.toList()));
        return dto;
    }


}
