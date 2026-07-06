package view;

import database.KelasGymDAO;
import javax.swing.*;
import java.awt.*;

public class TambahKelasFrame extends JFrame {
    private AdminDashboardFrame parent;
    private KelasGymDAO kelasDAO;

    private JTextField namaKelasField;
    private JTextField instrukturField;
    private JComboBox<String> hariCombo;
    private JTextField jamMulaiField;
    private JTextField jamSelesaiField;
    private JTextField kuotaField;

    public TambahKelasFrame(AdminDashboardFrame parent) {
        this.parent = parent;
        this.kelasDAO = new KelasGymDAO();
        initUI();
    }

    private void initUI() {
        setTitle("JASFIT - Tambah Kelas");
        setSize(450, 520);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("Tambah Kelas Baru");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        namaKelasField = new JTextField();
        instrukturField = new JTextField();
        jamMulaiField = new JTextField();
        jamSelesaiField = new JTextField();
        kuotaField = new JTextField();

        hariCombo = new JComboBox<>(new String[]{
                "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"
        });
        hariCombo.setFont(new Font("Arial", Font.PLAIN, 13));

        JButton simpanBtn = new JButton("Simpan");
        simpanBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        simpanBtn.setMaximumSize(new Dimension(350, 45));
        simpanBtn.setBackground(new Color(33, 150, 243));
        simpanBtn.setForeground(Color.WHITE);
        simpanBtn.setFont(new Font("Arial", Font.BOLD, 14));
        simpanBtn.setBorderPainted(false);
        simpanBtn.setFocusPainted(false);
        simpanBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        simpanBtn.addActionListener(e -> simpanKelas());

        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buatField("Nama Kelas", namaKelasField));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buatField("Instruktur", instrukturField));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buatCombo("Hari", hariCombo));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buatField("Jam Mulai (HH:MM)", jamMulaiField));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buatField("Jam Selesai (HH:MM)", jamSelesaiField));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buatField("Kuota", kuotaField));
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(simpanBtn);

        add(mainPanel);
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

    private void simpanKelas() {
        String namaKelas = namaKelasField.getText().trim();
        String instruktur = instrukturField.getText().trim();
        String hari = hariCombo.getSelectedItem().toString();
        String jamMulai = jamMulaiField.getText().trim();
        String jamSelesai = jamSelesaiField.getText().trim();
        String kuotaStr = kuotaField.getText().trim();

        if (namaKelas.isEmpty() || instruktur.isEmpty() ||
                jamMulai.isEmpty() || jamSelesai.isEmpty() || kuotaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Semua field harus diisi!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int kuota = Integer.parseInt(kuotaStr);
            boolean berhasil = kelasDAO.tambahKelas(
                    namaKelas, instruktur, hari,
                    jamMulai + ":00", jamSelesai + ":00", kuota);

            if (berhasil) {
                JOptionPane.showMessageDialog(this,
                        "Kelas berhasil ditambahkan!",
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                parent.refreshKelasPanel();
                dispose();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Kuota harus berupa angka!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}