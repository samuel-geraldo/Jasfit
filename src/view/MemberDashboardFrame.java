package view;

import database.MemberDAO;
import database.KelasGymDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.time.LocalDate;

public class MemberDashboardFrame extends JFrame {
    private int userId;
    private String memberNama;
    private MemberDAO memberDAO;
    private KelasGymDAO kelasDAO;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private int memberId;
    private JButton activeButton = null;

    public MemberDashboardFrame(int userId, String memberNama) {
        this.userId = userId;
        this.memberNama = memberNama;
        this.memberDAO = new MemberDAO();
        this.kelasDAO = new KelasGymDAO();
        this.memberId = getMemberId();
        initUI();
    }

    private int getMemberId() {
        try {
            ResultSet rs = memberDAO.getMemberByUserId(userId);
            if (rs != null && rs.next()) return rs.getInt("id");
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
        return -1;
    }

    private void initUI() {
        setTitle("JASFIT - Dashboard Member");
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
                g2.setColor(new Color(76, 175, 80));
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
        appName.setForeground(new Color(76, 175, 80));
        appName.setBorder(BorderFactory.createEmptyBorder(52, 0, 0, 0));
        logoPanel.add(appName, BorderLayout.CENTER);

        // Member info
        JPanel memberInfo = new JPanel(new GridLayout(2, 1));
        memberInfo.setBackground(new Color(28, 28, 45));
        memberInfo.setPreferredSize(new Dimension(200, 55));
        memberInfo.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JLabel memberNamaLabel = new JLabel(memberNama);
        memberNamaLabel.setFont(new Font("Arial", Font.BOLD, 13));
        memberNamaLabel.setForeground(Color.WHITE);

        JLabel memberRoleLabel = new JLabel("Member Aktif");
        memberRoleLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        memberRoleLabel.setForeground(new Color(76, 175, 80));

        memberInfo.add(memberNamaLabel);
        memberInfo.add(memberRoleLabel);

        // Top section
        JPanel sidebarTop = new JPanel();
        sidebarTop.setLayout(new BoxLayout(sidebarTop, BoxLayout.Y_AXIS));
        sidebarTop.setBackground(new Color(18, 18, 30));
        sidebarTop.add(logoPanel);
        sidebarTop.add(memberInfo);

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

        JButton dashBtn = createSidebarButton("Dashboard", "dashboard", new Color(76, 175, 80));
        JButton profilBtn = createSidebarButton("Profil Saya", "profil", new Color(33, 150, 243));
        JButton kelasBtn = createSidebarButton("Jadwal Kelas", "kelas", new Color(255, 152, 0));
        JButton tagihanBtn = createSidebarButton("Riwayat Tagihan", "tagihan", new Color(156, 39, 176));

        setActiveButton(dashBtn, new Color(76, 175, 80));

        menuPanel.add(menuLabel);
        menuPanel.add(dashBtn);
        menuPanel.add(profilBtn);
        menuPanel.add(kelasBtn);
        menuPanel.add(tagihanBtn);

        // Bottom logout
        JPanel logoutWrapper = new JPanel(new BorderLayout());
        logoutWrapper.setBackground(new Color(18, 18, 30));

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(35, 35, 55));
        sep.setPreferredSize(new Dimension(200, 1));

        logoutWrapper.add(sep, BorderLayout.NORTH);
        logoutWrapper.add(createLogoutButton(), BorderLayout.CENTER);

        sidebar.add(sidebarTop, BorderLayout.NORTH);
        sidebar.add(menuPanel, BorderLayout.CENTER);
        sidebar.add(logoutWrapper, BorderLayout.SOUTH);

        // ===== CONTENT =====
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.add(createDashboardPanel(), "dashboard");
        contentPanel.add(createProfilPanel(), "profil");
        contentPanel.add(createKelasPanel(), "kelas");
        contentPanel.add(createTagihanPanel(), "tagihan");

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
        table.setSelectionBackground(new Color(76, 175, 80, 50));
        table.setSelectionForeground(new Color(20, 20, 30));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        return table;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(25, 25, 35));

        JLabel subtitle = new JLabel("Selamat datang, " + memberNama);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(new Color(130, 130, 140));

