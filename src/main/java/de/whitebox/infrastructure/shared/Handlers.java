package de.whitebox.infrastructure.shared;

import de.whitebox.application.api.*;
import de.whitebox.domain.bank.*;
import de.whitebox.domain.shared.*;
import org.springframework.stereotype.*;

/**
 * This is a simple mediator, it listens for event and dispatch according
 * It MUST be a singleton in an application.
 * It is related to how the application communicate with other parts,e.g: CQRS
 *
 * @param broker Broker providing publish and subscriber methods
 */
@Component
public record Handlers(Broker broker, Ledger ledger) {
    public Handlers {
        broker.subscribes(event -> {
            if (event instanceof Credit credit) {
                ledger.performed(credit);
            } else if (event instanceof Debit debit) {
                ledger.performed(debit);
            } else if (event instanceof Opened opened) {
                ledger.opened(opened);
            }
        });
    }
}
