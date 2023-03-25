package de.whitebox.domain.bank;

import de.whitebox.domain.shared.*;

/**
 * A Value Object defining an opening deposit
 *
 * @param deposit Initial deposit that cannot be less than 25.00u
 */
public record Opening(double deposit) implements ValueObject {
    public boolean isMinimumRequired() {
        return deposit >= 25.00;
    }
}
