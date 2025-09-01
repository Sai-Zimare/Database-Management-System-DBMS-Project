import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Database URL, username, and password
    private static final String URL = "jdbc:mysql://localhost:3306/Bankdb";  // Changed to 'Bankdb'
    private static final String USER = "root"; // MySQL username
    private static final String PASSWORD = "Sai@123"; // MySQL password

    // Static method to get a database connection
    public static Connection getConnection() throws SQLException {
        try {
            // Try explicitly loading the MySQL JDBC driver
            System.out.println("Loading MySQL JDBC Driver...");
            Class.forName("com.mysql.cj.jdbc.Driver");  // Ensures that the driver is loaded
            System.out.println("Driver loaded successfully!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL Driver not found", e);
        }

        // Return the connection to the database
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
