//package src.KingPhurichaya;
//
//import javax.swing.*;
//        import java.awt.*;
//
//import static java.lang.String.join;
//
//class UI extends JFrame {
//    public UI() {
//        setTitle("Access Logs");
//        setSize(500, 400);
//        setLocationRelativeTo(null);
//        setLayout(new BorderLayout());
//
//        // ดึงข้อมูลจากระบบ
//        Class<?> logs = accessControlSystem.getInstance().getClass();
//
//
//        // แปลงเป็นข้อความแสดงผล
//        JTextArea logsArea = new JTextArea();
//        logsArea.setEditable(false);
//        logsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
//        logsArea.setText(join("\n", logs));
//
//        // ใส่ Scroll เพื่อเลื่อนดู
//        JScrollPane scrollPane = new JScrollPane(logsArea);
//        add(scrollPane, BorderLayout.CENTER);
//
//        // ปุ่มปิด
//        JButton closeButton = new JButton("Close");
//        closeButton.addActionListener(e -> dispose());
//
//        JPanel bottomPanel = new JPanel();
//        bottomPanel.add(closeButton);
//        add(bottomPanel, BorderLayout.SOUTH);
//    }
//
//    private String join(String lineBreak, Class<?> logs) {
//        return null;
//    }
//}
