package de.whitebox.domain.shared;


import java.util.*;
import java.util.function.*;

public interface Broker {
    void publish(List<Event> events);

    void subscribes(Consumer<Event> events);
}
