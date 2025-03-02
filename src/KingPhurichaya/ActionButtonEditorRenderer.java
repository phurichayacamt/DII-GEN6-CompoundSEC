package src.KingPhurichaya;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.EventObject;

public class ActionButtonEditorRenderer {
    // เหลือ 3 อาร์กิวเมนต์ (JTable, DefaultTableModel, MainFrame)
    public static void addActionButtons(JTable table, DefaultTableModel tableModel, MainFrame mainFrame) {
        table.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        table.getColumn("Actions").setCellEditor(new ButtonEditor(tableModel, mainFrame));
    }
}

// ✅ Renderer - Displays buttons in each row
class ButtonRenderer extends JPanel implements TableCellRenderer {
    public ButtonRenderer() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0)); // Set spacing between buttons

        JButton editButton = createButton("Edit");
        JButton revokeButton = createButton("Revoke");
        JButton lockButton = createButton("Lock");


        add(editButton);
        add(revokeButton);
        add(lockButton);

    }


    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(70, 30)); // Changed from 15 to 30 to match Editor
        return button;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this; // Return the panel containing buttons
    }
}

// ✅ Editor - Makes buttons clickable
class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    private final JButton editButton = new JButton("Edit");
    private final JButton revokeButton = new JButton("Revoke");
    private final JButton lockButton = new JButton("Lock");
    private final DefaultTableModel tableModel;

    private final MainFrame mainFrame;
    private int row;

    public ButtonEditor(DefaultTableModel tableModel, MainFrame mainFrame) {
        this.tableModel = tableModel;
        this.mainFrame = mainFrame;

        editButton.setPreferredSize(new Dimension(70, 30));
        revokeButton.setPreferredSize(new Dimension(70, 30));
        lockButton.setPreferredSize(new Dimension(70, 30));

        // Fixed action listeners to properly handle events
        editButton.addActionListener(e -> {
            System.out.println("Edit button clicked for row: " + row); // Debug output
            editCard();
        });

        revokeButton.addActionListener(e -> {
            System.out.println("Revoke button clicked for row: " + row); // Debug output
            updateCardStatus("Revoked");
        });

        lockButton.addActionListener(e -> {
            System.out.println("Lock button clicked for row: " + row); // Debug output
            updateCardStatus("Locked");
        });

        panel.add(editButton);
        panel.add(revokeButton);
        panel.add(lockButton);
    }

    private void editCard() {
        if (row < 0 || row >= tableModel.getRowCount()) {
            System.out.println("Invalid row index: " + row);
            fireEditingStopped();
            return;
        }

        try {
            String cardID = tableModel.getValueAt(row, 0).toString();
            String name = tableModel.getValueAt(row, 1).toString();
            String accessLevel = tableModel.getValueAt(row, 2).toString();
            String status = tableModel.getValueAt(row, 3).toString();
            String encryptedExpiryDate = tableModel.getValueAt(row, 4).toString();
            String expiryDate = TimeBasedEncryption.decrypt(encryptedExpiryDate);


            // สร้าง JDialog แบบกำหนดเองแทนการใช้ JOptionPane
            JDialog editDialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(panel), "Edit Card " + cardID, true);
            editDialog.setLayout(new BorderLayout());
            editDialog.setSize(400, 250);
            editDialog.setLocationRelativeTo(null);  // แสดงตรงกลางหน้าจอ

            // สร้าง Panel หลักสำหรับข้อมูล
            JPanel mainPanel = new JPanel(new GridLayout(5, 2, 10, 10));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // สร้าง Components สำหรับการแก้ไขข้อมูล
            JLabel cardIDLabel = new JLabel("Card ID:");
            JTextField cardIDField = new JTextField(cardID);
            cardIDField.setEditable(false);  // ไม่ให้แก้ไข Card ID

            JLabel nameLabel = new JLabel("Name:");
            JTextField nameField = new JTextField(name);

            JLabel accessLabel = new JLabel("Access Level:");
            JComboBox<String> accessBox = new JComboBox<>(new String[]{"Admin", "Employee", "Guest", "VIP", "Maintenance", "Cleaning"});
            accessBox.setSelectedItem(accessLevel);

            JLabel statusLabel = new JLabel("Status:");
            JComboBox<String> statusBox = new JComboBox<>(new String[]{"Active", "Inactive", "Revoked", "Locked"});
            statusBox.setSelectedItem(status);

            JLabel expiryLabel = new JLabel("Expiry Date (YYYY-MM-DD):");
            JTextField expiryField = new JTextField(expiryDate);

            // เพิ่ม Components ลงใน Panel
            mainPanel.add(cardIDLabel);
            mainPanel.add(cardIDField);
            mainPanel.add(nameLabel);
            mainPanel.add(nameField);
            mainPanel.add(accessLabel);
            mainPanel.add(accessBox);
            mainPanel.add(statusLabel);
            mainPanel.add(statusBox);
            mainPanel.add(expiryLabel);
            mainPanel.add(expiryField);

            // สร้าง Panel สำหรับปุ่ม
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton saveButton = new JButton("Save");
            JButton cancelButton = new JButton("Cancel");

            // ปรับแต่งปุ่มให้ดูดีขึ้น
            saveButton.setPreferredSize(new Dimension(100, 30));
            cancelButton.setPreferredSize(new Dimension(100, 30));
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);

            // เพิ่ม Action Listeners สำหรับปุ่ม
            saveButton.addActionListener(e -> {
                // บันทึกข้อมูลที่แก้ไข
                tableModel.setValueAt(nameField.getText(), row, 1);
                tableModel.setValueAt(accessBox.getSelectedItem(), row, 2);
                tableModel.setValueAt(statusBox.getSelectedItem(), row, 3);
                String newExpiry = expiryField.getText();
                String encryptedNewExpiry = TimeBasedEncryption.encrypt(newExpiry);
                tableModel.setValueAt(encryptedNewExpiry, row, 4);


                if (mainFrame != null) {
                    mainFrame.logAction("Edited Card", "Card ID: " + cardID);
                }
                editDialog.dispose();
                fireEditingStopped();
            });

            cancelButton.addActionListener(e -> {
                editDialog.dispose();
                fireEditingStopped();
            });

            // เพิ่ม Panel ลงใน Dialog
            editDialog.add(mainPanel, BorderLayout.CENTER);
            editDialog.add(buttonPanel, BorderLayout.SOUTH);

            // แสดง Dialog
            editDialog.setVisible(true);

        } catch (Exception ex) {
            System.out.println("Error in editCard: " + ex.getMessage());
            ex.printStackTrace();
            fireEditingStopped();
        }
    }

    private void updateCardStatus(String status) {
        if (row < 0 || row >= tableModel.getRowCount()) {
            System.out.println("Invalid row index: " + row);
            fireEditingStopped();
            return;
        }

        try {
            String cardID = tableModel.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to " + status.toLowerCase() + " this card?",
                    "Confirm " + status,
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                tableModel.setValueAt(status, row, 3);
                if (mainFrame != null) {
                    mainFrame.logAction(status + " Card", "Card ID: " + cardID);
                }
            }
        } catch (Exception ex) {
            System.out.println("Error in updateCardStatus: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            fireEditingStopped();
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.row = row; // Store row index for action reference
        System.out.println("Editor activated for row: " + row); // Debug output
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return ""; // Return empty string instead of null
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        return true;
    }

    // Override stopCellEditing to ensure proper cleanup
    @Override
    public boolean stopCellEditing() {
        fireEditingStopped();
        return super.stopCellEditing();
    }
}