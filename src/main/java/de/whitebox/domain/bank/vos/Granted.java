package de.whitebox.domain.bank.vos;

import de.whitebox.domain.shared.*;

/**
 * A Value Object defining a simple credit credit schema
 *
 * @param granted The amount credit as credit for a customer
 */
public record Granted(double granted) implements ValueObject {
    public Granted {
        if (granted < 0) throw new IllegalArgumentException("Credit credit should never be less then zero");
    }

    public static Granted standardCreditLine() {
        return new Granted(100.00);
    }

    public boolean notGrantedFor(double balance) {
        return balance + granted < 0;
    }
}
