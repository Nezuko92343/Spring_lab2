package com.example.library.config;

import com.example.library.service.BookServicePrototype;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AppConfig {

    @Bean
    @Primary  // Цей бін буде використовуватись за замовчуванням
    public BookServicePrototype prototypeBean() {
        System.out.println("Створення prototype біна через @Bean");
        return new BookServicePrototype();
    }

    @Bean
    public String libraryName() {
        return "Моя бібліотека";
    }
}