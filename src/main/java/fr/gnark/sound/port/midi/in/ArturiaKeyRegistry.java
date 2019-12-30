package fr.gnark.sound.port.midi.in;

import fr.gnark.sound.applications.Instruments;
import fr.gnark.sound.applications.Synthetizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleConsumer;

import static fr.gnark.sound.port.midi.in.Keys.*;

@Component
@Slf4j
public class ArturiaKeyRegistry {

    @Autowired
    private Synthetizer synthetizer;
    @Autowired
    private Instruments instruments;
    private final Map<Integer, Keys> _map = new HashMap<>();
    private final Map<Keys, DoubleConsumer> routingToApps = new EnumMap<>(Keys.class);

    @PostConstruct
    private void addItems() {
        this.routingToApps.put(ATTACK_1, synthetizer::modifyAttack);
        this.routingToApps.put(DECAY_1, synthetizer::modifyDecay);
        this.routingToApps.put(SUSTAIN_1, synthetizer::modifySustain);
        this.routingToApps.put(RELEASE_1, synthetizer::modifyRelease);
        this.routingToApps.put(NEXT, instruments::nextInstrument);
        this.routingToApps.put(PREVIOUS, instruments::previousInstrument);
        this.routingToApps.put(PARAM_1, instruments::changeParam1);

        this._map.put(73, ATTACK_1);
        this._map.put(75, DECAY_1);
        this._map.put(79, SUSTAIN_1);
        this._map.put(72, RELEASE_1);
        this._map.put(22, NEXT);
        this._map.put(93, PARAM_1);
        this._map.put(23, PREVIOUS);
    }

    public void triggerControlChange(final int keyValue, final double valueInPercent) {
        final Keys key = this._map.get(keyValue);
        if (key != null) {
            DoubleConsumer synthFunction = this.routingToApps.get(key);
            if (synthFunction != null) {
                synthFunction.accept(valueInPercent);
            } else {
                log.warn("Synthetizer function for key" + key + " not routed");
            }
        } else {
            log.warn("Key for value " + keyValue + " not found");
        }
    }
}
