package model;
import java.util.Stack;

public class User {
    private String id, name;
    private Stack<String> issueHistory = new Stack<>();
    
    public User(String id, String name) {
        this.id = id; this.name = name;
    }
    
    public void addIssue(String bookTitle) { 
        issueHistory.push(bookTitle); 
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
        return issueHistory.size(); 
}
    

}
