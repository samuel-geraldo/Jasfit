package database;

import java.sql.*;

public class MemberDAO {
    private Connection conn;

    public MemberDAO() {
        this.conn = DatabaseConnection.getConnection();
    }

    // Tambah member baru
    public boolean tambahMember(int userId, int paketId,
                                String tanggalDaftar, String tanggalExpired) {
        String sql = "INSERT INTO member (user_id, paket_id, tanggal_daftar, " +
                "tanggal_expired, status) VALUES (?, ?, ?, ?, 'aktif')";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, paketId);
            ps.setString(3, tanggalDaftar);
            ps.setString(4, tanggalExpired);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error tambah member: " + e.getMessage());
            return false;
        }
    }

    // Ambil data member berdasarkan user_id
    public ResultSet getMemberByUserId(int userId) {
        String sql = "SELECT m.*, p.nama_paket, p.jenis, p.harga, p.fasilitas " +
                "FROM member m " +
                "JOIN paket p ON m.paket_id = p.id " +
                "WHERE m.user_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error ambil member: " + e.getMessage());
            return null;
        }
    }

    // Update status member
    public boolean updateStatus(int memberId, String status) {
        String sql = "UPDATE member SET status = ? WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, memberId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error update status: " + e.getMessage());
            return false;
        }
    }

    // Perpanjang membership
    public boolean perpanjangMember(int memberId, int paketId, String tanggalExpiredBaru) {
        String sql = "UPDATE member SET paket_id = ?, tanggal_expired = ?, " +
                "status = 'aktif' WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, paketId);
            ps.setString(2, tanggalExpiredBaru);
            ps.setInt(3, memberId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error perpanjang member: " + e.getMessage());
            return false;
        }
    }

    // Ambil semua member untuk laporan
    public ResultSet getAllMemberLaporan() {
        String sql = "SELECT u.nama, u.email, p.jenis, m.status, " +
                "m.tanggal_daftar, m.tanggal_expired " +
                "FROM member m " +
                "JOIN users u ON m.user_id = u.id " +
                "JOIN paket p ON m.paket_id = p.id " +
                "ORDER BY m.tanggal_daftar DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error laporan member: " + e.getMessage());
            return null;
        }
    }

    // Hitung total member aktif
    public int countMemberAktif() {
        String sql = "SELECT COUNT(*) FROM member WHERE status = 'aktif'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error count member: " + e.getMessage());
        }
        return 0;
    }

    // Hitung total semua member
    public int countAllMember() {
        String sql = "SELECT COUNT(*) FROM member";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error count all member: " + e.getMessage());
        }
        return 0;
    }
}