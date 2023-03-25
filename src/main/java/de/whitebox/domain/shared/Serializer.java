package de.whitebox.domain.shared;

import java.lang.reflect.*;
import java.util.*;

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
