# SmartLibrary

## Proje Hakkında

SmartLibrary, Java dili kullanılarak geliştirilmiş, konsol tabanlı bir kütüphane yönetim sistemidir. Projenin temel amacı, kitap ekleme ,silme , düzenleme ve listeleme, öğrenci ekleme , silme , düzenleme ve listeleme , ödünç alma/iade etme işlemlerini yönetmek için temel CRUD (Create, Read, Update, Delete) operasyonlarını bir SQLite veritabanı üzerinde gerçekleştirmektir.



## Özellikler

- **Kitap Ekleme :** Yeni kitap bilgilerini (Başlık, Yazar, Yıl) alarak SQLite veritabanındaki books tablosuna kaydeder.
- **Kitap Güncelleme/Silme :** Mevcut kitapların bilgilerini günceller. Silme işleminde, kitabı silme işlemini gerçekleştirir.
- **Kitapları Listeleme :** Veritabanındaki tüm kitapların listesini ID, Başlık, Yazar ve Yıl bilgileriyle konsola yazdırır.
- **Öğrenci Ekleme :** Yeni öğrenci bilgilerini (İsim, Bölüm) alarak students tablosuna kaydeder.
- **Öğrenci Güncelleme/Silme :** Mevcut öğrenci bilgilerini günceller. Silme işleminde, silme işlemini gerçekleştirir.
- **Öğrencileri Listeleme :** Veritabanındaki tüm öğrencilerin listesini ID, İsim ve Bölüm bilgileriyle konsola yazdırır.
- **Kitap Ödünç Verme :** Öğrenci ve kitap ID'lerini alarak, ödünç verilecek kitabın halihazırda ödünçte olup olmadığını kontrol eder ve uygunsa kitabı ödünç verir.
- **Öğrencileri Listeleme :** Tüm ödünç geçmişini (ödünç verilmiş/iade edilmiş) listeler.
- **Kitapları Geri Teslim Alma :** Daha önceden ödünç verilmiş kitapları geri almak için kullanılan bir işlem.
---

## 🗂️ Proje Gereksinimleri Kontrol Listesi

- **Sınıflar (Class) ve Yapı:** Projenin tamamı, Book, Student, Loan, Person, Database ve 3 farklı Repository sınıfı olmak üzere mantıksal olarak ayrıştırılmış sınıflar kullanılarak yapılandırıldı.

- **Kalıtım (Inheritance):** Student sınıfı, ortak alanları (ID, İsim) yönetmek amacıyla Person sınıfından kalıtım (extends) almıştır.

- **Nesne İlişkileri (Kompozisyon):** Loan sınıfı, kendi içinde bookId ve studentId alanlarını tutarak, bir ödünç kaydının bir kitap ve bir öğrenci ile olan ilişkisini kurmuştur.

- **Constructor Kullanımı:** Tüm model sınıflarında, hem veritabanından mevcut kayıtları okumak için ID içeren constructor'lar hem de yeni veri eklemek için ID içermeyen constructor'lar doğru şekilde kullanılmıştır.

- **Koleksiyonlar (ArrayList):** Tüm Repository sınıflarında, veritabanından çekilen birden fazla kaydı (örneğin tüm kitapları veya tüm öğrencileri) Java hafızasında tutmak için ArrayList koleksiyonu etkin bir şekilde kullanılmaktadır.

- **Veritabanı Dosyası Oluşturma:** Uygulama, Database.java sınıfı aracılığıyla başlangıçta smartlibrary.db adlı yerel SQLite veritabanı dosyasını ve gerekli tabloları (books, students, loans) otomatik olarak oluşturmuştur.

- **SQLite Bağlantısı:** Veri depolama sistemi olarak SQLite motoru seçilmiş ve jdbc:sqlite URL yapısı kullanılarak başarılı bir bağlantı kurulmuştur.

- **JDBC CRUD İşlemleri:** Projenin üç ana bileşeni için (Kitap, Öğrenci, Ödünç) gerekli olan tüm add(), update(), delete(), getById() ve getAll() (CRUD operasyonları) metotları her bir Repository sınıfında eksiksiz olarak gerçekleştirilmiştir.

- **PreparedStatement Kullanımı:** Tüm veri manipülasyonu (ekleme, güncelleme) işlemleri, SQL enjeksiyon riskini önleyen ve güvenli veri aktarımı sağlayan PreparedStatement ile yapılmıştır.


## 📁 Proje Dosyalarını Hazırlama

- **İndirme/Klonlama:** Projenin zip dosyasını indirin veya Git ile klonlayın.

- **Klasörü Yerleştirme:** Proje zip veya klasörünü klasör olarak masaüstünüze çıkarın.

- **⚙️ Maven Bağımlılıklarını Yükleme: İntelliJ IDEA, genellikle pom.xml dosyasını açtığınızda sağ alt köşede "Maven projects need to be imported" uyarısı gözükür bu uyarının altındaki load a basarak projeyi çalış hale getirebilirsiniz.**



## 🗂️ Projenin Dosya Yapısı

```
SmartLibrary/
├── src/
│   ├── database/                          (Paket: Veritabanı bağlantılarını yönetir)
│   │   └── Database.java
│   ├── model/                             (Paket: Veri Taşıyıcı Nesneler)
│   │   ├── Book.java
│   │   ├── Loan.java
│   │   ├── Person.java
│   │   └── Student.java
│   ├── repository/                        (Paket: Veritabanı CRUD İşlemleri - Repository Pattern)
│   │   ├── BookRepository.java
│   │   ├── LoanRepository.java
│   │   └── StudentRepository.java
│   └── Main.java                         
├── pom.xml                              (Maven Yapılandırma dosyası)
└── smartlibrary.db                      (SQLite veritabanı dosyası)
```




## Teknik Detaylar

- Java dili kullanılarak geliştirilmiştir.
- Veri saklama için SQLite kullanılmıştır.
- Konsol tabanlı menü sistemi ile kullanıcı etkileşimi sağlanır.
- Geliştirme Mimarisi Java OOP + JDBC + SQlite
- JDBC bağlantısı, yerel dosya tabanlı erişim için jdbc:sqlite:smartlibrary.db formatıyla kurulmuştur.
- Bağımlılık Yönetimi Maven & pom.xml
