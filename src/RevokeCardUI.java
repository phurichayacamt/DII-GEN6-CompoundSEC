package src;

import javax.swing.*;

public class RevokeCardUI extends JFrame {
    public RevokeCardUI() {
        setTitle("Revoke Card");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JTextField cardIDField = new JTextField();
        JButton revokeButton = new JButton("Revoke Card");

        revokeButton.addActionListener(e -> {
            String cardID = cardIDField.getText();
            try {
                AccessControlSystem.getInstance().wait();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            JOptionPane.showMessageDialog(this, "Card Revoked Successfully!");
        });

        add(new JLabel("Enter Card ID to Revoke:"));
        add(cardIDField);
        add(revokeButton);
    }
}

