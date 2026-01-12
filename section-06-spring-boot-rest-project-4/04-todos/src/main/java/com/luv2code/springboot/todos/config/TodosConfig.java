package com.luv2code.springboot.todos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class TodosConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}

