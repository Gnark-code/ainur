package fr.gnark.sound.applications;

import fr.gnark.sound.domain.media.output.RealtimeAudioFormat;
import fr.gnark.sound.domain.media.output.WavConstants;
import fr.gnark.sound.domain.music.BaseNote;
import fr.gnark.sound.domain.music.Mode;
import fr.gnark.sound.domain.music.Note;
import fr.gnark.sound.domain.music.Scale;
import fr.gnark.sound.domain.physics.Frames;
import fr.gnark.sound.domain.physics.Peak;
import fr.gnark.sound.domain.physics.PeakAggregator;
import fr.gnark.sound.domain.physics.PeakFinder;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static fr.gnark.sound.domain.media.output.WavConstants.SAMPLE_RATE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Disabled("Needs a computer with sound card to run")
class SampleImporterTest {

    @Autowired
    private SampleImporter sampleImporter;
    @Autowired
    private Oscilloscope oscilloscope;
    private final RealtimeAudioFormat monoPlayer;
    private final RealtimeAudioFormat stereoPlayer;

    SampleImporterTest() throws LineUnavailableException {
        monoPlayer = new RealtimeAudioFormat(WavConstants.AUDIO_FORMAT_MONO);
        stereoPlayer = new RealtimeAudioFormat(WavConstants.AUDIO_FORMAT_STEREO);

    }

    @BeforeAll
    public static void init() {
        System.setProperty("java.awt.headless", "false");
    }


    @Test
    public void maple() throws IOException {
        double[] data = sampleImporter.stretch("classpath:samples/maple.wav", 1.0);
        Assertions.assertNotNull(data);
        for (final double v : data) {
            monoPlayer.storeDataStereo(v, v);
        }
    }

    @Test
    public void pitchShift() throws Exception {
        double[] data = sampleImporter.pitchShift("classpath:samples/guitar_e_44100_mono.wav", 1.0);
        Assertions.assertNotNull(data);
        //  getWindows(data);
        for (final double v : data) {
            monoPlayer.storeDataMono(v);
        }
    }

    @Test
    public void peakFinder() throws Exception {
        double[] data = sampleImporter.getWavBuffer("classpath:samples/maple.wav");

        final Frames frames = new Frames(8192).addWithOverlap(data, 8192);
        final BaseNoteCounter baseNoteCounter = new BaseNoteCounter();
        final Iterator<double[]> dataIterator = frames.iterator();
        while (dataIterator.hasNext()) {
            Set<BaseNote> baseNotes = extractBaseNote(dataIterator.next());
            baseNotes.forEach(baseNote ->
                    baseNoteCounter.add(baseNote)
            );
        }
        List<BaseNoteCounterItem> baseNotes = baseNoteCounter.extractMostFrequent(7);
        Set<BaseNote> baseNotes2 = extractBaseNote(data);
        Scale scale
                = new Scale(Mode.MAJOR, Note.builder().baseNote(BaseNote.A_FLAT).octave(1).build());
        final Set<BaseNote> expectedBaseNotes = scale.getNotes().stream().map(Note::getBaseNote).collect(Collectors.toSet());
        for (final BaseNoteCounterItem note : baseNotes) {
            Assertions.assertTrue(expectedBaseNotes.contains(note.getBaseNote()), "note : " + note);
        }
    }

    private Set<BaseNote> extractBaseNote(final double[] data) {
        final double cents = 25.0;

        final PeakFinder peakFinder = new PeakFinder(SAMPLE_RATE, 0.10f);
        final PeakAggregator peakAggregator = new PeakAggregator(cents);
        final List<Peak> peaks = peakAggregator.proceed(peakFinder.proceed(data))
                .stream()
                .sorted(Comparator.comparing(Peak::getMagnitude)).collect(Collectors.toList());

        if (peaks.size() > 10) {
            final List<Peak> mostPowerfulPeaks = peaks.subList(peaks.size() - 10, peaks.size());

            final List<Note> aggregates = mostPowerfulPeaks.stream()
                    .map(Peak::getFrequency)
                    .map(freq -> Note.getFromFrequency(freq, cents / 2))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
            final Set<BaseNote> baseNotes = aggregates.stream().map(Note::getBaseNote).collect(Collectors.toSet());
            return baseNotes;
        }
        return new HashSet<>();
    }

    @Test
    public void peakFinder2() throws Exception {
        final double cents = 25.0;
        double[] data = sampleImporter.getWavBuffer("classpath:samples/d_major_mono_44100.wav");
        final PeakFinder peakFinder = new PeakFinder(SAMPLE_RATE, 0.10f);
        final PeakAggregator peakAggregator = new PeakAggregator(cents);
        final List<Peak> peaks = peakAggregator.proceed(peakFinder.proceed(data))
                .stream()
                .sorted(Comparator.comparing(Peak::getMagnitude)).collect(Collectors.toList());
        final List<Peak> mostPowerfulPeaks = peaks.subList(peaks.size() - 10, peaks.size());

        final List<Note> aggregates = mostPowerfulPeaks.stream()
                .map(Peak::getFrequency)
                .map(freq -> Note.getFromFrequency(freq, cents / 4))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        final Set<BaseNote> baseNotes = aggregates.stream().map(Note::getBaseNote).collect(Collectors.toSet());
        Assertions.assertNotNull(peaks);
        Scale scale
                = new Scale(Mode.MAJOR, Note.builder().baseNote(BaseNote.D).octave(1).build());
        final Set<BaseNote> expectedBaseNotes = scale.getNotes().stream().map(Note::getBaseNote).collect(Collectors.toSet());
        for (final BaseNote note : baseNotes) {
            Assertions.assertTrue(expectedBaseNotes.contains(note), "note : " + note);
        }
    }

