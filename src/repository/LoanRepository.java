package repository;

import model.Loan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class LoanRepository {

    private Connection conn;

    public LoanRepository(Connection conn) {
        this.conn = conn;
    }


    public void addLoan(Loan loan) {                       // Ekle
        String sql = "INSERT INTO loans (bookId, studentId, dateBorrowed) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, loan.getBookId());
            ps.setInt(2, loan.getStudentId());
            ps.setString(3, loan.getDateBorrowed());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ödünç ekleme hatası: " + e.getMessage());
        }
    }

    public void updateLoan(Loan loan) {                      // Güncelle
        String sql = "UPDATE loans SET bookId=?, studentId=?, dateBorrowed=?, dateReturned=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, loan.getBookId());
            ps.setInt(2, loan.getStudentId());
            ps.setString(3, loan.getDateBorrowed());
            ps.setString(4, loan.getDateReturned());
            ps.setInt(5, loan.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ödünç güncelleme hatası: " + e.getMessage());
        }
    }

    public void deleteLoan(int id) {                         // Sil
        String sql = "DELETE FROM loans WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ödünç silme hatası: " + e.getMessage());
        }
    }

    public Loan getLoanById(int id) {                        // Id Al
        String sql = "SELECT * FROM loans WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Loan(
                        rs.getInt("id"),
                        rs.getInt("bookId"),
                        rs.getInt("studentId"),
                        rs.getString("dateBorrowed"),
                        rs.getString("dateReturned")
                );
            }
        } catch (SQLException e) {
            System.out.println("Ödünç getirme hatası: " + e.getMessage());
        }
        return null;
    }

    public List<Loan> getAllLoans() {                        // Hepsni getir
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans";
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Loan l = new Loan(
                        rs.getInt("id"),
                        rs.getInt("bookId"),
                        rs.getInt("studentId"),
                        rs.getString("dateBorrowed"),
                        rs.getString("dateReturned")
                );
                loans.add(l);
            }
        } catch (SQLException e) {
            System.out.println("Ödünçleri listeleme hatası: " + e.getMessage());
        }
        return loans;
    }

    // bir kitabın ödünçte olup olmadığını kontrol et
    public boolean isBookBorrowed(int bookId) {
        String sql = "SELECT * FROM loans WHERE bookId=? AND dateReturned IS NULL";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // kayıt varsa kitap ödünçte
        } catch (SQLException e) {
            System.out.println("Kitap ödünç kontrol hatası: " + e.getMessage());
            return false;
        }
    }

}
