package de.whitebox.infrastructure.api;

import de.whitebox.application.api.Query.*;
import de.whitebox.application.api.*;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * Simplest memory implementation, in real application it would be a real database.
 * PS: Documents or key values database fits better for this kind of data structure.
 */
@Repository
public class MemoryRegisters implements Registers {

    private Map<UUID, List<Transaction>> data = new HashMap<>();

    @Override
    public void add(Transaction transaction) {
        data.putIfAbsent(transaction.account(), new ArrayList<>());
        data.computeIfPresent(transaction.account(), (i, d) -> {
            var added = new ArrayList<>(d);
            added.add(transaction);
            return added;
        });
    }

    @Override
    public List<Transaction> of(UUID account) {
        return data.get(account);
    }
}
