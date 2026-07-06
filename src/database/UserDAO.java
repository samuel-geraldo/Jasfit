package database;

import model.Admin;
import model.Member;

import java.sql.*;

public class UserDAO {
    private Connection conn;

    public UserDAO() {
        this.conn = DatabaseConnection.getConnection();
    }

    // Login - cek email & password, return role
    public String[] login(String email, String password) {
        String sql = "SELECT id, nama, role FROM users WHERE email = ? AND password = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("nama"),
                        rs.getString("role")
                };
            }
        } catch (SQLException e) {
            System.out.println("Error login: " + e.getMessage());
        }
        return null;
    }

    // Tambah user baru (member)
    public boolean tambahUser(String nama, String email, String password,
                              String noTelp, String role) {
        String sql = "INSERT INTO users (nama, email, password, no_telp, role) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nama);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, noTelp);
            ps.setString(5, role);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error tambah user: " + e.getMessage());
            return false;
        }
    }

    // Ambil semua user dengan role member
    public ResultSet getAllMember() {
        String sql = "SELECT u.id, u.nama, u.email, u.no_telp, p.jenis, m.status, m.tanggal_expired " +
                "FROM users u " +
                "JOIN member m ON u.id = m.user_id " +
                "JOIN paket p ON m.paket_id = p.id " +
                "WHERE u.role = 'member'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error ambil member: " + e.getMessage());
            return null;
        }
    }

    // Hapus user berdasarkan id
    public boolean hapusUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error hapus user: " + e.getMessage());
            return false;
        }
    }

    // Update data user
    public boolean updateUser(int id, String nama, String email, String noTelp) {
        String sql = "UPDATE users SET nama = ?, email = ?, no_telp = ? WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nama);
            ps.setString(2, email);
            ps.setString(3, noTelp);
            ps.setInt(4, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error update user: " + e.getMessage());
            return false;
        }
    }
}