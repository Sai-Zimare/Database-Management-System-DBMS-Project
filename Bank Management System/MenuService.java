import java.util.Scanner;

public class MenuService {
    Scanner sc = new Scanner(System.in);

    public void start() {
        LoginService loginService = new LoginService();
        String role = loginService.login();

        if (role != null) {
            loadMenu(role);
        } else {
            System.out.println("Login failed! Please try again.");
        }
    }

    public void loadMenu(String role) {
        switch (role.toLowerCase()) {
            case "customer":
                customerMenu();
                break;
            case "employee":
                employeeMenu();
                break;
            case "admin":
                adminMenu();
                break;
            default:
                System.out.println("Invalid role access!");
        }
    }

    private void customerMenu() {
        CustomerService customerService = new CustomerService();
        AccountService accountService = new AccountService();
        CardService cardService = new CardService();
        LoanService loanService = new LoanService();
        int choice;

        do {
            System.out.println("\n=== Customer Dashboard ===");
            System.out.println("1. View Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Money");
            System.out.println("5. Apply for Loan");
            System.out.println("6. View My Cards");
            System.out.println("7. View My Loans");          // New
            System.out.println("8. Repay Loan");             // New
            System.out.println("9. Check Loan Status");      // New
            System.out.println("10. Logout");
            System.out.print("Choose: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter your customer ID: ");
                    int customerId = sc.nextInt();
                    accountService.viewAccountsByCustomerId(customerId);
                }
                case 2 -> {
                    System.out.print("Enter customer ID: ");
                    int customerId = sc.nextInt();
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = sc.nextDouble();
                    customerService.depositMoney(depositAmount, customerId);
                }
                case 3 -> {
                    System.out.print("Enter customer ID: ");
                    int customerId = sc.nextInt();
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawalAmount = sc.nextDouble();
                    customerService.withdrawMoney(withdrawalAmount, customerId);
                }
                case 4 -> {
                    System.out.print("Enter customer ID: ");
                    int customerId = sc.nextInt();
                    System.out.print("Enter amount to transfer: ");
                    double transferAmount = sc.nextDouble();
                    System.out.print("Enter recipient account number: ");
                    int recipientAccount = sc.nextInt();
                    customerService.transferMoney(transferAmount, customerId, recipientAccount);
                }
                case 5 -> {
                    System.out.print("Enter customer ID: ");
                    int customerId = sc.nextInt();
                    customerService.applyLoan(customerId);
                }
                case 6 -> {
                    System.out.print("Enter your customer ID: ");
                    int customerId = sc.nextInt();
                    cardService.viewCardsByCustomerId(customerId);
                }
                case 7 -> {
                    System.out.print("Enter your customer ID: ");
                    int customerId = sc.nextInt();
                    loanService.viewLoans(customerId);
                }
                case 8 -> {
                    System.out.print("Enter your customer ID: ");
                    int customerId = sc.nextInt();
                    System.out.print("Enter loan ID to repay: ");
                    int loanId = sc.nextInt();
                    System.out.print("Enter amount to repay: ");
                    double repayAmount = sc.nextDouble();
                    loanService.repayLoan(customerId, loanId, repayAmount);
                }
                case 9 -> {
                    System.out.print("Enter your customer ID: ");
                    int customerId = sc.nextInt();
                    System.out.print("Enter loan ID to check status: ");
                    int loanId = sc.nextInt();
                    loanService.checkLoanStatus(customerId, loanId);
                }
                case 10 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 10);
    }

    private void employeeMenu() {
        EmployeeService employeeService = new EmployeeService();
        AccountService accountService = new AccountService();
        CardService cardService = new CardService();
        int choice;

        do {
            System.out.println("\n=== Employee Dashboard ===");
            System.out.println("1. Add Customer");
            System.out.println("2. View Customer Details");
            System.out.println("3. Update Customer Details");
            System.out.println("4. Delete Customer");
            System.out.println("5. Search Customer");
            System.out.println("6. Reset Customer Password");
            System.out.println("7. View All Loans");
            System.out.println("8. Approve Loan");
            System.out.println("9. Reject Loan");
            System.out.println("10. Add Account for Customer");
            System.out.println("11. Issue Card to Customer");
            System.out.println("12. Logout");
            System.out.print("Choose: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> employeeService.addCustomer();
                case 2 -> employeeService.viewCustomerDetails();
                case 3 -> employeeService.updateCustomerDetails();
                case 4 -> employeeService.deleteCustomer();
                case 5 -> employeeService.searchCustomer();
                case 6 -> employeeService.resetCustomerPassword();
                case 7 -> employeeService.viewAllLoans();
                case 8 -> employeeService.approveLoan();
                case 9 -> employeeService.rejectLoan();
                case 10 -> {
                    System.out.print("Enter customer ID: ");
                    int customerId = sc.nextInt();
                    System.out.print("Enter branch ID: ");
                    int branchId = sc.nextInt();
                    System.out.print("Enter account type (Saving/Current/Salary): ");
                    String accountType = sc.next();
                    accountService.addAccount(customerId, branchId, accountType);
                }
                case 11 -> {
                    System.out.print("Enter customer ID: ");
                    int customerId = sc.nextInt();
                    System.out.print("Enter card type (Debit/Credit): ");
                    String cardType = sc.next();
                    cardService.issueCard(customerId, cardType);
                }
                case 12 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 12);
    }

    private void adminMenu() {
        AdminService adminService = new AdminService();
        BankTransactionService bankTransactionService = new BankTransactionService();
        int choice;

        do {
            System.out.println("\n=== Admin Dashboard ===");
            System.out.println("1. Add New Employee");
            System.out.println("2. Delete Employee");
            System.out.println("3. View All Employees");
            System.out.println("4. Search Employee");
            System.out.println("5. Update Employee Details");
            System.out.println("6. Reset Employee Password");
            System.out.println("7. View Transaction Report by Account");
            System.out.println("8. Logout");
            System.out.print("Choose: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> adminService.addEmployee();
                case 2 -> adminService.deleteEmployee();
                case 3 -> adminService.viewAllEmployees();
                case 4 -> adminService.searchEmployee();
                case 5 -> adminService.updateEmployeeDetails();
                case 6 -> adminService.resetEmployeePassword();
                case 7 -> {
                    System.out.print("Enter account ID: ");
                    int accId = sc.nextInt();
                    bankTransactionService.viewTransactions(accId);
                }
                case 8 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 8);
    }
}
