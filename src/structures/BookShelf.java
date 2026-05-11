package structures;
import model.Book;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookShelf {
    private ArrayList<Book> books = new ArrayList<>();
    private HashMap<String, List<Book>> categoryMap = new HashMap<>();
    
    public void addBook(Book book) {
        books.add(book);
        categoryMap.computeIfAbsent(book.getCategory(), k -> new ArrayList<>())
                  .add(book);
    }
    
    public List<Book> searchByTitle(String title) {
        return books.stream().filter(b -> b.getTitle().contains(title))
                           .toList();
    }
    
    public List<Book> getByCategory(String category) {
        return categoryMap.getOrDefault(category, new ArrayList<>());
    }
    
    public ArrayList<Book> getAllBooks() { return new ArrayList<>(books); }
    
    public void removeBook(String isbn) {
        books.removeIf(b -> b.getIsbn().equals(isbn));
       
        categoryMap.values().forEach(list -> list.removeIf(b -> b.getIsbn().equals(isbn)));
    }
}

