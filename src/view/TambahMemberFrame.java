package view;

import database.MemberDAO;
import database.PaketDAO;
import database.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.time.LocalDate;

public class TambahMemberFrame extends JFrame {
    private AdminDashboardFrame parent;
    private UserDAO userDAO;
    private MemberDAO memberDAO;
    private PaketDAO paketDAO;

    private JTextField namaField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField noTelpField;
    private JComboBox<String> paketCombo;
    private int[] paketIds;
    private double[] paketHarga;

    public TambahMemberFrame(AdminDashboardFrame parent) {
        this.parent = parent;
        this.userDAO = new UserDAO();
        this.memberDAO = new MemberDAO();
        this.paketDAO = new PaketDAO();
        initUI();
    }

    private void initUI() {
        setTitle("JASFIT - Tambah Member");
        setSize(450, 550);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("Tambah Member Baru");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        namaField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        noTelpField = new JTextField();
        paketCombo = new JComboBox<>();
        paketCombo.setFont(new Font("Arial", Font.PLAIN, 13));

        loadPaket();

        JButton simpanBtn = new JButton("Simpan");
        simpanBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        simpanBtn.setMaximumSize(new Dimension(350, 45));
        simpanBtn.setBackground(new Color(33, 150, 243));
        simpanBtn.setForeground(Color.WHITE);
        simpanBtn.setFont(new Font("Arial", Font.BOLD, 14));
        simpanBtn.setBorderPainted(false);
        simpanBtn.setFocusPainted(false);
        simpanBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        simpanBtn.addActionListener(e -> simpanMember());

        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buatField("Nama Lengkap", namaField));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buatField("Email", emailField));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buatPasswordField("Password", passwordField));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buatField("No. Telepon", noTelpField));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buatCombo("Pilih Paket", paketCombo));
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(simpanBtn);

        add(mainPanel);
    }

    private void loadPaket() {
        try {
            ResultSet rs = paketDAO.getAllPaket();
            java.util.List<Integer> ids = new java.util.ArrayList<>();
            java.util.List<Double> hargaList = new java.util.ArrayList<>();
            while (rs != null && rs.next()) {
                paketCombo.addItem(rs.getString("nama_paket") +
                        " - Rp " + (int) rs.getDouble("harga"));
                ids.add(rs.getInt("id"));
                hargaList.add(rs.getDouble("harga"));
            }
            paketIds = ids.stream().mapToInt(i -> i).toArray();
            paketHarga = hargaList.stream().mapToDouble(d -> d).toArray();
        } catch (Exception e) {
            System.out.println("Error load paket: " + e.getMessage());
        }
    }

    private JPanel buatField(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        field.setPreferredSize(new Dimension(350, 38));
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buatPasswordField(String label, JPasswordField field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        field.setPreferredSize(new Dimension(350, 38));
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buatCombo(String label, JComboBox<String> combo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        combo.setPreferredSize(new Dimension(350, 38));
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(combo, BorderLayout.CENTER);
        return panel;
    }

    private void simpanMember() {
        String nama = namaField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String noTelp = noTelpField.getText().trim();
        int paketIndex = paketCombo.getSelectedIndex();

        if (nama.isEmpty() || email.isEmpty() ||
                password.isEmpty() || noTelp.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Semua field harus diisi!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean userBerhasil = userDAO.tambahUser(
                nama, email, password, noTelp, "member");

        if (userBerhasil) {
            String[] loginResult = userDAO.login(email, password);
            if (loginResult != null) {
                int userId = Integer.parseInt(loginResult[0]);
                int paketId = paketIds[paketIndex];
                double harga = paketHarga[paketIndex];
                LocalDate today = LocalDate.now();
                LocalDate expired = today.plusMonths(1);

                // Tambah member
                memberDAO.tambahMember(userId, paketId,
                        today.toString(), expired.toString());

                // Catat transaksi otomatis
                try {
                    ResultSet rs = memberDAO.getMemberByUserId(userId);
                    if (rs != null && rs.next()) {
                        int memberId = rs.getInt("id");
                        String sql = "INSERT INTO transaksi " +
                                "(member_id, paket_id, total_bayar, tanggal_bayar, status_bayar) " +
                                "VALUES (?, ?, ?, ?, 'lunas')";
                        java.sql.Connection conn = database.DatabaseConnection.getConnection();
                        java.sql.PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setInt(1, memberId);
                        ps.setInt(2, paketId);
                        ps.setDouble(3, harga);
                        ps.setString(4, today.toString());
                        ps.executeUpdate();
                    }
                } catch (Exception ex) {
                    System.out.println("Error catat transaksi: " + ex.getMessage());
                }

                JOptionPane.showMessageDialog(this,
                        "Member berhasil ditambahkan!",
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                parent.refreshMemberPanel();
                dispose();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Email sudah terdaftar!",
                    "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }
}