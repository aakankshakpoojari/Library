package service;
import model.*;
import structures.*;

public class LibraryService {
    private static LibraryService instance;
    private BookShelf bookShelf = new BookShelf();
    private IssueQueue issueQueue = new IssueQueue();
    private TransactionStack transactionStack = new TransactionStack();
    private static final int MAX_ISSUES_PER_USER = 3;
    
    private LibraryService() {}  
    
    public static LibraryService getInstance() {
        if (instance == null) {
            instance = new LibraryService();
        }
        return instance;
    }
    
    public void addBook(Book book) {
        bookShelf.addBook(book);
        transactionStack.logTransaction("Added: " + book.getTitle());
    }
    
    public void requestIssue(String isbn) {
        issueQueue.addRequest(isbn);
        transactionStack.logTransaction("Requested: " + isbn);
    }
    
    public String processIssue() {
        String isbn = issueQueue.processNext();
        if (isbn != null) {
            transactionStack.logTransaction("Processed: " + isbn);
        }
        return isbn;
    }

    public java.util.List<Book> getFictionBooks() {
        return bookShelf.getByCategory("Fiction");
    }
    
    public java.util.List<Book> getTextBooks() {
        return bookShelf.getByCategory("Textbook");
    }
    
    public java.util.List<Book> searchBooks(String title) {
        return bookShelf.searchByTitle(title);
    }
    public java.util.List<Book> getAllBooks() {
    return bookShelf.getAllBooks();
}
    public String getPendingQueueStatus() {
    return issueQueue.isEmpty() ? "No pending requests" : 
        "Next: " + issueQueue.getNext();
}

public structures.TransactionStack getTransactionStack() {
    return transactionStack;
}

}
    



