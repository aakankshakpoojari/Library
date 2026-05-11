package structures;

/**
 * Represents a reversible transaction that can be undone.
 * Each transaction stores the action type and necessary data to reverse the operation.
 */
public class Transaction {
    public enum ActionType {
        ADD_BOOK,
        ISSUE_BOOK,
        RETURN_BOOK,
        REMOVE_BOOK
    }
    
    private ActionType action;
    private String bookIsbn;
    private String bookTitle;
    private String userId;
    private String category;
    private String author;
    private long timestamp;
    
    public Transaction(ActionType action, String bookIsbn, String bookTitle, 
                      String author, String category, String userId) {
        this.action = action;
        this.bookIsbn = bookIsbn;
        this.bookTitle = bookTitle;
        this.author = author;
        this.category = category;
        this.userId = userId;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters
    public ActionType getAction() { return action; }
    public String getBookIsbn() { return bookIsbn; }
    public String getBookTitle() { return bookTitle; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public String getUserId() { return userId; }
    public long getTimestamp() { return timestamp; }
    
    @Override
    public String toString() {
        return String.format("[%s] %s - ISBN: %s, Title: %s%s",
            action,
            System.currentTimeMillis() - timestamp < 1000 ? "Just now" : "Earlier",
            bookIsbn,
            bookTitle,
            userId != null ? ", User: " + userId : "");
    }
}
