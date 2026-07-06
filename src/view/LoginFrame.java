package view;

import database.UserDAO;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private UserDAO userDAO;

    public LoginFrame() {
        userDAO = new UserDAO();
        initUI();
    }

    private void initUI() {
        setTitle("JASFIT - Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(400, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        JPanel loginCard = new JPanel();
        loginCard.setLayout(new BorderLayout());
        loginCard.setBackground(Color.WHITE);
        loginCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(0, 0, 30, 0)
        ));
        loginCard.setPreferredSize(new Dimension(420, 520));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setPreferredSize(new Dimension(420, 160));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel logoLabel = new JLabel("JASFIT");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 42));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subLabel = new JLabel("Gym Management System");
        subLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subLabel.setForeground(new Color(255, 255, 255, 200));
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(Box.createVerticalGlue());
        headerPanel.add(logoLabel);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(subLabel);
        headerPanel.add(Box.createVerticalGlue());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 10, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.weightx = 1.0;

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(emailLabel, gbc);

        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(340, 42));
        emailField.setFont(new Font("Arial", Font.PLAIN, 13));
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        gbc.gridy = 1;
        formPanel.add(emailField, gbc);

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Arial", Font.BOLD, 13));
        gbc.gridy = 2;
        formPanel.add(passLabel, gbc);

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(340, 42));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 13));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        gbc.gridy = 3;
        formPanel.add(passwordField, gbc);

        loginButton = new JButton("Masuk");
        loginButton.setPreferredSize(new Dimension(340, 46));
        loginButton.setBackground(new Color(33, 150, 243));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 15));
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 4;
        gbc.insets = new Insets(20, 0, 8, 0);
        formPanel.add(loginButton, gbc);

        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        rolePanel.setBackground(Color.WHITE);
        JLabel roleInfo = new JLabel("Login sebagai:");
        roleInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        roleInfo.setForeground(new Color(150, 150, 150));
        JLabel adminBadge = new JLabel("Admin");
        adminBadge.setFont(new Font("Arial", Font.PLAIN, 12));
        adminBadge.setForeground(new Color(33, 150, 243));
        adminBadge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(33, 150, 243)),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));
        JLabel orLabel = new JLabel("atau");
        orLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        orLabel.setForeground(new Color(150, 150, 150));
        JLabel memberBadge = new JLabel("Member");
        memberBadge.setFont(new Font("Arial", Font.PLAIN, 12));
        memberBadge.setForeground(new Color(76, 175, 80));
        memberBadge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(76, 175, 80)),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));
        rolePanel.add(roleInfo);
        rolePanel.add(adminBadge);
        rolePanel.add(orLabel);
        rolePanel.add(memberBadge);

        gbc.gridy = 5;
        gbc.insets = new Insets(4, 0, 0, 0);
        formPanel.add(rolePanel, gbc);

        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Email dan password tidak boleh kosong!",
                        "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String[] result = userDAO.login(email, password);
            if (result != null) {
                int id = Integer.parseInt(result[0]);
                String nama = result[1];
                String role = result[2];
                dispose();
                if (role.equals("admin")) {
                    new AdminDashboardFrame(id, nama).setVisible(true);
                } else {
                    new MemberDashboardFrame(id, nama).setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Email atau password salah!",
                        "Login Gagal", JOptionPane.ERROR_MESSAGE);
            }
        });

        passwordField.addActionListener(e -> loginButton.doClick());

        loginCard.add(headerPanel, BorderLayout.NORTH);
        loginCard.add(formPanel, BorderLayout.CENTER);

        mainPanel.add(loginCard);
        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}