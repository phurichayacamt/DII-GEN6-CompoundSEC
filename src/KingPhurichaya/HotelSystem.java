
package src.KingPhurichaya;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

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

        customerButton = new JButton("Customer Panel");
        adminButton = new JButton("Admin Panel");

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
                    new HotelAccessGUI(); // เปิดหน้าแอดมิน
                    dispose(); // ปิด MainMenu หลังจากเปิดหน้าใหม่แล้ว
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect password!", "Access Denied", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private boolean validateAdminPassword(String password) {
        return "admin123".equals(password); // เปลี่ยนรหัสผ่านตามต้องการ
    }

    class CustomerGUI extends JFrame {
        private JComboBox<String> floorDropdown;
        private JComboBox<String> roomDropdown;
        private JLabel statusLabel;

        public CustomerGUI() {
            setTitle("Customer Panel");
            setSize(600, 300);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            JPanel selectionPanel = new JPanel(new GridLayout(3, 2, 10, 10));

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

            JButton confirmButton = new JButton("Confirm Selection");
            confirmButton.setBackground(new Color(34, 139, 34));
            confirmButton.setForeground(Color.BLACK);
            confirmButton.addActionListener(e -> confirmSelection());

            JButton backButton = new JButton("Back to Main Menu");
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

        private void confirmSelection() {
            String floor = (String) floorDropdown.getSelectedItem();
            String room = (String) roomDropdown.getSelectedItem();
            if (room == null) {
                statusLabel.setText("Please select a room!");
                return;
            }
            statusLabel.setText("Room " + room + " selected on " + floor);
        }


    }
}

