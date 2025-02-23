//package src;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.util.*;
//import java.io.*;
//
//
//class Card implements Serializable {
//    private String cardID;
//    private String accessLevel;
//    private String ownerName;
//    private String position;
//    private String encryptedID;
//
//    public Card(String cardID, String accessLevel, String ownerName, String position) {
//        this.cardID = cardID;
//        this.accessLevel = accessLevel;
//        this.ownerName = ownerName;
//        this.position = position;
//        this.encryptedID = encryptCardID(cardID);
//    }
//
//    private String encryptCardID(String cardID) {
//        return Base64.getEncoder().encodeToString(cardID.getBytes());
//    }
//
//    public String getCardID() { return cardID; }
//    public String getAccessLevel() { return accessLevel; }
//    public String getOwnerName() { return ownerName; }
//    public String getPosition() { return position; }
//    public String getEncryptedID() { return encryptedID; }
//}
//
//public class HotelAccessControl extends JFrame {
//    private Map<String, Card> registeredCards = new HashMap<>();
//    private final String DATA_FILE = "cards.dat";
//    private static final String DEFAULT_USERNAME = "admin";
//    private static final String DEFAULT_PASSWORD = "1234";
//
//    public HotelAccessControl() {
//        setTitle("Hotel Access Control");
//        setSize(700, 450);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLayout(new BorderLayout());
//
//        loadCardsFromFile();
//
//        JLabel titleLabel = new JLabel("Welcome to Hotel Access Control", JLabel.CENTER);
//        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
//        titleLabel.setOpaque(true);
//        titleLabel.setBackground(Color.BLUE);
//        titleLabel.setForeground(Color.WHITE);
//        add(titleLabel, BorderLayout.NORTH);
//
//        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
//        JButton accessBtn = new JButton("Access System");
//        JButton registerBtn = new JButton("Register Card");
//        JButton revokeBtn = new JButton("Revoke Card");
//        JButton logsBtn = new JButton("View Logs");
//        JButton countBtn = new JButton("Card Count");
//        JButton saveBtn = new JButton("Save Data");
//
//        panel.add(accessBtn);
//        panel.add(registerBtn);
//        panel.add(revokeBtn);
//        panel.add(logsBtn);
//        panel.add(countBtn);
//        panel.add(saveBtn);
//        add(panel, BorderLayout.CENTER);
//
//        JLabel footerLabel = new JLabel("\u00A9 2025 Hotel Security System", JLabel.CENTER);
//        add(footerLabel, BorderLayout.SOUTH);
//
//        accessBtn.addActionListener(e -> accessSystem());
//        registerBtn.addActionListener(e -> registerCard());
//        revokeBtn.addActionListener(e -> revokeCard());
//        logsBtn.addActionListener(e -> viewLogs());
//        countBtn.addActionListener(e -> showCardCount());
//        saveBtn.addActionListener(e -> saveCardsToFile());
//    }
//
//    private void accessSystem() {
//        String cardID = JOptionPane.showInputDialog(this, "Enter Card ID:");
//        if (cardID != null && registeredCards.containsKey(cardID)) {
//            Card card = registeredCards.get(cardID);
//            JOptionPane.showMessageDialog(this, "Access Granted!\nCard ID: " + cardID +
//                    "\nOwner: " + card.getOwnerName() +
//                    "\nPosition: " + card.getPosition() +
//                    "\nLevel: " + card.getAccessLevel());
//        } else {
//            JOptionPane.showMessageDialog(this, "Access Denied! Card not found.");
//        }
//    }
//
//    private void registerCard() {
//        String cardID = JOptionPane.showInputDialog(this, "Enter New Card ID:");
//        String ownerName = JOptionPane.showInputDialog(this, "Enter Owner Name:");
//        String[] positions = {"บัตรแอดมิน", "บัตรฉุกเฉิน", "บัตรพนักงาน"};
//        String position = (String) JOptionPane.showInputDialog(this, "Select Position:", "Position",
//                JOptionPane.QUESTION_MESSAGE, null, positions, positions[0]);
//
//        String[] levels = {"Low1", "Medium2", "High3","All"};
//        String accessLevel = (String) JOptionPane.showInputDialog(this, "Select Access Level:", "Access Level",
//                JOptionPane.QUESTION_MESSAGE, null, levels, levels[0]);
//
//        if (cardID != null && ownerName != null && position != null && accessLevel != null &&
//                !cardID.trim().isEmpty() && !registeredCards.containsKey(cardID)) {
//            registeredCards.put(cardID, new Card(cardID, accessLevel, ownerName, position));
//            JOptionPane.showMessageDialog(this, "Card Registered Successfully!\nCard ID: " + cardID +
//                    "\nOwner: " + ownerName +
//                    "\nPosition: " + position +
//                    "\nLevel: " + accessLevel);
//            saveCardsToFile();
//        } else {
//            JOptionPane.showMessageDialog(this, "Invalid or Duplicate Card ID!");
//        }
//    }
//
//    private void revokeCard() {
//        String cardID = JOptionPane.showInputDialog(this, "Enter Card ID to Revoke:");
//        if (cardID != null && registeredCards.remove(cardID) != null) {
//            JOptionPane.showMessageDialog(this, "Card Revoked Successfully! Card ID: " + cardID);
//            saveCardsToFile();
//        } else {
//            JOptionPane.showMessageDialog(this, "Card ID not found!");
//        }
//    }
//
//    private void viewLogs() {
//        JOptionPane.showMessageDialog(this, "Logs:\n (ยังไม่มีระบบ Log)");
//    }
//
//    private void showCardCount() {
//        JOptionPane.showMessageDialog(this, "Total Registered Cards: " + registeredCards.size());
//    }
//
//    private void saveCardsToFile() {
//        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
//            out.writeObject(registeredCards);
//            JOptionPane.showMessageDialog(this, "Data saved successfully!");
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(this, "Error saving data!");
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    private void loadCardsFromFile() {
//        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
//            registeredCards = (HashMap<String, Card>) in.readObject();
//        } catch (IOException | ClassNotFoundException e) {
//            registeredCards = new HashMap<>();
//        }
//    }
//
//    private static boolean login() {
//        int attempts = 0;
//        while (attempts < 3) {
//            String username = JOptionPane.showInputDialog("Enter Username:");
//            String password = JOptionPane.showInputDialog("Enter Password:");
//
//            if (DEFAULT_USERNAME.equals(username) && DEFAULT_PASSWORD.equals(password)) {
//                return true;
//            } else {
//                JOptionPane.showMessageDialog(null, "Invalid Username or Password! Try again.");
//                attempts++;
//            }
//        }
//        JOptionPane.showMessageDialog(null, "Too many failed attempts! Exiting system.");
//        System.exit(0);
//        return false;
//    }
//
//    public static void main(String[] args) {
//        if (login()) {
//            SwingUtilities.invokeLater(() -> {
//                HotelAccessControl frame = new HotelAccessControl();
//                frame.setVisible(true);
//            });
//        }
//    }
//}
