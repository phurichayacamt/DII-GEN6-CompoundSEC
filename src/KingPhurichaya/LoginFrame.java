//package src.KingPhurichaya;// LoginFrame.java
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//
//public class LoginFrame extends JFrame {
//    private JTextField usernameField;
//    private JPasswordField passwordField;
//    private JButton loginButton;
//
//    public LoginFrame() {
//        setTitle("Hotel Security System - Login");
//        setSize(400, 200);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        JPanel panel = new JPanel(new GridLayout(3, 2));
//        panel.add(new JLabel("Username:"));
//        usernameField = new JTextField();
//        panel.add(usernameField);
//
//        panel.add(new JLabel("Password:"));
//        passwordField = new JPasswordField();
//        panel.add(passwordField);
//
//        loginButton = new JButton("Login");
//        loginButton.addActionListener(e -> handleLogin());
//        panel.add(loginButton);
//
//        add(panel);
//    }
//
//    private void handleLogin() {
//        String username = usernameField.getText();
//        String password = new String(passwordField.getPassword());
//
//        if ("admin".equals(username) && "admin123".equals(password)) {
//            new AdminPanel().setVisible(true);
//            dispose();
//        } else if ("customer".equals(username) && "customer123".equals(password)) {
//            new CustomerPanel().setVisible(true);
//            dispose();
//        } else {
//            JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
//    }
//}
//
//
//
//
package src.KingPhurichaya;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("Hotel Security System - Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        panel.add(new JLabel());
        panel.add(loginButton);

        add(panel);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if ("king".equals(username) && "1234".equals(password)) {
            new MainFrame(); // เปิดหน้าหลัก
            dispose(); // ปิดหน้าล็อกอิน
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
