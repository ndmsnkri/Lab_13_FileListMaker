import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static ArrayList<String> currentList = new ArrayList<>();
    private static boolean needsToBeSaved = false;
    private static String currentFileName = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char choice;

        do {
            displayMenu();
            choice = getChoice(scanner);

            switch (choice) {
                case 'a':
                    addItem(scanner);
                    break;
                case 'd':
                    deleteItem(scanner);
                    break;
                case 'v':
                    viewList();
                    break;
                case 'o':
                    openListFile(scanner);
                    break;
                case 's':
                    saveListToFile(scanner);
                    break;
                case 'c':
                    clearList(scanner);
                    break;
                case 'q':
                    quitProgram(scanner);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (true);
    }

    private static void displayMenu() {
        System.out.println("Menu:");
        System.out.println("A - Add an item to the list");
        System.out.println("D - Delete an item from the list");
        System.out.println("V - View the list");
        System.out.println("O - Open a list file from disk");
        System.out.println("S - Save the current list file to disk");
        System.out.println("C - Clear the list");
        System.out.println("Q - Quit");
    }

    private static char getChoice(Scanner scanner) {
        return SafeInput.getRegExString(scanner, "Enter your choice", "[AaDdVvOoSsCcQq]").toLowerCase().charAt(0);
    }

    private static void addItem(Scanner scanner) {
        System.out.print("\nEnter an item to add: ");
        String item = scanner.nextLine().trim();

        if (!item.isEmpty()) {
            currentList.add(item);
            needsToBeSaved = true;
            System.out.println("Item added to the list.\n");
        } else {
            System.out.println("Item cannot be empty. Please try again.\n");
        }
    }

    private static void deleteItem(Scanner scanner) {
        if (currentList.isEmpty()) {
            System.out.println("The list is empty.\n");
            return;
        }

        viewList();
        int itemNumber = SafeInput.getRangedInt(scanner, "Enter the item number to delete", 1, currentList.size());

        String removedItem = currentList.remove(itemNumber - 1);
        needsToBeSaved = true;
        System.out.println("Item '" + removedItem + "' has been deleted.\n");
    }

    private static void viewList() {
        if (currentList.isEmpty()) {
            System.out.println("The list is empty.\n");
            return;
        }

        System.out.println("\nCurrent List:");
        for (int i = 0; i < currentList.size(); i++) {
            System.out.println((i + 1) + ". " + currentList.get(i));
        }
        System.out.println();
    }

    private static void openListFile(Scanner scanner) {
        if (needsToBeSaved) {
            if (!getConfirmation(scanner, "Save unsaved changes before opening a new file?")) {
                System.out.println("Changes abandoned.\n");
                return;
            } else {
                saveListToFile(scanner);
            }
        }

        System.out.print("Enter the filename to open: ");
        String fileName = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            currentList.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                currentList.add(line);
            }
            needsToBeSaved = false;
            currentFileName = fileName;
            System.out.println("List loaded from " + fileName + "\n");
        } catch (IOException e) {
            System.out.println("Error reading file. " + e.getMessage() + "\n");
        }
    }

    private static void saveListToFile(Scanner scanner) {
        if (currentFileName == null) {
            System.out.print("Enter the filename to save: ");
            currentFileName = scanner.nextLine();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFileName))) {
            for (String item : currentList) {
                writer.write(item);
                writer.newLine();
            }
            needsToBeSaved = false;
            System.out.println("List saved to " + currentFileName + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to file. " + e.getMessage() + "\n");
        }
    }

    private static void clearList(Scanner scanner) {
        if (currentList.isEmpty()) {
            System.out.println("The list is already empty.\n");
            return;
        }

        if (getConfirmation(scanner, "Are you sure you want to clear the list?")) {
            currentList.clear();
            needsToBeSaved = true;
            System.out.println("List cleared.\n");
        } else {
            System.out.println("List not cleared.\n");
        }
    }

    private static void quitProgram(Scanner scanner) {
        if (needsToBeSaved) {
            if (getConfirmation(scanner, "Save unsaved changes before quitting?")) {
                saveListToFile(scanner);
            } else {
                System.out.println("Changes abandoned.\n");
            }
        }

        System.out.println("Goodbye!");
        System.exit(0);
    }

    private static boolean getConfirmation(Scanner scanner, String prompt) {
        return SafeInput.getYNConfirm(scanner, prompt);
    }
}
