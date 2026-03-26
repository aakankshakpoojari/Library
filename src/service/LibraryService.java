package service;

import dao.BookDAO;
import dao.UserDAO;
import db.DBConnection;
import model.Book;
import model.User;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class LibraryService {
    private static LibraryService instance;
    private BookDAO bookDAO = new BookDAO();
    private UserDAO userDAO = new UserDAO();

    private LibraryService() {}

    public static LibraryService getInstance() {
        if (instance == null) {
            instance = new LibraryService();
        }
        return instance;
    }

    // ─── BOOK OPERATIONS ───────────────────────────────────────

    public void addBook(Book book) {
        try {
            if (bookDAO.exists(book.getIsbn())) {
                System.out.println("Book with this ISBN already exists!");
                return;
            }
            bookDAO.addBook(book);
            System.out.println("Book added: " + book.getTitle());
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void removeBook(String isbn) {
        try {
            if (!bookDAO.exists(isbn)) {
                System.out.println("Book not found!");
                return;
            }
            bookDAO.removeBook(isbn);
            System.out.println("Book removed successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listAllBooks() {
        try {
            List<Book> books = bookDAO.getAllBooks();
            if (books.isEmpty()) {
                System.out.println("No books in library.");
                return;
            }
            books.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void searchBooks(String title) {
        try {
            List<Book> books = bookDAO.searchByTitle(title);
            if (books.isEmpty()) {
                System.out.println("No books found.");
                return;
            }
            books.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ─── USER OPERATIONS ───────────────────────────────────────

    public void addUser(User user) {
        try {
            if (userDAO.exists(user.getId())) {
                System.out.println("User with this ID already exists!");
                return;
            }
            userDAO.addUser(user);
            System.out.println("User added: " + user.getName());
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void removeUser(String id) {
        try {
            if (!userDAO.exists(id)) {
                System.out.println("User not found!");
                return;
            }
            userDAO.removeUser(id);
            System.out.println("User removed successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listAllUsers() {
        try {
            List<User> users = userDAO.getAllUsers();
            if (users.isEmpty()) {
                System.out.println("No users registered.");
                return;
            }
            users.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ─── ISSUE OPERATIONS ──────────────────────────────────────

    public void issueBook(String userId, String isbn) {
        try {
            if (!userDAO.exists(userId)) {
                System.out.println("User not found!");
                return;
            }
            if (!bookDAO.exists(isbn)) {
                System.out.println("Book not found!");
                return;
            }
            String sql = "INSERT INTO issued_books (user_id, isbn) VALUES (?, ?)";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, userId);
            ps.setString(2, isbn);
            ps.executeUpdate();
            System.out.println("Book issued successfully!");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void returnBook(String userId, String isbn) {
        try {
            String sql = "UPDATE issued_books SET return_date = GETDATE() " +
                         "WHERE user_id = ? AND isbn = ? AND return_date IS NULL";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, userId);
            ps.setString(2, isbn);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Book returned successfully!");
            } else {
                System.out.println("No active issue found for this user and book.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listIssuedBooks(String userId) {
        try {
            String sql = "SELECT b.isbn, b.title, b.author, i.issue_date " +
                         "FROM issued_books i JOIN books b ON i.isbn = b.isbn " +
                         "WHERE i.user_id = ? AND i.return_date IS NULL";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(rs.getString("title") + " by " + rs.getString("author") +
                                   " | Issued on: " + rs.getDate("issue_date"));
            }
            if (!found) System.out.println("No books currently issued to this user.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void shutdown() {
        DBConnection.closeConnection();
    }
}