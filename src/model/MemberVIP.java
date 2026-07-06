package model;

import java.util.Date;

public class MemberVIP extends Member {

    public MemberVIP(int id, String nama, String email, String password,
                     String noTelp, Date tanggalDaftar, Date tanggalExpired,
                     String statusMembership) {
        super(id, nama, email, password, noTelp,
                tanggalDaftar, tanggalExpired, statusMembership, "VIP", 500000);
    }

    @Override
    public double hitungTagihan() {
        return getHargaPaket();
    }

    @Override
    public String getInfo() {
        return "Member VIP: " + getNama() +
                " | Fasilitas: Semua + Personal Trainer" +
                " | Harga: Rp 500.000" +
                " | Status: " + getStatusMembership();
    }
}
