package de.whitebox.infrastructure.shared;

import de.whitebox.domain.shared.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.function.*;

/**
 * Broker in memory, it should be replaced to a
 * distributed message system in order to guarantee
 * availability.
 */
@Component
public class MemoryBroker implements Broker {

    List<Consumer<Event>> consumers = new ArrayList<>();

    @Override
    public void publish(List<Event> events) {
        for (Event event : events) {
            for (Consumer<Event> consumer : consumers) {
                consumer.accept(event);
            }
        }
    }

    @Override
    public void subscribes(Consumer<Event> consumer) {
        consumers.add(consumer);
    }
}
