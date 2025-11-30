package app;

import dao.Database;
import dao.ItemDao;
import model.Item;

import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    private static ItemDao itemDao = new ItemDao();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Database.initialize();
        System.out.println("=== Lost & Found System ===");
        showMainMenu();
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Report Lost Item");
            System.out.println("2. Report Found Item");
            System.out.println("3. View All Lost Items");
            System.out.println("4. View All Found Items");
            System.out.println("5. Search Lost Items");
            System.out.println("6. Delete Lost Item");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1 -> reportLostItem();
                case 2 -> reportFoundItem();
                case 3 -> viewAllLostItems();
                case 4 -> viewAllFoundItems();
                case 5 -> searchLostItems();
                case 6 -> deleteLostItem();
                case 7 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option! Please try again.");
            }
        }
    }

    private static void reportLostItem() {
        System.out.println("\n--- Report Lost Item ---");
        Item item = getItemDetailsFromUser();

        try {
            itemDao.saveLost(item);
            System.out.println("✅ Lost item reported successfully!");
        } catch (Exception e) {
            System.out.println("❌ Error reporting lost item: " + e.getMessage());
        }
    }

    private static void reportFoundItem() {
        System.out.println("\n--- Report Found Item ---");
        Item item = getItemDetailsFromUser();

        try {
            itemDao.saveFound(item);
            System.out.println("✅ Found item reported successfully!");
        } catch (Exception e) {
            System.out.println("❌ Error reporting found item: " + e.getMessage());
        }
    }

    private static void viewAllLostItems() {
        System.out.println("\n--- All Lost Items ---");
        try {
            List<Item> items = itemDao.listLost();
            if (items.isEmpty()) {
                System.out.println("No lost items found.");
            } else {
                displayItems(items);
            }
        } catch (Exception e) {
            System.out.println("❌ Error retrieving lost items: " + e.getMessage());
        }
    }

    private static void viewAllFoundItems() {
        System.out.println("\n--- All Found Items ---");
        try {
            List<Item> items = itemDao.getFoundAll();
            if (items.isEmpty()) {
                System.out.println("No found items found.");
            } else {
                displayItems(items);
            }
        } catch (Exception e) {
            System.out.println("❌ Error retrieving found items: " + e.getMessage());
        }
    }

    private static void searchLostItems() {
        System.out.print("\nEnter item name to search: ");
        String name = scanner.nextLine();

        try {
            List<Item> items = itemDao.searchLostByName(name);
            if (items.isEmpty()) {
                System.out.println("No matching lost items found.");
            } else {
                System.out.println("--- Search Results ---");
                displayItems(items);
            }
        } catch (Exception e) {
            System.out.println("❌ Error searching lost items: " + e.getMessage());
        }
    }

    private static void deleteLostItem() {
        viewAllLostItems();
        System.out.print("\nEnter ID of lost item to delete: ");
        int id = getIntInput();

        try {
            itemDao.deleteLost(id);
            System.out.println("✅ Lost item deleted successfully!");
        } catch (Exception e) {
            System.out.println("❌ Error deleting lost item: " + e.getMessage());
        }
    }

    private static Item getItemDetailsFromUser() {
        Item item = new Item();

        System.out.print("Item Name: ");
        item.setName(scanner.nextLine());

        System.out.print("Description: ");
        item.setDescription(scanner.nextLine());

        System.out.print("Year: ");
        item.setYear(getIntInput());

        System.out.print("Month: ");
        item.setMonth(scanner.nextLine());

        System.out.print("Day: ");
        item.setDay(getIntInput());

        System.out.print("Location: ");
        item.setLocation(scanner.nextLine());

        System.out.print("Contact Info: ");
        item.setContact(scanner.nextLine());

        return item;
    }

    private static void displayItems(List<Item> items) {
        System.out.println("┌─────┬────────────────────┬────────────────────┬──────────┬────────┬─────┬────────────────────┬────────────────────┐");
        System.out.println("│ ID  │ Name               │ Description        │ Year     │ Month  │ Day │ Location           │ Contact            │");
        System.out.println("├─────┼────────────────────┼────────────────────┼──────────┼────────┼─────┼────────────────────┼────────────────────┤");

        for (Item item : items) {
            System.out.printf("│ %-3d │ %-18s │ %-18s │ %-8d │ %-6s │ %-3d │ %-18s │ %-18s │%n",
                    item.getId(),
                    truncate(item.getName(), 18),
                    truncate(item.getDescription(), 18),
                    item.getYear(),
                    truncate(item.getMonth(), 6),
                    item.getDay(),
                    truncate(item.getLocation(), 18),
                    truncate(item.getContact(), 18));
        }
        System.out.println("└─────┴────────────────────┴────────────────────┴──────────┴────────┴─────┴────────────────────┴────────────────────┘");
    }

    private static String truncate(String text, int length) {
        if (text == null) return "";
        return text.length() > length ? text.substring(0, length - 3) + "..." : text;
    }

    private static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}