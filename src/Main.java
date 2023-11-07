import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    private static ArrayList<String> itemList = new ArrayList<>();
    private static boolean needsToBeSaved = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char choice;

        do {
            displayMenu();
            choice = SafeInput.getRegExString(scanner, "Enter your choice", "[AaDdVvSsOoCcQq]").toLowerCase().charAt(0);

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
                case 's':
                    saveList(scanner);
                    break;
                case 'o':
                    if (needsToBeSaved) {
                        if (getConfirmation(scanner, "Current list has unsaved changes. Save before opening?")) {
                            saveList(scanner);
                        }
                    }
                    openList(scanner);
                    break;
                case 'c':
                    clearList(scanner);
                    break;
                case 'q':
                    if (needsToBeSaved) {
                        if (getConfirmation(scanner, "Current list has unsaved changes. Save before quitting?")) {
                            saveList(scanner);
                        }
                    }
                    System.out.println("Goodbye!");
                    return;
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
        System.out.println("S - Save the list to disk");
        System.out.println("O - Open a list from disk");
        System.out.println("C - Clear the list");
        System.out.println("Q - Quit");
    }

    private static void addItem(Scanner scanner) {
        System.out.print("\nEnter an item to add: ");
        String item = scanner.nextLine().trim();

        if (!item.isEmpty()) {
            itemList.add(item);
            needsToBeSaved = true;
            System.out.println("Item added to the list.\n");
        } else {
            System.out.println("Item cannot be empty. Please try again.\n");
        }
    }

    private static void deleteItem(Scanner scanner) {
        if (itemList.isEmpty()) {
            System.out.println("The list is empty.\n");
            return;
        }

        viewList();
        int itemNumber = SafeInput.getRangedInt(scanner, "Enter the item number to delete", 1, itemList.size());

        String removedItem = itemList.remove(itemNumber - 1);
        needsToBeSaved = true;
        System.out.println("Item '" + removedItem + "' has been deleted.\n");
    }

    private static void viewList() {
        if (itemList.isEmpty()) {
            System.out.println("The list is empty.\n");
            return;
        }

        System.out.println("\nCurrent List:");
        for (int i = 0; i < itemList.size(); i++) {
            System.out.println((i + 1) + ". " + itemList.get(i));
        }
        System.out.println();
    }

    private static void clearList(Scanner scanner) {
        if (!itemList.isEmpty()) {
            if (getConfirmation(scanner, "Are you sure you want to clear the list? This cannot be undone.")) {
                itemList.clear();
                needsToBeSaved = true;
                System.out.println("The list has been cleared.\n");
            }
        }
    }

    private static void saveList(Scanner scanner) {
        if (itemList.isEmpty()) {
            System.out.println("The list is empty, nothing to save.\n");
            return;
        }

        System.out.print("Enter a filename to save the list: ");
        String filename = scanner.nextLine();

        if (!filename.endsWith(".txt")) {
            filename += ".txt"; // Ensure the filename has the .txt extension
        }

        try {
            Path filePath = Paths.get(filename);
            Files.write(filePath, itemList);
            System.out.println("List has been saved to " + filename + "\n");
            needsToBeSaved = false;
        } catch (IOException e) {
            System.out.println("An error occurred while saving the list.");
            e.printStackTrace();
        }
    }

    private static void openList(Scanner scanner) {
        if (needsToBeSaved) {
            if (getConfirmation(scanner, "Current list has unsaved changes. Save before opening?")) {
                saveList(scanner);
            }
        }

        System.out.print("Enter the filename to open: ");
        String filename = scanner.nextLine();

        if (!filename.endsWith(".txt")) {
            filename += ".txt"; // Ensure the filename has the .txt extension
        }

        try {
            Path filePath = Paths.get(filename);
            List<String> lines = Files.readAllLines(filePath);
            itemList.clear();
            itemList.addAll(lines);
            System.out.println("List has been opened from " + filename + "\n");
            needsToBeSaved = false;
        } catch (IOException e) {
            System.out.println("An error occurred while opening the list.");
            e.printStackTrace();
        }
    }

    private static boolean getConfirmation(Scanner scanner, String prompt) {
        return SafeInput.getYNConfirm(scanner, prompt);
    }
}
