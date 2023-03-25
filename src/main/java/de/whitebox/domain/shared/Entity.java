package de.whitebox.domain.shared;

import java.util.*;

import static java.util.Collections.*;
import static java.util.UUID.*;

/**
 * It defines a entity managed by an event source persistence layer
 */

public abstract class Entity {

    public List<Event> changes() {
        return unmodifiableList(this.changes);
    }

    /**
     * This meant to be used as an optimistic lock, it is generic enough to be used in any database.
     *
     * @return The locked version for this entity
     */
    public UUID locked() {
        return this.locked;
    }

    public UUID id() {
        return this.id;
    }

    public abstract void mutate(Event event);

    protected Entity(List<Event> events, UUID locked, UUID id) {
        events.forEach(this::mutate);
        this.locked = locked;
        this.id = id;
    }

    protected Entity() {
        this.locked = randomUUID();
        this.id = randomUUID();
    }

    protected void apply(Event event) {
        mutate(event);
        changes.add(event);
    }

    private final List<Event> changes = new ArrayList<>();

    private UUID locked;
    private UUID id;

}
