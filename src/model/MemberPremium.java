package model;

import java.util.Date;

public class MemberPremium extends Member {

    public MemberPremium(int id, String nama, String email, String password,
                         String noTelp, Date tanggalDaftar, Date tanggalExpired,
                         String statusMembership) {
        super(id, nama, email, password, noTelp,
                tanggalDaftar, tanggalExpired, statusMembership, "Premium", 300000);
    }

    @Override
    public double hitungTagihan() {
        return getHargaPaket();
    }

    @Override
    public String getInfo() {
        return "Member Premium: " + getNama() +
                " | Fasilitas: Akses Gym + Kelas + Loker" +
                " | Harga: Rp 300.000" +
                " | Status: " + getStatusMembership();
    }
}