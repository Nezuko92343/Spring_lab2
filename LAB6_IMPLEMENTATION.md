# Лабораторна робота 6 - Реалізація

## 1. Встановлення СУБД (PostgreSQL через Docker)

**Файл:** `docker-compose.yml`
- Створено Docker Compose конфігурацію для PostgreSQL 16
- База даних: `library`
- Користувач: `library_user`
- Пароль: `library_pass`
- Порт: `5440`

## 2. Створення таблиць з тестовими даними

**Файл:** `src/main/resources/data.sql`
- Таблиця `authors` (один): id, name, country
- Таблиця `books` (багато): id, title, author_id (FK), keywords, image_path, rating, isbn, publication_year
- Первинний ключ books.id використовує sequence (`book_sequence`)
- Відношення один-до-багатьох: Author ← Book
- Додано 5 авторів та 7 книг

## 3. Налаштування Spring Boot

**Файл:** `pom.xml`
- `spring-boot-starter-data-jpa` - JPA підтримка
- `postgresql` - драйвер PostgreSQL
- `springdoc-openapi-starter-webmvc-ui` (v2.3.0) - OpenAPI/Swagger документація

**Файл:** `src/main/resources/application.properties`
- Підключення до PostgreSQL (localhost:5440)
- Hibernate DDL auto-update
- Відображення SQL запитів
- Автоматична ініціалізація даних з data.sql

## 4. Entity класи (ORM)

**Файл:** `src/main/java/com/example/library/entity/Author.java`
- @Entity, @Table(name = "authors")
- @Id з @GeneratedValue(strategy = IDENTITY)
- @OneToMany відношення до Book
- Lombok анотації (@Data, @NoArgsConstructor, @AllArgsConstructor)

**Файл:** `src/main/java/com/example/library/entity/Book.java`
- @Entity, @Table(name = "books")
- @Id з @GeneratedValue(strategy = SEQUENCE, generator = "book_seq")
- @SequenceGenerator для генерації ID
- @ManyToOne відношення до Author
- @NamedQuery для пошуку за роками публікації

## 5. Repository інтерфейси (CrudRepository)

**Файл:** `src/main/java/com/example/library/repository/BookJpaRepository.java`
- Розширює CrudRepository<Book, Long>
- **@Query (JPQL):** `findByRating(String rating)` - пошук за рейтингом
- **@NamedQuery:** `findByPublicationYearBetween(Integer, Integer)` - пошук за роками
- **Spring Data JPA методи:**
  - `findByAuthorName(String name)` - автоматична генерація
  - `findByTitleContainingIgnoreCase(String title)`
  - `findByKeywordsContainingIgnoreCase(String keyword)`

**Файл:** `src/main/java/com/example/library/repository/AuthorJpaRepository.java`
- Розширює CrudRepository<Author, Long>
- **@Query (JPQL):** `findByCountry(String country)` - пошук за країною
- **Spring Data JPA метод:** `findByNameContainingIgnoreCase(String name)`

## 6. Service рівень з транзакціями

**Файл:** `src/main/java/com/example/library/service/BookServiceJpa.java`
- CRUD операції для Book
- **@Transactional метод:** `addBookWithNewAuthor(BookDTO, AuthorDTO)`
  - Створює автора та книгу атомарно
  - При помилці створення книги - автор також не зберігається (rollback)
- Методи пошуку з використанням Repository

**Файл:** `src/main/java/com/example/library/service/AuthorServiceImpl.java`
- CRUD операції для Author
- @Transactional методи: createAuthor, updateAuthor, deleteAuthor
- Конвертація Entity ↔ DTO

## 7. REST Controllers (RESTful API)

**Файл:** `src/main/java/com/example/library/controller/rest/BookRestController.java`
Endpoints:
- `GET /api/books` - список всіх книг
- `GET /api/books/{id}` - книга за ID
- `POST /api/books` - створити книгу
- `PUT /api/books/{id}` - оновити книгу
- `DELETE /api/books/{id}` - видалити книгу
- `GET /api/books/search/rating?rating=` - пошук за рейтингом (JPQL @Query)
- `GET /api/books/search/year?startYear=&endYear=` - пошук за роками (@NamedQuery)
- `GET /api/books/search/author?name=` - пошук за автором (Spring Data JPA)
- `GET /api/books/search/title?title=` - пошук за назвою
- `GET /api/books/search/keyword?keyword=` - пошук за ключовими словами
- `POST /api/books/with-author` - транзакційне створення книги з автором

**Файл:** `src/main/java/com/example/library/controller/rest/AuthorRestController.java`
Endpoints:
- `GET /api/authors` - список всіх авторів
- `GET /api/authors/{id}` - автор за ID
- `POST /api/authors` - створити автора
- `PUT /api/authors/{id}` - оновити автора
- `DELETE /api/authors/{id}` - видалити автора
- `GET /api/authors/search/country?country=` - пошук за країною (JPQL @Query)
- `GET /api/authors/search/name?name=` - пошук за ім'ям (Spring Data JPA)

## 8. OpenAPI документація

**Файл:** `src/main/java/com/example/library/config/OpenApiConfig.java`
- Налаштування OpenAPI специфікації
- Інформація про API (назва, версія, опис)

**В Controllers:**
- `@Tag` - опис групи endpoints
- `@Operation` - короткий та повний опис операції
- `@Parameter` - опис параметрів
- `@ApiResponses` - коди HTTP статусів (200, 201, 204, 400, 404, 500)
- `@ApiResponse` - опис кожної відповіді

**Доступ до документації:**
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

## 9. DTO класи

**Файл:** `src/main/java/com/example/library/dto/BookDTO.java`
- Передача даних між шарами
- Поля: id, title, authorId, authorName, keywords, imagePath, rating, isbn, publicationYear

**Файл:** `src/main/java/com/example/library/dto/AuthorDTO.java`
- Поля: id, name, country

## Запуск проекту

1. Запустити PostgreSQL:
```bash
docker-compose up -d
```

2. Запустити Spring Boot:
```bash
./mvnw spring-boot:run
```

3. Відкрити Swagger UI:
```
http://localhost:8080/swagger-ui.html
```

## Тестування транзакцій

**Успішний сценарій:**
- POST `/api/books/with-author` з валідними даними
- Автор та книга створюються обидва

**Сценарій з помилкою:**
- POST `/api/books/with-author` з невалідними даними книги
- Транзакція відкочується, автор НЕ створюється

## Демонстрація вимог лабораторної

✅ **Вимога 1:** PostgreSQL через Docker
✅ **Вимога 2:** Таблиці з sequence для books.id, відношення 1:N (authors ← books)
✅ **Вимога 3:** Spring Boot проект
✅ **Вимога 4:** @Entity класи (Author, Book) + DTO
✅ **Вимога 5.1.1:** JPQL з @Query (findByRating, findByCountry)
✅ **Вимога 5.1.2:** @NamedQuery (findByPublicationYearBetween)
✅ **Вимога 5.2:** Spring Data JPA методи (findByAuthorName, findByNameContaining)
✅ **Вимога 6:** Service з @Transactional (addBookWithNewAuthor)
✅ **Вимога 7:** RESTful API Controllers
✅ **Вимога 8:** OpenAPI документація з описами, параметрами, HTTP кодами
✅ **Вимога 9:** Тестування через Swagger UI
