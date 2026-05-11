package model;
import java.util.Stack;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String id, name;
    private List<String> issuedBooksIsbn = new ArrayList<>(); // Current books issued
    private Stack<String> issueHistory = new Stack<>(); // History for undo
    
    public User(String id, String name) {
        this.id = id; 
        this.name = name;
    }
    
    public void addIssue(String bookIsbn) { 
        issuedBooksIsbn.add(bookIsbn);
        issueHistory.push(bookIsbn);
    }
    
    public boolean removeIssue(String bookIsbn) {
        return issuedBooksIsbn.remove(bookIsbn);
    }
    
    public String undoLastIssue() { 
        return issueHistory.isEmpty() ? null : issueHistory.pop(); 
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
    
    public Stack<String> getIssueHistory() { 
        return issueHistory; 
    }
    
    public int getIssueCount() { 
        return issuedBooksIsbn.size();
    }
    
    public List<String> getIssuedBooks() {
        return new ArrayList<>(issuedBooksIsbn);
    }
    
    public boolean hasBook(String isbn) {
        return issuedBooksIsbn.contains(isbn);
    }
}
