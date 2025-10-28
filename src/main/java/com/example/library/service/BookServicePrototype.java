package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Scope("prototype")
public class BookServicePrototype {
    
    @Autowired 
    private BookRepository repo;

    public BookServicePrototype() {
        System.out.println("BookServicePrototype створено (prototype)");
    }

    public List<Book> getAll() {
        return repo.findAll();
    }

    public void addBook(Book book) {
        repo.addBook(book);
    }

    public List<Book> search(String author, String title, String keyword) {
        return repo.search(author, title, keyword);
    }
}