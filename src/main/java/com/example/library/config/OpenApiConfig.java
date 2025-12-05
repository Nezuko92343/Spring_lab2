package com.example.library.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI (Swagger) configuration for the Library API.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI libraryOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Library Management API")
                        .description("""
                                RESTful API for managing a library catalog.

                                ## Features
                                - **Books Management**: CRUD operations for books with search capabilities
                                - **Authors Management**: CRUD operations for authors
                                - **Transactional Operations**: Create author with books, transfer books between authors

                                ## Technologies
                                - Spring Boot 3.x
                                - PostgreSQL with JDBC
                                - JdbcTemplate and JdbcClient for data access

                                ## Data Access Patterns
                                - **BookDao**: Implemented using JdbcTemplate (traditional approach)
                                - **AuthorDao**: Implemented using JdbcClient (modern Spring 6.1+ approach)

                                ## Transaction Demonstration
                                The API includes endpoints that demonstrate transactional behavior:
                                - `POST /api/authors/with-books` - Creates author with books atomically
                                - `POST /api/authors/{fromId}/transfer-books/{toId}` - Transfers books atomically
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Library API Support")
                                .email("support@library.example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local development server")
                ));
    }
}
