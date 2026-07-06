package model;

public class Admin extends Person {

    public Admin(int id, String nama, String email,
                 String password, String noTelp) {
        super(id, nama, email, password, noTelp, "admin");
    }

    @Override
    public String getInfo() {
        return "Admin: " + getNama() + " | Email: " + getEmail();
    }
}