package database;

import java.sql.*;

public class PaketDAO {
    private Connection conn;

    public PaketDAO() {
        this.conn = DatabaseConnection.getConnection();
    }

    // Ambil semua paket
    public ResultSet getAllPaket() {
        String sql = "SELECT * FROM paket";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error ambil paket: " + e.getMessage());
            return null;
        }
    }

    // Ambil paket berdasarkan id
    public ResultSet getPaketById(int id) {
        String sql = "SELECT * FROM paket WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error ambil paket: " + e.getMessage());
            return null;
        }
    }

    // Tambah paket baru
    public boolean tambahPaket(String namaPaket, String jenis,
                               double harga, int durasiBulan, String fasilitas) {
        String sql = "INSERT INTO paket (nama_paket, jenis, harga, " +
                "durasi_bulan, fasilitas) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, namaPaket);
            ps.setString(2, jenis);
            ps.setDouble(3, harga);
            ps.setInt(4, durasiBulan);
            ps.setString(5, fasilitas);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error tambah paket: " + e.getMessage());
            return false;
        }
    }

    // Update paket
    public boolean updatePaket(int id, String namaPaket, String jenis,
                               double harga, int durasiBulan, String fasilitas) {
        String sql = "UPDATE paket SET nama_paket = ?, jenis = ?, harga = ?, " +
                "durasi_bulan = ?, fasilitas = ? WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, namaPaket);
            ps.setString(2, jenis);
            ps.setDouble(3, harga);
            ps.setInt(4, durasiBulan);
            ps.setString(5, fasilitas);
            ps.setInt(6, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error update paket: " + e.getMessage());
            return false;
        }
    }

    // Hapus paket
    public boolean hapusPaket(int id) {
        String sql = "DELETE FROM paket WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error hapus paket: " + e.getMessage());
            return false;
        }
    }
}