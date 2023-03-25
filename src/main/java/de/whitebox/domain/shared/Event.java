package de.whitebox.domain.shared;

import java.time.*;
import java.util.*;

public interface Event {
    UUID id = UUID.randomUUID();
    LocalDateTime at = LocalDateTime.now();

    default UUID id() {
        return Event.id;
    }

    default LocalDateTime at() {
        return Event.at;
    }
}
