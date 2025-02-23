//package src.KingPhurichaya;
//
//import javax.swing.*;
//
//
//public class RegisterCardUI extends JFrame {
//    public RegisterCardUI() {
//        setTitle("Register New Card");
//        setSize(400, 300);
//        setLocationRelativeTo(null);
//        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
//
//        JTextField cardIDField = new JTextField();
//        JTextField ownerField = new JTextField();
//        String[] types = {"Admin", "Employee", "Emergency"};
//        JComboBox<String> typeDropdown = new JComboBox<>(types);
//        JButton registerButton = new JButton("Register");
//
//        registerButton.addActionListener(e -> {
//            String cardID = cardIDField.getText();
//            String owner = ownerField.getText();
//            String type = (String) typeDropdown.getSelectedItem();
//
//            Card newCard;
//            switch (type) {
//                case "Admin":
//                    newCard = new AdminCard(cardID, owner);
//                    break;
//                case "Employee":
//                    newCard = new EmployeeCard(cardID, owner);
//                    break;
//                case "Emergency":
//                    newCard = new EmergencyCard(cardID, owner);
//                    break;
//                default:
//                    return;
//            }
//
//            accessControlSystem.getInstance().getClass();
//            JOptionPane.showMessageDialog(this, "Card Registered Successfully!");
//        });
//
//        add(new JLabel("Card ID:"));
//        add(cardIDField);
//        add(new JLabel("Owner Name:"));
//        add(ownerField);
//        add(new JLabel("Card Type:"));
//        add(typeDropdown);
//        add(registerButton);
//    }
//}
//
