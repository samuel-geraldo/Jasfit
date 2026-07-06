package view;

import database.MemberDAO;
import database.KelasGymDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

public class AdminDashboardFrame extends JFrame {
    private int adminId;
    private String adminNama;
    private MemberDAO memberDAO;
    private KelasGymDAO kelasDAO;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JButton activeButton = null;

    public AdminDashboardFrame(int adminId, String adminNama) {
        this.adminId = adminId;
        this.adminNama = adminNama;
        this.memberDAO = new MemberDAO();
        this.kelasDAO = new KelasGymDAO();
        initUI();
    }

    private void initUI() {
        setTitle("JASFIT - Dashboard Admin");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // ===== SIDEBAR =====
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(new Color(18, 18, 30));
        sidebar.setPreferredSize(new Dimension(200, 0));

        // Logo barbell
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth() / 2;
                int cy = 30;
                g2.setColor(new Color(33, 150, 243));
                g2.setStroke(new BasicStroke(4));
                g2.drawLine(cx - 22, cy, cx + 22, cy);
                g2.setStroke(new BasicStroke(1));
                g2.fillRoundRect(cx - 34, cy - 9, 13, 18, 5, 5);
                g2.fillRoundRect(cx - 22, cy - 6, 7, 12, 3, 3);
                g2.fillRoundRect(cx + 15, cy - 6, 7, 12, 3, 3);
                g2.fillRoundRect(cx + 22, cy - 9, 13, 18, 5, 5);
            }
        };
        logoPanel.setBackground(new Color(18, 18, 30));
        logoPanel.setPreferredSize(new Dimension(200, 90));
        logoPanel.setLayout(new BorderLayout());

        JLabel appName = new JLabel("JASFIT", SwingConstants.CENTER);
        appName.setFont(new Font("Arial", Font.BOLD, 24));
        appName.setForeground(new Color(33, 150, 243));
        appName.setBorder(BorderFactory.createEmptyBorder(52, 0, 0, 0));
        logoPanel.add(appName, BorderLayout.CENTER);

        // Admin info
        JPanel adminInfo = new JPanel(new GridLayout(2, 1));
        adminInfo.setBackground(new Color(28, 28, 45));
        adminInfo.setPreferredSize(new Dimension(200, 55));
        adminInfo.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JLabel adminNamaLabel = new JLabel(adminNama);
        adminNamaLabel.setFont(new Font("Arial", Font.BOLD, 13));
        adminNamaLabel.setForeground(Color.WHITE);

        JLabel adminRoleLabel = new JLabel("Administrator");
        adminRoleLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        adminRoleLabel.setForeground(new Color(150, 150, 150));

        adminInfo.add(adminNamaLabel);
        adminInfo.add(adminRoleLabel);

        // Top section
        JPanel sidebarTop = new JPanel();
        sidebarTop.setLayout(new BoxLayout(sidebarTop, BoxLayout.Y_AXIS));
        sidebarTop.setBackground(new Color(18, 18, 30));
        sidebarTop.add(logoPanel);
        sidebarTop.add(adminInfo);

        // Menu section
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(18, 18, 30));

        JLabel menuLabel = new JLabel("  MENU");
        menuLabel.setFont(new Font("Arial", Font.BOLD, 10));
        menuLabel.setForeground(new Color(90, 90, 110));
        menuLabel.setMaximumSize(new Dimension(200, 28));
        menuLabel.setMinimumSize(new Dimension(200, 28));
        menuLabel.setPreferredSize(new Dimension(200, 28));
        menuLabel.setBorder(BorderFactory.createEmptyBorder(10, 12, 4, 0));

        JButton dashBtn = createSidebarButton("Dashboard", "dashboard", new Color(33, 150, 243));
        JButton memberBtn = createSidebarButton("Kelola Member", "member", new Color(76, 175, 80));
        JButton paketBtn = createSidebarButton("Kelola Paket", "paket", new Color(255, 152, 0));
        JButton kelasBtn = createSidebarButton("Kelola Kelas", "kelas", new Color(156, 39, 176));
        JButton transaksiBtn = createSidebarButton("Transaksi", "transaksi", new Color(0, 188, 212));

        setActiveButton(dashBtn, new Color(33, 150, 243));

        menuPanel.add(menuLabel);
        menuPanel.add(dashBtn);
        menuPanel.add(memberBtn);
        menuPanel.add(paketBtn);
        menuPanel.add(kelasBtn);
        menuPanel.add(transaksiBtn);

        // Bottom logout
        JPanel logoutPanel = new JPanel(new BorderLayout());
        logoutPanel.setBackground(new Color(18, 18, 30));
        logoutPanel.setPreferredSize(new Dimension(200, 44));

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(35, 35, 55));
        sep.setPreferredSize(new Dimension(200, 1));

        JPanel logoutWrapper = new JPanel(new BorderLayout());
        logoutWrapper.setBackground(new Color(18, 18, 30));
        logoutWrapper.add(sep, BorderLayout.NORTH);
        logoutWrapper.add(createLogoutButton(), BorderLayout.CENTER);

        sidebar.add(sidebarTop, BorderLayout.NORTH);
        sidebar.add(menuPanel, BorderLayout.CENTER);
        sidebar.add(logoutWrapper, BorderLayout.SOUTH);

        // ===== CONTENT =====
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.add(createDashboardPanel(), "dashboard");
        contentPanel.add(createMemberPanel(), "member");
        contentPanel.add(createPaketPanel(), "paket");
        contentPanel.add(createKelasPanel(), "kelas");
        contentPanel.add(createTransaksiPanel(), "transaksi");

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void setActiveButton(JButton btn, Color accent) {
        if (activeButton != null) {
            activeButton.setBackground(new Color(18, 18, 30));
            activeButton.setForeground(new Color(180, 180, 190));
        }
        activeButton = btn;
        activeButton.setBackground(new Color(28, 40, 60));
        activeButton.setForeground(accent);
    }

    private JButton createSidebarButton(String text, String card, Color accent) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(200, 44));
        btn.setMinimumSize(new Dimension(200, 44));
        btn.setPreferredSize(new Dimension(200, 44));
        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        btn.setForeground(new Color(180, 180, 190));
        btn.setBackground(new Color(18, 18, 30));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 12, 0, 0));
        btn.addActionListener(e -> {
            cardLayout.show(contentPanel, card);
            setActiveButton(btn, accent);
        });
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn != activeButton) {
                    btn.setBackground(new Color(25, 25, 42));
                    btn.setForeground(accent);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (btn != activeButton) {
                    btn.setBackground(new Color(18, 18, 30));
                    btn.setForeground(new Color(180, 180, 190));
                }
            }
        });
        return btn;
    }

    private JButton createLogoutButton() {
        JButton btn = new JButton("Keluar");
        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        btn.setForeground(new Color(255, 80, 80));
        btn.setBackground(new Color(18, 18, 30));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(10, 12, 10, 0));
        btn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        return btn;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(
                    javax.swing.table.TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0
                            ? Color.WHITE : new Color(248, 249, 252));
                    c.setForeground(new Color(40, 40, 50));
                }
                return c;
            }
        };
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(34);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(245, 246, 250));
        table.getTableHeader().setForeground(new Color(90, 90, 100));
        table.getTableHeader().setPreferredSize(new Dimension(0, 36));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, new Color(228, 228, 235)));
        table.setGridColor(new Color(238, 238, 242));
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(33, 150, 243, 50));
        table.setSelectionForeground(new Color(20, 20, 30));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        return table;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 245, 250));

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(25, 25, 35));

        JLabel subtitle = new JLabel("Selamat datang, " + adminNama);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(new Color(130, 130, 140));

        JPanel titleBox = new JPanel(new GridLayout(2, 1, 0, 2));
        titleBox.setBackground(new Color(245, 245, 250));
        titleBox.add(title);
        titleBox.add(subtitle);
        headerPanel.add(titleBox, BorderLayout.WEST);

        MemberDAO freshMember = new MemberDAO();
        KelasGymDAO freshKelas = new KelasGymDAO();
        int totalMember = freshMember.countAllMember();
        int memberAktif = freshMember.countMemberAktif();
        int totalKelas = freshKelas.countKelasAktif();
        int expired = totalMember - memberAktif;

        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 14, 0));
        statsPanel.setBackground(new Color(245, 245, 250));
        statsPanel.add(createStatCard("Total Member", String.valueOf(totalMember),
                new Color(33, 150, 243), "Semua member terdaftar"));
        statsPanel.add(createStatCard("Member Aktif", String.valueOf(memberAktif),
                new Color(76, 175, 80), "Aktif bulan ini"));
        statsPanel.add(createStatCard("Kelas Gym", String.valueOf(totalKelas),
                new Color(255, 152, 0), "Kelas tersedia"));
        statsPanel.add(createStatCard("Expired", String.valueOf(expired),
                new Color(244, 67, 54), "Perlu perpanjang"));

        JPanel tableCard = new JPanel(new BorderLayout(0, 10));
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(228, 228, 235)),
                BorderFactory.createEmptyBorder(16, 0, 0, 0)
        ));

        JLabel tableTitle = new JLabel("Member Terbaru");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 14));
        tableTitle.setForeground(new Color(35, 35, 45));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        String[] cols = {"Nama", "Email", "Paket", "Status", "Tgl Daftar", "Expired"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        try {
            ResultSet rs = memberDAO.getAllMemberLaporan();
            while (rs != null && rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("nama"), rs.getString("email"),
                        rs.getString("jenis"), rs.getString("status"),
                        rs.getString("tanggal_daftar"), rs.getString("tanggal_expired")
                });
            }
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }

        JTable table = createStyledTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        tableCard.add(tableTitle, BorderLayout.NORTH);
        tableCard.add(scroll, BorderLayout.CENTER);

        JPanel topSection = new JPanel(new BorderLayout(0, 16));
        topSection.setBackground(new Color(245, 245, 250));
        topSection.add(headerPanel, BorderLayout.NORTH);
        topSection.add(statsPanel, BorderLayout.CENTER);

        panel.add(topSection, BorderLayout.NORTH);
        panel.add(tableCard, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStatCard(String label, String value, Color color, String sub) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(228, 228, 235)),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        top.setBackground(Color.WHITE);
        JPanel bar = new JPanel();
        bar.setBackground(color);
        bar.setPreferredSize(new Dimension(32, 3));
        top.add(bar);

        JLabel valLabel = new JLabel(value);
        valLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valLabel.setForeground(color);

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Arial", Font.BOLD, 12));
        lblLabel.setForeground(new Color(50, 50, 60));

        JLabel subLabel = new JLabel(sub);
        subLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        subLabel.setForeground(new Color(160, 160, 170));

        JPanel content = new JPanel(new GridLayout(3, 1, 0, 2));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        content.add(valLabel);
        content.add(lblLabel);
        content.add(subLabel);

        card.add(top, BorderLayout.NORTH);
        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private JPanel createMemberPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        JLabel title = new JLabel("Kelola Member");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(25, 25, 35));

        String[] cols = {"Nama", "Email", "Paket", "Status", "Tgl Daftar", "Expired"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        try {
            ResultSet rs = memberDAO.getAllMemberLaporan();
            while (rs != null && rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("nama"), rs.getString("email"),
                        rs.getString("jenis"), rs.getString("status"),
                        rs.getString("tanggal_daftar"), rs.getString("tanggal_expired")
                });
            }
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }

        JTable table = createStyledTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton tambahBtn = new JButton("+ Tambah Member");
        tambahBtn.setBackground(new Color(33, 150, 243));
        tambahBtn.setForeground(Color.WHITE);
        tambahBtn.setFont(new Font("Arial", Font.BOLD, 13));
        tambahBtn.setBorderPainted(false);
        tambahBtn.setFocusPainted(false);
        tambahBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tambahBtn.setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));
        tambahBtn.addActionListener(e -> new TambahMemberFrame(this).setVisible(true));

        JButton hapusBtn = new JButton("Hapus Member");
        hapusBtn.setBackground(new Color(244, 67, 54));
        hapusBtn.setForeground(Color.WHITE);
        hapusBtn.setFont(new Font("Arial", Font.BOLD, 13));
        hapusBtn.setBorderPainted(false);
        hapusBtn.setFocusPainted(false);
        hapusBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        hapusBtn.setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));
        hapusBtn.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if (sel == -1) {
                JOptionPane.showMessageDialog(this, "Pilih member yang ingin dihapus!",
                        "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String nama = model.getValueAt(sel, 0).toString();
            String email = model.getValueAt(sel, 1).toString();
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Hapus member " + nama + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    ResultSet rs = new database.UserDAO().getAllMember();
                    while (rs != null && rs.next()) {
                        if (rs.getString("email").equals(email)) {
                            new database.UserDAO().hapusUser(rs.getInt("id"));
                            model.removeRow(sel);
                            JOptionPane.showMessageDialog(this, "Member berhasil dihapus!",
                                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        }
                    }
                } catch (Exception ex) { System.out.println("Error: " + ex.getMessage()); }
            }
        });

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(245, 245, 250));
        topBar.add(title, BorderLayout.WEST);
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setBackground(new Color(245, 245, 250));
        btnRow.add(hapusBtn);
        btnRow.add(tambahBtn);
        topBar.add(btnRow, BorderLayout.EAST);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(228, 228, 235)));

        panel.add(topBar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPaketPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        JLabel title = new JLabel("Kelola Paket");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(25, 25, 35));

        String[] cols = {"ID", "Nama Paket", "Jenis", "Harga", "Durasi", "Fasilitas"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        try {
            ResultSet rs = new database.PaketDAO().getAllPaket();
            while (rs != null && rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"), rs.getString("nama_paket"),
                        rs.getString("jenis"), "Rp " + (int) rs.getDouble("harga"),
                        rs.getInt("durasi_bulan") + " bulan", rs.getString("fasilitas")
                });
            }
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }

        JTable table = createStyledTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(228, 228, 235)));

        panel.add(title, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createKelasPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        JLabel title = new JLabel("Kelola Kelas Gym");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(25, 25, 35));

        String[] cols = {"ID", "Nama Kelas", "Instruktur", "Hari",
                "Jam Mulai", "Jam Selesai", "Kuota"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        try {
            ResultSet rs = kelasDAO.getAllKelas();
            while (rs != null && rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"), rs.getString("nama_kelas"),
                        rs.getString("instruktur"), rs.getString("hari"),
                        rs.getString("jam_mulai"), rs.getString("jam_selesai"),
                        rs.getInt("kuota")
                });
            }
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }

        JTable table = createStyledTable(model);

        JButton tambahBtn = new JButton("+ Tambah Kelas");
        tambahBtn.setBackground(new Color(156, 39, 176));
        tambahBtn.setForeground(Color.WHITE);
        tambahBtn.setFont(new Font("Arial", Font.BOLD, 13));
        tambahBtn.setBorderPainted(false);
        tambahBtn.setFocusPainted(false);
        tambahBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tambahBtn.setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));
        tambahBtn.addActionListener(e -> new TambahKelasFrame(this).setVisible(true));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(245, 245, 250));
        topBar.add(title, BorderLayout.WEST);
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnRow.setBackground(new Color(245, 245, 250));
        btnRow.add(tambahBtn);
        topBar.add(btnRow, BorderLayout.EAST);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(228, 228, 235)));

        panel.add(topBar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTransaksiPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        JLabel title = new JLabel("Data Transaksi");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(25, 25, 35));

        String[] cols = {"ID", "Member", "Paket", "Total Bayar", "Tanggal", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        try {
            String sql = "SELECT t.id, u.nama, p.nama_paket, t.total_bayar, " +
                    "t.tanggal_bayar, t.status_bayar FROM transaksi t " +
                    "JOIN member m ON t.member_id = m.id " +
                    "JOIN users u ON m.user_id = u.id " +
                    "JOIN paket p ON t.paket_id = p.id " +
                    "ORDER BY t.tanggal_bayar DESC";
            java.sql.Connection conn = database.DatabaseConnection.getConnection();
            java.sql.ResultSet rs = conn.prepareStatement(sql).executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"), rs.getString("nama"),
                        rs.getString("nama_paket"),
                        "Rp " + (int) rs.getDouble("total_bayar"),
                        rs.getString("tanggal_bayar"), rs.getString("status_bayar")
                });
            }
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }

        JTable table = createStyledTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(228, 228, 235)));

        panel.add(title, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    public void refreshMemberPanel() {
        contentPanel.remove(contentPanel.getComponent(1));
        contentPanel.add(createMemberPanel(), "member", 1);
        cardLayout.show(contentPanel, "member");
    }

    public void refreshKelasPanel() {
        contentPanel.remove(contentPanel.getComponent(3));
        contentPanel.add(createKelasPanel(), "kelas", 3);
        cardLayout.show(contentPanel, "kelas");
    }

    public void refreshTransaksiPanel() {
        contentPanel.remove(contentPanel.getComponent(4));
        contentPanel.add(createTransaksiPanel(), "transaksi", 4);
        cardLayout.show(contentPanel, "transaksi");
    }
}