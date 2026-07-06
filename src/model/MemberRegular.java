package model;

import java.util.Date;

public class MemberRegular extends Member {

    public MemberRegular(int id, String nama, String email, String password,
                         String noTelp, Date tanggalDaftar, Date tanggalExpired,
                         String statusMembership) {
        super(id, nama, email, password, noTelp,
                tanggalDaftar, tanggalExpired, statusMembership, "Regular", 150000);
    }

    @Override
    public double hitungTagihan() {
        return getHargaPaket();
    }

    @Override
    public String getInfo() {
        return "Member Regular: " + getNama() +
                " | Fasilitas: Akses Gym" +
                " | Harga: Rp 150.000" +
                " | Status: " + getStatusMembership();
    }
}