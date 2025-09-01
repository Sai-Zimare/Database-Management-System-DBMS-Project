import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class LoginService {
    Scanner sc = new Scanner(System.in);

    public String login() {
        System.out.print("Enter Username: ");
        String username = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        System.out.println("Select Role: 1. Customer 2. Employee 3. Admin");
        int choice = sc.nextInt();
        sc.nextLine(); // consume newline

        String roleTable = "";
        switch (choice) {
            case 1: roleTable = "customers"; break;
            case 2: roleTable = "employees"; break;
            case 3: roleTable = "admins"; break;
            default:
                System.out.println("Invalid role selected.");
                return null;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM " + roleTable + " WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Login Successful as " + roleTable.toUpperCase());
                return roleTable;
            } else {
                System.out.println("Invalid Username or Password.");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
