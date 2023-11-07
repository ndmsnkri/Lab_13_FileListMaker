import java.util.Scanner;

public class SafeInput {
    // Part A: getNonZeroLenString
    public static String getNonZeroLenString(Scanner pipe, String prompt) {
        String retString = "";
        do {
            System.out.print("\n" + prompt + ": ");
            retString = pipe.nextLine();
        } while (retString.length() == 0);
        return retString;
    }

    // Part B: getInt
    public static int getInt(Scanner pipe, String prompt) {
        int value = 0;
        boolean validInput = false;
        do {
            System.out.print("\n" + prompt + ": ");
            if (pipe.hasNextInt()) {
                value = pipe.nextInt();
                validInput = true;
            } else {
                pipe.next(); // Read and discard the invalid input
                System.out.println("Invalid input. Please enter an integer.");
            }
        } while (!validInput);
        pipe.nextLine(); // Clear the newline character
        return value;
    }

    // Part C: getDouble
    public static double getDouble(Scanner pipe, String prompt) {
        double value = 0.0;
        boolean validInput = false;
        do {
            System.out.print("\n" + prompt + ": ");
            if (pipe.hasNextDouble()) {
                value = pipe.nextDouble();
                validInput = true;
            } else {
                pipe.next(); // Read and discard the invalid input
                System.out.println("Invalid input. Please enter a double.");
            }
        } while (!validInput);
        pipe.nextLine(); // Clear the newline character
        return value;
    }

    // Part D: getRangedInt
    public static int getRangedInt(Scanner pipe, String prompt, int low, int high) {
        int value;
        do {
            value = getInt(pipe, prompt + " [" + low + " - " + high + "]");
        } while (value < low || value > high);
        return value;
    }

    // Part E: getRangedDouble
    public static double getRangedDouble(Scanner pipe, String prompt, double low, double high) {
        double value = 0.0;
        boolean validInput;
        do {
            System.out.print("\n" + prompt + " [" + low + " - " + high + "]: ");
            if (pipe.hasNextDouble()) {
                value = pipe.nextDouble();
                validInput = value >= low && value <= high; // Check if the value is within the specified range
                if (!validInput) {
                    System.out.println("Value out of range. Please enter a value between " + low + " and " + high + ".");
                }
            } else {
                pipe.next(); // Read and discard the invalid input
                System.out.println("Invalid input. Please enter a valid double.");
                validInput = false;
            }
        } while (!validInput);
        pipe.nextLine(); // Clear the newline character
        return value;
    }


    // Part F: getYNConfirm
    public static boolean getYNConfirm(Scanner pipe, String prompt) {
        String input;
        do {
            System.out.print("\n" + prompt + " (Y/N): ");
            input = pipe.nextLine().trim().toUpperCase();
        } while (!input.equals("Y") && !input.equals("N"));
        return input.equals("Y");
    }

    // Part G: getRegExString
    public static String getRegExString(Scanner pipe, String prompt, String regEx) {
        String value;
        do {
            value = getNonZeroLenString(pipe, prompt);
        } while (!value.matches(regEx));
        return value;
    }

    // Part H: prettyHeader
    public static void prettyHeader(String msg) {
        int length = msg.length();
        int padding = (60 - length) / 2;
        System.out.println("*****************************************************************");
        System.out.print("***");
        for (int i = 0; i < padding; i++) {
            System.out.print(" ");
        }
        System.out.print(msg);
        for (int i = 0; i < padding; i++) {
            System.out.print(" ");
        }
        if (length % 2 != 0) {
            System.out.print(" ");
        }
        System.out.println("***");
        System.out.println("*****************************************************************");
    }
}
