package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class AdminController {

    private final BookRepository bookRepository;

    
    public AdminController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/admin")
public String adminPage(Model model) {
    model.addAttribute("books", bookRepository.findAll());
    return "admin"; 
}


    @PostMapping("/admin/add")
    public String addBook(@RequestParam String title,
                          @RequestParam String author,
                          @RequestParam String keywords,
                          @RequestParam(required = false) String imagePath,
                          @RequestParam(required = false) MultipartFile file) throws IOException {

        String finalImagePath = imagePath;

        
        if (file != null && !file.isEmpty()) {
            String uploadDir = "src/main/resources/static/img/";
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);
            Files.write(filePath, file.getBytes());
            finalImagePath = "/img/" + fileName;
        }

        Book newBook = new Book(title, author, keywords, finalImagePath);
        bookRepository.addBook(newBook);

        return "redirect:/admin";
    }
    
    @PostMapping("/admin/edit")
public String editBook(@RequestParam String oldTitle,
                       @RequestParam String oldAuthor,
                       @RequestParam String title,
                       @RequestParam String author,
                       @RequestParam String keywords,
                       @RequestParam(required = false) String imagePath,
                       @RequestParam(required = false) MultipartFile file) throws IOException {

    Book book = bookRepository.findAll().stream()
            .filter(b -> b.getTitle().equals(oldTitle) && b.getAuthor().equals(oldAuthor))
            .findFirst().orElse(null);

    if(book != null) {
        book.setTitle(title);
        book.setAuthor(author);
        book.setKeywords(keywords);

        if(file != null && !file.isEmpty()) {
            String uploadDir = "src/main/resources/static/img/";
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);
            Files.write(filePath, file.getBytes());
            book.setImagePath("/img/" + fileName);
        } else {
            book.setImagePath(imagePath);
        }
    }

    return "redirect:/admin";
}

@PostMapping("/admin/delete")
public String deleteBook(@RequestParam String title, @RequestParam String author) {
    Book book = bookRepository.findAll().stream()
            .filter(b -> b.getTitle().equals(title) && b.getAuthor().equals(author))
            .findFirst()
            .orElse(null);

    if (book != null) {
        bookRepository.findAll().remove(book);
    }
    return "redirect:/admin";
}

}