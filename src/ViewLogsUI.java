//package src;
//
//import src.KingPhurichaya.ViewLogsUI;
//
//import javax.swing.*;
//import java.awt.*;
//
////class Start {
////    public static void main(String[] args) {
////        SwingUtilities.invokeLater(() -> {
////            Test mainFrame = new Test();
////            mainFrame.setVisible(true);
////        });
////    }
////}
////
////class Test extends JFrame {
////    public Test() {
////        setTitle("hotel Access Control");
////        setSize(800, 600);
////        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
////        setLocationRelativeTo(null);
////        setLayout(new BorderLayout());
////
////        JLabel headerLabel = new JLabel("Welcome to hotel Access Control", SwingConstants.CENTER);
////        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
////        headerLabel.setOpaque(true);
////        headerLabel.setBackground(new Color(0, 102, 204));
////        headerLabel.setForeground(Color.WHITE);
////        add(headerLabel, BorderLayout.NORTH);
////
////        JPanel buttonPanel = new JPanel();
////        buttonPanel.setLayout(new GridLayout(2, 2, 10, 10));
////
////        JButton accessButton = new JButton("Access System");
////        JButton registerButton = new JButton("Register Card");
////        JButton revokeButton = new JButton("Revoke Card");
////        JButton logsButton = new JButton("View Logs");
////
////        buttonPanel.add(accessButton);
////        buttonPanel.add(registerButton);
////        buttonPanel.add(revokeButton);
////        buttonPanel.add(logsButton);
////
////        add(buttonPanel, BorderLayout.CENTER);
////
////        JPanel footerPanel = new JPanel();
////        JLabel footerLabel = new JLabel("© 2025 hotel Security System", SwingConstants.CENTER);
////        footerPanel.add(footerLabel);
////        add(footerPanel, BorderLayout.SOUTH);
////
//////        // เชื่อมโยงปุ่มเข้ากับ UI อื่นๆ
//////        accessButton.addActionListener(e -> new AccessUI().setVisible(true));
//////        registerButton.addActionListener(e -> new RegisterCardUI().setVisible(true));
//////        revokeButton.addActionListener(e -> new RevokeCardUI().setVisible(true));
//////        logsButton.addActionListener(e -> new ViewLogsUI().setVisible(true));
////    }
////}
////
////
