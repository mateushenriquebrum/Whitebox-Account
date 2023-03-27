package de.whitebox.application.api;

import de.whitebox.application.api.Query.*;

import java.util.*;

public interface Registers {
    void add(Transaction transaction);
    List<Transaction> of(UUID customer);
}
