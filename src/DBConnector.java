import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class DBConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/phonebook";
    private static final String USER = "root";
    private static final String PASSWORD = "11111";

    public static Connection getConnection() throws SQLException {
        try {
            // Attempt to establish a connection
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            // Display a user-friendly error message if the connection fails
            showError("Unable to connect to the database. Please check your connection and try again.\nError: " + e.getMessage());
            throw e; // Re-throw the exception to handle it further if needed
        }
    }

    public static void loadContacts(DefaultTableModel tableModel) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM contacts")) {

            tableModel.setRowCount(0); // Clear the table before loading new data

            while (rs.next()) {
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                String address = rs.getString("address");
                tableModel.addRow(new String[]{name, phone, email, address});
            }
        } catch (SQLException e) {
            showError("Error loading contacts: " + e.getMessage());
        }
    }

    public static boolean addContact(String name, String phone, String email, String address) {
        String sql = "INSERT INTO contacts (name, phone, email, address) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, email);
            pstmt.setString(4, address);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            showError("Error adding contact: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateContact(String originalPhone, String name, String newPhone, String email, String address) {
        String sql = "UPDATE contacts SET name=?, phone=?, email=?, address=? WHERE phone=?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, newPhone);
            pstmt.setString(3, email);
            pstmt.setString(4, address);
            pstmt.setString(5, originalPhone);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showError("Error updating contact: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteContact(String phone) {
        String sql = "DELETE FROM contacts WHERE phone=?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, phone);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showError("Error deleting contact: " + e.getMessage());
            return false;
        }
    }

    private static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}