package fr.gnark.sound.domain.media;

import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.LineUnavailableException;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

@Slf4j
public class Dispatcher {
    private final ExecutorService executorService;
    // frequency processed / encoding running
    private final Map<Double, RealtimeEncoder> encodingInProcess;
    private final Deque<RealtimeEncoder> availableEncoders;

    public Dispatcher(final int numberOfLines, final Instrument instrument) throws LineUnavailableException {
        this.executorService = Executors.newFixedThreadPool(numberOfLines);
        encodingInProcess = new ConcurrentHashMap<>();
        availableEncoders = new LinkedBlockingDeque<>();
        for (int i = 0; i < numberOfLines; i++) {
            availableEncoders.add(new RealtimeEncoder(instrument));
        }
    }

    public void dispatch(final Event event) {
        if (!availableEncoders.isEmpty()) {
            final RealtimeEncoder encoder = availableEncoders.removeFirst();
            encodingInProcess.put(event.getFrequency(), encoder);
            this.executorService.submit(() -> {
                try {
                    encoder.handleEvent(event);
                } catch (final Exception e) {
                    log.error("error caught when processing event", e);
                }
            });

        } else {
            log.warn("could not process Event due to lack of available encoders");
        }
    }

    public void stop(final Double frequencyProcessed) {
        final RealtimeEncoder encoder = encodingInProcess.get(frequencyProcessed);
        if (encoder != null) {
            encoder.stop();
            availableEncoders.add(encoder);
        } else {
            log.warn("could not stop process for frequency " + frequencyProcessed);
        }

    }
}
