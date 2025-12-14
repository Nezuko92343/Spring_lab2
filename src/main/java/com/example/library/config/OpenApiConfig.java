package com.example.library.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Library Management API")
                        .version("1.0")
                        .description("RESTful API для управління бібліотекою книг та авторів. " +
                                "Підтримує повний CRUD функціонал, пошук за різними критеріями та транзакційні операції.")
                        .contact(new Contact()
                                .name("Library API Support")
                                .email("support@library.com")));
    }
}
