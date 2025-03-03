package src.KingPhurichaya;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

//Multi-Facade
public class BookingManager {
    private static final String CSV_FILE = "bookings.csv";  // Encapsulation CSV_FILE, model, table ป็น private → ซ่อนจากภายนอก
    private DefaultTableModel model;
    private JTable table;
    private MainFrame mainFrame;
    private JFrame frame;

    public BookingManager(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void showBookings() {
        if (frame != null && frame.isVisible()) {
            frame.toFront();
            return;
        }
        loadBookings();
    }

    public boolean isRoomAlreadyBooked(String room) {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2 && data[1].equals(room)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addBooking(String floor, String room, String customerName) {
        if (isRoomAlreadyBooked(room)) {
            JOptionPane.showMessageDialog(null, "Room " + room + " is already booked!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE, true))) {
            bw.write(floor + "," + room + "," + customerName + ",Pending"); // เพิ่มสถานะเริ่มต้นเป็น Pending
            bw.newLine();
            JOptionPane.showMessageDialog(null, "Booking successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void loadBookings() {
        frame = new JFrame("Bookings Management");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        String[] columnNames = {"Floor", "Room", "Customer Name", "Status", "Approve", "Reject"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String status = (data.length == 4) ? data[3] : "Pending";
                model.addRow(new Object[]{data[0], data[1], data[2], status, "Approve", "Reject"});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        table.getColumn("Approve").setCellRenderer(new ButtonRenderer());
        table.getColumn("Approve").setCellEditor(new ButtonEditor("Approved"));
        table.getColumn("Reject").setCellRenderer(new ButtonRenderer());
        table.getColumn("Reject").setCellEditor(new ButtonEditor("Rejected"));

        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> frame.dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void updateBookingStatus(int row, String status) {
        model.setValueAt(status, row, 3);
        saveUpdatedBookings();
        mainFrame.updateBookingStats(); // ✅ อัปเดตแดชบอร์ด
    }

    private void saveUpdatedBookings() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            int index = 0;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (index < model.getRowCount()) {
                    lines.add(data[0] + "," + data[1] + "," + data[2] + "," + model.getValueAt(index, 3));
                } else {
                    lines.add(line);
                }
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override //Overriding
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String action;
        private boolean clicked;
        private int row;

        public ButtonEditor(String action) {
            super(new JCheckBox());
            this.action = action;
            button = new JButton(action);
            button.addActionListener(this::actionPerformed);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            clicked = true;
            return button;
        }

        private void actionPerformed(ActionEvent e) {
            if (clicked) {
                updateBookingStatus(row, action);
            }
            clicked = false;
            fireEditingStopped();
        }
    }
}