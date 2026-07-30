package com.library.service;

import com.library.config.DatabaseConnection;
import com.library.exception.BookNotFoundException;
import com.library.exception.BookUnavailableException;
import com.library.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryService {
    public List<Book> getAllBooks() {

        List<Book> books = new ArrayList<>();
    
        String query = "SELECT * FROM books";
    
        try (
                Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)
        ) {
    
            while (rs.next()) {
    
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getBoolean("is_available")
                ));
    
            }
    
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    
        return books;
    }
    public void addNewBook(String title, String author) {

        String query = "INSERT INTO books(title, author, is_available) VALUES (?, ?, TRUE)";
    
        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)
        ) {
    
            pstmt.setString(1, title);
            pstmt.setString(2, author);
    
            int rows = pstmt.executeUpdate();
    
            if (rows > 0) {
                System.out.println("Book added successfully.");
            }
    
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }   
    
    
    public void borrowBook(int id, String borrowerName, String studentId,
        String phoneNumber, java.sql.Date borrowDate,
        java.sql.Date returnDate)
    throws BookNotFoundException, BookUnavailableException {

String query = "UPDATE books SET is_available = FALSE, "
+ "borrower_name = ?, student_id = ?, phone_number = ?, "
+ "borrow_date = ?, return_date = ? "
+ "WHERE id = ? AND is_available = TRUE";

try (
     Connection conn = DatabaseConnection.getConnection();
     PreparedStatement pstmt = conn.prepareStatement(query)
) {

    pstmt.setString(1, borrowerName);
    pstmt.setString(2, studentId);
    pstmt.setString(3, phoneNumber);
    pstmt.setDate(4, borrowDate);
    pstmt.setDate(5, returnDate);
    pstmt.setInt(6, id);

    int rows = pstmt.executeUpdate();

if (rows == 0) {
    throw new BookUnavailableException("Book is unavailable or not found.");
}

System.out.println("Book borrowed successfully.");

} catch (SQLException e) {
    System.out.println(e.getMessage());
  }
}
public void returnBook(int id) throws BookNotFoundException {

    String query = "UPDATE books SET "
            + "is_available = TRUE, "
            + "borrower_name = NULL, "
            + "student_id = NULL, "
            + "phone_number = NULL, "
            + "borrow_date = NULL, "
            + "return_date = NULL "
            + "WHERE id = ?";

    try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)
    ) {

        pstmt.setInt(1, id);

        int rows = pstmt.executeUpdate();

        if (rows == 0) {
            throw new BookNotFoundException("Book not found.");
        }

        System.out.println("Book returned successfully.");

    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
}
    public void deleteBook(int id) throws BookNotFoundException {

        String sql = "DELETE FROM books WHERE id = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setInt(1, id);
    
            int rows = stmt.executeUpdate();
    
            if (rows == 0) {
                throw new BookNotFoundException("Book not found with ID: " + id);
            }
    
            System.out.println("Book deleted successfully.");
    
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting book: " + e.getMessage());
        }
    }
    public List<Book> getBorrowedBooks() {

        List<Book> books = new ArrayList<>();
    
        String query = "SELECT * FROM books WHERE is_available = FALSE";
    
        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()
        ) {
    
            while (rs.next()) {
    
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getBoolean("is_available")
                );
    
                books.add(book);
            }
    
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    
        return books;
    }
    public Book searchBookById(int id) throws BookNotFoundException {

        String query = "SELECT * FROM books WHERE id = ?";
    
        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)
        ) {
    
            pstmt.setInt(1, id);
    
            ResultSet rs = pstmt.executeQuery();
    
            if (rs.next()) {
    
                return new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getBoolean("is_available")
                );
            }
    
            throw new BookNotFoundException("Book not found.");
    
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public void showDashboard() {
        try (Connection conn = DatabaseConnection.getConnection()) {
    
            int total = getCount(conn, "SELECT COUNT(*) FROM books");
            int available = getCount(conn, "SELECT COUNT(*) FROM books WHERE is_available = TRUE");
            int borrowed = getCount(conn, "SELECT COUNT(*) FROM books WHERE is_available = FALSE");
    
            System.out.println("\n======================================");
            System.out.println("         LIBRARY DASHBOARD");
            System.out.println("======================================");
            System.out.println("Total Books      : " + total);
            System.out.println("Available Books  : " + available);
            System.out.println("Borrowed Books   : " + borrowed);
            System.out.println("======================================");
    
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private int getCount(Connection conn, String query) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
    
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }
    public List<Book> getAvailableBooks() {

        List<Book> books = new ArrayList<>();
    
        String query = "SELECT * FROM books WHERE is_available = TRUE";
    
        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()
        ) {
    
            while (rs.next()) {
    
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getBoolean("is_available")
                );
    
                books.add(book);
            }
    
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    
        return books;
    }
    public void updateBook(int id, String title, String author)
        throws BookNotFoundException {

    String query = "UPDATE books SET title = ?, author = ? WHERE id = ?";

    try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)
    ) {

        pstmt.setString(1, title);
        pstmt.setString(2, author);
        pstmt.setInt(3, id);

        int rows = pstmt.executeUpdate();

        if (rows == 0) {
            throw new BookNotFoundException("Book not found.");
        }

        System.out.println("Book updated successfully.");

    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
}
}

