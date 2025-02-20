package src;

import src.AccessControlSystem;

import java.awt.BorderLayout;
import javax.swing.*;
import java.util.*;
import java.text.SimpleDateFormat;


// Abstract class for Access Cards
abstract class Card {
    protected String cardID;
    protected String owner;
    protected boolean isActive;
    protected int failedAttempts;
    protected Date lastUsed;

    public Card(String cardID, String owner) {
        this.cardID = cardID;
        this.owner = owner;
        this.isActive = true;
        this.failedAttempts = 0;
        this.lastUsed = null;
    }

    public abstract boolean accessArea(String area);

    public void lockCard() {
        this.isActive = false;
    }

    public void unlockCard() {
        this.isActive = true;
        this.failedAttempts = 0;
    }

    public void logAttempt(boolean success) {
        if (success) {
            this.failedAttempts = 0;
            this.lastUsed = new Date();
        } else {
            this.failedAttempts++;
            if (this.failedAttempts >= 3) {
                lockCard();
            }
        }
    }

    public boolean isLocked() {
        return !isActive;
    }
}

// Admin Card - Full Access
class AdminCard extends Card {
    public AdminCard(String cardID, String owner) {
        super(cardID, owner);
    }

    @Override
    public boolean accessArea(String area) {
        if (!isActive) return false;
        logAttempt(true);
        return true;
    }
}

// Employee Card - Limited Access with Time Constraints
class EmployeeCard extends Card {
    private final Set<String> allowedAreas = new HashSet<>(Arrays.asList("Low Floor", "Medium Floor"));
    private final int startHour = 8, endHour = 18;

    public EmployeeCard(String cardID, String owner) {
        super(cardID, owner);
    }

    @Override
    public boolean accessArea(String area) {
        if (!isActive || !allowedAreas.contains(area)) {
            logAttempt(false);
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour < startHour || hour >= endHour) {
            logAttempt(false);
            return false;
        }
        logAttempt(true);
        return true;
    }
}

// Emergency Card - One-Time Use Full Access
class EmergencyCard extends Card {
    private boolean used = false;

    public EmergencyCard(String cardID, String owner) {
        super(cardID, owner);
    }

    @Override
    public boolean accessArea(String area) {
        if (!isActive || used) {
            logAttempt(false);
            return false;
        }
        logAttempt(true);
        used = true;
        lockCard();
        return true;
    }
}

// Access Control System with Timestamp Logging
class AccessControlSystem {
    private final Map<String, Card> cardDatabase = new HashMap<>();
    private final List<String> accessLogs = new ArrayList<>();
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static Object getInstance() {
        return null;
    }


    public void registerCard(Card card) {
        cardDatabase.put(card.cardID, card);
    }

    public boolean accessRequest(String cardID, String area) {
        Card card = cardDatabase.get(cardID);
        if (card != null) {
            boolean access = card.accessArea(area);
            String timestamp = timeFormat.format(new Date());
            accessLogs.add(timestamp + " - Card " + cardID + " attempted access to " + area + " - " + (access ? "GRANTED" : "DENIED"));
            return access;
        }
        return false;
    }

    public List<String> getAccessLogs() {
        return accessLogs;
    }

    public List<String> getLogs() {

        return List.of();
    }
}

// GUI for Access Control System
class AccessControlGUI extends JFrame {
    private final AccessControlSystem system;
    private final JTextArea displayArea;

    public AccessControlGUI() {
        system = new AccessControlSystem();
        setTitle("Access Control Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        JButton addAdmin = new JButton("Add Admin Card");
        JButton addEmployee = new JButton("Add Employee Card");
        JButton accessTest = new JButton("Test Access");
        JButton showLogs = new JButton("Show Logs");

        panel.add(addAdmin);
        panel.add(addEmployee);
        panel.add(accessTest);
        panel.add(showLogs);
        add(panel, BorderLayout.SOUTH);

        addAdmin.addActionListener(e -> {
            AdminCard card = new AdminCard("A001", "Admin User");
            system.registerCard(card);
            displayArea.append("Admin Card A001 added.\n");
        });

        addEmployee.addActionListener(e -> {
            EmployeeCard card = new EmployeeCard("E001", "Employee User");
            system.registerCard(card);
            displayArea.append("Employee Card E001 added.\n");
        });

        accessTest.addActionListener(e -> {
            boolean access = (boolean) system.accessRequest("E001", "Low Floor");
            displayArea.append("Access attempt for Employee E001: " + (access ? "GRANTED" : "DENIED") + "\n");
        });

        showLogs.addActionListener(e -> {
            displayArea.append("Access Logs:\n");
            for (String log : system.getAccessLogs()) {
                displayArea.append(log + "\n");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AccessControlGUI().setVisible(true));
    }
}