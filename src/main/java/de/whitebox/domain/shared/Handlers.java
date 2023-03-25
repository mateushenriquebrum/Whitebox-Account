package de.whitebox.domain.shared;

import de.whitebox.domain.bank.events.*;

/**
 * This is a simple mediator, it listens for event and dispatch according
 * It should be a singleton in an application,
 * and it is most related to the application flow
 * @param broker Broker providing publish and subscriber methods
 */
public record Handlers(Broker broker) {
    public Handlers {
        broker.subscribes(event -> {
            if(event instanceof Balance balance) {
                System.out.println(balance);
            } else if (event instanceof Opened opened) {
                System.out.println(opened);
            }
        });
    }
}
