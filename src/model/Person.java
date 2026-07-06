package model;

public abstract class Person {
    private int id;
    private String nama;
    private String email;
    private String password;
    private String noTelp;
    private String role;

    public Person(int id, String nama, String email,
                  String password, String noTelp, String role) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.noTelp = noTelp;
        this.role = role;
    }

    // Getters
    public int getId() { return id; }
    public String getNama() { return nama; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getNoTelp() { return noTelp; }
    public String getRole() { return role; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNama(String nama) { this.nama = nama; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setNoTelp(String noTelp) { this.noTelp = noTelp; }
    public void setRole(String role) { this.role = role; }

    // Abstract method - wajib diimplementasi subclass
    public abstract String getInfo();
}