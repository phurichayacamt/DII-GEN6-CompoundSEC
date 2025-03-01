package src.KingPhurichaya;

import javax.swing.*;
import java.awt.*;

public class CustomerPanel extends JFrame {
    public CustomerPanel() {
        setTitle("Customer Panel");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Welcome, Customer!");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }
}
