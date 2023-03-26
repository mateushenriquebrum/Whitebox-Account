package de.whitebox.application.api;

import de.whitebox.application.api.Ledger.*;

import java.util.*;

public interface Transactions {
    void add(Transaction transaction);
    List<Transaction> of(UUID customer);
}
