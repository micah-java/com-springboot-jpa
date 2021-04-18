package com;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class ComSpringbootJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComSpringbootJpaApplication.class, args);
    }

}
