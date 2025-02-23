//package src.KingPhurichaya;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class AccessControlGUI {
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            new MainFrame();
//        });
//    }
//}
//
//class MainFrame extends JFrame {
//    public MainFrame() {
//        setTitle("Hotel Access Control");
//        setSize(800, 500);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        JPanel mainPanel = new JPanel();
//        mainPanel.setLayout(new BorderLayout());
//
//        JLabel titleLabel = new JLabel("Welcome to Hotel Access Control", SwingConstants.CENTER);
//        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
//        titleLabel.setOpaque(true);
//        titleLabel.setBackground(Color.BLUE);
//        titleLabel.setForeground(Color.WHITE);
//        mainPanel.add(titleLabel, BorderLayout.NORTH);
//
//        JPanel gridPanel = new JPanel(new GridLayout(3, 2, 10, 10));
//
//        JButton accessSystemButton = new JButton("Access System");
//        JButton registerCardButton = new JButton("Register Card");
//        JButton revokeCardButton = new JButton("Revoke Card");
//        JButton viewLogsButton = new JButton("View Logs");
//        JButton cardCountButton = new JButton("Card Count");
//        JButton saveDataButton = new JButton("Save Data");
//
//        accessSystemButton.addActionListener(e -> openFeatureWindow("Access System"));
//        registerCardButton.addActionListener(e -> openFeatureWindow("Register Card"));
//        revokeCardButton.addActionListener(e -> openFeatureWindow("Revoke Card"));
//        viewLogsButton.addActionListener(e -> openFeatureWindow("View Logs"));
//        cardCountButton.addActionListener(e -> openFeatureWindow("Card Count"));
//        saveDataButton.addActionListener(e -> openFeatureWindow("Save Data"));
//
//        gridPanel.add(accessSystemButton);
//        gridPanel.add(registerCardButton);
//        gridPanel.add(revokeCardButton);
//        gridPanel.add(viewLogsButton);
//        gridPanel.add(cardCountButton);
//        gridPanel.add(saveDataButton);
//
//        mainPanel.add(gridPanel, BorderLayout.CENTER);
//
//        JLabel footerLabel = new JLabel("\u00A9 2025 Hotel Security System", SwingConstants.CENTER);
//        mainPanel.add(footerLabel, BorderLayout.SOUTH);
//
//        add(mainPanel);
//        setVisible(true);
//    }
//
//    private void openFeatureWindow(String featureName) {
//        JFrame featureFrame = new JFrame(featureName);
//        featureFrame.setSize(400, 300);
//        featureFrame.setLocationRelativeTo(this);
//        JLabel label = new JLabel("Feature: " + featureName, SwingConstants.CENTER);
//        label.setFont(new Font("Arial", Font.BOLD, 16));
//        featureFrame.add(label);
//        featureFrame.setVisible(true);
//    }
//}
//
