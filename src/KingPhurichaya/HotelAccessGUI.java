package src.KingPhurichaya;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HotelAccessGUI extends JFrame {
    public HotelAccessGUI() {
        setTitle("Admin Panel");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HotelAccessGUI::new);
    }
}

// MainFrame เป็นหน้าจอหลักของระบบ Hotel Security System
class MainFrame extends JFrame {
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final DefaultTableModel auditTableModel;
    private final JTable auditTable;
    private final JLabel activeCardsLabel;
    private final JLabel revokedCardsLabel;
    private final JLabel lockedCardsLabel;
    private final DefaultListModel<String> logListModel;

    // เพิ่ม Label สำหรับสถิติการจอง
    private final JLabel approvedBookingsLabel;
    private final JLabel rejectedBookingsLabel;

    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    private static final String AUDIT_LOG_FILE = "audit_log.csv";

    // BookingManager ถูกสร้างโดยส่ง this เข้าไป
    private BookingManager bookingManager;


    public MainFrame() {
        setTitle("Hotel Security System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(243, 244, 246));

        // Navigation Panel
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        navPanel.setBackground(new Color(243, 244, 246));

        JButton dashboardButton = createStyledButton("Dashboard");
        JButton cardMgmtButton = createStyledButton("Card Management");
        JButton auditLogButton = createStyledButton("Audit Log");
        JButton viewBookingsButton = createStyledButton("View Bookings");
        JButton exitButton = createStyledButton("Exit Program");

        // สร้าง BookingManager โดยส่ง MainFrame (this)
        bookingManager = new BookingManager(this);

        viewBookingsButton.addActionListener(e -> bookingManager.loadBookings());

        exitButton.addActionListener(e -> System.exit(0));

        navPanel.add(dashboardButton);
        navPanel.add(cardMgmtButton);
        navPanel.add(auditLogButton);
        navPanel.add(viewBookingsButton);
        navPanel.add(exitButton);

        panel.add(navPanel, BorderLayout.NORTH);

        // Main Panel สำหรับ CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        panel.add(mainPanel, BorderLayout.CENTER);

        // ===== Dashboard Panel ===== //
        JPanel dashboardPanel = new JPanel(new BorderLayout(0, 20));
        dashboardPanel.setBackground(new Color(220, 230, 240)); // สีพื้นหลังของ Dashboard

        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 20, 10));
        statsPanel.setOpaque(false);

        activeCardsLabel = createStatsLabel("Active Cards: 0");
        revokedCardsLabel = createStatsLabel("Revoked Cards: 0");
        lockedCardsLabel = createStatsLabel("Locked Cards: 0");
        approvedBookingsLabel = createStatsLabel("Approved Bookings: 0");
        rejectedBookingsLabel = createStatsLabel("Rejected Bookings: 0");

        statsPanel.add(activeCardsLabel);
        statsPanel.add(revokedCardsLabel);
        statsPanel.add(lockedCardsLabel);
        statsPanel.add(approvedBookingsLabel);
        statsPanel.add(rejectedBookingsLabel);

        logListModel = new DefaultListModel<>();
        JList<String> logList = new JList<>(logListModel);
        JScrollPane logScrollPane = new JScrollPane(logList);

        dashboardPanel.add(statsPanel, BorderLayout.NORTH);
        dashboardPanel.add(logScrollPane, BorderLayout.CENTER);
        mainPanel.add(dashboardPanel, "Dashboard");

        // ===== Card Management Panel ===== //
        String[] columnNames = {"Card ID", "Name", "Access Level", "Status", "Expiry Date", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);

        JButton addCardButton = createStyledButton(" + Add New Card ! ");
        addCardButton.addActionListener(this::addNewCard);

        JPanel cardMgmtPanel = new JPanel(new BorderLayout(0, 20));
        cardMgmtPanel.add(tableScrollPane, BorderLayout.CENTER);
        cardMgmtPanel.add(addCardButton, BorderLayout.SOUTH);
        mainPanel.add(cardMgmtPanel, "Card Management");

        // ===== Audit Log Panel ===== //
        auditTableModel = new DefaultTableModel(new String[]{"Timestamp", "Action", "Details"}, 0);
        auditTable = new JTable(auditTableModel);
        JScrollPane auditScrollPane = new JScrollPane(auditTable);

