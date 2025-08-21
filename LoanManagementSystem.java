import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Loan {
    int id;
    String borrowerName;
    double amount;
    double interestRate; // annual %
    int durationMonths;
    String status; // Pending, Approved, Active, Closed
    double balance;
    int monthsPaid;

    public Loan(int id, String borrowerName, double amount, double interestRate, int durationMonths) {
        this.id = id;
        this.borrowerName = borrowerName;
        this.amount = amount;
        this.interestRate = interestRate;
        this.durationMonths = durationMonths;
        this.status = "Pending";
        this.balance = 0;
        this.monthsPaid = 0;
    }
}

public class LoanManagementSystem {
    private static List<Loan> loans = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static int nextId = 1;

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Micro Loan Management System ---");
            System.out.println("1. Apply for Loan");
            System.out.println("2. View All Loans");
            System.out.println("3. Approve Loan");
            System.out.println("4. Process Loan (Disburse)");
            System.out.println("5. Record Loan Repayment");
            System.out.println("6. Close Loan");
            System.out.println("7. Exit");
            System.out.print("Select option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // clear newline

            switch (choice) {
                case 1 -> applyLoan();
                case 2 -> viewLoans();
                case 3 -> approveLoan();
                case 4 -> processLoan();
                case 5 -> recordRepayment();
                case 6 -> closeLoan();
                case 7 -> {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void applyLoan() {
        System.out.print("Enter borrower name: ");
        String name = scanner.nextLine();
        System.out.print("Enter loan amount: ");
        double amount = scanner.nextDouble();
        System.out.print("Enter annual interest rate (%): ");
        double interestRate = scanner.nextDouble();
        System.out.print("Enter duration (months): ");
        int duration = scanner.nextInt();

        Loan loan = new Loan(nextId++, name, amount, interestRate, duration);
        loans.add(loan);
        System.out.println("Loan application submitted successfully. Loan ID: " + loan.id);
    }

    private static void viewLoans() {
        System.out.printf("%-5s %-15s %-10s %-10s %-10s %-12s %-10s %-12s%n",
                "ID", "Name", "Amount", "Interest", "Months", "Status", "Balance", "Months Paid");
        System.out.println("-----------------------------------------------------------------------------------");
        for (Loan loan : loans) {
            System.out.printf("%-5d %-15s %-10.2f %-10.2f %-10d %-12s %-10.2f %-12d%n",
                    loan.id, loan.borrowerName, loan.amount, loan.interestRate,
                    loan.durationMonths, loan.status, loan.balance, loan.monthsPaid);
        }
    }

    private static Loan findLoanById(int id) {
        return loans.stream().filter(l -> l.id == id).findFirst().orElse(null);
    }

    private static void approveLoan() {
        System.out.print("Enter Loan ID to approve: ");
        int id = scanner.nextInt();
        Loan loan = findLoanById(id);
        if (loan == null) {
            System.out.println("Loan ID not found.");
            return;
        }
        loan.status = "Approved";
        System.out.println("Loan approved.");
    }

    private static void processLoan() {
        System.out.print("Enter Loan ID to process: ");
        int id = scanner.nextInt();
        Loan loan = findLoanById(id);
        if (loan == null) {
            System.out.println("Loan ID not found.");
            return;
        }
        if (!loan.status.equals("Approved")) {
            System.out.println("Loan is not approved yet.");
            return;
        }

        // Calculate balance with monthly compound interest
        double monthlyRate = loan.interestRate / 12 / 100;
        loan.balance = loan.amount * Math.pow(1 + monthlyRate, loan.durationMonths);
        loan.status = "Active";

        System.out.printf("Loan processed. Total balance with interest: %.2f%n", loan.balance);
    }

    private static void recordRepayment() {
        System.out.print("Enter Loan ID for repayment: ");
        int id = scanner.nextInt();
        Loan loan = findLoanById(id);
        if (loan == null) {
            System.out.println("Loan ID not found.");
            return;
        }
        if (!loan.status.equals("Active")) {
            System.out.println("Loan is not active.");
            return;
        }

        System.out.print("Enter repayment amount: ");
        double repayment = scanner.nextDouble();
        loan.balance -= repayment;
        loan.monthsPaid += 1;

        if (loan.balance <= 0 || loan.monthsPaid >= loan.durationMonths) {
            loan.balance = 0;
            loan.status = "Closed";
            System.out.println("Loan fully repaid and closed.");
        } else {
            System.out.printf("Repayment recorded. Remaining balance: %.2f, Months paid: %d%n", loan.balance, loan.monthsPaid);
        }
    }

    private static void closeLoan() {
        System.out.print("Enter Loan ID to close: ");
        int id = scanner.nextInt();
        Loan loan = findLoanById(id);
        if (loan == null) {
            System.out.println("Loan ID not found.");
            return;
        }
        loan.status = "Closed";
        loan.balance = 0;
        System.out.println("Loan closed.");
    }
}
