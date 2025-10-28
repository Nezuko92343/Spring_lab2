package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookServiceSetter {
    private BookRepository repo;

    
    @Autowired
    public void setRepo(BookRepository repo) {
        this.repo = repo;
        System.out.println("BookServiceSetter: репозиторій ін'єктовано через сетер");
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