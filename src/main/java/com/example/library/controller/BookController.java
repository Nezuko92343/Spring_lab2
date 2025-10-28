package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.service.BookService;
import com.example.library.service.BookServicePrototype;
import com.example.library.service.BookServiceSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BookController {
    private final BookService service;
    private final BookServiceSetter serviceSetter;
    
    @Autowired
    private BookServicePrototype bookServicePrototype;

    public BookController(BookService service, BookServiceSetter serviceSetter, ApplicationContext context) {
        this.service = service;
        this.serviceSetter = serviceSetter;
       
    }

    @GetMapping("/")
    public String home(Model model) {
        
        List<Book> booksConstructor = service.getAll();
        List<Book> booksSetter = serviceSetter.getAll();
        
        
        List<Book> booksPrototype = bookServicePrototype.getAll();

        model.addAttribute("booksConstructor", booksConstructor);
        model.addAttribute("booksSetter", booksSetter);
        model.addAttribute("booksPrototype", booksPrototype);
        
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String author,
                         @RequestParam(required = false) String title,
                         @RequestParam(required = false) String keyword,
                         Model model) {

        List<Book> resultsConstructor = service.search(author, title, keyword);
        List<Book> resultsSetter = serviceSetter.search(author, title, keyword);
        List<Book> resultsPrototype = bookServicePrototype.search(author, title, keyword);


        if (resultsConstructor == null) resultsConstructor = new ArrayList<>();
        if (resultsSetter == null) resultsSetter = new ArrayList<>();
        if (resultsPrototype == null) resultsPrototype = new ArrayList<>();

        model.addAttribute("resultsConstructor", resultsConstructor);
        model.addAttribute("resultsSetter", resultsSetter);
        model.addAttribute("resultsPrototype", resultsPrototype);
        model.addAttribute("searchAuthor", author);
        model.addAttribute("searchTitle", title);
        model.addAttribute("searchKeyword", keyword);
        
        return "search";
    }
}