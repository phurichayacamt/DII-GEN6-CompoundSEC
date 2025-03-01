//package src.KingPhurichaya;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class LoginFrame extends JFrame {
//    private JTextField usernameField;
//    private JPasswordField passwordField;
//    private JButton loginButton;
//
//    public LoginFrame() {
//        setTitle("Login");
//        setSize(300, 150);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//        setLayout(new GridLayout(3, 2));
//
//        add(new JLabel("Username:"));
//        usernameField = new JTextField();
//        add(usernameField);
//
//        add(new JLabel("Password:"));
//        passwordField = new JPasswordField();
//        add(passwordField);
//
//        loginButton = new JButton("Login");
//        add(loginButton);
//
//        loginButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String username = usernameField.getText();
//                String password = new String(passwordField.getPassword());
//
//                if (username.equals("admin") && password.equals("admin123")) {
//                    // ถ้ารหัสผ่านถูกต้อง ให้เปิด HotelAccessGUI
//                    new HotelAccessGUI();
//                    dispose(); // ปิด LoginFrame
//                } else {
//                    JOptionPane.showMessageDialog(LoginFrame.this, "Invalid Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });
//
//        setVisible(true);
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(LoginFrame::new);
//    }
//}
