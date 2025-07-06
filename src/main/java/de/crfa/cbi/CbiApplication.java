package de.crfa.cbi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "de.crfa.cbi.repository")
@EntityScan(basePackages = "de.crfa.cbi.entity")
public class CbiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CbiApplication.class, args);
    }
}
