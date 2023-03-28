package de.whitebox.domain.bank;

import de.whitebox.domain.shared.*;

/**
 * A Value Object defining an opening deposit
 *
 * @param deposit Initial deposit that cannot be less than 25.00u
 */
public record Opening(double deposit, double minimum) implements ValueObject {
    public Opening(double deposit) {
        this(deposit, 25);
    }

    public boolean isMinimumRequired() {
        return deposit >= minimum();
    }
}
