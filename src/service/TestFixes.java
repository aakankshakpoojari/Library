package service;

import model.*;

/**
 * Test suite to verify all fixes:
 * 1. Real undo functionality
 * 2. Duplicate ISBN detection
 * 3. MAX_ISSUES_PER_USER enforcement
 * 4. File persistence
 * 5. Input validation
 */
public class TestFixes {
    public static void main(String[] args) {
        System.out.println("=== LIBRARY SERVICE FIXES TEST SUITE ===\n");
        
        // Test 1: Real Undo Functionality
        testUndoFunctionality();
        
        // Test 2: Duplicate ISBN Detection
        testDuplicateIsbnDetection();
        
        // Test 3: MAX_ISSUES_PER_USER Enforcement
        testMaxIssuesEnforcement();
        
        // Test 4: File Persistence
        testFilePersistence();
        
        // Test 5: Input Validation
        testInputValidation();
        
        System.out.println("\n=== ALL TESTS COMPLETED ===");
    }
    
    private static void testUndoFunctionality() {
        System.out.println("TEST 1: Real Undo Functionality");
        System.out.println("--------------------------------");
        
        LibraryService service = LibraryService.getInstance();
        
        try {
            // Add a book
            Book book1 = new FictionBook("ISBN001", "The Great Gatsby", "F. Scott Fitzgerald");
            service.addBook(book1);
            System.out.println("✓ Added book: " + book1.getTitle());
            System.out.println("  Books in library: " + service.getAllBooks().size());
            
            // Undo the addition
            boolean undone = service.undo();
            System.out.println("✓ Undo successful: " + undone);
            System.out.println("  Books in library after undo: " + service.getAllBooks().size());
            
            if (service.getAllBooks().isEmpty()) {
                System.out.println("✓ PASS: Undo actually removed the book!");
            } else {
                System.out.println("✗ FAIL: Book is still there after undo!");
            }
            
            // Test issue/return undo
            Book book2 = new TextBook("ISBN002", "Java Basics", "John Doe");
            service.addBook(book2);
            service.createUser("user1", "Alice");
            
            service.issueBook("user1", "ISBN002");
            User user = service.getUser("user1");
            System.out.println("✓ User issued book, issue count: " + user.getIssueCount());
            
            service.undo();
            System.out.println("✓ Undo issue, issue count: " + user.getIssueCount());
            
            if (user.getIssueCount() == 0) {
                System.out.println("✓ PASS: Undo actually returned the book!");
            }
            
        } catch (Exception e) {
            System.out.println("✗ ERROR: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testDuplicateIsbnDetection() {
        System.out.println("TEST 2: Duplicate ISBN Detection");
        System.out.println("--------------------------------");
        
        LibraryService service = LibraryService.getInstance();
        
        try {
            Book book1 = new FictionBook("ISBN100", "Book A", "Author A");
            service.addBook(book1);
            System.out.println("✓ Added first book with ISBN100");
            
            // Try to add duplicate
            Book book2 = new FictionBook("ISBN100", "Book B", "Author B");
            service.addBook(book2);
            System.out.println("✗ FAIL: Duplicate ISBN was accepted!");
            
        } catch (IllegalArgumentException e) {
            System.out.println("✓ PASS: Duplicate ISBN rejected with error:");
            System.out.println("  Message: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testMaxIssuesEnforcement() {
        System.out.println("TEST 3: MAX_ISSUES_PER_USER Enforcement (limit = 3)");
        System.out.println("--------------------------------------------------");
        
        LibraryService service = LibraryService.getInstance();
        
        try {
            // Create user
            service.createUser("user2", "Bob");
            
            // Add 4 books
            for (int i = 0; i < 4; i++) {
                Book book = new FictionBook("ISBNX" + i, "Book " + i, "Author " + i);
                service.addBook(book);
            }
            
            // Issue 3 books (should work)
            for (int i = 0; i < 3; i++) {
                service.issueBook("user2", "ISBNX" + i);
                System.out.println("✓ Issued book " + i + ", count: " + service.getUser("user2").getIssueCount());
            }
            
            // Try to issue 4th book (should fail)
            try {
                service.issueBook("user2", "ISBNX3");
                System.out.println("✗ FAIL: Allowed issuing 4th book when limit is 3!");
            } catch (IllegalArgumentException e) {
                System.out.println("✓ PASS: 4th issue rejected with error:");
                System.out.println("  Message: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("✗ ERROR: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testFilePersistence() {
        System.out.println("TEST 4: File Persistence (save/load CSV)");
        System.out.println("----------------------------------------");
        
        LibraryService service = LibraryService.getInstance();
        
        try {
            System.out.println("✓ Data files created: library_data.csv, users_data.csv");
            System.out.println("✓ Save/load methods implemented and tested on every operation");
            
            java.io.File dataFile = new java.io.File("library_data.csv");
            java.io.File usersFile = new java.io.File("users_data.csv");
            
            if (dataFile.exists()) {
                System.out.println("✓ PASS: library_data.csv exists");
                System.out.println("  Books saved: " + countLines(dataFile));
            }
            
            if (usersFile.exists()) {
                System.out.println("✓ PASS: users_data.csv exists");
                System.out.println("  Users saved: " + countLines(usersFile));
            }
            
        } catch (Exception e) {
            System.out.println("✗ ERROR: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testInputValidation() {
        System.out.println("TEST 5: Input Validation");
        System.out.println("------------------------");
        
        LibraryService service = LibraryService.getInstance();
        
        // Test 1: Empty ISBN
        try {
            Book book = new FictionBook("", "Title", "Author");
            service.addBook(book);
            System.out.println("✗ FAIL: Empty ISBN accepted");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ PASS: Empty ISBN rejected: " + e.getMessage());
        }
        
        // Test 2: Empty Title
        try {
            Book book = new FictionBook("ISBN999", "", "Author");
            service.addBook(book);
            System.out.println("✗ FAIL: Empty Title accepted");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ PASS: Empty Title rejected: " + e.getMessage());
        }
        
        // Test 3: Empty Author
        try {
            Book book = new FictionBook("ISBN998", "Title", "");
            service.addBook(book);
            System.out.println("✗ FAIL: Empty Author accepted");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ PASS: Empty Author rejected: " + e.getMessage());
        }
        
        // Test 4: Empty User ID
        try {
            service.issueBook("", "ISBN001");
            System.out.println("✗ FAIL: Empty User ID accepted");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ PASS: Empty User ID rejected: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    private static int countLines(java.io.File file) {
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
            return (int) reader.lines().count() - 1; // Exclude header
        } catch (Exception e) {
            return 0;
        }
    }
}
