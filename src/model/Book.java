package model;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }
    
    @Override
    public String toString() {
        return title + " by " + author + " [" + getCategory() + "]";
    }
}

