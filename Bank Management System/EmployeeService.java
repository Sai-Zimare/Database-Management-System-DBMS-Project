import java.sql.*;
import java.util.Scanner;

public class EmployeeService {
    Scanner sc = new Scanner(System.in);

    // Add a new customer to the database
    public void addCustomer() {
        System.out.print("Enter Customer Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Date of Birth (yyyy-mm-dd): ");
        String dob = sc.nextLine();

        System.out.print("Enter Gender (Male/Female/Other): ");
        String gender = sc.nextLine();

        System.out.print("Enter Customer Phone: ");
        String phone = sc.nextLine();

        System.out.print("Enter Customer Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Customer Address: ");
        String address = sc.nextLine();

        System.out.print("Enter Nationality (default Indian): ");
        String nationality = sc.nextLine();
        if (nationality.isEmpty()) {
            nationality = "Indian";
        }

        System.out.print("Enter Customer Password: ");
        String password = sc.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO Customer (name, dob, gender, phone, email, address, nationality, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setDate(2, Date.valueOf(dob));
            ps.setString(3, gender);
            ps.setString(4, phone);
            ps.setString(5, email);
            ps.setString(6, address);
            ps.setString(7, nationality);
            ps.setString(8, password);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Customer added successfully!");
            } else {
                System.out.println("Failed to add customer.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding customer: " + e.getMessage());
        }
    }

    // Fetch and display all branch details
    public void showBranchDetails() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT branch_id, branch_name FROM Branch"; // Assuming you have a Branch table
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println("Available Branches:");
            while (rs.next()) {
                int branchId = rs.getInt("branch_id");
                String branchName = rs.getString("branch_name");
                System.out.println("Branch ID: " + branchId + " - Branch Name: " + branchName);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching branches: " + e.getMessage());
        }
    }

    // Add account for a customer with branch selection
    public void addAccountForCustomer() {
        // Show available branches before asking for the branch ID
        showBranchDetails();

        System.out.print("Enter Customer ID: ");
        int customerId = sc.nextInt();
        sc.nextLine(); // consume newline

        System.out.print("Enter Branch ID: ");
        int branchId = sc.nextInt();
        sc.nextLine(); // consume newline

        // Now proceed with the logic for adding an account for the customer
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO Account (customer_id, branch_id) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, customerId);
            ps.setInt(2, branchId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Account created successfully!");
            } else {
                System.out.println("Failed to create account.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding account: " + e.getMessage());
        }
    }

    // View all loans
    public void viewAllLoans() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Loan";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("Loan ID: " + rs.getInt("loan_id"));
                System.out.println("Loan Amount: " + rs.getDouble("amount"));
                System.out.println("Loan Status: " + rs.getString("status"));
                System.out.println("-------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error viewing all loans: " + e.getMessage());
        }
    }

    // Approve a loan
    public void approveLoan() {
        System.out.print("Enter Loan ID to approve: ");
        int loanId = sc.nextInt();
        sc.nextLine(); // consume newline

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE Loan SET status = 'approved' WHERE loan_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, loanId);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Loan approved successfully!");
            } else {
                System.out.println("Loan not found or approval failed.");
            }
        } catch (SQLException e) {
            System.out.println("Error approving loan: " + e.getMessage());
        }
    }

    // Reject a loan
    public void rejectLoan() {
        System.out.print("Enter Loan ID to reject: ");
        int loanId = sc.nextInt();
        sc.nextLine(); // consume newline

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE Loan SET status = 'rejected' WHERE loan_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, loanId);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Loan rejected successfully!");
            } else {
                System.out.println("Loan not found or rejection failed.");
            }
        } catch (SQLException e) {
            System.out.println("Error rejecting loan: " + e.getMessage());
        }
    }

    // View customer details
    public void viewCustomerDetails() {
        System.out.print("Enter Customer ID to view details: ");
        int customerId = sc.nextInt();
        sc.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Customer WHERE customer_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, customerId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Customer ID: " + rs.getInt("customer_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("DOB: " + rs.getDate("dob"));
                System.out.println("Gender: " + rs.getString("gender"));
                System.out.println("Phone: " + rs.getString("phone"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Address: " + rs.getString("address"));
                System.out.println("Nationality: " + rs.getString("nationality"));
                System.out.println("-------------------------------");
            } else {
                System.out.println("No customer found with that ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error viewing customer details: " + e.getMessage());
        }
    }

    // Update customer details
    public void updateCustomerDetails() {
        System.out.print("Enter Customer ID to update: ");
        int customerId = sc.nextInt();
        sc.nextLine();

        System.out.println("Select the detail you want to update:");
        System.out.println("1. Name");
        System.out.println("2. Email");
        System.out.println("3. Phone");
        System.out.println("4. Address");
        System.out.println("5. Password");
        int choice = sc.nextInt();
        sc.nextLine();

        String column = null;
        String newValue;

        switch (choice) {
            case 1:
                column = "name";
                break;
            case 2:
                column = "email";
                break;
            case 3:
                column = "phone";
                break;
            case 4:
                column = "address";
                break;
            case 5:
                column = "password";
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        System.out.print("Enter new value: ");
        newValue = sc.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE Customer SET " + column + " = ? WHERE customer_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newValue);
            ps.setInt(2, customerId);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Customer " + column + " updated successfully!");
            } else {
                System.out.println("Failed to update customer.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating customer: " + e.getMessage());
        }
    }

    // Delete customer and resequence customer_id
    public void deleteCustomer() {
        System.out.print("Enter Customer ID to delete: ");
        int customerId = sc.nextInt();
        sc.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            String deleteSql = "DELETE FROM Customer WHERE customer_id = ?";
            PreparedStatement psDelete = conn.prepareStatement(deleteSql);
            psDelete.setInt(1, customerId);
            int rows = psDelete.executeUpdate();

            if (rows > 0) {
                // Resequence IDs
                String resequence = "SET @count = 0; " +
                        "UPDATE Customer SET customer_id = @count:=@count+1; " +
                        "ALTER TABLE Customer AUTO_INCREMENT = 1;";
                Statement stmt = conn.createStatement();
                for (String sql : resequence.split(";")) {
                    if (!sql.trim().isEmpty()) {
                        stmt.execute(sql.trim());
                    }
                }

                conn.commit();
                System.out.println("Customer deleted and IDs resequenced.");
            } else {
                conn.rollback();
                System.out.println("No such customer found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting customer: " + e.getMessage());
        }
    }

    // Search customer by name or email
    public void searchCustomer() {
        System.out.print("Enter Customer Name or Email to search: ");
        String searchTerm = sc.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Customer WHERE name LIKE ? OR email LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + searchTerm + "%");
            ps.setString(2, "%" + searchTerm + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("Customer ID: " + rs.getInt("customer_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Phone: " + rs.getString("phone"));
                System.out.println("Address: " + rs.getString("address"));
                System.out.println("-------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error searching for customer: " + e.getMessage());
        }
    }

    // Reset customer password
    public void resetCustomerPassword() {
        System.out.print("Enter Customer ID to reset password: ");
        int customerId = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter new password: ");
        String newPassword = sc.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE Customer SET password = ? WHERE customer_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newPassword);
            ps.setInt(2, customerId);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Password reset successfully!");
            } else {
                System.out.println("Customer not found or password not updated.");
            }
        } catch (SQLException e) {
            System.out.println("Error resetting password: " + e.getMessage());
        }
    }
}
