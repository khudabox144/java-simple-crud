package model;

public class Student {
    private String id;
    private String name;
    private String department;
    private String email;
    private double cgpa;
    
    public Student(String id, String name, String department, String email, double cgpa) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.email = email;
        this.cgpa = cgpa;
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public String getEmail() { return email; }
    public double getCgpa() { return cgpa; }
    
    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDepartment(String department) { this.department = department; }
    public void setEmail(String email) { this.email = email; }
    public void setCgpa(double cgpa) { this.cgpa = cgpa; }
    
    @Override
    public String toString() {
        return id + "," + name + "," + department + "," + email + "," + cgpa;
    }
}
