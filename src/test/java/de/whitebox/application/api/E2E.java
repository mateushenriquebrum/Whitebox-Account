package de.whitebox.application.api;

import de.whitebox.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.test.web.servlet.*;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * The intention is to go throw the whole system end to end and verify if all the parts
 * are assembled correctly, also it generates some confidence in infrastructure queries and serialization.
 * But should never be used to test domain logic as it is slow and brittle.
 */
@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
class E2E {

    @Autowired
    private MockMvc server;

    private String id;

    @Test
    void shouldGoFromCreationToOverdraftRecovery() throws Exception {
        open();
        debit(101);
        overdraft();
        credit(2);
        balance();
    }

    private void open() throws Exception {
        var request = post("/account/open")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """ 
                                {
                                "name" : "Mateus",
                                "surname": "Brum",
                                "deposit": 100
                                }
                                """);

        var response = server
                .perform(request)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");
        id = response.split("/")[2];
    }

    private void debit(double amount) throws Exception {
        var request = post("/account/debit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"id\" : \"" + id + "\", \"amount\": " + amount + "}");
        server
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].balance").value("-1.0"))
                .andExpect(jsonPath("$[1].amount").value("101.0"))
                .andExpect(jsonPath("$[1].type").value("OUT"));
    }

    private void credit(double amount) throws Exception {
        var request = post("/account/credit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"id\" : \"" + id + "\", \"amount\": " + amount + "}");
        server
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[2].balance").value("1.0"))
                .andExpect(jsonPath("$[2].amount").value("2.0"))
                .andExpect(jsonPath("$[2].type").value("IN"));
    }

    private void overdraft() throws Exception {
        var request = get("/account/overdrafts")
                .contentType(MediaType.APPLICATION_JSON);
        server
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].account").value(id));
    }

    private void balance() throws Exception {
        var request = get("/account/overdrafts")
                .contentType(MediaType.APPLICATION_JSON);
        server
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", empty()));
    }
}
