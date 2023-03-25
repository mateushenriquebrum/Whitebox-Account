package de.whitebox.domain.bank.vos;

import de.whitebox.domain.shared.*;

import java.util.*;

import static java.util.UUID.randomUUID;

/**
 * A Value Object describing the customer, in a real application it should also be an entity
 *
 * @param id      UUID for this customer
 * @param name    First name
 * @param surname Last name
 */
public record Customer(CustomerId id, String name, String surname) implements ValueObject {
    public Customer(String name, String surname) {
        this(new CustomerId(randomUUID()), name, surname);
    }

    public record CustomerId(UUID id) { }

}
