import java.sql.*;
import java.util.Scanner;

public class AdminService {
    Scanner sc = new Scanner(System.in);

    // Add a new employee to the database
    public void addEmployee() {
        System.out.print("Enter Employee Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Employee Designation: ");
        String designation = sc.nextLine();

        System.out.print("Enter Employee Salary: ");
        double salary = sc.nextDouble();
        sc.nextLine(); // consume newline

        System.out.print("Enter Employee Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Branch ID: ");
        int branchId = sc.nextInt();
        sc.nextLine(); // consume newline

        System.out.print("Enter Employee Password: ");
        String password = sc.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO employee (name, designation, salary, branch_id, email, password) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, designation);
            ps.setDouble(3, salary);
            ps.setInt(4, branchId);
            ps.setString(5, email);
            ps.setString(6, password);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Employee added successfully!");
            } else {
                System.out.println("Failed to add employee.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding employee: " + e.getMessage());
        }
    }

    // View all employees
    public void viewAllEmployees() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM employee";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("Employee ID: " + rs.getInt("employee_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Designation: " + rs.getString("designation"));
                System.out.println("Salary: " + rs.getDouble("salary"));
                System.out.println("Branch ID: " + rs.getInt("branch_id"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("-------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error viewing all employees: " + e.getMessage());
        }
    }

    // Search employee by name or email
    public void searchEmployee() {
        System.out.print("Enter Employee Name or Email to search: ");
        String searchTerm = sc.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM employee WHERE name LIKE ? OR email LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + searchTerm + "%");
            ps.setString(2, "%" + searchTerm + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("Employee ID: " + rs.getInt("employee_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Designation: " + rs.getString("designation"));
                System.out.println("Salary: " + rs.getDouble("salary"));
                System.out.println("Branch ID: " + rs.getInt("branch_id"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("-------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error searching employee: " + e.getMessage());
        }
    }

    // Update employee details
    public void updateEmployeeDetails() {
        System.out.print("Enter Employee ID to update: ");
        int employeeId = sc.nextInt();
        sc.nextLine(); // consume newline

        System.out.println("Select the detail you want to update:");
        System.out.println("1. Name");
        System.out.println("2. Designation");
        System.out.println("3. Salary");
        System.out.println("4. Email");
        System.out.println("5. Branch ID");
        System.out.println("6. Password");
        System.out.print("Enter choice: ");
        int choice = sc.nextInt();
        sc.nextLine(); // consume newline

        String updateSql = "UPDATE employee SET ";
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement psUpdate;

            switch (choice) {
                case 1:
                    System.out.print("Enter new Name: ");
                    updateSql += "name = ? WHERE employee_id = ?";
                    psUpdate = conn.prepareStatement(updateSql);
                    psUpdate.setString(1, sc.nextLine());
                    psUpdate.setInt(2, employeeId);
                    break;

                case 2:
                    System.out.print("Enter new Designation: ");
                    updateSql += "designation = ? WHERE employee_id = ?";
                    psUpdate = conn.prepareStatement(updateSql);
                    psUpdate.setString(1, sc.nextLine());
                    psUpdate.setInt(2, employeeId);
                    break;

                case 3:
                    System.out.print("Enter new Salary: ");
                    updateSql += "salary = ? WHERE employee_id = ?";
                    psUpdate = conn.prepareStatement(updateSql);
                    psUpdate.setDouble(1, sc.nextDouble());
                    sc.nextLine();
                    psUpdate.setInt(2, employeeId);
                    break;

                case 4:
                    System.out.print("Enter new Email: ");
                    updateSql += "email = ? WHERE employee_id = ?";
                    psUpdate = conn.prepareStatement(updateSql);
                    psUpdate.setString(1, sc.nextLine());
                    psUpdate.setInt(2, employeeId);
                    break;

                case 5:
                    System.out.print("Enter new Branch ID: ");
                    updateSql += "branch_id = ? WHERE employee_id = ?";
                    psUpdate = conn.prepareStatement(updateSql);
                    psUpdate.setInt(1, sc.nextInt());
                    sc.nextLine();
                    psUpdate.setInt(2, employeeId);
                    break;

                case 6:
                    System.out.print("Enter new Password: ");
                    updateSql += "password = ? WHERE employee_id = ?";
                    psUpdate = conn.prepareStatement(updateSql);
                    psUpdate.setString(1, sc.nextLine());
                    psUpdate.setInt(2, employeeId);
                    break;

                default:
                    System.out.println("Invalid choice.");
                    return;
            }

            int rowsUpdated = psUpdate.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Employee details updated successfully!");
            } else {
                System.out.println("Failed to update employee details.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating employee: " + e.getMessage());
        }
    }

    // Delete employee and reorder IDs (for demo only)
    public void deleteEmployee() {
        System.out.print("Enter Employee ID to delete: ");
        int employeeId = sc.nextInt();
        sc.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Transaction begins

            String sqlDelete = "DELETE FROM employee WHERE employee_id = ?";
            PreparedStatement psDelete = conn.prepareStatement(sqlDelete);
            psDelete.setInt(1, employeeId);

            int rows = psDelete.executeUpdate();
            if (rows > 0) {
                System.out.println("Employee deleted successfully!");

                // Reorder IDs (for demo only, not recommended in production)
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("SET @count = 0");
                stmt.executeUpdate("UPDATE employee SET employee_id = @count:=@count+1");
                stmt.executeUpdate("ALTER TABLE employee AUTO_INCREMENT = 1");

                System.out.println("Employee IDs reordered.");
            } else {
                System.out.println("No such employee found!");
            }

            conn.commit(); // Transaction ends
        } catch (SQLException e) {
            System.out.println("Error deleting employee: " + e.getMessage());
        }
    }

    // Reset employee password
    public void resetEmployeePassword() {
        System.out.print("Enter Employee ID to reset password: ");
        int employeeId = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter new password: ");
        String newPassword = sc.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE employee SET password = ? WHERE employee_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newPassword);
            ps.setInt(2, employeeId);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Password reset successfully!");
            } else {
                System.out.println("Employee not found or password not updated.");
            }
        } catch (SQLException e) {
            System.out.println("Error resetting password: " + e.getMessage());
        }
    }
}
