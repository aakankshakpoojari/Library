package structures;
import model.*;
public class TestStructures {
    public static void main(String[] args) {
        BookShelf shelf = new BookShelf();
        shelf.addBook(new FictionBook("F1", "Harry Potter", "Rowling"));
        shelf.addBook(new TextBook("T1", "Java", "Author"));
        
        System.out.println("Fiction: " + shelf.getByCategory("Fiction").size());
        System.out.println("Search Java: " + shelf.searchByTitle("Java").size());
    }
}
