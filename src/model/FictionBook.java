package model;
public class FictionBook extends Book {
    public FictionBook(String isbn, String title, String author) {
        super(isbn, title, author);
    }
    @Override
    public String getCategory() { return "Fiction"; }
}

