package de.whitebox.domain.bank;

import de.whitebox.domain.shared.*;

import java.util.*;

import static java.util.UUID.*;

/**
 * A Value Object describing the customer, in a real application it should also be an entity
 *
 * @param id      UUID for this customer
 * @param name    First name
 * @param surname Last name
 */
public record Customer(UUID id, String name, String surname) implements ValueObject {
    public Customer(String name, String surname) {
        this(randomUUID(), name, surname);
    }
}
