package app;

import dao.Database;
import dao.ItemDao;
import model.Item;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    private static ItemDao itemDao = new ItemDao();
    private static Scanner scanner = new Scanner(System.in);
    private static final int CURRENT_YEAR = LocalDate.now().getYear();

    public static void main(String[] args) {
        Database.initialize();
        System.out.println("====== Lost & Found System ======");
        showMainMenu();
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n====== MAIN MENU ======");
            System.out.println("1. Report Item");
            System.out.println("2. View Items");
            System.out.println("3. Search Items");
            System.out.println("4. Delete Items");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1 -> reportItem();
                case 2 -> viewItems();
                case 3 -> searchItems();
                case 4 -> deleteItems();
                case 5 -> {
                    System.out.println("Dushman Naroda");
                    return;
                }
                default -> System.out.println("Invalid option, try again");
            }
        }
    }

    // report functions
    private static void reportItem() {
        System.out.println("\n===== Report Item =====");
        System.out.println("1. Report Lost Item");
        System.out.println("2. Report Found Item");
        System.out.print("Choose option: ");

        int choice = getIntInput();

        switch (choice) {
            case 1 -> reportLostItem();
            case 2 -> reportFoundItem();
            default -> {
                System.out.println("Invalid option");
                return;
            }
        }
    }

    private static void reportLostItem() {
        System.out.println("\n===== Report Lost Item =====");
        Item item = getItemDetailsFromUser();

        try {
            int id = itemDao.saveLost(item);
            System.out.println("Lost item reported");
        } catch (Exception e) {
            System.out.println("Error reporting lost item: " + e.getMessage());
        }
    }

    private static void reportFoundItem() {
        System.out.println("\n===== Report Found Item =====");
        Item item = getItemDetailsFromUser();

        try {
            int id = itemDao.saveFound(item);
            System.out.println("Found item reported");
        } catch (Exception e) {
            System.out.println("Error reporting found item: " + e.getMessage());
        }
    }

    // view functions
    private static void viewItems() {
        System.out.println("\n====== View Items ======");
        System.out.println("1. View Lost Items");
        System.out.println("2. View Found Items");
        System.out.print("Choose option: ");

        int choice = getIntInput();

        switch (choice) {
            case 1 -> viewAllLostItems();
            case 2 -> viewAllFoundItems();
            default -> System.out.println("Invalid option");
        }
    }

    private static void viewAllLostItems() {
        System.out.println("\n===== All Lost Items =====");
        try {
            List<Item> items = itemDao.getLostAll();
            if (items.isEmpty()) {
                System.out.println("No lost items found.");
            } else {
                displayItems(items, "LOST");
            }
        } catch (Exception e) {
            System.out.println("Error getting lost items: " + e.getMessage());
        }
    }

    private static void viewAllFoundItems() {
        System.out.println("\n===== All Found Items =====");
        try {
            List<Item> items = itemDao.getFoundAll();
            if (items.isEmpty()) {
                System.out.println("No found items foun");
            } else {
                displayItems(items, "FOUND");
            }
        } catch (Exception e) {
            System.out.println("Error getting found items: " + e.getMessage());
        }
    }

    private static void searchItems() {
        System.out.println("\n--- Search Items ---");
        System.out.println("1. Search Lost Items");
        System.out.println("2. Search Found Items");
        System.out.print("Choose  option: ");

        int choice = getIntInput();
        System.out.print("Enter item name to search: ");
        String name = scanner.nextLine();

        try {
            List<Item> items;
            String type;

            switch (choice) {
                case 1 -> {
                    items = itemDao.searchLostByName(name);
                    type = "LOST";
                }
                case 2 -> {
                    items = itemDao.searchFoundByName(name);
                    type = "FOUND";
                }
                default -> {
                    System.out.println("Invalid option");
                    return;
                }
            }

            if (items.isEmpty()) {
                System.out.println("No matching " + (choice == 1 ? "lost" : "found") + " items found.");
            } else {
                System.out.println("===== Search Results (" + type + ") =====");
                displayItems(items, type);
            }
        } catch (Exception e) {
            System.out.println("Error searching items: " + e.getMessage());
        }
    }

    private static void deleteItems() {
        System.out.println("\n===== Delete Items =====");
        System.out.println("1. Delete Lost Item");
        System.out.println("2. Delete Found Item");
        System.out.print("Choose  option: ");

        int choice = getIntInput();

        try {
            switch (choice) {
                case 1 -> {
                    viewAllLostItems();
                    System.out.print("\nEnter ID of lost item to delete: ");
                    int id = getIntInput();
                    itemDao.deleteLost(id);
                    System.out.println("Lost item deleted successfully!");
                }
                case 2 -> {
                    viewAllFoundItems();
                    System.out.print("\nEnter ID of found item to delete: ");
                    int id = getIntInput();
                    itemDao.deleteFound(id);
                    System.out.println("Found item deleted successfully!");
                }
                default -> System.out.println("Invalid option");
            }
        } catch (Exception e) {
            System.out.println("Error deleting item: " + e.getMessage());
        }
    }

    private static Item getItemDetailsFromUser() {
        Item item = new Item();

        System.out.print("Item Name: ");
        item.setName(scanner.nextLine());

        System.out.print("Description: ");
        item.setDescription(scanner.nextLine());

        int[] date = getValidatedDateFromUser();
        item.setYear(date[0]);
        item.setMonth(String.valueOf(date[1]));
        item.setDay(date[2]);

        System.out.print("Location: ");
        item.setLocation(scanner.nextLine());

        System.out.print("Contact Info: ");
        item.setContact(scanner.nextLine());

        return item;
    }

    // date validation functions
    private static int[] getValidatedDateFromUser() {
        int year, month, day;

        while (true) {
            System.out.print("Year (1900-" + (CURRENT_YEAR) + "): ");
            year = getIntInput();
            if (year >= 1900 && year <= CURRENT_YEAR) {
                break;
            }
            System.out.println("Invalid year, enter between 1900 and " + (CURRENT_YEAR));
        }

        while (true) {
            System.out.print("Month (1-12): ");
            month = getIntInput();
            if (month >= 1 && month <= 12) {
                break;
            }
            System.out.println("Invalid month, enter between 1 and 12");
        }

        while (true) {
            System.out.print("Day: ");
            day = getIntInput();
            if (isValidDay(year, month, day)) {
                break;
            }
            int maxDays = getMaxDaysInMonth(year, month);
            System.out.println("Invalid day, enter between 1 and " + maxDays + " for " + getMonthName(month) + " " + year);
        }

        return new int[]{year, month, day};
    }

    private static boolean isValidDay(int year, int month, int day) {
        if (day < 1) return false;

        int maxDays = getMaxDaysInMonth(year, month);
        return day <= maxDays;
    }

    private static int getMaxDaysInMonth(int year, int month) {
        return YearMonth.of(year, month).lengthOfMonth();
    }

    private static String getMonthName(int month) {
        String[] monthNames = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        return monthNames[month - 1];
    }

    private static String formatDate(int day, int month, int year) {
        return String.format("%02d-%02d-%d", day, month, year);
    }

    // utility functions
    private static void displayItems(List<Item> items, String type) {
        if (items.isEmpty()) {
            System.out.println("No items to display.");
            return;
        }

        System.out.println("┌─────┬────────────────────┬────────────────────┬────────────┬────────────────────┬────────────────────┐");
        System.out.println("│ ID  │ Name               │ Description        │ Date       │ Location           │ Contact            │");
        System.out.println("├─────┼────────────────────┼────────────────────┼────────────┼────────────────────┼────────────────────┤");

        for (Item item : items) {
            String formattedDate = formatDate(item.getDay(), Integer.parseInt(item.getMonth()), item.getYear());

            System.out.printf("│ %-3d │ %-18s │ %-18s │ %-10s │ %-18s │ %-18s │%n",
                    item.getId(),
                    truncate(item.getName(), 18),
                    truncate(item.getDescription(), 18),
                    formattedDate,
                    truncate(item.getLocation(), 18),
                    truncate(item.getContact(), 18));
        }
        System.out.println("└─────┴────────────────────┴────────────────────┴────────────┴────────────────────┴────────────────────┘");
        System.out.println("Total " + type + " items: " + items.size());
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
                System.out.print("Enter a valid number: ");
            }
        }
    }
}