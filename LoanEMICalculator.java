import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class LoanEMICalculator {
    public static void main(String[] args) {
        double principalAmount = getInput("Enter principal amount.: ");
        int tenure = (int) getInput("Enter tenure (in months): ");
        double interestRate = getInput("Enter interest rate: ");

        double monthlyInterestRate = interestRate / 12 / 100;
        double emi = calculateEMI(principalAmount, monthlyInterestRate, tenure);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        StringBuilder output = new StringBuilder();
        output.append("Year\tInterest\tBalance\tLoan Paid To Date\tPrincipal\t(A)\tTotal Payment (A + B)\t(B)\n");
        output.append("=======================================================================================\n");

        double balance = principalAmount;
        double loanPaidToDate = 0;
        double totalPayment = 0;

        int year = 2023;
        double interest = 0;
        double principal = 0;

        for (int month = 1; month <= tenure; month++) {
            interest = balance * monthlyInterestRate;
            principal = emi - interest;
            balance -= principal;
            loanPaidToDate += principal;
            totalPayment += emi;

            String monthName = getMonthName(month);

            output.append(year).append("\t")
                    .append(decimalFormat.format(interest)).append("\t")
                    .append(decimalFormat.format(balance)).append("\t")
                    .append(decimalFormat.format(loanPaidToDate)).append("\t\t")
                    .append(decimalFormat.format(principal)).append("\t")
                    .append(decimalFormat.format(emi)).append("\t")
                    .append(decimalFormat.format(totalPayment)).append("\t\t")
                    .append(decimalFormat.format(emi - principal)).append("\n");

            if (month % 12 == 0) {
                year++;
                output.append("============================================================================================\n");
            }

            output.append(monthName).append("\n\n");
        }

        saveOutputToFile(output.toString());

        System.out.println("EMI calculation completed. Output saved to file 'loan_output.txt'.");
    }

    private static double getInput(String prompt) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.print(prompt);
        return scanner.nextDouble();
    }

    private static double calculateEMI(double principal, double monthlyInterestRate, int tenure) {
        double numerator = principal * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, tenure);
        double denominator = Math.pow(1 + monthlyInterestRate, tenure) - 1;
        return numerator / denominator;
    }

    private static String getMonthName(int month) {
        String[] monthNames = {
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        };
        return monthNames[(month - 1) % 12];
    }

    private static void saveOutputToFile(String output) {
        try {
            FileWriter writer = new FileWriter("loan_output.txt");
            writer.write(output);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while saving the output to a file");
            e.printStackTrace();
        }
    }
}
