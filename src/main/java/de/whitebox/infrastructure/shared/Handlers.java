package de.whitebox.infrastructure.shared;

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
public record Handlers(Broker broker) {
    public Handlers {
        broker.subscribes(event -> {
            if (event instanceof Balance balance) {
                System.out.println(balance);
            } else if (event instanceof Opened opened) {
                System.out.println(opened);
            }
        });
    }
}
