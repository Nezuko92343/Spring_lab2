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
import java.util.Objects;

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
                          @RequestParam(required = false) String rating,
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

        Book newBook = Book.builder()
                .title(title)
                .authorName(author)
                .keywords(keywords)
                .imagePath(finalImagePath)
                .rating(Objects.requireNonNullElse(rating, "0+"))
                .build();

        bookRepository.addBook(newBook);

        return "redirect:/admin";
    }

    @PostMapping("/admin/edit")
    public String editBook(@RequestParam String oldTitle,
                           @RequestParam String oldAuthor,
                           @RequestParam String title,
                           @RequestParam String author,
                           @RequestParam String keywords,
                           @RequestParam(required = false) String rating,
                           @RequestParam(required = false) String imagePath,
                           @RequestParam(required = false) MultipartFile file) throws IOException {

        Book book = bookRepository.findByTitleAndAuthor(oldTitle, oldAuthor);

        if (book != null) {
            book.setTitle(title);
            book.setAuthorName(author);
            book.setKeywords(keywords);
            book.setRating(Objects.requireNonNullElse(rating, "0+"));

            if (file != null && !file.isEmpty()) {
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
        Book book = bookRepository.findByTitleAndAuthor(title, author);

        if (book != null) {
            bookRepository.deleteBook(title);
        }
        return "redirect:/admin";
    }
}
