package dao;

import db.DBConnection;
import model.Book;
import model.FictionBook;
import model.TextBook;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public void addBook(Book book) throws SQLException {
        String sql = "INSERT INTO books (isbn, title, author, category) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setString(1, book.getIsbn());
        ps.setString(2, book.getTitle());
        ps.setString(3, book.getAuthor());
        ps.setString(4, book.getCategory());
        ps.executeUpdate();
    }

    public void removeBook(String isbn) throws SQLException {
        String sql = "DELETE FROM books WHERE isbn = ?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setString(1, isbn);
        ps.executeUpdate();
    }

    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        Statement st = DBConnection.getConnection().createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            books.add(mapBook(rs));
        }
        return books;
    }

    public List<Book> searchByTitle(String title) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setString(1, "%" + title + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            books.add(mapBook(rs));
        }
        return books;
    }

    public boolean exists(String isbn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM books WHERE isbn = ?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setString(1, isbn);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    private Book mapBook(ResultSet rs) throws SQLException {
        String isbn = rs.getString("isbn");
        String title = rs.getString("title");
        String author = rs.getString("author");
        String category = rs.getString("category");
        if (category.equals("Fiction")) {
            return new FictionBook(isbn, title, author);
        } else {
            return new TextBook(isbn, title, author);
        }
    }
}