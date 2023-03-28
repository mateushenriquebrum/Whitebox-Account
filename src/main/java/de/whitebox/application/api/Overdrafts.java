package de.whitebox.application.api;


import java.util.*;

/**
 * Overdrafts repository
 */
public interface Overdrafts {
    Overdraft of(UUID customer);

    void insert(Overdraft overdraft);

    void remove(UUID account);

    List<Overdraft> all();
}
