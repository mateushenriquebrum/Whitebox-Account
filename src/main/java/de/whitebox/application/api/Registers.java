package de.whitebox.application.api;

import de.whitebox.application.api.Query.*;

import java.util.*;


/**
 * Registers repository
 */
public interface Registers {
    void add(Transaction transaction);

    List<Transaction> of(UUID customer);
}
