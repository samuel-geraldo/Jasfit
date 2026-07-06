package database;

import java.sql.*;

public class KelasGymDAO {
    private Connection conn;

    public KelasGymDAO() {
        this.conn = DatabaseConnection.getConnection();
    }

    // Ambil semua kelas
    public ResultSet getAllKelas() {
        String sql = "SELECT * FROM kelas_gym";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error ambil kelas: " + e.getMessage());
            return null;
        }
    }

    // Tambah kelas baru
    public boolean tambahKelas(String namaKelas, String instruktur,
                               String hari, String jamMulai,
                               String jamSelesai, int kuota) {
        String sql = "INSERT INTO kelas_gym (nama_kelas, instruktur, hari, " +
                "jam_mulai, jam_selesai, kuota) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, namaKelas);
            ps.setString(2, instruktur);
            ps.setString(3, hari);
            ps.setString(4, jamMulai);
            ps.setString(5, jamSelesai);
            ps.setInt(6, kuota);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error tambah kelas: " + e.getMessage());
            return false;
        }
    }

    // Update kelas
    public boolean updateKelas(int id, String namaKelas, String instruktur,
                               String hari, String jamMulai,
                               String jamSelesai, int kuota) {
        String sql = "UPDATE kelas_gym SET nama_kelas = ?, instruktur = ?, " +
                "hari = ?, jam_mulai = ?, jam_selesai = ?, " +
                "kuota = ? WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, namaKelas);
            ps.setString(2, instruktur);
            ps.setString(3, hari);
            ps.setString(4, jamMulai);
            ps.setString(5, jamSelesai);
            ps.setInt(6, kuota);
            ps.setInt(7, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error update kelas: " + e.getMessage());
            return false;
        }
    }

    // Hapus kelas
    public boolean hapusKelas(int id) {
        String sql = "DELETE FROM kelas_gym WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error hapus kelas: " + e.getMessage());
            return false;
        }
    }

    // Daftarkan member ke kelas
    public boolean daftarKelas(int memberId, int kelasId, String tanggalDaftar) {
        String sql = "INSERT INTO pendaftaran_kelas (member_id, kelas_id, " +
                "tanggal_daftar, status) VALUES (?, ?, ?, 'aktif')";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, memberId);
            ps.setInt(2, kelasId);
            ps.setString(3, tanggalDaftar);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error daftar kelas: " + e.getMessage());
            return false;
        }
    }

    // Ambil kelas yang diikuti member
    public ResultSet getKelasByMember(int memberId) {
        String sql = "SELECT k.nama_kelas, k.instruktur, k.hari, " +
                "k.jam_mulai, k.jam_selesai, pk.status " +
                "FROM pendaftaran_kelas pk " +
                "JOIN kelas_gym k ON pk.kelas_id = k.id " +
                "WHERE pk.member_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, memberId);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error ambil kelas member: " + e.getMessage());
            return null;
        }
    }

    // Hitung total kelas aktif
    public int countKelasAktif() {
        String sql = "SELECT COUNT(*) FROM kelas_gym";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error count kelas: " + e.getMessage());
        }
        return 0;
    }
}