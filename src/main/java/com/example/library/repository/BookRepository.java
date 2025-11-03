package com.example.library.repository;

import com.example.library.model.Book;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BookRepository {

    private final List<Book> books = new ArrayList<>();

    public BookRepository() {
        
        initializeData();
        System.out.println("BookRepository ініціалізовано");
    }

    private void initializeData() {
       
        books.add(new Book("11/22/63", "Стівен Кінг", "фантастика, трилер", "/img/king.jpg", "0+"));
        books.add(new Book("Скажи мені", "Унн Фрейзер", "роман, психологія", "/img/tell_me.jpg", "18+"));
        books.add(new Book("Краще не читай", "Катерина Орловська", "трилер, горор, проза", "/img/book8.jpg", "0+"));
        books.add(new Book("Аутсайдер", "Стівен Кінг", "трилер, горор, фантастика", "/img/book6.jpg", "16+"));
        books.add(new Book("Те, що бенкетує вночі", "Т. Кінгфішер", "горор, проза, детектив", "/img/book4.jpg", "12+"));
    }

    public List<Book> findAll() {
        return new ArrayList<>(books); 
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public List<Book> search(String author, String title, String keyword) {
        return books.stream()
                .filter(b ->
                        (author == null || author.isEmpty() || b.getAuthor().toLowerCase().contains(author.toLowerCase())) &&
                        (title == null || title.isEmpty() || b.getTitle().toLowerCase().contains(title.toLowerCase())) &&
                        (keyword == null || keyword.isEmpty() || b.getKeywords().toLowerCase().contains(keyword.toLowerCase()))
                )
                .collect(Collectors.toList());
    }

    public boolean deleteBook(String title) {
        return books.removeIf(book -> book.getTitle().equals(title));
    }
}