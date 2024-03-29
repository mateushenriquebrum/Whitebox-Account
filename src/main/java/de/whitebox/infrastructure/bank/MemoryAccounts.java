package de.whitebox.infrastructure.bank;

import de.whitebox.application.bank.*;
import de.whitebox.domain.bank.*;
import de.whitebox.domain.shared.*;
import org.springframework.stereotype.*;

import java.util.*;

import static de.whitebox.domain.shared.Serializer.*;
import static java.util.UUID.*;

/**
 * Simplest memory implementation, in real application it would be a real database.
 * PS: Documents or key values database fits better for this kind of data structure.
 */
@Repository
public class MemoryAccounts implements Accounts {

    private Map<UUID, Source> data = new HashMap<>();

    /**
     * It is utterly important that shared memory manipulation is atomic,
     * otherwise it is impossible detect changes in data version, the same apply to any database.
     */
    public synchronized void persist(List<Event> events, UUID locked, UUID id) {
        data.computeIfPresent(id, (iid, src) -> {
            if (src.version != locked) {
                throw new OptimisticLockException();
            }
            var added = new ArrayList<>(src.events());
            added.addAll(events);
            return new Source("ACCOUNTS", randomUUID(), added);
        });
        data.putIfAbsent(id, new Source("ACCOUNTS", randomUUID(), events));
    }

    @Override
    public void persist(Account account) {
        persist(account.changes(), account.locked(), account.id());
    }

    @Override
    public Account of(UUID id) {
        var source = data.get(id);
        return serialize(Account.class, source.events(), source.version(), id);
    }

    /**
     * It represents a row in a SQL database or a collection in MongoDB for example
     *
     * @param entity  Entity type, e.g: Account, Customers, CreditLine, etc
     * @param version The latest version persisted
     * @param events  All the events persisted, it MUST change version everytime new events are persisted
     */
    record Source(String entity, UUID version, List<Event> events) {
    }

    public static class OptimisticLockException extends RuntimeException {
    }

}
