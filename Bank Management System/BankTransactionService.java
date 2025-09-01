import java.sql.*;

public class BankTransactionService {
    public void viewTransactions(int accountId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM banktransaction WHERE account_id = ? ORDER BY transaction_date DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.printf("Transaction ID: %d | Type: %s | Amount: %.2f | Date: %s\n",
                        rs.getInt("transaction_id"),
                        rs.getString("transaction_type"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("transaction_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}