    @Test
    public void peakFinder3() throws Exception {
        final double cents = 25.0;
        double[] data = sampleImporter.getWavBuffer("classpath:samples/d_major.wav");
        final PeakFinder peakFinder = new PeakFinder(SAMPLE_RATE, 0.10f);
        final PeakAggregator peakAggregator = new PeakAggregator(cents);
        final List<Peak> peaks = peakAggregator.proceed(peakFinder.proceed(data))
                .stream()
                .sorted(Comparator.comparing(Peak::getMagnitude)).collect(Collectors.toList());
        final List<Peak> mostPowerfulPeaks = peaks.subList(peaks.size() - 10, peaks.size());

        final List<Note> aggregates = mostPowerfulPeaks.stream()
                .map(Peak::getFrequency)
                .map(freq -> Note.getFromFrequency(freq, cents / 4))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        final Set<BaseNote> baseNotes = aggregates.stream().map(Note::getBaseNote).collect(Collectors.toSet());
        Assertions.assertNotNull(peaks);
        Scale scale
                = new Scale(Mode.MAJOR, Note.builder().baseNote(BaseNote.D).octave(1).build());
        final Set<BaseNote> expectedBaseNotes = scale.getNotes().stream().map(Note::getBaseNote).collect(Collectors.toSet());
        for (final BaseNote note : baseNotes) {
            Assertions.assertTrue(expectedBaseNotes.contains(note), "note : " + note);
        }
    }

    @Test
    public void findScaleOfMorrrisonsJig() throws Exception {
        final double cents = 25.0;
        double[] data = sampleImporter.getWavBuffer("classpath:samples/morrison_jig.wav");
        final Frames frames = new Frames(8192).addWithOverlap(data, 8192);
        final BaseNoteCounter baseNoteCounter = new BaseNoteCounter();
        final Iterator<double[]> dataIterator = frames.iterator();
        while (dataIterator.hasNext()) {
            Set<BaseNote> baseNotes = extractBaseNote(dataIterator.next());
            baseNotes.forEach(baseNote ->
                    baseNoteCounter.add(baseNote)
            );
        }
        List<BaseNoteCounterItem> baseNotes = baseNoteCounter.extractMostFrequent(7);
        Set<BaseNote> baseNotes2 = extractBaseNote(data);

        Scale scale
                = new Scale(Mode.DORIAN, Note.builder().baseNote(BaseNote.E_FLAT).octave(1).build());
        final Set<BaseNote> expectedBaseNotes = scale.getNotes().stream().map(Note::getBaseNote).collect(Collectors.toSet());
        for (final BaseNoteCounterItem note : baseNotes) {
            Assertions.assertTrue(expectedBaseNotes.contains(note.getBaseNote()), "note : " + note);
        }

    }

    private void getWindows(final double[] data, final int windowSize) throws InterruptedException {
        oscilloscope.addChart("new signal", "time", "amplitude", data);
        for (int i = 0; i < 2; i++) {
            double[] window = new double[windowSize];
            System.arraycopy(data, i * window.length, window, 0, window.length);
            oscilloscope.addChart("new signal" + i, "time", "amplitude", window);
        }
        oscilloscope.displayAll();
        Thread.sleep(500000);
    }

    private void getWindows(final double[] data) throws InterruptedException {
        this.getWindows(data, 1024);
    }


    private class BaseNoteCounter {
        private List<BaseNoteCounterItem> _items;


        public BaseNoteCounter() {
            _items = new ArrayList<>();
            final Iterator<BaseNote> iterator = BaseNote.iterator();
            while (iterator.hasNext()) {
                _items.add(new BaseNoteCounterItem(iterator.next()));
            }
        }

        public List<BaseNoteCounterItem> extractMostFrequent(final int number) {
            Collections.sort(_items);
            return _items.subList(_items.size() - number, _items.size());
        }


        public void add(final BaseNote baseNote) {
            _items.stream()
                    .filter(item -> item.getBaseNote().equals(baseNote))
                    .forEach(BaseNoteCounterItem::increment);
        }
    }

    @Getter
    @ToString
    private class BaseNoteCounterItem implements Comparable<BaseNoteCounterItem> {
        private final BaseNote baseNote;
        private final AtomicLong value;

        public BaseNoteCounterItem(BaseNote baseNote) {
            this.baseNote = baseNote;
            this.value = new AtomicLong(0);
        }

        public void increment() {
            this.value.incrementAndGet();
        }


        @Override
        public int compareTo(@NotNull BaseNoteCounterItem otherItem) {
            final long current = value.longValue();
            final long other = otherItem.value.longValue();
            if (current > other)
                return 1;
            if (current < other)
                return -1;
            return 0;
        }
    }
}