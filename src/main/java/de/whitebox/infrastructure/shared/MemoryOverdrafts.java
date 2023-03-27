package de.whitebox.infrastructure.shared;

import de.whitebox.application.api.*;
import org.springframework.stereotype.*;

import java.util.*;

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
}
