import database.Database;

import model.Book;
import model.Student;
import model.Loan;

import repository.BookRepository;
import repository.StudentRepository;
import repository.LoanRepository;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;


public class Main {


    private static final Scanner sc = new Scanner(System.in);
    private static BookRepository bookRepo;
    private static StudentRepository studentRepo;
    private static LoanRepository loanRepo;
    private static Connection conn; // Bağlantı nesnesini de static yapıyoruz

    public static void main(String[] args) {


        Database db = new Database();
        conn = db.getConnection();
        Database.createTables();

        bookRepo = new BookRepository(conn);
        studentRepo = new StudentRepository(conn);
        loanRepo = new LoanRepository(conn);

        int choice = -1;

        while (choice != 0) {

            displayMenu(); // Menüyü göster

            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                sc.nextLine();
            } else {
                System.out.println("Hata: Geçersiz seçim. Lütfen bir numara girin.");
                sc.nextLine();
                choice = -1;
                continue;
            }

            // Fonksiyon çağrıları ile yönlendirme
            switch (choice) {
                case 1: add_ktp(); break;
                case 2: islem_ktp(); break;
                case 3: list_ktp(); break;
                case 4: add_ogr(); break;
                case 5: islem_ogr(); break;
                case 6: list_ogr(); break;
                case 7: loan_ktp(); break;
                case 8: list_loan(); break;
                case 9: return_ktp(); break;
                case 0: System.out.println("Programdan çıkılıyor..."); break;
                default: System.out.println("Geçersiz seçim!");
            }
        }

