package src.KingPhurichaya;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;

public class HotelSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenu::new);
    }
}

class MainMenu extends JFrame {
    private JButton customerButton;
    private JButton adminButton;

    public MainMenu() {
        setTitle("Hotel Security System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 1, 10, 10));

        customerButton = new JButton("Customer ");
        adminButton = new JButton("Admin ");

        customerButton.setBackground(new Color(30, 144, 255));
        customerButton.setForeground(Color.BLACK);

        adminButton.setBackground(new Color(220, 20, 60));
        adminButton.setForeground(Color.BLACK);

        customerButton.addActionListener(this::openCustomerPanel);
        adminButton.addActionListener(this::openAdminPanel);

        add(customerButton);
        add(adminButton);

        setVisible(true);
    }

    private void openCustomerPanel(ActionEvent e) {
        dispose();
        new CustomerGUI();
    }

    private void openAdminPanel(ActionEvent e) {
        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(
                this,
                passwordField,
                "Enter Admin Password",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        if (option == JOptionPane.OK_OPTION) {
            String enteredPassword = new String(passwordField.getPassword());
            if ("admin123".equals(enteredPassword)) {
                new MainFrame(); // เปิดหน้าแอดมิน
                dispose(); // ปิด MainMenu หลังจากเปิดหน้าใหม่แล้ว
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect password!", "Access Denied", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

class CustomerGUI extends JFrame {
    private JTextField customerNameField;  // ✅ เพิ่มตัวแปรนี้
    private JComboBox<String> floorDropdown;
    private JComboBox<String> roomDropdown;
    private JLabel statusLabel;
    private JButton confirmButton;
    private JButton backButton;

    public CustomerGUI() {
        setTitle("Customer Panel");
        setSize(600, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel selectionPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        selectionPanel.add(new JLabel("Customer Name:"));
        customerNameField = new JTextField();
        selectionPanel.add(customerNameField);

        selectionPanel.add(new JLabel("Select Floor:"));
        String[] floors = {"1st Floor", "2nd Floor", "3rd Floor"};
        floorDropdown = new JComboBox<>(floors);
        selectionPanel.add(floorDropdown);

        selectionPanel.add(new JLabel("Select Room:"));
        roomDropdown = new JComboBox<>();
        updateRooms();
        selectionPanel.add(roomDropdown);

        statusLabel = new JLabel("Room Status: Unknown", SwingConstants.CENTER);
        selectionPanel.add(statusLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        confirmButton = new JButton("Confirm Selection");
        confirmButton.setBackground(new Color(34, 139, 34));
        confirmButton.setForeground(Color.BLACK);
        confirmButton.addActionListener(e -> confirmSelection());

        backButton = new JButton("Back to Main Menu");
        backButton.setBackground(new Color(255, 140, 0));
        backButton.setForeground(Color.BLACK);
        backButton.addActionListener(e -> goBackToMainMenu());

        buttonPanel.add(confirmButton);
        buttonPanel.add(backButton);

        add(selectionPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        floorDropdown.addActionListener(e -> updateRooms());

        setVisible(true);
    }

    private void confirmSelection() {
        String customerName = customerNameField.getText().trim();
        String selectedFloor = (String) floorDropdown.getSelectedItem();
        String selectedRoom = (String) roomDropdown.getSelectedItem();

        if (customerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter customer name.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedRoom == null) {
            JOptionPane.showMessageDialog(this, "Please select a room.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        saveBooking(customerName, selectedFloor, selectedRoom); // ✅ เพิ่มการบันทึกข้อมูล

        statusLabel.setText("Room " + selectedRoom + " on " + selectedFloor + " is booked by " + customerName);
        JOptionPane.showMessageDialog(this, "Room booked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateRooms() {
        roomDropdown.removeAllItems();
        String selectedFloor = (String) floorDropdown.getSelectedItem();
        if ("1st Floor".equals(selectedFloor)) {
            roomDropdown.addItem("Room 101");
            roomDropdown.addItem("Room 102");
        } else if ("2nd Floor".equals(selectedFloor)) {
            roomDropdown.addItem("Room 201");
            roomDropdown.addItem("Room 202");
        } else if ("3rd Floor".equals(selectedFloor)) {
            roomDropdown.addItem("Room 301");
            roomDropdown.addItem("Room 302");
        }
    }

    private void goBackToMainMenu() {
        dispose();
        new MainMenu();
    }

    private void saveBooking(String customerName, String floor, String room) { // ✅ บันทึกข้อมูลชื่อผู้จอง
        try (FileWriter writer = new FileWriter("bookings.csv", true)) {
            writer.append(customerName).append(",").append(floor).append(",").append(room).append("\n");
            writer.flush();
            System.out.println("Booking saved: " + customerName + " - " + floor + " - " + room);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
