//package src.KingPhurichaya;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.awt.event.*;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//public class HotelAccessGUI extends JFrame{
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(MainFrame::new);
//    }
//}
//
//class MainFrame extends JFrame {
//    private final DefaultTableModel tableModel;
//    private final JTable table;
//    private final DefaultTableModel auditTableModel;
//    private final JTable auditTable;
//    private final JLabel activeCardsLabel;
//    private final JLabel revokedCardsLabel;
//    private final JLabel lockedCardsLabel;
//    private final DefaultListModel<String> logListModel;
//    private final CardLayout cardLayout;
//    private final JPanel mainPanel;
//    private String status;
//
//    public MainFrame() {
//
//        setTitle("Hotel Security System");
//        setSize(1000, 600);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        // Main panel with 20px padding
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//        panel.setBackground(new Color(243, 244, 246)); // Light gray background
//
//        // Navigation Panel
//        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
//        navPanel.setBackground(new Color(243, 244, 246));
//
//        JButton dashboardButton = createStyledButton("Dashboard");
//        JButton cardMgmtButton = createStyledButton("Card Management");
//        cardMgmtButton.setPreferredSize(new Dimension(180, 50)); // Modified width for Card Management button
//        JButton auditLogButton = createStyledButton("Audit Log");
//
//        navPanel.add(dashboardButton);
//        navPanel.add(cardMgmtButton);
//        navPanel.add(auditLogButton);
//        panel.add(navPanel, BorderLayout.NORTH);
//
//        // Main Panel with CardLayout
//        cardLayout = new CardLayout();
//        mainPanel = new JPanel(cardLayout);
//        mainPanel.setBackground(new Color(243, 244, 246));
//        panel.add(mainPanel, BorderLayout.CENTER);
//
//        // Dashboard Panel
//        JPanel dashboardPanel = new JPanel(new BorderLayout(0, 20));
//        dashboardPanel.setBackground(new Color(243, 244, 246));
//
//        // Stats Panel
//        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
//        statsPanel.setBackground(new Color(243, 244, 246));
//
//        activeCardsLabel = createStatsLabel("Active Cards: 0");
//        revokedCardsLabel = createStatsLabel("Revoked Cards: 0");
//        lockedCardsLabel = createStatsLabel("Locked Cards: 0");
//
//        statsPanel.add(activeCardsLabel);
//        statsPanel.add(revokedCardsLabel);
//        statsPanel.add(lockedCardsLabel);
//
//        // Activity Log Panel
//        JPanel logPanel = new JPanel(new BorderLayout(0, 10));
//        logPanel.setBackground(Color.WHITE);
//        logPanel.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createLineBorder(new Color(229, 231, 235)),
//                BorderFactory.createEmptyBorder(15, 15, 15, 15)
//        ));
//
//        JLabel logTitle = new JLabel("Activity Log List");
//        logTitle.setFont(new Font("Arial", Font.BOLD, 14));
//        logTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
//
//        logListModel = new DefaultListModel<>();
//        JList<String> logList = new JList<>(logListModel);
//        logList.setBackground(Color.WHITE);
//
//        JScrollPane logScrollPane = new JScrollPane(logList);
//        logScrollPane.setBorder(null);
//
//        logPanel.add(logTitle, BorderLayout.NORTH);
//        logPanel.add(logScrollPane, BorderLayout.CENTER);
//
//        dashboardPanel.add(statsPanel, BorderLayout.NORTH);
//        dashboardPanel.add(logPanel, BorderLayout.CENTER);
//        mainPanel.add(dashboardPanel, "Dashboard");
//
//        // Card Management Panel
//        String[] columnNames = {"Card ID", "Name", "Access Level", "Status", "Expiry Date", "Actions"};
//        tableModel = new DefaultTableModel(columnNames, 0);
//        table = new JTable(tableModel);
//        ActionButtonEditorRenderer.addActionButtons(table, tableModel, this);
//        // กำหนดความกว้างของคอลัมน์ Actions
//        table.getColumnModel().getColumn(5).setPreferredWidth(200);
//        table.setRowHeight(5, 50); // แถวที่ 3 (index 2) สูง 50px
//        table.getColumnModel().getColumn(5).setMinWidth(250); // เพิ่มความกว้างขั้นต่ำ
//        table.getColumnModel().getColumn(5).setMaxWidth(300); // จำกัดความกว้างสูงสุด
//
//        table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(tableModel, this));
//
//        JScrollPane scrollPane = new JScrollPane(table);
//
//        JPanel cardMgmtPanel = new JPanel(new BorderLayout(0, 20));
//        cardMgmtPanel.setBackground(new Color(243, 244, 246));
//        JButton addCardButton = createStyledButton("Add New Card");
//        addCardButton.addActionListener(this::addNewCard);
//
//        cardMgmtPanel.add(scrollPane, BorderLayout.CENTER);
//        cardMgmtPanel.add(addCardButton, BorderLayout.SOUTH);
//        mainPanel.add(cardMgmtPanel, "Card Management");
//
//        // Audit Log Panel
//        String[] auditColumns = {"Timestamp", "Action", "Details"};
//        auditTableModel = new DefaultTableModel(auditColumns, 0);
//        auditTable = new JTable(auditTableModel);
//        JScrollPane auditScrollPane = new JScrollPane(auditTable);
//
//        JPanel auditPanel = new JPanel(new BorderLayout());
//        auditPanel.setBackground(new Color(243, 244, 246));
//        auditPanel.add(auditScrollPane, BorderLayout.CENTER);
//        mainPanel.add(auditPanel, "Audit Log");
//
//        add(panel);
//        setVisible(true);
//
//        // Navigation Button Actions
//        dashboardButton.addActionListener(e -> {
//            updateDashboard();
//            cardLayout.show(mainPanel, "Dashboard");
//            updateButtonStates(dashboardButton, cardMgmtButton, auditLogButton);
//        });
//        cardMgmtButton.addActionListener(e -> {
//            cardLayout.show(mainPanel, "Card Management");
//            updateButtonStates(cardMgmtButton, dashboardButton, auditLogButton);
//        });
//        auditLogButton.addActionListener(e -> {
//            cardLayout.show(mainPanel, "Audit Log");
//            updateButtonStates(auditLogButton, dashboardButton, cardMgmtButton);
//        });
//    }
//
//    private JButton createStyledButton(String text) {
//        JButton button = new JButton(text);
//        button.setFont(new Font("Arial", Font.BOLD, 14));
//        button.setBackground(new Color(59, 130, 246)); // Bright blue
//        button.setForeground(Color.black);
//        button.setFocusPainted(false);
//        button.setBorderPainted(false);
//        button.setPreferredSize(new Dimension(150, 40));
//        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        return button;
//    }
//
//    private JLabel createStatsLabel(String text) {
//        JLabel label = new JLabel(text, SwingConstants.CENTER);
//        label.setFont(new Font("Arial", Font.BOLD, 18));
//        label.setOpaque(true);
//        label.setBackground(Color.WHITE);
//        label.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createLineBorder(new Color(229, 231, 235)),
//                BorderFactory.createEmptyBorder(20, 15, 20, 15)
//        ));
//        return label;
//    }
//
//    private void updateButtonStates(JButton activeButton, JButton... inactiveButtons) {
//        activeButton.setBackground(new Color(37, 99, 235)); // Darker blue for active
//        for (JButton button : inactiveButtons) {
//            button.setBackground(new Color(59, 130, 246)); // Normal blue for inactive
//        }
//    }
//
//    private void addNewCard(ActionEvent e) {
//        JTextField cardIDField = new JTextField();
//        JTextField ownerField = new JTextField();
//        JComboBox<String> accessBox = new JComboBox<>(new String[]{"Admin", "Employee", "Guest", "VIP", "Maintenance", "Cleaning"});
//        JTextField expiryField = new JTextField();
//
//        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
//        panel.add(new JLabel("Card ID:"));
//        panel.add(cardIDField);
//        panel.add(new JLabel("Name:"));
//        panel.add(ownerField);
//        panel.add(new JLabel("Access Level:"));
//        panel.add(accessBox);
//        panel.add(new JLabel("Expiry Date (YYYY-MM-DD):"));
//        panel.add(expiryField);
//
//        int result = JOptionPane.showConfirmDialog(this, panel, "Register New Card", JOptionPane.OK_CANCEL_OPTION);
//        if (result == JOptionPane.OK_OPTION) {
//            String cardID = cardIDField.getText().trim();
//            String owner = ownerField.getText().trim();
//            String expiryDate = expiryField.getText().trim();
//
//            // เช็คว่าข้อมูลครบหรือไม่
//            if (cardID.isEmpty() || owner.isEmpty() || expiryDate.isEmpty()) {
//                JOptionPane.showMessageDialog(this, "กรุณากรอกข้อมูลให้ครบทุกช่อง", "Warning", JOptionPane.WARNING_MESSAGE);
//                return;
//            }
//
//            // เช็คว่า Card ID ซ้ำหรือไม่
//            for (int i = 0; i < tableModel.getRowCount(); i++) {
//                String existingCardID = tableModel.getValueAt(i, 0).toString();
//                if (cardID.equals(existingCardID)) {
//                    JOptionPane.showMessageDialog(this, "Card ID นี้ถูกใช้ไปแล้ว", "Error", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//            }
//
//            // เช็คว่า วันที่มีรูปแบบถูกต้องหรือไม่ (YYYY-MM-DD)
//            if (!expiryDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
//                JOptionPane.showMessageDialog(this, "กรุณากรอกวันที่ในรูปแบบ YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            // ถ้าทุกอย่างผ่าน เพิ่มข้อมูลลงตาราง
//            tableModel.addRow(new Object[]{cardID, owner, accessBox.getSelectedItem(), "Active", expiryDate, "Edit | Revoke | Lock"});
//            logAction("Registered Card", "Card ID: " + cardID);
//            updateDashboard();
//        }
//    }
//
//
//
//    void logAction(String action, String details) {
//        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        auditTableModel.addRow(new Object[]{timestamp, action, details});
//        logListModel.addElement(timestamp + " - " + action + " - " + details);
//    }
//
//    private void updateDashboard() {
//        int activeCount = 0, revokedCount = 0, lockedCount = 0;
//
//        for (int i = 0; i < tableModel.getRowCount(); i++) {
//            String status = (String) tableModel.getValueAt(i, 3); // คอลัมน์ Status
//            if ("Active".equals(status)) {
//                activeCount++;
//            } else if ("Revoked".equals(status)) {
//                revokedCount++;
//            } else if ("Locked".equals(status)) {
//                lockedCount++;
//            }
//        }
//
//        activeCardsLabel.setText("Active Cards: " + activeCount);
//        revokedCardsLabel.setText("Revoked Cards: " + revokedCount);
//        lockedCardsLabel.setText("Locked Cards: " + lockedCount);
//    }
//
//    private void addActionButtons() {
//        table.getColumn("Actions").setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
//            JPanel panel = new JPanel(new GridLayout(1, 3, 5, 0)); // ปรับให้เป็น GridLayout
//            JButton editButton = new JButton("Edit");
//            JButton revokeButton = new JButton("Revoke");
//            JButton lockButton = new JButton("Lock");
//
//            editButton.addActionListener(e -> editCard(row));
//            revokeButton.addActionListener(e -> updateCardStatus(row, "Revoked"));
//            lockButton.addActionListener(e -> updateCardStatus(row, "Locked"));
//
//            panel.add(editButton);
//            panel.add(revokeButton);
//            panel.add(lockButton);
//            return panel;
//        });
//    }
//
//    void editCard(int row) {
//        String cardID = (String) tableModel.getValueAt(row, 0);
//        String name = (String) tableModel.getValueAt(row, 1);
//        String accessLevel = (String) tableModel.getValueAt(row, 2);
//        String expiryDate = (String) tableModel.getValueAt(row, 4);
//
//        JTextField nameField = new JTextField(name);
//        JComboBox<String> accessBox = new JComboBox<>(new String[]{"Admin", "Employee", "Guest", "VIP", "Maintenance", "Cleaning"});
//        accessBox.setSelectedItem(accessLevel);
//        JTextField expiryField = new JTextField(expiryDate);
//
//        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
//        panel.add(new JLabel("Name:"));
//        panel.add(nameField);
//        panel.add(new JLabel("Access Level:"));
//        panel.add(accessBox);
//        panel.add(new JLabel("Expiry Date (YYYY-MM-DD):"));
//        panel.add(expiryField);
//
//        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Card", JOptionPane.OK_CANCEL_OPTION);
//        if (result == JOptionPane.OK_OPTION) {
//            tableModel.setValueAt(nameField.getText(), row, 1);
//            tableModel.setValueAt(accessBox.getSelectedItem(), row, 2);
//            tableModel.setValueAt(expiryField.getText(), row, 4);
//            logAction("Edited Card", "Card ID: " + cardID);
//        }
//    }
//
//    private void updateCardStatus(int row, String status) {
//        String cardID = (String) tableModel.getValueAt(row, 0);
//        tableModel.setValueAt(status, row, 3);
//        logAction(status + " Card", "Card ID: " + cardID);
//    }
//
//    public void add(int row) {
//    }
//}
package src.KingPhurichaya;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HotelAccessGUI extends JFrame {
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

    private static final String AUDIT_LOG_FILE = "audit_log.csv";

    public MainFrame() {
        setTitle("Hotel Security System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);



        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(243, 244, 246));

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        navPanel.setBackground(new Color(243, 244, 246));

        JButton dashboardButton = createStyledButton("Dashboard");
        JButton cardMgmtButton = createStyledButton("Card Management");
        JButton auditLogButton = createStyledButton("Audit Log");
        JButton exitButton = createStyledButton("Exit Program");

        exitButton.addActionListener(e -> System.exit(0));  // ปุ่มออกจากโปรแกรม

        navPanel.add(dashboardButton);
        navPanel.add(cardMgmtButton);
        navPanel.add(auditLogButton);
        navPanel.add(exitButton);  // เพิ่มปุ่มออก

        panel.add(navPanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        panel.add(mainPanel, BorderLayout.CENTER);

        // Dashboard Panel
        JPanel dashboardPanel = new JPanel(new BorderLayout(0, 20));
        dashboardPanel.setBackground(new Color(243, 244, 246));

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        activeCardsLabel = createStatsLabel("Active Cards: 0");
        revokedCardsLabel = createStatsLabel("Revoked Cards: 0");
        lockedCardsLabel = createStatsLabel("Locked Cards: 0");

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
        JScrollPane tableScrollPane = new JScrollPane(table);
        JButton addCardButton = createStyledButton("Add New Card");
        addCardButton.addActionListener(this::addNewCard);

        JPanel cardMgmtPanel = new JPanel(new BorderLayout(0, 20));
        cardMgmtPanel.add(tableScrollPane, BorderLayout.CENTER);
        cardMgmtPanel.add(addCardButton, BorderLayout.SOUTH);
        mainPanel.add(cardMgmtPanel, "Card Management");

        // Audit Log Panel
        auditTableModel = new DefaultTableModel(new String[]{"Timestamp", "Action", "Details"}, 0);
        auditTable = new JTable(auditTableModel);
        JScrollPane auditScrollPane = new JScrollPane(auditTable);
        mainPanel.add(new JPanel(new BorderLayout()) {{
            add(auditScrollPane, BorderLayout.CENTER);
        }}, "Audit Log");

        add(panel);
        setupTableActions(); // ตั้งค่า MouseListener สำหรับ table
        loadAuditLog();  // โหลดข้อมูลจากไฟล์ขึ้นมา

        dashboardButton.addActionListener(e -> showPanel("Dashboard", dashboardButton, cardMgmtButton, auditLogButton));
        cardMgmtButton.addActionListener(e -> showPanel("Card Management", cardMgmtButton, dashboardButton, auditLogButton));
        auditLogButton.addActionListener(e -> showPanel("Audit Log", auditLogButton, dashboardButton, cardMgmtButton));

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 40));
        return button;
    }

    private JLabel createStatsLabel(String text) {
        return new JLabel(text, SwingConstants.CENTER);
    }

    private void showPanel(String panel, JButton active, JButton... others) {
        cardLayout.show(mainPanel, panel);
        active.setBackground(Color.GRAY);
        for (JButton btn : others) btn.setBackground(null);
    }

    private void addNewCard(ActionEvent e) {
        JTextField cardIDField = new JTextField();
        JTextField ownerField = new JTextField();
        JComboBox<String> accessBox = new JComboBox<>(new String[]{"Admin", "Employee", "Guest", "VIP", "Maintenance", "Cleaning"});
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
            String cardID = cardIDField.getText().trim();
            String owner = ownerField.getText().trim();
            String expiryDate = expiryField.getText().trim();

            // เช็คว่าข้อมูลครบหรือไม่
            if (cardID.isEmpty() || owner.isEmpty() || expiryDate.isEmpty()) {
                JOptionPane.showMessageDialog(null, "กรุณากรอกข้อมูลให้ครบทุกช่อง", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // เช็คว่า Card ID ซ้ำหรือไม่
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String existingCardID = tableModel.getValueAt(i, 0).toString();
                if (cardID.equals(existingCardID)) {
                    JOptionPane.showMessageDialog(this, "Card ID นี้ถูกใช้ไปแล้ว", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // เช็คว่า วันที่มีรูปแบบถูกต้องหรือไม่ (YYYY-MM-DD)
            if (!expiryDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, "กรุณากรอกวันที่ในรูปแบบ YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // ถ้าทุกอย่างผ่าน เพิ่มข้อมูลลงตาราง
            tableModel.addRow(new Object[]{cardID, owner, accessBox.getSelectedItem(), "Active", expiryDate, "Edit | Revoke | Lock"});
            logAction("Registered Card", "Card ID: " + cardID);
            updateDashboard();
        }
    }

    private void setupTableActions() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = table.columnAtPoint(e.getPoint());
                int row = table.rowAtPoint(e.getPoint());
                if (column == 5) { // คอลัมน์ Actions (index 5)
                    String actionText = table.getValueAt(row, column).toString();
                    int clickX = e.getX();
                    int actionIndex = getClickedActionIndex(table, row, column, clickX);

                    if (actionIndex == 0) {
                        editCard(row);
                    } else if (actionIndex == 1) {
                        revokeCard(row);
                    } else if (actionIndex == 2) {
                        lockCard(row);
                    }
                }
            }
        });
    }

    private int getClickedActionIndex(JTable table, int row, int column, int clickX) {
        FontMetrics fm = table.getFontMetrics(table.getFont());
        String actionText = table.getValueAt(row, column).toString();
        String[] actions = actionText.split("\\|");

        int startX = table.getCellRect(row, column, true).x;
        int currentX = startX;

        for (int i = 0; i < actions.length; i++) {
            int actionWidth = fm.stringWidth(actions[i].trim() + " ");
            if (clickX >= currentX && clickX <= currentX + actionWidth) {
                return i;
            }
            currentX += actionWidth;
        }
        return -1; // ไม่มีอะไรโดนคลิก
    }

    private void editCard(int row) {
        JOptionPane.showMessageDialog(null, "Editing card at row: " + row);
        // ใส่โค้ดแก้ไขบัตรที่นี่
    }

    private void revokeCard(int row) {
        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to revoke this card?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.setValueAt("Revoked", row, 3); // เปลี่ยนสถานะเป็น Revoked
            logAction("Revoked Card", "Card ID: " + tableModel.getValueAt(row, 0));
            updateDashboard();
        }
    }

    private void lockCard(int row) {
        tableModel.setValueAt("Locked", row, 3); // เปลี่ยนสถานะเป็น Locked
        logAction("Locked Card", "Card ID: " + tableModel.getValueAt(row, 0));
        updateDashboard();
    }

    public void logAction(String action, String details) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        auditTableModel.addRow(new Object[]{timestamp, action, details});
        logListModel.addElement(timestamp + " - " + action + " - " + details);
        saveAuditLog(timestamp, action, details);
    }

    private void saveAuditLog(String timestamp, String action, String details) {
        try (PrintWriter out = new PrintWriter(new FileWriter(AUDIT_LOG_FILE, true))) {
            out.println(timestamp + "," + action + "," + details);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void loadAuditLog() {
        try (BufferedReader reader = new BufferedReader(new FileReader(AUDIT_LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length == 3) {
                    auditTableModel.addRow(parts);
                    logListModel.addElement(parts[0] + " - " + parts[1] + " - " + parts[2]);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing audit log found.");
        }
    }

    private void updateDashboard() {
        int active = 0, revoked = 0, locked = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            switch (String.valueOf(tableModel.getValueAt(i, 3))) {
                case "Active" -> active++;
                case "Revoked" -> revoked++;
                case "Locked" -> locked++;
            }
        }
        activeCardsLabel.setText("Active Cards: " + active);
        revokedCardsLabel.setText("Revoked Cards: " + revoked);
        lockedCardsLabel.setText("Locked Cards: " + locked);
    }
}