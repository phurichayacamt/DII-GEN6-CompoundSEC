//package src.KingPhurichaya;
//
//
//import javax.swing.*;
//import java.util.Calendar;
//
//public class AccessUI extends JFrame {
//    public AccessUI() {
//        setTitle("Access System");
//        setSize(400, 300);
//        setLocationRelativeTo(null);
//        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
//
//        JTextField cardIDField = new JTextField();
//        JTextField areaField = new JTextField();
//        JButton checkButton = new JButton("Check Access");
//        JTextArea resultArea = new JTextArea();
//        resultArea.setEditable(false);
//
//        checkButton.addActionListener(e -> {
//            String cardID = cardIDField.getText();
//            String area = areaField.getText();
//            Calendar accessControlSystem = null;
//            boolean access = accessControlSystem.getInstance().equals(cardID);
//            resultArea.setText("Access " + (access ? "GRANTED" : "DENIED"));
//        });
//
//        add(new JLabel("Card ID:"));
//        add(cardIDField);
//        add(new JLabel("Area:"));
//        add(areaField);
//        add(checkButton);
//        add(resultArea);
//    }
//}
