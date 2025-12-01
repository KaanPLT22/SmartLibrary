package repository;


import model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {

    private Connection conn;

    public StudentRepository(Connection conn) {
        this.conn = conn;
    }

    public void addStudent(Student student) {            // ekle
        String sql = "INSERT INTO students (name, department) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getName());
            ps.setString(2, student.getDepartment());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Öğrenci ekleme hatası: " + e.getMessage());
        }
    }

    public void updateStudent(Student student) {
        String sql = "UPDATE students SET name = ?, department = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,student.getName());
            ps.setString(2,student.getDepartment());
            ps.setInt(3,student.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Öğrenci güncelleme hatası: " + e.getMessage());
        }
    }


    public void deleteStudent(int id) {                  // sil
        String sql = "DELETE FROM students WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Öğrenci silme hatası: " + e.getMessage());
        }
    }


    public Student getStudentById(int id) {             // Ögrenci İd sini al
        String sql = "SELECT * FROM students WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);                                // Düz id çünkü veriyi biz veriyoruz (oto)
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department")
                );
            }
        } catch (SQLException e) {
            System.out.println("Öğrenci getirme hatası: " + e.getMessage());
        }
        return null;
    }

    public List<Student> getAllStudents() {             // hepsini getir
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Student s = new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department")
                );
                students.add(s);
            }
        } catch (SQLException e) {
            System.out.println("Öğrencileri listeleme hatası: " + e.getMessage());
        }
        return students;
    }


}
