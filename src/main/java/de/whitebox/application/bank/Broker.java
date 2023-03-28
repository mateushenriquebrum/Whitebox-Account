package de.whitebox.application.bank;


import de.whitebox.domain.shared.*;

import java.util.*;
import java.util.function.*;

/**
 * Broker in pub/sub communication style.
 * Most time will act as a mediator between bounded contexts
 */
public interface Broker {
    void publish(List<Event> events);

    void subscribes(Consumer<Event> events);
}
