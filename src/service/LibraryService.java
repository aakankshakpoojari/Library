package service;
import model.*;
import structures.*;
import java.io.*;
import java.util.*;

/**
 * Core library management service with validation, persistence, and reversible operations.
 * Implements singleton pattern and enforces business rules.
 */
public class LibraryService {
    private static LibraryService instance;
    private BookShelf bookShelf = new BookShelf();
    private IssueQueue issueQueue = new IssueQueue();
    private TransactionStack transactionStack = new TransactionStack();
    private Map<String, User> users = new HashMap<>();
    
    private static final int MAX_ISSUES_PER_USER = 3;
    private static final String DATA_FILE = "library_data.csv";
    private static final String USERS_FILE = "users_data.csv";
    
    private LibraryService() {
        loadFromFile();
    }
    
    public static LibraryService getInstance() {
        if (instance == null) {
            instance = new LibraryService();
        }
        return instance;
    }
    
    // ===== VALIDATION METHODS =====
    
    private void validateBook(Book book) throws IllegalArgumentException {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be empty");
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be empty");
        }
    }
    
    private void validateDuplicateIsbn(String isbn) throws IllegalArgumentException {
        for (Book book : bookShelf.getAllBooks()) {
            if (book.getIsbn().equals(isbn)) {
                throw new IllegalArgumentException("ISBN already exists: " + isbn);
            }
        }
    }
    
    private void validateUser(String userId) throws IllegalArgumentException {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }
    }
    
    // ===== BOOK MANAGEMENT =====
    
    public void addBook(Book book) throws IllegalArgumentException {
        validateBook(book);
        validateDuplicateIsbn(book.getIsbn());
        
        bookShelf.addBook(book);
        
        // Log transaction
        Transaction transaction = new Transaction(
            Transaction.ActionType.ADD_BOOK,
            book.getIsbn(),
            book.getTitle(),
            book.getAuthor(),
            book.getCategory(),
            null
        );
        transactionStack.logTransaction(transaction);
        
        saveToFile();
    }
    
    public List<Book> getAllBooks() {
        return bookShelf.getAllBooks();
    }
    
    public List<Book> getFictionBooks() {
        return bookShelf.getByCategory("Fiction");
    }
    
    public List<Book> getTextBooks() {
        return bookShelf.getByCategory("Textbook");
    }
    
    public List<Book> searchBooks(String title) {
        if (title == null || title.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return bookShelf.searchByTitle(title);
    }
    
    public Book getBookByIsbn(String isbn) {
        if (isbn == null) return null;
        for (Book book : bookShelf.getAllBooks()) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }
    
    // ===== ISSUE/RETURN MANAGEMENT =====
    
    public boolean issueBook(String userId, String isbn) throws IllegalArgumentException {
        validateUser(userId);
        validateIsbn(isbn);
        
        // Create or get user
        User user = users.computeIfAbsent(userId, k -> new User(userId, "User " + userId));
        
        // Enforce MAX_ISSUES_PER_USER limit
        if (user.getIssueCount() >= MAX_ISSUES_PER_USER) {
            throw new IllegalArgumentException(
                String.format("User %s has reached maximum books (%d)", userId, MAX_ISSUES_PER_USER)
            );
        }
        
        // Check if user already has this book
        if (user.hasBook(isbn)) {
            throw new IllegalArgumentException("User already has this book issued");
        }
        
        Book book = getBookByIsbn(isbn);
        if (book == null) {
            throw new IllegalArgumentException("Book not found: " + isbn);
        }
        
        // Issue the book
        user.addIssue(isbn);
        issueQueue.addRequest(isbn);
        
        // Log transaction
        Transaction transaction = new Transaction(
            Transaction.ActionType.ISSUE_BOOK,
            isbn,
            book.getTitle(),
            book.getAuthor(),
            book.getCategory(),
            userId
        );
        transactionStack.logTransaction(transaction);
        
        saveToFile();
        return true;
    }
    
    public boolean returnBook(String userId, String isbn) throws IllegalArgumentException {
        validateUser(userId);
        validateIsbn(isbn);
        
        User user = users.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        
        if (!user.removeIssue(isbn)) {
            throw new IllegalArgumentException("User does not have this book issued");
        }
        
        Book book = getBookByIsbn(isbn);
        if (book == null) {
            throw new IllegalArgumentException("Book not found: " + isbn);
        }
        
        // Log transaction
        Transaction transaction = new Transaction(
            Transaction.ActionType.RETURN_BOOK,
            isbn,
            book.getTitle(),
            book.getAuthor(),
            book.getCategory(),
            userId
        );
        transactionStack.logTransaction(transaction);
        
        saveToFile();
        return true;
    }
    
    public void requestIssue(String isbn) throws IllegalArgumentException {
        validateIsbn(isbn);
        issueQueue.addRequest(isbn);
    }
    
    public String processIssue() {
        String isbn = issueQueue.processNext();
        if (isbn != null) {
            Transaction transaction = new Transaction(
                Transaction.ActionType.ISSUE_BOOK,
                isbn,
                "Book",
                "Unknown",
                "Unknown",
                null
            );
            transactionStack.logTransaction(transaction);
        }
        return isbn;
    }
    
    // ===== UNDO FUNCTIONALITY =====
    
    public boolean undo() throws IllegalArgumentException {
        Transaction lastTransaction = transactionStack.undo();
        if (lastTransaction == null) {
            return false;
        }
        
        switch (lastTransaction.getAction()) {
            case ADD_BOOK:
                // Remove the added book
                bookShelf.removeBook(lastTransaction.getBookIsbn());
                break;
                
            case ISSUE_BOOK:
                // Return the issued book
                if (lastTransaction.getUserId() != null) {
                    User user = users.get(lastTransaction.getUserId());
                    if (user != null) {
                        user.removeIssue(lastTransaction.getBookIsbn());
                    }
                }
                break;
                
            case RETURN_BOOK:
                // Re-issue the returned book
                if (lastTransaction.getUserId() != null) {
                    User user = users.get(lastTransaction.getUserId());
                    if (user != null) {
                        user.addIssue(lastTransaction.getBookIsbn());
                    }
                }
                break;
                
            case REMOVE_BOOK:
                // Re-add the removed book - need to recreate it
                // This would require storing the full book object, not just ISBN
                break;
        }
        
        saveToFile();
        return true;
    }
    
    public String getLastTransaction() {
        Transaction t = transactionStack.peek();
        return t != null ? t.toString() : "No transactions";
    }
    
    // ===== USER MANAGEMENT =====
    
    public User getUser(String userId) {
        return users.get(userId);
    }
    
    public User createUser(String userId, String userName) throws IllegalArgumentException {
        validateUser(userId);
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty");
        }
        if (users.containsKey(userId)) {
            throw new IllegalArgumentException("User already exists: " + userId);
        }
        
        User user = new User(userId, userName);
        users.put(userId, user);
        saveToFile();
        return user;
    }
    
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    
    // ===== QUEUE MANAGEMENT =====
    
    public String getPendingQueueStatus() {
        return issueQueue.isEmpty() ? "No pending requests" : 
            "Next: " + issueQueue.getNext();
    }
    
    public structures.TransactionStack getTransactionStack() {
        return transactionStack;
    }
    
    // ===== PERSISTENCE =====
    
    private void validateIsbn(String isbn) throws IllegalArgumentException {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be empty");
        }
    }
    
    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            // Write books
            writer.println("ISBN,Title,Author,Category");
            for (Book book : bookShelf.getAllBooks()) {
                writer.printf("%s,%s,%s,%s\n", 
                    escapeCsv(book.getIsbn()),
                    escapeCsv(book.getTitle()),
                    escapeCsv(book.getAuthor()),
                    escapeCsv(book.getCategory())
                );
            }
            writer.flush();
        } catch (IOException e) {
            System.err.println("Error saving books to file: " + e.getMessage());
        }
        
        // Save user data
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            writer.println("UserID,Name,IssuedBooks");
            for (User user : users.values()) {
                String issuedBooks = String.join("|", user.getIssuedBooks());
                writer.printf("%s,%s,%s\n",
                    escapeCsv(user.getId()),
                    escapeCsv(user.getName()),
                    escapeCsv(issuedBooks)
                );
            }
            writer.flush();
        } catch (IOException e) {
            System.err.println("Error saving users to file: " + e.getMessage());
        }
    }
    
    public void loadFromFile() {
        // Load books
        File bookFile = new File(DATA_FILE);
        if (bookFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(bookFile))) {
                String line;
                boolean firstLine = true;
                while ((line = reader.readLine()) != null) {
                    if (firstLine) {
                        firstLine = false;
                        continue; // Skip header
                    }
                    
                    String[] parts = parseCSV(line);
                    if (parts.length >= 4) {
                        String isbn = parts[0];
                        String title = parts[1];
                        String author = parts[2];
                        String category = parts[3];
                        
                        Book book;
                        if ("Fiction".equals(category)) {
                            book = new FictionBook(isbn, title, author);
                        } else if ("Textbook".equals(category)) {
                            book = new TextBook(isbn, title, author);
                        } else {
                            continue;
                        }
                        
                        bookShelf.addBook(book);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading books from file: " + e.getMessage());
            }
        }
        
        // Load users
        File userFile = new File(USERS_FILE);
        if (userFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
                String line;
                boolean firstLine = true;
                while ((line = reader.readLine()) != null) {
                    if (firstLine) {
                        firstLine = false;
                        continue; // Skip header
                    }
                    
                    String[] parts = parseCSV(line);
                    if (parts.length >= 3) {
                        String userId = parts[0];
                        String name = parts[1];
                        String issuedBooksStr = parts[2];
                        
                        User user = new User(userId, name);
                        if (!issuedBooksStr.isEmpty()) {
                            String[] isbnList = issuedBooksStr.split("\\|");
                            for (String isbn : isbnList) {
                                user.addIssue(isbn);
                            }
                        }
                        users.put(userId, user);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading users from file: " + e.getMessage());
            }
        }
    }
    
    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
    
    private String[] parseCSV(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());
        
        return result.toArray(new String[0]);
    }
}


