package de.whitebox.infrastructure.shared;

import de.whitebox.application.api.*;
import de.whitebox.application.api.Query.*;
import org.springframework.stereotype.*;

import java.util.*;

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