        JPanel titleBox = new JPanel(new GridLayout(2, 1, 0, 2));
        titleBox.setBackground(new Color(245, 245, 250));
        titleBox.add(title);
        titleBox.add(subtitle);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 245, 250));
        headerPanel.add(titleBox, BorderLayout.WEST);

        // Stat cards - fixed height
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 14, 0));
        statsPanel.setBackground(new Color(245, 245, 250));

        try {
            ResultSet rs = memberDAO.getMemberByUserId(userId);
            if (rs != null && rs.next()) {
                statsPanel.add(createStatCard("Status",
                        rs.getString("status"), new Color(76, 175, 80), "Status membership"));
                statsPanel.add(createStatCard("Paket",
                        rs.getString("jenis"), new Color(33, 150, 243), "Paket aktif saat ini"));
                statsPanel.add(createStatCard("Expired",
                        rs.getString("tanggal_expired"), new Color(255, 152, 0), "Tanggal berakhir"));
            }
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }

        JPanel topSection = new JPanel(new BorderLayout(0, 16));
        topSection.setBackground(new Color(245, 245, 250));
        topSection.add(headerPanel, BorderLayout.NORTH);
        topSection.add(statsPanel, BorderLayout.CENTER);

        // Filler abu-abu biar konsisten sama admin
        JPanel filler = new JPanel();
        filler.setBackground(new Color(245, 245, 250));

        panel.add(topSection, BorderLayout.NORTH);
        panel.add(filler, BorderLayout.CENTER);
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

        JLabel valLabel = new JLabel(value != null ? value : "-");
        valLabel.setFont(new Font("Arial", Font.BOLD, 26));
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

    private JPanel createProfilPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        JLabel title = new JLabel("Profil Saya");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(25, 25, 35));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        // Pakai JPanel biasa bukan GridBagLayout
        JPanel profilCard = new JPanel();
        profilCard.setBackground(Color.WHITE);
        profilCard.setLayout(new BoxLayout(profilCard, BoxLayout.Y_AXIS));
        profilCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(228, 228, 235)),
                BorderFactory.createEmptyBorder(20, 28, 20, 28)
        ));

        try {
            ResultSet rs = memberDAO.getMemberByUserId(userId);
            if (rs != null && rs.next()) {
                profilCard.add(createProfilRow("Nama", memberNama));
                profilCard.add(createProfilRow("Jenis Paket", rs.getString("jenis")));
                profilCard.add(createProfilRow("Fasilitas", rs.getString("fasilitas")));
                profilCard.add(createProfilRow("Tanggal Daftar", rs.getString("tanggal_daftar")));
                profilCard.add(createProfilRow("Tanggal Expired", rs.getString("tanggal_expired")));
                profilCard.add(createProfilRow("Status", rs.getString("status")));
                profilCard.add(createProfilRow("Harga Paket",
                        "Rp " + (int) rs.getDouble("harga")));
            }
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }

        // Wrapper supaya profilCard tidak stretch ke bawah
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(245, 245, 250));
        wrapper.add(profilCard, BorderLayout.NORTH);

        panel.add(title, BorderLayout.NORTH);
        panel.add(wrapper, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createProfilRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(new Color(120, 120, 130));
        lbl.setPreferredSize(new Dimension(180, 24));

        JLabel val = new JLabel(value != null ? value : "-");
        val.setFont(new Font("Arial", Font.PLAIN, 13));
        val.setForeground(new Color(35, 35, 45));

        // Garis pembatas bawah
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 242)),
                BorderFactory.createEmptyBorder(10, 0, 10, 0)
        ));

        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.CENTER);
        return row;
    }

    private JPanel createKelasPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        JLabel title = new JLabel("Jadwal Kelas");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(25, 25, 35));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));

        JLabel labelTerdaftar = new JLabel("Kelas yang Diikuti");
        labelTerdaftar.setFont(new Font("Arial", Font.BOLD, 14));
        labelTerdaftar.setForeground(new Color(76, 175, 80));
        labelTerdaftar.setBorder(BorderFactory.createEmptyBorder(4, 0, 8, 0));

        String[] colsTerdaftar = {"Nama Kelas", "Instruktur", "Hari",
                "Jam Mulai", "Jam Selesai", "Status"};
        DefaultTableModel modelTerdaftar = new DefaultTableModel(colsTerdaftar, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        try {
            ResultSet rs = kelasDAO.getKelasByMember(memberId);
            while (rs != null && rs.next()) {
                modelTerdaftar.addRow(new Object[]{
                        rs.getString("nama_kelas"), rs.getString("instruktur"),
                        rs.getString("hari"), rs.getString("jam_mulai"),
                        rs.getString("jam_selesai"), rs.getString("status")
                });
            }
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }

        JTable tableTerdaftar = createStyledTable(modelTerdaftar);
        JScrollPane scrollTerdaftar = new JScrollPane(tableTerdaftar);
        scrollTerdaftar.setBorder(BorderFactory.createLineBorder(new Color(228, 228, 235)));

        JLabel labelSemua = new JLabel("Semua Kelas Tersedia");
        labelSemua.setFont(new Font("Arial", Font.BOLD, 14));
        labelSemua.setForeground(new Color(255, 152, 0));
        labelSemua.setBorder(BorderFactory.createEmptyBorder(12, 0, 8, 0));

        String[] colsSemua = {"ID", "Nama Kelas", "Instruktur", "Hari",
                "Jam Mulai", "Jam Selesai", "Kuota"};
        DefaultTableModel modelSemua = new DefaultTableModel(colsSemua, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        try {
            ResultSet rs = kelasDAO.getAllKelas();
            while (rs != null && rs.next()) {
                modelSemua.addRow(new Object[]{
                        rs.getInt("id"), rs.getString("nama_kelas"),
                        rs.getString("instruktur"), rs.getString("hari"),
                        rs.getString("jam_mulai"), rs.getString("jam_selesai"),
                        rs.getInt("kuota")
                });
            }
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }

        JTable tableSemua = createStyledTable(modelSemua);
        tableSemua.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollSemua = new JScrollPane(tableSemua);
        scrollSemua.setBorder(BorderFactory.createLineBorder(new Color(228, 228, 235)));

        JButton daftarBtn = new JButton("+ Daftar Kelas");
        daftarBtn.setBackground(new Color(76, 175, 80));
        daftarBtn.setForeground(Color.WHITE);
        daftarBtn.setFont(new Font("Arial", Font.BOLD, 13));
        daftarBtn.setBorderPainted(false);
        daftarBtn.setFocusPainted(false);
        daftarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        daftarBtn.setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));
        daftarBtn.addActionListener(e -> {
            int sel = tableSemua.getSelectedRow();
            if (sel == -1) {
                JOptionPane.showMessageDialog(this, "Pilih kelas yang ingin didaftarkan!",
                        "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int kelasId = (int) modelSemua.getValueAt(sel, 0);
            String namaKelas = modelSemua.getValueAt(sel, 1).toString();
            boolean berhasil = kelasDAO.daftarKelas(memberId, kelasId,
                    LocalDate.now().toString());
            if (berhasil) {
                JOptionPane.showMessageDialog(this,
                        "Berhasil daftar kelas " + namaKelas + "!",
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                refreshKelasPanel();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Gagal daftar kelas, mungkin sudah terdaftar!",
                        "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnPanel.setBackground(new Color(245, 245, 250));
        btnPanel.add(daftarBtn);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 250));
        topPanel.add(labelTerdaftar, BorderLayout.NORTH);
        topPanel.add(scrollTerdaftar, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(245, 245, 250));
        bottomPanel.add(labelSemua, BorderLayout.NORTH);
        bottomPanel.add(scrollSemua, BorderLayout.CENTER);
        bottomPanel.add(btnPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                topPanel, bottomPanel);
        splitPane.setDividerLocation(200);
        splitPane.setDividerSize(5);
        splitPane.setBorder(null);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 245, 250));
        headerPanel.add(title, BorderLayout.WEST);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }

    public void refreshKelasPanel() {
        contentPanel.remove(contentPanel.getComponent(2));
        contentPanel.add(createKelasPanel(), "kelas", 2);
        cardLayout.show(contentPanel, "kelas");
    }

    private JPanel createTagihanPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        JLabel title = new JLabel("Riwayat Tagihan");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(25, 25, 35));

        String[] cols = {"Paket", "Total Bayar", "Tanggal Bayar", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        try {
            String sql = "SELECT p.nama_paket, t.total_bayar, " +
                    "t.tanggal_bayar, t.status_bayar " +
                    "FROM transaksi t " +
                    "JOIN member m ON t.member_id = m.id " +
                    "JOIN paket p ON t.paket_id = p.id " +
                    "WHERE m.user_id = ? " +
                    "ORDER BY t.tanggal_bayar DESC";
            java.sql.Connection conn = database.DatabaseConnection.getConnection();
            java.sql.PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("nama_paket"),
                        "Rp " + (int) rs.getDouble("total_bayar"),
                        rs.getString("tanggal_bayar"),
                        rs.getString("status_bayar")
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
}