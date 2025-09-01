import java.sql.*;

public class AccountService {

    // Method to view accounts by customer ID
    public void viewAccountsByCustomerId(int customerId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM account WHERE customer_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.printf("Account ID: %d | Type: %s | Balance: %.2f | Open Date: %s\n",
                        rs.getInt("account_id"),
                        rs.getString("account_type"),
                        rs.getDouble("balance"),
                        rs.getDate("open_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to add an account for a customer
    public void addAccount(int customerId, int branchId, String accountType) {
        String sql = "INSERT INTO account (customer_id, branch_id, account_type, balance, open_date) VALUES (?, ?, ?, 0.0, CURRENT_DATE)";
        
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, customerId);
            ps.setInt(2, branchId);
            ps.setString(3, accountType);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Account added successfully for customer ID: " + customerId);
            } else {
                System.out.println("Failed to add account.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
