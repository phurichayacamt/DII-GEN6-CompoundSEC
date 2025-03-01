package src.KingPhurichaya;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BookingManager {
    private JTable table;
    private DefaultTableModel model;
    private String csvFile = "bookings.csv"; // ไฟล์ที่ใช้เก็บข้อมูล


    public void loadBookings() {
        JFrame frame = new JFrame("Bookings Management");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // สร้างหัวตาราง
        String[] columnNames = {"Floor", "Room", "Status", "Approve", "Reject"};
        model = new DefaultTableModel(columnNames, 0);

        // อ่านข้อมูลจาก CSV
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2) {
                    String status = (data.length == 3) ? data[2] : "Pending"; // ถ้ามีสถานะแล้วให้ใช้ ถ้าไม่มีให้ใส่ "Pending"
                    model.addRow(new Object[]{data[0], data[1], status, "Approve", "Reject"});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        table = new JTable(model);
        table.getColumn("Approve").setCellRenderer(new ButtonRenderer());
        table.getColumn("Approve").setCellEditor(new ButtonEditor(new JCheckBox(), "Approved"));

        table.getColumn("Reject").setCellRenderer(new ButtonRenderer());
        table.getColumn("Reject").setCellEditor(new ButtonEditor(new JCheckBox(), "Rejected"));

        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // Renderer สำหรับปุ่มใน JTable
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // Editor สำหรับให้แอดมินกดปุ่ม Approve หรือ Reject
    class ButtonEditor extends DefaultCellEditor {
        private String action;
        private boolean clicked;
        private JTable table;
        private int row;

        public ButtonEditor(JCheckBox checkBox, String action) {
            super(checkBox);
            this.action = action;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.table = table;
            this.row = row;
            JButton button = new JButton(action);
            button.addActionListener(this::actionPerformed);
            clicked = true;
            return button;
        }

        private void actionPerformed(ActionEvent e) {
            if (clicked) {
                if (action.equals("Approved")) {
                    approveBooking(row);
                } else {
                    rejectBooking(row);
                }
            }
            clicked = false;
            fireEditingStopped();
        }
    }

    // ✅ เมธอดอนุมัติการจอง
    private void approveBooking(int row) {
        model.setValueAt("Approved", row, 2); // เปลี่ยนสถานะใน JTable
        updateCSV(row, "Approved"); // อัปเดต CSV
    }

    // ❌ เมธอดปฏิเสธการจอง
    private void rejectBooking(int row) {
        model.setValueAt("Rejected", row, 2); // เปลี่ยนสถานะใน JTable
        updateCSV(row, "Rejected"); // อัปเดต CSV
    }

    // 📝 อัปเดตไฟล์ CSV เมื่อแอดมินกด Approve หรือ Reject
    private void updateCSV(int row, String status) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            int index = 0;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (index == row) {
                    lines.add(data[0] + "," + data[1] + "," + status); // เพิ่มสถานะ Approved หรือ Rejected
                } else {
                    lines.add(line);
                }
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // เขียนไฟล์ CSV ใหม่
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

