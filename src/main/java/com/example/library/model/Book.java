package com.example.library.model;

public class Book {
    private String title;
    private String author;
    private String keywords;
    private String imagePath; 

    
    public Book(String title, String author, String keywords, String imagePath) {
        this.title = title;
        this.author = author;
        this.keywords = keywords;
        this.imagePath = imagePath;
    }

    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public String getKeywords() { return keywords; }
    public void setKeywords(String keywords) { this.keywords = keywords; }
    
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
