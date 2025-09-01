import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class CardService {
    Scanner sc = new Scanner(System.in);

    // Generate a 16-digit card number
    private String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder(16);
        Random rand = new Random();
        for (int i = 0; i < 16; i++) {
            cardNumber.append(rand.nextInt(10));
        }
        return cardNumber.toString();
    }

    // Issue a card to a customer
    public void issueCard(int customerId, String cardType) {
        if (!cardType.equalsIgnoreCase("Debit") && !cardType.equalsIgnoreCase("Credit")) {
            System.out.println("Invalid card type. Please choose either 'Debit' or 'Credit'.");
            return;
        }

        String cardNumber = generateCardNumber();

        System.out.print("Enter expiry date (yyyy-mm-dd): ");
        String expiryDateStr = sc.nextLine();
        Date expiryDate;

        try {
            expiryDate = Date.valueOf(expiryDateStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Use yyyy-mm-dd.");
            return;
        }

        System.out.print("Enter CVV (3 digits): ");
        String cvv = sc.nextLine();

        if (!cvv.matches("\\d{3}")) {
            System.out.println("Invalid CVV. Please enter exactly 3 digits.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO Card (customer_id, card_number, card_type, expiry_date, cvv) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, customerId);
            ps.setString(2, cardNumber);
            ps.setString(3, cardType);
            ps.setDate(4, expiryDate);
            ps.setString(5, cvv);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Card issued successfully with card number: " + cardNumber);
            } else {
                System.out.println("❌ Failed to issue card.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error issuing card: " + e.getMessage());
        }
    }

    // Menu to issue card
    public void issueCardMenu() {
        System.out.print("Enter customer ID: ");
        int customerId = sc.nextInt();
        sc.nextLine(); // consume newline
        System.out.print("Enter card type (Debit/Credit): ");
        String cardType = sc.nextLine();
        issueCard(customerId, cardType);
    }

    // ✅ View all cards for a specific customer
    public void viewCardsByCustomerId(int customerId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Card WHERE customer_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                System.out.println("Card ID: " + rs.getInt("card_id"));
                System.out.println("Card Type: " + rs.getString("card_type"));
                System.out.println("Card Number: " + rs.getString("card_number"));
                System.out.println("Expiry Date: " + rs.getDate("expiry_date"));
                System.out.println("CVV: " + rs.getString("cvv"));
                System.out.println("-----------------------------");
            }

            if (!hasResults) {
                System.out.println("No cards found for customer ID: " + customerId);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error retrieving cards: " + e.getMessage());
        }
    }
}
