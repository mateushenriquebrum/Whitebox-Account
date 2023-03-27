package de.whitebox.domain;

import de.whitebox.domain.bank.*;
import org.junit.jupiter.api.*;

import static de.whitebox.domain.bank.Account.*;
import static de.whitebox.domain.shared.Serializer.serialize;
import static java.util.List.*;
import static java.util.UUID.*;
import static org.junit.jupiter.api.Assertions.*;

class SerializerTest {
    @Test
    void shouldUnmarshallAnEntity() {
        var initial = new Opening(300.00);
        var line = new Granting(100.00);
        var customer = new Customer("", "");
        var locked = randomUUID();
        var id = randomUUID();

        var unmarshalled = serialize(Account.class, of(new Opened(id, initial, line, customer)), locked, id);
        var expected = open(customer, initial, line);
        assertEquals(expected, unmarshalled);
        assertEquals(locked, unmarshalled.locked());
        assertEquals(id, unmarshalled.id());
    }
}
