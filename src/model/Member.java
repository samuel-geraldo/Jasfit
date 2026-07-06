package model;

import java.util.Date;

public class Member extends Person {
    private Date tanggalDaftar;
    private Date tanggalExpired;
    private String statusMembership;
    private String jenisPaket;
    private double hargaPaket;

    public Member(int id, String nama, String email, String password,
                  String noTelp, Date tanggalDaftar, Date tanggalExpired,
                  String statusMembership, String jenisPaket, double hargaPaket) {
        super(id, nama, email, password, noTelp, "member");
        this.tanggalDaftar = tanggalDaftar;
        this.tanggalExpired = tanggalExpired;
        this.statusMembership = statusMembership;
        this.jenisPaket = jenisPaket;
        this.hargaPaket = hargaPaket;
    }

    // Getters
    public Date getTanggalDaftar() { return tanggalDaftar; }
    public Date getTanggalExpired() { return tanggalExpired; }
    public String getStatusMembership() { return statusMembership; }
    public String getJenisPaket() { return jenisPaket; }
    public double getHargaPaket() { return hargaPaket; }

    // Setters
    public void setTanggalDaftar(Date tanggalDaftar) { this.tanggalDaftar = tanggalDaftar; }
    public void setTanggalExpired(Date tanggalExpired) { this.tanggalExpired = tanggalExpired; }
    public void setStatusMembership(String statusMembership) { this.statusMembership = statusMembership; }
    public void setJenisPaket(String jenisPaket) { this.jenisPaket = jenisPaket; }
    public void setHargaPaket(double hargaPaket) { this.hargaPaket = hargaPaket; }

    // Method hitungTagihan - akan di-override subclass
    public double hitungTagihan() {
        return hargaPaket;
    }

    @Override
    public String getInfo() {
        return "Member: " + getNama() + " | Paket: " + jenisPaket +
                " | Status: " + statusMembership;
    }
}