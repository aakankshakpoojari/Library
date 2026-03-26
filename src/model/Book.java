package model;

public abstract class Book {
    private String isbn, title, author;

    public Book(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }

    public abstract String getCategory();

    @Override
    public String toString() {
        return String.format("[%s] %s by %s (%s)", isbn, title, author, getCategory());
    }
}