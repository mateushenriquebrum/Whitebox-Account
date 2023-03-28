package de.whitebox.domain.shared;

import java.lang.reflect.*;
import java.util.*;

/**
 * This peace of code is responsible to reduce a bunch of events to an entity,
 * It can get a bit harry as it uses reflection, but once stable doesn't tend to change.
 */
public class Serializer {
    public static <T extends Entity> T serialize(Class<T> clazz, List<Event> events, UUID locked, UUID id) {
        try {
            return clazz.getConstructor(List.class, UUID.class, UUID.class).newInstance(events, locked, id);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new NotUnmarshalledException();
        }
    }

    static class NotUnmarshalledException extends RuntimeException {
    }
}
