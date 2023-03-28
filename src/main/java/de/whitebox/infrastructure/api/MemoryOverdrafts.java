package de.whitebox.infrastructure.api;

import de.whitebox.application.api.*;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * Simplest memory implementation, in real application it would be a real database.
 * PS: Documents or key values database fits better for this kind of data structure.
 */
@Repository
public class MemoryOverdrafts implements Overdrafts {

    private Map<UUID, Overdraft> data = new HashMap<>();

    @Override
    public Overdraft of(UUID customer) {
        return data.get(customer);
    }

    @Override
    public void insert(Overdraft overdraft) {
        data.putIfAbsent(overdraft.account(), overdraft);
    }

    @Override
    public void remove(UUID account) {
        data.remove(account);
    }

    @Override
    public List<Overdraft> all() {
        return new ArrayList<>(data.values());
    }
}
