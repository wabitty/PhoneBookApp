import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;

public class PhoneBookForm extends JFrame {
    private JPanel contentPane;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField addressField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JTable contactsTable;
    private JPanel ContentPane;
    private DefaultTableModel tableModel;

    public PhoneBookForm() {
        setTitle("Phone Book Application");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Name", "Phone", "Email", "Address"});
        contactsTable.setModel(tableModel);

        addButton.addActionListener(this::addContact);
        updateButton.addActionListener(this::updateContact);
        deleteButton.addActionListener(this::deleteContact);

        loadContacts();

        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        nameField = new JTextField(20);
        phoneField = new JTextField(20);
        emailField = new JTextField(20);
        addressField = new JTextField(20);

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);

        contactsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(contactsTable);
        panel.add(scrollPane);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        panel.add(buttonPanel);

        add(panel);
    }

    private void loadContacts() {
        DBConnector.loadContacts(tableModel);
    }

    private void addContact(ActionEvent e) {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();

        if (validateInput(name, phone, email)) {
            if (DBConnector.addContact(name, phone, email, address)) {
                loadContacts();
                clearFields();
            }
        }
    }

    private void updateContact(ActionEvent e) {
        int selectedRow = contactsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a contact to update.");
            return;
        }

        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();

        if (validateInput(name, phone, email)) {
            String originalPhone = (String) tableModel.getValueAt(selectedRow, 1);
            if (DBConnector.updateContact(originalPhone, name, phone, email, address)) {
                loadContacts();
                clearFields();
            }
        }
    }

    private void deleteContact(ActionEvent e) {
        int selectedRow = contactsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a contact to delete.");
            return;
        }

        String phone = (String) tableModel.getValueAt(selectedRow, 1);
        if (DBConnector.deleteContact(phone)) {
            loadContacts();
        }
    }

    private boolean validateInput(String name, String phone, String email) {
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name, Phone, and Email cannot be empty.");
            return false;
        }
        if (!phone.matches("\\d{10,}")) {
            JOptionPane.showMessageDialog(this, "Invalid phone number. It must contain at least 10 digits.");
            return false;
        }
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Invalid email address.");
            return false;
        }
        return true;
    }

    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new PhoneBookForm());
    }
}