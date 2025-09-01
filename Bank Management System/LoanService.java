import java.sql.*;
import java.util.Scanner;

public class LoanService {
    Scanner sc = new Scanner(System.in);

    // View loan details for a customer
    public void viewLoans(int customerId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Loan WHERE customer_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, customerId);

            ResultSet rs = ps.executeQuery();
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Loan ID: " + rs.getInt("loan_id"));
                System.out.println("Loan Type: " + rs.getString("loan_type"));
                System.out.println("Amount: " + rs.getDouble("amount"));
                System.out.println("Interest Rate: " + rs.getDouble("interest_rate"));
                System.out.println("Issued Date: " + rs.getDate("issued_date"));
                System.out.println("Due Date: " + rs.getDate("due_date"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("-----------------------------------");
            }

            if (!found) {
                System.out.println("No loans found for this customer.");
            }
        } catch (SQLException e) {
            System.out.println("Error viewing loans: " + e.getMessage());
        }
    }

    // Repay loan
    public void repayLoan(int customerId, int loanId, double amount) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Loan WHERE loan_id = ? AND customer_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, loanId);
            ps.setInt(2, customerId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double loanAmount = rs.getDouble("amount");
                if (loanAmount >= amount) {
                    // Update loan amount after repayment
                    sql = "UPDATE Loan SET amount = amount - ? WHERE loan_id = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setDouble(1, amount);
                    ps.setInt(2, loanId);
                    int rowsUpdated = ps.executeUpdate();

                    if (rowsUpdated > 0) {
                        System.out.println("Repayment successful!");
                        // Check if the loan is fully repaid and update status
                        if (loanAmount - amount == 0) {
                            sql = "UPDATE Loan SET status = 'Completed' WHERE loan_id = ?";
                            ps = conn.prepareStatement(sql);
                            ps.setInt(1, loanId);
                            ps.executeUpdate();
                            System.out.println("Loan fully repaid, status updated to 'Completed'.");
                        }
                    } else {
                        System.out.println("Error repaying the loan.");
                    }
                } else {
                    System.out.println("Repayment amount exceeds the loan amount.");
                }
            } else {
                System.out.println("Loan not found for this customer.");
            }
        } catch (SQLException e) {
            System.out.println("Error repaying loan: " + e.getMessage());
        }
    }

    // Check loan status
    public void checkLoanStatus(int customerId, int loanId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT status FROM Loan WHERE loan_id = ? AND customer_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, loanId);
            ps.setInt(2, customerId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Loan Status: " + rs.getString("status"));
            } else {
                System.out.println("Loan not found for this customer.");
            }
        } catch (SQLException e) {
            System.out.println("Error checking loan status: " + e.getMessage());
        }
    }
}
