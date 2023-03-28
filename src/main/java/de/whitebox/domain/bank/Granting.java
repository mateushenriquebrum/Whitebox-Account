package de.whitebox.domain.bank;

import de.whitebox.domain.shared.*;

/**
 * A Value Object defining a simple credit line schema
 *
 * @param granted The amount credit as credit for a customer
 */
public record Granting(double granted) implements ValueObject {
    public Granting {
        if (granted < 0) throw new IllegalArgumentException("Credit credit should never be less then zero");
    }

    public static Granting standardCreditLine() {
        return new Granting(100.00);
    }

    public boolean notGrantedFor(double balance) {
        return balance + granted < 0;
    }
}
