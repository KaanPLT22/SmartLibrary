package database;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class Database {

    private static final String DB_URL = "jdbc:sqlite:smartlibrary.db";



    // Sql Bağlantısı
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("SQLite bağlantısı başarılı!");
        } catch (SQLException e) {
            System.out.println("Bağlantı hatası: " + e.getMessage());
        }
        return conn;
    }


    // 2. Tabloları oluşturduğum kısım

    public static void createTables() {

        String createBooksTable = "CREATE TABLE IF NOT EXISTS books (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "author TEXT," +
                "year INTEGER" +
                ")";

        String createStudentsTable = "CREATE TABLE IF NOT EXISTS students (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "department TEXT" +
                ")";

        String createLoansTable = "CREATE TABLE IF NOT EXISTS loans (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "bookId INTEGER," +
                "studentId INTEGER," +
                "dateBorrowed TEXT," +
                "dateReturned TEXT" +
                ")";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createBooksTable);
            stmt.execute(createStudentsTable);
            stmt.execute(createLoansTable);

            System.out.println("Tablolar oluşturuldu!");

        } catch (SQLException e) {
            System.out.println("Tablo oluşturulurken hata: " + e.getMessage());
        }
    }


    // Bağlantıyı kapat
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.out.println("Bağlantı kapatma hatası: " + e.getMessage());
        }
    }
}