        JPanel auditPanel = new JPanel(new BorderLayout());
        auditPanel.setBackground(new Color(250, 245, 235)); // สีพื้นหลังของ Audit Log
        auditPanel.add(auditScrollPane, BorderLayout.CENTER);
        mainPanel.add(auditPanel, "Audit Log");

        add(panel);
        setupTableActions();
        loadAuditLog();

        dashboardButton.addActionListener(e -> showPanel("Dashboard", dashboardButton, cardMgmtButton, auditLogButton));
        cardMgmtButton.addActionListener(e -> showPanel("Card Management", cardMgmtButton, dashboardButton, auditLogButton));
        auditLogButton.addActionListener(e -> showPanel("Audit Log", auditLogButton, dashboardButton, cardMgmtButton));

        setVisible(true);
    }

    // อัปเดตสถิติการจอง //
    public void updateBookingStats() {
        int approvedCount = 0, rejectedCount = 0;
        File file = new File("bookings.csv");
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length >= 4) {
                        String status = data[3];
                        if ("Approved".equalsIgnoreCase(status)) {
                            approvedCount++;
                        } else if ("Rejected".equalsIgnoreCase(status)) {
                            rejectedCount++;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        approvedBookingsLabel.setText("Approved Bookings: " + approvedCount);
        rejectedBookingsLabel.setText("Rejected Bookings: " + rejectedCount);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 40));
        return button;
    }

    private JLabel createStatsLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        return label;
    }

    private void showPanel(String panelName, JButton active, JButton... others) {
        cardLayout.show(mainPanel, panelName);
        active.setBackground(Color.GRAY);
        for (JButton btn : others) {
            btn.setBackground(null);
        }
    }

    private void addNewCard(ActionEvent e) {
        // Map ระบุ Accessible Floors ตาม Access Level
        Map<String, String> accessFloorsMap = new HashMap<>();
        accessFloorsMap.put("Admin", "Low, Medium, High");
        accessFloorsMap.put("Employee", "Low, Medium");
        accessFloorsMap.put("Guest", "Low");
        accessFloorsMap.put("VIP", "Low, Medium, High");
        accessFloorsMap.put("Maintenance", "Low, Medium");
        accessFloorsMap.put("Cleaning", "Low");

        JTextField cardIDField = new JTextField();
        JTextField ownerField = new JTextField();
        JComboBox<String> accessBox = new JComboBox<>(new String[]{
                "Admin","Employee","Guest","VIP","Maintenance","Cleaning"
        });
        JTextField expiryField = new JTextField();

        JLabel floorsLabel = new JLabel("Floors: " + accessFloorsMap.get("Admin"));
        accessBox.addActionListener(ev -> {
            String selectedLevel = (String) accessBox.getSelectedItem();
            floorsLabel.setText("Floors: " + accessFloorsMap.get(selectedLevel));
        });

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.add(new JLabel("Card ID:"));
        inputPanel.add(cardIDField);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(ownerField);
        inputPanel.add(new JLabel("Access Level:"));
        inputPanel.add(accessBox);
        inputPanel.add(new JLabel("Expiry Date (YYYY-MM-DD):"));
        inputPanel.add(expiryField);
        inputPanel.add(new JLabel("Accessible Floors:"));
        inputPanel.add(floorsLabel);

        int result = JOptionPane.showConfirmDialog(this, inputPanel,
                "Register New Card", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String cardID = cardIDField.getText().trim();
            String owner = ownerField.getText().trim();
            String expiryDate = expiryField.getText().trim();

            if (cardID.isEmpty() || owner.isEmpty() || expiryDate.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill out all fields.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // ตรวจสอบว่า Card ID ซ้ำหรือไม่ //
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String existingCardID = tableModel.getValueAt(i, 0).toString();
                if (cardID.equals(existingCardID)) {
                    JOptionPane.showMessageDialog(this, "This Card ID has already been used.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (!expiryDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, "Please enter the date in YYYY-MM-DD format.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // เข้ารหัส Expiry Date ก่อนบันทึก //
            String encryptedExpiry = TimeBasedEncryption.encrypt(expiryDate);

            tableModel.addRow(new Object[]{
                    cardID, owner, accessBox.getSelectedItem(),
                    "Active", encryptedExpiry, "Edit | Revoke | Lock"
            });
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
                if (column == 5) { // Actions column
                    int actionIndex = getClickedActionIndex(table, row, column, e.getX());
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
        return -1;
    }

    private void editCard(int row) {
        if (row < 0 || row >= tableModel.getRowCount()) {
            JOptionPane.showMessageDialog(null, "Invalid row selected.");
            return;
        }
        try {
            String cardID = tableModel.getValueAt(row, 0).toString();
            String name = tableModel.getValueAt(row, 1).toString();
            String accessLevel = tableModel.getValueAt(row, 2).toString();
            String status = tableModel.getValueAt(row, 3).toString();

            // ถอดรหัส Expiry Date //
            String encryptedExpiryDate = tableModel.getValueAt(row, 4).toString();
            String expiryDate = TimeBasedEncryption.decrypt(encryptedExpiryDate);

            JDialog editDialog = new JDialog(this, "Edit Card " + cardID, true);
            editDialog.setLayout(new BorderLayout());
            editDialog.setSize(400, 250);
            editDialog.setLocationRelativeTo(null);

            JPanel mainPanel = new JPanel(new GridLayout(5, 2, 10, 10));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            mainPanel.add(new JLabel("Card ID:"));
            JTextField cardIDField = new JTextField(cardID);
            cardIDField.setEditable(false);
            mainPanel.add(cardIDField);

            mainPanel.add(new JLabel("Name:"));
            JTextField nameField = new JTextField(name);
            mainPanel.add(nameField);

            mainPanel.add(new JLabel("Access Level:"));
            JComboBox<String> accessBox = new JComboBox<>(new String[]{
                    "Admin","Employee","Guest","VIP","Emergency","Cleaning"
            });
            accessBox.setSelectedItem(accessLevel);
            mainPanel.add(accessBox);

            mainPanel.add(new JLabel("Status:"));
            JComboBox<String> statusBox = new JComboBox<>(new String[]{
                    "Active","Inactive","Revoked","Locked"
            });
            statusBox.setSelectedItem(status);
            mainPanel.add(statusBox);

            mainPanel.add(new JLabel("Expiry Date (YYYY-MM-DD):"));
            JTextField expiryField = new JTextField(expiryDate);
            mainPanel.add(expiryField);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton saveButton = new JButton("Save");
            JButton cancelButton = new JButton("Cancel");
            saveButton.setPreferredSize(new Dimension(100, 30));
            cancelButton.setPreferredSize(new Dimension(100, 30));
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);

            saveButton.addActionListener(e -> {
                tableModel.setValueAt(nameField.getText(), row, 1);
                tableModel.setValueAt(accessBox.getSelectedItem(), row, 2);
                tableModel.setValueAt(statusBox.getSelectedItem(), row, 3);

                String newExpiry = expiryField.getText();
                String encryptedNewExpiry = TimeBasedEncryption.encrypt(newExpiry);
                tableModel.setValueAt(encryptedNewExpiry, row, 4);

                logAction("Edited Card", "Card ID: " + cardID);
                updateDashboard();
                editDialog.dispose();
            });
            cancelButton.addActionListener(e -> editDialog.dispose());

            editDialog.add(mainPanel, BorderLayout.CENTER);
            editDialog.add(buttonPanel, BorderLayout.SOUTH);
            editDialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error editing card: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void revokeCard(int row) {
        int confirm = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to revoke this card?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.setValueAt("Revoked", row, 3);
            logAction("Revoked Card", "Card ID: " + tableModel.getValueAt(row, 0));
            updateDashboard();
        }
    }

    private void lockCard(int row) {
        tableModel.setValueAt("Locked", row, 3);
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
        File file = new File(AUDIT_LOG_FILE);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length == 3) {
                    auditTableModel.addRow(parts);
                    logListModel.addElement(parts[0] + " - " + parts[1] + " - " + parts[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // อัปเดตสถิติบน Dashboard (Active, Revoked, Locked) //
    public void updateDashboard() {
        int active = 0, revoked = 0, locked = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String status = String.valueOf(tableModel.getValueAt(i, 3));
            switch (status) {
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
