import java.util.Scanner;
import java.sql.*;
import java.time.LocalDate;

public class CustomerService {
    Scanner sc = new Scanner(System.in);

    // View account details
    public void viewAccount(int customerId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Account WHERE customer_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, customerId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Account ID: " + rs.getInt("account_id"));
                System.out.println("Customer ID: " + rs.getInt("customer_id"));
                System.out.println("Account Balance: " + rs.getDouble("balance"));
                System.out.println("Open Date: " + rs.getDate("open_date"));
            } else {
                System.out.println("No account found for this customer.");
            }
        } catch (SQLException e) {
            System.out.println("Error viewing account: " + e.getMessage());
        }
    }

    // Deposit money
    public void depositMoney(double amount, int customerId) {
        System.out.println("Depositing $" + amount + " into your account...");

        try (Connection conn = DBConnection.getConnection()) {
            int accountId = getAccountId(conn, customerId);
            if (accountId == -1) {
                System.out.println("No account found for this customer.");
                return;
            }

            String sql = "UPDATE Account SET balance = balance + ? WHERE account_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, amount);
            ps.setInt(2, accountId);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Deposit successful!");
                recordTransaction(conn, accountId, "Credit", amount);
            } else {
                System.out.println("Deposit failed.");
            }
        } catch (SQLException e) {
            System.out.println("Error depositing money: " + e.getMessage());
        }
    }

    // Withdraw money
    public void withdrawMoney(double amount, int customerId) {
        System.out.println("Withdrawing $" + amount + " from your account...");

        try (Connection conn = DBConnection.getConnection()) {
            int accountId = getAccountId(conn, customerId);
            if (accountId == -1) {
                System.out.println("No account found.");
                return;
            }

            String sql = "SELECT balance FROM Account WHERE account_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getDouble("balance");
                if (currentBalance >= amount) {
                    sql = "UPDATE Account SET balance = balance - ? WHERE account_id = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setDouble(1, amount);
                    ps.setInt(2, accountId);
                    int rowsUpdated = ps.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Withdrawal successful!");
                        recordTransaction(conn, accountId, "Debit", amount);
                    } else {
                        System.out.println("Withdrawal failed.");
                    }
                } else {
                    System.out.println("Insufficient funds.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error withdrawing money: " + e.getMessage());
        }
    }

    // Transfer money
    public void transferMoney(double amount, int customerId, int recipientAccountId) {
        System.out.println("Transferring $" + amount + " to account number " + recipientAccountId);

        try (Connection conn = DBConnection.getConnection()) {
            int senderAccountId = getAccountId(conn, customerId);
            if (senderAccountId == -1) {
                System.out.println("Sender account not found.");
                return;
            }

            // Check if recipient exists
            String sql = "SELECT balance FROM Account WHERE account_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, recipientAccountId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Transfer failed. Recipient not found.");
                return;
            }

            // Check sender balance
            sql = "SELECT balance FROM Account WHERE account_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, senderAccountId);
            rs = ps.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getDouble("balance");
                if (currentBalance >= amount) {
                    // Deduct from sender
                    sql = "UPDATE Account SET balance = balance - ? WHERE account_id = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setDouble(1, amount);
                    ps.setInt(2, senderAccountId);
                    int rowsUpdated = ps.executeUpdate();

                    if (rowsUpdated > 0) {
                        // Credit to recipient
                        sql = "UPDATE Account SET balance = balance + ? WHERE account_id = ?";
                        ps = conn.prepareStatement(sql);
                        ps.setDouble(1, amount);
                        ps.setInt(2, recipientAccountId);
                        rowsUpdated = ps.executeUpdate();

                        if (rowsUpdated > 0) {
                            System.out.println("Transfer successful!");
                            recordTransaction(conn, senderAccountId, "Debit", amount);
                            recordTransaction(conn, recipientAccountId, "Credit", amount);
                        } else {
                            System.out.println("Transfer failed. Recipient account update failed.");
                        }
                    } else {
                        System.out.println("Transfer failed. Sender account update failed.");
                    }
                } else {
                    System.out.println("Insufficient funds.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error transferring money: " + e.getMessage());
        }
    }

    // Apply for loan
    public void applyLoan(int customerId) {
        System.out.print("Enter loan amount: ");
        double loanAmount = sc.nextDouble();
        sc.nextLine(); // Consume newline

        System.out.print("Enter loan type (Home, Car, Personal, Education): ");
        String loanType = sc.nextLine().trim();

        System.out.print("Enter interest rate (e.g., 7.5): ");
        double interestRate = sc.nextDouble();

        System.out.print("Enter loan term (in months): ");
        int termMonths = sc.nextInt();
        sc.nextLine(); // Consume newline

        LocalDate issuedDate = LocalDate.now();
        LocalDate dueDate = issuedDate.plusMonths(termMonths);

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO Loan (customer_id, loan_type, amount, interest_rate, issued_date, due_date, status) " +
                         "VALUES (?, ?, ?, ?, ?, ?, 'Ongoing')";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, customerId);
            ps.setString(2, loanType);
            ps.setDouble(3, loanAmount);
            ps.setDouble(4, interestRate);
            ps.setDate(5, Date.valueOf(issuedDate));
            ps.setDate(6, Date.valueOf(dueDate));

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Loan application submitted successfully!");
            } else {
                System.out.println("Failed to apply for loan.");
            }
        } catch (SQLException e) {
            System.out.println("Error applying for loan: " + e.getMessage());
        }
    }

    // Record transaction in banktransaction table
    private void recordTransaction(Connection conn, int accountId, String transactionType, double amount) {
        try {
            String sql = "INSERT INTO banktransaction (account_id, transaction_type, amount) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, accountId);
            ps.setString(2, transactionType); // Must be 'Credit' or 'Debit'
            ps.setDouble(3, amount);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error recording transaction: " + e.getMessage());
        }
    }

    // Helper to get account ID from customer ID
    private int getAccountId(Connection conn, int customerId) throws SQLException {
        String sql = "SELECT account_id FROM Account WHERE customer_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, customerId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("account_id");
        }
        return -1;
    }
}
