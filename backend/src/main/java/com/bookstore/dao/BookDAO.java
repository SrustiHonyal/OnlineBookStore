package com.bookstore.dao;

import com.bookstore.model.Book;
import com.bookstore.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    // Get all books
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.*, c.name AS category_name FROM books b " +
                     "LEFT JOIN categories c ON b.category_id = c.id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(mapBook(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return books;
    }

    // Get book by ID
    public Book getBookById(int id) {
        String sql = "SELECT b.*, c.name AS category_name FROM books b " +
                     "LEFT JOIN categories c ON b.category_id = c.id WHERE b.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapBook(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // Search books
    public List<Book> searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.*, c.name AS category_name FROM books b " +
                     "LEFT JOIN categories c ON b.category_id = c.id " +
                     "WHERE b.title LIKE ? OR b.author LIKE ? OR b.description LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw); ps.setString(2, kw); ps.setString(3, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) books.add(mapBook(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return books;
    }

    // Get books by category
    public List<Book> getBooksByCategory(int categoryId) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.*, c.name AS category_name FROM books b " +
                     "LEFT JOIN categories c ON b.category_id = c.id WHERE b.category_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) books.add(mapBook(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return books;
    }

    // Add new book (Admin)
    public boolean addBook(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, category_id, price, stock, description, image_url, publisher, publish_year) " +
                     "VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setInt(4, book.getCategoryId());
            ps.setDouble(5, book.getPrice());
            ps.setInt(6, book.getStock());
            ps.setString(7, book.getDescription());
            ps.setString(8, book.getImageUrl());
            ps.setString(9, book.getPublisher());
            ps.setInt(10, book.getPublishYear());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // Update book (Admin)
    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET title=?, author=?, isbn=?, category_id=?, price=?, stock=?, " +
                     "description=?, image_url=?, publisher=?, publish_year=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setInt(4, book.getCategoryId());
            ps.setDouble(5, book.getPrice());
            ps.setInt(6, book.getStock());
            ps.setString(7, book.getDescription());
            ps.setString(8, book.getImageUrl());
            ps.setString(9, book.getPublisher());
            ps.setInt(10, book.getPublishYear());
            ps.setInt(11, book.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // Delete book (Admin)
    public boolean deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private Book mapBook(ResultSet rs) throws SQLException {
        Book b = new Book();
        b.setId(rs.getInt("id"));
        b.setTitle(rs.getString("title"));
        b.setAuthor(rs.getString("author"));
        b.setIsbn(rs.getString("isbn"));
        b.setCategoryId(rs.getInt("category_id"));
        b.setCategoryName(rs.getString("category_name"));
        b.setPrice(rs.getDouble("price"));
        b.setStock(rs.getInt("stock"));
        b.setDescription(rs.getString("description"));
        b.setImageUrl(rs.getString("image_url"));
        b.setPublisher(rs.getString("publisher"));
        b.setPublishYear(rs.getInt("publish_year"));
        return b;
    }
}
