package model;
public class TextBook extends Book {
    public TextBook(String isbn, String title, String author) {
        super(isbn, title, author);
    }
    @Override
    public String getCategory() { return "Textbook"; }
}
