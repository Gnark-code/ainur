package fr.gnark.sound.applications;

import fr.gnark.sound.domain.media.AudioFormatOutput;
import fr.gnark.sound.domain.music.Mode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ScaleAGogoTest {

    @Autowired
    private ScaleAGogo scaleAGogo;

    @Test
    void getPreviewData() throws IOException {
        final byte[] data = scaleAGogo.getPreviewData(Mode.DOUBLE_HARMONIC_MAJOR);
        Assertions.assertTrue(data.length > 0 && data.length < AudioFormatOutput.MAX_BUFFER_SIZE);
    }
}