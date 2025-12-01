package model;

public class Student extends Person {
    private String department;


    public Student() {
        super(); //
    }

    // Veritabanından gelen ID ile constructor
    public Student(int id, String name, String department) {
        super(id, name);
        this.department = department;
    }

    // Yeni öğrenci eklerken kullanılacak constructor (ID otomatik)
    public Student(String name, String department) {
        super();
        this.name = name;
        this.department = department;
    }


    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
}
