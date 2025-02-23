package src.KingPhurichaya;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class HotelAccessGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}

class MainFrame extends JFrame {
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final DefaultTableModel auditTableModel;
    private final JTable auditTable;
    private final JLabel activeCardsLabel;
    private final JLabel revokedCardsLabel;
    private final JLabel lockedCardsLabel;
    private final DefaultListModel<String> logListModel;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public MainFrame() {
        setTitle("Compound Security System");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Navigation Panel
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton dashboardButton = createStyledButton("Dashboard");
        JButton cardMgmtButton = createStyledButton("Card Management");
        JButton auditLogButton = createStyledButton("Audit Log");

        navPanel.add(dashboardButton);
        navPanel.add(cardMgmtButton);
        navPanel.add(auditLogButton);
        panel.add(navPanel, BorderLayout.NORTH);

        // Main Panel with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        panel.add(mainPanel, BorderLayout.CENTER);

        // Dashboard Panel
        JPanel dashboardPanel = new JPanel(new BorderLayout(10, 10));
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        activeCardsLabel = createStyledLabel("Active Cards: 0");
        revokedCardsLabel = createStyledLabel("Revoked Cards: 0");
        lockedCardsLabel = createStyledLabel("Locked Cards: 0");
        statsPanel.add(activeCardsLabel);
        statsPanel.add(revokedCardsLabel);
        statsPanel.add(lockedCardsLabel);

        logListModel = new DefaultListModel<>();
        JList<String> logList = new JList<>(logListModel);
        JScrollPane logScrollPane = new JScrollPane(logList);

        dashboardPanel.add(statsPanel, BorderLayout.NORTH);
        dashboardPanel.add(logScrollPane, BorderLayout.CENTER);
        mainPanel.add(dashboardPanel, "Dashboard");

        // Card Management Panel
        String[] columnNames = {"Card ID", "Name", "Access Level", "Status", "Expiry Date", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel cardMgmtPanel = new JPanel(new BorderLayout(10, 10));
        JButton addCardButton = createStyledButton("! Add New Card +");
        addCardButton.addActionListener(this::addNewCard);
        cardMgmtPanel.add(scrollPane, BorderLayout.CENTER);
        cardMgmtPanel.add(addCardButton, BorderLayout.SOUTH);
        mainPanel.add(cardMgmtPanel, "Card Management");

        // Audit Log Panel
        String[] auditColumns = {"Timestamp", "Action", "Details"};
        auditTableModel = new DefaultTableModel(auditColumns, 0);
        auditTable = new JTable(auditTableModel);
        JScrollPane auditScrollPane = new JScrollPane(auditTable);

        JPanel auditPanel = new JPanel(new BorderLayout(10, 10));
        auditPanel.add(auditScrollPane, BorderLayout.CENTER);
        mainPanel.add(auditPanel, "Audit Log");

        add(panel);
        setVisible(true);

        // Navigation Button Actions
        dashboardButton.addActionListener(e -> {
            updateDashboard();
            cardLayout.show(mainPanel, "Dashboard");
        });
        cardMgmtButton.addActionListener(e -> cardLayout.show(mainPanel, "Card Management"));
        auditLogButton.addActionListener(e -> cardLayout.show(mainPanel, "Audit Log"));
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setOpaque(true);
        label.setBackground(new Color(240, 240, 240));
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        return label;
    }

    private void addNewCard(ActionEvent e) {
        JTextField cardIDField = new JTextField();
        JTextField ownerField = new JTextField();
        String[] accessLevels = {"Admin(เข้าถึงทุกชั้น)", "Employee(เข้าถึงชั้นทำงานและชั้นส่วนกลาง)", "Guest(เข้าถึงเฉพาะชั้นที่จองห้องพัก และพื้นที่สาธารณะ )", "VIP(เข้าถึงชั้นพิเศษ)", "Maintenance(เข้าถึงทุกชั้นที่ต้องซ่อมบำรุง)", "Cleaning(เข้าถึงชั้นที่ต้องทำความสะอาด)"};
        JComboBox<String> accessBox = new JComboBox<>(accessLevels);
        JTextField expiryField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("Card ID:"));
        panel.add(cardIDField);
        panel.add(new JLabel("Name:"));
        panel.add(ownerField);
        panel.add(new JLabel("Access Level:"));
        panel.add(accessBox);
        panel.add(new JLabel("Expiry Date (YYYY-MM-DD):"));
        panel.add(expiryField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Register New Card", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String cardID = cardIDField.getText();
            String owner = ownerField.getText();
            String accessLevel = (String) accessBox.getSelectedItem();
            String expiryDate = expiryField.getText();
            if (!cardID.isEmpty() && !owner.isEmpty()) {
                tableModel.addRow(new Object[]{cardID, owner, accessLevel, "Active", expiryDate, "Edit | Revoke | Lock | Extend"});
                logAction("Registered Card", "Card ID: " + cardID);
                updateDashboard();
            }
        }
    }

    private void logAction(String action, String details) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        auditTableModel.addRow(new Object[]{timestamp, action, details});
        logListModel.addElement(timestamp + " - " + action + " - " + details);
    }

    private void updateDashboard() {
        activeCardsLabel.setText("Active Cards: " + tableModel.getRowCount());
    }
}