        // Bağlantıyı kapat
        Database.closeConnection(conn);
        sc.close();
    }


    private static void displayMenu() {
        System.out.println("\n--- SmartLibrary Menü ---");
        System.out.println("1. Kitap Ekle");
        System.out.println("2. Kitaplar Güncelleme Ve Sil İşlemleri");
        System.out.println("3. Kitapları Listele");
        System.out.println("4. Öğrenci Ekle");
        System.out.println("5. Öğrenci Güncelle/Sil İşlemleri");
        System.out.println("6. Öğrencileri Listele");
        System.out.println("7. Kitap Ödünç Ver");
        System.out.println("8. Ödünç Alım Listesini Görüntüle");
        System.out.println("9. Kitapları Geri Teslim Al");
        System.out.print("0. Çıkış\nSeçiminiz: ");
    }

    //  KİTAP İŞLEMLERİ

    // 1. Kitap Ekleme
    private static void add_ktp() {
        System.out.print("Kitap Adı: ");
        String title = sc.nextLine();
        System.out.print("Yazar: ");
        String author = sc.nextLine();
        System.out.print("Yıl: ");
        if (sc.hasNextInt()) {
            int year = sc.nextInt();
            sc.nextLine();
            bookRepo.addBook(new Book(title, author, year));
            System.out.println("✅ Kitap eklendi!");
        } else {
            System.out.println("Hata: Yıl için sayısal bir değer girilmelidir.");
            sc.nextLine();
        }
    }

    // 3. Kitapları Listele
    private static void list_ktp() {
        List<Book> books = bookRepo.getAllBooks();
        System.out.println("\n--- Kitap Listesi ---");
        if (books.isEmpty()) {
            System.out.println("Kayıtlı kitap bulunmamaktadır.");
            return;
        }
        for (Book b : books) {
            System.out.println(b.getId() + ". " + b.getTitle() + " - " + b.getAuthor() + " (" + b.getYear() + ")");
        }
    }

    // 2. Kitap Güncelleme ve Silme Ana İşlemi
    private static void islem_ktp() {
        System.out.println("\n--- Kitap İşlemleri  ---");
        System.out.println("1. Kitap Güncelle");
        System.out.println("2. Kitap Sil");
        System.out.print("Seçim: ");
        int bookAction = -1;

        if (sc.hasNextInt()) {
            bookAction = sc.nextInt();
            sc.nextLine();
        } else {
            System.out.println("Hata: Geçersiz seçim. Lütfen bir numara girin.");
            sc.nextLine();
            return;
        }

        // Güncelleme ve Silme öncesi listeyi gösterme
        if (bookAction == 1 || bookAction == 2) {
            List<Book> allBooks = bookRepo.getAllBooks();
            System.out.println("\n--- İşlem Yapılacak Mevcut Kitap Listesi ---");
            if (allBooks.isEmpty()) {
                System.out.println("Sistemde kayıtlı kitap bulunmamaktadır.");
                return;
            }
            for (Book b : allBooks) {
                System.out.println("ID: " + b.getId() + " | Adı: " + b.getTitle() + " | Yazar: " + b.getAuthor() + " | Yıl: " + b.getYear());
            }
            System.out.println("-------------------------------------------");
        }

        if (bookAction == 1) {
            update_ktp(); //
        } else if (bookAction == 2) {
            delete_ktp(); //
        } else {
            System.out.println("Geçersiz işlem seçimi. Ana menüye dönülüyor.");
        }
    }

    // --- Kitap Güncelleme alt fonksiyonu (update_ktp) ---
    private static void update_ktp() {
        System.out.print("Güncellenecek Kitabın ID'sini girin: ");
        if (!sc.hasNextInt()) {
            System.out.println("Hata: ID için sayısal değer girilmelidir.");
            sc.nextLine();
            return;
        }
        int id = sc.nextInt();
        sc.nextLine();

        Book existingBook = bookRepo.getBookById(id);
        if (existingBook == null) {
            System.out.println("Hata: Bu ID'de kitap bulunamadı.");
            return;
        }

        System.out.println("--- Kitap Güncelleme ---");
        System.out.println("Mevcut Ad: " + existingBook.getTitle());
        System.out.print("Yeni Ad: (Değiştirmek istemiyorsanız Enter'a basın): ");
        String newTitle = sc.nextLine();
        if (!newTitle.trim().isEmpty()) existingBook.setTitle(newTitle);

        System.out.println("Mevcut Yazar: " + existingBook.getAuthor());
        System.out.print("Yeni Yazar (Değiştirmek istemiyorsanız Enter'a basın): ");
        String newAuthor = sc.nextLine();
        if (!newAuthor.trim().isEmpty()) existingBook.setAuthor(newAuthor);

        System.out.println("Mevcut Yıl: " + existingBook.getYear());
        System.out.print("Yeni Yıl (" + existingBook.getYear() + " olarak kalması için '0' veya aynı yılı girin, aksi halde yeni yılı girin): ");

        if (sc.hasNextInt()) {
            int newYear = sc.nextInt();
            if (newYear != 0 && newYear != existingBook.getYear()) {
                existingBook.setYear(newYear);
            }
        }
        sc.nextLine();

        bookRepo.updateBook(existingBook);
        System.out.println("✅ Kitap başarıyla güncellendi!");
    }

    // --- Kitap Silme ---
    private static void delete_ktp() {
        System.out.print("Silinecek Kitabın ID'sini girin: ");
        if (!sc.hasNextInt()) {
            System.out.println("Hata: ID için sayısal değer girilmelidir.");
            sc.nextLine();
            return;
        }
        int idToDelete = sc.nextInt();
        sc.nextLine();

        if (loanRepo.isBookBorrowed(idToDelete)) {
            System.out.println("❌ HATA: Bu kitap şu anda ödünçte olduğu için silinemez. Önce iade edilmesi gerekmektedir.");
        }
        else if (bookRepo.getBookById(idToDelete) != null) {
            bookRepo.deleteBook(idToDelete);
            System.out.println("✅ " + idToDelete + " ID'li kitap başarıyla silinmiştir.");
        }
        else {
            System.out.println("Hata: Belirtilen ID'ye sahip kitap bulunamadı.");
        }
    }

    // ===========================================
    // === ÖĞRENCİ İŞLEMLERİ (4, 5, 6) ===
    // ===========================================

    // 4. Öğrenci Eklemee
    private static void add_ogr() {
        System.out.print("Öğrenci Adı: ");
        String name = sc.nextLine();
        System.out.print("Bölüm: ");
        String department = sc.nextLine();
        studentRepo.addStudent(new Student(name, department));
        System.out.println("✅ Öğrenci eklendi!");
    }

    // 6. Öğrencileri Listele
    private static void list_ogr() {
        List<Student> students = studentRepo.getAllStudents();
        System.out.println("\n--- Öğrenci Listesi ---");
        if (students.isEmpty()) {
            System.out.println("Kayıtlı öğrenci bulunmamaktadır.");
            return;
        }
        for (Student s : students) {
            System.out.println(s.getId() + ". " + s.getName() + " - " + s.getDepartment());
        }
    }

    // 5. Öğrenci Güncelleme ve Sil İşlemlerinin yapılıdıgı yer
    private static void islem_ogr() {
        List<Student> allStudentsAction = studentRepo.getAllStudents();
        System.out.println("\n--- Mevcut Öğrenci Listesi ---");
        if (allStudentsAction.isEmpty()) {
            System.out.println("Sistemde kayıtlı öğrenci bulunmamaktadır.");
            return;
        }
        for (Student s : allStudentsAction) {
            System.out.println("ID: " + s.getId() + " | İsim: " + s.getName() + " | Bölüm: " + s.getDepartment());
        }
        System.out.println("----------------------------");

        System.out.println("\n--- Öğrenci İşlemleri  ---");
        System.out.println("1. Öğrenci Güncelle");
        System.out.println("2. Öğrenci Sil");
        System.out.print("Seçim: ");
        int studentAction = -1;

        if (sc.hasNextInt()) {
            studentAction = sc.nextInt();
            sc.nextLine();
        } else {
            System.out.println("Hata: Geçersiz seçim. Lütfen bir numara girin.");
            sc.nextLine();
            return;
        }

        if (studentAction == 1) {
            update_ogr();
        } else if (studentAction == 2) {
            delete_ogr();
        } else {
            System.out.println("Geçersiz işlem seçimi. Ana menüye dönülüyor.");
        }
    }

    // --- Öğrenci Güncelleme   ---
    private static void update_ogr() {
        System.out.print("Güncellenecek Öğrenci ID: ");
        if (!sc.hasNextInt()) {
            System.out.println("Hata: ID için sayısal değer girilmelidir.");
            sc.nextLine();
            return;
        }
        int id = sc.nextInt();
        sc.nextLine();

        Student existingStudent = studentRepo.getStudentById(id);
        if (existingStudent == null) {
            System.out.println("Hata: Bu ID'de öğrenci bulunamadı.");
            return;
        }

        // Güncelleme
        System.out.println("Mevcut İsim: " + existingStudent.getName());
        System.out.print("Yeni İsim (Değiştirmek istemiyorsanız Enter'a basın): ");
        String newName = sc.nextLine();
        if (!newName.trim().isEmpty()) existingStudent.setName(newName);

        System.out.println("Mevcut Bölüm: " + existingStudent.getDepartment());
        System.out.print("Yeni Bölüm (Değiştirmek istemiyorsanız Enter'a basın): ");
        String newDepartment = sc.nextLine();
        if (!newDepartment.trim().isEmpty()) existingStudent.setDepartment(newDepartment);

        studentRepo.updateStudent(existingStudent);
        System.out.println("✅ Öğrenci başarıyla güncellendi!");
    }

    // --- Öğrenci Silme  ---
    private static void delete_ogr() {
        System.out.print("Silinecek Öğrenci ID: ");
        if (!sc.hasNextInt()) {
            System.out.println("Hata: ID için sayısal değer girilmelidir.");
            sc.nextLine();
            return;
        }
        int idToDelete = sc.nextInt();
        sc.nextLine();

        boolean hasActiveLoans = false;
        List<Loan> allLoans = loanRepo.getAllLoans();

        // Ödünç Vermesi Gereken Bir Kitap varmı Kontrolu
        for (Loan l : allLoans) {
            if (l.getStudentId() == idToDelete && l.getDateReturned() == null) {
                hasActiveLoans = true;
                break;
            }
        }

        if (hasActiveLoans) {
            System.out.println("❌ HATA: Bu öğrencinin iade etmediği kitaplar olduğu için silinemez.");
        }
        else if (studentRepo.getStudentById(idToDelete) != null) {
            studentRepo.deleteStudent(idToDelete);
            System.out.println("✅ " + idToDelete + " ID'li öğrenci başarıyla silinmiştir.");
        }
        else {
            System.out.println("Hata: Belirtilen ID'ye sahip öğrenci bulunamadı.");
        }
    }

    // === ÖDÜNÇ İŞLEMLERİ ===


    //Kitap Ödünç Ver
    private static void loan_ktp() {
        // Öğrenci listeleme
        List<Student> allStudentsForLoan = studentRepo.getAllStudents();
        System.out.println("\n--- Öğrenciler ---");
        if (allStudentsForLoan.isEmpty()) {
            System.out.println("Ödünç vermek için kayıtlı öğrenci yok.");
            return;
        }
        for (Student s : allStudentsForLoan) {
            System.out.println(s.getId() + ". " + s.getName() + " - " + s.getDepartment());
        }

        System.out.print("Ödünç vereceğiniz Öğrencinin ID'si: ");
        if (!sc.hasNextInt()) {
            System.out.println("Hata: Öğrenci ID için sayısal değer girilmelidir.");
            sc.nextLine();
            return;
        }
        int studentId = sc.nextInt();

        // Kitapları göster
        List<Book> allBooksForLoan = bookRepo.getAllBooks();
        System.out.println("\n--- Ödünç Verilebilecek Kitaplar ---");
        boolean hasAvailableBook = false;
        for (Book b : allBooksForLoan) {
            if (!loanRepo.isBookBorrowed(b.getId())) {
                System.out.println(b.getId() + ". " + b.getTitle() + " - " + b.getAuthor());
                hasAvailableBook = true;
            }
        }
        if (!hasAvailableBook) {
            System.out.println("Şu anda ödünç verilebilecek boş kitap bulunmamaktadır.");
            sc.nextLine();
            return;
        }

        System.out.print("Ödünç verilecek Kitabın ID'si: ");
        if (!sc.hasNextInt()) {
            System.out.println("Hata: Kitap ID için sayısal değer girilmelidir.");
            sc.nextLine();
            return;
        }
        int bookId = sc.nextInt();
        sc.nextLine();

        if (studentRepo.getStudentById(studentId) == null || bookRepo.getBookById(bookId) == null) {
            System.out.println("Hata: Geçersiz Öğrenci veya Kitap ID'si.");
        } else if (loanRepo.isBookBorrowed(bookId)) {
            System.out.println("❌ HATA: Bu kitap zaten ödünçte!");
        } else {
            System.out.print("Ödünç Tarihi (GG-AA-YYYY): ");
            String dateBorrowed = sc.nextLine();
            loanRepo.addLoan(new Loan(bookId, studentId, dateBorrowed));
            System.out.println("✅ Kitap ödünç verildi!");
        }
    }

    // Ödünç Alım Listesini Görüntüle
    private static void list_loan() {
        List<Loan> loans = loanRepo.getAllLoans();
        System.out.println("\n--- Ödünç Listesi ---");
        if (loans.isEmpty()) {
            System.out.println("Kayıtlı ödünç işlemi bulunmamaktadır.");
            return;
        }
        for (Loan l : loans) {

            Book book = bookRepo.getBookById(l.getBookId());
            Student student = studentRepo.getStudentById(l.getStudentId());

            String bookName = (book != null) ? book.getTitle() : "⚠️ Silinmiş Kitap (" + l.getBookId() + ")";
            String studentName = (student != null) ? student.getName() : "⚠️ Silinmiş Öğrenci (" + l.getStudentId() + ")";

            System.out.println(
                    l.getId() + ". [ KitapID: " + l.getBookId() +
                            " | Kitap Adı: " + bookName +
                            " ]  [ ÖğrenciID: " + l.getStudentId() +
                            " | Öğrenci Adı: " + studentName +
                            " ]  Tarih: " + l.getDateBorrowed() +
                            "  İade: " + (l.getDateReturned() == null ? "Henüz iade edilmedi" : l.getDateReturned())
            );
        }
    }

    //Kitapları Geri Teslim Al
    private static void return_ktp() {
        List<Loan> allLoans = loanRepo.getAllLoans();
        System.out.println("\n--- Geri Alınabilecek Kitapların Listesi (Aktif Ödünçler) ---");
        boolean hasLoanToReturn = false;

        for (Loan l : allLoans) {
            if (l.getDateReturned() == null) {

                Book b = bookRepo.getBookById(l.getBookId());
                Student s = studentRepo.getStudentById(l.getStudentId());

                if (b != null && s != null) {
                    System.out.println(l.getId() + ". " + b.getTitle() + " - " + s.getName() + " (" + s.getId() + ")");
                    hasLoanToReturn = true;
                }
            }
        }

        if (!hasLoanToReturn) {
            System.out.println("Şu anda geri alınacak kitap bulunmamaktadır.");
            return;
        }

        System.out.print("İade edilecek Ödünç ID: ");
        if (!sc.hasNextInt()) {
            System.out.println("Hata: Ödünç ID için sayısal değer girilmelidir.");
            sc.nextLine();
            return;
        }
        int loanId = sc.nextInt();
        sc.nextLine();

        Loan loan = loanRepo.getLoanById(loanId);
        if (loan != null && loan.getDateReturned() == null) {
            System.out.print("İade Tarihi (GG-AA-YYYY): ");
            String dateReturned = sc.nextLine();
            loan.setDateReturned(dateReturned);
            loanRepo.updateLoan(loan);
            System.out.println("✅ Kitap iade alındı!");
        } else {
            System.out.println("Geçersiz ödünç ID veya kitap zaten iade edilmiş.");
        }
    }
}