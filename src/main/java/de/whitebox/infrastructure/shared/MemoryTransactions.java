package de.whitebox.infrastructure.shared;

import de.whitebox.application.api.*;
import de.whitebox.application.api.Ledger.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class MemoryTransactions implements Transactions {

    private Map<UUID, List<Transaction>> data = new HashMap<>();

    @Override
    public void add(Transaction transaction) {
        data.putIfAbsent(transaction.customer(), new ArrayList<>());
        data.computeIfPresent(transaction.customer(), (i, d) -> {
           var added = new ArrayList<>(d);
           added.add(transaction);
           return added;
        });
    }

    @Override
    public List<Transaction> of(UUID customer) {
        return data.get(customer);
    }
}
