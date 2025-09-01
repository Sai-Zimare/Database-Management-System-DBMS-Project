import java.util.Scanner;

public class MainApplication {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); // Open the scanner

        try {
            System.out.println("=== Welcome to Bank System ===");
            System.out.print("Enter your role (customer/employee/admin): ");
            String role = sc.nextLine().toLowerCase();

            // Ensure we pass the role correctly and load the menu
            MenuService menuService = new MenuService();
            menuService.loadMenu(role);
        } finally {
            // Close the scanner in the finally block to ensure it's done at the end
            sc.close();
        }
    }
}
