package de.whitebox;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.*;

/**
 * The entry point to start the whole application
 */
@SpringBootApplication
@ComponentScan(basePackages = "de.whitebox.infrastructure")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}