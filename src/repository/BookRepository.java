package repository;

import model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {

    private Connection conn;

    public BookRepository(Connection conn) {
        this.conn = conn;
    }

    public void addBook(Book book) {          // Kitap Ekleme Kısmı
        String sql = "INSERT INTO books (title, author, year) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setInt(3, book.getYear());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Kitap ekleme hatası: " + e.getMessage());
        }
    }

    public void updateBook(Book book) {       // Güncelleme Kısmı
        String sql = "UPDATE books SET title=?, author=?, year=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setInt(3, book.getYear());
            ps.setInt(4, book.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Kitap güncelleme hatası: " + e.getMessage());
        }
    }

    public void deleteBook(int id) {          // Slme Kısmı
        String sql = "DELETE FROM books WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Kitap silme hatası: " + e.getMessage());
        }
    }

    public Book getBookById(int id) {         // Id yi alma bölümü
        String sql = "SELECT * FROM books WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("year")
                );
            }
        } catch (SQLException e) {
            System.out.println("Kitap getirme hatası: " + e.getMessage());
        }
        return null;
    }

    public List<Book> getAllBooks() {         // bütün kitapları aldığımız yer
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("year")
                );
                books.add(book);
            }
        } catch (SQLException e) {
            System.out.println("Kitapları listeleme hatası: " + e.getMessage());
        }
        return books;
    }
}