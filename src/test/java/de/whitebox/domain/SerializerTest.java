package de.whitebox.domain;

import de.whitebox.domain.bank.*;
import de.whitebox.domain.shared.*;
import org.junit.jupiter.api.*;

import static de.whitebox.domain.bank.Account.*;
import static java.util.List.*;
import static java.util.UUID.*;
import static org.junit.jupiter.api.Assertions.*;

class SerializerTest {
    @Test
    void shouldUnmarshallAnEntity() {
        var initial = new Opening(300.00);
        var line = new Granted(100.00);
        var customer = new Customer("", "");
        var locked = randomUUID();
        var id = randomUUID();

        var unmarshalled = Serializer.serialize(Account.class, of(new Opened(customer, initial, line)), locked, id);
        var expected = open(customer, initial, line);
        assertEquals(expected, unmarshalled);
        assertEquals(locked, unmarshalled.locked());
        assertEquals(id, unmarshalled.id());
    }
}
