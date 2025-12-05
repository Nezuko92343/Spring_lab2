-- Drop tables if exist
DROP TABLE IF EXISTS books CASCADE;
DROP TABLE IF EXISTS authors CASCADE;

-- Create sequence for authors (demonstrates sequence usage)
CREATE SEQUENCE IF NOT EXISTS authors_id_seq START WITH 1 INCREMENT BY 1;

-- Create authors table with SERIAL primary key (auto-increment)
CREATE TABLE authors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    country VARCHAR(100),
    birth_year INTEGER,
    CONSTRAINT uk_author_name UNIQUE (name)
);

-- Create books table with SERIAL primary key and foreign key to authors
CREATE TABLE books (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author_id INTEGER NOT NULL,
    keywords VARCHAR(500),
    image_path VARCHAR(255),
    rating VARCHAR(10) DEFAULT '0+',
    publication_year INTEGER,
    isbn VARCHAR(20),
    CONSTRAINT fk_book_author FOREIGN KEY (author_id) REFERENCES authors(id) ON DELETE CASCADE,
    CONSTRAINT uk_book_isbn UNIQUE (isbn)
);

-- Create indexes for better search performance
CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_books_author_id ON books(author_id);
CREATE INDEX idx_books_rating ON books(rating);
CREATE INDEX idx_authors_name ON authors(name);

-- Insert test data for authors
INSERT INTO authors (name, country, birth_year) VALUES
    ('Stephen King', 'USA', 1947),
    ('Unn Frazer', 'Norway', 1980),
    ('Kateryna Orlovska', 'Ukraine', 1985),
    ('T. Kingfisher', 'USA', 1978),
    ('Taras Shevchenko', 'Ukraine', 1814),
    ('Lina Kostenko', 'Ukraine', 1930);

-- Insert test data for books
INSERT INTO books (title, author_id, keywords, image_path, rating, publication_year, isbn) VALUES
    ('11/22/63', 1, 'thriller, time travel, history', '/img/112263.jpg', '16+', 2011, '978-1-4516-2728-2'),
    ('The Outsider', 1, 'horror, mystery, detective', '/img/outsider.jpg', '18+', 2018, '978-1-5011-8098-2'),
    ('It', 1, 'horror, childhood, friendship', '/img/it.jpg', '18+', 1986, '978-0-670-81302-3'),
    ('Say to Me', 2, 'romance, drama, psychology', '/img/saytome.jpg', '16+', 2019, '978-82-999-0001-1'),
    ('Better Not to Read', 3, 'mystery, thriller, ukrainian', '/img/betternot.jpg', '12+', 2020, '978-617-999-0001-2'),
    ('What Feasts at Night', 4, 'horror, gothic, mystery', '/img/feasts.jpg', '16+', 2024, '978-1-250-83088-9'),
    ('Kobzar', 5, 'poetry, classic, ukrainian', '/img/kobzar.jpg', '0+', 1840, '978-966-999-0001-3'),
    ('Wanderings of the Heart', 6, 'poetry, romance, ukrainian', '/img/wanderings.jpg', '6+', 1961, '978-966-999-0002-4');

-- View for books with author names (useful for queries)
CREATE OR REPLACE VIEW books_with_authors AS
SELECT
    b.id,
    b.title,
    a.id as author_id,
    a.name as author_name,
    a.country as author_country,
    b.keywords,
    b.image_path,
    b.rating,
    b.publication_year,
    b.isbn
FROM books b
JOIN authors a ON b.author_id = a.id;
