package src.KingPhurichaya;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

    // ทำให้ HotelAccessGUI เป็น static class
    public  class HotelAccessGUI extends JFrame {
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
    private JButton confirmButton;


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
        JButton viewBookingsButton = createStyledButton("View Bookings");
        JButton exitButton = createStyledButton("Exit Program");
        BookingManager bookingManager = new BookingManager();
        viewBookingsButton.addActionListener(e -> bookingManager.loadBookings()); // ✅ ใส่ event
        navPanel.add(viewBookingsButton); // ✅ เพิ่มปุ่มเข้า Panel
        viewBookingsButton.addActionListener(e -> loadBookings());
        add(viewBookingsButton);
        exitButton.addActionListener(e -> System.exit(0));  // ปุ่มออกจากโปรแกรม


        navPanel.add(dashboardButton);
        navPanel.add(cardMgmtButton);
        navPanel.add(auditLogButton);
        navPanel.add(viewBookingsButton);
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
    private void acceptBooking(String bookingID) {
        updateBookingStatus(bookingID, "Accepted");
        JOptionPane.showMessageDialog(null, "การจอง " + bookingID + " ได้รับการยอมรับแล้ว!", "สำเร็จ", JOptionPane.INFORMATION_MESSAGE);
    }
    public void approveBooking(String bookingID) {
        System.out.println("Booking Approved: " + bookingID);
        JOptionPane.showMessageDialog(this, "Booking " + bookingID + " has been approved!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }


    private void rejectBooking(String bookingID) {
        updateBookingStatus(bookingID, "Rejected");
        JOptionPane.showMessageDialog(null, "การจอง " + bookingID + " ถูกปฏิเสธ!", "สำเร็จ", JOptionPane.INFORMATION_MESSAGE);
    }

    // ฟังก์ชันสำหรับอัปเดตสถานะการจองในไฟล์ CSV
    private void updateBookingStatus(String bookingID, String newStatus) {
        String filePath = "bookings.csv";
        List<String> updatedLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(bookingID)) {
                    parts[5] = newStatus; // สมมติว่า column 5 เป็นสถานะการจอง
                    line = String.join(",", parts);
                }
                updatedLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private void loadBookings() {
        StringBuilder bookings = new StringBuilder("Customer Bookings:\n");

        try (BufferedReader reader = new BufferedReader(new FileReader("bookings.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                bookings.append(line).append("\n");
            }
            JOptionPane.showMessageDialog(this, bookings.toString(), "Bookings", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadBookingsTable() {
        String[] columnNames = {"Booking ID", "Customer Name", "Room", "Check-in Date", "Check-out Date", "Status", "Actions"};
        DefaultTableModel bookingsTableModel = new DefaultTableModel(columnNames, 0);
        JTable bookingsTable = new JTable(bookingsTableModel);
        JScrollPane scrollPane = new JScrollPane(bookingsTable);

        File file = new File("bookings.csv");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "No bookings found!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    bookingsTableModel.addRow(new Object[]{parts[0], parts[1], parts[2], parts[3], parts[4], "Pending", "Approve | Reject"});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        bookingsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = bookingsTable.rowAtPoint(e.getPoint());
                int col = bookingsTable.columnAtPoint(e.getPoint());
                if (col == 6) { // Actions column
                    int actionIndex = getClickedActionIndex(bookingsTable, row, col, e.getX());
                    if (actionIndex == 0) {
                        String bookingID = (String) bookingsTable.getValueAt(row, 0); // ดึงค่าจากคอลัมน์แรก
                        approveBooking(bookingID);
                    } else if (actionIndex == 1) {
                        String bookingID = (String) bookingsTable.getValueAt(row, 0);
                        rejectBooking(bookingID);
                    }


                }
            }
        });

        JDialog bookingsDialog = new JDialog(this, "Customer Bookings", true);
        bookingsDialog.setSize(600, 400);
        bookingsDialog.add(scrollPane);
        bookingsDialog.setLocationRelativeTo(this);
        bookingsDialog.setVisible(true);
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
//    private void loadBookingsTable() {
//        String[] columnNames = {"Booking ID", "Customer Name", "Room", "Check-in Date", "Check-out Date", "Status", "Actions"};
//        DefaultTableModel bookingsTableModel = new DefaultTableModel(columnNames, 0);
//        JTable bookingsTable = new JTable(bookingsTableModel);
//        JScrollPane scrollPane = new JScrollPane(bookingsTable);
//
//        // โหลดข้อมูลการจองจากไฟล์
//        try (BufferedReader reader = new BufferedReader(new FileReader("bookings.csv"))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts.length >= 5) {
//                    bookingsTableModel.addRow(new Object[]{parts[0], parts[1], parts[2], parts[3], parts[4], "Pending", "Approve | Reject"});
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }






    }

