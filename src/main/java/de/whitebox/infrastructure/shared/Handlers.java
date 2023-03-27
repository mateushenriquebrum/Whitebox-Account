package de.whitebox.infrastructure.shared;

import de.whitebox.application.api.*;
import de.whitebox.application.bank.*;
import de.whitebox.domain.bank.*;
import org.springframework.stereotype.*;

/**
 * This is a simple mediator, it listens for event and dispatch according
 * It MUST be a singleton in an application.
 * It is related to how the application communicate with other parts,e.g: CQRS
 *
 * @param broker Broker providing publish and subscriber methods
 */
@Component
public record Handlers(Broker broker, Query query) {
    public Handlers {
        broker.subscribes(event -> {
            if (event instanceof Credited credited) {
                query.add(credited);
            } else if (event instanceof Debited debited) {
                query.add(debited);
            } else if (event instanceof Opened opened) {
                query.add(opened);
            } else if (event instanceof Balanced balance) {
                query.add(balance);
            } else if (event instanceof Overdrafted overdrafted) {
                query.add(overdrafted);
            }
        });
    }
}
